package bspkrs.treecapitator.fml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import bspkrs.treecapitator.ConfigTreeDefinition;
import bspkrs.treecapitator.Strings;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TCSettings;
import bspkrs.util.ConfigCategory;
import bspkrs.util.Configuration;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;

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
        List<ThirdPartyModConfig> finalList = new LinkedList<ThirdPartyModConfig>();
        
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
        
        defaultModCfgs.put("AppliedEnergistics", new ThirdPartyModConfig("AppliedEnergistics", "AppliedEnergistics.cfg", "",
                "item:appeng.toolQuartzAxe", "<item:appeng.toolQuartzAxe>", "", true).setOverrideIMC(false));
        
        defaultModCfgs.put("BiomesOPlenty", new ThirdPartyModConfig("BiomesOPlenty", "BiomesOPlenty.cfg",
                "block:Bamboo ID; block:Colourized Leaves ID; block:Fruit Leaf Block ID; block:Leaf Block ID 1; block:Leaf Block ID 2; " +
                        "block:Log Block ID 1; block:Log Block ID 2; block:Log Block ID 3; block:Log Block ID 4; block:Petal ID",
                "item:Muddy Axe ID; item:Amethyst Axe ID", "<item:Muddy Axe ID>; <item:Amethyst Axe ID>", "", true)
                .setOverrideIMC(false)
                .addConfigTreeDef(Strings.OAK, new ConfigTreeDefinition("", "<block:Leaf Block ID 1>,4; <block:Leaf Block ID 1>,7; " +
                        "<block:Leaf Block ID 1>,12; <block:Leaf Block ID 1>,15; <block:Fruit Leaf Block ID>; <block:Leaf Block ID 2>,0; " +
                        "<block:Leaf Block ID 2>,8; <block:Leaf Block ID 2>,2; <block:Leaf Block ID 2>,10; 18,2; 18,10")
                //.setRequireLeafDecayCheck(false)
                )
                .addConfigTreeDef(Strings.BIRCH, new ConfigTreeDefinition("", "<block:Leaf Block ID 1>,0; <block:Leaf Block ID 1>,8")
                //.setRequireLeafDecayCheck(false)
                )
                .addConfigTreeDef(Strings.JUNGLE, new ConfigTreeDefinition("", "").setMaxLeafIDDist(3))
                
                .addConfigTreeDef("acacia", new ConfigTreeDefinition("<block:Log Block ID 1>,0; <block:Log Block ID 1>,4; <block:Log Block ID 1>,8",
                        "<block:Colourized Leaves ID>,0; <block:Colourized Leaves ID>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("cherry", new ConfigTreeDefinition("<block:Log Block ID 1>,1; <block:Log Block ID 1>,5; <block:Log Block ID 1>,9",
                        "<block:Leaf Block ID 2>,1; <block:Leaf Block ID 2>,3; <block:Leaf Block ID 2>,9; <block:Leaf Block ID 2>,11")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("darkwood", new ConfigTreeDefinition("<block:Log Block ID 1>,2; <block:Log Block ID 1>,6; <block:Log Block ID 1>,10",
                        "<block:Leaf Block ID 1>,3; <block:Leaf Block ID 1>,11")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("fir", new ConfigTreeDefinition("<block:Log Block ID 1>,3; <block:Log Block ID 1>,7; <block:Log Block ID 1>,11",
                        "<block:Leaf Block ID 1>,5; <block:Leaf Block ID 1>,13")
                        .setRequireLeafDecayCheck(false))
                
                .addConfigTreeDef("holy", new ConfigTreeDefinition("<block:Log Block ID 2>,0; <block:Log Block ID 2>,4; <block:Log Block ID 2>,8",
                        "<block:Leaf Block ID 1>,6; <block:Leaf Block ID 1>,14")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("magic", new ConfigTreeDefinition("<block:Log Block ID 2>,1; <block:Log Block ID 2>,5; <block:Log Block ID 2>,9",
                        "<block:Leaf Block ID 1>,2; <block:Leaf Block ID 1>,10")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("mangrove", new ConfigTreeDefinition("<block:Log Block ID 2>,2; <block:Log Block ID 2>,6; <block:Log Block ID 2>,10",
                        "<block:Colourized Leaves ID>,1; <block:Colourized Leaves ID>,9")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("palm", new ConfigTreeDefinition("<block:Log Block ID 2>,3; <block:Log Block ID 2>,7; <block:Log Block ID 2>,11",
                        "<block:Colourized Leaves ID>,2; <block:Colourized Leaves ID>,10")
                        .setRequireLeafDecayCheck(false))
                
                .addConfigTreeDef("redwood", new ConfigTreeDefinition("<block:Log Block ID 3>,0; <block:Log Block ID 3>,4; <block:Log Block ID 3>,8",
                        "<block:Colourized Leaves ID>,3; <block:Colourized Leaves ID>,11")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("willow", new ConfigTreeDefinition("<block:Log Block ID 3>,1; <block:Log Block ID 3>,5; <block:Log Block ID 3>,9",
                        "<block:Colourized Leaves ID>,4; <block:Colourized Leaves ID>,12")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("dead", new ConfigTreeDefinition("<block:Log Block ID 3>,2; <block:Log Block ID 3>,6; <block:Log Block ID 3>,10", ""))
                .addConfigTreeDef("big_flower", new ConfigTreeDefinition("<block:Log Block ID 3>,3; <block:Log Block ID 3>,7; <block:Log Block ID 3>,11",
                        "<block:Petal ID>"))
                
                .addConfigTreeDef("pine", new ConfigTreeDefinition("<block:Log Block ID 4>,0; <block:Log Block ID 4>,4; <block:Log Block ID 4>,8",
                        "<block:Colourized Leaves ID>,5; <block:Colourized Leaves ID>,13")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("hellbark", new ConfigTreeDefinition("<block:Log Block ID 4>,1; <block:Log Block ID 4>,5; <block:Log Block ID 4>,9",
                        "<block:Leaf Block ID 2>,4; <block:Leaf Block ID 2>,12")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("jacaranda", new ConfigTreeDefinition("<block:Log Block ID 4>,2; <block:Log Block ID 4>,6; <block:Log Block ID 4>,10",
                        "<block:Leaf Block ID 2>,5; <block:Leaf Block ID 2>,13")
                        .setRequireLeafDecayCheck(false))
                );
        
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
        
        defaultModCfgs.put("Gems_Plus", new ThirdPartyModConfig("GP", "GP.cfg", "", "item:AgateAxe; item:AmethystAxe; item:ChrysocollaAxe; " +
                "item:CitrineAxe; item:EmeraldAxe; item:GarnetAxe; item:JadeAxe; item:JasperAxe; item:MalachiteAxe; item:OnyxAxe; item:PhoenixiteAxe; " +
                "item:QuartzAxe; item:RubyAxe; item:SapphireAxe; item:SpinelAxe; item:SugiliteAxe; item:TopazAxe; item:TourmalineAxe",
                "<item:AgateAxe>; <item:AmethystAxe>; <item:ChrysocollaAxe>; <item:CitrineAxe>; <item:EmeraldAxe>; <item:GarnetAxe>; <item:JadeAxe>; " +
                        "<item:JasperAxe>; <item:MalachiteAxe>; <item:OnyxAxe>; <item:PhoenixiteAxe>; <item:QuartzAxe>; <item:RubyAxe>; <item:SapphireAxe>; " +
                        "<item:SpinelAxe>; <item:SugiliteAxe>; <item:TopazAxe>; <item:TourmalineAxe>", "", true).setOverrideIMC(false));
        
        defaultModCfgs.put("GraviSuite", new ThirdPartyModConfig("GraviSuite", "GraviSuite.cfg", "", "items:advChainsawID",
                "<items:advChainsawID>", "", true));
        
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
        
        defaultModCfgs.put("meteors", new ThirdPartyModConfig("meteors", "meteors.cfg", "",
                "item:Frezarite Axe ID; item:Meteor Axe ID",
                "<item:Frezarite Axe ID>; <item:Meteor Axe ID>", "", true)
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
        
        defaultModCfgs.put("Railcraft", new ThirdPartyModConfig("Railcraft", "railcraft/railcraft.cfg", "", "item:tool.steel.axe", "<item:tool.steel.axe>",
                "", true).setOverrideIMC(false));
        
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
                "block:Log; block:MagicLog; block:Leaves; block:MagicLeaves; block:MagicLogSpecial; block:Hedge", "item:IronwoodAxe; item:SteeleafAxe; item:MinotaurAxe",
                "<item:IronwoodAxe>; <item:SteeleafAxe>; <item:MinotaurAxe>", "", true)
                .setOverrideIMC(false)
                .addConfigTreeDef("oak", new ConfigTreeDefinition("<block:Log>,0; <block:Log>,4; <block:Log>,8; <block:Log>,12",
                        "<block:Leaves>,0; <block:Leaves>,3; <block:Leaves>,8; <block:Leaves>,11"))
                .addConfigTreeDef("canopy", new ConfigTreeDefinition("<block:Log>,1; <block:Log>,5; <block:Log>,9; <block:Log>,13",
                        "<block:Leaves>, 1; <block:Leaves>,9"))
                .addConfigTreeDef("mangrove", new ConfigTreeDefinition("<block:Log>,2; <block:Log>,6; <block:Log>,10; <block:Log>,14",
                        "<block:Leaves>, 2; <block:Leaves>,10"))
                .addConfigTreeDef("darkwood", new ConfigTreeDefinition("<block:Log>,3; <block:Log>,7; <block:Log>,11;  <block:Log>,15",
                        "<block:Hedge>,1").setMaxLeafIDDist(2).setRequireLeafDecayCheck(false).setMaxHorLeafBreakDist(5))
                .addConfigTreeDef("time", new ConfigTreeDefinition("<block:MagicLog>,0; <block:MagicLog>,4; <block:MagicLog>,8; <block:MagicLog>,12; " +
                        "<block:MagicLogSpecial>,0",
                        "<block:MagicLeaves>,0; <block:MagicLeaves>,8"))
                .addConfigTreeDef("transformation", new ConfigTreeDefinition("<block:MagicLog>,1; <block:MagicLog>,5; <block:MagicLog>,9; <block:MagicLog>,13; " +
                        "<block:MagicLogSpecial>,1",
                        "<block:MagicLeaves>,1; <block:MagicLeaves>,9"))
                .addConfigTreeDef("miner", new ConfigTreeDefinition("<block:MagicLog>,2; <block:MagicLog>,6; <block:MagicLog>,10; <block:MagicLog>,14; " +
                        "<block:MagicLogSpecial>,2",
                        "<block:MagicLeaves>,2; <block:MagicLeaves>,10").setOnlyDestroyUpwards(false))
                .addConfigTreeDef("sorting", new ConfigTreeDefinition("<block:MagicLog>,3; <block:MagicLog>,7; <block:MagicLog>,11; <block:MagicLog>,15; " +
                        "<block:MagicLogSpecial>,3",
                        "<block:MagicLeaves>,3; <block:MagicLeaves>,11")));
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
            {
                TCLog.debug("Loading file config for mod %s (config category %s)...", cc.get(Strings.MOD_ID).getString(), ctgy);
                registerUserModConfig(new ThirdPartyModConfig(config, ctgy));
            }
        }
    }
    
    protected void imcSendMessageBoP()
    {
        if (Loader.isModLoaded("TreeCapitator"))
        {
            NBTTagCompound tpModCfg = new NBTTagCompound();
            tpModCfg.setString("modID", "BiomesOPlenty");
            tpModCfg.setString("configPath", "BiomesOPlenty.cfg");
            tpModCfg.setString("blockConfigKeys", "block:Bamboo ID; block:Colourized Leaves ID; block:Fruit Leaf Block ID; " +
                    "block:Leaf Block ID 1; block:Leaf Block ID 2; block:Log Block ID 1; block:Log Block ID 2; " +
                    "block:Log Block ID 3; block:Log Block ID 4; block:Petal ID");
            tpModCfg.setString("itemConfigKeys", "item:Muddy Axe ID; item:Amethyst Axe ID");
            tpModCfg.setString("axeIDList", "<item:Muddy Axe ID>; <item:Amethyst Axe ID>");
            tpModCfg.setString("shearsIDList", "");
            tpModCfg.setBoolean("useShiftedItemID", true);
            
            NBTTagList treeList = new NBTTagList();
            
            // Vanilla Oak additions
            NBTTagCompound tree = new NBTTagCompound();
            tree.setString("treeName", "vanilla_oak");
            tree.setString("logConfigKeys", "");
            tree.setString("leafConfigKeys", "<block:Leaf Block ID 1>,4; <block:Leaf Block ID 1>,7; " +
                    "<block:Leaf Block ID 1>,12; <block:Leaf Block ID 1>,15; <block:Fruit Leaf Block ID>; <block:Leaf Block ID 2>,0; " +
                    "<block:Leaf Block ID 2>,8; <block:Leaf Block ID 2>,2; <block:Leaf Block ID 2>,10");
            treeList.appendTag(tree);
            
            // Vanilla Birch additions
            tree = new NBTTagCompound();
            tree.setString("treeName", "vanilla_birch");
            tree.setString("logConfigKeys", "");
            tree.setString("leafConfigKeys", "<block:Leaf Block ID 1>,0; <block:Leaf Block ID 1>,8");
            treeList.appendTag(tree);
            
            // BoP acacia
            tree = new NBTTagCompound();
            tree.setString("treeName", "acacia");
            tree.setString("logConfigKeys", "<block:Log Block ID 1>,0; <block:Log Block ID 1>,4; <block:Log Block ID 1>,8");
            tree.setString("leafConfigKeys", "<block:Colourized Leaves ID>,0; <block:Colourized Leaves ID>,8");
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            // BoP cherry
            tree = new NBTTagCompound();
            tree.setString("treeName", "cherry");
            tree.setString("logConfigKeys", "<block:Log Block ID 1>,1; <block:Log Block ID 1>,5; <block:Log Block ID 1>,9");
            tree.setString("leafConfigKeys", "<block:Leaf Block ID 2>,1; <block:Leaf Block ID 2>,3; <block:Leaf Block ID 2>,9; <block:Leaf Block ID 2>,11");
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            // BoP darkwood
            tree = new NBTTagCompound();
            tree.setString("treeName", "darkwood");
            tree.setString("logConfigKeys", "<block:Log Block ID 1>,2; <block:Log Block ID 1>,6; <block:Log Block ID 1>,10");
            tree.setString("leafConfigKeys", "<block:Leaf Block ID 1>,3; <block:Leaf Block ID 1>,11");
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            // BoP fir
            tree = new NBTTagCompound();
            tree.setString("treeName", "fir");
            tree.setString("logConfigKeys", "<block:Log Block ID 1>,3; <block:Log Block ID 1>,7; <block:Log Block ID 1>,11");
            tree.setString("leafConfigKeys", "<block:Leaf Block ID 1>,5; <block:Leaf Block ID 1>,13");
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            
            // BoP holy
            tree = new NBTTagCompound();
            tree.setString("treeName", "holy");
            tree.setString("logConfigKeys", "<block:Log Block ID 2>,0; <block:Log Block ID 2>,4; <block:Log Block ID 2>,8");
            tree.setString("leafConfigKeys", "<block:Leaf Block ID 1>,6; <block:Leaf Block ID 1>,14");
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            // BoP magic
            tree = new NBTTagCompound();
            tree.setString("treeName", "magic");
            tree.setString("logConfigKeys", "<block:Log Block ID 2>,1; <block:Log Block ID 2>,5; <block:Log Block ID 2>,9");
            tree.setString("leafConfigKeys", "<block:Leaf Block ID 1>,2; <block:Leaf Block ID 1>,10");
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            // BoP mangrove
            tree = new NBTTagCompound();
            tree.setString("treeName", "mangrove");
            tree.setString("logConfigKeys", "<block:Log Block ID 2>,2; <block:Log Block ID 2>,6; <block:Log Block ID 2>,10");
            tree.setString("leafConfigKeys", "<block:Colourized Leaves ID>,1; <block:Colourized Leaves ID>,9");
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            // BoP palm
            tree = new NBTTagCompound();
            tree.setString("treeName", "palm");
            tree.setString("logConfigKeys", "<block:Log Block ID 2>,3; <block:Log Block ID 2>,7; <block:Log Block ID 2>,11");
            tree.setString("leafConfigKeys", "<block:Colourized Leaves ID>,2; <block:Colourized Leaves ID>,10");
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            
            // BoP redwood
            tree = new NBTTagCompound();
            tree.setString("treeName", "redwood");
            tree.setString("logConfigKeys", "<block:Log Block ID 3>,0; <block:Log Block ID 3>,4; <block:Log Block ID 3>,8");
            tree.setString("leafConfigKeys", "<block:Colourized Leaves ID>,3; <block:Colourized Leaves ID>,11");
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            // BoP willow
            tree = new NBTTagCompound();
            tree.setString("treeName", "willow");
            tree.setString("logConfigKeys", "<block:Log Block ID 3>,1; <block:Log Block ID 3>,5; <block:Log Block ID 3>,9");
            tree.setString("leafConfigKeys", "<block:Colourized Leaves ID>,4; <block:Colourized Leaves ID>,12");
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            // BoP dead
            tree = new NBTTagCompound();
            tree.setString("treeName", "dead");
            tree.setString("logConfigKeys", "<block:Log Block ID 3>,2; <block:Log Block ID 3>,6; <block:Log Block ID 3>,10");
            tree.setString("leafConfigKeys", "");
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            // BoP big_flower
            tree = new NBTTagCompound();
            tree.setString("treeName", "big_flower");
            tree.setString("logConfigKeys", "<block:Log Block ID 3>,3; <block:Log Block ID 3>,7; <block:Log Block ID 3>,11");
            tree.setString("leafConfigKeys", "<block:Petal ID>");
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            
            // BoP pine
            tree = new NBTTagCompound();
            tree.setString("treeName", "pine");
            tree.setString("logConfigKeys", "<block:Log Block ID 4>,0; <block:Log Block ID 4>,4; <block:Log Block ID 4>,8");
            tree.setString("leafConfigKeys", "<block:Colourized Leaves ID>,5; <block:Colourized Leaves ID>,13");
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            // BoP hellbark
            tree = new NBTTagCompound();
            tree.setString("treeName", "hellbark");
            tree.setString("logConfigKeys", "<block:Log Block ID 4>,1; <block:Log Block ID 4>,5; <block:Log Block ID 4>,9");
            tree.setString("leafConfigKeys", "<block:Leaf Block ID 2>,4; <block:Leaf Block ID 2>,12");
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            // BoP jacaranda
            tree = new NBTTagCompound();
            tree.setString("treeName", "jacaranda");
            tree.setString("logConfigKeys", "<block:Log Block ID 4>,2; <block:Log Block ID 4>,6; <block:Log Block ID 4>,10");
            tree.setString("leafConfigKeys", "<block:Leaf Block ID 1>,5; <block:Leaf Block ID 1>,13");
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            
            tpModCfg.setTag("trees", treeList);
            
            FMLInterModComms.sendMessage("TreeCapitator", "ThirdPartyModConfig", tpModCfg);
        }
    }
    
    /**
     * This method is provided as an example for mods to use if they want to send an IMC message to TreeCapitator. The message should be
     * sent in the @Init mod event method.
     */
    protected void imcSendMessageEBXL()
    {
        if (Loader.isModLoaded("TreeCapitator"))
        {
            NBTTagCompound tpModCfg = new NBTTagCompound();
            tpModCfg.setString("modID", "ExtraBiomesXL");
            tpModCfg.setString("configPath", "extrabiomes/extrabiomes.cfg");
            tpModCfg.setString("blockConfigKeys", "block:customlog.id; block:quarterlog0.id; block:quarterlog1.id; block:quarterlog2.id; block:quarterlog3.id; " +
                    "block:autumnleaves.id; block:greenleaves.id");
            tpModCfg.setString("itemConfigKeys", "");
            tpModCfg.setString("axeIDList", "");
            tpModCfg.setString("shearsIDList", "");
            tpModCfg.setBoolean("useShiftedItemID", true);
            
            NBTTagList treeList = new NBTTagList();
            
            // Vanilla Oak additions
            NBTTagCompound tree = new NBTTagCompound();
            tree.setString("treeName", "vanilla_oak");
            tree.setString("logConfigKeys", "<block:quarterlog0.id>,2; <block:quarterlog1.id>,2; <block:quarterlog2.id>,2; <block:quarterlog3.id>,2;");
            tree.setString("leafConfigKeys", "<block:autumnleaves.id>");
            treeList.appendTag(tree);
            
            // Vanilla Spruce additions
            tree = new NBTTagCompound();
            tree.setString("treeName", "vanilla_spruce");
            tree.setString("logConfigKeys", "");
            tree.setString("leafConfigKeys", "<block:autumnleaves.id>");
            treeList.appendTag(tree);
            
            // EBXL fir
            tree = new NBTTagCompound();
            tree.setString("treeName", "fir");
            tree.setString("logConfigKeys", "<block:customlog.id>,0; <block:quarterlog0.id>,1; <block:quarterlog1.id>,1; <block:quarterlog2.id>,1; <block:quarterlog3.id>,1");
            tree.setString("leafConfigKeys", "<block:greenleaves.id>,0; <block:greenleaves.id>,8");
            tree.setInteger("maxHorLeafBreakDist", 10);
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            
            // EBXL redwood
            tree = new NBTTagCompound();
            tree.setString("treeName", "redwood");
            tree.setString("logConfigKeys", "<block:quarterlog0.id>,0; <block:quarterlog1.id>,0; <block:quarterlog2.id>,0; <block:quarterlog3.id>,0");
            tree.setString("leafConfigKeys", "<block:greenleaves.id>,1; <block:greenleaves.id>,9");
            tree.setInteger("maxHorLeafBreakDist", 10);
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            
            // EBXL acacia
            tree = new NBTTagCompound();
            tree.setString("treeName", "acacia");
            tree.setString("logConfigKeys", "<block:customlog.id>,1");
            tree.setString("leafConfigKeys", "<block:greenleaves.id>,2");
            treeList.appendTag(tree);
            
            tpModCfg.setTag("trees", treeList);
            
            FMLInterModComms.sendMessage("TreeCapitator", "ThirdPartyModConfig", tpModCfg);
        }
    }
}
