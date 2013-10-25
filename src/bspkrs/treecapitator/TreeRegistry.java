package bspkrs.treecapitator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import bspkrs.util.BlockID;
import bspkrs.util.Coord;

public class TreeRegistry
{
    private Map<String, TreeDefinition>       treeDefs;
    private Map<BlockID, String>              logToStringMap;
    private TreeDefinition                    masterDefinition;
    private Map<String, ConfigTreeDefinition> vanTrees;
    private Set<BlockID>                      blacklist;
    private Set<Coord>                        blocksBeingChopped;
    
    private static TreeRegistry               instance;
    
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
        blacklist = new HashSet<BlockID>();
        blocksBeingChopped = new HashSet<Coord>();
    }
    
    protected void initVanillaTreeDefs()
    {
        vanTrees = new TreeMap<String, ConfigTreeDefinition>();
        vanTrees.put(Strings.OAK, new ConfigTreeDefinition().addLogID(new BlockID(17, 0)).addLogID(new BlockID(17, 4))
                .addLogID(new BlockID(17, 8)).addLogID(new BlockID(17, 12))
                .addLeafID(new BlockID(18, 0)).addLeafID(new BlockID(18, 8)));
        vanTrees.put(Strings.SPRUCE, new ConfigTreeDefinition().addLogID(new BlockID(17, 1)).addLogID(new BlockID(17, 5))
                .addLogID(new BlockID(17, 9)).addLogID(new BlockID(17, 13))
                .addLeafID(new BlockID(18, 1)).addLeafID(new BlockID(18, 9)));
        vanTrees.put(Strings.BIRCH, new ConfigTreeDefinition().addLogID(new BlockID(17, 2)).addLogID(new BlockID(17, 6))
                .addLogID(new BlockID(17, 10)).addLogID(new BlockID(17, 14))
                .addLeafID(new BlockID(18, 2)).addLeafID(new BlockID(18, 10)));
        vanTrees.put(Strings.JUNGLE, new ConfigTreeDefinition().addLogID(new BlockID(17, 3)).addLogID(new BlockID(17, 7))
                .addLogID(new BlockID(17, 11)).addLogID(new BlockID(17, 15))
                .addLeafID(new BlockID(18, 3)).addLeafID(new BlockID(18, 11))
                .addLeafID(new BlockID(18, 0)).addLeafID(new BlockID(18, 8))
                .setMaxHorLeafBreakDist(6).setRequireLeafDecayCheck(false).setUseAdvancedTopLogLogic(true));
        vanTrees.put(Strings.MUSH_BROWN, new ConfigTreeDefinition().addLogID(new BlockID(99, 10)).addLogID(new BlockID(99, 15))
                .addLeafID(new BlockID(99, 1)).addLeafID(new BlockID(99, 2))
                .addLeafID(new BlockID(99, 3)).addLeafID(new BlockID(99, 4))
                .addLeafID(new BlockID(99, 5)).addLeafID(new BlockID(99, 6))
                .addLeafID(new BlockID(99, 7)).addLeafID(new BlockID(99, 8))
                .addLeafID(new BlockID(99, 9)).addLeafID(new BlockID(99, 14))
                .setMaxHorLeafBreakDist(6).setRequireLeafDecayCheck(false));
        vanTrees.put(Strings.MUSH_RED, new ConfigTreeDefinition().addLogID(new BlockID(100, 10)).addLogID(new BlockID(100, 15))
                .addLeafID(new BlockID(100, 1)).addLeafID(new BlockID(100, 2))
                .addLeafID(new BlockID(100, 3)).addLeafID(new BlockID(100, 4))
                .addLeafID(new BlockID(100, 5)).addLeafID(new BlockID(100, 6))
                .addLeafID(new BlockID(100, 7)).addLeafID(new BlockID(100, 8))
                .addLeafID(new BlockID(100, 9)).addLeafID(new BlockID(100, 14))
                .setMaxHorLeafBreakDist(6).setRequireLeafDecayCheck(false));
    }
    
    protected void registerVanillaTreeDefs()
    {
        for (Entry<String, ConfigTreeDefinition> e : vanTrees.entrySet())
            this.registerTree(e.getKey(), e.getValue());
    }
    
    /**
     * Registers the given tree definition. If the new key already exists the existing definition is updated. If the new definition contains
     * a log that is already part of a tree, the existing definition is merged into the new definition.
     * 
     * @param newKey
     * @param newTD
     */
    public void registerTree(String newKey, TreeDefinition newTD)
    {
        // Do NOT register null tree defs!
        if (newTD != null)
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
                else if (logToStringMap.containsKey(blockID) && !sharedLogTrees.contains(logToStringMap.get(blockID)))
                    // Whoa! this BlockID isn't new, we need to do some merging
                    sharedLogTrees.add(logToStringMap.get(blockID));
            
            if (!newKey.trim().isEmpty() && !isRegistered(newKey) && sharedLogTrees.size() == 0)
            {
                // New definition all around.  Easy.
                TCLog.debug("Tree Definition \"%s\" is new.  Proceeding to insert new key.", newKey);
                // insert newTD
                treeDefs.put(newKey, newTD);
                // update logToStringMap
                logToStringMap.putAll(toAdd);
            }
            else if (!newKey.trim().isEmpty())
            {
                if (sharedLogTrees.size() > 0)
                {
                    // merge all shared log TreeDefinition objects with our new TreeDefinition
                    for (String existingKey : sharedLogTrees)
                    {
                        TCLog.debug("Tree Definition \"%s\" contains a log that is registered with an existing tree (%s).  " +
                                "The existing definition will be merged with the new tree.", newKey, existingKey);
                        
                        // append the existing definition to our new definition
                        newTD.appendWithSettings(treeDefs.remove(existingKey));
                    }
                    
                    // insert newTD
                    treeDefs.put(newKey, newTD);
                    
                    // update logToStringMap for all logs in the new definition
                    for (BlockID blockID : newTD.getLogList())
                        logToStringMap.put(blockID, newKey);
                }
                else
                { // A tree is defined for that key; append the new definition to the existing tree
                    TCLog.debug("\"%s\" is already registered.  The new definition will be appended to the existing entry.", newKey);
                    treeDefs.get(newKey).appendWithSettings(newTD);
                    logToStringMap.putAll(toAdd);
                }
            }
            else
            /* newKey is empty! */
            {
                if (sharedLogTrees.size() == 1)
                {
                    String existingTree = sharedLogTrees.remove(0);
                    // append the new def to the existing tree
                    treeDefs.get(existingTree).appendWithSettings(newTD);
                    
                    // update logToStringMap
                    for (BlockID log : newTD.getLogList())
                        if (!logToStringMap.containsKey(log))
                            logToStringMap.put(log, existingTree);
                    
                    // update the master def
                    masterDefinition.append(newTD);
                    
                }
                else
                {
                    TCLog.warning("TreeDefinition cannot be registered with an empty Key.");
                    return;
                }
            }
            // Update our master tree definition
            
            if (!newKey.trim().isEmpty())
                masterDefinition.append(treeDefs.get(newKey));
        }
        else
            TCLog.warning("TreeDefinition cannot be null when registering a tree!");
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
        Set<Integer> processed = new HashSet<Integer>();
        
        for (BlockID log : masterDefinition.logBlocks)
        {
            if (!processed.contains(log.id))
            {
                processed.add(log.id);
                r += "," + log.id;
            }
        }
        
        return r.replaceFirst(",", "");
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
        if (!blacklist.contains(log))
            return masterDefinition.isLogBlock(log);
        else
            return false;
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
        {
            if (TCSettings.useStrictBlockPairing)
            {
                String treeKey = logToStringMap.get(blockID);
                TreeDefinition treeDef = get(logToStringMap.get(blockID));
                
                if (treeDef != null)
                {
                    return treeDef;
                }
                else
                {
                    if (blockID.metadata > -1)
                    {
                        treeKey = logToStringMap.get(new BlockID(blockID.id));
                        return get(treeKey);
                    }
                }
            }
            return masterDefinition;
        }
        else
            return null;
    }
    
    public Set<BlockID> masterLogList()
    {
        return masterDefinition.getLogList();
    }
    
    public Set<BlockID> masterLeafList()
    {
        return masterDefinition.getLeafList();
    }
    
    public Map<String, ConfigTreeDefinition> vanillaTrees()
    {
        return new TreeMap<String, ConfigTreeDefinition>(vanTrees);
    }
    
    public Set<BlockID> blacklist()
    {
        return new HashSet<BlockID>(blacklist);
    }
    
    // This must be done after all trees are registered to avoid screwing up the registration process
    public void readBlacklistFromDelimitedString(String dList)
    {
        blacklist = TCUtils.getDelimitedStringAsBlockIDHashSet(dList, ";");
    }
    
    protected void readFromNBT(NBTTagCompound ntc)
    {
        // treeDefs;
        treeDefs = new HashMap<String, TreeDefinition>();
        NBTTagList l = ntc.getTagList(Strings.TREE_DEFS);
        for (int i = 0; i < l.tagCount(); i++)
        {
            NBTTagCompound treeNBT = (NBTTagCompound) l.tagAt(i);
            treeDefs.put(treeNBT.getString(Strings.TREE_NAME), new TreeDefinition(treeNBT));
        }
        
        // logToStringMap;
        logToStringMap = new HashMap<BlockID, String>();
        l = ntc.getTagList(Strings.LOG_STR_MAP);
        for (int i = 0; i < l.tagCount(); i++)
        {
            NBTTagString s = (NBTTagString) l.tagAt(i);
            String[] t = s.data.split("=");
            logToStringMap.put(new BlockID(t[0], ","), t[1]);
        }
        
        // masterDefinition;
        masterDefinition = new TreeDefinition(ntc.getCompoundTag(Strings.MASTER_DEF));
        
        // blacklist
        blacklist = TCUtils.getDelimitedStringAsBlockIDHashSet(ntc.getString(Strings.BLACKLIST), ";");
    }
    
    public void writeToNBT(NBTTagCompound ntc)
    {
        // treeDefs
        NBTTagList trees = new NBTTagList();
        trees.setName(Strings.TREE_DEFS);
        for (Entry<String, TreeDefinition> e : treeDefs.entrySet())
        {
            NBTTagCompound tree = new NBTTagCompound();
            e.getValue().writeToNBT(tree);
            tree.setString(Strings.TREE_NAME, e.getKey());
            trees.appendTag(tree);
        }
        ntc.setTag(Strings.TREE_DEFS, trees);
        
        //        logToStringMap;   
        NBTTagList entries = new NBTTagList();
        entries.setName(Strings.LOG_STR_MAP);
        for (Entry<BlockID, String> e : logToStringMap.entrySet())
        {
            NBTTagString s = new NBTTagString("", e.getKey() + "=" + e.getValue());
            entries.appendTag(s);
        }
        ntc.setTag(Strings.LOG_STR_MAP, entries);
        
        //        masterDefinition;
        NBTTagCompound md = new NBTTagCompound();
        masterDefinition.writeToNBT(md);
        ntc.setTag(Strings.MASTER_DEF, md);
        
        // blacklist
        ntc.setString(Strings.BLACKLIST, TCUtils.getSetAsDelimitedString(blacklist, ";"));
    }
}
