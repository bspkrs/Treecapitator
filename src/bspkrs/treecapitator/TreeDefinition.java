package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bspkrs.util.BlockID;
import bspkrs.util.HashCodeUtil;

public class TreeDefinition
{
    private List<BlockID> logBlocks;
    private List<BlockID> leafBlocks;
    private boolean       onlyDestroyUpwards;
    private boolean       requireLeafDecayCheck;
    // max horizontal distance that logs will be broken
    private int           maxLogBreakDist;
    private int           maxLeafIDDist;
    private int           maxLeafBreakDist;
    private int           minLeavesToID;
    
    public TreeDefinition()
    {
        logBlocks = new ArrayList<BlockID>();
        leafBlocks = new ArrayList<BlockID>();
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof TreeDefinition))
            return false;
        
        if (o == this)
            return true;
        
        TreeDefinition td = (TreeDefinition) o;
        return td.logBlocks.equals(logBlocks) && td.leafBlocks.equals(leafBlocks);
        
    }
    
    @Override
    public int hashCode()
    {
        int result = 23;
        result = HashCodeUtil.hash(result, logBlocks);
        result = HashCodeUtil.hash(result, leafBlocks);
        return result;
    }
    
    public boolean isLogBlock(BlockID blockID)
    {
        return logBlocks.contains(blockID);
    }
    
    public boolean isLeafBlock(BlockID blockID)
    {
        return leafBlocks.contains(blockID);
    }
    
    public TreeDefinition addLogID(BlockID blockID)
    {
        if (!isLogBlock(blockID))
            logBlocks.add(blockID);
        
        return this;
    }
    
    public TreeDefinition addLeafID(BlockID blockID)
    {
        if (!isLeafBlock(blockID))
            leafBlocks.add(blockID);
        
        return this;
    }
    
    public TreeDefinition setOnlyDestroyUpwards(boolean onlyDestroyUpwards)
    {
        this.onlyDestroyUpwards = onlyDestroyUpwards;
        return this;
    }
    
    public TreeDefinition setRequireLeafDecayCheck(boolean requireLeafDecayCheck)
    {
        this.requireLeafDecayCheck = requireLeafDecayCheck;
        return this;
    }
    
    public TreeDefinition setMaxLogBreakDist(int maxLogBreakDist)
    {
        this.maxLogBreakDist = maxLogBreakDist;
        return this;
    }
    
    public TreeDefinition setMaxLeafIDDist(int maxLeafIDDist)
    {
        this.maxLeafIDDist = maxLeafIDDist;
        return this;
    }
    
    public TreeDefinition setMaxLeafBreakDist(int maxLeafBreakDist)
    {
        this.maxLeafBreakDist = maxLeafBreakDist;
        return this;
    }
    
    public TreeDefinition setMinLeavesToID(int minLeavesToID)
    {
        this.minLeavesToID = minLeavesToID;
        return this;
    }
    
    /**
     * Retrieves a copy of the list of logs in this TreeDefinition.
     * 
     * @return
     */
    public List<BlockID> getLogIDList()
    {
        List<BlockID> copy = new ArrayList<BlockID>(logBlocks.size());
        Collections.copy(copy, logBlocks);
        return copy;
    }
    
    /**
     * Retrieves a copy of the list of leaves in this TreeDefinition.
     * 
     * @return
     */
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
    
    public boolean requireLeafDecayCheck()
    {
        return requireLeafDecayCheck;
    }
    
    public int maxLogBreakDist()
    {
        return maxLogBreakDist;
    }
    
    public int maxLeafIDDist()
    {
        return maxLeafIDDist;
    }
    
    public int maxLeafBreakDist()
    {
        return maxLeafBreakDist;
    }
    
    public int minLeavesToID()
    {
        return minLeavesToID;
    }
}
