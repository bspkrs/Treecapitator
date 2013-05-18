package bspkrs.treecapitator.fml;

import net.minecraft.nbt.NBTTagCompound;
import bspkrs.fml.util.ForgePacketHelper;
import bspkrs.treecapitator.InstanceHandler;
import bspkrs.treecapitator.TCLog;
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
        
        if (!TreeCapitatorMod.isCoreModLoaded && FMLClientHandler.instance().getClient().isSingleplayer())
        {
            String s = "TreeCapitator CoreMod code has not been injected. Ensure the downloaded .jar file is in the coremods folder and not mods.";
            FMLClientHandler.instance().getClient().thePlayer.addChatMessage(s);
            TCLog.severe(s);
            serverDetected = false;
        }
        
        if (serverDetected)
            FMLClientHandler.instance().getClient().thePlayer.addChatMessage("TreeCapitator client-side features enabled.");
    }
    
    public void onServerConfigReceived(NBTTagCompound nbtTCSettings, NBTTagCompound nbtTreeRegistry, NBTTagCompound nbtToolRegistry)
    {
        InstanceHandler ih = new InstanceHandler(nbtTCSettings, nbtTreeRegistry, nbtToolRegistry);
        
    }
}
