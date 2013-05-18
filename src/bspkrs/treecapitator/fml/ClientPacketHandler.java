package bspkrs.treecapitator.fml;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.nbt.NBTTagCompound;
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
            @SuppressWarnings("rawtypes")
            Class[] decodeAs = { NBTTagCompound.class, NBTTagCompound.class, NBTTagCompound.class };
            Object[] packetReadout = ForgePacketHelper.readPacketData(data, decodeAs);
            TreeCapitatorClient.instance.onServerConfigReceived((NBTTagCompound) packetReadout[0], (NBTTagCompound) packetReadout[1],
                    (NBTTagCompound) packetReadout[2]);
        }
    }
}
