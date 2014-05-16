package bspkrs.treecapitator.forge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import bspkrs.helpers.block.BlockHelper;
import bspkrs.helpers.entity.player.EntityPlayerHelper;
import bspkrs.helpers.item.ItemHelper;
import bspkrs.helpers.world.WorldHelper;
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

public class ForgeEventHandler
{
    private Map<String, Boolean>         playerSneakingMap = new HashMap<String, Boolean>();
    private Map<CachedBreakSpeed, Float> breakSpeedCache   = new ConcurrentHashMap<CachedBreakSpeed, Float>(1000);
    
    @SubscribeEvent
    public void onBlockClicked(PlayerInteractEvent event)
    {
        if (TreecapitatorMod.proxy.isEnabled() && event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK))
        {
            if (!WorldHelper.isAirBlock(event.entityPlayer.worldObj, event.x, event.y, event.z))
                playerSneakingMap.put(EntityPlayerHelper.getGameProfile(event.entityPlayer).getName(), event.entityPlayer.isSneaking());
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
                boolean swappedSneak = !((playerSneakingMap.containsKey(EntityPlayerHelper.getGameProfile(event.entityPlayer).getName())
                        && (playerSneakingMap.get(EntityPlayerHelper.getGameProfile(event.entityPlayer).getName()) == event.entityPlayer.isSneaking()))
                        || !playerSneakingMap.containsKey(EntityPlayerHelper.getGameProfile(event.entityPlayer).getName()));
                
                CachedBreakSpeed c = new CachedBreakSpeed(event, swappedSneak);
                if (!this.breakSpeedCache.containsKey(c))
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
                    
                    this.breakSpeedCache.put(c, event.newSpeed);
                }
                else
                    event.newSpeed = this.breakSpeedCache.get(c);
            }
        }
    }
    
    @SubscribeEvent
    public void onBlockHarvested(BreakEvent event)
    {
        if (event.block != null && event.world != null && event.getPlayer() != null)
        {
            List<CachedBreakSpeed> toRemove = new ArrayList<CachedBreakSpeed>();
            for (CachedBreakSpeed bs : this.breakSpeedCache.keySet())
                if (bs.entityPlayer.getGameProfile().getName().equals(event.getPlayer().getGameProfile().getName()))
                    toRemove.add(bs);
            
            for (CachedBreakSpeed bs : toRemove)
                this.breakSpeedCache.remove(bs);
            
            if (playerSneakingMap.containsKey(event.getPlayer().getGameProfile().getName()))
                playerSneakingMap.remove(event.getPlayer().getGameProfile().getName());
            
            if (TreecapitatorMod.proxy.isEnabled() && !event.world.isRemote)
            {
                ModulusBlockID blockID = new ModulusBlockID(event.block, event.blockMetadata, 4);
                
                if (((TCSettings.allowAutoTreeDetection && TreeRegistry.canAutoDetect(event.world, event.block, event.x, event.y, event.z)) || TreeRegistry.instance().isRegistered(blockID))
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
            
            if (ModConfigRegistry.instance().isChanged())
                ModConfigRegistry.instance().writeChangesToConfig(TCConfigHandler.instance().getConfig());
        }
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
            
            return bs.entityPlayer.equals(this.entityPlayer) && (oItem != null ? (thisItem != null ? thisItem.isItemEqual(oItem) : false) : thisItem == null)
                    && BlockHelper.getUniqueID(bs.block).equals(BlockHelper.getUniqueID(this.block))
                    && bs.isSneaking == this.isSneaking && bs.swappedSneak == this.swappedSneak
                    && bs.metadata == this.metadata && bs.originalSpeed == this.originalSpeed && bs.x == this.x && bs.y == this.y && bs.z == this.z;
        }
        
        @Override
        public int hashCode()
        {
            ItemStack thisItem = this.entityPlayer.getCurrentEquippedItem();
            HashFunction hf = Hashing.md5();
            Hasher h = hf.newHasher()
                    .putString(EntityPlayerHelper.getGameProfile(this.entityPlayer).getName(), Charsets.UTF_8)
                    .putString(BlockHelper.getUniqueID(this.block), Charsets.UTF_8)
                    .putBoolean(this.isSneaking)
                    .putBoolean(this.swappedSneak)
                    .putInt(this.metadata)
                    .putFloat(this.originalSpeed)
                    .putInt(x + z << 8 + y << 16);
            
            if (thisItem != null)
                h.putString(ItemHelper.getUniqueID(thisItem.getItem()), Charsets.UTF_8)
                        .putInt(thisItem.getItemDamage());
            
            return h.hash().hashCode();
        }
    }
}
