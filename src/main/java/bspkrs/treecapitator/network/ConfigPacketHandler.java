package bspkrs.treecapitator.network;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import bspkrs.treecapitator.TreecapitatorMod;

@ChannelHandler.Sharable
public class ConfigPacketHandler extends SimpleChannelInboundHandler<TCPacketConfig>
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TCPacketConfig msg) throws Exception
    {
        if (TreecapitatorMod.proxy.isEnabled())
        {
            TreecapitatorMod.instance.nbtManager().setRemoteNBTs(msg.settings, msg.treeRegistry, msg.toolRegistry).registerRemoteInstances();
        }
    }
}
