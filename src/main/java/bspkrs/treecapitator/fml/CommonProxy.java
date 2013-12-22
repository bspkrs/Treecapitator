package bspkrs.treecapitator.fml;

import bspkrs.treecapitator.TCLog;

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
        TCLog.debug("Block Clicked: %s, %s", id, metadata);
    }
}
