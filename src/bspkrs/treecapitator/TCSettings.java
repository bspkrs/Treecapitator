package bspkrs.treecapitator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTextureTile;
import net.minecraft.nbt.NBTTagCompound;
import bspkrs.util.ListUtils;

public final class TCSettings
{
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
    
    public static String  idResolverModID                = "IDResolver";
    public static String  multiMineID                    = "AS_MultiMine";
    public static boolean enableEnchantmentMode          = false;
    public static boolean requireItemInAxeListForEnchant = false;
    public static String  axeIDList                      = ListUtils.getListAsDelimitedString(ToolRegistry.instance().vanillaAxeList(), "; ");
    public static boolean needItem                       = true;
    public static boolean onlyDestroyUpwards             = true;
    public static boolean destroyLeaves                  = true;
    public static boolean requireLeafDecayCheck          = true;
    public static boolean shearLeaves                    = false;
    public static boolean shearVines                     = false;
    public static String  shearIDList                    = ListUtils.getListAsDelimitedString(ToolRegistry.instance().vanillaShearsList(), "; ");
    public static float   logHardnessNormal              = 2.0F;
    public static float   logHardnessModified            = 4.0F;
    public static float   breakSpeedModifier             = 0.3F;
    public static boolean disableInCreative              = false;
    public static boolean disableCreativeDrops           = false;
    public static boolean allowItemDamage                = true;
    public static boolean allowMoreBlocksThanDamage      = false;
    public static float   damageMultiplier               = 1.0F;
    public static boolean useIncreasingItemDamage        = false;
    public static int     increaseDamageEveryXBlocks     = 15;
    public static float   damageIncreaseAmount           = 1.0F;
    public static String  sneakAction                    = "disable";
    public static int     maxHorLogBreakDist             = 16;
    public static int     maxVerLogBreakDist             = -1;
    public static boolean allowSmartTreeDetection        = true;
    public static int     maxLeafIDDist                  = 1;
    public static int     maxLeafBreakDist               = 4;
    public static int     minLeavesToID                  = 3;
    public static boolean useStrictBlockPairing          = true;
    public static boolean userConfigOverridesIMC         = false;
    public static boolean allowDebugOutput               = false;
    public static boolean allowDebugLogging              = false;
    
    public static boolean isForge                        = false;
    public static Block   wood;
    
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
    
    protected void readFromNBT(NBTTagCompound ntc)
    {
        // TODO
    }
    
    protected void writeToNBT(NBTTagCompound ntc)
    {
        // TODO
    }
}
