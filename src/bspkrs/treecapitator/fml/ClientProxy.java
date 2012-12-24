package bspkrs.treecapitator.fml;

import java.util.EnumSet;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void onLoad()
    {
        new TreeCapitatorClient();
        TickRegistry.registerTickHandler(new TreeCapitatorTicker(EnumSet.of(TickType.CLIENT)), Side.CLIENT);
    }
    
    @Override
    public boolean isEnabled()
    {
        return TreeCapitatorClient.instance.serverDetected;
    }
    
    @Override
    public void debugOutputBlockID(int id, int metadata)
    {
        super.debugOutputBlockID(id, metadata);
        if (FMLClientHandler.instance().getClient().thePlayer.isSneaking())
            FMLClientHandler.instance().getClient().thePlayer.addChatMessage("Block Clicked: " + id + ", " + metadata);
    }
}
