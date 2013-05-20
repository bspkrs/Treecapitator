package bspkrs.treecapitator;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTextureTile;
import net.minecraft.nbt.NBTTagCompound;
import bspkrs.treecapitator.fml.ModConfigRegistry;
import bspkrs.treecapitator.fml.ThirdPartyModConfig;
import bspkrs.util.ConfigCategory;
import bspkrs.util.Configuration;
import bspkrs.util.ListUtils;
import cpw.mods.fml.common.Loader;

public final class TCSettings
{
    // Global
    public static boolean     allowDebugLogging              = false;
    public static boolean     allowDebugOutput               = false;
    public static boolean     allowItemDamage                = true;
    public static boolean     allowMoreBlocksThanDamage      = false;
    public static boolean     allowSmartTreeDetection        = true;
    public static float       damageIncreaseAmount           = 1.0F;
    public static float       damageMultiplier               = 1.0F;
    public static boolean     destroyLeaves                  = true;
    public static boolean     disableCreativeDrops           = false;
    public static boolean     disableInCreative              = false;
    public static boolean     enableEnchantmentMode          = false;
    public static int         enchantmentID                  = 111;
    private static final int  enchantmentWeight              = 5;
    public static int         increaseDamageEveryXBlocks     = 15;
    public static boolean     needItem                       = true;
    public static boolean     onlyDestroyUpwards             = true;
    public static boolean     requireItemInAxeListForEnchant = false;
    public static boolean     shearLeaves                    = false;
    public static boolean     shearVines                     = false;
    public static String      sneakAction                    = "disable";
    public static boolean     useIncreasingItemDamage        = false;
    public static boolean     useStrictBlockPairing          = true;
    
    // Per-tree
    public static float       breakSpeedModifier             = 0.3F;
    public static int         maxHorLogBreakDist             = 16;
    public static int         maxLeafBreakDist               = 4;
    public static int         maxLeafIDDist                  = 1;
    public static int         maxVerLogBreakDist             = -1;
    public static int         minLeavesToID                  = 3;
    public static boolean     requireLeafDecayCheck          = true;
    
    // Mod config settings (Forge only)
    public static String      idResolverModID                = "IDResolver";
    public static String      multiMineModID                 = "AS_MultiMine";
    public static boolean     userConfigOverridesIMC         = false;
    
    // ML only
    public static float       logHardnessModified            = 4.0F;
    public static float       logHardnessNormal              = 2.0F;
    public static String      axeIDList                      = ListUtils.getListAsDelimitedString(ToolRegistry.instance().vanillaAxeList(), "; ");
    public static String      shearIDList                    = ListUtils.getListAsDelimitedString(ToolRegistry.instance().vanillaShearsList(), "; ");
    
    // static object references
    public static Block       wood;
    public static Enchantment treecapitating;
    
    public static boolean     isForge                        = false;
    
    private static TCSettings instance;
    
    public static TCSettings instance()
    {
        if (instance == null)
            new TCSettings();
        
        return instance;
    }
    
    public static void preInit()
    {
        preInit(false);
    }
    
    public static void preInit(boolean isForgeVersion)
    {
        isForge = isForgeVersion;
        
        if (!isForge)
        {
            Block.blocksList[Block.wood.blockID] = null;
            wood = new BlockTree(Block.wood.blockID);
            Block.blocksList[wood.blockID] = wood;
            Item.itemsList[wood.blockID] = null;
            Item.itemsList[wood.blockID] = (new ItemMultiTextureTile(wood.blockID - 256, wood, BlockLog.woodType)).setUnlocalizedName("log");
            
            TreeRegistry.instance().registerVanillaTreeDefs();
        }
        else
        {}
    }
    
    private TCSettings()
    {
        instance = this;
    }
    
    protected void handleEnchantmentID(int id)
    {
        if (enableEnchantmentMode && enchantmentID != id && id > 0 && id < 256)
        {
            if (Enchantment.enchantmentsList[enchantmentID] != null)
                Enchantment.enchantmentsList[enchantmentID] = null;
            enchantmentID = id;
            treecapitating = new EnchantmentTreecapitating(enchantmentID, enchantmentWeight);
        }
    }
    
    protected void readFromNBT(NBTTagCompound ntc)
    {
        allowItemDamage = ntc.getBoolean("allowItemDamage");
        allowMoreBlocksThanDamage = ntc.getBoolean("allowMoreBlocksThanDamage");
        allowSmartTreeDetection = ntc.getBoolean("allowSmartTreeDetection");
        breakSpeedModifier = ntc.getFloat("breakSpeedModifier");
        damageIncreaseAmount = ntc.getFloat("damageIncreaseAmount");
        damageMultiplier = ntc.getFloat("damageMultiplier");
        destroyLeaves = ntc.getBoolean("destroyLeaves");
        disableCreativeDrops = ntc.getBoolean("disableCreativeDrops");
        disableInCreative = ntc.getBoolean("disableInCreative");
        enableEnchantmentMode = ntc.getBoolean("enableEnchantmentMode");
        
        handleEnchantmentID(ntc.getInteger("enchantmentID"));
        
        increaseDamageEveryXBlocks = ntc.getInteger("increaseDamageEveryXBlocks");
        maxHorLogBreakDist = ntc.getInteger("maxHorLogBreakDist");
        maxLeafBreakDist = ntc.getInteger("maxLeafBreakDist");
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
        useIncreasingItemDamage = ntc.getBoolean("useIncreasingItemDamage");
        useStrictBlockPairing = ntc.getBoolean("useStrictBlockPairing");
    }
    
    public void writeToNBT(NBTTagCompound ntc)
    {
        ntc.setBoolean("allowItemDamage", allowItemDamage);
        ntc.setBoolean("allowMoreBlocksThanDamage", allowMoreBlocksThanDamage);
        ntc.setBoolean("allowSmartTreeDetection", allowSmartTreeDetection);
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
        ntc.setInteger("maxLeafBreakDist", maxLeafBreakDist);
        ntc.setInteger("maxLeafIDDist", maxLeafIDDist);
        ntc.setInteger("maxVerLogBreakDist", maxVerLogBreakDist);
        ntc.setInteger("minLeavesToID", minLeavesToID);
        ntc.setBoolean("needItem", needItem);
        ntc.setBoolean("onlyDestroyUpwards", onlyDestroyUpwards);
        ntc.setBoolean("requireItemInAxeListForEnchant", requireItemInAxeListForEnchant);
        ntc.setBoolean("requireLeafDecayCheck", requireLeafDecayCheck);
        ntc.setBoolean("shearLeaves", shearLeaves);
        ntc.setBoolean("shearVines", shearVines);
        ntc.setString("sneakAction", sneakAction);
        ntc.setBoolean("useIncreasingItemDamage", useIncreasingItemDamage);
        ntc.setBoolean("useStrictBlockPairing", useStrictBlockPairing);
    }
    
    /**
     * Synchronizes current values with the config object. config must be loaded (config.load()).
     * 
     * @param config
     */
    public void readFromConfiguration(Configuration config)
    {
        // Global settings
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
        TCSettings.enableEnchantmentMode = config.getBoolean("enableEnchantmentMode", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.enableEnchantmentMode, Strings.enableEnchantmentModeDesc);
        
        handleEnchantmentID(config.getInt("enchantmentID", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.enchantmentID, 0, Enchantment.enchantmentsList.length, Strings.enchantmentIDDesc));
        
        TCSettings.increaseDamageEveryXBlocks = config.getInt("increaseDamageEveryXBlocks", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.increaseDamageEveryXBlocks, 1, 500, Strings.increaseDamageEveryXBlocksDesc);
        TCSettings.needItem = config.getBoolean("needItem", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.needItem, Strings.needItemDesc);
        TCSettings.requireItemInAxeListForEnchant = config.getBoolean("requireItemInAxeListForEnchant", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.requireItemInAxeListForEnchant, Strings.requireItemInAxeListForEnchantDesc);
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
        
        // Per-tree settings
        TCSettings.breakSpeedModifier = config.getFloat("breakSpeedModifier", Strings.PER_TREE_DEFAULTS_CTGY,
                TCSettings.breakSpeedModifier, 0.01F, 1F, Strings.breakSpeedModifierDesc);
        TCSettings.maxHorLogBreakDist = config.getInt("maxHorLogBreakDist", Strings.PER_TREE_DEFAULTS_CTGY,
                TCSettings.maxHorLogBreakDist, -1, 100, Strings.maxHorLogBreakDistDesc);
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
        
        TCLog.configs(config, Strings.GLOBALS_SETTINGS_CTGY);
        TCLog.configs(config, Strings.PER_TREE_DEFAULTS_CTGY);
        
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
        
        // Load all configs found in the file to ModConfigRegistry
        for (String ctgy : config.getCategoryNames())
        {
            ConfigCategory cc = config.getCategory(ctgy);
            if (ctgy.indexOf(Strings.TREE_MOD_CFG_CTGY + ".") != -1 && cc.containsKey(Strings.MOD_ID))
                if (Loader.isModLoaded(cc.get(Strings.MOD_ID).getString())
                        || cc.get(Strings.MOD_ID).equals(Strings.VAN_TREES_ITEMS_CTGY))
                    ModConfigRegistry.instance().registerUserModConfig(new ThirdPartyModConfig(config, ctgy));
        }
        
        if (TCSettings.enableEnchantmentMode)
            treecapitating = new EnchantmentTreecapitating(enchantmentID, 5);
    }
    
    public void writeToConfiguration(Configuration config)
    {
        // TODO, not sure if even needed since the read method pretty much just synchs up
    }
}
