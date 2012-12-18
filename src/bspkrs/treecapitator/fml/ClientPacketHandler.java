package bspkrs.treecapitator.fml;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import bspkrs.fml.util.ForgePacketHelper;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ClientPacketHandler implements IPacketHandler
{
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
        int packetType = ForgePacketHelper.readPacketID(data);
        
        if (packetType == 0)
        {
            TreeCapitatorClient.instance.setServerDetected();
        }
        else if (packetType == 1)
        {
            Class[] decodeAs = { String.class, String.class, Float.class, Float.class };
            Object[] packetReadout = ForgePacketHelper.readPacketData(data, decodeAs);
            TreeCapitatorClient.instance.onServerConfigReceived((String) packetReadout[0], (String) packetReadout[1], (Float) packetReadout[2],
                    (Float) packetReadout[3]);
        }
    }
}
