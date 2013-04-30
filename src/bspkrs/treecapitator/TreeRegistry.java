package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bspkrs.util.BlockID;

public class TreeRegistry
{
    private Map<String, TreeDefinition> treeDefs;
    private Map<BlockID, String>        logToStringMap;
    private List<BlockID>               masterLogList;
    private List<BlockID>               masterLeafList;
    private TreeDefinition              genericDefinition;
    
    private static TreeRegistry         instance;
    
    public static TreeRegistry instance()
    {
        if (instance == null)
            new TreeRegistry();
        
        return instance;
    }
    
    protected TreeRegistry()
    {
        instance = this;
        
        treeDefs = new HashMap<String, TreeDefinition>();
        masterLogList = new ArrayList<BlockID>();
        masterLeafList = new ArrayList<BlockID>();
        genericDefinition = new TreeDefinition();
        
        // Vanilla oak definition
        treeDefs.put(Strings.OAK, (new TreeDefinition()).addLogID(new BlockID(17, 0)).addLogID(new BlockID(17, 4)).addLogID(new BlockID(17, 8))
                .addLogID(new BlockID(17, 12)).addLeafID(new BlockID(18, 0)));
        masterLogList.addAll(treeDefs.get(Strings.OAK).getLogList());
        for (BlockID leaf : treeDefs.get(Strings.OAK).getLeafList())
            if (!masterLeafList.contains(leaf))
                masterLeafList.add(leaf);
        
        // Vanilla spruce definition
        treeDefs.put(Strings.SPRUCE, (new TreeDefinition()).addLogID(new BlockID(17, 1)).addLogID(new BlockID(17, 5)).addLogID(new BlockID(17, 9))
                .addLogID(new BlockID(17, 13)).addLeafID(new BlockID(18, 1)));
        masterLogList.addAll(treeDefs.get(Strings.SPRUCE).getLogList());
        for (BlockID leaf : treeDefs.get(Strings.SPRUCE).getLeafList())
            if (!masterLeafList.contains(leaf))
                masterLeafList.add(leaf);
        
        // Vanilla birch definition
        treeDefs.put(Strings.BIRCH, (new TreeDefinition()).addLogID(new BlockID(17, 2)).addLogID(new BlockID(17, 6)).addLogID(new BlockID(17, 10))
                .addLogID(new BlockID(17, 14)).addLeafID(new BlockID(18, 2)));
        masterLogList.addAll(treeDefs.get(Strings.BIRCH).getLogList());
        for (BlockID leaf : treeDefs.get(Strings.BIRCH).getLeafList())
            if (!masterLeafList.contains(leaf))
                masterLeafList.add(leaf);
        
        // Vanilla jungle definition
        treeDefs.put(Strings.JUNGLE, (new TreeDefinition()).addLogID(new BlockID(17, 3)).addLogID(new BlockID(17, 7)).addLogID(new BlockID(17, 11))
                .addLogID(new BlockID(17, 15)).addLeafID(new BlockID(18, 3)).setMaxLeafBreakDist(5));
        masterLogList.addAll(treeDefs.get(Strings.JUNGLE).getLogList());
        for (BlockID leaf : treeDefs.get(Strings.JUNGLE).getLeafList())
            if (!masterLeafList.contains(leaf))
                masterLeafList.add(leaf);
        
        refreshGenericDefinition();
    }
    
    /**
     * Registers the given tree definition. If the new key already exists the existing definition is updated. If the new definition contains
     * a log that is already part of a tree, the existing definition is merged into the new definition.
     * 
     * TreeRegistry.instance().updateGenericDefinition() should be called once all trees are registered.
     * 
     * @param newKey
     * @param newTD
     */
    public void registerTree(String newKey, TreeDefinition newTD)
    {
        // list of trees that have at least one log blockID common with this tree
        List<String> sharedLogTrees = new LinkedList<String>();
        // logToStringMap entries to add
        Map<BlockID, String> toAdd = new HashMap<BlockID, String>();
        
        // Check each log to see if an existing definition already uses it
        for (BlockID blockID : newTD.getLogList())
            if (!isRegistered(blockID))
            {
                // never seen this log, add to masterLogList
                masterLogList.add(blockID);
                // build the toAdd map of new log keys
                toAdd.put(blockID, newKey);
            }
            else if (!sharedLogTrees.contains(logToStringMap.get(blockID)))
                // Whoa! this BlockID isn't new, we need to do some merging
                sharedLogTrees.add(logToStringMap.get(blockID));
        
        // Update masterLeafList with any leaf IDs it doesn't have
        for (BlockID blockID : newTD.getLeafList())
            if (!masterLeafList.contains(blockID))
                masterLeafList.add(blockID);
        
        if (!isRegistered(newKey) && sharedLogTrees.size() == 0)
        { // New definition all around.  Easy.
            TCLog.info("Tree Definition \"%s\" is new.  Proceeding to insert new key.", newKey);
            treeDefs.put(newKey, newTD);
            logToStringMap.putAll(toAdd);
        }
        else
        {
            if (sharedLogTrees.size() > 0)
            {
                // merge all shared log TreeDefinition objects with our new TreeDefinition
                for (String existingKey : sharedLogTrees)
                {
                    TCLog.info("Tree Definition \"%s\" contains a log that is registered with an existing tree (%s).  " +
                            "The existing definition will be merged with the new tree.", newKey, existingKey);
                    
                    // append the existing definition to our new definition
                    newTD.append(treeDefs.remove(existingKey));
                }
                
                // update logToStringMap for all logs in the new definition
                for (BlockID blockID : newTD.getLogList())
                    logToStringMap.put(blockID, newKey);
            }
            else
            { // A tree is defined for that key; append the new definition to the existing tree
                TCLog.info("\"%s\" is already registered.  The new definition will be appended to the existing entry.", newKey);
                treeDefs.get(newKey).append(newTD);
                logToStringMap.putAll(toAdd);
            }
        }
    }
    
    /**
     * Updates the genericDefinition private object based on masterLogList and masterLeafList. Should be called once all tree definitions
     * are registered.
     */
    public void refreshGenericDefinition()
    {
        genericDefinition = new TreeDefinition(masterLogList, masterLeafList);
    }
    
    public TreeDefinition genericDefinition()
    {
        return this.genericDefinition;
    }
    
    public boolean isRegistered(String key)
    {
        return treeDefs.containsKey(key);
    }
    
    public boolean isRegistered(BlockID log)
    {
        return masterLogList.contains(log);
    }
    
    public TreeDefinition get(String key)
    {
        if (isRegistered(key))
            return treeDefs.get(key);
        else
            return null;
    }
    
    public TreeDefinition get(BlockID blockID)
    {
        if (isRegistered(blockID))
            return get(logToStringMap.get(blockID));
        else
            return null;
    }
    
    public List<BlockID> masterLogList()
    {
        return new ArrayList<BlockID>(masterLogList);
    }
    
    public List<BlockID> masterLeafList()
    {
        return new ArrayList<BlockID>(masterLeafList);
    }
}
