package bspkrs.treecapitator.config;

import java.util.LinkedHashSet;

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
        enabled = ntc.getBoolean(Reference.ENABLED);
        allowItemDamage = ntc.getBoolean(Reference.ALLOW_ITEM_DAMAGE);
        allowMoreBlocksThanDamage = ntc.getBoolean(Reference.ALLOW_MORE_BLOCKS_THAN_DAMAGE);
        allowSmartTreeDetection = ntc.getBoolean(Reference.ALLOW_SMART_TREE_DETECT);
        allowAutoTreeDetection = ntc.getBoolean(Reference.ALLOW_AUTO_TREE_DETECT);
        allowAutoAxeDetection = ntc.getBoolean(Reference.ALLOW_AUTO_AXE_DETECT);
        treeHeightDecidesBreakSpeed = ntc.getBoolean(Reference.TREE_HEIGHT_DECIDES_BREAK_SPEED);
        treeHeightModifier = ntc.getFloat(Reference.TREE_HEIGHT_MOD);
        breakSpeedModifier = ntc.getFloat(Reference.BREAK_SPEED_MOD);
        damageIncreaseAmount = ntc.getFloat(Reference.DAMAGE_INCREASE_AMOUNT);
        damageMultiplier = ntc.getFloat(Reference.DAMAGE_MULTIPLIER);
        destroyLeaves = ntc.getBoolean(Reference.DESTROY_LEAVES);
        disableCreativeDrops = ntc.getBoolean(Reference.DISABLE_CREATIVE_DROPS);
        disableInCreative = ntc.getBoolean(Reference.DISABLE_IN_CREATIVE);
        enableEnchantmentMode = ntc.getBoolean(Reference.ENABLE_ENCHANT_MODE);
        allowOreDictionaryLookup = ntc.getBoolean(Reference.ALLOW_ORE_DICT_LOOKUP);
        oreDictionaryLogStrings = ntc.getString(Reference.ORE_DICT_LOG_KEYS);
        oreDictionaryLeafStrings = ntc.getString(Reference.ORE_DICT_LEAF_KEYS);
        
        handleEnchantmentID(ntc.getInteger(Reference.ENCHANT_ID));
        
        increaseDamageEveryXBlocks = ntc.getInteger(Reference.INCREASE_DAMAGE_X_BLOCKS);
        maxHorLogBreakDist = ntc.getInteger(Reference.MAX_H_LOG_DIST);
        maxHorLeafBreakDist = ntc.getInteger(Reference.MAX_H_LEAF_DIST);
        maxLeafIDDist = ntc.getInteger(Reference.MAX_LEAF_ID_DIST);
        maxVerLogBreakDist = ntc.getInteger(Reference.MAX_V_LOG_DIST);
        minLeavesToID = ntc.getInteger(Reference.MIN_LEAF_ID);
        needItem = ntc.getBoolean(Reference.NEED_ITEM);
        onlyDestroyUpwards = ntc.getBoolean(Reference.ONLY_DESTROY_UPWARDS);
        requireItemInAxeListForEnchant = ntc.getBoolean(Reference.REQ_ITEM_IN_AXE_LIST_ENCHANT);
        requireLeafDecayCheck = ntc.getBoolean(Reference.REQ_DECAY_CHECK);
        shearLeaves = ntc.getBoolean(Reference.SHEAR_LEAVES);
        shearVines = ntc.getBoolean(Reference.SHEAR_VINES);
        sneakAction = ntc.getString(Reference.SNEAK_ACTION);
        stackDrops = ntc.getBoolean(Reference.STACK_DROPS);
        itemsDropInPlace = ntc.getBoolean(Reference.ITEMS_DROP_IN_PLACE);
        useAdvancedTopLogLogic = ntc.getBoolean(Reference.USE_ADV_TOP_LOG_LOGIC);
        useIncreasingItemDamage = ntc.getBoolean(Reference.USE_INCREASING_ITEM_DAMAGE);
        useStrictBlockPairing = ntc.getBoolean(Reference.USE_STRICT_BLOCK_PAIRING);
    }
    
    public void writeToNBT(NBTTagCompound ntc)
    {
        ntc.setBoolean(Reference.ENABLED, enabled);
        ntc.setBoolean(Reference.ALLOW_ITEM_DAMAGE, allowItemDamage);
        ntc.setBoolean(Reference.ALLOW_MORE_BLOCKS_THAN_DAMAGE, allowMoreBlocksThanDamage);
        ntc.setBoolean(Reference.ALLOW_ORE_DICT_LOOKUP, allowOreDictionaryLookup);
        ntc.setBoolean(Reference.ALLOW_SMART_TREE_DETECT, allowSmartTreeDetection);
        ntc.setBoolean(Reference.ALLOW_AUTO_TREE_DETECT, allowAutoTreeDetection);
        ntc.setBoolean(Reference.ALLOW_AUTO_AXE_DETECT, allowAutoAxeDetection);
        ntc.setBoolean(Reference.TREE_HEIGHT_DECIDES_BREAK_SPEED, treeHeightDecidesBreakSpeed);
        ntc.setFloat(Reference.TREE_HEIGHT_MOD, treeHeightModifier);
        ntc.setFloat(Reference.BREAK_SPEED_MOD, breakSpeedModifier);
        ntc.setFloat(Reference.DAMAGE_INCREASE_AMOUNT, damageIncreaseAmount);
        ntc.setFloat(Reference.DAMAGE_MULTIPLIER, damageMultiplier);
        ntc.setBoolean(Reference.DESTROY_LEAVES, destroyLeaves);
        ntc.setBoolean(Reference.DISABLE_CREATIVE_DROPS, disableCreativeDrops);
        ntc.setBoolean(Reference.DISABLE_IN_CREATIVE, disableInCreative);
        ntc.setBoolean(Reference.ENABLE_ENCHANT_MODE, enableEnchantmentMode);
        ntc.setInteger(Reference.ENCHANT_ID, enchantmentID);
        ntc.setInteger(Reference.INCREASE_DAMAGE_X_BLOCKS, increaseDamageEveryXBlocks);
        ntc.setInteger(Reference.MAX_H_LOG_DIST, maxHorLogBreakDist);
        ntc.setInteger(Reference.MAX_H_LEAF_DIST, maxHorLeafBreakDist);
        ntc.setInteger(Reference.MAX_LEAF_ID_DIST, maxLeafIDDist);
        ntc.setInteger(Reference.MAX_V_LOG_DIST, maxVerLogBreakDist);
        ntc.setInteger(Reference.MIN_LEAF_ID, minLeavesToID);
        ntc.setBoolean(Reference.NEED_ITEM, needItem);
        ntc.setBoolean(Reference.ONLY_DESTROY_UPWARDS, onlyDestroyUpwards);
        ntc.setString(Reference.ORE_DICT_LOG_KEYS, oreDictionaryLogStrings);
        ntc.setString(Reference.ORE_DICT_LEAF_KEYS, oreDictionaryLeafStrings);
        ntc.setBoolean(Reference.REQ_ITEM_IN_AXE_LIST_ENCHANT, requireItemInAxeListForEnchant);
        ntc.setBoolean(Reference.REQ_DECAY_CHECK, requireLeafDecayCheck);
        ntc.setBoolean(Reference.SHEAR_LEAVES, shearLeaves);
        ntc.setBoolean(Reference.SHEAR_VINES, shearVines);
        ntc.setString(Reference.SNEAK_ACTION, sneakAction);
        ntc.setBoolean(Reference.STACK_DROPS, stackDrops);
        ntc.setBoolean(Reference.ITEMS_DROP_IN_PLACE, itemsDropInPlace);
        ntc.setBoolean(Reference.USE_ADV_TOP_LOG_LOGIC, useAdvancedTopLogLogic);
        ntc.setBoolean(Reference.USE_INCREASING_ITEM_DAMAGE, useIncreasingItemDamage);
        ntc.setBoolean(Reference.USE_STRICT_BLOCK_PAIRING, useStrictBlockPairing);
    }
    
    /**
     * Synchronizes current values with the config object. config must be loaded (config.load()).
     * 
     * @param config
     */
    public void syncConfiguration(Configuration config)
    {
        // Since I moved that setting...
        config.moveProperty(Reference.MISC_CTGY, Reference.SNEAK_ACTION, Reference.TREE_CHOP_BEHAVIOR_CTGY);
        
        LinkedHashSet<String> orderedKeys = new LinkedHashSet<String>();
        
        // Misc settings
        allowDebugLogging = config.getBoolean(Reference.ALLOW_DEBUG_LOGGING, Reference.MISC_CTGY,
                allowDebugLoggingDefault, Reference.allowDebugLoggingDesc, Reference.LANG_KEY_BASE + Reference.ALLOW_DEBUG_LOGGING);
        orderedKeys.add(Reference.ALLOW_DEBUG_LOGGING);
        disableCreativeDrops = config.getBoolean(Reference.DISABLE_CREATIVE_DROPS, Reference.MISC_CTGY,
                disableCreativeDropsDefault, Reference.disableCreativeDropsDesc, Reference.LANG_KEY_BASE + Reference.DISABLE_CREATIVE_DROPS);
        orderedKeys.add(Reference.DISABLE_CREATIVE_DROPS);
        disableInCreative = config.getBoolean(Reference.DISABLE_IN_CREATIVE, Reference.MISC_CTGY,
                disableInCreativeDefault, Reference.disableInCreativeDesc, Reference.LANG_KEY_BASE + Reference.DISABLE_IN_CREATIVE);
        orderedKeys.add(Reference.DISABLE_IN_CREATIVE);
        allowOreDictionaryLookup = config.getBoolean(Reference.ALLOW_ORE_DICT_LOOKUP, Reference.MISC_CTGY,
                allowOreDictionaryLookupDefault, Reference.allowOreDictionaryLookupDesc, Reference.LANG_KEY_BASE + Reference.ALLOW_ORE_DICT_LOOKUP);
        orderedKeys.add(Reference.ALLOW_ORE_DICT_LOOKUP);
        oreDictionaryLogStrings = config.getString(Reference.ORE_DICT_LOG_KEYS, Reference.MISC_CTGY,
                oreDictionaryLogStringsDefault, Reference.oreDictionaryLogStringsDesc, Reference.LANG_KEY_BASE + Reference.ORE_DICT_LOG_KEYS);
        orderedKeys.add(Reference.ORE_DICT_LOG_KEYS);
        oreDictionaryLeafStrings = config.getString(Reference.ORE_DICT_LEAF_KEYS, Reference.MISC_CTGY,
                oreDictionaryLeafStringsDefault, Reference.oreDictionaryLeafStringsDesc, Reference.LANG_KEY_BASE + Reference.ORE_DICT_LEAF_KEYS);
        orderedKeys.add(Reference.ORE_DICT_LEAF_KEYS);
        blockIDBlacklist = config.getString(Reference.BLOCK_ID_BLACKLIST, Reference.MISC_CTGY,
                blockIDBlacklistDefault, Reference.blockIDBlacklistDesc, Reference.LANG_KEY_BASE + Reference.BLOCK_ID_BLACKLIST);
        orderedKeys.add(Reference.BLOCK_ID_BLACKLIST);
        itemIDBlacklist = config.getString(Reference.ITEM_ID_BLACKLIST, Reference.MISC_CTGY,
                itemIDBlacklistDefault, Reference.itemIDBlacklistDesc, Reference.LANG_KEY_BASE + Reference.ITEM_ID_BLACKLIST);
        orderedKeys.add(Reference.ITEM_ID_BLACKLIST);
        config.addCustomCategoryLanguageKey(Reference.MISC_CTGY, Reference.LANG_KEY_BASE + Reference.CTGY_LANG_KEY + Reference.MISC_CTGY);
        config.setCategoryPropertyOrder(Reference.MISC_CTGY, orderedKeys);
        
        orderedKeys = new LinkedHashSet<String>();
        // Break Speed settings
        breakSpeedModifier = config.getFloat(Reference.BREAK_SPEED_MOD, Reference.BREAK_SPEED_CTGY,
                breakSpeedModifierDefault, 0.01F, 1F, Reference.breakSpeedModifierDesc, Reference.LANG_KEY_BASE + Reference.BREAK_SPEED_MOD);
        orderedKeys.add(Reference.BREAK_SPEED_MOD);
        treeHeightDecidesBreakSpeed = config.getBoolean(Reference.TREE_HEIGHT_DECIDES_BREAK_SPEED, Reference.BREAK_SPEED_CTGY,
                treeHeightDecidesBreakSpeedDefault, Reference.treeHeightDecidesBreakSpeedDesc, Reference.LANG_KEY_BASE + Reference.TREE_HEIGHT_DECIDES_BREAK_SPEED);
        orderedKeys.add(Reference.TREE_HEIGHT_DECIDES_BREAK_SPEED);
        treeHeightModifier = config.getFloat(Reference.TREE_HEIGHT_MOD, Reference.BREAK_SPEED_CTGY,
                treeHeightModifierDefault, 0.25F, 10.0F, Reference.treeHeightModifierDesc, Reference.LANG_KEY_BASE + Reference.TREE_HEIGHT_MOD);
        orderedKeys.add(Reference.TREE_HEIGHT_MOD);
        config.addCustomCategoryLanguageKey(Reference.BREAK_SPEED_CTGY, Reference.LANG_KEY_BASE + Reference.CTGY_LANG_KEY + Reference.BREAK_SPEED_CTGY);
        config.setCategoryPropertyOrder(Reference.BREAK_SPEED_CTGY, orderedKeys);
        
        orderedKeys = new LinkedHashSet<String>();
        // Item settings
        allowAutoAxeDetection = config.getBoolean(Reference.ALLOW_AUTO_AXE_DETECT, Reference.ITEM_CTGY,
                allowAutoAxeDetectionDefault, Reference.allowAutoAxeDetectionDesc, Reference.LANG_KEY_BASE + Reference.ALLOW_AUTO_AXE_DETECT);
        orderedKeys.add(Reference.ALLOW_AUTO_AXE_DETECT);
        needItem = config.getBoolean(Reference.NEED_ITEM, Reference.ITEM_CTGY,
                needItemDefault, Reference.needItemDesc, Reference.LANG_KEY_BASE + Reference.NEED_ITEM);
        orderedKeys.add(Reference.NEED_ITEM);
        allowItemDamage = config.getBoolean(Reference.ALLOW_ITEM_DAMAGE, Reference.ITEM_CTGY,
                allowItemDamageDefault, Reference.allowItemDamageDesc, Reference.LANG_KEY_BASE + Reference.ALLOW_ITEM_DAMAGE);
        orderedKeys.add(Reference.ALLOW_ITEM_DAMAGE);
        damageMultiplier = config.getFloat(Reference.DAMAGE_MULTIPLIER, Reference.ITEM_CTGY,
                damageMultiplierDefault, 0.1F, 50.0F, Reference.damageMultiplierDesc, Reference.LANG_KEY_BASE + Reference.DAMAGE_MULTIPLIER);
        orderedKeys.add(Reference.DAMAGE_MULTIPLIER);
        allowMoreBlocksThanDamage = config.getBoolean(Reference.ALLOW_MORE_BLOCKS_THAN_DAMAGE, Reference.ITEM_CTGY,
                allowMoreBlocksThanDamageDefault, Reference.allowMoreBlocksThanDamageDesc, Reference.LANG_KEY_BASE + Reference.ALLOW_MORE_BLOCKS_THAN_DAMAGE);
        orderedKeys.add(Reference.ALLOW_MORE_BLOCKS_THAN_DAMAGE);
        useIncreasingItemDamage = config.getBoolean(Reference.USE_INCREASING_ITEM_DAMAGE, Reference.ITEM_CTGY,
                useIncreasingItemDamageDefault, Reference.useIncreasingItemDamageDesc, Reference.LANG_KEY_BASE + Reference.USE_INCREASING_ITEM_DAMAGE);
        orderedKeys.add(Reference.USE_INCREASING_ITEM_DAMAGE);
        damageIncreaseAmount = config.getFloat(Reference.DAMAGE_INCREASE_AMOUNT, Reference.ITEM_CTGY,
                damageIncreaseAmountDefault, 0.1F, 100.0F, Reference.damageIncreaseAmountDesc, Reference.LANG_KEY_BASE + Reference.DAMAGE_INCREASE_AMOUNT);
        orderedKeys.add(Reference.DAMAGE_INCREASE_AMOUNT);
        increaseDamageEveryXBlocks = config.getInt(Reference.INCREASE_DAMAGE_X_BLOCKS, Reference.ITEM_CTGY,
                increaseDamageEveryXBlocksDefault, 1, 500, Reference.increaseDamageEveryXBlocksDesc, Reference.LANG_KEY_BASE + Reference.INCREASE_DAMAGE_X_BLOCKS);
        orderedKeys.add(Reference.INCREASE_DAMAGE_X_BLOCKS);
        config.addCustomCategoryLanguageKey(Reference.ITEM_CTGY, Reference.LANG_KEY_BASE + Reference.CTGY_LANG_KEY + Reference.ITEM_CTGY);
        config.setCategoryPropertyOrder(Reference.ITEM_CTGY, orderedKeys);
        
        orderedKeys = new LinkedHashSet<String>();
        // Tree Chop Behavior settings
        allowAutoTreeDetection = config.getBoolean(Reference.ALLOW_AUTO_TREE_DETECT, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                allowAutoTreeDetectionDefault, Reference.allowAutoTreeDetectionDesc, Reference.LANG_KEY_BASE + Reference.ALLOW_AUTO_TREE_DETECT);
        orderedKeys.add(Reference.ALLOW_AUTO_TREE_DETECT);
        allowSmartTreeDetection = config.getBoolean(Reference.ALLOW_SMART_TREE_DETECT, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                allowSmartTreeDetectionDefault, Reference.allowSmartTreeDetectionDesc, Reference.LANG_KEY_BASE + Reference.ALLOW_SMART_TREE_DETECT);
        orderedKeys.add(Reference.ALLOW_SMART_TREE_DETECT);
        maxLeafIDDist = config.getInt(Reference.MAX_LEAF_ID_DIST, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                maxLeafIDDistDefault, 1, 8, Reference.maxLeafIDDistDesc, Reference.LANG_KEY_BASE + Reference.MAX_LEAF_ID_DIST);
        orderedKeys.add(Reference.MAX_LEAF_ID_DIST);
        minLeavesToID = config.getInt(Reference.MIN_LEAF_ID, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                minLeavesToIDDefault, 0, 8, Reference.minLeavesToIDDesc, Reference.LANG_KEY_BASE + Reference.MIN_LEAF_ID);
        orderedKeys.add(Reference.MIN_LEAF_ID);
        useAdvancedTopLogLogic = config.getBoolean(Reference.USE_ADV_TOP_LOG_LOGIC, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                useAdvancedTopLogLogicDefault, Reference.useAdvancedTopLogLogicDesc, Reference.LANG_KEY_BASE + Reference.USE_ADV_TOP_LOG_LOGIC);
        orderedKeys.add(Reference.USE_ADV_TOP_LOG_LOGIC);
        useStrictBlockPairing = config.getBoolean(Reference.USE_STRICT_BLOCK_PAIRING, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                useStrictBlockPairingDefault, Reference.useStrictBlockPairingDesc, Reference.LANG_KEY_BASE + Reference.USE_STRICT_BLOCK_PAIRING);
        orderedKeys.add(Reference.USE_STRICT_BLOCK_PAIRING);
        destroyLeaves = config.getBoolean(Reference.DESTROY_LEAVES, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                destroyLeavesDefault, Reference.destroyLeavesDesc, Reference.LANG_KEY_BASE + Reference.DESTROY_LEAVES);
        orderedKeys.add(Reference.DESTROY_LEAVES);
        requireLeafDecayCheck = config.getBoolean(Reference.REQ_DECAY_CHECK, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                requireLeafDecayCheckDefault, Reference.requireLeafDecayCheckDesc, Reference.LANG_KEY_BASE + Reference.REQ_DECAY_CHECK);
        orderedKeys.add(Reference.REQ_DECAY_CHECK);
        shearLeaves = config.getBoolean(Reference.SHEAR_LEAVES, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                shearLeavesDefault, Reference.shearLeavesDesc, Reference.LANG_KEY_BASE + Reference.SHEAR_LEAVES);
        orderedKeys.add(Reference.SHEAR_LEAVES);
        shearVines = config.getBoolean(Reference.SHEAR_VINES, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                shearVinesDefault, Reference.shearVinesDesc, Reference.LANG_KEY_BASE + Reference.SHEAR_VINES);
        orderedKeys.add(Reference.SHEAR_VINES);
        maxHorLeafBreakDist = config.getInt(Reference.MAX_H_LEAF_DIST, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                maxHorLeafBreakDistDefault, -1, 100, Reference.maxHorLeafBreakDistDesc, Reference.LANG_KEY_BASE + Reference.MAX_H_LEAF_DIST);
        orderedKeys.add(Reference.MAX_H_LEAF_DIST);
        maxHorLogBreakDist = config.getInt(Reference.MAX_H_LOG_DIST, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                maxHorLogBreakDistDefault, -1, 100, Reference.maxHorLogBreakDistDesc, Reference.LANG_KEY_BASE + Reference.MAX_H_LOG_DIST);
        orderedKeys.add(Reference.MAX_H_LOG_DIST);
        maxVerLogBreakDist = config.getInt(Reference.MAX_V_LOG_DIST, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                maxVerLogBreakDistDefault, -1, 255, Reference.maxVerLogBreakDistDesc, Reference.LANG_KEY_BASE + Reference.MAX_V_LOG_DIST);
        orderedKeys.add(Reference.MAX_V_LOG_DIST);
        onlyDestroyUpwards = config.getBoolean(Reference.ONLY_DESTROY_UPWARDS, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                onlyDestroyUpwardsDefault, Reference.onlyDestroyUpwardsDesc, Reference.LANG_KEY_BASE + Reference.ONLY_DESTROY_UPWARDS);
        orderedKeys.add(Reference.ONLY_DESTROY_UPWARDS);
        sneakAction = config.getString(Reference.SNEAK_ACTION, Reference.TREE_CHOP_BEHAVIOR_CTGY, sneakActionDefault,
                Reference.sneakActionDesc, new String[] { Reference.ENABLE, Reference.DISABLE, Reference.NONE }, Reference.LANG_KEY_BASE + Reference.SNEAK_ACTION);
        orderedKeys.add(Reference.SNEAK_ACTION);
        stackDrops = config.getBoolean(Reference.STACK_DROPS, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                stackDropsDefault, Reference.stackDropsDesc, Reference.LANG_KEY_BASE + Reference.STACK_DROPS);
        orderedKeys.add(Reference.STACK_DROPS);
        itemsDropInPlace = config.getBoolean(Reference.ITEMS_DROP_IN_PLACE, Reference.TREE_CHOP_BEHAVIOR_CTGY,
                itemsDropInPlaceDefault, Reference.itemsDropInPlaceDesc, Reference.LANG_KEY_BASE + Reference.ITEMS_DROP_IN_PLACE);
        orderedKeys.add(Reference.ITEMS_DROP_IN_PLACE);
        config.addCustomCategoryLanguageKey(Reference.TREE_CHOP_BEHAVIOR_CTGY, Reference.LANG_KEY_BASE + Reference.CTGY_LANG_KEY + Reference.TREE_CHOP_BEHAVIOR_CTGY);
        config.setCategoryPropertyOrder(Reference.TREE_CHOP_BEHAVIOR_CTGY, orderedKeys);
        
        orderedKeys = new LinkedHashSet<String>();
        // Enchantment Mode settings
        enableEnchantmentMode = config.getBoolean(Reference.ENABLE_ENCHANT_MODE, Reference.ENCHANTMENT_MODE_CTGY,
                enableEnchantmentModeDefault, Reference.enableEnchantmentModeDesc, Reference.LANG_KEY_BASE + Reference.ENABLE_ENCHANT_MODE);
        orderedKeys.add(Reference.ENABLE_ENCHANT_MODE);
        handleEnchantmentID(config.getInt(Reference.ENCHANT_ID, Reference.ENCHANTMENT_MODE_CTGY,
                enchantmentIDDefault, 0, Enchantment.enchantmentsList.length - 1, Reference.enchantmentIDDesc, Reference.LANG_KEY_BASE + Reference.ENCHANT_ID));
        orderedKeys.add(Reference.ENCHANT_ID);
        requireItemInAxeListForEnchant = config.getBoolean(Reference.REQ_ITEM_IN_AXE_LIST_ENCHANT, Reference.ENCHANTMENT_MODE_CTGY,
                requireItemInAxeListForEnchantDefault, Reference.requireItemInAxeListForEnchantDesc, Reference.LANG_KEY_BASE + Reference.REQ_ITEM_IN_AXE_LIST_ENCHANT);
        orderedKeys.add(Reference.REQ_ITEM_IN_AXE_LIST_ENCHANT);
        config.addCustomCategoryLanguageKey(Reference.ENCHANTMENT_MODE_CTGY, Reference.LANG_KEY_BASE + Reference.CTGY_LANG_KEY + Reference.ENCHANTMENT_MODE_CTGY);
        config.setCategoryPropertyOrder(Reference.ENCHANTMENT_MODE_CTGY, orderedKeys);
        
        enabled = config.getBoolean(Reference.ENABLED, Reference.SETTINGS_CTGY,
                enabledDefault, Reference.enabledDesc, Reference.LANG_KEY_BASE + Reference.ENABLED);
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
