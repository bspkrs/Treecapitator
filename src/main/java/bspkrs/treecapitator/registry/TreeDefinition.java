package bspkrs.treecapitator.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.util.TCConst;
import bspkrs.util.BlockID;
import bspkrs.util.HashCodeUtil;
import bspkrs.util.ListUtils;
import bspkrs.util.config.ConfigCategory;
import bspkrs.util.config.Configuration;
import bspkrs.util.config.Property;

public class TreeDefinition
{
    protected List<BlockID> logBlocks;
    protected List<BlockID> leafBlocks;
    protected boolean       allowSmartTreeDetection;
    protected boolean       onlyDestroyUpwards;
    protected boolean       requireLeafDecayCheck;
    // max horizontal distance that logs will be broken
    protected int           maxHorLogBreakDist;
    protected int           maxVerLogBreakDist;
    protected int           maxLeafIDDist;
    protected int           maxHorLeafBreakDist;
    protected int           minLeavesToID;
    protected float         breakSpeedModifier;
    protected boolean       useAdvancedTopLogLogic;
    
    public TreeDefinition()
    {
        logBlocks = new ArrayList<BlockID>();
        leafBlocks = new ArrayList<BlockID>();
        
        allowSmartTreeDetection = TCSettings.allowSmartTreeDetection;
        onlyDestroyUpwards = TCSettings.onlyDestroyUpwards;
        requireLeafDecayCheck = TCSettings.requireLeafDecayCheck;
        maxHorLogBreakDist = TCSettings.maxHorLogBreakDist;
        maxVerLogBreakDist = TCSettings.maxVerLogBreakDist;
        maxLeafIDDist = TCSettings.maxLeafIDDist;
        maxHorLeafBreakDist = TCSettings.maxHorLeafBreakDist;
        minLeavesToID = TCSettings.minLeavesToID;
        breakSpeedModifier = TCSettings.breakSpeedModifier;
        useAdvancedTopLogLogic = TCSettings.useAdvancedTopLogLogic;
    }
    
    @Override
    public String toString()
    {
        return "Logs: " + ListUtils.getListAsDelimitedString(logBlocks, "; ") + "  Leaves: " + ListUtils.getListAsDelimitedString(leafBlocks, "; ");
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
    
    public TreeDefinition(Configuration config, String category)
    {
        this();
        readFromConfiguration(config, category);
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
        if (toAdd.useAdvancedTopLogLogic != TCSettings.useAdvancedTopLogLogic)
            useAdvancedTopLogLogic = toAdd.useAdvancedTopLogLogic;
        
        return this;
    }
    
    public TreeDefinition readFromNBT(NBTTagCompound treeDefNBT)
    {
        if (treeDefNBT.hasKey(TCConst.ALLOW_SMART_TREE_DETECT))
            allowSmartTreeDetection = treeDefNBT.getBoolean(TCConst.ALLOW_SMART_TREE_DETECT);
        if (treeDefNBT.hasKey(TCConst.ONLY_DESTROY_UPWARDS))
            onlyDestroyUpwards = treeDefNBT.getBoolean(TCConst.ONLY_DESTROY_UPWARDS);
        if (treeDefNBT.hasKey(TCConst.REQ_DECAY_CHECK))
            requireLeafDecayCheck = treeDefNBT.getBoolean(TCConst.REQ_DECAY_CHECK);
        if (treeDefNBT.hasKey(TCConst.MAX_H_LOG_DIST))
            maxHorLogBreakDist = treeDefNBT.getInteger(TCConst.MAX_H_LOG_DIST);
        if (treeDefNBT.hasKey(TCConst.MAX_V_LOG_DIST))
            maxVerLogBreakDist = treeDefNBT.getInteger(TCConst.MAX_V_LOG_DIST);
        if (treeDefNBT.hasKey(TCConst.MAX_H_LEAF_DIST))
            maxHorLeafBreakDist = treeDefNBT.getInteger(TCConst.MAX_H_LEAF_DIST);
        if (treeDefNBT.hasKey(TCConst.MAX_LEAF_ID_DIST))
            maxLeafIDDist = treeDefNBT.getInteger(TCConst.MAX_LEAF_ID_DIST);
        if (treeDefNBT.hasKey(TCConst.MIN_LEAF_ID))
            minLeavesToID = treeDefNBT.getInteger(TCConst.MIN_LEAF_ID);
        if (treeDefNBT.hasKey(TCConst.BREAK_SPEED_MOD))
            breakSpeedModifier = treeDefNBT.getFloat(TCConst.BREAK_SPEED_MOD);
        if (treeDefNBT.hasKey("useAdvancedTopLogLogic"))
            useAdvancedTopLogLogic = treeDefNBT.getBoolean("useAdvancedTopLogLogic");
        
        if (treeDefNBT.hasKey(TCConst.LOGS) && treeDefNBT.getString(TCConst.LOGS).length() > 0)
            logBlocks = ListUtils.getDelimitedStringAsBlockIDList(treeDefNBT.getString(TCConst.LOGS), ";");
        else
            logBlocks = new ArrayList<BlockID>();
        
        if (treeDefNBT.hasKey(TCConst.LEAVES) && treeDefNBT.getString(TCConst.LEAVES).length() > 0)
            leafBlocks = ListUtils.getDelimitedStringAsBlockIDList(treeDefNBT.getString(TCConst.LEAVES), ";");
        else
            leafBlocks = new ArrayList<BlockID>();
        
        return this;
    }
    
    public void writeToNBT(NBTTagCompound treeDefNBT)
    {
        treeDefNBT.setBoolean(TCConst.ALLOW_SMART_TREE_DETECT, allowSmartTreeDetection);
        treeDefNBT.setBoolean(TCConst.ONLY_DESTROY_UPWARDS, onlyDestroyUpwards);
        treeDefNBT.setBoolean(TCConst.REQ_DECAY_CHECK, requireLeafDecayCheck);
        treeDefNBT.setInteger(TCConst.MAX_H_LOG_DIST, maxHorLogBreakDist);
        treeDefNBT.setInteger(TCConst.MAX_V_LOG_DIST, maxVerLogBreakDist);
        treeDefNBT.setInteger(TCConst.MAX_H_LEAF_DIST, maxHorLeafBreakDist);
        treeDefNBT.setInteger(TCConst.MAX_LEAF_ID_DIST, maxLeafIDDist);
        treeDefNBT.setInteger(TCConst.MIN_LEAF_ID, minLeavesToID);
        treeDefNBT.setFloat(TCConst.BREAK_SPEED_MOD, breakSpeedModifier);
        treeDefNBT.setBoolean("useAdvancedTopLogLogic", useAdvancedTopLogLogic);
        
        treeDefNBT.setString(TCConst.LOGS, ListUtils.getListAsDelimitedString(logBlocks, ";"));
        treeDefNBT.setString(TCConst.LEAVES, ListUtils.getListAsDelimitedString(leafBlocks, ";"));
    }
    
    public TreeDefinition readFromConfiguration(Configuration config, String category)
    {
        ConfigCategory cc = config.getCategory(category);
        
        if (cc.containsKey(TCConst.ALLOW_SMART_TREE_DETECT))
            onlyDestroyUpwards = cc.get(TCConst.ALLOW_SMART_TREE_DETECT).getBoolean(TCSettings.allowSmartTreeDetection);
        if (cc.containsKey(TCConst.ONLY_DESTROY_UPWARDS))
            onlyDestroyUpwards = cc.get(TCConst.ONLY_DESTROY_UPWARDS).getBoolean(TCSettings.onlyDestroyUpwards);
        if (cc.containsKey(TCConst.REQ_DECAY_CHECK))
            requireLeafDecayCheck = cc.get(TCConst.REQ_DECAY_CHECK).getBoolean(TCSettings.requireLeafDecayCheck);
        if (cc.containsKey(TCConst.MAX_H_LOG_DIST))
            maxHorLogBreakDist = cc.get(TCConst.MAX_H_LOG_DIST).getInt(TCSettings.maxHorLogBreakDist);
        if (cc.containsKey(TCConst.MAX_V_LOG_DIST))
            maxVerLogBreakDist = cc.get(TCConst.MAX_V_LOG_DIST).getInt(TCSettings.maxVerLogBreakDist);
        if (cc.containsKey(TCConst.MAX_H_LEAF_DIST))
            maxHorLeafBreakDist = cc.get(TCConst.MAX_H_LEAF_DIST).getInt(TCSettings.maxHorLeafBreakDist);
        if (cc.containsKey(TCConst.MAX_LEAF_ID_DIST))
            maxLeafIDDist = cc.get(TCConst.MAX_LEAF_ID_DIST).getInt(TCSettings.maxLeafIDDist);
        if (cc.containsKey(TCConst.MIN_LEAF_ID))
            minLeavesToID = cc.get(TCConst.MIN_LEAF_ID).getInt();
        if (cc.containsKey(TCConst.BREAK_SPEED_MOD))
            breakSpeedModifier = (float) cc.get(TCConst.BREAK_SPEED_MOD).getDouble(TCSettings.breakSpeedModifier);
        if (cc.containsKey("useAdvancedTopLogLogic"))
            useAdvancedTopLogLogic = cc.get("useAdvancedTopLogLogic").getBoolean(TCSettings.useAdvancedTopLogLogic);
        
        logBlocks = ListUtils.getDelimitedStringAsBlockIDList(cc.get(TCConst.LOGS).getString(), "; ");
        if (cc.containsKey(TCConst.LEAVES))
            leafBlocks = ListUtils.getDelimitedStringAsBlockIDList(cc.get(TCConst.LEAVES).getString(), "; ");
        
        return this;
    }
    
    public void writeToConfiguration(Configuration config, String category)
    {
        Property temp;
        if (allowSmartTreeDetection != TCSettings.allowSmartTreeDetection)
        {
            temp = config.get(category, TCConst.ALLOW_SMART_TREE_DETECT, TCSettings.allowSmartTreeDetection, TCConst.OPTIONAL);
            temp.set(allowSmartTreeDetection);
            temp.setLanguageKey("bspkrs.tc.configgui." + TCConst.ALLOW_SMART_TREE_DETECT);
        }
        if (onlyDestroyUpwards != TCSettings.onlyDestroyUpwards)
        {
            temp = config.get(category, TCConst.ONLY_DESTROY_UPWARDS, TCSettings.onlyDestroyUpwards, TCConst.OPTIONAL);
            temp.set(onlyDestroyUpwards);
            temp.setLanguageKey("bspkrs.tc.configgui." + TCConst.ONLY_DESTROY_UPWARDS);
        }
        if (requireLeafDecayCheck != TCSettings.requireLeafDecayCheck)
        {
            temp = config.get(category, TCConst.REQ_DECAY_CHECK, TCSettings.requireLeafDecayCheck, TCConst.OPTIONAL);
            temp.set(requireLeafDecayCheck);
            temp.setLanguageKey("bspkrs.tc.configgui." + TCConst.REQ_DECAY_CHECK);
        }
        if (maxHorLogBreakDist != TCSettings.maxHorLogBreakDist)
        {
            temp = config.get(category, TCConst.MAX_H_LOG_DIST, TCSettings.maxHorLogBreakDist, TCConst.OPTIONAL);
            temp.set(maxHorLogBreakDist);
            temp.setLanguageKey("bspkrs.tc.configgui." + TCConst.MAX_H_LOG_DIST);
        }
        if (maxVerLogBreakDist != TCSettings.maxVerLogBreakDist)
        {
            temp = config.get(category, TCConst.MAX_V_LOG_DIST, TCSettings.maxVerLogBreakDist, TCConst.OPTIONAL);
            temp.set(maxVerLogBreakDist);
            temp.setLanguageKey("bspkrs.tc.configgui." + TCConst.MAX_V_LOG_DIST);
        }
        if (maxHorLeafBreakDist != TCSettings.maxHorLeafBreakDist)
        {
            temp = config.get(category, TCConst.MAX_H_LEAF_DIST, TCSettings.maxHorLeafBreakDist, TCConst.OPTIONAL);
            temp.set(maxHorLeafBreakDist);
            temp.setLanguageKey("bspkrs.tc.configgui." + TCConst.MAX_H_LEAF_DIST);
        }
        if (maxLeafIDDist != TCSettings.maxLeafIDDist)
        {
            temp = config.get(category, TCConst.MAX_LEAF_ID_DIST, TCSettings.maxLeafIDDist, TCConst.OPTIONAL);
            temp.set(maxLeafIDDist);
            temp.setLanguageKey("bspkrs.tc.configgui." + TCConst.MAX_LEAF_ID_DIST);
        }
        if (minLeavesToID != TCSettings.minLeavesToID)
        {
            temp = config.get(category, TCConst.MIN_LEAF_ID, TCSettings.minLeavesToID, TCConst.OPTIONAL);
            temp.set(minLeavesToID);
            temp.setLanguageKey("bspkrs.tc.configgui." + TCConst.MIN_LEAF_ID);
        }
        if (breakSpeedModifier != TCSettings.breakSpeedModifier)
        {
            temp = config.get(category, TCConst.BREAK_SPEED_MOD, TCSettings.breakSpeedModifier, TCConst.OPTIONAL);
            temp.set(breakSpeedModifier);
            temp.setLanguageKey("bspkrs.tc.configgui." + TCConst.BREAK_SPEED_MOD);
        }
        if (useAdvancedTopLogLogic != TCSettings.useAdvancedTopLogLogic)
        {
            temp = config.get(category, "useAdvancedTopLogLogic", TCSettings.useAdvancedTopLogLogic, TCConst.OPTIONAL);
            temp.set(useAdvancedTopLogLogic);
            temp.setLanguageKey("bspkrs.tc.configgui.useAdvancedTopLogLogic");
        }
        
        temp = config.get(category, TCConst.LOGS, ListUtils.getListAsDelimitedString(logBlocks, "; "));
        temp.setLanguageKey("bspkrs.tc.configgui." + TCConst.LOGS);
        temp = config.get(category, TCConst.LEAVES, ListUtils.getListAsDelimitedString(leafBlocks, "; "));
        temp.setLanguageKey("bspkrs.tc.configgui." + TCConst.LEAVES);
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
    
    public TreeDefinition setUseAdvancedTopLogLogic(boolean useAdvancedTopLogLogic)
    {
        this.useAdvancedTopLogLogic = useAdvancedTopLogLogic;
        return this;
    }
    
    /**
     * Retrieves a copy of the list of logs in this TreeDefinition.
     * 
     * @return
     */
    public List<BlockID> getLogList()
    {
        return logBlocks;
    }
    
    /**
     * Retrieves a copy of the list of leaves in this TreeDefinition.
     * 
     * @return
     */
    public List<BlockID> getLeafList()
    {
        return leafBlocks;
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
    
    public boolean useAdvancedTopLogLogic()
    {
        return useAdvancedTopLogLogic;
    }
}
