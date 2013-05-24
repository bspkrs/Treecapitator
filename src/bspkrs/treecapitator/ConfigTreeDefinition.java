package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import bspkrs.util.BlockID;
import bspkrs.util.ConfigCategory;
import bspkrs.util.Configuration;
import bspkrs.util.HashCodeUtil;
import bspkrs.util.ListUtils;

public class ConfigTreeDefinition extends TreeDefinition
{
    protected String logKeys;
    protected String leafKeys;
    
    public ConfigTreeDefinition()
    {
        super();
        logKeys = ListUtils.getListAsDelimitedString(logBlocks, "; ");
        leafKeys = ListUtils.getListAsDelimitedString(leafBlocks, "; ");
    }
    
    public ConfigTreeDefinition(List<BlockID> logs, List<BlockID> leaves)
    {
        super(logs, leaves);
        logKeys = ListUtils.getListAsDelimitedString(logBlocks, "; ");
        leafKeys = ListUtils.getListAsDelimitedString(leafBlocks, "; ");
    }
    
    public ConfigTreeDefinition(String configLogs, String configLeaves)
    {
        logKeys = configLogs;
        leafKeys = configLeaves;
    }
    
    public ConfigTreeDefinition(Configuration config, String category)
    {
        readFromConfiguration(config, category);
    }
    
    public ConfigTreeDefinition(NBTTagCompound treeDefNBT)
    {
        readFromNBT(treeDefNBT);
    }
    
    @Override
    public ConfigTreeDefinition readFromNBT(NBTTagCompound treeDefNBT)
    {
        super.readFromNBT(treeDefNBT);
        
        if (treeDefNBT.hasKey(Strings.LOG_CFG_KEYS))
            logKeys = treeDefNBT.getString(Strings.LOG_CFG_KEYS);
        
        if (treeDefNBT.hasKey(Strings.LEAF_CFG_KEYS))
            leafKeys = treeDefNBT.getString(Strings.LEAF_CFG_KEYS);
        
        return this;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound treeDefNBT)
    {
        super.writeToNBT(treeDefNBT);
        
        treeDefNBT.setString(Strings.LOG_CFG_KEYS, logKeys);
        treeDefNBT.setString(Strings.LEAF_CFG_KEYS, leafKeys);
    }
    
    public ConfigTreeDefinition readFromConfiguration(Configuration config, String category)
    {
        ConfigCategory cc = config.getCategory(category);
        if (cc.containsKey(Strings.ONLY_DESTROY_UPWARDS))
            onlyDestroyUpwards = cc.get(Strings.ONLY_DESTROY_UPWARDS).getBoolean(TCSettings.onlyDestroyUpwards);
        if (cc.containsKey(Strings.REQ_DECAY_CHECK))
            requireLeafDecayCheck = cc.get(Strings.REQ_DECAY_CHECK).getBoolean(TCSettings.requireLeafDecayCheck);
        if (cc.containsKey(Strings.MAX_H_LOG_DIST))
            maxHorLogBreakDist = cc.get(Strings.MAX_H_LOG_DIST).getInt(TCSettings.maxHorLogBreakDist);
        if (cc.containsKey(Strings.MAX_V_LOG_DIST))
            maxVerLogBreakDist = cc.get(Strings.MAX_V_LOG_DIST).getInt(TCSettings.maxVerLogBreakDist);
        if (cc.containsKey(Strings.MAX_H_LEAF_DIST))
            maxHorLeafBreakDist = cc.get(Strings.MAX_H_LEAF_DIST).getInt(TCSettings.maxHorLeafBreakDist);
        if (cc.containsKey(Strings.MAX_LEAF_ID_DIST))
            maxLeafIDDist = cc.get(Strings.MAX_LEAF_ID_DIST).getInt(TCSettings.maxLeafIDDist);
        if (cc.containsKey(Strings.MIN_LEAF_ID))
            minLeavesToID = cc.get(Strings.MIN_LEAF_ID).getInt();
        if (cc.containsKey(Strings.BREAK_SPEED_MOD))
            breakSpeedModifier = (float) cc.get(Strings.BREAK_SPEED_MOD).getDouble(TCSettings.breakSpeedModifier);
        
        logKeys = cc.get(Strings.LOG_CFG_KEYS).getString();
        leafKeys = cc.get(Strings.LEAF_CFG_KEYS).getString();
        
        return this;
    }
    
    public void writeToConfiguration(Configuration config, String category)
    {
        if (onlyDestroyUpwards != TCSettings.onlyDestroyUpwards)
            config.get(category, Strings.ONLY_DESTROY_UPWARDS, onlyDestroyUpwards);
        if (requireLeafDecayCheck != TCSettings.requireLeafDecayCheck)
            config.get(category, Strings.REQ_DECAY_CHECK, requireLeafDecayCheck);
        if (maxHorLogBreakDist != TCSettings.maxHorLogBreakDist)
            config.get(category, Strings.MAX_H_LOG_DIST, maxHorLogBreakDist);
        if (maxVerLogBreakDist != TCSettings.maxVerLogBreakDist)
            config.get(category, Strings.MAX_V_LOG_DIST, maxVerLogBreakDist);
        if (maxHorLeafBreakDist != TCSettings.maxHorLeafBreakDist)
            config.get(category, Strings.MAX_H_LEAF_DIST, maxHorLeafBreakDist);
        if (maxLeafIDDist != TCSettings.maxLeafIDDist)
            config.get(category, Strings.MAX_LEAF_ID_DIST, maxLeafIDDist);
        if (minLeavesToID != TCSettings.minLeavesToID)
            config.get(category, Strings.MIN_LEAF_ID, minLeavesToID);
        if (breakSpeedModifier != TCSettings.breakSpeedModifier)
            config.get(category, Strings.BREAK_SPEED_MOD, breakSpeedModifier);
        
        config.get(category, Strings.LOG_CFG_KEYS, logKeys);
        config.get(category, Strings.LEAF_CFG_KEYS, leafKeys);
    }
    
    public TreeDefinition getTagsReplacedTreeDef(Map<String, String> tagMap)
    {
        logBlocks = new ArrayList<BlockID>();
        leafBlocks = new ArrayList<BlockID>();
        
        String rLogs = logKeys;
        String rLeaves = leafKeys;
        
        for (Entry<String, String> e : tagMap.entrySet())
        {
            rLogs = rLogs.replace(e.getKey(), e.getValue());
            rLeaves = rLeaves.replace(e.getKey(), e.getValue());
        }
        
        for (String log : rLogs.split(";"))
            super.addLogID(new BlockID(log, ",", 17));
        
        for (String leaf : rLeaves.split(";"))
            super.addLeafID(new BlockID(leaf, ",", 17));
        
        return this;
    }
    
    @Override
    public ConfigTreeDefinition addLogID(BlockID blockID)
    {
        ConfigTreeDefinition r = (ConfigTreeDefinition) super.addLogID(blockID);
        logKeys = ListUtils.getListAsDelimitedString(r.logBlocks, "; ");
        
        return r;
    }
    
    @Override
    public ConfigTreeDefinition addLeafID(BlockID blockID)
    {
        ConfigTreeDefinition r = (ConfigTreeDefinition) super.addLeafID(blockID);
        leafKeys = ListUtils.getListAsDelimitedString(r.leafBlocks, "; ");
        
        return r;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof ConfigTreeDefinition))
            return false;
        
        if (o == this)
            return true;
        
        ConfigTreeDefinition td = (ConfigTreeDefinition) o;
        return td.logBlocks.equals(logBlocks) && td.leafBlocks.equals(leafBlocks);
        
    }
    
    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = HashCodeUtil.hash(result, logKeys);
        result = HashCodeUtil.hash(result, leafKeys);
        return result;
    }
    
    /**
     * Retrieves a copy of the list of logs in this TreeDefinition.
     * 
     * @return
     */
    public String getConfigLogList()
    {
        return logKeys;
    }
    
    /**
     * Retrieves a copy of the list of leaves in this TreeDefinition.
     * 
     * @return
     */
    public String getConfigLeafList()
    {
        return leafKeys;
    }
    
    @Override
    public ConfigTreeDefinition setOnlyDestroyUpwards(boolean onlyDestroyUpwards)
    {
        this.onlyDestroyUpwards = onlyDestroyUpwards;
        return this;
    }
    
    @Override
    public ConfigTreeDefinition setRequireLeafDecayCheck(boolean requireLeafDecayCheck)
    {
        this.requireLeafDecayCheck = requireLeafDecayCheck;
        return this;
    }
    
    @Override
    public ConfigTreeDefinition setMaxHorLogBreakDist(int maxHorLogBreakDist)
    {
        this.maxHorLogBreakDist = maxHorLogBreakDist;
        return this;
    }
    
    @Override
    public ConfigTreeDefinition setMaxVerLogBreakDist(int maxVerLogBreakDist)
    {
        this.maxVerLogBreakDist = maxVerLogBreakDist;
        return this;
    }
    
    @Override
    public ConfigTreeDefinition setMaxLeafIDDist(int maxLeafIDDist)
    {
        this.maxLeafIDDist = maxLeafIDDist;
        return this;
    }
    
    @Override
    public ConfigTreeDefinition setMaxHorLeafBreakDist(int maxHorLeafBreakDist)
    {
        this.maxHorLeafBreakDist = maxHorLeafBreakDist;
        return this;
    }
    
    @Override
    public ConfigTreeDefinition setMinLeavesToID(int minLeavesToID)
    {
        this.minLeavesToID = minLeavesToID;
        return this;
    }
    
    @Override
    public ConfigTreeDefinition setBreakSpeedModifier(float breakSpeedModifier)
    {
        this.breakSpeedModifier = breakSpeedModifier;
        return this;
    }
}
