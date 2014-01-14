package bspkrs.treecapitator.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import bspkrs.helpers.nbt.NBTTagCompoundHelper;
import bspkrs.helpers.nbt.NBTTagListHelper;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.util.TCConst;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.BlockID;
import bspkrs.util.Coord;
import bspkrs.util.ListUtils;

public class TreeRegistry
{
    private Map<String, TreeDefinition> treeDefs;
    private Map<BlockID, String>        logToStringMap;
    private TreeDefinition              masterDefinition;
    private Map<String, TreeDefinition> vanTrees;
    private List<BlockID>               blacklist;
    private Set<Coord>                  blocksBeingChopped;
    
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
        blacklist = new ArrayList<BlockID>();
        blocksBeingChopped = new HashSet<Coord>();
    }
    
    protected void initVanillaTreeDefs()
    {
        vanTrees = new TreeMap<String, TreeDefinition>();
        vanTrees.put(TCConst.OAK, new TreeDefinition().addLogID(new BlockID(Blocks.log, 0)).addLogID(new BlockID(Blocks.log, 4))
                .addLogID(new BlockID(Blocks.log, 8)).addLogID(new BlockID(Blocks.log, 12))
                .addLeafID(new BlockID(Blocks.leaves, 0)).addLeafID(new BlockID(Blocks.leaves, 8)));
        vanTrees.put(TCConst.SPRUCE, new TreeDefinition().addLogID(new BlockID(Blocks.log, 1)).addLogID(new BlockID(Blocks.log, 5))
                .addLogID(new BlockID(Blocks.log, 9)).addLogID(new BlockID(Blocks.log, 13))
                .addLeafID(new BlockID(Blocks.leaves, 1)).addLeafID(new BlockID(Blocks.leaves, 9)));
        vanTrees.put(TCConst.BIRCH, new TreeDefinition().addLogID(new BlockID(Blocks.log, 2)).addLogID(new BlockID(Blocks.log, 6))
                .addLogID(new BlockID(Blocks.log, 10)).addLogID(new BlockID(Blocks.log, 14))
                .addLeafID(new BlockID(Blocks.leaves, 2)).addLeafID(new BlockID(Blocks.leaves, 10)));
        vanTrees.put(TCConst.JUNGLE, new TreeDefinition().addLogID(new BlockID(Blocks.log, 3)).addLogID(new BlockID(Blocks.log, 7))
                .addLogID(new BlockID(Blocks.log, 11)).addLogID(new BlockID(Blocks.log, 15))
                .addLeafID(new BlockID(Blocks.leaves, 3)).addLeafID(new BlockID(Blocks.leaves, 11))
                .addLeafID(new BlockID(Blocks.leaves, 0)).addLeafID(new BlockID(Blocks.leaves, 8))
                .setMaxHorLeafBreakDist(6).setRequireLeafDecayCheck(false).setUseAdvancedTopLogLogic(true));
        vanTrees.put(TCConst.ACACIA, new TreeDefinition().addLogID(new BlockID(Blocks.log2, 0)).addLogID(new BlockID(Blocks.log2, 4))
                .addLogID(new BlockID(Blocks.log2, 8)).addLogID(new BlockID(Blocks.log2, 12))
                .addLeafID(new BlockID(Blocks.leaves2, 0)).addLeafID(new BlockID(Blocks.leaves2, 8))
                .setUseAdvancedTopLogLogic(true));
        vanTrees.put(TCConst.DARK_OAK, new TreeDefinition().addLogID(new BlockID(Blocks.log2, 1)).addLogID(new BlockID(Blocks.log2, 5))
                .addLogID(new BlockID(Blocks.log2, 9)).addLogID(new BlockID(Blocks.log2, 13))
                .addLeafID(new BlockID(Blocks.leaves2, 1)).addLeafID(new BlockID(Blocks.leaves2, 9))
                .setUseAdvancedTopLogLogic(true));
        vanTrees.put(TCConst.FUTURE_TREE_1, new TreeDefinition().addLogID(new BlockID(Blocks.log2, 2)).addLogID(new BlockID(Blocks.log2, 6))
                .addLogID(new BlockID(Blocks.log2, 10)).addLogID(new BlockID(Blocks.log2, 14))
                .addLeafID(new BlockID(Blocks.leaves2, 2)).addLeafID(new BlockID(Blocks.leaves2, 10)));
        vanTrees.put(TCConst.FUTURE_TREE_2, new TreeDefinition().addLogID(new BlockID(Blocks.log2, 3)).addLogID(new BlockID(Blocks.log2, 7))
                .addLogID(new BlockID(Blocks.log2, 11)).addLogID(new BlockID(Blocks.log2, 15))
                .addLeafID(new BlockID(Blocks.leaves2, 3)).addLeafID(new BlockID(Blocks.leaves2, 11)));
        vanTrees.put(TCConst.MUSH_BROWN, new TreeDefinition().addLogID(new BlockID(Blocks.brown_mushroom_block, 10)).addLogID(new BlockID(Blocks.brown_mushroom_block, 15))
                .addLeafID(new BlockID(Blocks.brown_mushroom_block, 1)).addLeafID(new BlockID(Blocks.brown_mushroom_block, 2))
                .addLeafID(new BlockID(Blocks.brown_mushroom_block, 3)).addLeafID(new BlockID(Blocks.brown_mushroom_block, 4))
                .addLeafID(new BlockID(Blocks.brown_mushroom_block, 5)).addLeafID(new BlockID(Blocks.brown_mushroom_block, 6))
                .addLeafID(new BlockID(Blocks.brown_mushroom_block, 7)).addLeafID(new BlockID(Blocks.brown_mushroom_block, 8))
                .addLeafID(new BlockID(Blocks.brown_mushroom_block, 9)).addLeafID(new BlockID(Blocks.brown_mushroom_block, 14))
                .setMaxHorLeafBreakDist(6).setRequireLeafDecayCheck(false));
        vanTrees.put(TCConst.MUSH_RED, new TreeDefinition().addLogID(new BlockID(Blocks.red_mushroom_block, 10)).addLogID(new BlockID(Blocks.red_mushroom_block, 15))
                .addLeafID(new BlockID(Blocks.red_mushroom_block, 1)).addLeafID(new BlockID(Blocks.red_mushroom_block, 2))
                .addLeafID(new BlockID(Blocks.red_mushroom_block, 3)).addLeafID(new BlockID(Blocks.red_mushroom_block, 4))
                .addLeafID(new BlockID(Blocks.red_mushroom_block, 5)).addLeafID(new BlockID(Blocks.red_mushroom_block, 6))
                .addLeafID(new BlockID(Blocks.red_mushroom_block, 7)).addLeafID(new BlockID(Blocks.red_mushroom_block, 8))
                .addLeafID(new BlockID(Blocks.red_mushroom_block, 9)).addLeafID(new BlockID(Blocks.red_mushroom_block, 14))
                .setMaxHorLeafBreakDist(6).setRequireLeafDecayCheck(false));
    }
    
    protected void registerVanillaTreeDefs()
    {
        for (Entry<String, TreeDefinition> e : vanTrees.entrySet())
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
                TCLog.debug("    New key %s added with values: %s", newKey, newTD.toString());
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
                    TCLog.debug("    New key %s added with values: %s", newKey, newTD.toString());
                    
                    // update logToStringMap for all logs in the new definition
                    for (BlockID blockID : newTD.getLogList())
                        logToStringMap.put(blockID, newKey);
                }
                else
                { // A tree is defined for that key; append the new definition to the existing tree
                    TCLog.debug("\"%s\" is already registered.  The new definition will be appended to the existing entry.", newKey);
                    treeDefs.get(newKey).appendWithSettings(newTD);
                    TCLog.debug("    Key %s appended with values: %s", newKey, newTD.toString());
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
                    TCLog.debug("    Blank key tree appended with values: %s", newTD.toString());
                    
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
        Set<String> processed = new HashSet<String>();
        
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
    
    public List<BlockID> masterLogList()
    {
        return masterDefinition.getLogList();
    }
    
    public List<BlockID> masterLeafList()
    {
        return masterDefinition.getLeafList();
    }
    
    public Map<String, TreeDefinition> vanillaTrees()
    {
        return new TreeMap<String, TreeDefinition>(vanTrees);
    }
    
    public List<BlockID> blacklist()
    {
        return new ArrayList<BlockID>(blacklist);
    }
    
    // This must be done after all trees are registered to avoid screwing up the registration process
    public void readBlacklistFromDelimitedString(String dList)
    {
        blacklist = ListUtils.getDelimitedStringAsBlockIDList(dList, ";");
    }
    
    protected void readFromNBT(NBTTagCompound ntc)
    {
        // treeDefs;
        treeDefs = new HashMap<String, TreeDefinition>();
        NBTTagList l = NBTTagCompoundHelper.getTagList(ntc, TCConst.TREE_DEFS, (byte) 10);
        for (int i = 0; i < l.tagCount(); i++)
        {
            NBTTagCompound treeNBT = NBTTagListHelper.getCompoundTagAt(l, i);;
            treeDefs.put(treeNBT.getString(TCConst.TREE_NAME), new TreeDefinition(treeNBT));
        }
        
        // logToStringMap;
        logToStringMap = new HashMap<BlockID, String>();
        l = NBTTagCompoundHelper.getTagList(ntc, TCConst.LOG_STR_MAP, (byte) 8);
        for (int i = 0; i < l.tagCount(); i++)
        {
            String s = NBTTagListHelper.getStringTagAt(l, i);
            String[] t = s.split("=");
            logToStringMap.put(new BlockID(t[0], ","), t[1]);
        }
        
        // masterDefinition;
        masterDefinition = new TreeDefinition(ntc.getCompoundTag(TCConst.MASTER_DEF));
        
        // blacklist
        blacklist = ListUtils.getDelimitedStringAsBlockIDList(ntc.getString(TCConst.BLACKLIST), ";");
    }
    
    public void writeToNBT(NBTTagCompound ntc)
    {
        // treeDefs
        NBTTagList trees = new NBTTagList();
        for (Entry<String, TreeDefinition> e : treeDefs.entrySet())
        {
            NBTTagCompound tree = new NBTTagCompound();
            e.getValue().writeToNBT(tree);
            tree.setString(TCConst.TREE_NAME, e.getKey());
            trees.appendTag(tree);
        }
        ntc.setTag(TCConst.TREE_DEFS, trees);
        
        //        logToStringMap;   
        NBTTagList entries = new NBTTagList();
        for (Entry<BlockID, String> e : logToStringMap.entrySet())
        {
            NBTTagString s = new NBTTagString(e.getKey() + "=" + e.getValue());
            entries.appendTag(s);
        }
        ntc.setTag(TCConst.LOG_STR_MAP, entries);
        
        //        masterDefinition;
        NBTTagCompound md = new NBTTagCompound();
        masterDefinition.writeToNBT(md);
        ntc.setTag(TCConst.MASTER_DEF, md);
        
        // blacklist
        ntc.setString(TCConst.BLACKLIST, ListUtils.getListAsDelimitedString(blacklist, ";"));
    }
}
