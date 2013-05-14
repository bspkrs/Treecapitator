package bspkrs.treecapitator.fml;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import bspkrs.treecapitator.Strings;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TCSettings;
import bspkrs.util.Configuration;
import cpw.mods.fml.common.Loader;

public class TCConfigHandler
{
    private static TCConfigHandler instance;
    
    public static TCConfigHandler instance()
    {
        if (instance == null)
            new TCConfigHandler();
        
        return instance;
    }
    
    protected static TCConfigHandler setInstance(File file)
    {
        new TCConfigHandler(file);
        return instance;
    }
    
    private TCConfigHandler()
    {
        instance = this;
    }
    
    protected Configuration config;
    
    private TCConfigHandler(File file)
    {
        this();
        config = new Configuration(file);
        config.load();
        
        TCSettings.allowDebugLogging = config.getBoolean("allowDebugLogging", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.allowDebugLogging, Strings.allowDebugLoggingDesc);
        TCSettings.allowDebugOutput = config.getBoolean("allowDebugOutput", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.allowDebugOutput, Strings.allowDebugOutputDesc);
        TCSettings.allowItemDamage = config.getBoolean("allowItemDamage", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.allowItemDamage, Strings.allowItemDamageDesc);
        TCSettings.allowMoreBlocksThanDamage = config.getBoolean("allowMoreBlocksThanDamage", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.allowMoreBlocksThanDamage, Strings.allowMoreBlocksThanDamageDesc);
        TCSettings.allowSmartTreeDetection = config.getBoolean("allowSmartTreeDetection", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.allowSmartTreeDetection, Strings.allowSmartTreeDetectionDesc);
        TCSettings.axeIDList = config.getString("axeIDList", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.axeIDList, Strings.axeIDListDesc);
        TCSettings.damageIncreaseAmount = config.getFloat("damageIncreaseAmount", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.damageIncreaseAmount, 0.1F, 100.0F, Strings.damageIncreaseAmountDesc);
        TCSettings.damageMultiplier = config.getFloat("damageMultiplier", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.damageMultiplier, 0.1F, 50.0F, Strings.damageMultiplierDesc);
        TCSettings.destroyLeaves = config.getBoolean("destroyLeaves", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.destroyLeaves, Strings.destroyLeavesDesc);
        TCSettings.disableCreativeDrops = config.getBoolean("disableCreativeDrops", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.disableCreativeDrops, Strings.disableCreativeDropsDesc);
        TCSettings.disableInCreative = config.getBoolean("disableInCreative", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.disableInCreative, Strings.disableInCreativeDesc);
        TCSettings.increaseDamageEveryXBlocks = config.getInt("increaseDamageEveryXBlocks", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.increaseDamageEveryXBlocks, 1, 500, Strings.increaseDamageEveryXBlocksDesc);
        //        TCSettings.logHardnessNormal = config.getFloat("logHardnessNormal", Strings.BLOCK_CTGY, 
        //TCSettings.logHardnessNormal, 0F, 100F, Strings.logHardnessNormalDesc);
        //        TCSettings.logHardnessModified = config.getFloat("logHardnessModified", Strings.BLOCK_CTGY, 
        //TCSettings.logHardnessModified, 0F, 100F, Strings.logHardnessModifiedDesc);
        TCSettings.needItem = config.getBoolean("needItem", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.needItem, Strings.needItemDesc);
        TCSettings.shearIDList = config.getString("shearIDList", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.shearIDList, Strings.shearIDListDesc);
        TCSettings.shearLeaves = config.getBoolean("shearLeaves", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.shearLeaves, Strings.shearLeavesDesc);
        TCSettings.shearVines = config.getBoolean("shearVines", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.shearVines, Strings.shearVinesDesc);
        TCSettings.sneakAction = config.getString("sneakAction", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.sneakAction, Strings.sneakActionDesc);
        TCSettings.useIncreasingItemDamage = config.getBoolean("useIncreasingItemDamage", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.useIncreasingItemDamage, Strings.useIncreasingItemDamageDesc);
        TCSettings.useStrictBlockPairing = config.getBoolean("useStrictBlockPairing", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.useStrictBlockPairing, Strings.useStrictBlockPairingDesc);
        config.addCustomCategoryComment(Strings.GLOBALS_SETTINGS_CTGY, Strings.GLOBALS_SETTINGS_CTGY_DESC);
        
        TCSettings.breakSpeedModifier = config.getFloat("breakSpeedModifier", Strings.PER_TREE_DEFAULTS_CTGY,
                TCSettings.breakSpeedModifier, 0.01F, 1F, Strings.breakSpeedModifierDesc);
        TCSettings.maxHorLogBreakDist = config.getInt("maxHorLogBreakDist", Strings.PER_TREE_DEFAULTS_CTGY,
                TCSettings.maxHorLogBreakDist, -1, 100, Strings.maxHorBreakDistDesc);
        TCSettings.maxVerLogBreakDist = config.getInt("maxVerLogBreakDist", Strings.PER_TREE_DEFAULTS_CTGY,
                TCSettings.maxVerLogBreakDist, -1, 255, Strings.maxVerLogBreakDistDesc);
        TCSettings.maxLeafIDDist = config.getInt("maxLeafIDDist", Strings.PER_TREE_DEFAULTS_CTGY,
                TCSettings.maxLeafIDDist, 1, 8, Strings.maxLeafIDDistDesc);
        TCSettings.minLeavesToID = config.getInt("minLeavesToID", Strings.PER_TREE_DEFAULTS_CTGY,
                TCSettings.minLeavesToID, 0, 8, Strings.minLeavesToIDDesc);
        TCSettings.onlyDestroyUpwards = config.getBoolean("onlyDestroyUpwards", Strings.PER_TREE_DEFAULTS_CTGY,
                TCSettings.onlyDestroyUpwards, Strings.onlyDestroyUpwardsDesc);
        TCSettings.requireLeafDecayCheck = config.getBoolean("requireLeafDecayCheck", Strings.PER_TREE_DEFAULTS_CTGY,
                TCSettings.requireLeafDecayCheck, Strings.requireLeafDecayCheckDesc);
        config.addCustomCategoryComment(Strings.PER_TREE_DEFAULTS_CTGY, Strings.PER_TREE_DEFAULTS_CTGY_DESC);
        
        /*
         * Get / Set 3rd Party Mod configs
         */
        TCSettings.idResolverModID = config.getString("idResolverModID", Strings.TREE_MOD_CFG_CTGY,
                TCSettings.idResolverModID, Strings.idResolverModIDDesc);
        TCSettings.multiMineID = config.getString("multiMineID", Strings.TREE_MOD_CFG_CTGY,
                TCSettings.multiMineID, Strings.multiMineIDDesc);
        TCSettings.userConfigOverridesIMC = config.getBoolean("userConfigOverridesIMC", Strings.TREE_MOD_CFG_CTGY,
                TCSettings.userConfigOverridesIMC, Strings.userConfigOverridesIMCDesc);
        
        if (!config.hasCategory(Strings.TREE_MOD_CFG_CTGY + "." + Strings.VAN_TREES))
        {
            // Write default tree/mod settings to config
            Map<String, ThirdPartyModConfig> m = ModConfigRegistry.instance().defaultConfigs();
            for (Entry<String, ThirdPartyModConfig> e : m.entrySet())
                e.getValue().writeToConfiguration(config, Strings.TREE_MOD_CFG_CTGY + "." + e.getKey());
            
            TCLog.info("Looks like a fresh config; default config loaded.");
        }
        else
            TCLog.info("Proceeding to load tree/mod configs from file.");
        
        for (String ctgy : config.getCategoryNames())
        {
            if (ctgy.indexOf(Strings.TREE_MOD_CFG_CTGY + ".") != -1 && config.getCategory(ctgy).containsKey(Strings.MOD_ID))
            {
                if (Loader.isModLoaded(config.getCategory(ctgy).get(Strings.MOD_ID).getString()))
                    ModConfigRegistry.instance().registerUserModConfig(new ThirdPartyModConfig(config, ctgy));
            }
        }
        
        config.save();
    }
}
