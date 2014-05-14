package bspkrs.treecapitator.registry;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import bspkrs.helpers.nbt.NBTTagCompoundHelper;
import bspkrs.helpers.nbt.NBTTagListHelper;
import bspkrs.treecapitator.TreecapitatorMod;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.fml.gui.GuiConfigCustomCategoryListEntry;
import bspkrs.treecapitator.util.Reference;
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
    private Map<String, TreeDefinition> treesMap;
    
    /*
     * This special constructor provides the default vanilla tree "mod"
     */
    protected ThirdPartyModConfig(boolean init)
    {
        modID = TreecapitatorMod.metadata.modId;
        overrideIMC = TCSettings.userConfigOverridesIMC;
        
        if (init)
        {
            axeKeys = ListUtils.getListAsDelimitedString(ToolRegistry.instance().vanillaAxeList(), "; ");
            shearsKeys = ListUtils.getListAsDelimitedString(ToolRegistry.instance().vanillaShearsList(), "; ");
            treesMap = TreeRegistry.instance().vanillaTrees();
        }
        else
        {
            axeKeys = "";
            shearsKeys = "";
            treesMap = new TreeMap<String, TreeDefinition>();
        }
    }
    
    protected ThirdPartyModConfig()
    {
        this(true);
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
    
    public void merge(ThirdPartyModConfig toMerge)
    {
        if (!this.modID.equals(toMerge.modID))
            throw new IllegalArgumentException(String.format("Cannot merge ThirdPartyModConfig objects with different modID values! this.modID: %s  toMerge.modID: %s", this.modID, toMerge.modID));
        
        this.overrideIMC = this.overrideIMC || toMerge.overrideIMC;
        
        if (!toMerge.axeKeys.trim().isEmpty())
        {
            String axes = this.axeKeys.trim();
            
            if (!axes.isEmpty() && axes.endsWith(";"))
            {
                axes = axes.substring(0, axes.length() - 1);
            }
            
            for (String entry : toMerge.axeKeys.trim().split(";"))
                if (!axes.contains(entry.trim()))
                    axes += "; " + entry.trim();
            
            if (axes.startsWith(";"))
                axes = axes.substring(1).trim();
            
            this.axeKeys = axes;
        }
        
        if (!toMerge.shearsKeys.trim().isEmpty())
        {
            String shears = this.shearsKeys.trim();
            
            if (!shears.isEmpty() && shears.endsWith(";"))
            {
                shears = shears.substring(0, shears.length() - 1);
            }
            
            for (String entry : toMerge.shearsKeys.trim().split(";"))
                if (!shears.contains(entry.trim()))
                    shears += "; " + entry.trim();
            
            if (shears.startsWith(";"))
                shears = shears.substring(1).trim();
            
            this.shearsKeys = shears;
        }
        
        for (Entry<String, TreeDefinition> newEntry : toMerge.treesMap.entrySet())
        {
            if (this.treesMap.containsKey(newEntry.getKey()))
            {
                this.treesMap.get(newEntry.getKey()).appendWithSettings(newEntry.getValue());
                continue;
            }
            
            for (Entry<String, TreeDefinition> entry : this.treesMap.entrySet())
                if (newEntry.getValue().hasCommonLog(entry.getValue()))
                {
                    entry.getValue().appendWithSettings(newEntry.getValue());
                    continue;
                }
            
            this.treesMap.put(newEntry.getKey(), newEntry.getValue());
        }
    }
    
    public ThirdPartyModConfig readFromNBT(NBTTagCompound tpModCfg)
    {
        modID = tpModCfg.getString(Reference.MOD_ID);
        if (tpModCfg.hasKey(Reference.AXE_ID_LIST))
            axeKeys = tpModCfg.getString(Reference.AXE_ID_LIST);
        if (tpModCfg.hasKey(Reference.SHEARS_ID_LIST))
            shearsKeys = tpModCfg.getString(Reference.SHEARS_ID_LIST);
        
        treesMap = new TreeMap<String, TreeDefinition>();
        
        NBTTagList treeList = NBTTagCompoundHelper.getTagList(tpModCfg, Reference.TREES, (byte) 10);
        
        for (int i = 0; i < treeList.tagCount(); i++)
        {
            NBTTagCompound tree = NBTTagListHelper.getCompoundTagAt(treeList, i);
            this.addTreeDef(tree.getString(Reference.TREE_NAME), new TreeDefinition(tree));
        }
        
        return this;
    }
    
    public void writeToNBT(NBTTagCompound tpModCfg)
    {
        tpModCfg.setString(Reference.MOD_ID, modID);
        if (axeKeys.length() > 0)
            tpModCfg.setString(Reference.AXE_ID_LIST, axeKeys);
        if (shearsKeys.length() > 0)
            tpModCfg.setString(Reference.SHEARS_ID_LIST, shearsKeys);
        
        NBTTagList treeList = new NBTTagList();
        for (Entry<String, TreeDefinition> e : treesMap.entrySet())
        {
            NBTTagCompound tree = new NBTTagCompound();
            e.getValue().writeToNBT(tree);
            tree.setString(Reference.TREE_NAME, e.getKey());
            treeList.appendTag(tree);
        }
        
        tpModCfg.setTag(Reference.TREES, treeList);
    }
    
    public ThirdPartyModConfig readFromConfiguration(Configuration config, String category)
    {
        ConfigCategory cc = config.getCategory(category);
        cc.setCustomIGuiConfigListEntryClass(GuiConfigCustomCategoryListEntry.class);
        modID = config.get(category, Reference.MOD_ID, modID, (String) null, Property.Type.MOD_ID)
                .setLanguageKey("bspkrs.tc.configgui." + Reference.MOD_ID).getString();
        if (cc.containsKey(Reference.AXE_ID_LIST))
            axeKeys = cc.get(Reference.AXE_ID_LIST).setLanguageKey("bspkrs.tc.configgui." + Reference.AXE_ID_LIST).getString();
        if (cc.containsKey(Reference.SHEARS_ID_LIST))
            shearsKeys = cc.get(Reference.SHEARS_ID_LIST).setLanguageKey("bspkrs.tc.configgui." + Reference.SHEARS_ID_LIST).getString();
        
        overrideIMC = config.getBoolean(Reference.OVERRIDE_IMC, category, TCSettings.userConfigOverridesIMC, Reference.overrideIMCDesc,
                "bspkrs.tc.configgui." + Reference.OVERRIDE_IMC);
        
        treesMap = new TreeMap<String, TreeDefinition>();
        
        for (ConfigCategory ctgy : cc.getChildren())
        {
            addTreeDef(ctgy.getName(), new TreeDefinition(config, ctgy.getQualifiedName()));
        }
        
        return this;
    }
    
    public void writeToConfiguration(Configuration config, String category)
    {
        config.get(category, Reference.MOD_ID, modID, (String) null, Property.Type.MOD_ID).setLanguageKey("bspkrs.tc.configgui." + Reference.MOD_ID);
        config.get(category, Reference.AXE_ID_LIST, axeKeys).setLanguageKey("bspkrs.tc.configgui." + Reference.AXE_ID_LIST);
        config.get(category, Reference.SHEARS_ID_LIST, shearsKeys).setLanguageKey("bspkrs.tc.configgui." + Reference.SHEARS_ID_LIST);
        config.getBoolean(Reference.OVERRIDE_IMC, category, overrideIMC, Reference.overrideIMCDesc, "bspkrs.tc.configgui." + Reference.OVERRIDE_IMC);
        
        for (Entry<String, TreeDefinition> e : treesMap.entrySet())
            if (!e.getKey().startsWith(category))
                e.getValue().writeToConfiguration(config, category + "." + e.getKey());
            else
                e.getValue().writeToConfiguration(config, e.getKey());
        
        config.setCategoryCustomIGuiConfigListEntryClass(category, GuiConfigCustomCategoryListEntry.class);
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
        for (Entry<String, TreeDefinition> e : treesMap.entrySet())
            TreeRegistry.instance().registerTree(e.getKey(), e.getValue());
        
        return this;
    }
    
    public ThirdPartyModConfig registerTools()
    {
        for (ItemID axe : ListUtils.getDelimitedStringAsItemIDList(axeKeys, ";"))
            if (!axe.id.trim().isEmpty())
                ToolRegistry.instance().registerAxe(axe);
        
        for (ItemID shears : ListUtils.getDelimitedStringAsItemIDList(shearsKeys, ";"))
            if (!shears.id.trim().isEmpty())
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
}
