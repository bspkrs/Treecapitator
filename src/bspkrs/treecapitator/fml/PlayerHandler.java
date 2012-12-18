package bspkrs.treecapitator.fml;

import net.minecraft.block.Block;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import bspkrs.treecapitator.TreeBlockBreaker;
import bspkrs.treecapitator.TreeCapitator;
import bspkrs.util.BlockID;

public class PlayerHandler
{
    @ForgeSubscribe
    public void onBlockClicked(PlayerInteractEvent event)
    {
        if (event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) && TreeCapitatorMod.instance.proxy.isEnabled())
        {
            Block block = Block.blocksList[event.entityPlayer.worldObj.getBlockId(event.x, event.y, event.z)];
            
            if (block != null)
            {
                int metadata = event.entityPlayer.worldObj.getBlockMetadata(event.x, event.y, event.z);
                
                if (TreeCapitator.allowDebugOutput)
                    TreeCapitatorMod.instance.proxy.debugOutputBlockID(block.blockID, metadata);
                
                BlockID blockID = new BlockID(block, metadata);
                
                if (TreeCapitator.isLogBlock(blockID))
                {
                    block.setHardness(TreeBlockBreaker.getBlockHardness(event.entityPlayer));
                }
            }
        }
    }
}
