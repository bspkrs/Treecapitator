package bspkrs.treecapitator.fml;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.world.World;
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
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
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
    private final String            versionURL     = "https://dl.dropbox.com/u/20748481/Minecraft/1.4.5/treeCapitatorForge.version";
    private final String            mcfTopic       = "http://www.minecraftforum.net/topic/1009577-";
    
    public static final String      BLOCK_ID_CTGY  = "block_id";
    public static final String      BLOCK_SETTINGS = "block_settings";
    public static final String      ITEM_CTGY      = "item_settings";
    public static final String      LEAF_VINE      = "leaf_and_vine_settings";
    public static final String      MISC           = "miscellaneous_settings";
    
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
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        
        config.load();
        TreeCapitator.allowUpdateCheck = Config.getBoolean(config, "allowUpdateCheck", MISC, TreeCapitator.allowUpdateCheck, TreeCapitator.allowUpdateCheckDesc);
        TreeCapitator.allowDebugOutput = Config.getBoolean(config, "allowDebugOutput", MISC, TreeCapitator.allowDebugOutput, TreeCapitator.allowDebugOutputDesc);
        TreeCapitator.onlyDestroyUpwards = Config.getBoolean(config, "onlyDestroyUpwards", MISC, TreeCapitator.onlyDestroyUpwards, TreeCapitator.onlyDestroyUpwardsDesc);
        TreeCapitator.disableInCreative = Config.getBoolean(config, "disableInCreative", MISC, TreeCapitator.disableInCreative, TreeCapitator.disableInCreativeDesc);
        TreeCapitator.disableCreativeDrops = Config.getBoolean(config, "disableCreativeDrops", MISC, TreeCapitator.disableCreativeDrops, TreeCapitator.disableCreativeDropsDesc);
        TreeCapitator.sneakAction = Config.getString(config, "sneakAction", MISC, TreeCapitator.sneakAction, TreeCapitator.sneakActionDesc);
        TreeCapitator.maxBreakDistance = Config.getInt(config, "maxBreakDistance", MISC, TreeCapitator.maxBreakDistance, -1, 100, TreeCapitator.maxBreakDistanceDesc);
        
        TreeCapitator.axeIDList = Config.getString(config, "axeIDList", ITEM_CTGY, TreeCapitator.axeIDList, TreeCapitator.axeIDListDesc);
        TreeCapitator.shearIDList = Config.getString(config, "shearIDList", ITEM_CTGY, TreeCapitator.shearIDList, TreeCapitator.shearIDListDesc);
        TreeCapitator.needItem = Config.getBoolean(config, "needItem", ITEM_CTGY, TreeCapitator.needItem, TreeCapitator.needItemDesc);
        TreeCapitator.allowItemDamage = Config.getBoolean(config, "allowItemDamage", ITEM_CTGY, TreeCapitator.allowItemDamage, TreeCapitator.allowItemDamageDesc);
        TreeCapitator.allowMoreBlocksThanDamage = Config.getBoolean(config, "allowMoreBlocksThanDamage", ITEM_CTGY, TreeCapitator.allowMoreBlocksThanDamage, TreeCapitator.allowMoreBlocksThanDamageDesc);
        
        TreeCapitator.destroyLeaves = Config.getBoolean(config, "destroyLeaves", LEAF_VINE, TreeCapitator.destroyLeaves, TreeCapitator.destroyLeavesDesc);
        TreeCapitator.shearLeaves = Config.getBoolean(config, "shearLeaves", LEAF_VINE, TreeCapitator.shearLeaves, TreeCapitator.shearLeavesDesc);
        TreeCapitator.shearVines = Config.getBoolean(config, "shearVines", LEAF_VINE, TreeCapitator.shearVines, TreeCapitator.shearVinesDesc);
        
        TreeCapitator.allowGetOnlineTreeConfig = Config.getBoolean(config, "allowGetOnlineTreeConfig", BLOCK_SETTINGS, TreeCapitator.allowGetOnlineTreeConfig, TreeCapitator.allowGetOnlineTreeConfigDesc);
        TreeCapitator.remoteTreeConfigURL = Config.getString(config, "remoteTreeConfigURL", BLOCK_SETTINGS, TreeCapitator.remoteTreeConfigURL, TreeCapitator.remoteTreeConfigURLDesc);
        TreeCapitator.remoteTreeConfig = Config.getString(config, "remoteTreeConfig", BLOCK_SETTINGS, TreeCapitator.remoteTreeConfig, TreeCapitator.remoteTreeConfigDesc);
        TreeCapitator.remoteTreeConfig = TreeCapitator.getRemoteConfig();
        TreeCapitator.localTreeConfig = Config.getString(config, "localTreeConfig", BLOCK_SETTINGS, TreeCapitator.localTreeConfig, TreeCapitator.localTreeConfigDesc);
        TreeCapitator.useOnlineTreeConfig = Config.getBoolean(config, "useOnlineTreeConfig", BLOCK_SETTINGS, TreeCapitator.useOnlineTreeConfig, TreeCapitator.useOnlineTreeConfigDesc);
        TreeCapitator.logHardnessNormal = Config.getFloat(config, "logHardnessNormal", BLOCK_SETTINGS, TreeCapitator.logHardnessNormal, 0F, 100F, TreeCapitator.logHardnessNormalDesc);
        TreeCapitator.logHardnessModified = Config.getFloat(config, "logHardnessModified", BLOCK_SETTINGS, TreeCapitator.logHardnessModified, 0F, 100F, TreeCapitator.logHardnessModifiedDesc);
        
        if (!config.hasCategory(BLOCK_ID_CTGY))
        {
            config.addCustomCategoryComment(BLOCK_ID_CTGY, TreeCapitator.configBlockIDDesc);
            for (String key : TreeCapitator.configBlockList.keySet())
            {
                HashMap<String, String> entry = TreeCapitator.configBlockList.get(key);
                for (String blockType : entry.keySet())
                    config.get(BLOCK_ID_CTGY + "." + key, blockType, entry.get(blockType));
            }
        }
        else
        {
            config.addCustomCategoryComment(BLOCK_ID_CTGY, TreeCapitator.configBlockIDDesc);
            TreeCapitator.configBlockList = new HashMap<String, HashMap<String, String>>();
            
            for (String ctgy : config.categories.keySet())
            {
                if (ctgy.indexOf(BLOCK_ID_CTGY + ".") != -1)
                {
                    HashMap<String, String> blocks = new HashMap<String, String>();
                    
                    if (config.getCategory(ctgy).containsKey(TreeCapitator.LOGS))
                    {
                        blocks.put(TreeCapitator.LOGS, config.getCategory(ctgy).get(TreeCapitator.LOGS).value);
                        
                        if (config.getCategory(ctgy).containsKey(TreeCapitator.LEAVES))
                            blocks.put(TreeCapitator.LEAVES, config.getCategory(ctgy).get(TreeCapitator.LEAVES).value);
                        
                        TreeCapitator.configBlockList.put(ctgy, blocks);
                    }
                }
            }
        }
        
        config.save();
        
        if (TreeCapitator.allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic, FMLLog.getLogger());
            versionChecker.checkVersionWithLogging();
        }
    }
    
    @Init
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new PlayerHandler());
        TreeCapitator.init(true);
        proxy.onLoad();
    }
    
    @ServerStarted
    public void serverStarted(FMLServerStartedEvent event)
    {
        new TreeCapitatorServer();
        TreeCapitator.parseConfigBlockList(TreeCapitator.localTreeConfig);
    }
    
    public void onBlockHarvested(World world, int x, int y, int z, Block block, int metadata, EntityPlayer entityPlayer)
    {
        if (proxy.isEnabled())
        {
            BlockID blockID = new BlockID(block, metadata);
            
            if (TreeCapitator.isLogBlock(blockID))
            {
                if (TreeBlockBreaker.isBreakingPossible(world, entityPlayer))
                {
                    blockID = TreeCapitator.logIDList.get(TreeCapitator.logIDList.indexOf(blockID));
                    TreeBlockBreaker breaker;
                    
                    if (TreeCapitator.useStrictBlockPairing)
                        breaker = new TreeBlockBreaker(entityPlayer, TreeCapitator.logToLogListMap.get(blockID), TreeCapitator.logToLeafListMap.get(blockID));
                    else
                        breaker = new TreeBlockBreaker(entityPlayer, TreeCapitator.logIDList, TreeCapitator.leafIDList);
                    
                    breaker.onBlockHarvested(world, x, y, z, metadata, entityPlayer);
                }
            }
        }
    }
    
    public static boolean isItemInWorldManagerReplaced(EntityPlayerMP player)
    {
        return !player.theItemInWorldManager.getClass().getSimpleName().equals(ItemInWorldManager.class.getSimpleName());
    }
}
