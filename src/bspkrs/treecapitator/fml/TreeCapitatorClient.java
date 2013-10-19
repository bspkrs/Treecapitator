package bspkrs.treecapitator.fml;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import bspkrs.fml.util.ForgePacketHelper;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TreeCapitatorClient implements IPacketHandler
{
    public boolean                     serverDetected;
    private static TreeCapitatorClient instance;
    
    public static TreeCapitatorClient instance()
    {
        if (instance == null)
            new TreeCapitatorClient();
        
        return instance;
    }
    
    public TreeCapitatorClient()
    {
        instance = this;
        serverDetected = false;
    }
    
    public void onClientConnect()
    {
        serverDetected = false;
        PacketDispatcher.sendPacketToServer(ForgePacketHelper.createPacket("TreeCapitator", 0, null));
    }
    
    public void setServerDetected()
    {
        serverDetected = true;
    }
    
    public void onServerConfigReceived(NBTTagCompound nbtTCSettings, NBTTagCompound nbtTreeRegistry, NBTTagCompound nbtToolRegistry)
    {
        TreeCapitatorMod.instance.nbtManager().setRemoteNBTs(nbtTCSettings, nbtTreeRegistry, nbtToolRegistry).registerRemoteInstances();
    }
    
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
        int packetType = ForgePacketHelper.readPacketID(data);
        
        if (packetType == 0)
        {
            TreeCapitatorClient.instance().setServerDetected();
        }
        else if (packetType == 1)
        {
            @SuppressWarnings("rawtypes")
            Class[] decodeAs = { NBTTagCompound.class, NBTTagCompound.class, NBTTagCompound.class };
            Object[] packetReadout = ForgePacketHelper.readPacketData(data, decodeAs);
            onServerConfigReceived((NBTTagCompound) packetReadout[0], (NBTTagCompound) packetReadout[1], (NBTTagCompound) packetReadout[2]);
        }
    }
}
