package bspkrs.treecapitator.config;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTTagCompound;
import bspkrs.treecapitator.EnchantmentTreecapitating;
import bspkrs.treecapitator.util.Reference;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.config.Configuration;

public final class TCSettings
{
    // Global
    private final static boolean allowDebugLoggingDefault              = false;
    public static boolean        allowDebugLogging                     = allowDebugLoggingDefault;
    private final static boolean allowAutoAxeDetectionDefault          = true;
    public static boolean        allowAutoAxeDetection                 = allowAutoAxeDetectionDefault;
    private final static boolean allowAutoTreeDetectionDefault         = true;
    public static boolean        allowAutoTreeDetection                = allowAutoTreeDetectionDefault;
    private final static boolean allowItemDamageDefault                = true;
    public static boolean        allowItemDamage                       = allowItemDamageDefault;
    private final static boolean allowMoreBlocksThanDamageDefault      = false;
    public static boolean        allowMoreBlocksThanDamage             = allowMoreBlocksThanDamageDefault;
    private final static float   damageIncreaseAmountDefault           = 1.0F;
    public static float          damageIncreaseAmount                  = damageIncreaseAmountDefault;
    private final static float   damageMultiplierDefault               = 1.0F;
    public static float          damageMultiplier                      = damageMultiplierDefault;
    private final static boolean destroyLeavesDefault                  = true;
    public static boolean        destroyLeaves                         = destroyLeavesDefault;
    private final static boolean disableCreativeDropsDefault           = false;
    public static boolean        disableCreativeDrops                  = disableCreativeDropsDefault;
    private final static boolean disableInCreativeDefault              = false;
    public static boolean        disableInCreative                     = disableInCreativeDefault;
    private final static boolean enabledDefault                        = true;
    public static boolean        enabled                               = enabledDefault;
    private final static boolean enableEnchantmentModeDefault          = false;
    public static boolean        enableEnchantmentMode                 = enableEnchantmentModeDefault;
    private final static int     enchantmentIDDefault                  = 187;
    public static int            enchantmentID                         = enchantmentIDDefault;
    private static final int     enchantmentWeight                     = 5;
    private final static int     increaseDamageEveryXBlocksDefault     = 8;
    public static int            increaseDamageEveryXBlocks            = increaseDamageEveryXBlocksDefault;
    private final static boolean needItemDefault                       = true;
    public static boolean        needItem                              = needItemDefault;
    private final static boolean requireItemInAxeListForEnchantDefault = true;
    public static boolean        requireItemInAxeListForEnchant        = requireItemInAxeListForEnchantDefault;
    private final static boolean shearLeavesDefault                    = false;
    public static boolean        shearLeaves                           = shearLeavesDefault;
    private final static boolean shearVinesDefault                     = false;
    public static boolean        shearVines                            = shearVinesDefault;
    private final static String  sneakActionDefault                    = "disable";
    public static String         sneakAction                           = sneakActionDefault;
    private final static boolean stackDropsDefault                     = false;
    public static boolean        stackDrops                            = stackDropsDefault;
    private final static boolean itemsDropInPlaceDefault               = true;
    public static boolean        itemsDropInPlace                      = itemsDropInPlaceDefault;
    private final static boolean useIncreasingItemDamageDefault        = false;
    public static boolean        useIncreasingItemDamage               = useIncreasingItemDamageDefault;
    private final static boolean useStrictBlockPairingDefault          = true;
    public static boolean        useStrictBlockPairing                 = useStrictBlockPairingDefault;
    
    // Per-tree
    private final static boolean allowSmartTreeDetectionDefault        = true;
    public static boolean        allowSmartTreeDetection               = allowSmartTreeDetectionDefault;
    private final static float   breakSpeedModifierDefault             = 0.256F;
    public static float          breakSpeedModifier                    = breakSpeedModifierDefault;
    private final static int     maxHorLogBreakDistDefault             = 16;
    public static int            maxHorLogBreakDist                    = maxHorLogBreakDistDefault;
    private final static int     maxHorLeafBreakDistDefault            = 6;
    public static int            maxHorLeafBreakDist                   = maxHorLeafBreakDistDefault;
    private final static int     maxLeafIDDistDefault                  = 1;
    public static int            maxLeafIDDist                         = maxLeafIDDistDefault;
    private final static int     maxVerLogBreakDistDefault             = -1;
    public static int            maxVerLogBreakDist                    = maxVerLogBreakDistDefault;
    private final static int     minLeavesToIDDefault                  = 3;
    public static int            minLeavesToID                         = minLeavesToIDDefault;
    private final static boolean onlyDestroyUpwardsDefault             = true;
    public static boolean        onlyDestroyUpwards                    = onlyDestroyUpwardsDefault;
    private final static boolean requireLeafDecayCheckDefault          = true;
    public static boolean        requireLeafDecayCheck                 = requireLeafDecayCheckDefault;
    private final static boolean useAdvancedTopLogLogicDefault         = true;
    public static boolean        useAdvancedTopLogLogic                = useAdvancedTopLogLogicDefault;
    
    // Mod config settings (Forge only)
    public final static String   multiMineModIDDefault                 = "AS_MultiMine";
    public static String         multiMineModID                        = multiMineModIDDefault;
    public final static boolean  userConfigOverridesIMCDefault         = false;
    public static boolean        userConfigOverridesIMC                = userConfigOverridesIMCDefault;
    public final static boolean  saveIMCConfigsToFileDefault           = true;
    public static boolean        saveIMCConfigsToFile                  = saveIMCConfigsToFileDefault;
    
    // Forge Only
    private final static boolean treeHeightDecidesBreakSpeedDefault    = true;
    public static boolean        treeHeightDecidesBreakSpeed           = treeHeightDecidesBreakSpeedDefault;
    private final static float   treeHeightModifierDefault             = 2.0F;
    public static float          treeHeightModifier                    = treeHeightModifierDefault;
    private final static boolean allowOreDictionaryLookupDefault       = true;
    public static boolean        allowOreDictionaryLookup              = allowOreDictionaryLookupDefault;
    private final static String  oreDictionaryLogStringsDefault        = "logWood, woodRubber,";
    public static String         oreDictionaryLogStrings               = oreDictionaryLogStringsDefault;
    private final static String  oreDictionaryLeafStringsDefault       = "treeLeaves,";
    public static String         oreDictionaryLeafStrings              = oreDictionaryLeafStringsDefault;
    private final static String  blockIDBlacklistDefault               = "";
    public static String         blockIDBlacklist                      = blockIDBlacklistDefault;
    private final static String  itemIDBlacklistDefault                = "";
    public static String         itemIDBlacklist                       = itemIDBlacklistDefault;
    
    // static object references
    public static Enchantment    treecapitating;
    
    private static TCSettings    instance;
    
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
        }
    }
    
    public void readFromNBT(NBTTagCompound ntc)
    {
        enabled = ntc.getBoolean("enabled");
        allowItemDamage = ntc.getBoolean("allowItemDamage");
        allowMoreBlocksThanDamage = ntc.getBoolean("allowMoreBlocksThanDamage");
        allowSmartTreeDetection = ntc.getBoolean("allowSmartTreeDetection");
        allowAutoTreeDetection = ntc.getBoolean(Reference.ALLOW_AUTO_TREE_DETECT);
        allowAutoAxeDetection = ntc.getBoolean(Reference.ALLOW_AUTO_AXE_DETECT);
        treeHeightDecidesBreakSpeed = ntc.getBoolean("treeHeightDecidesBreakSpeed");
        treeHeightModifier = ntc.getFloat("treeHeightModifier");
        breakSpeedModifier = ntc.getFloat(Reference.BREAK_SPEED_MOD);
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
        maxHorLogBreakDist = ntc.getInteger(Reference.MAX_H_LOG_DIST);
        maxHorLeafBreakDist = ntc.getInteger(Reference.MAX_H_LEAF_DIST);
        maxLeafIDDist = ntc.getInteger(Reference.MAX_LEAF_ID_DIST);
        maxVerLogBreakDist = ntc.getInteger(Reference.MAX_V_LOG_DIST);
        minLeavesToID = ntc.getInteger(Reference.MIN_LEAF_ID);
        needItem = ntc.getBoolean("needItem");
        onlyDestroyUpwards = ntc.getBoolean(Reference.ONLY_DESTROY_UPWARDS);
        requireItemInAxeListForEnchant = ntc.getBoolean("requireItemInAxeListForEnchant");
        requireLeafDecayCheck = ntc.getBoolean(Reference.REQ_DECAY_CHECK);
        shearLeaves = ntc.getBoolean("shearLeaves");
        shearVines = ntc.getBoolean("shearVines");
        sneakAction = ntc.getString("sneakAction");
        stackDrops = ntc.getBoolean("stackDrops");
        itemsDropInPlace = ntc.getBoolean("itemsDropInPlace");
        useAdvancedTopLogLogic = ntc.getBoolean(Reference.USE_ADV_TOP_LOG_LOGIC);
        useIncreasingItemDamage = ntc.getBoolean("useIncreasingItemDamage");
        useStrictBlockPairing = ntc.getBoolean("useStrictBlockPairing");
    }
    
    public void writeToNBT(NBTTagCompound ntc)
    {
        ntc.setBoolean("enabled", enabled);
        ntc.setBoolean("allowItemDamage", allowItemDamage);
        ntc.setBoolean("allowMoreBlocksThanDamage", allowMoreBlocksThanDamage);
        ntc.setBoolean("allowOreDictionaryLookup", allowOreDictionaryLookup);
        ntc.setBoolean("allowSmartTreeDetection", allowSmartTreeDetection);
        ntc.setBoolean(Reference.ALLOW_AUTO_TREE_DETECT, allowAutoTreeDetection);
        ntc.setBoolean(Reference.ALLOW_AUTO_AXE_DETECT, allowAutoAxeDetection);
        ntc.setBoolean("treeHeightDecidesBreakSpeed", treeHeightDecidesBreakSpeed);
        ntc.setFloat("treeHeightModifier", treeHeightModifier);
        ntc.setFloat(Reference.BREAK_SPEED_MOD, breakSpeedModifier);
        ntc.setFloat("damageIncreaseAmount", damageIncreaseAmount);
        ntc.setFloat("damageMultiplier", damageMultiplier);
        ntc.setBoolean("destroyLeaves", destroyLeaves);
        ntc.setBoolean("disableCreativeDrops", disableCreativeDrops);
        ntc.setBoolean("disableInCreative", disableInCreative);
        ntc.setBoolean("enableEnchantmentMode", enableEnchantmentMode);
        ntc.setInteger("enchantmentID", enchantmentID);
        ntc.setInteger("increaseDamageEveryXBlocks", increaseDamageEveryXBlocks);
        ntc.setInteger(Reference.MAX_H_LOG_DIST, maxHorLogBreakDist);
        ntc.setInteger(Reference.MAX_H_LEAF_DIST, maxHorLeafBreakDist);
        ntc.setInteger(Reference.MAX_LEAF_ID_DIST, maxLeafIDDist);
        ntc.setInteger(Reference.MAX_V_LOG_DIST, maxVerLogBreakDist);
        ntc.setInteger(Reference.MIN_LEAF_ID, minLeavesToID);
        ntc.setBoolean("needItem", needItem);
        ntc.setBoolean(Reference.ONLY_DESTROY_UPWARDS, onlyDestroyUpwards);
        ntc.setString("oreDictionaryLogStrings", oreDictionaryLogStrings);
        ntc.setString("oreDictionaryLeafStrings", oreDictionaryLeafStrings);
        ntc.setBoolean("requireItemInAxeListForEnchant", requireItemInAxeListForEnchant);
        ntc.setBoolean(Reference.REQ_DECAY_CHECK, requireLeafDecayCheck);
        ntc.setBoolean("shearLeaves", shearLeaves);
        ntc.setBoolean("shearVines", shearVines);
        ntc.setString("sneakAction", sneakAction);
        ntc.setBoolean("stackDrops", stackDrops);
        ntc.setBoolean("itemsDropInPlace", itemsDropInPlace);
        ntc.setBoolean(Reference.USE_ADV_TOP_LOG_LOGIC, useAdvancedTopLogLogic);
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
        // Since I moved that setting...
        config.moveProperty(Reference.MISC_CTGY, "sneakAction", Reference.TREE_CHOP_BEHAVIOR_CTGY);
        
        // Misc settings
        allowDebugLogging = config.getBoolean(Reference.ALLOW_DEBUG_LOGGING, Reference.MISC_CTGY,
                allowDebugLoggingDefault, Reference.allowDebugLoggingDesc, Reference.LANG_KEY_BASE + Reference.ALLOW_DEBUG_LOGGING);
        disableCreativeDrops = config.getBoolean("disableCreativeDrops", Reference.MISC_CTGY,
                disableCreativeDropsDefault, Reference.disableCreativeDropsDesc, Reference.LANG_KEY_BASE + "disableCreativeDrops");
        disableInCreative = config.getBoolean("disableInCreative", Reference.MISC_CTGY,
                disableInCreativeDefault, Reference.disableInCreativeDesc, Reference.LANG_KEY_BASE + "disableInCreative");
        allowOreDictionaryLookup = config.getBoolean("allowOreDictionaryLookup", Reference.MISC_CTGY,
                allowOreDictionaryLookupDefault, Reference.allowOreDictionaryLookupDesc, Reference.LANG_KEY_BASE + "allowOreDictionaryLookup");
        oreDictionaryLogStrings = config.getString("oreDictionaryLogStrings", Reference.MISC_CTGY,
                oreDictionaryLogStringsDefault, Reference.oreDictionaryLogStringsDesc, Reference.LANG_KEY_BASE + "oreDictionaryLogStrings");
        oreDictionaryLeafStrings = config.getString("oreDictionaryLeafStrings", Reference.MISC_CTGY,
                oreDictionaryLeafStringsDefault, Reference.oreDictionaryLeafStringsDesc, Reference.LANG_KEY_BASE + "oreDictionaryLeafStrings");
        blockIDBlacklist = config.getString("blockIDBlacklist", Reference.MISC_CTGY,
                blockIDBlacklistDefault, Reference.blockIDBlacklistDesc, Reference.LANG_KEY_BASE + "blockIDBlacklist");
        itemIDBlacklist = config.getString("itemIDBlacklist", Reference.MISC_CTGY,
                itemIDBlacklistDefault, Reference.itemIDBlacklistDesc, Reference.LANG_KEY_BASE + "itemIDBlacklist");
        config.addCustomCategoryLanguageKey(Reference.MISC_CTGY, Reference.LANG_KEY_BASE + Reference.CTGY_LANG_KEY + Reference.MISC_CTGY);
        
        // Break Speed settings
        breakSpeedModifier = config.getFloat(Reference.BREAK_SPEED_MOD, Reference.BREAK_SPEED_CTGY,
                breakSpeedModifierDefault, 0.01F, 1F, Reference.breakSpeedModifierDesc, Reference.LANG_KEY_BASE + Reference.BREAK_SPEED_MOD);
        treeHeightDecidesBreakSpeed = config.getBoolean("treeHeightDecidesBreakSpeed", Reference.BREAK_SPEED_CTGY,
                treeHeightDecidesBreakSpeedDefault, Reference.treeHeightDecidesBreakSpeedDesc, Reference.LANG_KEY_BASE + "treeHeightDecidesBreakSpeed");
        treeHeightModifier = config.getFloat("treeHeightModifier", Reference.BREAK_SPEED_CTGY,
                treeHeightModifierDefault, 0.25F, 10.0F, Reference.treeHeightModifierDesc, Reference.LANG_KEY_BASE + "treeHeightModifier");
        config.addCustomCategoryLanguageKey(Reference.BREAK_SPEED_CTGY, Reference.LANG_KEY_BASE + Reference.CTGY_LANG_KEY + Reference.BREAK_SPEED_CTGY);
        
        // Item settings
        allowAutoAxeDetection = config.getBoolean(Reference.ALLOW_AUTO_AXE_DETECT, Reference.ITEM_CTGY,
                allowAutoAxeDetectionDefault, Reference.allowAutoAxeDetectionDesc, Reference.LANG_KEY_BASE + Reference.ALLOW_AUTO_AXE_DETECT);
        needItem = config.getBoolean("needItem", Reference.ITEM_CTGY,
                needItemDefault, Reference.needItemDesc, Reference.LANG_KEY_BASE + "needItem");
        allowItemDamage = config.getBoolean("allowItemDamage", Reference.ITEM_CTGY,
                allowItemDamageDefault, Reference.allowItemDamageDesc, Reference.LANG_KEY_BASE + "allowItemDamage");
        allowMoreBlocksThanDamage = config.getBoolean("allowMoreBlocksThanDamage", Reference.ITEM_CTGY,
                allowMoreBlocksThanDamageDefault, Reference.allowMoreBlocksThanDamageDesc, Reference.LANG_KEY_BASE + "allowMoreBlocksThanDamage");
        increaseDamageEveryXBlocks = config.getInt("increaseDamageEveryXBlocks", Reference.ITEM_CTGY,
                increaseDamageEveryXBlocksDefault, 1, 500, Reference.increaseDamageEveryXBlocksDesc, Reference.LANG_KEY_BASE + "increaseDamageEveryXBlocks");
        useIncreasingItemDamage = config.getBoolean("useIncreasingItemDamage", Reference.ITEM_CTGY,
                useIncreasingItemDamageDefault, Reference.useIncreasingItemDamageDesc, Reference.LANG_KEY_BASE + "useIncreasingItemDamage");
        damageIncreaseAmount = config.getFloat("damageIncreaseAmount", Reference.ITEM_CTGY,
                damageIncreaseAmountDefault, 0.1F, 100.0F, Reference.damageIncreaseAmountDesc, Reference.LANG_KEY_BASE + "damageIncreaseAmount");
        damageMultiplier = config.getFloat("damageMultiplier", Reference.ITEM_CTGY,
                damageMultiplierDefault, 0.1F, 50.0F, Reference.damageMultiplierDesc, Reference.LANG_KEY_BASE + "damageMultiplier");
        config.addCustomCategoryLanguageKey(Reference.ITEM_CTGY, Reference.LANG_KEY_BASE + Reference.CTGY_LANG_KEY + Reference.ITEM_CTGY);
        
        // Tree Chop Behavior settings
        allowAutoTreeDetection = config.getBoolean(Reference.ALLOW_AUTO_TREE_DETECT, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                allowAutoTreeDetectionDefault, Reference.allowAutoTreeDetectionDesc, Reference.LANG_KEY_BASE + Reference.ALLOW_AUTO_TREE_DETECT);
        allowSmartTreeDetection = config.getBoolean(Reference.ALLOW_SMART_TREE_DETECT, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                allowSmartTreeDetectionDefault, Reference.allowSmartTreeDetectionDesc, Reference.LANG_KEY_BASE + Reference.ALLOW_SMART_TREE_DETECT);
        useAdvancedTopLogLogic = config.getBoolean(Reference.USE_ADV_TOP_LOG_LOGIC, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                useAdvancedTopLogLogicDefault, Reference.useAdvancedTopLogLogicDesc, Reference.LANG_KEY_BASE + Reference.USE_ADV_TOP_LOG_LOGIC);
        useStrictBlockPairing = config.getBoolean("useStrictBlockPairing", Reference.TREE_CHOP_BEHAVIOR_CTGY,
                useStrictBlockPairingDefault, Reference.useStrictBlockPairingDesc, Reference.LANG_KEY_BASE + "useStrictBlockPairing");
        destroyLeaves = config.getBoolean("destroyLeaves", Reference.TREE_CHOP_BEHAVIOR_CTGY,
                destroyLeavesDefault, Reference.destroyLeavesDesc, Reference.LANG_KEY_BASE + "destroyLeaves");
        shearLeaves = config.getBoolean("shearLeaves", Reference.TREE_CHOP_BEHAVIOR_CTGY,
                shearLeavesDefault, Reference.shearLeavesDesc, Reference.LANG_KEY_BASE + "shearLeaves");
        shearVines = config.getBoolean("shearVines", Reference.TREE_CHOP_BEHAVIOR_CTGY,
                shearVinesDefault, Reference.shearVinesDesc, Reference.LANG_KEY_BASE + "shearVines");
        maxHorLeafBreakDist = config.getInt(Reference.MAX_H_LEAF_DIST, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                maxHorLeafBreakDistDefault, -1, 100, Reference.maxHorLeafBreakDistDesc, Reference.LANG_KEY_BASE + Reference.MAX_H_LEAF_DIST);
        maxHorLogBreakDist = config.getInt(Reference.MAX_H_LOG_DIST, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                maxHorLogBreakDistDefault, -1, 100, Reference.maxHorLogBreakDistDesc, Reference.LANG_KEY_BASE + Reference.MAX_H_LOG_DIST);
        maxVerLogBreakDist = config.getInt(Reference.MAX_V_LOG_DIST, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                maxVerLogBreakDistDefault, -1, 255, Reference.maxVerLogBreakDistDesc, Reference.LANG_KEY_BASE + Reference.MAX_V_LOG_DIST);
        maxLeafIDDist = config.getInt(Reference.MAX_LEAF_ID_DIST, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                maxLeafIDDistDefault, 1, 8, Reference.maxLeafIDDistDesc, Reference.LANG_KEY_BASE + Reference.MAX_LEAF_ID_DIST);
        minLeavesToID = config.getInt(Reference.MIN_LEAF_ID, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                minLeavesToIDDefault, 0, 8, Reference.minLeavesToIDDesc, Reference.LANG_KEY_BASE + Reference.MIN_LEAF_ID);
        onlyDestroyUpwards = config.getBoolean(Reference.ONLY_DESTROY_UPWARDS, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                onlyDestroyUpwardsDefault, Reference.onlyDestroyUpwardsDesc, Reference.LANG_KEY_BASE + Reference.ONLY_DESTROY_UPWARDS);
        requireLeafDecayCheck = config.getBoolean(Reference.REQ_DECAY_CHECK, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                requireLeafDecayCheckDefault, Reference.requireLeafDecayCheckDesc, Reference.LANG_KEY_BASE + Reference.REQ_DECAY_CHECK);
        sneakAction = config.getString("sneakAction", Reference.TREE_CHOP_BEHAVIOR_CTGY,
                sneakActionDefault, Reference.sneakActionDesc, new String[] { "enable", "disable", "none" }, Reference.LANG_KEY_BASE + "sneakAction");
        stackDrops = config.getBoolean("stackDrops", Reference.TREE_CHOP_BEHAVIOR_CTGY,
                stackDropsDefault, Reference.stackDropsDesc, Reference.LANG_KEY_BASE + "stackDrops");
        itemsDropInPlace = config.getBoolean("itemsDropInPlace", Reference.TREE_CHOP_BEHAVIOR_CTGY,
                itemsDropInPlaceDefault, Reference.itemsDropInPlaceDesc, Reference.LANG_KEY_BASE + "itemsDropInPlace");
        config.addCustomCategoryLanguageKey(Reference.TREE_CHOP_BEHAVIOR_CTGY, Reference.LANG_KEY_BASE + Reference.CTGY_LANG_KEY + Reference.TREE_CHOP_BEHAVIOR_CTGY);
        
        // Enchantment Mode settings
        enableEnchantmentMode = config.getBoolean("enableEnchantmentMode", Reference.ENCHANTMENT_MODE_CTGY,
                enableEnchantmentModeDefault, Reference.enableEnchantmentModeDesc, Reference.LANG_KEY_BASE + "enableEnchantmentMode");
        handleEnchantmentID(config.getInt("enchantmentID", Reference.ENCHANTMENT_MODE_CTGY,
                enchantmentIDDefault, 0, Enchantment.enchantmentsList.length - 1, Reference.enchantmentIDDesc, Reference.LANG_KEY_BASE + "enchantmentID"));
        requireItemInAxeListForEnchant = config.getBoolean("requireItemInAxeListForEnchant", Reference.ENCHANTMENT_MODE_CTGY,
                requireItemInAxeListForEnchantDefault, Reference.requireItemInAxeListForEnchantDesc, Reference.LANG_KEY_BASE + "requireItemInAxeListForEnchant");
        config.addCustomCategoryLanguageKey(Reference.ENCHANTMENT_MODE_CTGY, Reference.LANG_KEY_BASE + Reference.CTGY_LANG_KEY + Reference.ENCHANTMENT_MODE_CTGY);
        
        enabled = config.getBoolean("enabled", Reference.SETTINGS_CTGY,
                enabledDefault, Reference.enabledDesc, Reference.LANG_KEY_BASE + "enabled");
        config.addCustomCategoryLanguageKey(Reference.SETTINGS_CTGY, Reference.LANG_KEY_BASE + Reference.CTGY_LANG_KEY + Reference.SETTINGS_CTGY);
        
        config.addCustomCategoryComment(Reference.SETTINGS_CTGY, "ATTENTION: Editing this file manually is no longer necessary UNLESS YOU ARE ADDING NEW MODS/TREES. \n" +
                "On the Mods list screen select the entry for Treecapitator, then click the Config button to modify these settings.");
        
        // Log configs if we are in debug logging mode
        if (allowDebugLogging)
        {
            TCLog.configs(config, Reference.MISC_CTGY);
            TCLog.configs(config, Reference.BREAK_SPEED_CTGY);
            TCLog.configs(config, Reference.ITEM_CTGY);
            TCLog.configs(config, Reference.TREE_CHOP_BEHAVIOR_CTGY);
            TCLog.configs(config, Reference.ENCHANTMENT_MODE_CTGY);
        }
    }
}
