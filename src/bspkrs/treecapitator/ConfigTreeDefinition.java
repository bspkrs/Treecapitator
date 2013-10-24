package bspkrs.treecapitator;

import java.util.HashSet;
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
        logKeys = TCUtils.getSetAsDelimitedString(logBlocks, "; ");
        leafKeys = TCUtils.getSetAsDelimitedString(leafBlocks, "; ");
    }
    
    public ConfigTreeDefinition(List<BlockID> logs, List<BlockID> leaves)
    {
        super(logs, leaves);
        logKeys = TCUtils.getSetAsDelimitedString(logBlocks, "; ");
        leafKeys = TCUtils.getSetAsDelimitedString(leafBlocks, "; ");
    }
    
    public ConfigTreeDefinition(String configLogs, String configLeaves)
    {
        this();
        logKeys = configLogs;
        leafKeys = configLeaves;
    }
    
    public ConfigTreeDefinition(Configuration config, String category)
    {
        this();
        readFromConfiguration(config, category);
    }
    
    public ConfigTreeDefinition(NBTTagCompound treeDefNBT)
    {
        this();
        readFromNBT(treeDefNBT);
    }
    
    @Override
    public ConfigTreeDefinition readFromNBT(NBTTagCompound treeDefNBT)
    {
        super.readFromNBT(treeDefNBT);
        
        if (treeDefNBT.hasKey(Strings.LOG_CFG_KEYS))
            logKeys = treeDefNBT.getString(Strings.LOG_CFG_KEYS);
        else
            logKeys = ListUtils.getListAsDelimitedString((List<?>) logBlocks, "; ");
        
        if (treeDefNBT.hasKey(Strings.LEAF_CFG_KEYS))
            leafKeys = treeDefNBT.getString(Strings.LEAF_CFG_KEYS);
        else
            leafKeys = ListUtils.getListAsDelimitedString((List<?>) leafBlocks, "; ");
        
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
        
        if (cc.containsKey(Strings.ALLOW_SMART_TREE_DETECT))
            onlyDestroyUpwards = cc.get(Strings.ALLOW_SMART_TREE_DETECT).getBoolean(TCSettings.allowSmartTreeDetection);
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
        if (cc.containsKey(Strings.LEAF_CFG_KEYS))
            leafKeys = cc.get(Strings.LEAF_CFG_KEYS).getString();
        
        return this;
    }
    
    public void writeToConfiguration(Configuration config, String category)
    {
        if (allowSmartTreeDetection != TCSettings.allowSmartTreeDetection)
            config.get(category, Strings.ALLOW_SMART_TREE_DETECT, TCSettings.allowSmartTreeDetection, Strings.OPTIONAL).set(allowSmartTreeDetection);
        if (onlyDestroyUpwards != TCSettings.onlyDestroyUpwards)
            config.get(category, Strings.ONLY_DESTROY_UPWARDS, TCSettings.onlyDestroyUpwards, Strings.OPTIONAL).set(onlyDestroyUpwards);
        if (requireLeafDecayCheck != TCSettings.requireLeafDecayCheck)
            config.get(category, Strings.REQ_DECAY_CHECK, TCSettings.requireLeafDecayCheck, Strings.OPTIONAL).set(requireLeafDecayCheck);
        if (maxHorLogBreakDist != TCSettings.maxHorLogBreakDist)
            config.get(category, Strings.MAX_H_LOG_DIST, TCSettings.maxHorLogBreakDist, Strings.OPTIONAL).set(maxHorLogBreakDist);
        if (maxVerLogBreakDist != TCSettings.maxVerLogBreakDist)
            config.get(category, Strings.MAX_V_LOG_DIST, TCSettings.maxVerLogBreakDist, Strings.OPTIONAL).set(maxVerLogBreakDist);
        if (maxHorLeafBreakDist != TCSettings.maxHorLeafBreakDist)
            config.get(category, Strings.MAX_H_LEAF_DIST, TCSettings.maxHorLeafBreakDist, Strings.OPTIONAL).set(maxHorLeafBreakDist);
        if (maxLeafIDDist != TCSettings.maxLeafIDDist)
            config.get(category, Strings.MAX_LEAF_ID_DIST, TCSettings.maxLeafIDDist, Strings.OPTIONAL).set(maxLeafIDDist);
        if (minLeavesToID != TCSettings.minLeavesToID)
            config.get(category, Strings.MIN_LEAF_ID, TCSettings.minLeavesToID, Strings.OPTIONAL).set(minLeavesToID);
        if (breakSpeedModifier != TCSettings.breakSpeedModifier)
            config.get(category, Strings.BREAK_SPEED_MOD, TCSettings.breakSpeedModifier, Strings.OPTIONAL).set(breakSpeedModifier);
        
        config.get(category, Strings.LOG_CFG_KEYS, logKeys);
        config.get(category, Strings.LEAF_CFG_KEYS, leafKeys);
    }
    
    public TreeDefinition getTagsReplacedTreeDef(Map<String, String> tagMap)
    {
        logBlocks = new HashSet<BlockID>();
        leafBlocks = new HashSet<BlockID>();
        
        String rLogs = logKeys;
        String rLeaves = leafKeys;
        
        for (Entry<String, String> e : tagMap.entrySet())
        {
            rLogs = rLogs.replace(e.getKey(), e.getValue());
            rLeaves = rLeaves.replace(e.getKey(), e.getValue());
        }
        
        for (String log : rLogs.split(";"))
        {
            BlockID blockID = new BlockID(log, ",", 0);
            if (blockID.id > 0)
                super.addLogID(blockID);
        }
        
        for (String leaf : rLeaves.split(";"))
        {
            BlockID blockID = new BlockID(leaf, ",", 0);
            if (blockID.id > 0)
                super.addLeafID(blockID);
        }
        
        return this;
    }
    
    @Override
    public ConfigTreeDefinition addLogID(BlockID blockID)
    {
        ConfigTreeDefinition r = (ConfigTreeDefinition) super.addLogID(blockID);
        logKeys = TCUtils.getSetAsDelimitedString(r.logBlocks, "; ");
        
        return r;
    }
    
    @Override
    public ConfigTreeDefinition addLeafID(BlockID blockID)
    {
        ConfigTreeDefinition r = (ConfigTreeDefinition) super.addLeafID(blockID);
        leafKeys = TCUtils.getSetAsDelimitedString(r.leafBlocks, "; ");
        
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
    public ConfigTreeDefinition setAllowSmartTreeDetection(boolean allowSmartTreeDetection)
    {
        this.allowSmartTreeDetection = allowSmartTreeDetection;
        return this;
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
