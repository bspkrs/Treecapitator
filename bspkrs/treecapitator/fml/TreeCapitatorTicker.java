package bspkrs.treecapitator.fml;

import java.util.EnumSet;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)

public class TreeCapitatorTicker implements ITickHandler 
{
	private EnumSet<TickType> tickTypes = EnumSet.noneOf(TickType.class);

	public TreeCapitatorTicker(EnumSet<TickType> tickTypes) {
		this.tickTypes = tickTypes;
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

	private void tick(EnumSet<TickType> tickTypes, boolean isStart) {
		for (TickType tickType : tickTypes) 
		{
			if (!TreeCapitatorMod.onTick(tickType, isStart)) 
			{
				this.tickTypes.remove(tickType);
				this.tickTypes.removeAll(tickType.partnerTicks());
			}
		}
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
