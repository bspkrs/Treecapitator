package bspkrs.treecapitator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
        tagMap = new HashMap<String, String>();
    }
    
    public ThirdPartyModConfig setTagMap(Map<String, String> newTagMap)
    {
        tagMap.clear();
        tagMap.putAll(newTagMap);
        
        return this;
    }
    
    public void populateTreeDefsFromConfig()
    {
        for (Entry<String, ConfigTreeDefinition> e : configTreesMap.entrySet())
        {
            String key = e.getKey();
            ConfigTreeDefinition ctd = e.getValue();
            
            for (String logs : ctd.getConfigLogList());
        }
    }
    
    public String replaceThirdPartyBlockTags(String input)
    {
        for (String tag : tagMap.keySet())
            input = input.replace(tag, tagMap.get(tag));
        
        return input;
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
}
