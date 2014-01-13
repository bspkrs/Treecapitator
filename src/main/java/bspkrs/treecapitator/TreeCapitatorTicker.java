package bspkrs.treecapitator;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.helpers.entity.player.EntityPlayerHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TreeCapitatorTicker
{
    private Minecraft mcClient;
    
    public TreeCapitatorTicker()
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
                        EntityPlayerHelper.addChatMessage(mcClient.thePlayer, new ChatComponentText(msg));
            
            FMLCommonHandler.instance().bus().unregister(this);
        }
    }
}
