package bspkrs.treecapitator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import bspkrs.treecapitator.fml.IDResolverMapping;
import bspkrs.treecapitator.fml.IDResolverMappingList;
import bspkrs.util.CommonUtils;
import bspkrs.util.Configuration;
import cpw.mods.fml.common.Loader;

public class ThirdPartyModConfig
{
    private String                            modID;
    private String                            configPath;
    private String                            blockKeys;
    private String                            itemKeys;
    private String                            axeKeys;
    private String                            shearsKeys;
    private boolean                           shiftIndex;
    private Map<String, ConfigTreeDefinition> configTreesMap;
    private Map<String, TreeDefinition>       treesMap;
    private Map<String, String>               tagMap;
    
    public ThirdPartyModConfig(String modID, String configPath, String blockKeys, String itemKeys, boolean shiftIndex)
    {
        this.modID = modID;
        this.configPath = configPath;
        this.blockKeys = blockKeys;
        this.itemKeys = itemKeys;
        this.shiftIndex = shiftIndex;
        
        configTreesMap = new HashMap<String, ConfigTreeDefinition>();
        treesMap = new HashMap<String, TreeDefinition>();
        refreshReplacementTags();
    }
    
    public ThirdPartyModConfig readFromNBT(NBTTagCompound treeDefNBT)
    {
        /*if (treeDefNBT.hasKey(Strings.onlyDestroyUpwards))
            onlyDestroyUpwards = treeDefNBT.getBoolean(Strings.onlyDestroyUpwards);
        if (treeDefNBT.hasKey(Strings.requireLeafDecayCheck))
            requireLeafDecayCheck = treeDefNBT.getBoolean(Strings.requireLeafDecayCheck);
        if (treeDefNBT.hasKey(Strings.maxHorLogBreakDist))
            maxLogBreakDist = treeDefNBT.getInteger(Strings.maxHorLogBreakDist);
        if (treeDefNBT.hasKey(Strings.maxLeafBreakDist))
            maxLeafBreakDist = treeDefNBT.getInteger(Strings.maxLeafBreakDist);
        if (treeDefNBT.hasKey(Strings.maxLeafIDDist))
            maxLeafIDDist = treeDefNBT.getInteger(Strings.maxLeafIDDist);
        if (treeDefNBT.hasKey(Strings.minLeavesToID))
            minLeavesToID = treeDefNBT.getInteger(Strings.minLeavesToID);
        if (treeDefNBT.hasKey(Strings.breakSpeedModifier))
            breakSpeedModifier = treeDefNBT.getFloat(Strings.breakSpeedModifier);
        
        logBlocks = new ArrayList<BlockID>();
        leafBlocks = new ArrayList<BlockID>();
        
        NBTTagList logList = treeDefNBT.getTagList(Strings.LOGS);
        
        for (int i = 0; i < logList.tagCount(); i++)
        {
            NBTTagCompound log = (NBTTagCompound) logList.tagAt(i);
            logBlocks.add(new BlockID(log.getInteger(Strings.id), log.getInteger(Strings.metadata)));
        }
        
        if (treeDefNBT.hasKey(Strings.LEAVES))
        {
            NBTTagList leafList = treeDefNBT.getTagList(Strings.LEAVES);
            
            for (int i = 0; i < leafList.tagCount(); i++)
            {
                NBTTagCompound leaf = (NBTTagCompound) leafList.tagAt(i);
                leafBlocks.add(new BlockID(leaf.getInteger(Strings.id), leaf.getInteger(Strings.metadata)));
            }
        }*/
        
        return this;
    }
    
    public void writeToNBT(NBTTagCompound treeDefNBT)
    {
        /*treeDefNBT.setBoolean(Strings.onlyDestroyUpwards, onlyDestroyUpwards);
        treeDefNBT.setBoolean(Strings.requireLeafDecayCheck, requireLeafDecayCheck);
        treeDefNBT.setInteger(Strings.maxHorLogBreakDist, maxLogBreakDist);
        treeDefNBT.setInteger(Strings.maxLeafBreakDist, maxLeafBreakDist);
        treeDefNBT.setInteger(Strings.maxLeafIDDist, maxLeafIDDist);
        treeDefNBT.setInteger(Strings.minLeavesToID, minLeavesToID);
        treeDefNBT.setFloat(Strings.breakSpeedModifier, breakSpeedModifier);
        
        NBTTagList logList = new NBTTagList();
        
        for (BlockID logBlock : logBlocks)
        {
            NBTTagCompound log = new NBTTagCompound();
            log.setInteger(Strings.id, logBlock.id);
            log.setInteger(Strings.metadata, logBlock.metadata);
            logList.appendTag(log);
        }
        
        treeDefNBT.setTag(Strings.LOGS, logList);
        
        if (leafBlocks.size() > 0)
        {
            NBTTagList leafList = new NBTTagList();
            
            for (BlockID leafBlock : leafBlocks)
            {
                NBTTagCompound leaf = new NBTTagCompound();
                leaf.setInteger(Strings.id, leafBlock.id);
                leaf.setInteger(Strings.metadata, leafBlock.metadata);
                leafList.appendTag(leaf);
            }
            
            treeDefNBT.setTag(Strings.LEAVES, leafList);
        }*/
    }
    
    public ThirdPartyModConfig addConfigTreeDef(String key, ConfigTreeDefinition tree)
    {
        if (!configTreesMap.containsKey(key))
            configTreesMap.put(key, tree);
        else
            TCLog.warning("Mod %s attempted to add two tree configs with the same name: %s", modID, key);
        
        return this;
    }
    
    public ThirdPartyModConfig addTreeDef(String key, TreeDefinition tree)
    {
        if (!treesMap.containsKey(key))
            treesMap.put(key, tree);
        else
            TCLog.warning("Mod %s attempted to add two tree definitions with the same id: %s", modID, key);
        
        return this;
    }
    
    public void registerAllTrees()
    {
        for (Entry<String, TreeDefinition> e : treesMap.entrySet())
            TreeRegistry.instance().registerTree(e.getKey(), e.getValue());
    }
    
    public void registerLawnTools()
    {   
        
    }
    
    public String modID()
    {
        return modID;
    }
    
    public String configPath()
    {
        return configPath;
    }
    
    public String blockKeys()
    {
        return blockKeys;
    }
    
    public boolean shiftIndex()
    {
        return shiftIndex;
    }
    
    public void refreshTreeDefinitionsFromConfig()
    {
        treesMap.clear();
        
        for (Entry<String, ConfigTreeDefinition> e : configTreesMap.entrySet())
            treesMap.put(e.getKey(), e.getValue().getTagsReplacedTreeDef(tagMap));
    }
    
    private void refreshReplacementTags()
    {
        tagMap = new HashMap<String, String>();
        
        TCLog.debug("Processing Mod \"%s\" config file \"%s\"...", modID, configPath);
        
        if (Loader.instance().isModLoaded(modID))
        {
            File file = new File(Loader.instance().getConfigDir(), configPath.trim());
            if (file.exists())
            {
                Configuration thirdPartyConfig = new Configuration(file);
                String idrClassName = Loader.instance().getIndexedModList().get(modID).getMod().getClass().getName();
                thirdPartyConfig.load();
                getReplacementTagsForKeys(thirdPartyConfig, blockKeys, idrClassName, false);
                getReplacementTagsForKeys(thirdPartyConfig, itemKeys, idrClassName, true);
            }
            else
                TCLog.warning("Mod config file %s does not exist when processing Mod %s.", configPath, modID);
        }
        else
            TCLog.debug("Mod " + modID + " is not loaded.");
        
    }
    
    private void getReplacementTagsForKeys(Configuration thirdPartyConfig, String keys, String idrClassName, boolean isItemList)
    {
        for (String configID : keys.trim().split(";"))
        {
            String[] subString = configID.trim().split(":");
            String configValue = thirdPartyConfig.get(/* ctgy */subString[0].trim(), /* prop name */subString[1].trim(), 0).getString();
            String tagID = "<" + subString[0].trim() + ":" + subString[1].trim() + ">";
            
            if (!tagMap.containsKey(tagID))
            {
                // TCLog.debug("configValue: %s", configValue);
                IDResolverMapping mapping = IDResolverMappingList.instance().getMappingForModAndOldID(idrClassName, CommonUtils.parseInt(configValue));
                
                if (mapping != null)
                    configValue = String.valueOf(mapping.newID);
                // TCLog.debug("configValue: %s", configValue);
                
                if (isItemList && shiftIndex)
                    configValue = String.valueOf(CommonUtils.parseInt(configValue, -256) + 256);
                
                // TCLog.debug("configValue: %s", configValue);
                
                if (!configValue.equals("0"))
                {
                    tagMap.put(tagID, configValue);
                    TCLog.debug("Third Party Mod Config Tag %s will map to %s for mod %s", tagID, configValue, modID);
                }
            }
            else
                TCLog.warning("Duplicate Third Party Config Tag detected: " + tagID + " is already mapped to " + tagMap.get(tagID));
        }
    }
}
