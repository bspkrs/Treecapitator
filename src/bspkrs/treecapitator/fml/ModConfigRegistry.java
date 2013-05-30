package bspkrs.treecapitator.fml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import bspkrs.treecapitator.ConfigTreeDefinition;
import bspkrs.treecapitator.Strings;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TCSettings;
import bspkrs.util.ConfigCategory;
import bspkrs.util.Configuration;
import cpw.mods.fml.common.Loader;

public class ModConfigRegistry
{
    private static ModConfigRegistry         instance;
    
    private Map<String, ThirdPartyModConfig> userModCfgs;
    private Map<String, ThirdPartyModConfig> imcModCfgs;
    private Map<String, ThirdPartyModConfig> defaultModCfgs;
    
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
    
    public void registerUserModConfig(ThirdPartyModConfig tpmc)
    {
        TCLog.debug("Registering user mod config %s", tpmc.modID());
        if (!userModCfgs.containsKey(tpmc.modID()))
            userModCfgs.put(tpmc.modID(), tpmc);
        else
            TCLog.warning("User config contains multiple 3rd party mod configs for mod id \"%s\".  The first entry will be used.", tpmc.modID());
    }
    
    public void registerIMCModConfig(ThirdPartyModConfig tpmc)
    {
        TCLog.debug("Registering IMC mod config %s", tpmc.modID());
        if (!imcModCfgs.containsKey(tpmc.modID()))
            imcModCfgs.put(tpmc.modID(), tpmc);
        else
            TCLog.warning("Mod \"%s\" sent multiple IMC messages. The first message will be used.", tpmc.modID());
    }
    
    // TODO: add code to write IMC configs to the user config if the mod is not already configured there and a flag is set in config
    
    protected void refreshUserTagMaps()
    {
        for (ThirdPartyModConfig tpmc : userModCfgs.values())
            tpmc.refreshReplacementTags();
    }
    
    protected void refreshIMCTagMaps()
    {
        for (ThirdPartyModConfig tpmc : imcModCfgs.values())
            tpmc.refreshReplacementTags();
    }
    
    protected void refreshAllTagMaps()
    {
        refreshUserTagMaps();
        refreshIMCTagMaps();
    }
    
    protected void applyPrioritizedModConfigs()
    {
        List<ThirdPartyModConfig> finalList = new ArrayList<ThirdPartyModConfig>();
        
        TCLog.info("Prioritizing User and IMC mod configs...");
        for (Entry<String, ThirdPartyModConfig> e : imcModCfgs.entrySet())
            if (!userModCfgs.containsKey(e.getKey()) || !userModCfgs.get(e.getKey()).overrideIMC())
            {
                finalList.add(e.getValue());
                TCLog.debug("IMC mod config loaded for %s.", e.getValue().modID());
            }
        
        for (Entry<String, ThirdPartyModConfig> e : userModCfgs.entrySet())
            if (!imcModCfgs.containsKey(e.getKey()) || e.getValue().overrideIMC())
            {
                finalList.add(e.getValue());
                TCLog.debug("User mod config loaded for %s.", e.getValue().modID());
            }
        
        TCLog.info("Getting tag replacements from configs...");
        for (ThirdPartyModConfig cfg : finalList)
            cfg.refreshReplacementTags();
        
        TCLog.info("Registering items and trees...");
        for (ThirdPartyModConfig cfg : finalList)
            cfg.registerTools().registerTrees();
    }
    
    protected void initDefaultModConfigs()
    {
        defaultModCfgs = new TreeMap<String, ThirdPartyModConfig>();
        defaultModCfgs.put(Strings.VAN_TREES_ITEMS_CTGY, new ThirdPartyModConfig());
        /*
         * TODO: appliedenergistics {
        S:axeIDList=<item:appeng.toolQuartzAxe>
        S:configPath=AppliedEnergistics.cfg
        S:itemConfigKeys=item:appeng.toolQuartzAxe
        S:modID=AppliedEnergistics
        B:overrideIMC=false
        B:useShiftedItemID=true
        }
         */
        
        defaultModCfgs.put("BiomesOPlenty", new ThirdPartyModConfig("BiomesOPlenty", "BiomesOPlenty.cfg",
                "block:Bamboo ID; block:Colourized Leaves ID; block:Fruit Leaf Block ID; block:Leaf Block ID 1; block:Leaf Block ID 2; " +
                        "block:Log Block ID 1; block:Log Block ID 2; block:Log Block ID 3; block:Petal ID",
                "item:Muddy Axe ID; item:Amethyst Axe ID", "<item:Muddy Axe ID>; <item:Amethyst Axe ID>", "", true)
                .setOverrideIMC(false)
                .addConfigTreeDef(Strings.OAK, new ConfigTreeDefinition("", "<block:Leaf Block ID 1>,4; <block:Leaf Block ID 1>,7; " +
                        "<block:Leaf Block ID 1>,12; <block:Leaf Block ID 1>,15; <block:Fruit Leaf Block ID>; <block:Leaf Block ID 2>,0; " +
                        "<block:Leaf Block ID 2>,8; <block:Leaf Block ID 2>,2; <block:Leaf Block ID 2>,10;")
                //.setRequireLeafDecayCheck(false)
                )
                .addConfigTreeDef(Strings.BIRCH, new ConfigTreeDefinition("", "<block:Leaf Block ID 1>,0; <block:Leaf Block ID 1>,8")
                //.setRequireLeafDecayCheck(false)
                )
                .addConfigTreeDef("bamboo", new ConfigTreeDefinition("<block:Bamboo ID>",
                        "<block:Leaf Block ID 1>,1; <block:Leaf Block ID 1>,9")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("cherry", new ConfigTreeDefinition("<block:Log Block ID 1>,1; <block:Log Block ID 1>,5; <block:Log Block ID 1>,9",
                        "<block:Leaf Block ID 2>,1; <block:Leaf Block ID 2>,3; <block:Leaf Block ID 2>,9; <block:Leaf Block ID 2>,11")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("willow", new ConfigTreeDefinition("<block:Log Block ID 3>,1",
                        "<block:Colourized Leaves ID>,4; <block:Colourized Leaves ID>,12")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("magic", new ConfigTreeDefinition("<block:Log Block ID 2>,1",
                        "<block:Leaf Block ID 1>,2; <block:Leaf Block ID 1>,10")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("palm", new ConfigTreeDefinition("<block:Log Block ID 2>,3", "<block:Colourized Leaves ID>,2")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("redwood", new ConfigTreeDefinition("<block:Log Block ID 3>,0",
                        "<block:Colourized Leaves ID>,3; <block:Colourized Leaves ID>,11")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("dead", new ConfigTreeDefinition(
                        "<block:Log Block ID 3>,2; <block:Log Block ID 3>,6; <block:Log Block ID 3>,10", ""))
                .addConfigTreeDef("acacia", new ConfigTreeDefinition("<block:Log Block ID 1>,0",
                        "<block:Colourized Leaves ID>,0; <block:Colourized Leaves ID>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("mangrove", new ConfigTreeDefinition("<block:Log Block ID 2>,2",
                        "<block:Colourized Leaves ID>,1; <block:Colourized Leaves ID>,9")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("darkwood", new ConfigTreeDefinition("<block:Log Block ID 1>,2",
                        "<block:Leaf Block ID 1>,3; <block:Leaf Block ID 1>,11")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("fir", new ConfigTreeDefinition("<block:Log Block ID 2>,0", "<block:Leaf Block ID 1>,6")
                        .setRequireLeafDecayCheck(false)));
        
        defaultModCfgs.put("DivineRPG", new ThirdPartyModConfig("DivineRPG", "DivineRPG.cfg", "block:eucalyptus",
                "item:Bedrock Axe; item:Crystal Axe; item:Realmite Axe; item:azuriteaxe; item:corruptedaxe; item:denseaxe; item:divineaxe; " +
                        "item:donatoraxe; item:energyaxe; item:mythrilaxe; item:plasmaaxe; item:serenityaxe; item:twilightaxe",
                "<item:Bedrock Axe>; <item:Crystal Axe>; <item:Realmite Axe>; <item:azuriteaxe>; <item:corruptedaxe>; <item:denseaxe>; " +
                        "<item:divineaxe>; <item:donatoraxe>; <item:energyaxe>; <item:mythrilaxe>; <item:plasmaaxe>; <item:serenityaxe>; " +
                        "<item:twilightaxe>", "", true)
                .setOverrideIMC(false)
                .addConfigTreeDef("eucalyptus", new ConfigTreeDefinition("<block:eucalyptus>", "18"))); // still not sure on this
        
        defaultModCfgs.put("ExtrabiomesXL", new ThirdPartyModConfig("ExtrabiomesXL", "extrabiomes/extrabiomes.cfg",
                "block:customlog.id; block:quarterlog0.id; block:quarterlog1.id; block:quarterlog2.id; block:quarterlog3.id; " +
                        "block:autumnleaves.id; block:greenleaves.id")
                .setOverrideIMC(false)
                .addConfigTreeDef(Strings.OAK,
                        new ConfigTreeDefinition("<block:quarterlog0.id>,2; <block:quarterlog1.id>,2; <block:quarterlog2.id>,2; <block:quarterlog3.id>,2;",
                                "<block:autumnleaves.id>"))
                .addConfigTreeDef(Strings.SPRUCE,
                        new ConfigTreeDefinition("", "<block:autumnleaves.id>"))
                .addConfigTreeDef("redwood",
                        new ConfigTreeDefinition("<block:quarterlog0.id>,0; <block:quarterlog1.id>,0; <block:quarterlog2.id>,0; " +
                                "<block:quarterlog3.id>,0", "<block:greenleaves.id>,1; <block:greenleaves.id>,9")
                                .setMaxHorLeafBreakDist(10).setRequireLeafDecayCheck(false))
                .addConfigTreeDef("fir",
                        new ConfigTreeDefinition("<block:customlog.id>,0; <block:quarterlog0.id>,1; <block:quarterlog1.id>,1; " +
                                "<block:quarterlog2.id>,1; <block:quarterlog3.id>,1", "<block:greenleaves.id>,0; <block:greenleaves.id>,8")
                                .setMaxHorLeafBreakDist(10).setRequireLeafDecayCheck(false))
                .addConfigTreeDef("acacia", new ConfigTreeDefinition("<block:customlog.id>,1", "<block:greenleaves.id>,2")));
        
        defaultModCfgs.put("Forestry", new ThirdPartyModConfig("Forestry", "forestry/base.conf", "block:log1; block:log2; block:log3; " +
                "block:log4; block:log5; block:log6; block:log7; block:leaves")
                .setOverrideIMC(false)
                .addConfigTreeDef("larch", new ConfigTreeDefinition("<block:log1>,0; <block:log1>,4; <block:log1>,8",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("teak", new ConfigTreeDefinition("<block:log1>,1; <block:log1>,5; <block:log1>,9",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("acacia", new ConfigTreeDefinition("<block:log1>,2; <block:log1>,6; <block:log1>,10",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("lime", new ConfigTreeDefinition("<block:log1>,3; <block:log1>,7; <block:log1>,11",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("chestnut", new ConfigTreeDefinition("<block:log2>,0; <block:log2>,4; <block:log2>,8",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("wenge", new ConfigTreeDefinition("<block:log2>,1; <block:log2>,5; <block:log2>,9",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("baobab", new ConfigTreeDefinition("<block:log2>,2; <block:log2>,6; <block:log2>,10",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("sequoia", new ConfigTreeDefinition("<block:log2>,3; <block:log2>,7; <block:log2>,11",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("kapok", new ConfigTreeDefinition("<block:log3>,0; <block:log3>,4; <block:log3>,8",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("ebony", new ConfigTreeDefinition("<block:log3>,1; <block:log3>,5; <block:log3>,9",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("mahogany", new ConfigTreeDefinition("<block:log3>,2; <block:log3>,6; <block:log3>,10",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("balsa", new ConfigTreeDefinition("<block:log3>,3; <block:log3>,7; <block:log3>,11",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("willow", new ConfigTreeDefinition("<block:log4>,0; <block:log4>,4; <block:log4>,8",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("walnut", new ConfigTreeDefinition("<block:log4>,1; <block:log4>,5; <block:log4>,9",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("boojum", new ConfigTreeDefinition("<block:log4>,2; <block:log4>,6; <block:log4>,10",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("cherry", new ConfigTreeDefinition("<block:log4>,3; <block:log4>,7; <block:log4>,11",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("bullpine", new ConfigTreeDefinition("<block:log6>,0; <block:log6>,4; <block:log6>,8",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("giant_sequoia", new ConfigTreeDefinition("<block:log7>,0; <block:log7>,4; <block:log7>,8",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("datepalm", new ConfigTreeDefinition("<block:log5>,2; block:log5>,6; block:log5>,10",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("bluemahoe", new ConfigTreeDefinition("<block:log5>,0; <block:log5>,4; <block:log5>,8",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("white_poplar", new ConfigTreeDefinition("<block:log5>,1; <block:log5>,5; <block:log5>,9",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("lemon", new ConfigTreeDefinition("<block:log6>,3; <block:log6>,7; <block:log6>,11",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("plum", new ConfigTreeDefinition("<block:log6>,1; <block:log6>,5; <block:log6>,9",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("papaya", new ConfigTreeDefinition("<block:log5>,3; <block:log5>,7; <block:log5>,11",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("sugar_maple", new ConfigTreeDefinition("<block:log6>,2; <block:log6>,6; <block:log6>,10",
                        "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false)));
        
        defaultModCfgs.put("IC2", new ThirdPartyModConfig("IC2", "IC2.cfg", "block:blockRubWood; block:blockRubLeaves",
                "item:itemToolBronzeAxe; item:itemToolChainsaw", "<item:itemToolBronzeAxe>; <item:itemToolChainsaw>",
                "<item:itemToolChainsaw>", true)
                .setOverrideIMC(false)
                .addConfigTreeDef("rubber", new ConfigTreeDefinition("<block:blockRubWood>", "<block:blockRubLeaves>")));
        
        defaultModCfgs.put("Mekanism", new ThirdPartyModConfig("Mekanism", "Mekanism.cfg", "",
                "item:BronzeAxe; item:BronzePaxel; item:DiamondPaxel; item:GlowstoneAxe; item:GlowstonePaxel; item:GoldPaxel; " +
                        "item:IronPaxel; item:LazuliAxe; item:LazuliPaxel; item:ObsidianAxe; item:ObsidianPaxel; item:platinumAxe; " +
                        "item:platinumPaxel; item:SteelAxe; item:SteelPaxel; item:StonePaxel; item:WoodPaxel; item:OsmiumAxe; item:OsmiumPaxel",
                "<item:BronzeAxe>; <item:BronzePaxel>; <item:DiamondPaxel>; <item:GlowstoneAxe>; <item:GlowstonePaxel>; <item:GoldPaxel>; " +
                        "<item:IronPaxel>; <item:LazuliAxe>; <item:LazuliPaxel>; <item:ObsidianAxe>; <item:ObsidianPaxel>; <item:PlatinumAxe>; " +
                        "<item:PlatinumPaxel>; <item:SteelAxe>; <item:SteelPaxel>; <item:StonePaxel>; <item:WoodPaxel>; <item:OsmiumAxe>; " +
                        "<item:OsmiumPaxel>", "", true)
                .setOverrideIMC(false));
        
        defaultModCfgs.put("MineFactoryReloaded", new ThirdPartyModConfig("MineFactoryReloaded", "powercrystals/minefactoryreloaded/common.cfg",
                "block:ID.RubberWood; block:ID.RubberLeaves; block:ID.RubberSapling")
                .setOverrideIMC(false)
                .addConfigTreeDef("rubber", new ConfigTreeDefinition("<block:ID.RubberWood>", "<block:ID.RubberLeaves>")));
        
        defaultModCfgs.put("Natura", new ThirdPartyModConfig("Natura", "Natura.txt", "block:Bloodwood Block; block:Flora Leaves; " +
                "block:Redwood Block; block:Sakura Leaves; block:Wood Block; block:Rare Log; block:Rare Leaves; block:Willow Log")
                .setOverrideIMC(false)
                .addConfigTreeDef("bloodwood", new ConfigTreeDefinition("<block:Bloodwood Block>", "<block:Sakura Leaves>,2"))
                .addConfigTreeDef("eucalyptus", new ConfigTreeDefinition("<block:Wood Block>,0; <block:Wood Block>,4; <block:Wood Block>,8",
                        "<block:Flora Leaves>,1; <block:Flora Leaves>,9"))
                .addConfigTreeDef("ghostwood", new ConfigTreeDefinition("<block:Wood Block>,2; <block:Wood Block>, 6; <block:Wood Block>, 10",
                        "<block:Sakura Leaves>,1"))
                .addConfigTreeDef("hopseed", new ConfigTreeDefinition("<block:Wood Block>,3; <block:Wood Block>, 7; <block:Wood Block>, 11",
                        "<block:Flora Leaves>,2"))
                .addConfigTreeDef("redwood", new ConfigTreeDefinition("<block:Redwood Block>", "<block:Flora Leaves>,0"))
                .addConfigTreeDef("sakura", new ConfigTreeDefinition("<block:Wood Block>, 1; <block:Wood Block>, 5; <block:Wood Block>, 9",
                        "<block:Sakura Leaves>,0; <block:Sakura Leaves>,8"))
                .addConfigTreeDef("amaranth", new ConfigTreeDefinition("<block:Rare Log>,2; <block:Rare Log>,6; <block:Rare Log>,10",
                        "<block:Rare Leaves>,2; <block:Rare Leaves>,10"))
                .addConfigTreeDef("maple", new ConfigTreeDefinition("<block:Rare Log>,0; <block:Rare Log>,4; <block:Rare Log>,8",
                        "<block:Rare Leaves>,0; <block:Rare Leaves>,8"))
                .addConfigTreeDef("siverbell", new ConfigTreeDefinition("<block:Rare Log>,1; <block:Rare Log>,5; <block:Rare Log>,9",
                        "<block:Rare Leaves>,1; <block:Rare Leaves>,9"))
                .addConfigTreeDef("tigerwood", new ConfigTreeDefinition("<block:Rare Log>,3; <block:Rare Log>,7; <block:Rare Log>,11",
                        "<block:Rare Leaves>,3; <block:Rare Leaves>,11"))
                .addConfigTreeDef("willow", new ConfigTreeDefinition("<block:Willow Log>",
                        "<block:Sakura Leaves>,3; <block:Sakura Leaves>,11; <block:Sakura Leaves>,15").setMaxHorLeafBreakDist(5)));
        
        /*
         *  TODO:   railcraft {
            S:axeIDList=<item:tool.steel.axe>
            S:configPath=railcraft/railcraft.cfg
            S:itemConfigKeys=item:tool.steel.axe
            S:modID=Railcraft
            B:overrideIMC=false
            B:useShiftedItemID=true
        }

         */
        
        defaultModCfgs.put("RedPowerWorld", new ThirdPartyModConfig("RedPowerWorld", "redpower/redpower.cfg",
                "blocks.world:log.id; blocks.world:leaves.id",
                "items.world:axeRuby.id; items.world:axeGreenSapphire.id; items.world:axeSapphire.id",
                "<items.world:axeRuby.id>; <items.world:axeGreenSapphire.id>; <items.world:axeSapphire.id>", "", true)
                .setOverrideIMC(false)
                .addConfigTreeDef("rubber", new ConfigTreeDefinition("<blocks.world:log.id>", "<blocks.world:leaves.id>")));
        
        defaultModCfgs.put("Thaumcraft", new ThirdPartyModConfig("Thaumcraft", "Thaumcraft.cfg", "block:BlockMagicalLog; block:BlockMagicalLeaves",
                "item:Thaumaxe", "<item:Thaumaxe>", "", true)
                .setOverrideIMC(false)
                .addConfigTreeDef("greatwood",
                        new ConfigTreeDefinition("<block:BlockMagicalLog>,0; <block:BlockMagicalLog>,4; <block:BlockMagicalLog>,8",
                                "<block:BlockMagicalLeaves>,0; <block:BlockMagicalLeaves>,8"))
                .addConfigTreeDef("silverwood",
                        new ConfigTreeDefinition("<block:BlockMagicalLog>,1; <block:BlockMagicalLog>,5; <block:BlockMagicalLog>,9",
                                "<block:BlockMagicalLeaves>,1; <block:BlockMagicalLeaves>,9")));
        
        defaultModCfgs.put("TConstruct", new ThirdPartyModConfig("TConstruct", "TinkersWorkshop.txt", "",
                "tools:Axe; tools:Lumber Axe; tools:Mattock", "<tools:Axe>; <tools:Lumber Axe>; <tools:Mattock>", "", true)
                .setOverrideIMC(false));
        
        defaultModCfgs.put("TwilightForest", new ThirdPartyModConfig("TwilightForest", "TwilightForest.cfg",
                "block:Log; block:MagicLog; block:Leaves; block:MagicLeaves; block:Hedge",
                "<item:IronwoodAxe>; <item:SteeleafAxe>; <item:MinotaurAxe>", "", true)
                .setOverrideIMC(false)
                .addConfigTreeDef("oak", new ConfigTreeDefinition("<block:Log>,0; <block:Log>,4; <block:Log>,8; <block:Log>,12",
                        "<block:Leaves>,0; <block:Leaves>,3; <block:Leaves>,8; <block:Leaves>,11"))
                .addConfigTreeDef("canopy", new ConfigTreeDefinition("<block:Log>,1; <block:Log>,5; <block:Log>,9; <block:Log>,13",
                        "<block:Leaves>, 1; <block:Leaves>,9"))
                .addConfigTreeDef("mangrove", new ConfigTreeDefinition("<block:Log>,2; <block:Log>,6; <block:Log>,10; <block:Log>,14",
                        "<block:Leaves>, 1; <block:Leaves>,9"))
                .addConfigTreeDef("darkwood", new ConfigTreeDefinition("<block:Log>,3; <block:Log>,7; <block:Log>,11;  <block:Log>,15",
                        "<block:Hedge>,1"))
                .addConfigTreeDef("time", new ConfigTreeDefinition("<block:MagicLog>,0; <block:MagicLog>,4; <block:MagicLog>,8; <block:MagicLog>,12",
                        "<block:MagicLeaves>,0; <block:MagicLeaves>,8")));
    }
    
    public Map<String, ThirdPartyModConfig> defaultConfigs()
    {
        return new TreeMap<String, ThirdPartyModConfig>(defaultModCfgs);
    }
    
    /*
     * Gets/Sets user configs from the config object
     */
    public void syncConfiguration(Configuration config)
    {
        /*
         * Get / Set 3rd Party Mod configs
         */
        TCSettings.idResolverModID = config.getString("idResolverModID", Strings.TREE_MOD_CFG_CTGY,
                TCSettings.idResolverModID, Strings.idResolverModIDDesc);
        TCSettings.multiMineModID = config.getString("multiMineID", Strings.TREE_MOD_CFG_CTGY,
                TCSettings.multiMineModID, Strings.multiMineIDDesc);
        TCSettings.userConfigOverridesIMC = config.getBoolean("userConfigOverridesIMC", Strings.TREE_MOD_CFG_CTGY,
                TCSettings.userConfigOverridesIMC, Strings.userConfigOverridesIMCDesc);
        
        TCLog.configs(config, Strings.TREE_MOD_CFG_CTGY);
        
        config.addCustomCategoryComment(Strings.TREE_MOD_CFG_CTGY, Strings.TREE_MOD_CFG_CTGY_DESC);
        
        if (!config.hasCategory(Strings.TREE_MOD_CFG_CTGY + "." + Strings.VAN_TREES_ITEMS_CTGY))
        {
            // Write default tree/mod settings to config
            Map<String, ThirdPartyModConfig> m = defaultConfigs();
            for (Entry<String, ThirdPartyModConfig> e : m.entrySet())
                e.getValue().writeToConfiguration(config, Strings.TREE_MOD_CFG_CTGY + "." + e.getKey());
            
            TCLog.info("Looks like a fresh config; default config loaded.");
        }
        else
            TCLog.info("Proceeding to load tree/mod configs from file.");
        
        config.addCustomCategoryComment(Strings.TREE_MOD_CFG_CTGY + "." + Strings.VAN_TREES_ITEMS_CTGY, Strings.VAN_TREES_ITEMS_CTGY_DESC);
        
        // Load all configs found in the file to ModConfigRegistry
        for (String ctgy : config.getCategoryNames())
        {
            ConfigCategory cc = config.getCategory(ctgy);
            if (ctgy.indexOf(Strings.TREE_MOD_CFG_CTGY + ".") != -1 && cc.containsKey(Strings.MOD_ID) && Loader.isModLoaded(cc.get(Strings.MOD_ID).getString()))
                registerUserModConfig(new ThirdPartyModConfig(config, ctgy));
        }
    }
    /*static
    {
        // This one can go, me thinks... looks like he's hung up the wrench
        HashMap<String, String> zapapples = new HashMap<String, String>();
        zapapples.put(Strings.MOD_ID, "ZapApples");
        zapapples.put(Strings.CONFIG_PATH, "ZapApples.cfg");
        zapapples.put(Strings.BLOCK_CFG_KEYS, "block:zapAppleLogID; block:zapAppleLeavesID; block:zapAppleFlowersID");
        
        HashMap<String, String> zapapple = new HashMap<String, String>();
        zapapple.put(Strings.LOGS, "<block:zapAppleLogID>");
        zapapple.put(Strings.LEAVES, "<block:zapAppleLeavesID>; <block:zapAppleFlowersID>");
        
    }*/
}
