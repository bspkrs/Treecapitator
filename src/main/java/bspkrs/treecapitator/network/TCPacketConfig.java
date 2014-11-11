package bspkrs.treecapitator.network;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import bspkrs.network.BSPacket;
import bspkrs.treecapitator.TreecapitatorMod;

public class TCPacketConfig implements BSPacket
{
    protected NBTTagCompound settings;
    protected NBTTagCompound treeRegistry;
    protected NBTTagCompound toolRegistry;

    public TCPacketConfig()
    {
        this(TreecapitatorMod.instance.nbtManager().getPacketArray());
    }

    public TCPacketConfig(NBTTagCompound settings, NBTTagCompound treeRegistry, NBTTagCompound toolRegistry)
    {
        this(new NBTTagCompound[] { settings, treeRegistry, toolRegistry });
    }

    public TCPacketConfig(NBTTagCompound[] packetArray)
    {
        assert packetArray.length == 3 : "Length of packetArray must be 3!";
        settings = packetArray[0];
        treeRegistry = packetArray[1];
        toolRegistry = packetArray[2];
    }

    @Override
    public void readBytes(PacketBuffer buffer)
    {
        try
        {
            settings = buffer.readNBTTagCompoundFromBuffer();
            treeRegistry = buffer.readNBTTagCompoundFromBuffer();
            toolRegistry = buffer.readNBTTagCompoundFromBuffer();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void writeBytes(PacketBuffer buffer)
    {
        try
        {
            buffer.writeNBTTagCompoundToBuffer(settings);
            buffer.writeNBTTagCompoundToBuffer(treeRegistry);
            buffer.writeNBTTagCompoundToBuffer(toolRegistry);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
}
