package bspkrs.treecapitator.fml;

import bspkrs.fml.util.ForgePacketHelper;
import bspkrs.treecapitator.TreeCapitator;
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
        FMLClientHandler.instance().getClient().thePlayer.addChatMessage("TreeCapitator server detected. Client-side features enabled.");
    }
    
    public void onServerConfigReceived(String logBlockList, String axeIDList, float logHardnessNormal, float logHardnessModified)
    {
        TreeCapitator.logBlockList = logBlockList;
        TreeCapitator.parseBlockIDList(TreeCapitator.logBlockList, TreeCapitator.logIDList);
        TreeCapitator.axeIDList = axeIDList;
        TreeCapitator.logHardnessNormal = logHardnessNormal;
        TreeCapitator.logHardnessModified = logHardnessModified;
    }
}
