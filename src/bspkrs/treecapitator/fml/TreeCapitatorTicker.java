package bspkrs.treecapitator.fml;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import bspkrs.fml.util.bspkrsCoreProxy;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TreeCapitatorTicker implements ITickHandler
{
    private Minecraft         mcClient;
    
    private EnumSet<TickType> tickTypes = EnumSet.noneOf(TickType.class);
    
    public TreeCapitatorTicker(EnumSet<TickType> tickTypes)
    {
        this.tickTypes = tickTypes;
        this.mcClient = FMLClientHandler.instance().getClient();
    }
    
    @Override
    public void tickStart(EnumSet<TickType> tickTypes, Object... tickData)
    {
        tick(tickTypes, true);
    }
    
    @Override
    public void tickEnd(EnumSet<TickType> tickTypes, Object... tickData)
    {
        tick(tickTypes, false);
    }
    
    private void tick(EnumSet<TickType> tickTypes, boolean isStart)
    {
        for (TickType tickType : tickTypes)
        {
            if (!onTick(tickType, isStart))
            {
                this.tickTypes.remove(tickType);
                this.tickTypes.removeAll(tickType.partnerTicks());
            }
        }
    }
    
    public boolean onTick(TickType tick, boolean isStart)
    {
        if (isStart)
        {
            return true;
        }
        
        if (mcClient != null && mcClient.thePlayer != null)
        {
            if (bspkrsCoreProxy.instance.allowUpdateCheck && TreeCapitatorMod.versionChecker != null)
                if (!TreeCapitatorMod.versionChecker.isCurrentVersionBySubStringAsFloatNewer(TreeCapitatorMod.instance.metadata.version.length() - 1, TreeCapitatorMod.instance.metadata.version.length()))
                    for (String msg : TreeCapitatorMod.versionChecker.getInGameMessage())
                        mcClient.thePlayer.addChatMessage(msg);
            
            if (mcClient.isSingleplayer() && TreeCapitatorMod.isItemInWorldManagerReplaced((EntityPlayerMP) mcClient.getIntegratedServer().worldServerForDimension(mcClient.thePlayer.dimension).getPlayerEntityByName(mcClient.thePlayer.username)))
                mcClient.thePlayer.addChatMessage("Warning: The ItemInWorldManager object for your player entity has been replaced (most likely by another mod). TreeCapitator will probably not work.");
            
            return false;
        }
        
        return true;
    }
    
    @Override
    public EnumSet<TickType> ticks()
    {
        return this.tickTypes;
    }
    
    @Override
    public String getLabel()
    {
        return "TreeCapitatorTicker";
    }
    
}
