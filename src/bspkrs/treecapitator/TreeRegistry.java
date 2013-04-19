package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bspkrs.util.BlockID;

public class TreeRegistry
{
    private Map<String, TreeDefinition> treeDefs;
    private Map<BlockID, String>        logToStringMap;
    private List<BlockID>               masterLogList;
    
    private static TreeRegistry         instance;
    
    public static TreeRegistry instance()
    {
        if (instance == null)
            new TreeRegistry();
        
        return instance;
    }
    
    TreeRegistry()
    {
        instance = this;
        
        treeDefs = new HashMap<String, TreeDefinition>();
        masterLogList = new ArrayList<BlockID>();
        
        // Vanilla oak definition
        treeDefs.put(Strings.OAK, (new TreeDefinition()).addLogID(new BlockID(17, 0)).addLogID(new BlockID(17, 4)).addLogID(new BlockID(17, 8))
                .addLogID(new BlockID(17, 12)).addLeafID(new BlockID(18, 0)));
        masterLogList.addAll(treeDefs.get(Strings.OAK).getLogList());
        
        // Vanilla spruce definition
        treeDefs.put(Strings.SPRUCE, (new TreeDefinition()).addLogID(new BlockID(17, 1)).addLogID(new BlockID(17, 5)).addLogID(new BlockID(17, 9))
                .addLogID(new BlockID(17, 13)).addLeafID(new BlockID(18, 1)));
        masterLogList.addAll(treeDefs.get(Strings.SPRUCE).getLogList());
        
        // Vanilla birch definition
        treeDefs.put(Strings.BIRCH, (new TreeDefinition()).addLogID(new BlockID(17, 2)).addLogID(new BlockID(17, 6)).addLogID(new BlockID(17, 10))
                .addLogID(new BlockID(17, 14)).addLeafID(new BlockID(18, 2)));
        masterLogList.addAll(treeDefs.get(Strings.BIRCH).getLogList());
        
        // Vanilla jungle definition
        treeDefs.put(Strings.JUNGLE, (new TreeDefinition()).addLogID(new BlockID(17, 3)).addLogID(new BlockID(17, 7)).addLogID(new BlockID(17, 11))
                .addLogID(new BlockID(17, 15)).addLeafID(new BlockID(18, 3)).setMaxLeafBreakDist(5));
        masterLogList.addAll(treeDefs.get(Strings.JUNGLE).getLogList());
    }
    
    public void registerTree(String key, TreeDefinition td)
    {
        // TODO: fix this up to check for TreeDefinitions that have common logs
        // TODO: add the code to populate the logToStringMap
        for (BlockID blockID : td.getLogList())
            if (!masterLogList.contains(blockID))
                masterLogList.add(blockID);
        
        if (!isRegistered(key))
        {
            treeDefs.put(key, td);
        }
        else
        {
            TCLog.info("\"%S\" is already registered.  The new definition will be appended to the existing entry.", key);
            treeDefs.get(key).append(td);
        }
    }
    
    public boolean isRegistered(String key)
    {
        return treeDefs.containsKey(key);
    }
    
    public TreeDefinition get(String key)
    {
        if (isRegistered(key))
            return treeDefs.get(key);
        else
            return null;
    }
    
    public List<BlockID> getMasterLogList()
    {
        return new ArrayList<BlockID>(masterLogList);
    }
}
