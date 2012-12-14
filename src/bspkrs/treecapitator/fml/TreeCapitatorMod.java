package bspkrs.treecapitator.fml;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ItemInWorldManager;
import net.minecraft.src.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import bspkrs.fml.util.Config;
import bspkrs.treecapitator.TreeBlockBreaker;
import bspkrs.treecapitator.TreeCapitator;
import bspkrs.util.BlockID;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;

@Mod(name = "TreeCapitator", modid = "TreeCapitator", version = "Forge " + TreeCapitator.versionNumber, useMetadata = true)
@NetworkMod(clientSideRequired = false, serverSideRequired = false,
        clientPacketHandlerSpec = @SidedPacketHandler(channels = { "TreeCapitator" }, packetHandler = ClientPacketHandler.class),
        serverPacketHandlerSpec = @SidedPacketHandler(channels = { "TreeCapitator" }, packetHandler = ServerPacketHandler.class),
        connectionHandler = ConnectionHandler.class)
public class TreeCapitatorMod
{
    public static ModVersionChecker versionChecker;
    private final String            versionURL = "https://dl.dropbox.com/u/20748481/Minecraft/1.4.5/treeCapitatorForge.version";
    private final String            mcfTopic   = "http://www.minecraftforum.net/topic/1009577-";
    
    public ModMetadata              metadata;
    
    @SidedProxy(clientSide = "bspkrs.treecapitator.fml.ClientProxy", serverSide = "bspkrs.treecapitator.fml.CommonProxy")
    public static CommonProxy       proxy;
    
    @Instance(value = "TreeCapitator")
    public static TreeCapitatorMod  instance;
    
    public TreeCapitatorMod()
    {}
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = event.getModMetadata();
        metadata.version = "Forge " + TreeCapitator.versionNumber;
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
        TreeCapitator.logBlockList = Config.getString(config, "logBlockList", ctgyGen, TreeCapitator.logBlockList, TreeCapitator.logBlockListDesc);
        TreeCapitator.leafBlockList = Config.getString(config, "leafBlockList", ctgyGen, TreeCapitator.leafBlockList, TreeCapitator.leafBlockListDesc);
        // TODO: add parser for old idList format to convert to new list format so that people don't bitch
        config.save();
    }
    
    @Init
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new PlayerHandler());
        TreeCapitator.init(true);
        proxy.onLoad();
    }
    
    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        TreeCapitator.parseBlockIDList(TreeCapitator.logBlockList, TreeCapitator.logIDList);
        TreeCapitator.parseBlockIDList(TreeCapitator.leafBlockList, TreeCapitator.leafIDList);
    }
    
    @ServerStarted
    public void serverStarted(FMLServerStartedEvent event)
    {
        new TreeCapitatorServer();
    }
    
    public void onBlockHarvested(World world, int x, int y, int z, Block block, int metadata, EntityPlayer entityPlayer)
    {
        BlockID blockID = new BlockID(block, metadata);
        
        if (TreeCapitator.isLogConfigured(blockID))
        {
            if (TreeBlockBreaker.isBreakingPossible(world, entityPlayer))
            {
                
                TreeBlockBreaker breaker = new TreeBlockBreaker(entityPlayer, TreeCapitator.logIDList.get(TreeCapitator.logIDList.indexOf(blockID)));
                breaker.onBlockHarvested(world, x, y, z, metadata, entityPlayer);
            }
        }
    }
    
    public static boolean isItemInWorldManagerReplaced(EntityPlayerMP player)
    {
        return !player.theItemInWorldManager.getClass().getSimpleName().equals(ItemInWorldManager.class.getSimpleName());
    }
}
