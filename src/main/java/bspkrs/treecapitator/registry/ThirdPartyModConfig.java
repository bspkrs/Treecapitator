package bspkrs.treecapitator.registry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import bspkrs.treecapitator.TreecapitatorMod;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.util.Reference;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.ItemID;
import bspkrs.util.ListUtils;

public class ThirdPartyModConfig
{
    private final String                modID;
    private List<ItemID>                axeList;
    private List<ItemID>                shearsList;
    private boolean                     overrideIMC;
    private Map<String, TreeDefinition> treesMap;

    private boolean                     isChanged   = false;
    private static List<String>         orderedKeys = new ArrayList<String>();
    private static Set<String>          validKeys   = new HashSet<String>();

    /*
     * This special constructor provides the default vanilla tree "mod"
     */
    protected ThirdPartyModConfig(boolean init)
    {
        modID = Reference.MINECRAFT;
        overrideIMC = TCSettings.userConfigOverridesIMC;

        if (init)
        {
            axeList = new ArrayList<ItemID>(ToolRegistry.instance().vanillaAxeList());
            shearsList = new ArrayList<ItemID>(ToolRegistry.instance().vanillaShearsList());
            treesMap = TreeRegistry.instance().vanillaTrees();
        }
        else
        {
            axeList = new ArrayList<ItemID>();
            shearsList = new ArrayList<ItemID>();
            treesMap = new TreeMap<String, TreeDefinition>();
        }
    }

    protected ThirdPartyModConfig()
    {
        this(true);
    }

    protected ThirdPartyModConfig(String modID)
    {
        this.modID = modID;
        overrideIMC = TCSettings.userConfigOverridesIMC;
        axeList = new ArrayList<ItemID>();
        shearsList = new ArrayList<ItemID>();
        treesMap = new TreeMap<String, TreeDefinition>();
    }

    public void merge(ThirdPartyModConfig toMerge)
    {
        if (!modID.equals(toMerge.modID))
            throw new IllegalArgumentException(String.format("Cannot merge ThirdPartyModConfig objects with different modID values! this.modID: %s  toMerge.modID: %s", modID, toMerge.modID));

        overrideIMC = overrideIMC || toMerge.overrideIMC;

        for (ItemID itemID : toMerge.axeList)
            if (!axeList.contains(itemID))
                axeList.add(itemID);

        for (ItemID itemID : toMerge.shearsList)
            if (!shearsList.contains(itemID))
                shearsList.add(itemID);

        for (Entry<String, TreeDefinition> newEntry : toMerge.treesMap.entrySet())
        {
            if (treesMap.containsKey(newEntry.getKey()))
            {
                treesMap.get(newEntry.getKey()).appendWithSettings(newEntry.getValue());
                continue;
            }

            for (Entry<String, TreeDefinition> entry : treesMap.entrySet())
                if (newEntry.getValue().hasCommonLog(entry.getValue()))
                {
                    entry.getValue().appendWithSettings(newEntry.getValue());
                    continue;
                }

            treesMap.put(newEntry.getKey(), newEntry.getValue());
        }

        isChanged = true;
    }

    @SuppressWarnings("unchecked")
    public static boolean isValidNBT(NBTTagCompound tpModCfg)
    {
        for (String s : tpModCfg.getKeySet())
            if (!validKeys.contains(s))
                TCLog.warning("Unknown tag \"%s\" found while verifying a ThirdPartyModConfig NBTTagCompound object", s);

        if (!tpModCfg.hasKey(Reference.MOD_ID))
        {
            TCLog.severe("ThirdPartyModConfig NBTTagCompound objects must contain a string tag with the key \"%s\"", Reference.MOD_ID);
            return false;
        }

        if (tpModCfg.hasKey(Reference.TREES))
        {
            NBTTagList treeList = tpModCfg.getTagList(Reference.TREES, (byte) 10);
            for (int i = 0; i < treeList.tagCount(); i++)
                if (!TreeDefinition.isValidNBT(treeList.getCompoundTagAt(i)))
                    return false;
        }

        return true;
    }

    public static ThirdPartyModConfig readFromNBT(NBTTagCompound tpModCfg)
    {
        ThirdPartyModConfig tpmc = new ThirdPartyModConfig(tpModCfg.getString(Reference.MOD_ID));

        if (tpModCfg.hasKey(Reference.AXE_ID_LIST))
            for (ItemID itemID : ListUtils.getDelimitedStringAsItemIDList(tpModCfg.getString(Reference.AXE_ID_LIST), ";"))
                tpmc.addAxe(itemID);

        if (tpModCfg.hasKey(Reference.SHEARS_ID_LIST))
            for (ItemID itemID : ListUtils.getDelimitedStringAsItemIDList(tpModCfg.getString(Reference.SHEARS_ID_LIST), ";"))
                tpmc.addShears(itemID);

        if (tpModCfg.hasKey(Reference.TREES))
        {
            NBTTagList treeList = tpModCfg.getTagList(Reference.TREES, (byte) 10);
            for (int i = 0; i < treeList.tagCount(); i++)
            {
                NBTTagCompound tree = treeList.getCompoundTagAt(i);
                tpmc.addTreeDef(tree.getString(Reference.TREE_NAME), new TreeDefinition(tree));
            }
        }

        tpmc.isChanged = true;

        return tpmc;
    }

    public void writeToNBT(NBTTagCompound tpModCfg)
    {
        tpModCfg.setString(Reference.MOD_ID, modID);
        if (axeList.size() > 0)
            tpModCfg.setString(Reference.AXE_ID_LIST, ListUtils.getListAsDelimitedString(axeList, "; "));
        if (shearsList.size() > 0)
            tpModCfg.setString(Reference.SHEARS_ID_LIST, ListUtils.getListAsDelimitedString(shearsList, "; "));

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

    public static ThirdPartyModConfig readFromConfiguration(Configuration config, String category)
    {
        ConfigCategory cc = config.getCategory(category);
        ThirdPartyModConfig tpmc = new ThirdPartyModConfig(config.get(category, Reference.MOD_ID, Reference.MINECRAFT, null, Property.Type.MOD_ID)
                .setLanguageKey("bspkrs.tc.configgui." + Reference.MOD_ID).getString());
        if (cc.containsKey(Reference.AXE_ID_LIST))
            for (ItemID itemID : ListUtils.getDelimitedStringAsItemIDList(cc.get(Reference.AXE_ID_LIST).setLanguageKey("bspkrs.tc.configgui." + Reference.AXE_ID_LIST).getString(), ";"))
                tpmc.addAxe(itemID);
        if (cc.containsKey(Reference.SHEARS_ID_LIST))
            for (ItemID itemID : ListUtils.getDelimitedStringAsItemIDList(cc.get(Reference.SHEARS_ID_LIST).setLanguageKey("bspkrs.tc.configgui." + Reference.SHEARS_ID_LIST).getString(), ";"))
                tpmc.addShears(itemID);

        tpmc.overrideIMC = config.getBoolean(Reference.OVERRIDE_IMC, category, TCSettings.userConfigOverridesIMC, Reference.overrideIMCDesc,
                "bspkrs.tc.configgui." + Reference.OVERRIDE_IMC);

        TreecapitatorMod.proxy.setCategoryConfigEntryClass(config, category);
        cc.setPropertyOrder(orderedKeys);

        for (ConfigCategory ctgy : cc.getChildren())
            tpmc.addTreeDef(ctgy.getName(), new TreeDefinition(config, ctgy.getQualifiedName()));

        return tpmc;
    }

    public void writeToConfiguration(Configuration config, String category)
    {
        config.get(category, Reference.MOD_ID, modID, null, Property.Type.MOD_ID).setLanguageKey("bspkrs.tc.configgui." + Reference.MOD_ID);
        config.get(category, Reference.AXE_ID_LIST, ListUtils.getListAsDelimitedString(axeList, "; ")).setLanguageKey("bspkrs.tc.configgui." + Reference.AXE_ID_LIST);
        config.get(category, Reference.SHEARS_ID_LIST, ListUtils.getListAsDelimitedString(shearsList, "; ")).setLanguageKey("bspkrs.tc.configgui." + Reference.SHEARS_ID_LIST);
        config.getBoolean(Reference.OVERRIDE_IMC, category, overrideIMC, Reference.overrideIMCDesc, "bspkrs.tc.configgui." + Reference.OVERRIDE_IMC);

        for (Entry<String, TreeDefinition> e : treesMap.entrySet())
            if (!e.getKey().startsWith(category))
                e.getValue().writeToConfiguration(config, category + "." + e.getKey());
            else
                e.getValue().writeToConfiguration(config, e.getKey());

        TreecapitatorMod.proxy.setCategoryConfigEntryClass(config, category);
        config.setCategoryPropertyOrder(category, orderedKeys);

        isChanged = false;
    }

    public ThirdPartyModConfig addTreeDef(String key, TreeDefinition tree)
    {
        if (!treesMap.containsKey(key))
            treesMap.put(key, tree);
        else
            treesMap.get(key).appendWithSettings(tree);

        isChanged = true;
        return this;
    }

    public ThirdPartyModConfig addAxe(ItemID axe)
    {
        if (!axeList.contains(axe))
            axeList.add(axe);

        isChanged = true;
        return this;
    }

    public ThirdPartyModConfig addShears(ItemID shears)
    {
        if (!shearsList.contains(shears))
            shearsList.add(shears);

        isChanged = true;
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
        for (ItemID axe : axeList)
            if (!axe.id.trim().isEmpty())
                ToolRegistry.instance().registerAxe(axe);

        for (ItemID shears : shearsList)
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

    public boolean isChanged()
    {
        return isChanged;
    }

    static
    {
        orderedKeys.add(Reference.MOD_ID);
        orderedKeys.add(Reference.AXE_ID_LIST);
        orderedKeys.add(Reference.SHEARS_ID_LIST);
        orderedKeys.add(Reference.OVERRIDE_IMC);

        validKeys.add(Reference.MOD_ID);
        validKeys.add(Reference.AXE_ID_LIST);
        validKeys.add(Reference.SHEARS_ID_LIST);
        validKeys.add(Reference.OVERRIDE_IMC);
        validKeys.add(Reference.TREES);
    }
}
