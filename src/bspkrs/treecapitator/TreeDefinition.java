package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import bspkrs.util.BlockID;
import bspkrs.util.HashCodeUtil;

public class TreeDefinition
{
    protected HashSet<BlockID> logBlocks;
    protected HashSet<BlockID> leafBlocks;
    protected boolean          allowSmartTreeDetection;
    protected boolean          onlyDestroyUpwards;
    protected boolean          requireLeafDecayCheck;
    // max horizontal distance that logs will be broken
    protected int              maxHorLogBreakDist;
    protected int              maxVerLogBreakDist;
    protected int              maxLeafIDDist;
    protected int              maxHorLeafBreakDist;
    protected int              minLeavesToID;
    protected float            breakSpeedModifier;
    
    public TreeDefinition()
    {
        logBlocks = new HashSet<BlockID>();
        leafBlocks = new HashSet<BlockID>();
        
        allowSmartTreeDetection = TCSettings.allowSmartTreeDetection;
        onlyDestroyUpwards = TCSettings.onlyDestroyUpwards;
        requireLeafDecayCheck = TCSettings.requireLeafDecayCheck;
        maxHorLogBreakDist = TCSettings.maxHorLogBreakDist;
        maxVerLogBreakDist = TCSettings.maxVerLogBreakDist;
        maxLeafIDDist = TCSettings.maxLeafIDDist;
        maxHorLeafBreakDist = TCSettings.maxHorLeafBreakDist;
        minLeavesToID = TCSettings.minLeavesToID;
        breakSpeedModifier = TCSettings.breakSpeedModifier;
    }
    
    public TreeDefinition(List<BlockID> logs, List<BlockID> leaves)
    {
        this();
        logBlocks.addAll(logs);
        leafBlocks.addAll(leaves);
    }
    
    public TreeDefinition(NBTTagCompound tree)
    {
        this();
        this.readFromNBT(tree);
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
        
        if (toAdd.allowSmartTreeDetection != TCSettings.allowSmartTreeDetection)
            allowSmartTreeDetection = toAdd.allowSmartTreeDetection;
        if (toAdd.onlyDestroyUpwards != TCSettings.onlyDestroyUpwards)
            onlyDestroyUpwards = toAdd.onlyDestroyUpwards;
        if (toAdd.requireLeafDecayCheck != TCSettings.requireLeafDecayCheck)
            requireLeafDecayCheck = toAdd.requireLeafDecayCheck;
        if (toAdd.maxHorLogBreakDist != TCSettings.maxHorLogBreakDist)
            maxHorLogBreakDist = toAdd.maxHorLogBreakDist;
        if (toAdd.maxHorLeafBreakDist != TCSettings.maxHorLeafBreakDist)
            maxHorLeafBreakDist = toAdd.maxHorLeafBreakDist;
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
        if (treeDefNBT.hasKey(Strings.ALLOW_SMART_TREE_DETECT))
            allowSmartTreeDetection = treeDefNBT.getBoolean(Strings.ALLOW_SMART_TREE_DETECT);
        if (treeDefNBT.hasKey(Strings.ONLY_DESTROY_UPWARDS))
            onlyDestroyUpwards = treeDefNBT.getBoolean(Strings.ONLY_DESTROY_UPWARDS);
        if (treeDefNBT.hasKey(Strings.REQ_DECAY_CHECK))
            requireLeafDecayCheck = treeDefNBT.getBoolean(Strings.REQ_DECAY_CHECK);
        if (treeDefNBT.hasKey(Strings.MAX_H_LOG_DIST))
            maxHorLogBreakDist = treeDefNBT.getInteger(Strings.MAX_H_LOG_DIST);
        if (treeDefNBT.hasKey(Strings.MAX_V_LOG_DIST))
            maxVerLogBreakDist = treeDefNBT.getInteger(Strings.MAX_V_LOG_DIST);
        if (treeDefNBT.hasKey(Strings.MAX_H_LEAF_DIST))
            maxHorLeafBreakDist = treeDefNBT.getInteger(Strings.MAX_H_LEAF_DIST);
        if (treeDefNBT.hasKey(Strings.MAX_LEAF_ID_DIST))
            maxLeafIDDist = treeDefNBT.getInteger(Strings.MAX_LEAF_ID_DIST);
        if (treeDefNBT.hasKey(Strings.MIN_LEAF_ID))
            minLeavesToID = treeDefNBT.getInteger(Strings.MIN_LEAF_ID);
        if (treeDefNBT.hasKey(Strings.BREAK_SPEED_MOD))
            breakSpeedModifier = treeDefNBT.getFloat(Strings.BREAK_SPEED_MOD);
        
        if (treeDefNBT.hasKey(Strings.LOGS) && treeDefNBT.getString(Strings.LOGS).length() > 0)
            logBlocks = (HashSet<BlockID>) TCUtils.getDelimitedStringAsBlockIDHashSet(treeDefNBT.getString(Strings.LOGS), ";");
        else
            logBlocks = new HashSet<BlockID>();
        
        if (treeDefNBT.hasKey(Strings.LEAVES) && treeDefNBT.getString(Strings.LEAVES).length() > 0)
            leafBlocks = (HashSet<BlockID>) TCUtils.getDelimitedStringAsBlockIDHashSet(treeDefNBT.getString(Strings.LEAVES), ";");
        else
            leafBlocks = new HashSet<BlockID>();
        
        return this;
    }
    
    public void writeToNBT(NBTTagCompound treeDefNBT)
    {
        treeDefNBT.setBoolean(Strings.ALLOW_SMART_TREE_DETECT, allowSmartTreeDetection);
        treeDefNBT.setBoolean(Strings.ONLY_DESTROY_UPWARDS, onlyDestroyUpwards);
        treeDefNBT.setBoolean(Strings.REQ_DECAY_CHECK, requireLeafDecayCheck);
        treeDefNBT.setInteger(Strings.MAX_H_LOG_DIST, maxHorLogBreakDist);
        treeDefNBT.setInteger(Strings.MAX_V_LOG_DIST, maxVerLogBreakDist);
        treeDefNBT.setInteger(Strings.MAX_H_LEAF_DIST, maxHorLeafBreakDist);
        treeDefNBT.setInteger(Strings.MAX_LEAF_ID_DIST, maxLeafIDDist);
        treeDefNBT.setInteger(Strings.MIN_LEAF_ID, minLeavesToID);
        treeDefNBT.setFloat(Strings.BREAK_SPEED_MOD, breakSpeedModifier);
        
        treeDefNBT.setString(Strings.LOGS, TCUtils.getSetAsDelimitedString(logBlocks, ";"));
        treeDefNBT.setString(Strings.LEAVES, TCUtils.getSetAsDelimitedString(leafBlocks, ";"));
    }
    
    /*
     * Field setters
     */
    public TreeDefinition setAllowSmartTreeDetection(boolean allowSmartTreeDetection)
    {
        this.allowSmartTreeDetection = allowSmartTreeDetection;
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
    
    public TreeDefinition setMaxHorLeafBreakDist(int maxLeafBreakDist)
    {
        maxHorLeafBreakDist = maxLeafBreakDist;
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
    public boolean allowSmartTreeDetection()
    {
        return allowSmartTreeDetection;
    }
    
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
    
    public int maxHorLeafBreakDist()
    {
        return maxHorLeafBreakDist;
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
