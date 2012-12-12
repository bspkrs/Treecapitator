package bspkrs.treecapitator.fml;

import java.util.EnumSet;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.registry.TickRegistry;

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
}
