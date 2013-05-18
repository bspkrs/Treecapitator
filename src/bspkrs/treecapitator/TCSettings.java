package bspkrs.treecapitator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTextureTile;
import net.minecraft.nbt.NBTTagCompound;
import bspkrs.util.ListUtils;

public final class TCSettings
{
    public static boolean     allowDebugLogging              = false;
    
    public static boolean     allowDebugOutput               = false;
    
    public static boolean     allowItemDamage                = true;
    
    public static boolean     allowMoreBlocksThanDamage      = false;
    public static boolean     allowSmartTreeDetection        = true;
    public static float       breakSpeedModifier             = 0.3F;
    public static float       damageIncreaseAmount           = 1.0F;
    public static float       damageMultiplier               = 1.0F;
    public static boolean     destroyLeaves                  = true;
    public static boolean     disableCreativeDrops           = false;
    public static boolean     disableInCreative              = false;
    public static boolean     enableEnchantmentMode          = false;
    public static String      idResolverModID                = "IDResolver";
    public static int         increaseDamageEveryXBlocks     = 15;
    public static boolean     isForge                        = false;
    public static int         maxHorLogBreakDist             = 16;
    public static int         maxLeafBreakDist               = 4;
    public static int         maxLeafIDDist                  = 1;
    public static int         maxVerLogBreakDist             = -1;
    public static int         minLeavesToID                  = 3;
    public static String      multiMineID                    = "AS_MultiMine";
    public static boolean     needItem                       = true;
    public static boolean     onlyDestroyUpwards             = true;
    public static boolean     requireItemInAxeListForEnchant = false;
    public static boolean     requireLeafDecayCheck          = true;
    public static boolean     shearLeaves                    = false;
    public static boolean     shearVines                     = false;
    public static String      sneakAction                    = "disable";
    public static boolean     useIncreasingItemDamage        = false;
    public static boolean     userConfigOverridesIMC         = false;
    public static boolean     useStrictBlockPairing          = true;
    
    // ML only
    public static float       logHardnessModified            = 4.0F;
    public static float       logHardnessNormal              = 2.0F;
    public static String      axeIDList                      = ListUtils.getListAsDelimitedString(ToolRegistry.instance().vanillaAxeList(), "; ");
    public static String      shearIDList                    = ListUtils.getListAsDelimitedString(ToolRegistry.instance().vanillaShearsList(), "; ");
    
    public static Block       wood;
    
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
            
            //**logIDList.add(new BlockID(wood.blockID));
            TreeRegistry.instance().registerVanillaTreeDefs();
        }
        else
        {}
    }
    
    private TCSettings()
    {
        instance = this;
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
}
