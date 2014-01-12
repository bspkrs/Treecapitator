package bspkrs.treecapitator.fml;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class NetworkHandler
{
    @SubscribeEvent
    public void clientLoggedIn(PlayerLoggedInEvent event)
    {
        TreeCapitatorClient.instance().onClientConnect();
    }
}
