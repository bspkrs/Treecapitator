package bspkrs.treecapitator.config;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTTagCompound;
import bspkrs.treecapitator.EnchantmentTreecapitating;
import bspkrs.treecapitator.util.TCConst;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.BSConfiguration;
import cpw.mods.fml.common.registry.LanguageRegistry;

public final class TCSettings
{
    // Global
    public static boolean     allowDebugLogging              = false;
    public static boolean     allowDebugOutput               = false;
    public static boolean     allowItemDamage                = true;
    public static boolean     allowMoreBlocksThanDamage      = false;
    public static float       damageIncreaseAmount           = 1.0F;
    public static float       damageMultiplier               = 1.0F;
    public static boolean     destroyLeaves                  = true;
    public static boolean     disableCreativeDrops           = false;
    public static boolean     disableInCreative              = false;
    public static boolean     enableEnchantmentMode          = false;
    public static int         enchantmentID                  = 187;
    private static final int  enchantmentWeight              = 5;
    public static int         increaseDamageEveryXBlocks     = 8;
    public static boolean     needItem                       = true;
    public static boolean     requireItemInAxeListForEnchant = true;
    public static boolean     shearLeaves                    = false;
    public static boolean     shearVines                     = false;
    public static String      sneakAction                    = "disable";
    public static boolean     useIncreasingItemDamage        = false;
    public static boolean     useStrictBlockPairing          = true;
    
    // Per-tree
    public static boolean     allowSmartTreeDetection        = true;
    public static float       breakSpeedModifier             = 0.256F;
    public static int         maxHorLogBreakDist             = 16;
    public static int         maxHorLeafBreakDist            = 4;
    public static int         maxLeafIDDist                  = 1;
    public static int         maxVerLogBreakDist             = -1;
    public static int         minLeavesToID                  = 3;
    public static boolean     onlyDestroyUpwards             = true;
    public static boolean     requireLeafDecayCheck          = true;
    public static boolean     useAdvancedTopLogLogic         = true;
    
    // Mod config settings (Forge only)
    public static String      idResolverModID                = "IDResolver";
    public static String      multiMineModID                 = "AS_MultiMine";
    public static boolean     userConfigOverridesIMC         = false;
    
    // Forge Only
    public static boolean     treeHeightDecidesBreakSpeed    = true;
    public static float       treeHeightModifier             = 2.0F;
    public static boolean     allowOreDictionaryLookup       = true;
    public static String      oreDictionaryLogStrings        = "logWood,";
    public static String      oreDictionaryLeafStrings       = "treeLeaves,";
    public static String      blockIDBlacklist               = "";
    public static String      itemIDBlacklist                = "";
    
    // static object references
    public static Enchantment treecapitating;
    
    private static TCSettings instance;
    
    public static TCSettings instance()
    {
        if (instance == null)
            new TCSettings();
        
        return instance;
    }
    
    private TCSettings()
    {
        instance = this;
    }
    
    public void handleEnchantmentID(int id)
    {
        if (id >= 0 && id < 256 && enableEnchantmentMode)
        {
            if (Enchantment.enchantmentsList[enchantmentID] != null)
                Enchantment.enchantmentsList[enchantmentID] = null;
            enchantmentID = id;
            treecapitating = new EnchantmentTreecapitating(enchantmentID, enchantmentWeight);
            treecapitating.setName("treecapitating");
            LanguageRegistry.instance().addStringLocalization("enchantment.treecapitating", "Treecapitating");
            /*if (isForge)
                Enchantment.addToBookList(treecapitating);*/
        }
    }
    
    public void readFromNBT(NBTTagCompound ntc)
    {
        allowItemDamage = ntc.getBoolean("allowItemDamage");
        allowMoreBlocksThanDamage = ntc.getBoolean("allowMoreBlocksThanDamage");
        allowSmartTreeDetection = ntc.getBoolean("allowSmartTreeDetection");
        treeHeightDecidesBreakSpeed = ntc.getBoolean("treeHeightDecidesBreakSpeed");
        treeHeightModifier = ntc.getFloat("treeHeightModifier");
        breakSpeedModifier = ntc.getFloat("breakSpeedModifier");
        damageIncreaseAmount = ntc.getFloat("damageIncreaseAmount");
        damageMultiplier = ntc.getFloat("damageMultiplier");
        destroyLeaves = ntc.getBoolean("destroyLeaves");
        disableCreativeDrops = ntc.getBoolean("disableCreativeDrops");
        disableInCreative = ntc.getBoolean("disableInCreative");
        enableEnchantmentMode = ntc.getBoolean("enableEnchantmentMode");
        allowOreDictionaryLookup = ntc.getBoolean("allowOreDictionaryLookup");
        oreDictionaryLogStrings = ntc.getString("oreDictionaryLogStrings");
        oreDictionaryLeafStrings = ntc.getString("oreDictionaryLeafStrings");
        
        handleEnchantmentID(ntc.getInteger("enchantmentID"));
        
        increaseDamageEveryXBlocks = ntc.getInteger("increaseDamageEveryXBlocks");
        maxHorLogBreakDist = ntc.getInteger("maxHorLogBreakDist");
        maxHorLeafBreakDist = ntc.getInteger("maxHorLeafBreakDist");
        maxLeafIDDist = ntc.getInteger("maxLeafIDDist");
        maxVerLogBreakDist = ntc.getInteger("maxVerLogBreakDist");
        minLeavesToID = ntc.getInteger("minLeavesToID");
        needItem = ntc.getBoolean("needItem");
        onlyDestroyUpwards = ntc.getBoolean("onlyDestroyUpwards");
        requireItemInAxeListForEnchant = ntc.getBoolean("requireItemInAxeListForEnchant");
        requireLeafDecayCheck = ntc.getBoolean("requireLeafDecayCheck");
        shearLeaves = ntc.getBoolean("shearLeaves");
        shearVines = ntc.getBoolean("shearVines");
        sneakAction = ntc.getString("sneakAction");
        useAdvancedTopLogLogic = ntc.getBoolean("useAdvancedTopLogLogic");
        useIncreasingItemDamage = ntc.getBoolean("useIncreasingItemDamage");
        useStrictBlockPairing = ntc.getBoolean("useStrictBlockPairing");
    }
    
    public void writeToNBT(NBTTagCompound ntc)
    {
        ntc.setBoolean("allowItemDamage", allowItemDamage);
        ntc.setBoolean("allowMoreBlocksThanDamage", allowMoreBlocksThanDamage);
        ntc.setBoolean("allowOreDictionaryLookup", allowOreDictionaryLookup);
        ntc.setBoolean("allowSmartTreeDetection", allowSmartTreeDetection);
        ntc.setBoolean("treeHeightDecidesBreakSpeed", treeHeightDecidesBreakSpeed);
        ntc.setFloat("treeHeightModifier", treeHeightModifier);
        ntc.setFloat("breakSpeedModifier", breakSpeedModifier);
        ntc.setFloat("damageIncreaseAmount", damageIncreaseAmount);
        ntc.setFloat("damageMultiplier", damageMultiplier);
        ntc.setBoolean("destroyLeaves", destroyLeaves);
        ntc.setBoolean("disableCreativeDrops", disableCreativeDrops);
        ntc.setBoolean("disableInCreative", disableInCreative);
        ntc.setBoolean("enableEnchantmentMode", enableEnchantmentMode);
        ntc.setInteger("enchantmentID", enchantmentID);
        ntc.setInteger("increaseDamageEveryXBlocks", increaseDamageEveryXBlocks);
        ntc.setInteger("maxHorLogBreakDist", maxHorLogBreakDist);
        ntc.setInteger("maxHorLeafBreakDist", maxHorLeafBreakDist);
        ntc.setInteger("maxLeafIDDist", maxLeafIDDist);
        ntc.setInteger("maxVerLogBreakDist", maxVerLogBreakDist);
        ntc.setInteger("minLeavesToID", minLeavesToID);
        ntc.setBoolean("needItem", needItem);
        ntc.setBoolean("onlyDestroyUpwards", onlyDestroyUpwards);
        ntc.setString("oreDictionaryLogStrings", oreDictionaryLogStrings);
        ntc.setString("oreDictionaryLeafStrings", oreDictionaryLeafStrings);
        ntc.setBoolean("requireItemInAxeListForEnchant", requireItemInAxeListForEnchant);
        ntc.setBoolean("requireLeafDecayCheck", requireLeafDecayCheck);
        ntc.setBoolean("shearLeaves", shearLeaves);
        ntc.setBoolean("shearVines", shearVines);
        ntc.setString("sneakAction", sneakAction);
        ntc.setBoolean("useAdvancedTopLogLogic", useAdvancedTopLogLogic);
        ntc.setBoolean("useIncreasingItemDamage", useIncreasingItemDamage);
        ntc.setBoolean("useStrictBlockPairing", useStrictBlockPairing);
    }
    
    /**
     * Synchronizes current values with the config object. config must be loaded (config.load()).
     * 
     * @param config
     */
    public void syncConfiguration(BSConfiguration config)
    {
        // Global settings
        allowDebugLogging = config.getBoolean("allowDebugLogging", TCConst.GLOBALS_SETTINGS_CTGY,
                allowDebugLogging, TCConst.allowDebugLoggingDesc);
        allowDebugOutput = config.getBoolean("allowDebugOutput", TCConst.GLOBALS_SETTINGS_CTGY,
                allowDebugOutput, TCConst.allowDebugOutputDesc);
        allowItemDamage = config.getBoolean("allowItemDamage", TCConst.GLOBALS_SETTINGS_CTGY,
                allowItemDamage, TCConst.allowItemDamageDesc);
        allowMoreBlocksThanDamage = config.getBoolean("allowMoreBlocksThanDamage", TCConst.GLOBALS_SETTINGS_CTGY,
                allowMoreBlocksThanDamage, TCConst.allowMoreBlocksThanDamageDesc);
        damageIncreaseAmount = config.getFloat("damageIncreaseAmount", TCConst.GLOBALS_SETTINGS_CTGY,
                damageIncreaseAmount, 0.1F, 100.0F, TCConst.damageIncreaseAmountDesc);
        damageMultiplier = config.getFloat("damageMultiplier", TCConst.GLOBALS_SETTINGS_CTGY,
                damageMultiplier, 0.1F, 50.0F, TCConst.damageMultiplierDesc);
        destroyLeaves = config.getBoolean("destroyLeaves", TCConst.GLOBALS_SETTINGS_CTGY,
                destroyLeaves, TCConst.destroyLeavesDesc);
        disableCreativeDrops = config.getBoolean("disableCreativeDrops", TCConst.GLOBALS_SETTINGS_CTGY,
                disableCreativeDrops, TCConst.disableCreativeDropsDesc);
        disableInCreative = config.getBoolean("disableInCreative", TCConst.GLOBALS_SETTINGS_CTGY,
                disableInCreative, TCConst.disableInCreativeDesc);
        enableEnchantmentMode = config.getBoolean("enableEnchantmentMode", TCConst.GLOBALS_SETTINGS_CTGY,
                enableEnchantmentMode, TCConst.enableEnchantmentModeDesc);
        
        handleEnchantmentID(config.getInt("enchantmentID", TCConst.GLOBALS_SETTINGS_CTGY,
                enchantmentID, 0, Enchantment.enchantmentsList.length - 1, TCConst.enchantmentIDDesc));
        
        increaseDamageEveryXBlocks = config.getInt("increaseDamageEveryXBlocks", TCConst.GLOBALS_SETTINGS_CTGY,
                increaseDamageEveryXBlocks, 1, 500, TCConst.increaseDamageEveryXBlocksDesc);
        needItem = config.getBoolean("needItem", TCConst.GLOBALS_SETTINGS_CTGY,
                needItem, TCConst.needItemDesc);
        requireItemInAxeListForEnchant = config.getBoolean("requireItemInAxeListForEnchant", TCConst.GLOBALS_SETTINGS_CTGY,
                requireItemInAxeListForEnchant, TCConst.requireItemInAxeListForEnchantDesc);
        shearLeaves = config.getBoolean("shearLeaves", TCConst.GLOBALS_SETTINGS_CTGY,
                shearLeaves, TCConst.shearLeavesDesc);
        shearVines = config.getBoolean("shearVines", TCConst.GLOBALS_SETTINGS_CTGY,
                shearVines, TCConst.shearVinesDesc);
        sneakAction = config.getString("sneakAction", TCConst.GLOBALS_SETTINGS_CTGY,
                sneakAction, TCConst.sneakActionDesc);
        useIncreasingItemDamage = config.getBoolean("useIncreasingItemDamage", TCConst.GLOBALS_SETTINGS_CTGY,
                useIncreasingItemDamage, TCConst.useIncreasingItemDamageDesc);
        useStrictBlockPairing = config.getBoolean("useStrictBlockPairing", TCConst.GLOBALS_SETTINGS_CTGY,
                useStrictBlockPairing, TCConst.useStrictBlockPairingDesc);
        treeHeightDecidesBreakSpeed = config.getBoolean("treeHeightDecidesBreakSpeed", TCConst.GLOBALS_SETTINGS_CTGY,
                treeHeightDecidesBreakSpeed, TCConst.treeHeightDecidesBreakSpeedDesc);
        treeHeightModifier = config.getFloat("treeHeightModifier", TCConst.GLOBALS_SETTINGS_CTGY,
                treeHeightModifier, 0.25F, 10.0F, TCConst.treeHeightModifierDesc);
        allowOreDictionaryLookup = config.getBoolean("allowOreDictionaryLookup", TCConst.GLOBALS_SETTINGS_CTGY,
                allowOreDictionaryLookup, TCConst.allowOreDictionaryLookupDesc);
        oreDictionaryLogStrings = config.getString("oreDictionaryLogStrings", TCConst.GLOBALS_SETTINGS_CTGY,
                oreDictionaryLogStrings, TCConst.oreDictionaryLogStringsDesc);
        oreDictionaryLeafStrings = config.getString("oreDictionaryLeafStrings", TCConst.GLOBALS_SETTINGS_CTGY,
                oreDictionaryLeafStrings, TCConst.oreDictionaryLeafStringsDesc);
        blockIDBlacklist = config.getString("blockIDBlacklist", TCConst.GLOBALS_SETTINGS_CTGY,
                blockIDBlacklist, TCConst.blockIDBlacklistDesc);
        itemIDBlacklist = config.getString("itemIDBlacklist", TCConst.GLOBALS_SETTINGS_CTGY,
                itemIDBlacklist, TCConst.itemIDBlacklistDesc);
        config.addCustomCategoryComment(TCConst.GLOBALS_SETTINGS_CTGY, TCConst.GLOBALS_SETTINGS_CTGY_DESC);
        
        // Per-tree settings
        allowSmartTreeDetection = config.getBoolean("allowSmartTreeDetection", TCConst.PER_TREE_DEFAULTS_CTGY,
                allowSmartTreeDetection, TCConst.allowSmartTreeDetectionDesc);
        breakSpeedModifier = config.getFloat("breakSpeedModifier", TCConst.PER_TREE_DEFAULTS_CTGY,
                breakSpeedModifier, 0.01F, 1F, TCConst.breakSpeedModifierDesc);
        maxHorLeafBreakDist = config.getInt("maxHorLeafBreakDist", TCConst.PER_TREE_DEFAULTS_CTGY,
                maxHorLeafBreakDist, -1, 100, TCConst.maxHorLeafBreakDistDesc);
        maxHorLogBreakDist = config.getInt("maxHorLogBreakDist", TCConst.PER_TREE_DEFAULTS_CTGY,
                maxHorLogBreakDist, -1, 100, TCConst.maxHorLogBreakDistDesc);
        maxVerLogBreakDist = config.getInt("maxVerLogBreakDist", TCConst.PER_TREE_DEFAULTS_CTGY,
                maxVerLogBreakDist, -1, 255, TCConst.maxVerLogBreakDistDesc);
        maxLeafIDDist = config.getInt("maxLeafIDDist", TCConst.PER_TREE_DEFAULTS_CTGY,
                maxLeafIDDist, 1, 8, TCConst.maxLeafIDDistDesc);
        minLeavesToID = config.getInt("minLeavesToID", TCConst.PER_TREE_DEFAULTS_CTGY,
                minLeavesToID, 0, 8, TCConst.minLeavesToIDDesc);
        onlyDestroyUpwards = config.getBoolean("onlyDestroyUpwards", TCConst.PER_TREE_DEFAULTS_CTGY,
                onlyDestroyUpwards, TCConst.onlyDestroyUpwardsDesc);
        requireLeafDecayCheck = config.getBoolean("requireLeafDecayCheck", TCConst.PER_TREE_DEFAULTS_CTGY,
                requireLeafDecayCheck, TCConst.requireLeafDecayCheckDesc);
        useAdvancedTopLogLogic = config.getBoolean("useAdvancedTopLogLogic", TCConst.PER_TREE_DEFAULTS_CTGY,
                useAdvancedTopLogLogic, TCConst.useAdvancedTopLogLogicDesc);
        config.addCustomCategoryComment(TCConst.PER_TREE_DEFAULTS_CTGY, TCConst.PER_TREE_DEFAULTS_CTGY_DESC);
        
        // Log configs if we are in debug logging mode
        if (allowDebugLogging)
        {
            TCLog.configs(config, TCConst.GLOBALS_SETTINGS_CTGY);
            TCLog.configs(config, TCConst.PER_TREE_DEFAULTS_CTGY);
        }
    }
}
