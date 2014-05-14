package bspkrs.treecapitator.network;

import bspkrs.network.BSMessageToMessageCodec;
import bspkrs.treecapitator.util.Reference;

public class TCMessageToMessageCodec extends BSMessageToMessageCodec
{
    public TCMessageToMessageCodec()
    {
        addDiscriminator(Reference.PACKET_LOGIN, TCPacketLogin.class);
        addDiscriminator(Reference.PACKET_CONFIG, TCPacketConfig.class);
    }
}
