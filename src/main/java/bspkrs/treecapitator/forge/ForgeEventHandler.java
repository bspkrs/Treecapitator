package bspkrs.treecapitator.forge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameData;
import bspkrs.treecapitator.Treecapitator;
import bspkrs.treecapitator.TreecapitatorMod;
import bspkrs.treecapitator.config.TCConfigHandler;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.registry.ModConfigRegistry;
import bspkrs.treecapitator.registry.TreeDefinition;
import bspkrs.treecapitator.registry.TreeRegistry;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.ModulusBlockID;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class ForgeEventHandler
{
    private final Map<String, Boolean>         playerSneakingMap = new ConcurrentHashMap<String, Boolean>(64);
    private final Map<CachedBreakSpeed, Float> breakSpeedCache   = new ConcurrentHashMap<CachedBreakSpeed, Float>(64);

    @SubscribeEvent
    public void onBlockClicked(PlayerInteractEvent event)
    {
        if (TreecapitatorMod.proxy.isEnabled() && !TCSettings.sneakAction.equalsIgnoreCase("none") && event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK))
        {
            if (!event.entityPlayer.worldObj.isAirBlock(event.pos))
                playerSneakingMap.put(event.entityPlayer.getGameProfile().getName(), event.entityPlayer.isSneaking());
        }
    }

    @SubscribeEvent
    public void getPlayerBreakSpeed(BreakSpeed event)
    {
        ModulusBlockID blockID = new ModulusBlockID(event.state, 4);
        BlockPos pos = event.pos;

        if (TreecapitatorMod.proxy.isEnabled() && (TreeRegistry.instance().isRegistered(blockID)
                || (TCSettings.allowAutoTreeDetection && TreeRegistry.canAutoDetect(event.entityPlayer.worldObj, event.state.getBlock(), pos)))
                && Treecapitator.isBreakingPossible(event.entityPlayer, event.pos, false))
        {
            TreeDefinition treeDef;
            if (TCSettings.allowAutoTreeDetection)
                treeDef = TreeRegistry.autoDetectTree(event.entityPlayer.worldObj, blockID, pos, TCSettings.allowDebugLogging);
            else
                treeDef = TreeRegistry.instance().get(blockID);

            if (treeDef != null)
            {
                Boolean isSneaking = playerSneakingMap.get(event.entityPlayer.getGameProfile().getName());
                boolean swappedSneak = !(((isSneaking != null) && (isSneaking.booleanValue() == event.entityPlayer.isSneaking())) || (isSneaking == null));

                CachedBreakSpeed cachedBreakSpeed = new CachedBreakSpeed(event, swappedSneak);
                Float newBreakSpeed = breakSpeedCache.get(cachedBreakSpeed);

                if (newBreakSpeed == null)
                {
                    if (!swappedSneak)
                    {
                        if (TCSettings.treeHeightDecidesBreakSpeed)
                        {
                            if (Treecapitator.isBreakingEnabled(event.entityPlayer))
                            {
                                int height = Treecapitator.getTreeHeight(treeDef, event.entityPlayer.worldObj, pos, event.entityPlayer);
                                if (height > 1)
                                    event.newSpeed = event.originalSpeed / (height * TCSettings.treeHeightModifier);
                            }
                        }
                        else if (Treecapitator.isBreakingEnabled(event.entityPlayer))
                            event.newSpeed = event.originalSpeed * treeDef.breakSpeedModifier();
                    }
                    else
                        event.newSpeed = 0.0f;

                    breakSpeedCache.put(cachedBreakSpeed, event.newSpeed);
                }
                else
                    event.newSpeed = newBreakSpeed;
            }
        }
    }

    @SubscribeEvent
    public void onBlockHarvested(BreakEvent event)
    {
        if ((event.state != null) && (event.world != null) && (event.getPlayer() != null))
        {
            if (TreecapitatorMod.proxy.isEnabled() && !event.world.isRemote)
            {
                ModulusBlockID blockID = new ModulusBlockID(event.state, 4);

                if ((TreeRegistry.instance().isRegistered(blockID) || (TCSettings.allowAutoTreeDetection
                        && TreeRegistry.canAutoDetect(event.world, event.state.getBlock(), event.pos)))
                        && Treecapitator.isBreakingPossible(event.getPlayer(), event.pos, TCSettings.allowDebugLogging))
                {
                    BlockPos pos = event.pos;
                    if (TreeRegistry.instance().trackTreeChopEventAt(pos))
                    {
                        TCLog.debug("BlockID " + blockID + " is a log.");

                        TreeDefinition treeDef;
                        if (TCSettings.allowAutoTreeDetection)
                            treeDef = TreeRegistry.autoDetectTree(event.world, blockID, pos, TCSettings.allowDebugLogging);
                        else
                            treeDef = TreeRegistry.instance().get(blockID);

                        if (treeDef != null)
                            (new Treecapitator(event.getPlayer(), treeDef)).onBlockHarvested(event.world, pos);

                        TreeRegistry.instance().endTreeChopEventAt(pos);
                    }
                    else
                        TCLog.debug("Previous chopping event detected for block @%s", pos.toString());
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
        for (CachedBreakSpeed bs : breakSpeedCache.keySet())
            if (bs.entityPlayer.getGameProfile().getName().equals(player.getGameProfile().getName()))
                toRemove.add(bs);

        for (CachedBreakSpeed bs : toRemove)
            breakSpeedCache.remove(bs);

        if (playerSneakingMap.containsKey(player.getGameProfile().getName()))
            playerSneakingMap.remove(player.getGameProfile().getName());
    }

    private class CachedBreakSpeed extends BreakSpeed
    {
        private final boolean isSneaking;
        private final boolean swappedSneak;

        public CachedBreakSpeed(BreakSpeed event, boolean swappedSneak)
        {
            super(event.entityPlayer, event.state, event.originalSpeed, event.pos);
            isSneaking = event.entityPlayer.isSneaking();
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
            ItemStack thisItem = entityPlayer.getCurrentEquippedItem();

            return bs.entityPlayer.getGameProfile().getName().equals(entityPlayer.getGameProfile().getName())
                    && ((oItem != null) && (oItem.getItem() != null) ? ((thisItem != null) && (thisItem.getItem() != null)
                            ? GameData.getItemRegistry().getNameForObject(thisItem.getItem()).equals(GameData.getItemRegistry().getNameForObject(oItem.getItem())) : false)
                            : (thisItem == null) || (thisItem.getItem() == null))
                    && GameData.getBlockRegistry().getNameForObject(bs.state.getBlock()).equals(GameData.getBlockRegistry().getNameForObject(state.getBlock()))
                    && (bs.isSneaking == isSneaking) && (bs.swappedSneak == swappedSneak)
                    && (bs.state.getBlock().getMetaFromState(bs.state) == state.getBlock().getMetaFromState(state))
                    && (bs.originalSpeed == originalSpeed) && (bs.pos.equals(pos));
        }

        @Override
        public int hashCode()
        {
            ItemStack thisItem = entityPlayer.getCurrentEquippedItem();
            HashFunction hf = Hashing.md5();
            Hasher h = hf.newHasher()
                    .putString(entityPlayer.getGameProfile().getName(), Charsets.UTF_8)
                    .putString(GameData.getBlockRegistry().getNameForObject(state.getBlock()).toString(), Charsets.UTF_8)
                    .putBoolean(isSneaking)
                    .putBoolean(swappedSneak)
                    .putInt(state.getBlock().getMetaFromState(state))
                    .putFloat(originalSpeed)
                    .putInt(pos.hashCode());

            if ((thisItem != null) && (thisItem.getItem() != null))
                h.putString(GameData.getItemRegistry().getNameForObject(thisItem.getItem()).toString(), Charsets.UTF_8)
                        .putInt(thisItem.getMetadata());

            return h.hash().hashCode();
        }
    }
}
