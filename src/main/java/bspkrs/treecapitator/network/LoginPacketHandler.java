package bspkrs.treecapitator.network;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import bspkrs.treecapitator.TreecapitatorMod;
import bspkrs.treecapitator.util.TCConst;

@ChannelHandler.Sharable
public class LoginPacketHandler extends SimpleChannelInboundHandler<TCPacketLogin>
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TCPacketLogin msg) throws Exception
    {
        if (msg.protocolVersion == TCConst.PROTOCOL_VERSION)
        {
            TreecapitatorMod.proxy.setServerDetected();
        }
    }
}
