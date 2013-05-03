package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeVersion;
import bspkrs.treecapitator.Strings;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TreeCapitator;
import bspkrs.treecapitator.TreeRegistry;
import bspkrs.treecapitator.fml.TreeCapitatorMod;
import bspkrs.util.BSProp;
import bspkrs.util.BSPropRegistry;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;

public class mod_TreeCapitator extends BaseMod
{
    @BSProp(info = TreeCapitator.axeIDListDesc + "\n")
    public static String      axeIDList                  = TreeCapitator.axeIDList;
    @BSProp(info = TreeCapitator.needItemDesc + "\n")
    public static boolean     needItem                   = TreeCapitator.needItem;
    @BSProp(info = TreeCapitator.onlyDestroyUpwardsDesc + "\n")
    public static boolean     onlyDestroyUpwards         = TreeCapitator.onlyDestroyUpwards;
    @BSProp(info = TreeCapitator.destroyLeavesDesc + "\n")
    public static boolean     destroyLeaves              = TreeCapitator.destroyLeaves;
    @BSProp(info = TreeCapitator.shearLeavesDesc + "\n")
    public static boolean     shearLeaves                = TreeCapitator.shearLeaves;
    @BSProp(info = TreeCapitator.shearVinesDesc + "\n")
    public static boolean     shearVines                 = TreeCapitator.shearVines;
    @BSProp(info = TreeCapitator.shearIDListDesc + "\n")
    public static String      shearIDList                = TreeCapitator.shearIDList;
    @BSProp(info = TreeCapitator.logHardnessNormalDesc + "\n")
    public static float       logHardnessNormal          = TreeCapitator.logHardnessNormal;
    @BSProp(info = TreeCapitator.logHardnessModifiedDesc + "\n")
    public static float       logHardnessModified        = TreeCapitator.logHardnessModified;
    @BSProp(info = TreeCapitator.disableInCreativeDesc + "\n")
    public static boolean     disableInCreative          = TreeCapitator.disableInCreative;
    @BSProp(info = TreeCapitator.disableCreativeDropsDesc + "\n")
    public static boolean     disableCreativeDrops       = TreeCapitator.disableCreativeDrops;
    @BSProp(info = TreeCapitator.allowItemDamageDesc + "\n")
    public static boolean     allowItemDamage            = TreeCapitator.allowItemDamage;
    @BSProp(info = TreeCapitator.allowMoreBlocksThanDamageDesc + "\n")
    public static boolean     allowMoreBlocksThanDamage  = TreeCapitator.allowMoreBlocksThanDamage;
    @BSProp(info = TreeCapitator.sneakActionDesc + "\n")
    public static String      sneakAction                = TreeCapitator.sneakAction;
    @BSProp(info = Strings.maxBreakDistanceDesc + "\n")
    public static int         maxBreakDistance           = TreeCapitator.maxBreakDistance;
    
    @BSProp(info = TreeCapitator.requireLeafDecayCheckDesc + "\n")
    public static boolean     requireLeafDecayCheck      = TreeCapitator.requireLeafDecayCheck;
    @BSProp(info = TreeCapitator.damageMultiplierDesc + "\n")
    public static float       damageMultiplier           = TreeCapitator.damageMultiplier;
    @BSProp(info = TreeCapitator.useIncreasingItemDamageDesc + "\n")
    public static boolean     useIncreasingItemDamage    = TreeCapitator.useIncreasingItemDamage;
    @BSProp(info = TreeCapitator.increaseDamageEveryXBlocksDesc + "\n")
    public static int         increaseDamageEveryXBlocks = TreeCapitator.increaseDamageEveryXBlocks;
    @BSProp(info = TreeCapitator.damageIncreaseAmountDesc + "\n")
    public static float       damageIncreaseAmount       = TreeCapitator.damageIncreaseAmount;
    @BSProp(info = TreeCapitator.allowSmartTreeDetectionDesc + "\n\n**ONLY EDIT WHAT IS BELOW THIS**")
    public static boolean     allowSmartTreeDetection    = TreeCapitator.allowSmartTreeDetection;
    
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
                versionChecker.checkVersionWithLogging();
            }
            
            ModLoader.setInGameHook(this, true, true);
            
            TreeCapitator.preInit(false);
            TreeCapitator.axeIDList = axeIDList;
            TreeCapitator.needItem = needItem;
            TreeCapitator.onlyDestroyUpwards = onlyDestroyUpwards;
            TreeCapitator.destroyLeaves = destroyLeaves;
            TreeCapitator.shearLeaves = shearLeaves;
            TreeCapitator.shearVines = shearVines;
            TreeCapitator.shearIDList = shearIDList;
            TreeCapitator.logHardnessNormal = logHardnessNormal;
            TreeCapitator.logHardnessModified = logHardnessModified;
            TreeCapitator.disableInCreative = disableInCreative;
            TreeCapitator.disableCreativeDrops = disableCreativeDrops;
            TreeCapitator.allowItemDamage = allowItemDamage;
            TreeCapitator.allowMoreBlocksThanDamage = allowMoreBlocksThanDamage;
            TreeCapitator.sneakAction = sneakAction;
            TreeCapitator.maxBreakDistance = maxBreakDistance;
            
            TreeCapitator.requireLeafDecayCheck = requireLeafDecayCheck;
            TreeCapitator.damageMultiplier = damageMultiplier;
            TreeCapitator.useIncreasingItemDamage = useIncreasingItemDamage;
            TreeCapitator.increaseDamageEveryXBlocks = increaseDamageEveryXBlocks;
            TreeCapitator.damageIncreaseAmount = damageIncreaseAmount;
            TreeCapitator.allowSmartTreeDetection = allowSmartTreeDetection;
            
            TreeRegistry.instance();
        }
    }
    
    @Override
    public boolean onTickInGame(float f, Minecraft mc)
    {
        if (mod_bspkrsCore.allowUpdateCheck && versionChecker != null)
        {
            if (!versionChecker.isCurrentVersion())
                for (String msg : versionChecker.getInGameMessage())
                    mc.thePlayer.addChatMessage(msg);
        }
        
        if (isForgeDetected)
            mc.thePlayer.addChatMessage("\247cMinecraft Forge has been detected! You should not be using the ModLoader version of " + getName() + "! Use the Forge version instead!");
        
        return false;
    }
}
