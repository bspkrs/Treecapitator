package bspkrs.treecapitator.registry;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import bspkrs.treecapitator.config.TCConfigHandler;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.forge.OreDictionaryHandler;
import bspkrs.treecapitator.util.Reference;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.BlockID;
import bspkrs.util.ItemID;
import bspkrs.util.ModulusBlockID;
import net.minecraftforge.fml.common.Loader;

public class ModConfigRegistry
{
    private static ModConfigRegistry         instance;

    private Map<String, ThirdPartyModConfig> userModCfgs;
    private Map<String, ThirdPartyModConfig> imcModCfgs;
    private Map<String, ThirdPartyModConfig> defaultModCfgs;

    private boolean                          isChanged = false;

    public static ModConfigRegistry instance()
    {
        if (instance == null)
            new ModConfigRegistry();

        return instance;
    }

    protected ModConfigRegistry()
    {
        instance = this;

        userModCfgs = new HashMap<String, ThirdPartyModConfig>();
        imcModCfgs = new HashMap<String, ThirdPartyModConfig>();

        initDefaultModConfigs();
    }

    public boolean isChanged()
    {
        return isChanged;
    }

    public void registerUserModConfig(ThirdPartyModConfig tpmc)
    {
        TCLog.debug("Registering user mod config %s", tpmc.modID());
        if (!userModCfgs.containsKey(tpmc.modID()))
            userModCfgs.put(tpmc.modID(), tpmc);
        else
        {
            TCLog.info("User config contains multiple 3rd party mod configs for mod %s. These entries will be merged.", tpmc.modID());
            userModCfgs.get(tpmc.modID()).merge(tpmc);
        }

        this.isChanged = true;
    }

    public void registerIMCModConfig(String sendingMod, ThirdPartyModConfig tpmc)
    {
        TCLog.debug("Registering IMC mod config %s sent by %s", tpmc.modID(), sendingMod);
        if (!imcModCfgs.containsKey(tpmc.modID()))
            imcModCfgs.put(tpmc.modID(), tpmc.setOverrideIMC(false));
        else
        {
            TCLog.info("Multiple IMC messages sent for mod %s. The new message will be merged with previous messages.", tpmc.modID());
            imcModCfgs.get(tpmc.modID()).merge(tpmc);
        }

        this.isChanged = true;
    }

    public void appendTreeToModConfig(String modID, String treeName, TreeDefinition treeDef)
    {
        if (userModCfgs.containsKey(modID))
            userModCfgs.get(modID).addTreeDef(treeName, treeDef);
        else
        {
            ThirdPartyModConfig tpmc = new ThirdPartyModConfig(modID);
            tpmc.addTreeDef(treeName, treeDef);
            userModCfgs.put(modID, tpmc);
        }

        this.isChanged = true;
    }

    public void appendAxeToModConfig(String modID, ItemID axe)
    {
        if (userModCfgs.containsKey(modID))
            userModCfgs.get(modID).addAxe(axe);
        else
        {
            ThirdPartyModConfig tpmc = new ThirdPartyModConfig(modID);
            tpmc.addAxe(axe);
            userModCfgs.put(modID, tpmc);
        }

        this.isChanged = true;
    }

    public void applyPrioritizedModConfigs()
    {
        List<ThirdPartyModConfig> finalList = new LinkedList<ThirdPartyModConfig>();

        TCLog.info("Prioritizing User and IMC mod configs...");
        for (Entry<String, ThirdPartyModConfig> e : imcModCfgs.entrySet())
            if (!userModCfgs.containsKey(e.getKey()) || !userModCfgs.get(e.getKey()).overrideIMC())
            {
                finalList.add(e.getValue());
                TCLog.debug("IMC mod config loaded for %s.", e.getValue().modID());
                if (TCSettings.saveIMCConfigsToFile)
                    writeToConfigFile(e.getValue());
            }

        for (Entry<String, ThirdPartyModConfig> e : userModCfgs.entrySet())
            if (!imcModCfgs.containsKey(e.getKey()) || e.getValue().overrideIMC())
            {
                finalList.add(e.getValue());
                TCLog.debug("User mod config loaded for %s.", e.getValue().modID());
            }

        TCLog.info("Registering items and trees...");
        TreeRegistry.instance().initMapsAndLists();
        ToolRegistry.instance().initLists();
        for (ThirdPartyModConfig cfg : finalList)
            cfg.registerTools().registerTrees();

        if (OreDictionaryHandler.instance().generateAndRegisterOreDictionaryTreeDefinitions())
            writeToConfigFile(userModCfgs.get(Reference.MINECRAFT));

        this.isChanged = false;
        TCConfigHandler.instance().getConfig().save();
        TCConfigHandler.instance().setShouldRefreshRegistries(true);
    }

    public Map<String, ThirdPartyModConfig> defaultConfigs()
    {
        return new TreeMap<String, ThirdPartyModConfig>(defaultModCfgs);
    }

    public void writeChangesToConfig(Configuration config)
    {
        if (this.isChanged)
            for (Entry<String, ThirdPartyModConfig> entry : userModCfgs.entrySet())
                if (entry.getValue().isChanged())
                    this.writeToConfigFile(entry.getValue());

        this.isChanged = false;
        config.save();
    }

    /**
     * Gets user configs from the config object
     */
    public void syncConfig(Configuration config)
    {
        userModCfgs = new HashMap<String, ThirdPartyModConfig>();
        /*
         * Get / Set 3rd Party Mod configs
         */
        TCSettings.userConfigOverridesIMC = config.getBoolean("userConfigOverridesIMC", Reference.CTGY_TREE_MOD_CFG,
                TCSettings.userConfigOverridesIMCDefault, Reference.userConfigOverridesIMCDesc, "bspkrs.tc.configgui.userConfigOverridesIMC");
        TCSettings.saveIMCConfigsToFile = config.getBoolean("saveIMCConfigsToFile", Reference.CTGY_TREE_MOD_CFG,
                TCSettings.saveIMCConfigsToFileDefault, Reference.saveIMCConfigsToFileDesc, "bspkrs.tc.configgui.saveIMCConfigsToFile");

        TCLog.configs(config, Reference.CTGY_TREE_MOD_CFG);

        config.setCategoryComment(Reference.CTGY_TREE_MOD_CFG, Reference.TREE_MOD_CFG_CTGY_DESC);
        config.setCategoryLanguageKey(Reference.CTGY_TREE_MOD_CFG, "bspkrs.tc.configgui.ctgy." + Reference.CTGY_TREE_MOD_CFG);

        if (!config.hasCategory(Reference.CTGY_TREE_MOD_CFG + "." + Reference.CTGY_VAN_TREES_ITEMS))
        {
            // Write default tree/mod settings to config
            for (Entry<String, ThirdPartyModConfig> e : defaultConfigs().entrySet())
                e.getValue().writeToConfiguration(config, Reference.CTGY_TREE_MOD_CFG + "." + e.getKey());

            TCLog.info("Looks like a fresh config; default config loaded.");
        }
        else
            TCLog.info("Proceeding to load tree/mod configs from file.");

        config.setCategoryComment(Reference.CTGY_TREE_MOD_CFG + "." + Reference.CTGY_VAN_TREES_ITEMS, Reference.VAN_TREES_ITEMS_CTGY_DESC);
        config.setCategoryLanguageKey(Reference.CTGY_TREE_MOD_CFG + "." + Reference.CTGY_VAN_TREES_ITEMS,
                "bspkrs.tc.configgui.ctgy." + Reference.CTGY_TREE_MOD_CFG + "." + Reference.CTGY_VAN_TREES_ITEMS);

        // Load all configs found in the file to ModConfigRegistry
        for (String ctgy : config.getCategoryNames())
        {
            ConfigCategory cc = config.getCategory(ctgy);
            if (ctgy.indexOf(Reference.CTGY_TREE_MOD_CFG + ".") != -1 && cc.containsKey(Reference.MOD_ID)
                    && (cc.get(Reference.MOD_ID).getString().equals(Reference.MINECRAFT) || Loader.isModLoaded(cc.get(Reference.MOD_ID).getString())))
            {
                TCLog.debug("Loading file config for mod %s (config category %s)...", cc.get(Reference.MOD_ID).getString(), ctgy);
                registerUserModConfig(ThirdPartyModConfig.readFromConfiguration(config, ctgy));
            }
        }
    }

    /**
     * Configuration.save() MUST be called after this method!
     */
    public void writeToConfigFile(ThirdPartyModConfig tpmc)
    {
        Configuration config = TCConfigHandler.instance().getConfig();
        if (config == null)
            throw new RuntimeException("Cannot write to a null config object!");

        String modCtgy = "";

        for (String ctgy : config.getCategoryNames())
        {
            ConfigCategory cc = config.getCategory(ctgy);
            if (ctgy.indexOf(Reference.CTGY_TREE_MOD_CFG + ".") != -1 && cc.containsKey(Reference.MOD_ID) && tpmc.modID().equals(cc.get(Reference.MOD_ID).getString()))
            {
                modCtgy = ctgy;
                break;
            }
        }

        if (modCtgy.isEmpty())
            modCtgy = Reference.CTGY_TREE_MOD_CFG + "." + tpmc.modID();
        else if (config.hasCategory(modCtgy))
            config.removeCategory(config.getCategory(modCtgy));

        tpmc.writeToConfiguration(config, modCtgy);

        config.setCategoryComment(Reference.CTGY_TREE_MOD_CFG + "." + Reference.CTGY_VAN_TREES_ITEMS, Reference.VAN_TREES_ITEMS_CTGY_DESC);
        config.setCategoryLanguageKey(Reference.CTGY_TREE_MOD_CFG + "." + Reference.CTGY_VAN_TREES_ITEMS,
                "bspkrs.tc.configgui.ctgy." + Reference.CTGY_TREE_MOD_CFG + "." + Reference.CTGY_VAN_TREES_ITEMS);
    }

    protected void initDefaultModConfigs()
    {
        defaultModCfgs = new TreeMap<String, ThirdPartyModConfig>();
        defaultModCfgs.put(Reference.CTGY_VAN_TREES_ITEMS, new ThirdPartyModConfig());

        //        defaultModCfgs.put("AppliedEnergistics", new ThirdPartyModConfig("AppliedEnergistics", "AppliedEnergistics.cfg", "",
        //                "item:appeng.toolQuartzAxe", "<item:appeng.toolQuartzAxe>", "", true).setOverrideIMC(false));

        defaultModCfgs.put("BiomesOPlenty", new ThirdPartyModConfig("BiomesOPlenty")
                .addAxe(new ItemID("BiomesOPlenty:axeMud"))
                .addAxe(new ItemID("BiomesOPlenty:axeAmethyst"))
                .addTreeDef("bop_cherry", new TreeDefinition().addLogID(new ModulusBlockID("BiomesOPlenty:logs1", 1, 4)).addLeafID(new ModulusBlockID("BiomesOPlenty:leaves3", 3, 8)))
                .addTreeDef("bop_darkwood", new TreeDefinition().addLogID(new ModulusBlockID("BiomesOPlenty:logs1", 2, 4)).addLeafID(new ModulusBlockID("BiomesOPlenty:leaves1", 3, 8)))
                .addTreeDef("bop_magic", new TreeDefinition().addLogID(new ModulusBlockID("BiomesOPlenty:logs2", 1, 4)).addLeafID(new ModulusBlockID("BiomesOPlenty:leaves1", 2, 8)))
                );

        //        defaultModCfgs.put("DivineRPG", new ThirdPartyModConfig("DivineRPG", "DivineRPG.cfg", "block:eucalyptus",
        //                "item:Bedrock Axe; item:Crystal Axe; item:Realmite Axe; item:azuriteaxe; item:corruptedaxe; item:denseaxe; item:divineaxe; " +
        //                        "item:donatoraxe; item:energyaxe; item:mythrilaxe; item:plasmaaxe; item:serenityaxe; item:twilightaxe",
        //                "<item:Bedrock Axe>; <item:Crystal Axe>; <item:Realmite Axe>; <item:azuriteaxe>; <item:corruptedaxe>; <item:denseaxe>; " +
        //                        "<item:divineaxe>; <item:donatoraxe>; <item:energyaxe>; <item:mythrilaxe>; <item:plasmaaxe>; <item:serenityaxe>; " +
        //                        "<item:twilightaxe>", "", true)
        //                .setOverrideIMC(false)
        //                .addTreeDef("eucalyptus", new TreeDefinition("<block:eucalyptus>", "18"))); // still not sure on this

        //        defaultModCfgs.put("Forestry", new ThirdPartyModConfig("Forestry", "forestry/base.conf", "block:log1; block:log2; block:log3; " +
        //                "block:log4; block:log5; block:log6; block:log7; block:leaves")
        //                .setOverrideIMC(false)
        //                .addTreeDef("larch", new TreeDefinition("<block:log1>,0; <block:log1>,4; <block:log1>,8",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("teak", new TreeDefinition("<block:log1>,1; <block:log1>,5; <block:log1>,9",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("acacia", new TreeDefinition("<block:log1>,2; <block:log1>,6; <block:log1>,10",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("lime", new TreeDefinition("<block:log1>,3; <block:log1>,7; <block:log1>,11",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("chestnut", new TreeDefinition("<block:log2>,0; <block:log2>,4; <block:log2>,8",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("wenge", new TreeDefinition("<block:log2>,1; <block:log2>,5; <block:log2>,9",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("baobab", new TreeDefinition("<block:log2>,2; <block:log2>,6; <block:log2>,10",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("sequoia", new TreeDefinition("<block:log2>,3; <block:log2>,7; <block:log2>,11",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("kapok", new TreeDefinition("<block:log3>,0; <block:log3>,4; <block:log3>,8",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("ebony", new TreeDefinition("<block:log3>,1; <block:log3>,5; <block:log3>,9",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("mahogany", new TreeDefinition("<block:log3>,2; <block:log3>,6; <block:log3>,10",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("balsa", new TreeDefinition("<block:log3>,3; <block:log3>,7; <block:log3>,11",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("willow", new TreeDefinition("<block:log4>,0; <block:log4>,4; <block:log4>,8",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("walnut", new TreeDefinition("<block:log4>,1; <block:log4>,5; <block:log4>,9",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("boojum", new TreeDefinition("<block:log4>,2; <block:log4>,6; <block:log4>,10",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("cherry", new TreeDefinition("<block:log4>,3; <block:log4>,7; <block:log4>,11",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("bullpine", new TreeDefinition("<block:log6>,0; <block:log6>,4; <block:log6>,8",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("giant_sequoia", new TreeDefinition("<block:log7>,0; <block:log7>,4; <block:log7>,8",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("datepalm", new TreeDefinition("<block:log5>,2; block:log5>,6; block:log5>,10",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("bluemahoe", new TreeDefinition("<block:log5>,0; <block:log5>,4; <block:log5>,8",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("white_poplar", new TreeDefinition("<block:log5>,1; <block:log5>,5; <block:log5>,9",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("lemon", new TreeDefinition("<block:log6>,3; <block:log6>,7; <block:log6>,11",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("plum", new TreeDefinition("<block:log6>,1; <block:log6>,5; <block:log6>,9",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("papaya", new TreeDefinition("<block:log5>,3; <block:log5>,7; <block:log5>,11",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false))
        //                .addTreeDef("sugar_maple", new TreeDefinition("<block:log6>,2; <block:log6>,6; <block:log6>,10",
        //                        "<block:leaves>,0; <block:leaves>,8")
        //                        .setRequireLeafDecayCheck(false)));

        //        defaultModCfgs.put("Gems_Plus", new ThirdPartyModConfig("GP", "GP.cfg", "", "item:AgateAxe; item:AmethystAxe; item:ChrysocollaAxe; " +
        //                "item:CitrineAxe; item:EmeraldAxe; item:GarnetAxe; item:JadeAxe; item:JasperAxe; item:MalachiteAxe; item:OnyxAxe; item:PhoenixiteAxe; " +
        //                "item:QuartzAxe; item:RubyAxe; item:SapphireAxe; item:SpinelAxe; item:SugiliteAxe; item:TopazAxe; item:TourmalineAxe",
        //                "<item:AgateAxe>; <item:AmethystAxe>; <item:ChrysocollaAxe>; <item:CitrineAxe>; <item:EmeraldAxe>; <item:GarnetAxe>; <item:JadeAxe>; " +
        //                        "<item:JasperAxe>; <item:MalachiteAxe>; <item:OnyxAxe>; <item:PhoenixiteAxe>; <item:QuartzAxe>; <item:RubyAxe>; <item:SapphireAxe>; " +
        //                        "<item:SpinelAxe>; <item:SugiliteAxe>; <item:TopazAxe>; <item:TourmalineAxe>", "", true).setOverrideIMC(false));

        //        defaultModCfgs.put("GraviSuite", new ThirdPartyModConfig("GraviSuite", "GraviSuite.cfg", "", "items:advChainsawID",
        //                "<items:advChainsawID>", "", true));

        defaultModCfgs.put("IC2", new ThirdPartyModConfig("IC2").addAxe(new ItemID("IC2:itemToolBronzeAxe")).addAxe(new ItemID("IC2:itemToolChainsaw"))
                .addShears(new ItemID("IC2:itemToolChainsaw"))
                .setOverrideIMC(false)
                .addTreeDef("ic2_rubber_tree", new TreeDefinition().addLogID(new BlockID("IC2:blockRubWood")).addLeafID(new BlockID("IC2:blockRubLeaves"))));

        //        defaultModCfgs.put("Mekanism", new ThirdPartyModConfig("Mekanism", "Mekanism.cfg", "",
        //                "item:BronzeAxe; item:BronzePaxel; item:DiamondPaxel; item:GlowstoneAxe; item:GlowstonePaxel; item:GoldPaxel; " +
        //                        "item:IronPaxel; item:LazuliAxe; item:LazuliPaxel; item:ObsidianAxe; item:ObsidianPaxel; item:platinumAxe; " +
        //                        "item:platinumPaxel; item:SteelAxe; item:SteelPaxel; item:StonePaxel; item:WoodPaxel; item:OsmiumAxe; item:OsmiumPaxel",
        //                "<item:BronzeAxe>; <item:BronzePaxel>; <item:DiamondPaxel>; <item:GlowstoneAxe>; <item:GlowstonePaxel>; <item:GoldPaxel>; " +
        //                        "<item:IronPaxel>; <item:LazuliAxe>; <item:LazuliPaxel>; <item:ObsidianAxe>; <item:ObsidianPaxel>; <item:PlatinumAxe>; " +
        //                        "<item:PlatinumPaxel>; <item:SteelAxe>; <item:SteelPaxel>; <item:StonePaxel>; <item:WoodPaxel>; <item:OsmiumAxe>; " +
        //                        "<item:OsmiumPaxel>", "", true)
        //                .setOverrideIMC(false));

        //        defaultModCfgs.put("meteors", new ThirdPartyModConfig("meteors", "meteors.cfg", "",
        //                "item:Frezarite Axe ID; item:Meteor Axe ID",
        //                "<item:Frezarite Axe ID>; <item:Meteor Axe ID>", "", true)
        //                .setOverrideIMC(false));

        //        defaultModCfgs.put("MineFactoryReloaded", new ThirdPartyModConfig("MineFactoryReloaded", "powercrystals/minefactoryreloaded/common.cfg",
        //                "block:ID.RubberWood; block:ID.RubberLeaves; block:ID.RubberSapling")
        //                .setOverrideIMC(false)
        //                .addTreeDef("rubber", new TreeDefinition("<block:ID.RubberWood>", "<block:ID.RubberLeaves>")));

        defaultModCfgs.put("Natura", new ThirdPartyModConfig("Natura")
                .addAxe(new ItemID("Natura:natura.axe.bloodwood"))
                .addAxe(new ItemID("Natura:natura.axe.darkwood"))
                .addAxe(new ItemID("Natura:natura.axe.fusewood"))
                .addAxe(new ItemID("Natura:natura.axe.ghostwood"))
                .addAxe(new ItemID("Natura:natura.axe.netherquartz"))
                .setOverrideIMC(false)
                //                                .addTreeDef("bloodwood", new TreeDefinition("<block:Bloodwood Block>", "<block:Sakura Leaves>,2").addLogID(new ModulusBlockID("", 0, 4)).addLeafID(new ModulusBlockID("", 0, 8)))
                .addTreeDef("eucalyptus", new TreeDefinition().addLogID(new ModulusBlockID("Natura:tree", 0, 4)).addLeafID(new ModulusBlockID("Natura:floraleaves", 1, 8)))
                .addTreeDef("sakura", new TreeDefinition().addLogID(new ModulusBlockID("Natura:tree", 1, 4)).addLeafID(new ModulusBlockID("Natura:floraleavesnocolor", 0, 8)))
                .addTreeDef("ghostwood", new TreeDefinition().addLogID(new ModulusBlockID("Natura:tree", 2, 4)).addLeafID(new ModulusBlockID("Natura:floraleavesnocolor", 1, 8)))
                .addTreeDef("hopseed", new TreeDefinition().addLogID(new ModulusBlockID("Natura:tree", 3, 4)).addLeafID(new ModulusBlockID("Natura:floraleaves", 2, 8)))
                //                                .addTreeDef("redwood", new TreeDefinition("<block:Redwood Block>", "<block:Flora Leaves>,0"))
                //                                .addTreeDef("amaranth", new TreeDefinition("<block:Rare Log>,2; <block:Rare Log>,6; <block:Rare Log>,10",
                //                                        "<block:Rare Leaves>,2; <block:Rare Leaves>,10"))
                //                                .addTreeDef("maple", new TreeDefinition("<block:Rare Log>,0; <block:Rare Log>,4; <block:Rare Log>,8",
                //                                        "<block:Rare Leaves>,0; <block:Rare Leaves>,8"))
                //                                .addTreeDef("siverbell", new TreeDefinition("<block:Rare Log>,1; <block:Rare Log>,5; <block:Rare Log>,9",
                //                                        "<block:Rare Leaves>,1; <block:Rare Leaves>,9"))
                //                                .addTreeDef("tigerwood", new TreeDefinition("<block:Rare Log>,3; <block:Rare Log>,7; <block:Rare Log>,11",
                //                                        "<block:Rare Leaves>,3; <block:Rare Leaves>,11"))
                //                                .addTreeDef("willow", new TreeDefinition("<block:Willow Log>",
                //                                        "<block:Sakura Leaves>,3; <block:Sakura Leaves>,11; <block:Sakura Leaves>,15").setMaxHorLeafBreakDist(5))
                );

        //        defaultModCfgs.put("Railcraft", new ThirdPartyModConfig("Railcraft", "railcraft/railcraft.cfg", "", "item:tool.steel.axe", "<item:tool.steel.axe>",
        //                "", true).setOverrideIMC(false));

        defaultModCfgs.put("Thaumcraft", new ThirdPartyModConfig("Thaumcraft")
                .addAxe(new ItemID("Thaumcraft:ItemAxeThaumium"))
                .addAxe(new ItemID("Thaumcraft:ItemAxeElemental"))
                .setOverrideIMC(false)
                .addTreeDef("greatwood", new TreeDefinition().addLogID(new ModulusBlockID("Thaumcraft:blockMagicalLog", 0, 4)).addLeafID(new ModulusBlockID("Thaumcraft:blockMagicalLeaves", 0, 8))
                        .setMaxHorLeafBreakDist(7).setRequireLeafDecayCheck(false))
                .addTreeDef("silverwood", new TreeDefinition().addLogID(new ModulusBlockID("Thaumcraft:blockMagicalLog", 1, 4)).addLeafID(new ModulusBlockID("Thaumcraft:blockMagicalLeaves", 1, 8))));

        defaultModCfgs.put("TConstruct", new ThirdPartyModConfig("TConstruct")
                .addAxe(new ItemID("TConstruct:hatchet"))
                .addAxe(new ItemID("TConstruct:mattock"))
                .addAxe(new ItemID("TConstruct:lumberaxe"))
                .setOverrideIMC(false));

        defaultModCfgs.put("TwilightForest", new ThirdPartyModConfig("TwilightForest")
                .addAxe(new ItemID("TwilightForest:item.ironwoodAxe"))
                .addAxe(new ItemID("TwilightForest:item.knightlyAxe"))
                .addAxe(new ItemID("TwilightForest:item.minotaurAxe"))
                .addAxe(new ItemID("TwilightForest:item.steeleafAxe"))
                .setOverrideIMC(false)
                .addTreeDef("TF_oak", new TreeDefinition().addLogID(new ModulusBlockID("TwilightForest:tile.TFLog", 0, 4)).addLeafID(new ModulusBlockID("TwilightForest:tile.TFLeaves", 0, 8)))
                .addTreeDef("TF_canopy", new TreeDefinition().addLogID(new ModulusBlockID("TwilightForest:tile.TFLog", 1, 4)).addLeafID(new ModulusBlockID("TwilightForest:tile.TFLeaves", 1, 8)))
                .addTreeDef("TF_mangrove", new TreeDefinition().addLogID(new ModulusBlockID("TwilightForest:tile.TFLog", 2, 4)).addLeafID(new ModulusBlockID("TwilightForest:tile.TFLeaves", 2, 8)))
                .addTreeDef("TF_darkwood", new TreeDefinition().addLogID(new ModulusBlockID("TwilightForest:tile.TFLog", 3, 4)).addLeafID(new BlockID("TwilightForest:tile.TFHedge", 1))
                        .setMaxLeafIDDist(2).setRequireLeafDecayCheck(false).setMaxHorLeafBreakDist(5))
                .addTreeDef("TF_time", new TreeDefinition().addLogID(new ModulusBlockID("TwilightForest:tile.TFMagicLog", 0, 4)).addLogID(new ModulusBlockID("TwilightForest:tile.TFMagicLogSpecial", 0, 4))
                        .addLeafID(new ModulusBlockID("TwilightForest:tile.TFMagicLeaves", 0, 8)))
                .addTreeDef("TF_transformation", new TreeDefinition().addLogID(new ModulusBlockID("TwilightForest:tile.TFMagicLog", 1, 4)).addLogID(new ModulusBlockID("TwilightForest:tile.TFMagicLogSpecial", 1, 4))
                        .addLeafID(new ModulusBlockID("TwilightForest:tile.TFMagicLeaves", 1, 8)))
                .addTreeDef("TF_miner", new TreeDefinition().addLogID(new ModulusBlockID("TwilightForest:tile.TFMagicLog", 2, 4)).addLogID(new ModulusBlockID("TwilightForest:tile.TFMagicLogSpecial", 2, 4))
                        .addLeafID(new ModulusBlockID("TwilightForest:tile.TFMagicLeaves", 2, 8)))
                .addTreeDef("TF_sorting", new TreeDefinition().addLogID(new ModulusBlockID("TwilightForest:tile.TFMagicLog", 3, 4)).addLogID(new ModulusBlockID("TwilightForest:tile.TFMagicLogSpecial", 3, 4))
                        .addLeafID(new ModulusBlockID("TwilightForest:tile.TFMagicLeaves", 3, 8))));
    }
}
