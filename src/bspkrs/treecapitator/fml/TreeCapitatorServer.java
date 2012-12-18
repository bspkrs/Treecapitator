package bspkrs.treecapitator.fml;

import bspkrs.fml.util.ForgePacketHelper;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TreeCapitator;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TreeCapitatorServer
{
    public static TreeCapitatorServer instance;
    
    public TreeCapitatorServer()
    {
        instance = this;
        if (TreeCapitator.useOnlineTreeConfig)
            if (TreeCapitator.remoteTreeConfig.trim().length() > 0)
                TreeCapitator.localTreeConfig = TreeCapitator.remoteTreeConfig.trim();
            else
                TCLog.warning("Online Block Config string is empty, try running with allowGetOnlineBlockConfig=true");
    }
    
    public void onPlayerLoggedIn(Player player)
    {
        Object[] toSend = { TreeCapitator.localTreeConfig, TreeCapitator.axeIDList, TreeCapitator.logHardnessNormal, TreeCapitator.logHardnessModified };
        PacketDispatcher.sendPacketToPlayer(ForgePacketHelper.createPacket("TreeCapitator", 1, toSend), player);
    }
}
