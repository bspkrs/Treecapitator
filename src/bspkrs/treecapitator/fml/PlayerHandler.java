package bspkrs.treecapitator.fml;

import net.minecraft.src.Block;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import bspkrs.treecapitator.TreeBlockBreaker;
import bspkrs.treecapitator.TreeCapitator;

public class PlayerHandler
{
    @ForgeSubscribe
    public void onBlockClicked(PlayerInteractEvent event)
    {
        if (event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK))
        {
            int blockID = event.entityPlayer.worldObj.getBlockId(event.x, event.y, event.z);
            if (blockID > 0)
            {
                Block block = Block.blocksList[blockID];
                if (block != null && TreeCapitator.logClasses.contains(block.getClass()))
                    block.setHardness(TreeBlockBreaker.getBlockHardness(event.entityPlayer));
            }
        }
    }
}
