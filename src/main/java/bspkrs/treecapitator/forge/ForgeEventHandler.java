package bspkrs.treecapitator.forge;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import bspkrs.helpers.block.BlockHelper;
import bspkrs.helpers.entity.player.EntityPlayerHelper;
import bspkrs.helpers.world.WorldHelper;
import bspkrs.treecapitator.TreecapitatorMod;
import bspkrs.treecapitator.Treecapitator;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.registry.TreeDefinition;
import bspkrs.treecapitator.registry.TreeRegistry;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.BlockID;
import bspkrs.util.CommonUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandler
{
    private Map<String, Boolean> playerSneakingMap = new HashMap<String, Boolean>();
    
    @SubscribeEvent
    public void onBlockClicked(PlayerInteractEvent event)
    {
        if (event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) && TreecapitatorMod.proxy.isEnabled())
        {
            if (WorldHelper.isAirBlock(event.entityPlayer.worldObj, event.x, event.y, event.z))
            {
                Block block = WorldHelper.getBlock(event.entityPlayer.worldObj, event.x, event.y, event.z);
                int metadata = event.entityPlayer.worldObj.getBlockMetadata(event.x, event.y, event.z);
                
                if (TCSettings.allowDebugOutput && event.entityPlayer.isSneaking() && block != null)
                {
                    TreecapitatorMod.proxy.debugOutputBlockID(BlockHelper.getUniqueID(block), metadata);
                }
                
                playerSneakingMap.put(EntityPlayerHelper.getGameProfile(event.entityPlayer).getName(), event.entityPlayer.isSneaking());
            }
        }
    }
    
    @SubscribeEvent
    public void getPlayerBreakSpeed(BreakSpeed event)
    {
        BlockID blockID = new BlockID(event.block, event.metadata);
        
        if (TreecapitatorMod.proxy.isEnabled() && TreeRegistry.instance().isRegistered(blockID) &&
                Treecapitator.isAxeItemEquipped(event.entityPlayer))
        {
            TreeDefinition treeDef = TreeRegistry.instance().get(blockID);
            if (treeDef != null)
            {
                if (TCSettings.treeHeightDecidesBreakSpeed)
                {
                    MovingObjectPosition thing = CommonUtils.getPlayerLookingSpot(event.entityPlayer, true);
                    if (thing != null && thing.typeOfHit == MovingObjectType.BLOCK)
                    {
                        if ((playerSneakingMap.containsKey(EntityPlayerHelper.getGameProfile(event.entityPlayer))
                                && (playerSneakingMap.get(EntityPlayerHelper.getGameProfile(event.entityPlayer)) == event.entityPlayer.isSneaking()))
                                || !playerSneakingMap.containsKey(EntityPlayerHelper.getGameProfile(event.entityPlayer)))
                        {
                            if (Treecapitator.isBreakingEnabled(event.entityPlayer))
                            {
                                int height = Treecapitator.getTreeHeight(treeDef, event.entityPlayer.worldObj, thing.blockX, thing.blockY, thing.blockZ, event.metadata, event.entityPlayer);
                                if (height > 1)
                                    event.newSpeed = event.originalSpeed / (height * TCSettings.treeHeightModifier);
                            }
                        }
                        else
                        {
                            event.newSpeed = 0.0000000001f;
                        }
                    }
                }
                else
                    event.newSpeed = event.originalSpeed * treeDef.breakSpeedModifier();
            }
            else
                TCLog.severe("TreeRegistry reported block ID %s is a log, but TreeDefinition lookup failed! " +
                        "Please report this to bspkrs (include a copy of this log file and your config).", blockID);
        }
    }
    
    @SubscribeEvent
    public void onBlockHarvested(BreakEvent event)
    {
        if (event.block != null && event.world != null && event.getPlayer() != null)
            TreecapitatorMod.instance.onBlockHarvested(event.world, event.x, event.y, event.z, event.block, event.blockMetadata, event.getPlayer());
    }
}
