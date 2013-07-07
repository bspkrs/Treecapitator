package bspkrs.treecapitator.fml;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TCSettings;
import bspkrs.treecapitator.TreeCapitator;
import bspkrs.treecapitator.TreeDefinition;
import bspkrs.treecapitator.TreeRegistry;
import bspkrs.util.BlockID;
import bspkrs.util.CommonUtils;

import java.util.HashMap;
import java.util.Map;

public class PlayerHandler
{
    private Map<String, Boolean> playerSneakingMapping = new HashMap<String, Boolean>();
    
    @ForgeSubscribe
    public void onBlockClicked(PlayerInteractEvent event)
    {
        if (event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) && TreeCapitatorMod.proxy.isEnabled())
        {
            if (TCSettings.allowDebugOutput && event.entityPlayer.isSneaking())
            {
                Block block = Block.blocksList[event.entityPlayer.worldObj.getBlockId(event.x, event.y, event.z)];
                
                if (block != null)
                {
                    int metadata = event.entityPlayer.worldObj.getBlockMetadata(event.x, event.y, event.z);
                    
                    TreeCapitatorMod.proxy.debugOutputBlockID(block.blockID, metadata);
                }
            }
            
            playerSneakingMapping.put(event.entityPlayer.getEntityName(), event.entityPlayer.isSneaking());
        }
    }
    
    @ForgeSubscribe
    public void getPlayerBreakSpeed(BreakSpeed event)
    {
        BlockID blockID = new BlockID(event.block.blockID, event.metadata);
        EntityPlayer player = event.entityPlayer;
        
        if (TreeCapitatorMod.proxy.isEnabled() && TreeRegistry.instance().isRegistered(blockID) &&
                TreeCapitator.isAxeItemEquipped(player))
        {
            TreeDefinition treeDef = TreeRegistry.instance().get(blockID);
            if (treeDef != null)
            {
                if (TCSettings.treeHeightDecidesBreakSpeed)
                {
                    MovingObjectPosition thing = CommonUtils.getPlayerLookingSpot(player, true);
                    if (thing != null && thing.typeOfHit == EnumMovingObjectType.TILE)
                    {
                        if ((playerSneakingMapping.containsKey(player.getEntityName()) && (playerSneakingMapping.get(player.getEntityName()) == player.isSneaking())) || !playerSneakingMapping.containsKey(player.getEntityName()))
                        {
                            if (TreeCapitator.isBreakingEnabled(player)) {
                                int height = TreeCapitator.getTreeHeight(treeDef, player.worldObj, thing.blockX, thing.blockY, thing.blockZ, event.metadata, player);
                                if (height > 1)
                                    event.newSpeed = event.originalSpeed / (height * 2);
                            }
                        }
                        else
                            event.newSpeed = 0.001f;
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
}
