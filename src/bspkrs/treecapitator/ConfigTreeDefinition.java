package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import bspkrs.util.BlockID;
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
        // TODO: finish this
        return this;
    }
    
    public void writeToConfiguration(Configuration config, String category)
    {
        // TODO: finish this
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
            super.addLogID(new BlockID(log));
        
        for (String leaf : rLeaves.split(";"))
            super.addLeafID(new BlockID(leaf));
        
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
    // TODO: fix this up
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
    // TODO: fix this up
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
    public ConfigTreeDefinition setMaxLeafBreakDist(int maxLeafBreakDist)
    {
        this.maxLeafBreakDist = maxLeafBreakDist;
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
