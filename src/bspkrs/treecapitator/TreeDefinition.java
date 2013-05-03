package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
        
        onlyDestroyUpwards = TreeCapitator.onlyDestroyUpwards;
        requireLeafDecayCheck = TreeCapitator.requireLeafDecayCheck;
        maxLogBreakDist = TreeCapitator.maxBreakDistance;
        maxLeafIDDist = TreeCapitator.maxLeafIDDist;
        maxLeafBreakDist = TreeCapitator.maxLeafBreakDist;
        minLeavesToID = TreeCapitator.minLeavesToID;
    }
    
    public TreeDefinition(List<BlockID> logs, List<BlockID> leaves)
    {
        this();
        logBlocks.addAll(logs);
        leafBlocks.addAll(leaves);
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
    
    public TreeDefinition append(TreeDefinition toAdd)
    {
        for (BlockID blockID : toAdd.logBlocks)
            if (!logBlocks.contains(blockID))
                logBlocks.add(blockID);
        
        for (BlockID blockID : toAdd.leafBlocks)
            if (!leafBlocks.contains(blockID))
                leafBlocks.add(blockID);
        
        if (toAdd.onlyDestroyUpwards != TreeCapitator.onlyDestroyUpwards)
            onlyDestroyUpwards = toAdd.onlyDestroyUpwards;
        if (toAdd.requireLeafDecayCheck != TreeCapitator.requireLeafDecayCheck)
            requireLeafDecayCheck = toAdd.requireLeafDecayCheck;
        if (toAdd.maxLogBreakDist != TreeCapitator.maxBreakDistance)
            maxLogBreakDist = toAdd.maxLogBreakDist;
        if (toAdd.maxLeafBreakDist != TreeCapitator.maxLeafBreakDist)
            maxLeafBreakDist = toAdd.maxLeafBreakDist;
        if (toAdd.maxLeafIDDist != TreeCapitator.maxLeafIDDist)
            maxLeafIDDist = toAdd.maxLeafIDDist;
        if (toAdd.minLeavesToID != TreeCapitator.minLeavesToID)
            minLeavesToID = toAdd.minLeavesToID;
        
        return this;
    }
    
    public TreeDefinition readFromNBT(NBTTagCompound treeDefNBT)
    {
        if (treeDefNBT.hasKey(Strings.onlyDestroyUpwards))
            onlyDestroyUpwards = treeDefNBT.getBoolean(Strings.onlyDestroyUpwards);
        if (treeDefNBT.hasKey(Strings.requireLeafDecayCheck))
            requireLeafDecayCheck = treeDefNBT.getBoolean(Strings.requireLeafDecayCheck);
        if (treeDefNBT.hasKey(Strings.maxLogBreakDist))
            maxLogBreakDist = treeDefNBT.getInteger(Strings.maxLogBreakDist);
        if (treeDefNBT.hasKey(Strings.maxLeafBreakDist))
            maxLeafBreakDist = treeDefNBT.getInteger(Strings.maxLeafBreakDist);
        if (treeDefNBT.hasKey(Strings.maxLeafIDDist))
            maxLeafIDDist = treeDefNBT.getInteger(Strings.maxLeafIDDist);
        if (treeDefNBT.hasKey(Strings.minLeavesToID))
            minLeavesToID = treeDefNBT.getInteger(Strings.minLeavesToID);
        
        logBlocks = new ArrayList<BlockID>();
        leafBlocks = new ArrayList<BlockID>();
        
        NBTTagList logList = treeDefNBT.getTagList(Strings.LOGS);
        
        for (int i = 0; i < logList.tagCount(); i++)
        {
            NBTTagCompound log = (NBTTagCompound) logList.tagAt(i);
            logBlocks.add(new BlockID(log.getInteger(Strings.id), log.getInteger(Strings.metadata)));
        }
        
        if (treeDefNBT.hasKey(Strings.LEAVES))
        {
            NBTTagList leafList = treeDefNBT.getTagList(Strings.LEAVES);
            
            for (int i = 0; i < leafList.tagCount(); i++)
            {
                NBTTagCompound leaf = (NBTTagCompound) leafList.tagAt(i);
                leafBlocks.add(new BlockID(leaf.getInteger(Strings.id), leaf.getInteger(Strings.metadata)));
            }
        }
        
        return this;
    }
    
    public void writeToNBT(NBTTagCompound treeDefNBT)
    {
        treeDefNBT.setBoolean(Strings.onlyDestroyUpwards, onlyDestroyUpwards);
        treeDefNBT.setBoolean(Strings.requireLeafDecayCheck, requireLeafDecayCheck);
        treeDefNBT.setInteger(Strings.maxLogBreakDist, maxLogBreakDist);
        treeDefNBT.setInteger(Strings.maxLeafBreakDist, maxLeafBreakDist);
        treeDefNBT.setInteger(Strings.maxLeafIDDist, maxLeafIDDist);
        treeDefNBT.setInteger(Strings.minLeavesToID, minLeavesToID);
        
        NBTTagList logList = new NBTTagList();
        
        for (BlockID logBlock : logBlocks)
        {
            NBTTagCompound log = new NBTTagCompound();
            log.setInteger(Strings.id, logBlock.id);
            log.setInteger(Strings.metadata, logBlock.metadata);
            logList.appendTag(log);
        }
        
        treeDefNBT.setTag(Strings.LOGS, logList);
        
        if (leafBlocks.size() > 0)
        {
            NBTTagList leafList = new NBTTagList();
            
            for (BlockID leafBlock : leafBlocks)
            {
                NBTTagCompound leaf = new NBTTagCompound();
                leaf.setInteger(Strings.id, leafBlock.id);
                leaf.setInteger(Strings.metadata, leafBlock.metadata);
                leafList.appendTag(leaf);
            }
            
            treeDefNBT.setTag(Strings.LEAVES, leafList);
        }
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
    public List<BlockID> getLogList()
    {
        return new ArrayList<BlockID>(logBlocks);
    }
    
    /**
     * Retrieves a copy of the list of leaves in this TreeDefinition.
     * 
     * @return
     */
    public List<BlockID> getLeafList()
    {
        return new ArrayList<BlockID>(leafBlocks);
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
