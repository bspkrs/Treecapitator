package bspkrs.treecapitator.config;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTTagCompound;
import bspkrs.treecapitator.EnchantmentTreecapitating;
import bspkrs.treecapitator.util.TCConst;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.config.Configuration;

public final class TCSettings
{
    // Global
    private final static boolean allowDebugLoggingDefault              = false;
    public static boolean        allowDebugLogging                     = allowDebugLoggingDefault;
    private final static boolean allowDebugOutputDefault               = false;
    public static boolean        allowDebugOutput                      = allowDebugOutputDefault;
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
    private final static int     maxHorLeafBreakDistDefault            = 4;
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
    private final static String  oreDictionaryLogStringsDefault        = "logWood,";
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
        stackDrops = ntc.getBoolean("stackDrops");
        itemsDropInPlace = ntc.getBoolean("itemsDropInPlace");
        useAdvancedTopLogLogic = ntc.getBoolean("useAdvancedTopLogLogic");
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
        ntc.setBoolean("stackDrops", stackDrops);
        ntc.setBoolean("itemsDropInPlace", itemsDropInPlace);
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
        // Since I moved that setting...
        config.moveProperty(TCConst.MISC_CTGY, "sneakAction", TCConst.TREE_CHOP_BEHAVIOR_CTGY);
        
        // Misc settings
        allowDebugLogging = config.getBoolean("allowDebugLogging", TCConst.MISC_CTGY,
                allowDebugLoggingDefault, TCConst.allowDebugLoggingDesc, "bspkrs.tc.configgui.allowDebugLogging");
        allowDebugOutput = config.getBoolean("allowDebugOutput", TCConst.MISC_CTGY,
                allowDebugOutputDefault, TCConst.allowDebugOutputDesc, "bspkrs.tc.configgui.allowDebugOutput");
        disableCreativeDrops = config.getBoolean("disableCreativeDrops", TCConst.MISC_CTGY,
                disableCreativeDropsDefault, TCConst.disableCreativeDropsDesc, "bspkrs.tc.configgui.disableCreativeDrops");
        disableInCreative = config.getBoolean("disableInCreative", TCConst.MISC_CTGY,
                disableInCreativeDefault, TCConst.disableInCreativeDesc, "bspkrs.tc.configgui.disableInCreative");
        allowOreDictionaryLookup = config.getBoolean("allowOreDictionaryLookup", TCConst.MISC_CTGY,
                allowOreDictionaryLookupDefault, TCConst.allowOreDictionaryLookupDesc, "bspkrs.tc.configgui.allowOreDictionaryLookup");
        oreDictionaryLogStrings = config.getString("oreDictionaryLogStrings", TCConst.MISC_CTGY,
                oreDictionaryLogStringsDefault, TCConst.oreDictionaryLogStringsDesc, "bspkrs.tc.configgui.oreDictionaryLogStrings");
        oreDictionaryLeafStrings = config.getString("oreDictionaryLeafStrings", TCConst.MISC_CTGY,
                oreDictionaryLeafStringsDefault, TCConst.oreDictionaryLeafStringsDesc, "bspkrs.tc.configgui.oreDictionaryLeafStrings");
        blockIDBlacklist = config.getString("blockIDBlacklist", TCConst.MISC_CTGY,
                blockIDBlacklistDefault, TCConst.blockIDBlacklistDesc, "bspkrs.tc.configgui.blockIDBlacklist");
        itemIDBlacklist = config.getString("itemIDBlacklist", TCConst.MISC_CTGY,
                itemIDBlacklistDefault, TCConst.itemIDBlacklistDesc, "bspkrs.tc.configgui.itemIDBlacklist");
        config.addCustomCategoryLanguageKey(TCConst.MISC_CTGY, "bspkrs.tc.configgui.ctgy." + TCConst.MISC_CTGY);
        
        // Break Speed settings
        breakSpeedModifier = config.getFloat("breakSpeedModifier", TCConst.BREAK_SPEED_CTGY,
                breakSpeedModifierDefault, 0.01F, 1F, TCConst.breakSpeedModifierDesc, "bspkrs.tc.configgui.breakSpeedModifier");
        treeHeightDecidesBreakSpeed = config.getBoolean("treeHeightDecidesBreakSpeed", TCConst.BREAK_SPEED_CTGY,
                treeHeightDecidesBreakSpeedDefault, TCConst.treeHeightDecidesBreakSpeedDesc, "bspkrs.tc.configgui.treeHeightDecidesBreakSpeed");
        treeHeightModifier = config.getFloat("treeHeightModifier", TCConst.BREAK_SPEED_CTGY,
                treeHeightModifierDefault, 0.25F, 10.0F, TCConst.treeHeightModifierDesc, "bspkrs.tc.configgui.treeHeightModifier");
        config.addCustomCategoryLanguageKey(TCConst.BREAK_SPEED_CTGY, "bspkrs.tc.configgui.ctgy." + TCConst.BREAK_SPEED_CTGY);
        
        // Item settings
        needItem = config.getBoolean("needItem", TCConst.ITEM_CTGY,
                needItemDefault, TCConst.needItemDesc, "bspkrs.tc.configgui.needItem");
        allowItemDamage = config.getBoolean("allowItemDamage", TCConst.ITEM_CTGY,
                allowItemDamageDefault, TCConst.allowItemDamageDesc, "bspkrs.tc.configgui.allowItemDamage");
        allowMoreBlocksThanDamage = config.getBoolean("allowMoreBlocksThanDamage", TCConst.ITEM_CTGY,
                allowMoreBlocksThanDamageDefault, TCConst.allowMoreBlocksThanDamageDesc, "bspkrs.tc.configgui.allowMoreBlocksThanDamage");
        increaseDamageEveryXBlocks = config.getInt("increaseDamageEveryXBlocks", TCConst.ITEM_CTGY,
                increaseDamageEveryXBlocksDefault, 1, 500, TCConst.increaseDamageEveryXBlocksDesc, "bspkrs.tc.configgui.increaseDamageEveryXBlocks");
        useIncreasingItemDamage = config.getBoolean("useIncreasingItemDamage", TCConst.ITEM_CTGY,
                useIncreasingItemDamageDefault, TCConst.useIncreasingItemDamageDesc, "bspkrs.tc.configgui.useIncreasingItemDamage");
        damageIncreaseAmount = config.getFloat("damageIncreaseAmount", TCConst.ITEM_CTGY,
                damageIncreaseAmountDefault, 0.1F, 100.0F, TCConst.damageIncreaseAmountDesc, "bspkrs.tc.configgui.damageIncreaseAmount");
        damageMultiplier = config.getFloat("damageMultiplier", TCConst.ITEM_CTGY,
                damageMultiplierDefault, 0.1F, 50.0F, TCConst.damageMultiplierDesc, "bspkrs.tc.configgui.damageMultiplier");
        config.addCustomCategoryLanguageKey(TCConst.ITEM_CTGY, "bspkrs.tc.configgui.ctgy." + TCConst.ITEM_CTGY);
        
        // Tree Chop Behavior settings
        allowSmartTreeDetection = config.getBoolean("allowSmartTreeDetection", TCConst.TREE_CHOP_BEHAVIOR_CTGY,
                allowSmartTreeDetectionDefault, TCConst.allowSmartTreeDetectionDesc, "bspkrs.tc.configgui.allowSmartTreeDetection");
        useAdvancedTopLogLogic = config.getBoolean("useAdvancedTopLogLogic", TCConst.TREE_CHOP_BEHAVIOR_CTGY,
                useAdvancedTopLogLogicDefault, TCConst.useAdvancedTopLogLogicDesc, "bspkrs.tc.configgui.useAdvancedTopLogLogic");
        useStrictBlockPairing = config.getBoolean("useStrictBlockPairing", TCConst.TREE_CHOP_BEHAVIOR_CTGY,
                useStrictBlockPairingDefault, TCConst.useStrictBlockPairingDesc, "bspkrs.tc.configgui.useStrictBlockPairing");
        destroyLeaves = config.getBoolean("destroyLeaves", TCConst.TREE_CHOP_BEHAVIOR_CTGY,
                destroyLeavesDefault, TCConst.destroyLeavesDesc, "bspkrs.tc.configgui.destroyLeaves");
        shearLeaves = config.getBoolean("shearLeaves", TCConst.TREE_CHOP_BEHAVIOR_CTGY,
                shearLeavesDefault, TCConst.shearLeavesDesc, "bspkrs.tc.configgui.shearLeaves");
        shearVines = config.getBoolean("shearVines", TCConst.TREE_CHOP_BEHAVIOR_CTGY,
                shearVinesDefault, TCConst.shearVinesDesc, "bspkrs.tc.configgui.shearVines");
        maxHorLeafBreakDist = config.getInt("maxHorLeafBreakDist", TCConst.TREE_CHOP_BEHAVIOR_CTGY,
                maxHorLeafBreakDistDefault, -1, 100, TCConst.maxHorLeafBreakDistDesc, "bspkrs.tc.configgui.maxHorLeafBreakDist");
        maxHorLogBreakDist = config.getInt("maxHorLogBreakDist", TCConst.TREE_CHOP_BEHAVIOR_CTGY,
                maxHorLogBreakDistDefault, -1, 100, TCConst.maxHorLogBreakDistDesc, "bspkrs.tc.configgui.maxHorLogBreakDist");
        maxVerLogBreakDist = config.getInt("maxVerLogBreakDist", TCConst.TREE_CHOP_BEHAVIOR_CTGY,
                maxVerLogBreakDistDefault, -1, 255, TCConst.maxVerLogBreakDistDesc, "bspkrs.tc.configgui.maxVerLogBreakDist");
        maxLeafIDDist = config.getInt("maxLeafIDDist", TCConst.TREE_CHOP_BEHAVIOR_CTGY,
                maxLeafIDDistDefault, 1, 8, TCConst.maxLeafIDDistDesc, "bspkrs.tc.configgui.maxLeafIDDist");
        minLeavesToID = config.getInt("minLeavesToID", TCConst.TREE_CHOP_BEHAVIOR_CTGY,
                minLeavesToIDDefault, 0, 8, TCConst.minLeavesToIDDesc, "bspkrs.tc.configgui.minLeavesToID");
        onlyDestroyUpwards = config.getBoolean("onlyDestroyUpwards", TCConst.TREE_CHOP_BEHAVIOR_CTGY,
                onlyDestroyUpwardsDefault, TCConst.onlyDestroyUpwardsDesc, "bspkrs.tc.configgui.onlyDestroyUpwards");
        requireLeafDecayCheck = config.getBoolean("requireLeafDecayCheck", TCConst.TREE_CHOP_BEHAVIOR_CTGY,
                requireLeafDecayCheckDefault, TCConst.requireLeafDecayCheckDesc, "bspkrs.tc.configgui.requireLeafDecayCheck");
        sneakAction = config.getString("sneakAction", TCConst.TREE_CHOP_BEHAVIOR_CTGY,
                sneakActionDefault, TCConst.sneakActionDesc, new String[] { "enable", "disable", "none" }, "bspkrs.tc.configgui.sneakAction");
        stackDrops = config.getBoolean("stackDrops", TCConst.TREE_CHOP_BEHAVIOR_CTGY,
                stackDropsDefault, TCConst.stackDropsDesc, "bspkrs.tc.configgui.stackDrops");
        itemsDropInPlace = config.getBoolean("itemsDropInPlace", TCConst.TREE_CHOP_BEHAVIOR_CTGY,
                itemsDropInPlaceDefault, TCConst.itemsDropInPlaceDesc, "bspkrs.tc.configgui.itemsDropInPlace");
        config.addCustomCategoryLanguageKey(TCConst.TREE_CHOP_BEHAVIOR_CTGY, "bspkrs.tc.configgui.ctgy." + TCConst.TREE_CHOP_BEHAVIOR_CTGY);
        
        // Enchantment Mode settings
        enableEnchantmentMode = config.getBoolean("enableEnchantmentMode", TCConst.ENCHANTMENT_MODE_CTGY,
                enableEnchantmentModeDefault, TCConst.enableEnchantmentModeDesc, "bspkrs.tc.configgui.enableEnchantmentMode");
        handleEnchantmentID(config.getInt("enchantmentID", TCConst.ENCHANTMENT_MODE_CTGY,
                enchantmentIDDefault, 0, Enchantment.enchantmentsList.length - 1, TCConst.enchantmentIDDesc, "bspkrs.tc.configgui.enchantmentID"));
        requireItemInAxeListForEnchant = config.getBoolean("requireItemInAxeListForEnchant", TCConst.ENCHANTMENT_MODE_CTGY,
                requireItemInAxeListForEnchantDefault, TCConst.requireItemInAxeListForEnchantDesc, "bspkrs.tc.configgui.requireItemInAxeListForEnchant");
        config.addCustomCategoryLanguageKey(TCConst.ENCHANTMENT_MODE_CTGY, "bspkrs.tc.configgui.ctgy." + TCConst.ENCHANTMENT_MODE_CTGY);
        
        enabled = config.getBoolean("enabled", TCConst.SETTINGS_CTGY,
                enabledDefault, TCConst.enabledDesc, "bspkrs.tc.configgui.enabled");
        config.addCustomCategoryLanguageKey(TCConst.SETTINGS_CTGY, "bspkrs.tc.configgui.ctgy." + TCConst.SETTINGS_CTGY);
        
        config.addCustomCategoryComment(TCConst.SETTINGS_CTGY, "ATTENTION: Editing this file manually is no longer necessary UNLESS YOU ARE ADDING NEW MODS/TREES. \n" +
                "On the Mods list screen select the entry for Treecapitator, then click the Config button to modify these settings.");
        
        // Log configs if we are in debug logging mode
        if (allowDebugLogging)
        {
            TCLog.configs(config, TCConst.MISC_CTGY);
            TCLog.configs(config, TCConst.BREAK_SPEED_CTGY);
            TCLog.configs(config, TCConst.ITEM_CTGY);
            TCLog.configs(config, TCConst.TREE_CHOP_BEHAVIOR_CTGY);
            TCLog.configs(config, TCConst.ENCHANTMENT_MODE_CTGY);
        }
    }
}
