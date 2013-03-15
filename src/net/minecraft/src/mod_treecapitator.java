package net.minecraft.src;

import net.minecraft.client.Minecraft;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TreeCapitator;
import bspkrs.util.ModVersionChecker;

public class mod_treecapitator extends BaseMod
{
    @MLProp(info = TreeCapitator.axeIDListDesc)
    public static String      axeIDList                  = TreeCapitator.axeIDList;
    @MLProp(info = TreeCapitator.needItemDesc)
    public static boolean     needItem                   = TreeCapitator.needItem;
    @MLProp(info = TreeCapitator.onlyDestroyUpwardsDesc)
    public static boolean     onlyDestroyUpwards         = TreeCapitator.onlyDestroyUpwards;
    @MLProp(info = TreeCapitator.destroyLeavesDesc)
    public static boolean     destroyLeaves              = TreeCapitator.destroyLeaves;
    @MLProp(info = TreeCapitator.shearLeavesDesc)
    public static boolean     shearLeaves                = TreeCapitator.shearLeaves;
    @MLProp(info = TreeCapitator.shearVinesDesc)
    public static boolean     shearVines                 = TreeCapitator.shearVines;
    @MLProp(info = TreeCapitator.shearIDListDesc)
    public static String      shearIDList                = TreeCapitator.shearIDList;
    @MLProp(info = TreeCapitator.logHardnessNormalDesc)
    public static float       logHardnessNormal          = TreeCapitator.logHardnessNormal;
    @MLProp(info = TreeCapitator.logHardnessModifiedDesc)
    public static float       logHardnessModified        = TreeCapitator.logHardnessModified;
    @MLProp(info = TreeCapitator.disableInCreativeDesc)
    public static boolean     disableInCreative          = TreeCapitator.disableInCreative;
    @MLProp(info = TreeCapitator.disableCreativeDropsDesc)
    public static boolean     disableCreativeDrops       = TreeCapitator.disableCreativeDrops;
    @MLProp(info = TreeCapitator.allowItemDamageDesc)
    public static boolean     allowItemDamage            = TreeCapitator.allowItemDamage;
    @MLProp(info = TreeCapitator.allowMoreBlocksThanDamageDesc)
    public static boolean     allowMoreBlocksThanDamage  = TreeCapitator.allowMoreBlocksThanDamage;
    @MLProp(info = TreeCapitator.sneakActionDesc)
    public static String      sneakAction                = TreeCapitator.sneakAction;
    @MLProp(info = TreeCapitator.maxBreakDistanceDesc)
    public static int         maxBreakDistance           = TreeCapitator.maxBreakDistance;
    
    @MLProp(info = TreeCapitator.requireLeafDecayCheckDesc)
    public static boolean     requireLeafDecayCheck      = TreeCapitator.requireLeafDecayCheck;
    @MLProp(info = TreeCapitator.damageMultiplierDesc)
    public static float       damageMultiplier           = TreeCapitator.damageMultiplier;
    @MLProp(info = TreeCapitator.useIncreasingItemDamageDesc)
    public static boolean     useIncreasingItemDamage    = TreeCapitator.useIncreasingItemDamage;
    @MLProp(info = TreeCapitator.increaseDamageEveryXBlocksDesc)
    public static int         increaseDamageEveryXBlocks = TreeCapitator.increaseDamageEveryXBlocks;
    @MLProp(info = TreeCapitator.damageIncreaseAmountDesc)
    public static float       damageIncreaseAmount       = TreeCapitator.damageIncreaseAmount;
    @MLProp(info = TreeCapitator.allowSmartTreeDetectionDesc + "\n\n**ONLY EDIT WHAT IS BELOW THIS**")
    public static boolean     allowSmartTreeDetection    = TreeCapitator.allowSmartTreeDetection;
    
    private ModVersionChecker versionChecker;
    private final String      versionURL                 = "https://dl.dropbox.com/u/20748481/Minecraft/1.5.0/treeCapitator.version";
    private final String      mcfTopic                   = "http://www.minecraftforum.net/topic/1009577-";
    
    public mod_treecapitator()
    {
        if (mod_bspkrsCore.allowUpdateCheck)
            versionChecker = new ModVersionChecker(getName(), getVersion(), versionURL, mcfTopic, TCLog.INSTANCE.getLogger());
    }
    
    @Override
    public String getName()
    {
        return "TreeCapitator";
    }
    
    @Override
    public String getVersion()
    {
        return "ML " + TreeCapitator.VERSION_NUMBER;
    }
    
    @Override
    public String getPriorities()
    {
        return "required-after:mod_bspkrsCore";
    }
    
    @Override
    public void load()
    {
        if (mod_bspkrsCore.allowUpdateCheck && versionChecker != null)
            versionChecker.checkVersionWithLogging();
        ModLoader.setInGameHook(this, true, true);
        
        TreeCapitator.init(false);
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
        return false;
    }
}
