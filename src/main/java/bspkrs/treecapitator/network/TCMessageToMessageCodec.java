package bspkrs.treecapitator.network;

import bspkrs.network.BSMessageToMessageCodec;
import bspkrs.treecapitator.util.TCConst;

public class TCMessageToMessageCodec extends BSMessageToMessageCodec
{
    public TCMessageToMessageCodec()
    {
        addDiscriminator(TCConst.PACKET_LOGIN, TCPacketLogin.class);
        addDiscriminator(TCConst.PACKET_CONFIG, TCPacketConfig.class);
    }
}
