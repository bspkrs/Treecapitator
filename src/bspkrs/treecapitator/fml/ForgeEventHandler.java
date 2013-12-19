package bspkrs.treecapitator.fml;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TCSettings;
import bspkrs.treecapitator.TreeDefinition;
import bspkrs.treecapitator.TreeRegistry;
import bspkrs.treecapitator.Treecapitator;
import bspkrs.util.BlockID;
import bspkrs.util.CommonUtils;

public class ForgeEventHandler
{
    private Map<String, Boolean> playerSneakingMap = new HashMap<String, Boolean>();
    
    @ForgeSubscribe
    public void onBlockClicked(PlayerInteractEvent event)
    {
        if (event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) && TreeCapitatorMod.proxy.isEnabled())
        {
            Block block = Block.blocksList[event.entityPlayer.worldObj.getBlockId(event.x, event.y, event.z)];
            if (block != null)
            {
                int metadata = event.entityPlayer.worldObj.getBlockMetadata(event.x, event.y, event.z);
                
                if (TCSettings.allowDebugOutput && event.entityPlayer.isSneaking() && block != null)
                {
                    TreeCapitatorMod.proxy.debugOutputBlockID(block.blockID, metadata);
                }
                
                playerSneakingMap.put(event.entityPlayer.getEntityName(), event.entityPlayer.isSneaking());
            }
        }
    }
    
    @ForgeSubscribe
    public void getPlayerBreakSpeed(BreakSpeed event)
    {
        BlockID blockID = new BlockID(event.block.blockID, event.metadata);
        EntityPlayer player = event.entityPlayer;
        
        if (TreeCapitatorMod.proxy.isEnabled() && TreeRegistry.instance().isRegistered(blockID) &&
                Treecapitator.isAxeItemEquipped(player))
        {
            TreeDefinition treeDef = TreeRegistry.instance().get(blockID);
            if (treeDef != null)
            {
                if (TCSettings.treeHeightDecidesBreakSpeed)
                {
                    MovingObjectPosition thing = CommonUtils.getPlayerLookingSpot(player, true);
                    if (thing != null && thing.typeOfHit == EnumMovingObjectType.TILE)
                    {
                        if ((playerSneakingMap.containsKey(player.getEntityName()) && (playerSneakingMap.get(player.getEntityName()) == player.isSneaking()))
                                || !playerSneakingMap.containsKey(player.getEntityName()))
                        {
                            if (Treecapitator.isBreakingEnabled(player))
                            {
                                int height = Treecapitator.getTreeHeight(treeDef, player.worldObj, thing.blockX, thing.blockY, thing.blockZ, event.metadata, player);
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
    
    @ForgeSubscribe
    public void onBlockHarvested(BreakEvent event)
    {
        if (event.block != null && event.world != null && event.getPlayer() != null)
            TreeCapitatorMod.instance.onBlockHarvested(event.world, event.x, event.y, event.z, event.block, event.blockMetadata, event.getPlayer());
    }
}
