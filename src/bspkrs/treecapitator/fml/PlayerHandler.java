package bspkrs.treecapitator.fml;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerEvent;
import bspkrs.treecapitator.TreeBlockBreaker;
import bspkrs.treecapitator.TreeCapitator;

public class PlayerHandler
{
    @ForgeSubscribe
    public void handleBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        if (TreeCapitator.logClasses.contains(event.block.getClass()))
            event.newSpeed = TreeBlockBreaker.getBlockHardness(event.entityPlayer);
    }
}
