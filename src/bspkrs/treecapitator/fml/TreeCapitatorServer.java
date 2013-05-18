package bspkrs.treecapitator.fml;

import net.minecraft.nbt.NBTTagCompound;
import bspkrs.fml.util.ForgePacketHelper;
import bspkrs.treecapitator.TCSettings;
import bspkrs.treecapitator.ToolRegistry;
import bspkrs.treecapitator.TreeRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TreeCapitatorServer
{
    public static TreeCapitatorServer instance;
    
    public TreeCapitatorServer()
    {
        instance = this;
    }
    
    public void onPlayerLoggedIn(Player player)
    {
        NBTTagCompound nbtTCSettings = new NBTTagCompound();
        NBTTagCompound nbtTreeRegistry = new NBTTagCompound();
        NBTTagCompound nbtToolRegistry = new NBTTagCompound();
        
        TCSettings.instance().writeToNBT(nbtTCSettings);
        TreeRegistry.instance().writeToNBT(nbtTreeRegistry);
        ToolRegistry.instance().writeToNBT(nbtToolRegistry);
        
        Object[] paquetaUno = { nbtTCSettings, nbtTreeRegistry, nbtToolRegistry };
        PacketDispatcher.sendPacketToPlayer(ForgePacketHelper.createPacket("TreeCapitator", 1, paquetaUno), player);
    }
}
