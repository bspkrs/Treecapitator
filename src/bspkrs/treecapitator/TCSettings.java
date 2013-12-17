package bspkrs.treecapitator;

import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTTagCompound;
import bspkrs.util.Configuration;
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
    public static Block       wood;
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
    
    protected void readFromNBT(NBTTagCompound ntc)
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
    public void syncConfiguration(Configuration config)
    {
        // Global settings
        allowDebugLogging = config.getBoolean("allowDebugLogging", Strings.GLOBALS_SETTINGS_CTGY,
                allowDebugLogging, Strings.allowDebugLoggingDesc);
        allowDebugOutput = config.getBoolean("allowDebugOutput", Strings.GLOBALS_SETTINGS_CTGY,
                allowDebugOutput, Strings.allowDebugOutputDesc);
        allowItemDamage = config.getBoolean("allowItemDamage", Strings.GLOBALS_SETTINGS_CTGY,
                allowItemDamage, Strings.allowItemDamageDesc);
        allowMoreBlocksThanDamage = config.getBoolean("allowMoreBlocksThanDamage", Strings.GLOBALS_SETTINGS_CTGY,
                allowMoreBlocksThanDamage, Strings.allowMoreBlocksThanDamageDesc);
        damageIncreaseAmount = config.getFloat("damageIncreaseAmount", Strings.GLOBALS_SETTINGS_CTGY,
                damageIncreaseAmount, 0.1F, 100.0F, Strings.damageIncreaseAmountDesc);
        damageMultiplier = config.getFloat("damageMultiplier", Strings.GLOBALS_SETTINGS_CTGY,
                damageMultiplier, 0.1F, 50.0F, Strings.damageMultiplierDesc);
        destroyLeaves = config.getBoolean("destroyLeaves", Strings.GLOBALS_SETTINGS_CTGY,
                destroyLeaves, Strings.destroyLeavesDesc);
        disableCreativeDrops = config.getBoolean("disableCreativeDrops", Strings.GLOBALS_SETTINGS_CTGY,
                disableCreativeDrops, Strings.disableCreativeDropsDesc);
        disableInCreative = config.getBoolean("disableInCreative", Strings.GLOBALS_SETTINGS_CTGY,
                disableInCreative, Strings.disableInCreativeDesc);
        enableEnchantmentMode = config.getBoolean("enableEnchantmentMode", Strings.GLOBALS_SETTINGS_CTGY,
                enableEnchantmentMode, Strings.enableEnchantmentModeDesc);
        
        handleEnchantmentID(config.getInt("enchantmentID", Strings.GLOBALS_SETTINGS_CTGY,
                enchantmentID, 0, Enchantment.enchantmentsList.length - 1, Strings.enchantmentIDDesc));
        
        increaseDamageEveryXBlocks = config.getInt("increaseDamageEveryXBlocks", Strings.GLOBALS_SETTINGS_CTGY,
                increaseDamageEveryXBlocks, 1, 500, Strings.increaseDamageEveryXBlocksDesc);
        needItem = config.getBoolean("needItem", Strings.GLOBALS_SETTINGS_CTGY,
                needItem, Strings.needItemDesc);
        requireItemInAxeListForEnchant = config.getBoolean("requireItemInAxeListForEnchant", Strings.GLOBALS_SETTINGS_CTGY,
                requireItemInAxeListForEnchant, Strings.requireItemInAxeListForEnchantDesc);
        shearLeaves = config.getBoolean("shearLeaves", Strings.GLOBALS_SETTINGS_CTGY,
                shearLeaves, Strings.shearLeavesDesc);
        shearVines = config.getBoolean("shearVines", Strings.GLOBALS_SETTINGS_CTGY,
                shearVines, Strings.shearVinesDesc);
        sneakAction = config.getString("sneakAction", Strings.GLOBALS_SETTINGS_CTGY,
                sneakAction, Strings.sneakActionDesc);
        useIncreasingItemDamage = config.getBoolean("useIncreasingItemDamage", Strings.GLOBALS_SETTINGS_CTGY,
                useIncreasingItemDamage, Strings.useIncreasingItemDamageDesc);
        useStrictBlockPairing = config.getBoolean("useStrictBlockPairing", Strings.GLOBALS_SETTINGS_CTGY,
                useStrictBlockPairing, Strings.useStrictBlockPairingDesc);
        treeHeightDecidesBreakSpeed = config.getBoolean("treeHeightDecidesBreakSpeed", Strings.GLOBALS_SETTINGS_CTGY,
                treeHeightDecidesBreakSpeed, Strings.treeHeightDecidesBreakSpeedDesc);
        treeHeightModifier = config.getFloat("treeHeightModifier", Strings.GLOBALS_SETTINGS_CTGY,
                treeHeightModifier, 0.25F, 10.0F, Strings.treeHeightModifierDesc);
        allowOreDictionaryLookup = config.getBoolean("allowOreDictionaryLookup", Strings.GLOBALS_SETTINGS_CTGY,
                allowOreDictionaryLookup, Strings.allowOreDictionaryLookupDesc);
        oreDictionaryLogStrings = config.getString("oreDictionaryLogStrings", Strings.GLOBALS_SETTINGS_CTGY,
                oreDictionaryLogStrings, Strings.oreDictionaryLogStringsDesc);
        oreDictionaryLeafStrings = config.getString("oreDictionaryLeafStrings", Strings.GLOBALS_SETTINGS_CTGY,
                oreDictionaryLeafStrings, Strings.oreDictionaryLeafStringsDesc);
        blockIDBlacklist = config.getString("blockIDBlacklist", Strings.GLOBALS_SETTINGS_CTGY,
                blockIDBlacklist, Strings.blockIDBlacklistDesc);
        itemIDBlacklist = config.getString("itemIDBlacklist", Strings.GLOBALS_SETTINGS_CTGY,
                itemIDBlacklist, Strings.itemIDBlacklistDesc);
        config.addCustomCategoryComment(Strings.GLOBALS_SETTINGS_CTGY, Strings.GLOBALS_SETTINGS_CTGY_DESC);
        
        // Per-tree settings
        allowSmartTreeDetection = config.getBoolean("allowSmartTreeDetection", Strings.PER_TREE_DEFAULTS_CTGY,
                allowSmartTreeDetection, Strings.allowSmartTreeDetectionDesc);
        breakSpeedModifier = config.getFloat("breakSpeedModifier", Strings.PER_TREE_DEFAULTS_CTGY,
                breakSpeedModifier, 0.01F, 1F, Strings.breakSpeedModifierDesc);
        maxHorLeafBreakDist = config.getInt("maxHorLeafBreakDist", Strings.PER_TREE_DEFAULTS_CTGY,
                maxHorLeafBreakDist, -1, 100, Strings.maxHorLeafBreakDistDesc);
        maxHorLogBreakDist = config.getInt("maxHorLogBreakDist", Strings.PER_TREE_DEFAULTS_CTGY,
                maxHorLogBreakDist, -1, 100, Strings.maxHorLogBreakDistDesc);
        maxVerLogBreakDist = config.getInt("maxVerLogBreakDist", Strings.PER_TREE_DEFAULTS_CTGY,
                maxVerLogBreakDist, -1, 255, Strings.maxVerLogBreakDistDesc);
        maxLeafIDDist = config.getInt("maxLeafIDDist", Strings.PER_TREE_DEFAULTS_CTGY,
                maxLeafIDDist, 1, 8, Strings.maxLeafIDDistDesc);
        minLeavesToID = config.getInt("minLeavesToID", Strings.PER_TREE_DEFAULTS_CTGY,
                minLeavesToID, 0, 8, Strings.minLeavesToIDDesc);
        onlyDestroyUpwards = config.getBoolean("onlyDestroyUpwards", Strings.PER_TREE_DEFAULTS_CTGY,
                onlyDestroyUpwards, Strings.onlyDestroyUpwardsDesc);
        requireLeafDecayCheck = config.getBoolean("requireLeafDecayCheck", Strings.PER_TREE_DEFAULTS_CTGY,
                requireLeafDecayCheck, Strings.requireLeafDecayCheckDesc);
        useAdvancedTopLogLogic = config.getBoolean("useAdvancedTopLogLogic", Strings.PER_TREE_DEFAULTS_CTGY,
                useAdvancedTopLogLogic, Strings.useAdvancedTopLogLogicDesc);
        config.addCustomCategoryComment(Strings.PER_TREE_DEFAULTS_CTGY, Strings.PER_TREE_DEFAULTS_CTGY_DESC);
        
        // Log configs if we are in debug logging mode
        Level level = TCLog.INSTANCE.getLogger().getLevel();
        if (allowDebugLogging)
            TCLog.INSTANCE.getLogger().setLevel(Level.CONFIG);
        
        TCLog.configs(config, Strings.GLOBALS_SETTINGS_CTGY);
        TCLog.configs(config, Strings.PER_TREE_DEFAULTS_CTGY);
        
        if (allowDebugLogging)
            TCLog.INSTANCE.getLogger().setLevel(level);
    }
}
