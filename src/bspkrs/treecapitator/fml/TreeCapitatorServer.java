package bspkrs.treecapitator.fml;

import bspkrs.fml.util.ForgePacketHelper;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TreeCapitatorServer
{
    public static TreeCapitatorServer instance;
    
    public TreeCapitatorServer()
    {
        instance = this;
    }
    
    public void onPlayerLoggedIn(Player player)
    {
        Object[] paquetaUno = {};
        PacketDispatcher.sendPacketToPlayer(ForgePacketHelper.createPacket("TreeCapitator", 1, paquetaUno), player);
    }
}
