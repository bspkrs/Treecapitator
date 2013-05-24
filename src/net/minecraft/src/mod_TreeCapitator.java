package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeVersion;
import bspkrs.treecapitator.Strings;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TCSettings;
import bspkrs.treecapitator.ToolRegistry;
import bspkrs.treecapitator.fml.TreeCapitatorMod;
import bspkrs.util.BSProp;
import bspkrs.util.BSPropRegistry;
import bspkrs.util.Const;
import bspkrs.util.ItemID;
import bspkrs.util.ListUtils;
import bspkrs.util.ModVersionChecker;

public class mod_TreeCapitator extends BaseMod
{
    @BSProp(info = Strings.axeIDListDesc + "\n")
    public static String      axeIDList                  = TCSettings.axeIDList;
    @BSProp(info = Strings.needItemDesc + "\n")
    public static boolean     needItem                   = TCSettings.needItem;
    @BSProp(info = Strings.onlyDestroyUpwardsDesc + "\n")
    public static boolean     onlyDestroyUpwards         = TCSettings.onlyDestroyUpwards;
    @BSProp(info = Strings.destroyLeavesDesc + "\n")
    public static boolean     destroyLeaves              = TCSettings.destroyLeaves;
    @BSProp(info = Strings.shearLeavesDesc + "\n")
    public static boolean     shearLeaves                = TCSettings.shearLeaves;
    @BSProp(info = Strings.shearVinesDesc + "\n")
    public static boolean     shearVines                 = TCSettings.shearVines;
    @BSProp(info = Strings.shearIDListDesc + "\n")
    public static String      shearIDList                = TCSettings.shearIDList;
    @BSProp(info = Strings.logHardnessNormalDesc + "\n")
    public static float       logHardnessNormal          = TCSettings.logHardnessNormal;
    @BSProp(info = Strings.logHardnessModifiedDesc + "\n")
    public static float       logHardnessModified        = TCSettings.logHardnessModified;
    @BSProp(info = Strings.disableInCreativeDesc + "\n")
    public static boolean     disableInCreative          = TCSettings.disableInCreative;
    @BSProp(info = Strings.disableCreativeDropsDesc + "\n")
    public static boolean     disableCreativeDrops       = TCSettings.disableCreativeDrops;
    @BSProp(info = Strings.allowItemDamageDesc + "\n")
    public static boolean     allowItemDamage            = TCSettings.allowItemDamage;
    @BSProp(info = Strings.allowMoreBlocksThanDamageDesc + "\n")
    public static boolean     allowMoreBlocksThanDamage  = TCSettings.allowMoreBlocksThanDamage;
    @BSProp(info = Strings.sneakActionDesc + "\n")
    public static String      sneakAction                = TCSettings.sneakAction;
    @BSProp(info = Strings.maxHorLeafBreakDistDesc + "\n")
    public static int         maxHorLeafBreakDist        = TCSettings.maxHorLeafBreakDist;
    @BSProp(info = Strings.maxHorLogBreakDistDesc + "\n")
    public static int         maxHorLogBreakDist         = TCSettings.maxHorLogBreakDist;
    @BSProp(info = Strings.maxVerLogBreakDistDesc + "\n")
    public static int         maxVerLogBreakDist         = TCSettings.maxVerLogBreakDist;
    @BSProp(info = Strings.requireLeafDecayCheckDesc + "\n")
    public static boolean     requireLeafDecayCheck      = TCSettings.requireLeafDecayCheck;
    @BSProp(info = Strings.damageMultiplierDesc + "\n")
    public static float       damageMultiplier           = TCSettings.damageMultiplier;
    @BSProp(info = Strings.useIncreasingItemDamageDesc + "\n")
    public static boolean     useIncreasingItemDamage    = TCSettings.useIncreasingItemDamage;
    @BSProp(info = Strings.increaseDamageEveryXBlocksDesc + "\n")
    public static int         increaseDamageEveryXBlocks = TCSettings.increaseDamageEveryXBlocks;
    @BSProp(info = Strings.damageIncreaseAmountDesc + "\n")
    public static float       damageIncreaseAmount       = TCSettings.damageIncreaseAmount;
    @BSProp(info = Strings.allowSmartTreeDetectionDesc + "\n")
    public static boolean     allowSmartTreeDetection    = TCSettings.allowSmartTreeDetection;
    @BSProp(info = Strings.useStrictBlockPairingDesc + "\n")
    public static boolean     useStrictBlockPairing      = TCSettings.useStrictBlockPairing;
    @BSProp(info = Strings.enableEnchantmentModeDesc + "\n")
    public static boolean     enableEnchantmentMode      = TCSettings.enableEnchantmentMode;
    @BSProp(info = Strings.enchantmentIDDesc + "\n\n**ONLY EDIT WHAT IS BELOW THIS**")
    public static int         enchantmentID              = TCSettings.enchantmentID;
    
    private ModVersionChecker versionChecker;
    private final String      versionURL                 = "http://bspk.rs/Minecraft/" + Const.MCVERSION + "/treeCapitator.version";
    private final String      mcfTopic                   = "http://www.minecraftforum.net/topic/1009577-";
    private boolean           isForgeDetected;
    
    public mod_TreeCapitator()
    {
        BSPropRegistry.registerPropHandler(this.getClass());
    }
    
    @Override
    public String getName()
    {
        return "TreeCapitator";
    }
    
    @Override
    public String getVersion()
    {
        return "ML " + Strings.VERSION_NUMBER;
    }
    
    @Override
    public String getPriorities()
    {
        return "required-after:mod_bspkrsCore";
    }
    
    @Override
    public void load()
    {
        try
        {
            ForgeVersion.getVersion();
            isForgeDetected = true;
            TCLog.severe("Minecraft Forge has been detected! You should not be using the ModLoader version of %s!", getName());
        }
        catch (Throwable e)
        {
            isForgeDetected = false;
        }
        
        try
        {
            if (TreeCapitatorMod.isCoreModLoaded)
                ;
            TCLog.warning("%s Forge has been detected! ModLoader version of %s will be ignored!", getName(), getName());
        }
        catch (Throwable e)
        {
            if (mod_bspkrsCore.allowUpdateCheck)
            {
                versionChecker = new ModVersionChecker(getName(), getVersion(), versionURL, mcfTopic);
                versionChecker.checkVersionWithLoggingBySubStringAsFloat(getVersion().length() - 1, getVersion().length());
            }
            
            ModLoader.setInGameHook(this, true, true);
            
            TCSettings.preInit(false);
            TCSettings.axeIDList = axeIDList;
            TCSettings.needItem = needItem;
            TCSettings.onlyDestroyUpwards = onlyDestroyUpwards;
            TCSettings.destroyLeaves = destroyLeaves;
            TCSettings.shearLeaves = shearLeaves;
            TCSettings.shearVines = shearVines;
            TCSettings.shearIDList = shearIDList;
            TCSettings.logHardnessNormal = logHardnessNormal;
            TCSettings.logHardnessModified = logHardnessModified;
            TCSettings.disableInCreative = disableInCreative;
            TCSettings.disableCreativeDrops = disableCreativeDrops;
            TCSettings.allowItemDamage = allowItemDamage;
            TCSettings.allowMoreBlocksThanDamage = allowMoreBlocksThanDamage;
            TCSettings.sneakAction = sneakAction;
            TCSettings.maxHorLogBreakDist = maxHorLogBreakDist;
            TCSettings.maxHorLeafBreakDist = maxHorLeafBreakDist;
            TCSettings.maxVerLogBreakDist = maxVerLogBreakDist;
            
            TCSettings.requireLeafDecayCheck = requireLeafDecayCheck;
            TCSettings.damageMultiplier = damageMultiplier;
            TCSettings.useIncreasingItemDamage = useIncreasingItemDamage;
            TCSettings.increaseDamageEveryXBlocks = increaseDamageEveryXBlocks;
            TCSettings.damageIncreaseAmount = damageIncreaseAmount;
            TCSettings.allowSmartTreeDetection = allowSmartTreeDetection;
            TCSettings.useStrictBlockPairing = useStrictBlockPairing;
            TCSettings.enableEnchantmentMode = enableEnchantmentMode;
            TCSettings.instance().handleEnchantmentID(enchantmentID);
            
            for (ItemID itemID : ListUtils.getDelimitedStringAsItemIDList(TCSettings.axeIDList, ";"))
                ToolRegistry.instance().registerAxe(itemID);
            for (ItemID itemID : ListUtils.getDelimitedStringAsItemIDList(TCSettings.shearIDList, ";"))
                ToolRegistry.instance().registerShears(itemID);
        }
    }
    
    @Override
    public boolean onTickInGame(float f, Minecraft mc)
    {
        if (mod_bspkrsCore.allowUpdateCheck && versionChecker != null)
        {
            if (!versionChecker.isCurrentVersionBySubStringAsFloatNewer(getVersion().length() - 1, getVersion().length()))
                for (String msg : versionChecker.getInGameMessage())
                    mc.thePlayer.addChatMessage(msg);
        }
        
        if (isForgeDetected)
            mc.thePlayer.addChatMessage("\247cMinecraft Forge has been detected! You should not be using the ModLoader version of " + getName() + "! Use the Forge version instead!");
        
        return false;
    }
}
