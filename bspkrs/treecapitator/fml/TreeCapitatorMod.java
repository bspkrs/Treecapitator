package bspkrs.treecapitator.fml;

import java.util.EnumSet;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.TickType;
import bspkrs.fml.util.Config;
import bspkrs.treecapitator.*;
import bspkrs.util.ModVersionChecker;

@Mod(name="TreeCapitator", modid="TreeCapitator", version="FML 1.3.2.r08", useMetadata=true)
@NetworkMod(clientSideRequired=false, serverSideRequired=false)
public class TreeCapitatorMod
{
    private static ModVersionChecker versionChecker;
    private String versionURL = "https://dl.dropbox.com/u/20748481/Minecraft/1.3.1/treeCapitatorFML.version";
    private String mcfTopic = "http://www.minecraftforum.net/topic/1009577-";

    @SideOnly(Side.CLIENT)
    public static Minecraft mcClient;

    public ModMetadata metadata;

    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        TreeCapitator.init();
        metadata = event.getModMetadata();
        metadata.version = "FML " + TreeCapitator.versionNumber;
        versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic, FMLLog.getLogger());
        versionChecker.checkVersionWithLogging();
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        String ctgyGen = Configuration.CATEGORY_GENERAL;

        config.load();
        TreeCapitator.allowUpdateCheck = Config.getBoolean(config, "allowUpdateCheck", ctgyGen, TreeCapitator.allowUpdateCheck, TreeCapitator.allowUpdateCheckDesc);
        TreeCapitator.axeIDList = Config.getString(config, "axeIDList", ctgyGen, TreeCapitator.axeIDList, TreeCapitator.axeIDListDesc);
        TreeCapitator.needItem = Config.getBoolean(config, "needItem", ctgyGen, TreeCapitator.needItem, TreeCapitator.needItemDesc);
        TreeCapitator.onlyDestroyUpwards = Config.getBoolean(config, "onlyDestroyUpwards", ctgyGen, TreeCapitator.onlyDestroyUpwards, TreeCapitator.onlyDestroyUpwardsDesc);
        TreeCapitator.destroyLeaves = Config.getBoolean(config, "destroyLeaves", ctgyGen, TreeCapitator.destroyLeaves, TreeCapitator.destroyLeavesDesc);
        TreeCapitator.shearLeaves = Config.getBoolean(config, "shearLeaves", ctgyGen, TreeCapitator.shearLeaves, TreeCapitator.shearLeavesDesc);
        TreeCapitator.shearVines = Config.getBoolean(config, "shearVines", ctgyGen, TreeCapitator.shearVines, TreeCapitator.shearVinesDesc);
        TreeCapitator.shearIDList = Config.getString(config, "shearIDList", ctgyGen, TreeCapitator.shearIDList, TreeCapitator.shearIDListDesc);;
        TreeCapitator.logHardnessNormal = Config.getFloat(config, "logHardnessNormal", ctgyGen, TreeCapitator.logHardnessNormal, 0F, 100F, TreeCapitator.logHardnessNormalDesc);
        TreeCapitator.logHardnessModified = Config.getFloat(config, "logHardnessModified", ctgyGen, TreeCapitator.logHardnessModified, 0F, 100F, TreeCapitator.logHardnessModifiedDesc);
        TreeCapitator.disableInCreative = Config.getBoolean(config, "disableInCreative", ctgyGen, TreeCapitator.disableInCreative, TreeCapitator.disableInCreativeDesc);
        TreeCapitator.disableCreativeDrops = Config.getBoolean(config, "disableCreativeDrops", ctgyGen, TreeCapitator.disableCreativeDrops, TreeCapitator.disableCreativeDropsDesc);
        TreeCapitator.allowItemDamage = Config.getBoolean(config, "allowItemDamage", ctgyGen, TreeCapitator.allowItemDamage, TreeCapitator.allowItemDamageDesc);
        TreeCapitator.allowMoreBlocksThanDamage = Config.getBoolean(config, "allowMoreBlocksThanDamage", ctgyGen, TreeCapitator.allowMoreBlocksThanDamage, TreeCapitator.allowMoreBlocksThanDamageDesc);
        TreeCapitator.sneakAction = Config.getString(config, "sneakAction", ctgyGen, TreeCapitator.sneakAction, TreeCapitator.sneakActionDesc);
        TreeCapitator.maxBreakDistance = Config.getInt(config, "maxBreakDistance", ctgyGen, TreeCapitator.maxBreakDistance, -1, 100, TreeCapitator.maxBreakDistanceDesc);
        config.save();
    }

    @Init
    public void init(FMLInitializationEvent event)
    {
        if(event.getSide().equals(Side.CLIENT))
        {
            TickRegistry.registerTickHandler(new TreeCapitatorTicker(EnumSet.of(TickType.CLIENT)), Side.CLIENT);
            this.mcClient = FMLClientHandler.instance().getClient();
        }
    }

    @SideOnly(Side.CLIENT)
    public static boolean onTick(TickType tick, boolean isStart)
    {
        if (isStart) {
            return true;
        }

        if (mcClient != null && mcClient.thePlayer != null)
        {
            if(TreeCapitator.allowUpdateCheck)
                if(!versionChecker.isCurrentVersion())
                    for(String msg : versionChecker.getInGameMessage())
                        mcClient.thePlayer.addChatMessage(msg);
            return false;
        }

        return true;
    }
}
