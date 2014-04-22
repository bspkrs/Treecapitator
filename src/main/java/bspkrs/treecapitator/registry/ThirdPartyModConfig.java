package bspkrs.treecapitator.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import bspkrs.helpers.nbt.NBTTagCompoundHelper;
import bspkrs.helpers.nbt.NBTTagListHelper;
import bspkrs.treecapitator.TreecapitatorMod;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.util.TCConst;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.ItemID;
import bspkrs.util.ListUtils;
import bspkrs.util.config.ConfigCategory;
import bspkrs.util.config.Configuration;
import bspkrs.util.config.Property;

public class ThirdPartyModConfig
{
    private String                      modID;
    private String                      axeKeys;
    private String                      shearsKeys;
    private boolean                     overrideIMC;
    private Map<String, TreeDefinition> configTreesMap;
    private Map<String, TreeDefinition> treesMap;
    private Map<String, String>         tagMap;
    
    /*
     * This special constructor provides the default vanilla tree "mod"
     */
    protected ThirdPartyModConfig(boolean init)
    {
        modID = TreecapitatorMod.metadata.modId;
        overrideIMC = TCSettings.userConfigOverridesIMC;
        
        tagMap = new HashMap<String, String>();
        treesMap = new TreeMap<String, TreeDefinition>();
        
        if (init)
        {
            axeKeys = ListUtils.getListAsDelimitedString(ToolRegistry.instance().vanillaAxeList(), "; ");
            shearsKeys = ListUtils.getListAsDelimitedString(ToolRegistry.instance().vanillaShearsList(), "; ");
            configTreesMap = TreeRegistry.instance().vanillaTrees();
            refreshTreeDefinitionsFromConfig();
        }
        else
        {
            axeKeys = "";
            shearsKeys = "";
            configTreesMap = new TreeMap<String, TreeDefinition>();
        }
    }
    
    protected ThirdPartyModConfig()
    {
        this(true);
    }
    
    public ThirdPartyModConfig(String modID, String configPath, String axeKeys, String shearsKeys)
    {
        this.modID = modID;
        this.axeKeys = axeKeys;
        this.shearsKeys = shearsKeys;
        overrideIMC = TCSettings.userConfigOverridesIMC;
        
        configTreesMap = new TreeMap<String, TreeDefinition>();
        treesMap = new TreeMap<String, TreeDefinition>();
    }
    
    public ThirdPartyModConfig(String modID, String configPath)
    {
        this(modID, configPath, "", "");
    }
    
    public ThirdPartyModConfig(Configuration config, String category)
    {
        this(false);
        readFromConfiguration(config, category);
    }
    
    public ThirdPartyModConfig(NBTTagCompound tpModCfg)
    {
        this(false);
        readFromNBT(tpModCfg);
    }
    
    public ThirdPartyModConfig readFromNBT(NBTTagCompound tpModCfg)
    {
        modID = tpModCfg.getString(TCConst.MOD_ID);
        if (tpModCfg.hasKey(TCConst.AXE_ID_LIST))
            axeKeys = tpModCfg.getString(TCConst.AXE_ID_LIST);
        if (tpModCfg.hasKey(TCConst.SHEARS_ID_LIST))
            shearsKeys = tpModCfg.getString(TCConst.SHEARS_ID_LIST);
        
        configTreesMap = new TreeMap<String, TreeDefinition>();
        
        NBTTagList treeList = NBTTagCompoundHelper.getTagList(tpModCfg, TCConst.TREES, (byte) 10);
        
        for (int i = 0; i < treeList.tagCount(); i++)
        {
            NBTTagCompound tree = NBTTagListHelper.getCompoundTagAt(treeList, i);
            this.addConfigTreeDef(tree.getString(TCConst.TREE_NAME), new TreeDefinition(tree));
        }
        
        return this;
    }
    
    public void writeToNBT(NBTTagCompound tpModCfg)
    {
        tpModCfg.setString(TCConst.MOD_ID, modID);
        if (axeKeys.length() > 0)
            tpModCfg.setString(TCConst.AXE_ID_LIST, axeKeys);
        if (shearsKeys.length() > 0)
            tpModCfg.setString(TCConst.SHEARS_ID_LIST, shearsKeys);
        
        NBTTagList treeList = new NBTTagList();
        for (Entry<String, TreeDefinition> e : configTreesMap.entrySet())
        {
            NBTTagCompound tree = new NBTTagCompound();
            e.getValue().writeToNBT(tree);
            tree.setString(TCConst.TREE_NAME, e.getKey());
            treeList.appendTag(tree);
        }
        
        tpModCfg.setTag(TCConst.TREES, treeList);
    }
    
    public ThirdPartyModConfig readFromConfiguration(Configuration config, String category)
    {
        ConfigCategory cc = config.getCategory(category);
        modID = cc.get(TCConst.MOD_ID).getString();
        if (cc.containsKey(TCConst.AXE_ID_LIST))
            axeKeys = cc.get(TCConst.AXE_ID_LIST).getString();
        if (cc.containsKey(TCConst.SHEARS_ID_LIST))
            shearsKeys = cc.get(TCConst.SHEARS_ID_LIST).getString();
        
        overrideIMC = config.getBoolean(TCConst.OVERRIDE_IMC, category, TCSettings.userConfigOverridesIMC, TCConst.overrideIMCDesc);
        
        configTreesMap = new TreeMap<String, TreeDefinition>();
        
        for (String ctgy : config.getCategoryNames())
        {
            if (ctgy.indexOf(category + ".") != -1)
            {
                addConfigTreeDef(config.getCategory(ctgy).getQualifiedName(), new TreeDefinition(config, ctgy));
            }
        }
        
        return this;
    }
    
    public void writeToConfiguration(Configuration config, String category)
    {
        Property temp;
        temp = config.get(category, TCConst.MOD_ID, modID);
        temp.setLanguageKey("bspkrs.tc.configgui." + TCConst.MOD_ID);
        if (axeKeys.length() > 0)
        {
            temp = config.get(category, TCConst.AXE_ID_LIST, axeKeys);
            temp.setLanguageKey("bspkrs.tc.configgui." + TCConst.AXE_ID_LIST);
        }
        if (shearsKeys.length() > 0)
        {
            temp = config.get(category, TCConst.SHEARS_ID_LIST, shearsKeys);
            temp.setLanguageKey("bspkrs.tc.configgui." + TCConst.SHEARS_ID_LIST);
        }
        
        config.getBoolean(TCConst.OVERRIDE_IMC, category, overrideIMC, TCConst.overrideIMCDesc, "bspkrs.tc.configgui." + TCConst.OVERRIDE_IMC);
        
        for (Entry<String, TreeDefinition> e : configTreesMap.entrySet())
            if (!e.getKey().startsWith(category))
                e.getValue().writeToConfiguration(config, category + "." + e.getKey());
            else
                e.getValue().writeToConfiguration(config, e.getKey());
    }
    
    public ThirdPartyModConfig addConfigTreeDef(String key, TreeDefinition tree)
    {
        if (!configTreesMap.containsKey(key))
            configTreesMap.put(key, tree);
        else
        {
            TCLog.warning("Mod %s attempted to add two tree configs with the same name: %s", modID, key);
            configTreesMap.get(key).append(tree);
        }
        
        return this;
    }
    
    public ThirdPartyModConfig addTreeDef(String key, TreeDefinition tree)
    {
        if (!treesMap.containsKey(key))
            treesMap.put(key, tree);
        else
        {
            TCLog.warning("Mod %s attempted to add two tree definitions with the same id: %s", modID, key);
            treesMap.get(key).append(tree);
        }
        
        return this;
    }
    
    public ThirdPartyModConfig registerTrees()
    {
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
            if (!axe.id.equals(""))
                ToolRegistry.instance().registerAxe(axe);
        
        for (ItemID shears : ListUtils.getDelimitedStringAsItemIDList(shearsList, ";"))
            if (!shears.id.equals(""))
                ToolRegistry.instance().registerShears(shears);
        
        return this;
    }
    
    public String modID()
    {
        return modID;
    }
    
    public boolean overrideIMC()
    {
        return overrideIMC;
    }
    
    public ThirdPartyModConfig setOverrideIMC(boolean bol)
    {
        overrideIMC = bol;
        return this;
    }
    
    public ThirdPartyModConfig refreshTreeDefinitionsFromConfig()
    {
        treesMap.clear();
        
        for (Entry<String, TreeDefinition> e : configTreesMap.entrySet())
            treesMap.put(e.getKey(), e.getValue());
        
        return this;
    }
}
