package bspkrs.treecapitator.fml;

import bspkrs.fml.util.ForgePacketHelper;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TCSettings;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TreeCapitatorClient
{
    public static TreeCapitatorClient instance;
    public boolean                    serverDetected;
    
    public TreeCapitatorClient()
    {
        instance = this;
        serverDetected = false;
    }
    
    public void onClientConnect()
    {
        serverDetected = false;
        PacketDispatcher.sendPacketToServer(ForgePacketHelper.createPacket("TreeCapitator", 0, null));
    }
    
    public void setServerDetected()
    {
        serverDetected = true;
        
        if (!TreeCapitatorMod.instance.isCoreModLoaded && FMLClientHandler.instance().getClient().isSingleplayer())
        {
            String s = "TreeCapitator CoreMod code has not been injected. Ensure the downloaded .jar file is in the coremods folder and not mods.";
            FMLClientHandler.instance().getClient().thePlayer.addChatMessage(s);
            TCLog.severe(s);
            serverDetected = false;
        }
        
        if (serverDetected)
            FMLClientHandler.instance().getClient().thePlayer.addChatMessage("TreeCapitator client-side features enabled.");
    }
    
    public void onServerConfigReceived(String blockIDList, String axeIDList, float logHardnessNormal, float logHardnessModified)
    {
        TCLog.debug("Server sent block ID list: %s", blockIDList);
        
        if (!FMLClientHandler.instance().getClient().isSingleplayer())
            ;
        
        TCSettings.axeIDList = axeIDList;
        TCSettings.logHardnessNormal = logHardnessNormal;
        TCSettings.logHardnessModified = logHardnessModified;
    }
}
