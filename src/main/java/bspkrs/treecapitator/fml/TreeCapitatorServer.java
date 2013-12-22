package bspkrs.treecapitator.fml;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import bspkrs.fml.util.ForgePacketHelper;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TreeCapitatorServer implements IPacketHandler
{
    private static TreeCapitatorServer instance;
    
    public static TreeCapitatorServer instance()
    {
        if (instance == null)
            new TreeCapitatorServer();
        
        return instance;
    }
    
    public TreeCapitatorServer()
    {
        instance = this;
    }
    
    public void onPlayerLoggedIn(Player player)
    {
        PacketDispatcher.sendPacketToPlayer(ForgePacketHelper.createPacket("TreeCapitator", 1, TreeCapitatorMod.instance.nbtManager().getPacketArray()), player);
    }
    
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
        int packetType = ForgePacketHelper.readPacketID(data);
        
        if (packetType == 0)
        {
            PacketDispatcher.sendPacketToPlayer(ForgePacketHelper.createPacket("TreeCapitator", 0, null), player);
            onPlayerLoggedIn(player);
        }
    }
}
