package bspkrs.treecapitator.fml;

import bspkrs.treecapitator.TreeCapitator;

public class CommonProxy
{
    public void onLoad()
    {}
    
    public boolean isEnabled()
    {
        return true;
    }
    
    public void debugOutputBlockID(int id, int metadata)
    {
        TreeCapitator.debugString("Clicked Block: %s, %s", id, metadata);
    }
    
    public void debugString(String msg)
    {
        TreeCapitator.debugString(msg);
    }
}
