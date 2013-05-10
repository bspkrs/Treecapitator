package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import bspkrs.util.BlockID;
import bspkrs.util.HashCodeUtil;
import bspkrs.util.ListUtils;

public class TreeDefinition
{
    protected List<BlockID> logBlocks;
    protected List<BlockID> leafBlocks;
    protected boolean       onlyDestroyUpwards;
    protected boolean       requireLeafDecayCheck;
    // max horizontal distance that logs will be broken
    protected int           maxHorLogBreakDist;
    protected int           maxVerLogBreakDist;
    protected int           maxLeafIDDist;
    protected int           maxLeafBreakDist;
    protected int           minLeavesToID;
    protected float         breakSpeedModifier;
    
    public TreeDefinition()
    {
        logBlocks = new ArrayList<BlockID>();
        leafBlocks = new ArrayList<BlockID>();
        
        onlyDestroyUpwards = TCSettings.onlyDestroyUpwards;
        requireLeafDecayCheck = TCSettings.requireLeafDecayCheck;
        maxHorLogBreakDist = TCSettings.maxHorLogBreakDist;
        maxVerLogBreakDist = TCSettings.maxVerLogBreakDist;
        maxLeafIDDist = TCSettings.maxLeafIDDist;
        maxLeafBreakDist = TCSettings.maxLeafBreakDist;
        minLeavesToID = TCSettings.minLeavesToID;
        breakSpeedModifier = TCSettings.breakSpeedModifier;
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
        
        return this;
    }
    
    public TreeDefinition appendWithSettings(TreeDefinition toAdd)
    {
        append(toAdd);
        
        if (toAdd.onlyDestroyUpwards != TCSettings.onlyDestroyUpwards)
            onlyDestroyUpwards = toAdd.onlyDestroyUpwards;
        if (toAdd.requireLeafDecayCheck != TCSettings.requireLeafDecayCheck)
            requireLeafDecayCheck = toAdd.requireLeafDecayCheck;
        if (toAdd.maxHorLogBreakDist != TCSettings.maxHorLogBreakDist)
            maxHorLogBreakDist = toAdd.maxHorLogBreakDist;
        if (toAdd.maxLeafBreakDist != TCSettings.maxLeafBreakDist)
            maxLeafBreakDist = toAdd.maxLeafBreakDist;
        if (toAdd.maxLeafIDDist != TCSettings.maxLeafIDDist)
            maxLeafIDDist = toAdd.maxLeafIDDist;
        if (toAdd.minLeavesToID != TCSettings.minLeavesToID)
            minLeavesToID = toAdd.minLeavesToID;
        if (toAdd.breakSpeedModifier != TCSettings.breakSpeedModifier)
            breakSpeedModifier = toAdd.breakSpeedModifier;
        
        return this;
    }
    
    public TreeDefinition readFromNBT(NBTTagCompound treeDefNBT)
    {
        if (treeDefNBT.hasKey(Strings.onlyDestroyUpwards))
            onlyDestroyUpwards = treeDefNBT.getBoolean(Strings.onlyDestroyUpwards);
        if (treeDefNBT.hasKey(Strings.requireLeafDecayCheck))
            requireLeafDecayCheck = treeDefNBT.getBoolean(Strings.requireLeafDecayCheck);
        if (treeDefNBT.hasKey(Strings.maxHorLogBreakDist))
            maxHorLogBreakDist = treeDefNBT.getInteger(Strings.maxHorLogBreakDist);
        if (treeDefNBT.hasKey(Strings.maxVerLogBreakDist))
            maxVerLogBreakDist = treeDefNBT.getInteger(Strings.maxVerLogBreakDist);
        if (treeDefNBT.hasKey(Strings.maxLeafBreakDist))
            maxLeafBreakDist = treeDefNBT.getInteger(Strings.maxLeafBreakDist);
        if (treeDefNBT.hasKey(Strings.maxLeafIDDist))
            maxLeafIDDist = treeDefNBT.getInteger(Strings.maxLeafIDDist);
        if (treeDefNBT.hasKey(Strings.minLeavesToID))
            minLeavesToID = treeDefNBT.getInteger(Strings.minLeavesToID);
        if (treeDefNBT.hasKey(Strings.breakSpeedModifier))
            breakSpeedModifier = treeDefNBT.getFloat(Strings.breakSpeedModifier);
        
        logBlocks = ListUtils.getDelimitedStringAsBlockIDList(treeDefNBT.getString(Strings.LOGS), ";");
        leafBlocks = ListUtils.getDelimitedStringAsBlockIDList(treeDefNBT.getString(Strings.LEAVES), ";");
        
        return this;
    }
    
    public void writeToNBT(NBTTagCompound treeDefNBT)
    {
        treeDefNBT.setBoolean(Strings.onlyDestroyUpwards, onlyDestroyUpwards);
        treeDefNBT.setBoolean(Strings.requireLeafDecayCheck, requireLeafDecayCheck);
        treeDefNBT.setInteger(Strings.maxHorLogBreakDist, maxHorLogBreakDist);
        treeDefNBT.setInteger(Strings.maxVerLogBreakDist, maxVerLogBreakDist);
        treeDefNBT.setInteger(Strings.maxLeafBreakDist, maxLeafBreakDist);
        treeDefNBT.setInteger(Strings.maxLeafIDDist, maxLeafIDDist);
        treeDefNBT.setInteger(Strings.minLeavesToID, minLeavesToID);
        treeDefNBT.setFloat(Strings.breakSpeedModifier, breakSpeedModifier);
        
        treeDefNBT.setString(Strings.LOGS, ListUtils.getListAsDelimitedString(logBlocks, ";"));
        treeDefNBT.setString(Strings.LEAVES, ListUtils.getListAsDelimitedString(leafBlocks, ";"));
    }
    
    /*
     * Field setters
     */
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
    
    public TreeDefinition setMaxHorLogBreakDist(int maxHorLogBreakDist)
    {
        this.maxHorLogBreakDist = maxHorLogBreakDist;
        return this;
    }
    
    public TreeDefinition setMaxVerLogBreakDist(int maxVerLogBreakDist)
    {
        this.maxVerLogBreakDist = maxVerLogBreakDist;
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
    
    public TreeDefinition setBreakSpeedModifier(float breakSpeedModifier)
    {
        this.breakSpeedModifier = breakSpeedModifier;
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
    
    /*
     * Field accessors
     */
    public boolean onlyDestroyUpwards()
    {
        return onlyDestroyUpwards;
    }
    
    public boolean requireLeafDecayCheck()
    {
        return requireLeafDecayCheck;
    }
    
    public int maxHorLogBreakDist()
    {
        return maxHorLogBreakDist;
    }
    
    public int maxVerLogBreakDist()
    {
        return maxVerLogBreakDist;
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
    
    public float breakSpeedModifier()
    {
        return breakSpeedModifier;
    }
}
