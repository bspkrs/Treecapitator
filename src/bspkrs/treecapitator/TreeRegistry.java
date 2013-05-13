package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bspkrs.util.BlockID;
import bspkrs.util.Coord;

public class TreeRegistry
{
    private Map<String, TreeDefinition> treeDefs;
    private Map<BlockID, String>        logToStringMap;
    private TreeDefinition              masterDefinition;
    private ConfigTreeDefinition        vanOak;
    private ConfigTreeDefinition        vanSpruce;
    private ConfigTreeDefinition        vanBirch;
    private ConfigTreeDefinition        vanJungle;
    private ConfigTreeDefinition        vanMushBrown;
    private ConfigTreeDefinition        vanMushRed;
    private List<Coord>                 blocksBeingChopped;
    
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
        
        initMapsAndLists();
        initVanillaTreeDefs();
    }
    
    protected void initMapsAndLists()
    {
        treeDefs = new HashMap<String, TreeDefinition>();
        logToStringMap = new HashMap<BlockID, String>();
        masterDefinition = new TreeDefinition();
        blocksBeingChopped = new ArrayList<Coord>();
    }
    
    protected void initVanillaTreeDefs()
    {
        vanOak = new ConfigTreeDefinition().addLogID(new BlockID(17, 0)).addLogID(new BlockID(17, 4))
                .addLogID(new BlockID(17, 8)).addLogID(new BlockID(17, 12))
                .addLeafID(new BlockID(18, 0)).addLeafID(new BlockID(18, 8));
        vanSpruce = new ConfigTreeDefinition().addLogID(new BlockID(17, 1)).addLogID(new BlockID(17, 5))
                .addLogID(new BlockID(17, 9)).addLogID(new BlockID(17, 13))
                .addLeafID(new BlockID(18, 1)).addLeafID(new BlockID(18, 9));
        vanBirch = new ConfigTreeDefinition().addLogID(new BlockID(17, 2)).addLogID(new BlockID(17, 6))
                .addLogID(new BlockID(17, 10)).addLogID(new BlockID(17, 14))
                .addLeafID(new BlockID(18, 2)).addLeafID(new BlockID(18, 10));
        vanJungle = new ConfigTreeDefinition().addLogID(new BlockID(17, 3)).addLogID(new BlockID(17, 7))
                .addLogID(new BlockID(17, 11)).addLogID(new BlockID(17, 15))
                .addLeafID(new BlockID(18, 3)).addLeafID(new BlockID(18, 11))
                .addLeafID(new BlockID(18, 0)).addLeafID(new BlockID(18, 8))
                .setMaxLeafBreakDist(6).setRequireLeafDecayCheck(false);
        vanMushBrown = new ConfigTreeDefinition().addLogID(new BlockID(99, 10)).addLogID(new BlockID(99, 15))
                .addLeafID(new BlockID(99, 1)).addLeafID(new BlockID(99, 2))
                .addLeafID(new BlockID(99, 3)).addLeafID(new BlockID(99, 4))
                .addLeafID(new BlockID(99, 5)).addLeafID(new BlockID(99, 6))
                .addLeafID(new BlockID(99, 7)).addLeafID(new BlockID(99, 8))
                .addLeafID(new BlockID(99, 9)).addLeafID(new BlockID(99, 14))
                .setMaxLeafBreakDist(6).setRequireLeafDecayCheck(false);
        vanMushRed = new ConfigTreeDefinition().addLogID(new BlockID(100, 10)).addLogID(new BlockID(100, 15))
                .addLeafID(new BlockID(100, 1)).addLeafID(new BlockID(100, 2))
                .addLeafID(new BlockID(100, 3)).addLeafID(new BlockID(100, 4))
                .addLeafID(new BlockID(100, 5)).addLeafID(new BlockID(100, 6))
                .addLeafID(new BlockID(100, 7)).addLeafID(new BlockID(100, 8))
                .addLeafID(new BlockID(100, 9)).addLeafID(new BlockID(100, 14))
                .setMaxLeafBreakDist(6).setRequireLeafDecayCheck(false);
    }
    
    protected void registerVanillaTreeDefs()
    {
        registerTree(Strings.OAK, vanOak);
        registerTree(Strings.SPRUCE, vanSpruce);
        registerTree(Strings.BIRCH, vanBirch);
        registerTree(Strings.JUNGLE, vanJungle);
        registerTree(Strings.MUSH_BROWN, vanMushBrown);
        registerTree(Strings.MUSH_RED, vanMushRed);
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
                // build the toAdd map of new log keys
                toAdd.put(blockID, newKey);
            }
            else if (!sharedLogTrees.contains(logToStringMap.get(blockID)))
                // Whoa! this BlockID isn't new, we need to do some merging
                sharedLogTrees.add(logToStringMap.get(blockID));
        
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
                    newTD.appendWithSettings(treeDefs.remove(existingKey));
                }
                
                // update logToStringMap for all logs in the new definition
                for (BlockID blockID : newTD.getLogList())
                    logToStringMap.put(blockID, newKey);
            }
            else
            { // A tree is defined for that key; append the new definition to the existing tree
                TCLog.info("\"%s\" is already registered.  The new definition will be appended to the existing entry.", newKey);
                treeDefs.get(newKey).appendWithSettings(newTD);
                logToStringMap.putAll(toAdd);
            }
        }
        
        // Update our master tree definition
        masterDefinition.append(treeDefs.get(newKey));
    }
    
    public boolean trackTreeChopEventAt(Coord c)
    {
        if (!blocksBeingChopped.contains(c))
        {
            blocksBeingChopped.add(c);
            return true;
        }
        return false;
    }
    
    public void endTreeChopEventAt(Coord c)
    {
        if (blocksBeingChopped.contains(c))
            blocksBeingChopped.remove(c);
    }
    
    /**
     * Gets a comma-delimited string with all generic log IDs (no metadata).
     * 
     * @return
     */
    public String getMultiMineExclusionString()
    {
        String r = "";
        List<Integer> processed = new ArrayList<Integer>();
        
        for (BlockID log : masterDefinition.logBlocks)
        {
            if (!processed.contains(log.id))
            {
                processed.add(log.id);
                r += ", " + log.id;
            }
        }
        
        return r.replaceFirst(", ", "");
    }
    
    public TreeDefinition masterDefinition()
    {
        return masterDefinition;
    }
    
    /**
     * Checks if a given tree definition key has been previously defined.
     * 
     * @param key
     * @return
     */
    public boolean isRegistered(String key)
    {
        return treeDefs.containsKey(key);
    }
    
    /**
     * Checks if a given BlockID has been registered with any tree.
     * 
     * @param log
     * @return
     */
    public boolean isRegistered(BlockID log)
    {
        return masterDefinition.isLogBlock(log);
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
            if (TCSettings.useStrictBlockPairing)
                return get(logToStringMap.get(blockID));
            else
                return masterDefinition;
        else
            return null;
    }
    
    public List<BlockID> masterLogList()
    {
        return masterDefinition.getLogList();
    }
    
    public List<BlockID> masterLeafList()
    {
        return masterDefinition.getLeafList();
    }
    
    public TreeDefinition vanillaOak()
    {
        return vanOak;
    }
}
