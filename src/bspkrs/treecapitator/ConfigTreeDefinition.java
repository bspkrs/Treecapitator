package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import bspkrs.util.BlockID;
import bspkrs.util.HashCodeUtil;

public class ConfigTreeDefinition extends TreeDefinition
{
    protected String logKeys;
    protected String leafKeys;
    
    public ConfigTreeDefinition()
    {
        super();
        logKeys = "";
        leafKeys = "";
    }
    
    public ConfigTreeDefinition(List<BlockID> logs, List<BlockID> leaves)
    {
        super(logs, leaves);
    }
    
    public ConfigTreeDefinition(String configLogs, String configLeaves)
    {
        logKeys = configLogs;
        leafKeys = configLeaves;
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
        int result = 23;
        result = HashCodeUtil.hash(result, logBlocks);
        result = HashCodeUtil.hash(result, leafBlocks);
        return result;
    }
    
    @Override
    public TreeDefinition readFromNBT(NBTTagCompound treeDefNBT)
    {
        super.readFromNBT(treeDefNBT);
        
        //if (treeDefNBT.hasKey(Strings.LOG_VALS))
        logKeys = treeDefNBT.getString(Strings.LOG_VALS);
        
        //if (treeDefNBT.hasKey(Strings.LEAF_VALS))
        leafKeys = treeDefNBT.getString(Strings.LEAF_VALS);
        
        return this;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound treeDefNBT)
    {
        super.writeToNBT(treeDefNBT);
        
        treeDefNBT.setString(Strings.LOG_VALS, logKeys);
        treeDefNBT.setString(Strings.LEAF_VALS, leafKeys);
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
}
