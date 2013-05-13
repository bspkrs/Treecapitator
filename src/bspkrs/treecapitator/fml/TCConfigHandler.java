package bspkrs.treecapitator.fml;

import java.io.File;

import bspkrs.treecapitator.Strings;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TCSettings;
import bspkrs.util.Configuration;

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
        
        /*
         * Get / Set 3rd Party Mod config lists
         */
        
        if (!config.hasCategory(Strings.TREE_MOD_CFG_CTGY))
        {
            TCSettings.idResolverModID = config.getString("idResolverModID", Strings.TREE_MOD_CFG_CTGY, TCSettings.idResolverModID, Strings.idResolverModIDDesc);
            TCSettings.multiMineID = config.getString("multiMineID", Strings.TREE_MOD_CFG_CTGY, TCSettings.multiMineID, Strings.multiMineIDDesc);
            
            //TODO: load default 3rd party configs
            TCLog.info("Default block config loaded.");
        }
        else
        {
            TCSettings.idResolverModID = config.getString("idResolverModID", Strings.TREE_MOD_CFG_CTGY, TCSettings.idResolverModID, Strings.idResolverModIDDesc);
            TCSettings.multiMineID = config.getString("multiMineID", Strings.TREE_MOD_CFG_CTGY, TCSettings.multiMineID, Strings.multiMineIDDesc);
            
            // TODO: load config shit
            TCLog.info("File block config loaded.");
        }
        
        /* config.addCustomCategoryComment(Strings.TREE_BLOCK_CTGY, Strings.configBlockIDDesc);
         
         if (!config.hasCategory(Strings.THIRD_PARTY_CFG_CTGY))
         {
             for (String key : TCSettings.thirdPartyConfig.keySet())
             {
                 HashMap<String, String> tpconfig = TCSettings.thirdPartyConfig.get(key);
                 for (String entry : tpconfig.keySet())
                     config.get(Strings.THIRD_PARTY_CFG_CTGY + "." + key, entry, tpconfig.get(entry));
                 
             }
         }
         else
         {
             TCSettings.thirdPartyConfig = new HashMap<String, HashMap<String, String>>();
             
             for (String ctgy : config.getCategoryNames())
             {
                 if (ctgy.indexOf(Strings.THIRD_PARTY_CFG_CTGY + ".") != -1)
                 {
                     HashMap<String, String> entries = new HashMap<String, String>();
                     ConfigCategory currentCtgy = config.getCategory(ctgy);
                     
                     // fixed issue with old configs not having the right prop name
                     if (currentCtgy.containsKey("modName"))
                         config.renameProperty(ctgy, "modName", Strings.MOD_ID);
                     
                     if (currentCtgy.containsKey(Strings.MOD_ID))
                     {
                         for (String tpCfgEntry : currentCtgy.keySet())
                             entries.put(tpCfgEntry, currentCtgy.get(tpCfgEntry).getString());
                         
                         if (entries.containsKey(Strings.ITEM_VALUES) && !entries.containsKey(Strings.SHIFT_INDEX))
                             entries.put(Strings.SHIFT_INDEX, "true");
                         
                         TCSettings.thirdPartyConfig.put(ctgy, entries);
                     }
                 }
             }
         }
         
         //Strings.localBlockIDList = TreeCapitator.getStringFromConfigBlockList();
         //config.get(Strings.BLOCK_CTGY, "localTreeConfig", "", TreeCapitator.localBlockIDListDesc).set(Strings.localBlockIDList);
         
         config.addCustomCategoryComment(Strings.THIRD_PARTY_CFG_CTGY, Strings.thirdPartyConfigDesc);*/
        
        config.save();
    }
}
