package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import bspkrs.util.BlockID;
import bspkrs.util.HashCodeUtil;

public class ConfigTreeDefinition extends TreeDefinition
{
    protected List<String> logKeys;
    protected List<String> leafKeys;
    
    public ConfigTreeDefinition()
    {
        super();
        logKeys = new ArrayList<String>();
        leafKeys = new ArrayList<String>();
    }
    
    public ConfigTreeDefinition(List<BlockID> logs, List<BlockID> leaves)
    {
        super(logs, leaves);
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
        int result = 23;
        result = HashCodeUtil.hash(result, logBlocks);
        result = HashCodeUtil.hash(result, leafBlocks);
        return result;
    }
    
    @Override
    public TreeDefinition readFromNBT(NBTTagCompound treeDefNBT)
    {
        super.readFromNBT(treeDefNBT);
        
        return this;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound treeDefNBT)
    {
        super.writeToNBT(treeDefNBT);
    }
}
