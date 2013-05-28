package bspkrs.treecapitator.fml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
        defaultModCfgs = new HashMap<String, ThirdPartyModConfig>();
        defaultModCfgs.put(Strings.VAN_TREES_ITEMS_CTGY, new ThirdPartyModConfig());
        
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
        
        defaultModCfgs.put("Forestry", new ThirdPartyModConfig("Forestry", "forestry/base.conf", "block:log1; block:log2; block:log3; block:log4; block:leaves")
                .setOverrideIMC(false)
                .addConfigTreeDef("larch", new ConfigTreeDefinition("<block:log1>,0; <block:log1>,4; <block:log1>,8", "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("teak", new ConfigTreeDefinition("<block:log1>,1; <block:log1>,5; <block:log1>,9", "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("acacia", new ConfigTreeDefinition("<block:log1>,2; <block:log1>,6; <block:log1>,10", "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("lime", new ConfigTreeDefinition("<block:log1>,3; <block:log1>,7; <block:log1>,11", "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("chestnut", new ConfigTreeDefinition("<block:log2>,0; <block:log2>,4; <block:log2>,8", "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("wenge", new ConfigTreeDefinition("<block:log2>,1; <block:log2>,5; <block:log2>,9", "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("baobab", new ConfigTreeDefinition("<block:log2>,2; <block:log2>,6; <block:log2>,10", "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("sequoia", new ConfigTreeDefinition("<block:log2>,3; <block:log2>,7; <block:log2>,11", "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("kapok", new ConfigTreeDefinition("<block:log3>,0; <block:log3>,4; <block:log3>,8", "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("ebony", new ConfigTreeDefinition("<block:log3>,1; <block:log3>,5; <block:log3>,9", "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("mahogany", new ConfigTreeDefinition("<block:log3>,2; <block:log3>,6; <block:log3>,10", "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("balsa", new ConfigTreeDefinition("<block:log3>,3; <block:log3>,7; <block:log3>,11", "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("willow", new ConfigTreeDefinition("<block:log4>,0; <block:log4>,4; <block:log4>,8", "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("walnut", new ConfigTreeDefinition("<block:log4>,1; <block:log4>,5; <block:log4>,9", "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("boojum", new ConfigTreeDefinition("<block:log4>,2; <block:log4>,6; <block:log4>,10", "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false))
                .addConfigTreeDef("cherry", new ConfigTreeDefinition("<block:log4>,3; <block:log4>,7; <block:log4>,11", "<block:leaves>,0; <block:leaves>,8")
                        .setRequireLeafDecayCheck(false)));
        
        defaultModCfgs.put("IC2", new ThirdPartyModConfig("IC2", "IC2.cfg", "block:blockRubWood; block:blockRubLeaves",
                "item:itemToolBronzeAxe; item:itemToolChainsaw", "<item:itemToolBronzeAxe>; <item:itemToolChainsaw>",
                "<item:itemToolChainsaw>", true)
                .setOverrideIMC(false)
                .addConfigTreeDef("rubber", new ConfigTreeDefinition("<block:blockRubWood>", "<block:blockRubLeaves>")));
        
        /*
         * 
        Natura
        
        HashMap<String, String> inficraft = new HashMap<String, String>();
        TODO inficraft.put(Strings.MOD_ID, "Flora Trees");
        inficraft.put(Strings.CONFIG_PATH, "InfiCraft/FloraSoma.txt");
        inficraft.put(Strings.BLOCK_CFG_KEYS, "block:Bloodwood Block; block:Flora Leaves; block:Redwood Block; block:Sakura Leaves; block:Wood Block");
        
        HashMap<String, String> inficraft_bloodwood = new HashMap<String, String>();
        inficraft_bloodwood.put(Strings.LOGS, "<block:Bloodwood Block>");
        inficraft_bloodwood.put(Strings.LEAVES, "<block:Sakura Leaves>,2");
        
        HashMap<String, String> inficraft_eucalyptus = new HashMap<String, String>();
        inficraft_eucalyptus.put(Strings.LOGS, "<block:Wood Block>,0; <block:Wood Block>,4; <block:Wood Block>,8");
        inficraft_eucalyptus.put(Strings.LEAVES, "<block:Flora Leaves>,1");
        
        HashMap<String, String> inficraft_ghostwood = new HashMap<String, String>();
        inficraft_ghostwood.put(Strings.LOGS, "<block:Wood Block>,2; <block:Wood Block>, 6; <block:Wood Block>, 10");
        inficraft_ghostwood.put(Strings.LEAVES, "<block:Sakura Leaves>,1");
        
        HashMap<String, String> inficraft_hopseed = new HashMap<String, String>();
        inficraft_hopseed.put(Strings.LOGS, "<block:Wood Block>,3; <block:Wood Block>, 7; <block:Wood Block>, 11");
        inficraft_hopseed.put(Strings.LEAVES, "<block:Flora Leaves>,2");
        
        HashMap<String, String> inficraft_redwood = new HashMap<String, String>();
        inficraft_redwood.put(Strings.LOGS, "<block:Redwood Block>");
        inficraft_redwood.put(Strings.LEAVES, "<block:Flora Leaves>,0");
        
        HashMap<String, String> inficraft_sakura = new HashMap<String, String>();
        inficraft_sakura.put(Strings.LOGS, "<block:Wood Block>, 1; <block:Wood Block>, 5; <block:Wood Block>, 9");
        inficraft_sakura.put(Strings.LEAVES, "<block:Sakura Leaves>,0");
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
    }
    
    public Map<String, ThirdPartyModConfig> defaultConfigs()
    {
        return new HashMap<String, ThirdPartyModConfig>(defaultModCfgs);
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
            Map<String, ThirdPartyModConfig> m = ModConfigRegistry.instance().defaultConfigs();
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
                ModConfigRegistry.instance().registerUserModConfig(new ThirdPartyModConfig(config, ctgy));
        }
    }
    /*static
    {
        HashMap<String, String> vanilla_oak;
        HashMap<String, String> vanilla_birch;
        HashMap<String, String> vanilla_spruce;
        HashMap<String, String> vanilla_jungle;
        
        HashMap<String, String> biomesoplenty = new HashMap<String, String>();
        TODO biomesoplenty.put(Strings.MOD_ID, "BiomesOPlenty");
        biomesoplenty.put(Strings.CONFIG_PATH, "BiomesOPlenty.cfg");
        biomesoplenty.put(Strings.BLOCK_CFG_KEYS, "block:Acacia Leaves ID; block:Acacia Log ID; block:Apple Leaves ID; " +
                "block:Fruitless Apple Leaves ID; block:Bamboo ID; block:Bamboo Leaves ID; block:Cherry Log ID; " +
                "block:Dark Leaves ID; block:Dark Log ID; block:Dying Leaves ID; block:Dead Log ID; block:Fir Leaves ID; " +
                "block:Fir Log ID; block:Magic Log ID; block:Magic Leaves ID; block:Mangrove Leaves ID; block:Mangrove Log ID; " +
                "block:Maple Leaves ID; block:Orange Autumn Leaves ID; block:Origin Leaves ID; block:Palm Leaves ID; " +
                "block:Palm Log ID; block:Pink Cherry Leaves ID; block:Redwood Leaves ID; block:Redwood Log ID; " +
                "block:White Cherry Leaves ID; block:Willow Leaves ID; block:Willow Log ID; block:Yellow Autumn Leaves ID");
        biomesoplenty.put(Strings.ITEM_CFG_KEYS, "item:Muddy Axe ID");
        biomesoplenty.put(Strings.SHIFT_INDEX, "true");
        vanilla_oak = new HashMap<String, String>();
        vanilla_oak.put(Strings.LEAVES, "<block:Dying Leaves ID>; <block:Origin Leaves ID>; " +
                "<block:Apple Leaves ID>; <block:Fruitless Apple Leaves ID>; <block:Orange Autumn Leaves ID>; " +
                "<block:Maple Leaves ID>");
        vanilla_birch = new HashMap<String, String>();
        vanilla_birch.put(Strings.LEAVES, "<block:Yellow Autumn Leaves ID>");
        
        HashMap<String, String> biomesoplenty_dead = new HashMap<String, String>();
        biomesoplenty_dead.put(Strings.LOGS, "<block:Dead Log ID>");
        
        HashMap<String, String> biomesoplenty_acacia = new HashMap<String, String>();
        biomesoplenty_acacia.put(Strings.LOGS, "<block:Acacia Log ID>");
        biomesoplenty_acacia.put(Strings.LEAVES, "<block:Acacia Leaves ID>");
        
        HashMap<String, String> biomesoplenty_bamboo = new HashMap<String, String>();
        biomesoplenty_bamboo.put(Strings.LOGS, "<block:Bamboo ID>");
        biomesoplenty_bamboo.put(Strings.LEAVES, "<block:Bamboo Leaves ID>");
        
        HashMap<String, String> biomesoplenty_cherry = new HashMap<String, String>();
        biomesoplenty_cherry.put(Strings.LOGS, "<block:Cherry Log ID>");
        biomesoplenty_cherry.put(Strings.LEAVES, "<block:Pink Cherry Leaves ID>; <block:White Cherry Leaves ID>");
        
        HashMap<String, String> biomesoplenty_dark = new HashMap<String, String>();
        biomesoplenty_dark.put(Strings.LOGS, "<block:Dark Log ID>");
        biomesoplenty_dark.put(Strings.LEAVES, "<block:Dark Leaves ID>; <block:White Cherry Leaves ID>");
        
        HashMap<String, String> biomesoplenty_fir = new HashMap<String, String>();
        biomesoplenty_fir.put(Strings.LOGS, "<block:Fir Log ID>");
        biomesoplenty_fir.put(Strings.LEAVES, "<block:Fir Leaves ID>");
        
        HashMap<String, String> biomesoplenty_magic = new HashMap<String, String>();
        biomesoplenty_magic.put(Strings.LOGS, "<block:Magic Log ID>");
        biomesoplenty_magic.put(Strings.LEAVES, "<block:Magic Leaves ID>");
        
        HashMap<String, String> biomesoplenty_mangrove = new HashMap<String, String>();
        biomesoplenty_mangrove.put(Strings.LOGS, "<block:Mangrove Log ID>");
        biomesoplenty_mangrove.put(Strings.LEAVES, "<block:Mangrove Leaves ID>");
        
        HashMap<String, String> biomesoplenty_palm = new HashMap<String, String>();
        biomesoplenty_palm.put(Strings.LOGS, "<block:Palm Log ID>");
        biomesoplenty_palm.put(Strings.LEAVES, "<block:Palm Leaves ID>");
        
        HashMap<String, String> biomesoplenty_redwood = new HashMap<String, String>();
        biomesoplenty_redwood.put(Strings.LOGS, "<block:Redwood Log ID>");
        biomesoplenty_redwood.put(Strings.LEAVES, "<block:Redwood Leaves ID>");
        
        HashMap<String, String> biomesoplenty_willow = new HashMap<String, String>();
        biomesoplenty_willow.put(Strings.LOGS, "<block:Willow Log ID>");
        biomesoplenty_willow.put(Strings.LEAVES, "<block:Willow Leaves ID>");
        
        HashMap<String, String> divinerpg = new HashMap<String, String>();
        TODO divinerpg.put(Strings.MOD_ID, "DivineRPG");
        divinerpg.put(Strings.CONFIG_PATH, "DivineRPG.cfg");
        divinerpg.put(Strings.BLOCK_CFG_KEYS, "block:eucalyptus");
        divinerpg.put(Strings.ITEM_CFG_KEYS, "item:Bedrock Axe; item:Crystal Axe; item:Realmite Axe; item:azuriteaxe; item:corruptedaxe; " +
                "item:denseaxe; item:divineaxe; item:donatoraxe; item:energyaxe; item:mythrilaxe; item:plasmaaxe; item:serenityaxe; item:twilightaxe");
        
        divinerpg.put(Strings.AXE_ID_LIST, "<item:Bedrock Axe>; <item:Crystal Axe>; <item:Realmite Axe>; <item:azuriteaxe>; <item:corruptedaxe>; " +
                "<item:denseaxe>; <item:divineaxe>; <item:donatoraxe>; <item:energyaxe>; <item:mythrilaxe>; " +
                "<item:plasmaaxe>; <item:serenityaxe>; <item:twilightaxe>");
        divinerpg.put(Strings.SHIFT_INDEX, "true");
        
        HashMap<String, String> divinerpg_eucalyptus = new HashMap<String, String>();
        divinerpg_eucalyptus.put(Strings.LOGS, "<block:eucalyptus>");
        divinerpg_eucalyptus.put(Strings.LEAVES, "18"); // not sure on this? haven't found any of them yet and no sapling
        
        HashMap<String, String> mfreloaded = new HashMap<String, String>();
        TODO mfreloaded.put(Strings.MOD_ID, "MFReloaded");
        mfreloaded.put(Strings.CONFIG_PATH, "MFReloaded.cfg");
        mfreloaded.put(Strings.BLOCK_CFG_KEYS, "block:ID.RubberWood; block:ID.RubberLeaves; block:ID.RubberSapling");
        
        HashMap<String, String> mfr_rubber = new HashMap<String, String>();
        mfr_rubber.put(Strings.LOGS, "<block:ID.RubberWood>");
        mfr_rubber.put(Strings.LEAVES, "<block:ID.RubberLeaves>");
        
        HashMap<String, String> twilightforest = new HashMap<String, String>();
        TODO twilightforest.put(Strings.MOD_ID, "TwilightForest");
        twilightforest.put(Strings.CONFIG_PATH, "TwilightForest.cfg");
        twilightforest.put(Strings.BLOCK_CFG_KEYS, "block:Log; block:MagicLog; block:MagicStrings.LOGSpecial; block:Leaves; block:MagicLeaves; block:Hedge");
        twilightforest.put(Strings.ITEM_CFG_KEYS, "item:IronwoodAxe; item:SteeleafAxe; item:MinotaurAxe");
        twilightforest.put(Strings.AXE_ID_LIST, "<item:IronwoodAxe>; <item:SteeleafAxe>; <item:MinotaurAxe>; ");
        twilightforest.put(Strings.SHIFT_INDEX, "true");
        
        HashMap<String, String> twilight_oaks = new HashMap<String, String>();
        twilight_oaks.put(Strings.LOGS, "<block:Log>,0; <block:Log>,4; <block:Log>,8; <block:Log>,12");
        twilight_oaks.put(Strings.LEAVES, "<block:Leaves>,0; <block:Leaves>,3; <block:Leaves>,8; <block:Leaves>,11");
        
        HashMap<String, String> twilight_canopy = new HashMap<String, String>();
        twilight_canopy.put(Strings.LOGS, "<block:Log>,1; <block:Log>,5; <block:Log>,9; <block:Log>,13");
        twilight_canopy.put(Strings.LEAVES, "<block:Leaves>, 1; <block:Leaves>,9");
        
        HashMap<String, String> twilight_mangrove = new HashMap<String, String>();
        twilight_mangrove.put(Strings.LOGS, "<block:Log>,2; <block:Log>,6; <block:Log>,10; <block:Log>,14");
        twilight_mangrove.put(Strings.LEAVES, "<block:Leaves>, 1; <block:Leaves>,9");
        
        HashMap<String, String> twilight_darkwood = new HashMap<String, String>();
        twilight_darkwood.put(Strings.LOGS, "<block:Log>,3; <block:Log>,7; <block:Log>,11;  <block:Log>,15");
        twilight_darkwood.put(Strings.LEAVES, "<block:Hedge>,1");
        
        HashMap<String, String> twilight_time = new HashMap<String, String>();
        twilight_time.put(Strings.LOGS, "<block:MagicLog>,0; <block:Log>,4; <block:Log>,8; <block:MagicLog>,12");
        twilight_time.put(Strings.LEAVES, "<block:MagicLeaves>,0; <block:MagicLeaves>,8");
        
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
