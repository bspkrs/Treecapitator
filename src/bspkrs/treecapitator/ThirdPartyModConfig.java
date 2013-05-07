package bspkrs.treecapitator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
