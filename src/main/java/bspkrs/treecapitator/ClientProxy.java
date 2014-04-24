package bspkrs.treecapitator;

import net.minecraft.util.ChatComponentText;
import bspkrs.helpers.entity.player.EntityPlayerHelper;
import bspkrs.treecapitator.config.TCSettings;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    public boolean serverDetected = false;
    
    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        new TCClientTicker();
    }
    
    @Override
    public boolean isEnabled()
    {
        return serverDetected && TCSettings.enabled;
    }
    
    @Override
    public void setServerDetected()
    {
        serverDetected = true;
    }
    
    @Override
    public void debugOutputBlockID(String id, int metadata)
    {
        if (FMLClientHandler.instance().getClient().thePlayer.isSneaking())
        {
            super.debugOutputBlockID(id, metadata);
            EntityPlayerHelper.addChatMessage(FMLClientHandler.instance().getClient().thePlayer, new ChatComponentText("Block Clicked: " + id + ", " + metadata));
        }
    }
}
