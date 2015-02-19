package bspkrs.treecapitator;

import java.util.EnumMap;

import net.minecraftforge.common.config.Configuration;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.network.ConfigPacketHandler;
import bspkrs.treecapitator.network.LoginPacketHandler;
import bspkrs.treecapitator.network.TCMessageToMessageCodec;
import bspkrs.treecapitator.network.TCPacketConfig;
import bspkrs.treecapitator.network.TCPacketLogin;
import bspkrs.treecapitator.util.TCLog;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy
{
    protected static EnumMap<Side, FMLEmbeddedChannel> networkChannel;

    public void init(FMLInitializationEvent event)
    {
        networkChannel = NetworkRegistry.INSTANCE.newChannel(TreecapitatorMod.metadata.modId, new TCMessageToMessageCodec(),
                new LoginPacketHandler(), new ConfigPacketHandler());

        FMLCommonHandler.instance().bus().register(this);
    }

    public boolean isEnabled()
    {
        return TCSettings.enabled;
    }

    public void setServerDetected()
    {}

    public void debugOutputBlockID(String id, int metadata)
    {
        TCLog.debug("Block Clicked: %s, %s", id, metadata);
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent e)
    {
        FMLEmbeddedChannel channel = networkChannel.get(Side.SERVER);

        channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(
                FMLOutboundHandler.OutboundTarget.PLAYER);
        channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(e.player);

        channel.writeOutbound(new TCPacketLogin());

        channel.writeOutbound(new TCPacketConfig(TreecapitatorMod.instance.nbtManager().getPacketArray()));
    }

    public void setCategoryConfigEntryClass(Configuration config, String Category)
    {}
}
