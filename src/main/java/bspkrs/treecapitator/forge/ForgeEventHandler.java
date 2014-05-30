package bspkrs.treecapitator.forge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import bspkrs.treecapitator.Treecapitator;
import bspkrs.treecapitator.TreecapitatorMod;
import bspkrs.treecapitator.config.TCConfigHandler;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.registry.ModConfigRegistry;
import bspkrs.treecapitator.registry.TreeDefinition;
import bspkrs.treecapitator.registry.TreeRegistry;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.Coord;
import bspkrs.util.ModulusBlockID;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameData;

public class ForgeEventHandler
{
    private Map<String, Boolean>         playerSneakingMap = new ConcurrentHashMap<String, Boolean>(64);
    private Map<CachedBreakSpeed, Float> breakSpeedCache   = new ConcurrentHashMap<CachedBreakSpeed, Float>(64);
    
    @SubscribeEvent
    public void onBlockClicked(PlayerInteractEvent event)
    {
        if (TreecapitatorMod.proxy.isEnabled() && !TCSettings.sneakAction.equalsIgnoreCase("none") && event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK))
        {
            if (!event.entityPlayer.worldObj.isAirBlock(event.x, event.y, event.z))
                playerSneakingMap.put(event.entityPlayer.getGameProfile().getName(), event.entityPlayer.isSneaking());
        }
    }
    
    @SubscribeEvent
    public void getPlayerBreakSpeed(BreakSpeed event)
    {
        ModulusBlockID blockID = new ModulusBlockID(event.block, event.metadata, 4);
        
        if (TreecapitatorMod.proxy.isEnabled() && (TreeRegistry.instance().isRegistered(blockID)
                || (TCSettings.allowAutoTreeDetection && TreeRegistry.canAutoDetect(event.entityPlayer.worldObj, event.block, event.x, event.y, event.z)))
                && Treecapitator.isBreakingPossible(event.entityPlayer, event.block, event.metadata, false))
        {
            
            TreeDefinition treeDef;
            if (TCSettings.allowAutoTreeDetection)
                treeDef = TreeRegistry.autoDetectTree(event.entityPlayer.worldObj, blockID, new Coord(event.x, event.y, event.z), TCSettings.allowDebugLogging);
            else
                treeDef = TreeRegistry.instance().get(blockID);
            
            if (treeDef != null)
            {
                Boolean isSneaking = playerSneakingMap.get(event.entityPlayer.getGameProfile().getName());
                boolean swappedSneak = !((isSneaking != null && (isSneaking.booleanValue() == event.entityPlayer.isSneaking())) || isSneaking == null);
                
                CachedBreakSpeed cachedBreakSpeed = new CachedBreakSpeed(event, swappedSneak);
                Float newBreakSpeed = this.breakSpeedCache.get(cachedBreakSpeed);
                
                if (newBreakSpeed == null)
                {
                    if (!swappedSneak)
                    {
                        if (TCSettings.treeHeightDecidesBreakSpeed)
                        {
                            if (Treecapitator.isBreakingEnabled(event.entityPlayer))
                            {
                                int height = Treecapitator.getTreeHeight(treeDef, event.entityPlayer.worldObj, event.x, event.y, event.z, event.metadata, event.entityPlayer);
                                if (height > 1)
                                    event.newSpeed = event.originalSpeed / (height * TCSettings.treeHeightModifier);
                            }
                        }
                        else if (Treecapitator.isBreakingEnabled(event.entityPlayer))
                            event.newSpeed = event.originalSpeed * treeDef.breakSpeedModifier();
                    }
                    else
                        event.newSpeed = 0.0f;
                    
                    this.breakSpeedCache.put(cachedBreakSpeed, event.newSpeed);
                }
                else
                    event.newSpeed = newBreakSpeed;
            }
        }
    }
    
    @SubscribeEvent
    public void onBlockHarvested(BreakEvent event)
    {
        if (event.block != null && event.world != null && event.getPlayer() != null)
        {
            if (TreecapitatorMod.proxy.isEnabled() && !event.world.isRemote)
            {
                ModulusBlockID blockID = new ModulusBlockID(event.block, event.blockMetadata, 4);
                
                if ((TreeRegistry.instance().isRegistered(blockID) || (TCSettings.allowAutoTreeDetection && TreeRegistry.canAutoDetect(event.world, event.block, event.x, event.y, event.z)))
                        && Treecapitator.isBreakingPossible(event.getPlayer(), event.block, event.blockMetadata, TCSettings.allowDebugLogging))
                {
                    Coord blockPos = new Coord(event.x, event.y, event.z);
                    if (TreeRegistry.instance().trackTreeChopEventAt(blockPos))
                    {
                        TCLog.debug("BlockID " + blockID + " is a log.");
                        
                        TreeDefinition treeDef;
                        if (TCSettings.allowAutoTreeDetection)
                            treeDef = TreeRegistry.autoDetectTree(event.world, blockID, blockPos, TCSettings.allowDebugLogging);
                        else
                            treeDef = TreeRegistry.instance().get(blockID);
                        
                        if (treeDef != null)
                            (new Treecapitator(event.getPlayer(), treeDef)).onBlockHarvested(event.world, event.x, event.y, event.z, event.blockMetadata);
                        
                        TreeRegistry.instance().endTreeChopEventAt(blockPos);
                    }
                    else
                        TCLog.debug("Previous chopping event detected for block @%s", blockPos.toString());
                }
            }
            
            cleanUpCaches(event.getPlayer());
            
            if (ModConfigRegistry.instance().isChanged())
                ModConfigRegistry.instance().writeChangesToConfig(TCConfigHandler.instance().getConfig());
        }
    }
    
    public void cleanUpCaches(EntityPlayer player)
    {
        List<CachedBreakSpeed> toRemove = new ArrayList<CachedBreakSpeed>();
        for (CachedBreakSpeed bs : this.breakSpeedCache.keySet())
            if (bs.entityPlayer.getGameProfile().getName().equals(player.getGameProfile().getName()))
                toRemove.add(bs);
        
        for (CachedBreakSpeed bs : toRemove)
            this.breakSpeedCache.remove(bs);
        
        if (playerSneakingMap.containsKey(player.getGameProfile().getName()))
            playerSneakingMap.remove(player.getGameProfile().getName());
    }
    
    private class CachedBreakSpeed extends BreakSpeed
    {
        private boolean isSneaking;
        private boolean swappedSneak;
        
        public CachedBreakSpeed(BreakSpeed event, boolean swappedSneak)
        {
            super(event.entityPlayer, event.block, event.metadata, event.originalSpeed, event.x, event.y, event.z);
            this.isSneaking = event.entityPlayer.isSneaking();
            this.swappedSneak = swappedSneak;
        }
        
        @Override
        public boolean equals(Object o)
        {
            if (this == o)
                return true;
            
            if (!(o instanceof CachedBreakSpeed))
                return false;
            
            CachedBreakSpeed bs = (CachedBreakSpeed) o;
            
            ItemStack oItem = bs.entityPlayer.getCurrentEquippedItem();
            ItemStack thisItem = this.entityPlayer.getCurrentEquippedItem();
            
            return bs.entityPlayer.getGameProfile().getName().equals(this.entityPlayer.getGameProfile().getName())
                    && (oItem != null && oItem.getItem() != null ? (thisItem != null && thisItem.getItem() != null
                            ? GameData.itemRegistry.getNameForObject(thisItem).equals(GameData.itemRegistry.getNameForObject(oItem)) : false) : thisItem == null || thisItem.getItem() == null)
                    && GameData.blockRegistry.getNameForObject(bs.block).equals(GameData.blockRegistry.getNameForObject(this.block))
                    && bs.isSneaking == this.isSneaking && bs.swappedSneak == this.swappedSneak
                    && bs.metadata == this.metadata && bs.originalSpeed == this.originalSpeed && bs.x == this.x && bs.y == this.y && bs.z == this.z;
        }
        
        @Override
        public int hashCode()
        {
            ItemStack thisItem = this.entityPlayer.getCurrentEquippedItem();
            HashFunction hf = Hashing.md5();
            Hasher h = hf.newHasher()
                    .putString(this.entityPlayer.getGameProfile().getName(), Charsets.UTF_8)
                    .putString(GameData.blockRegistry.getNameForObject(this.block), Charsets.UTF_8)
                    .putBoolean(this.isSneaking)
                    .putBoolean(this.swappedSneak)
                    .putInt(this.metadata)
                    .putFloat(this.originalSpeed)
                    .putInt(x + z << 8 + y << 16);
            
            if (thisItem != null && thisItem.getItem() != null)
                h.putString(GameData.itemRegistry.getNameForObject(thisItem.getItem()), Charsets.UTF_8)
                        .putInt(thisItem.getItemDamage());
            
            return h.hash().hashCode();
        }
    }
}
