package bspkrs.treecapitator;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TCClientTicker
{
    private Minecraft mcClient;

    public TCClientTicker()
    {
        mcClient = FMLClientHandler.instance().getClient();
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event)
    {
        if (event.phase.equals(Phase.START))
            return;

        if (mcClient != null && mcClient.thePlayer != null)
        {
            if (bspkrsCoreMod.instance.allowUpdateCheck && TreecapitatorMod.versionChecker != null)
                if (!TreecapitatorMod.versionChecker.isCurrentVersion())
                    for (String msg : TreecapitatorMod.versionChecker.getInGameMessage())
                        mcClient.thePlayer.addChatMessage(new ChatComponentText(msg));

            FMLCommonHandler.instance().bus().unregister(this);
        }
    }
}
