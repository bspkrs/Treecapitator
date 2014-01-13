package bspkrs.treecapitator.network;

import net.minecraft.network.PacketBuffer;
import bspkrs.network.BSPacket;
import bspkrs.treecapitator.util.TCConst;

public class TCPacketLogin implements BSPacket
{
    public byte protocolVersion = TCConst.PROTOCOL_VERSION;
    
    @Override
    public void readBytes(PacketBuffer bytes)
    {
        protocolVersion = bytes.readByte();
    }
    
    @Override
    public void writeBytes(PacketBuffer bytes)
    {
        bytes.writeByte(protocolVersion);
    }
}
