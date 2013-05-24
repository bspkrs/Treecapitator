package bspkrs.treecapitator.fml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import bspkrs.treecapitator.ConfigTreeDefinition;
import bspkrs.treecapitator.Strings;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TCSettings;
import bspkrs.treecapitator.ToolRegistry;
import bspkrs.treecapitator.TreeDefinition;
import bspkrs.treecapitator.TreeRegistry;
import bspkrs.util.CommonUtils;
import bspkrs.util.ConfigCategory;
import bspkrs.util.Configuration;
import bspkrs.util.ItemID;
import bspkrs.util.ListUtils;
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
    private boolean                           overrideIMC;
    private Map<String, ConfigTreeDefinition> configTreesMap;
    private Map<String, TreeDefinition>       treesMap;
    private Map<String, String>               tagMap;
    
    /*
     * This special constructor provides the default vanilla tree "mod"
     */
    protected ThirdPartyModConfig()
    {
        modID = TreeCapitatorMod.metadata.modId;
        configPath = "TreeCapitator.cfg";
        blockKeys = "";
        itemKeys = "";
        axeKeys = ListUtils.getListAsDelimitedString(ToolRegistry.instance().vanillaAxeList(), "; ");
        shearsKeys = ListUtils.getListAsDelimitedString(ToolRegistry.instance().vanillaShearsList(), "; ");
        shiftIndex = false;
        overrideIMC = TCSettings.userConfigOverridesIMC;
        
        configTreesMap = TreeRegistry.instance().vanillaTrees();
        tagMap = new HashMap<String, String>();
        treesMap = new HashMap<String, TreeDefinition>();
        
        this.refreshTreeDefinitionsFromConfig();
    }
    
    public ThirdPartyModConfig(String modID, String configPath, String blockKeys, String itemKeys, String axeKeys, String shearsKeys, boolean shiftIndex)
    {
        this.modID = modID;
        this.configPath = configPath;
        this.blockKeys = blockKeys;
        this.itemKeys = itemKeys;
        this.axeKeys = axeKeys;
        this.shearsKeys = shearsKeys;
        this.shiftIndex = shiftIndex;
        overrideIMC = TCSettings.userConfigOverridesIMC;
        
        configTreesMap = new HashMap<String, ConfigTreeDefinition>();
        treesMap = new HashMap<String, TreeDefinition>();
    }
    
    public ThirdPartyModConfig(String modID, String configPath, String blockKeys)
    {
        this(modID, configPath, blockKeys, "", "", "", true);
    }
    
    public ThirdPartyModConfig(String modID, String configPath, String itemKeys, String axeKeys, String shearsKeys, boolean shiftIndex)
    {
        this(modID, configPath, "", itemKeys, axeKeys, shearsKeys, shiftIndex);
    }
    
    public ThirdPartyModConfig(Configuration config, String category)
    {
        this();
        readFromConfiguration(config, category);
    }
    
    public ThirdPartyModConfig(NBTTagCompound tpModCfg)
    {
        this();
        readFromNBT(tpModCfg);
    }
    
    public ThirdPartyModConfig readFromNBT(NBTTagCompound tpModCfg)
    {
        modID = tpModCfg.getString(Strings.MOD_ID);
        configPath = tpModCfg.getString(Strings.CONFIG_PATH);
        blockKeys = tpModCfg.getString(Strings.BLOCK_CFG_KEYS);
        itemKeys = tpModCfg.getString(Strings.ITEM_CFG_KEYS);
        axeKeys = tpModCfg.getString(Strings.AXE_ID_LIST);
        shearsKeys = tpModCfg.getString(Strings.SHEARS_ID_LIST);
        shiftIndex = tpModCfg.getBoolean(Strings.SHIFT_INDEX);
        
        configTreesMap = new HashMap<String, ConfigTreeDefinition>();
        
        NBTTagList treeList = tpModCfg.getTagList(Strings.TREES);
        
        for (int i = 0; i < treeList.tagCount(); i++)
        {
            NBTTagCompound tree = (NBTTagCompound) treeList.tagAt(i);
            this.addConfigTreeDef(tree.getString(Strings.TREE_NAME), new ConfigTreeDefinition(tree));
        }
        
        return this;
    }
    
    public void writeToNBT(NBTTagCompound tpModCfg)
    {
        tpModCfg.setName(modID);
        tpModCfg.setString(Strings.MOD_ID, modID);
        if (configPath.length() > 0)
            tpModCfg.setString(Strings.CONFIG_PATH, configPath);
        if (blockKeys.length() > 0)
            tpModCfg.setString(Strings.BLOCK_CFG_KEYS, blockKeys);
        if (itemKeys.length() > 0)
            tpModCfg.setString(Strings.ITEM_CFG_KEYS, itemKeys);
        if (axeKeys.length() > 0)
            tpModCfg.setString(Strings.AXE_ID_LIST, axeKeys);
        if (shearsKeys.length() > 0)
            tpModCfg.setString(Strings.SHEARS_ID_LIST, shearsKeys);
        tpModCfg.setBoolean(Strings.SHIFT_INDEX, shiftIndex);
        
        NBTTagList treeList = new NBTTagList();
        for (Entry<String, ConfigTreeDefinition> e : configTreesMap.entrySet())
        {
            NBTTagCompound tree = new NBTTagCompound();
            e.getValue().writeToNBT(tree);
            tree.setString(Strings.TREE_NAME, e.getKey());
            treeList.appendTag(tree);
        }
        
        tpModCfg.setTag(Strings.TREES, treeList);
    }
    
    public ThirdPartyModConfig readFromConfiguration(Configuration config, String category)
    {
        ConfigCategory cc = config.getCategory(category);
        modID = cc.get(Strings.MOD_ID).getString();
        configPath = cc.get(Strings.CONFIG_PATH).getString();
        if (cc.containsKey(Strings.BLOCK_CFG_KEYS))
            blockKeys = cc.get(Strings.BLOCK_CFG_KEYS).getString();
        if (cc.containsKey(Strings.ITEM_CFG_KEYS))
            itemKeys = cc.get(Strings.ITEM_CFG_KEYS).getString();
        if (cc.containsKey(Strings.AXE_ID_LIST))
            axeKeys = cc.get(Strings.AXE_ID_LIST).getString();
        if (cc.containsKey(Strings.SHEARS_ID_LIST))
            shearsKeys = cc.get(Strings.SHEARS_ID_LIST).getString();
        shiftIndex = cc.get(Strings.SHIFT_INDEX).getBoolean(true);
        if (cc.containsKey(Strings.OVERRIDE_IMC))
            overrideIMC = cc.get(Strings.OVERRIDE_IMC).getBoolean(TCSettings.userConfigOverridesIMC);
        
        configTreesMap = new HashMap<String, ConfigTreeDefinition>();
        
        for (String ctgy : config.getCategoryNames())
        {
            if (ctgy.indexOf(category + ".") != -1)
            {
                addConfigTreeDef(config.getCategory(ctgy).getName(), new ConfigTreeDefinition(config, ctgy));
            }
        }
        
        return this;
    }
    
    public void writeToConfiguration(Configuration config, String category)
    {
        config.get(category, Strings.MOD_ID, modID);
        if (configPath.length() > 0)
            config.get(category, Strings.CONFIG_PATH, configPath);
        if (blockKeys.length() > 0)
            config.get(category, Strings.BLOCK_CFG_KEYS, blockKeys);
        if (itemKeys.length() > 0)
            config.get(category, Strings.ITEM_CFG_KEYS, itemKeys);
        if (axeKeys.length() > 0)
            config.get(category, Strings.AXE_ID_LIST, axeKeys);
        if (shearsKeys.length() > 0)
            config.get(category, Strings.SHEARS_ID_LIST, shearsKeys);
        config.get(category, Strings.SHIFT_INDEX, shiftIndex);
        
        for (Entry<String, ConfigTreeDefinition> e : configTreesMap.entrySet())
            e.getValue().writeToConfiguration(config, category + "." + e.getKey());
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
    
    public ThirdPartyModConfig registerTrees()
    {
        if (configTreesMap.size() != treesMap.size())
            refreshTreeDefinitionsFromConfig();
        
        for (Entry<String, TreeDefinition> e : treesMap.entrySet())
            TreeRegistry.instance().registerTree(e.getKey(), e.getValue());
        
        return this;
    }
    
    public ThirdPartyModConfig registerTools()
    {
        String axeList = axeKeys;
        String shearsList = shearsKeys;
        for (Entry<String, String> e : tagMap.entrySet())
        {
            axeList = axeList.replace(e.getKey(), e.getValue());
            shearsList = shearsList.replace(e.getKey(), e.getValue());
        }
        
        for (ItemID axe : ListUtils.getDelimitedStringAsItemIDList(axeList, ";"))
            ToolRegistry.instance().registerAxe(axe);
        
        for (ItemID shears : ListUtils.getDelimitedStringAsItemIDList(shearsList, ";"))
            ToolRegistry.instance().registerShears(shears);
        
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
    
    public boolean overrideIMC()
    {
        return overrideIMC;
    }
    
    public void setOverrideIMC(boolean bol)
    {
        overrideIMC = bol;
    }
    
    public void refreshTreeDefinitionsFromConfig()
    {
        treesMap.clear();
        
        for (Entry<String, ConfigTreeDefinition> e : configTreesMap.entrySet())
            treesMap.put(e.getKey(), e.getValue().getTagsReplacedTreeDef(tagMap));
    }
    
    protected void refreshReplacementTags()
    {
        tagMap = new HashMap<String, String>();
        
        TCLog.debug("Processing Mod \"%s\" config file \"%s\"...", modID, configPath);
        
        if (Loader.isModLoaded(modID))
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
        if (keys.length() > 0)
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
