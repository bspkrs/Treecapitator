package bspkrs.treecapitator.fml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bspkrs.util.BlockID;

public class TreeDefinition
{
    private List<BlockID> logBlocks;
    private List<BlockID> leafBlocks;
    private boolean       onlyDestroyUpwards;
    private boolean       requireLeafDecayCheck;
    private int           maxLogBreakDist;
    private int           maxLeafIDDist;
    private int           maxLeafBreakDist;
    private int           minLeavesToID;
    
    public TreeDefinition()
    {
        logBlocks = new ArrayList<BlockID>();
        leafBlocks = new ArrayList<BlockID>();
    }
    
    public boolean isTreeBlock(BlockID blockID)
    {
        return logBlocks.contains(blockID);
    }
    
    public boolean isLeafBlock(BlockID blockID)
    {
        return leafBlocks.contains(blockID);
    }
    
    public TreeDefinition addLogID(BlockID blockID)
    {
        logBlocks.add(blockID);
        return this;
    }
    
    public TreeDefinition addLeafID(BlockID blockID)
    {
        leafBlocks.add(blockID);
        return this;
    }
    
    public List<BlockID> getLogIDList()
    {
        List<BlockID> copy = new ArrayList<BlockID>(logBlocks.size());
        Collections.copy(copy, logBlocks);
        return copy;
    }
    
    public List<BlockID> getLeafIDList()
    {
        List<BlockID> copy = new ArrayList<BlockID>(leafBlocks.size());
        Collections.copy(copy, leafBlocks);
        return copy;
    }
    
    public boolean onlyDestroyUpwards()
    {
        return onlyDestroyUpwards;
    }
    
    public TreeDefinition setOnlyDestroyUpwards(boolean onlyDestroyUpwards)
    {
        this.onlyDestroyUpwards = onlyDestroyUpwards;
        return this;
    }
    
    public boolean requireLeafDecayCheck()
    {
        return requireLeafDecayCheck;
    }
    
    public TreeDefinition setRequireLeafDecayCheck(boolean requireLeafDecayCheck)
    {
        this.requireLeafDecayCheck = requireLeafDecayCheck;
        return this;
    }
    
    public int maxLogBreakDist()
    {
        return maxLogBreakDist;
    }
    
    public TreeDefinition setMaxLogBreakDist(int maxLogBreakDist)
    {
        this.maxLogBreakDist = maxLogBreakDist;
        return this;
    }
    
    public int maxLeafIDDist()
    {
        return maxLeafIDDist;
    }
    
    public TreeDefinition setMaxLeafIDDist(int maxLeafIDDist)
    {
        this.maxLeafIDDist = maxLeafIDDist;
        return this;
    }
    
    public int maxLeafBreakDist()
    {
        return maxLeafBreakDist;
    }
    
    public TreeDefinition setMaxLeafBreakDist(int maxLeafBreakDist)
    {
        this.maxLeafBreakDist = maxLeafBreakDist;
        return this;
    }
    
    public int minLeavesToID()
    {
        return minLeavesToID;
    }
    
    public TreeDefinition setMinLeavesToID(int minLeavesToID)
    {
        this.minLeavesToID = minLeavesToID;
        return this;
    }
}
