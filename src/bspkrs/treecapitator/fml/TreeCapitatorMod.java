package bspkrs.treecapitator.fml;

import java.io.File;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import bspkrs.fml.util.Config;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TreeBlockBreaker;
import bspkrs.treecapitator.TreeCapitator;
import bspkrs.util.BlockID;
import bspkrs.util.CommonUtils;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.common.Loader;
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
    private final String            versionURL           = "https://dl.dropbox.com/u/20748481/Minecraft/1.4.6/treeCapitatorForge.version";
    private final String            mcfTopic             = "http://www.minecraftforum.net/topic/1009577-";
    
    public static final String      BLOCK_ID_CTGY        = "2_block_id";
    public static final String      THIRD_PARTY_CFG_CTGY = "1_third_party_configs";
    public static final String      BLOCK_CTGY           = "block_settings";
    public static final String      ITEM_CTGY            = "item_settings";
    public static final String      LEAF_CTGY            = "leaf_and_vine_settings";
    public static final String      MISC                 = "miscellaneous_settings";
    public static final String      GENERAL              = Configuration.CATEGORY_GENERAL;
    public static final String      HAS_BEEN_CONVERTED   = "hasBeenConverted";
    
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
        TreeCapitator.init(true);
        metadata = event.getModMetadata();
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        
        config.load();
        TreeCapitator.allowUpdateCheck = Config.getBoolean(config, "allowUpdateCheck", MISC, TreeCapitator.allowUpdateCheck, TreeCapitator.allowUpdateCheckDesc);
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
        TreeCapitator.damageMultiplier = Config.getFloat(config, "damageMultiplier", ITEM_CTGY, TreeCapitator.damageMultiplier, 0.1F, 50.0F, TreeCapitator.damageMultiplierDesc);
        
        TreeCapitator.destroyLeaves = Config.getBoolean(config, "destroyLeaves", LEAF_CTGY, TreeCapitator.destroyLeaves, TreeCapitator.destroyLeavesDesc);
        TreeCapitator.shearLeaves = Config.getBoolean(config, "shearLeaves", LEAF_CTGY, TreeCapitator.shearLeaves, TreeCapitator.shearLeavesDesc);
        TreeCapitator.shearVines = Config.getBoolean(config, "shearVines", LEAF_CTGY, TreeCapitator.shearVines, TreeCapitator.shearVinesDesc);
        
        TreeCapitator.logHardnessNormal = Config.getFloat(config, "logHardnessNormal", BLOCK_CTGY, TreeCapitator.logHardnessNormal, 0F, 100F, TreeCapitator.logHardnessNormalDesc);
        TreeCapitator.logHardnessModified = Config.getFloat(config, "logHardnessModified", BLOCK_CTGY, TreeCapitator.logHardnessModified, 0F, 100F, TreeCapitator.logHardnessModifiedDesc);
        
        if (config.hasCategory(GENERAL) && (!config.hasKey(GENERAL, HAS_BEEN_CONVERTED) || !Boolean.getBoolean(config.get(GENERAL, HAS_BEEN_CONVERTED, true).value)))
        {
            TreeCapitator.allowUpdateCheck = Config.getBoolean(config, "allowUpdateCheck", GENERAL, TreeCapitator.allowUpdateCheck, TreeCapitator.allowUpdateCheckDesc);
            Config.setFromOldCtgy(config, "allowUpdateCheck", GENERAL, MISC);
            TreeCapitator.onlyDestroyUpwards = Config.getBoolean(config, "onlyDestroyUpwards", GENERAL, TreeCapitator.onlyDestroyUpwards, TreeCapitator.onlyDestroyUpwardsDesc);
            Config.setFromOldCtgy(config, "onlyDestroyUpwards", GENERAL, MISC);
            TreeCapitator.disableInCreative = Config.getBoolean(config, "disableInCreative", GENERAL, TreeCapitator.disableInCreative, TreeCapitator.disableInCreativeDesc);
            Config.setFromOldCtgy(config, "disableInCreative", GENERAL, MISC);
            TreeCapitator.disableCreativeDrops = Config.getBoolean(config, "disableCreativeDrops", GENERAL, TreeCapitator.disableCreativeDrops, TreeCapitator.disableCreativeDropsDesc);
            Config.setFromOldCtgy(config, "disableCreativeDrops", GENERAL, MISC);
            TreeCapitator.sneakAction = Config.getString(config, "sneakAction", GENERAL, TreeCapitator.sneakAction, TreeCapitator.sneakActionDesc);
            Config.setFromOldCtgy(config, "sneakAction", GENERAL, MISC);
            TreeCapitator.maxBreakDistance = Config.getInt(config, "maxBreakDistance", GENERAL, TreeCapitator.maxBreakDistance, -1, 100, TreeCapitator.maxBreakDistanceDesc);
            Config.setFromOldCtgy(config, "maxBreakDistance", GENERAL, MISC);
            
            TreeCapitator.axeIDList = Config.getString(config, "axeIDList", GENERAL, TreeCapitator.axeIDList, TreeCapitator.axeIDListDesc);
            Config.setFromOldCtgy(config, "axeIDList", GENERAL, ITEM_CTGY);
            TreeCapitator.shearIDList = Config.getString(config, "shearIDList", GENERAL, TreeCapitator.shearIDList, TreeCapitator.shearIDListDesc);
            Config.setFromOldCtgy(config, "shearIDList", GENERAL, ITEM_CTGY);
            TreeCapitator.needItem = Config.getBoolean(config, "needItem", GENERAL, TreeCapitator.needItem, TreeCapitator.needItemDesc);
            Config.setFromOldCtgy(config, "needItem", GENERAL, ITEM_CTGY);
            TreeCapitator.allowItemDamage = Config.getBoolean(config, "allowItemDamage", GENERAL, TreeCapitator.allowItemDamage, TreeCapitator.allowItemDamageDesc);
            Config.setFromOldCtgy(config, "allowItemDamage", GENERAL, ITEM_CTGY);
            TreeCapitator.allowMoreBlocksThanDamage = Config.getBoolean(config, "allowMoreBlocksThanDamage", GENERAL, TreeCapitator.allowMoreBlocksThanDamage, TreeCapitator.allowMoreBlocksThanDamageDesc);
            Config.setFromOldCtgy(config, "allowMoreBlocksThanDamage", GENERAL, ITEM_CTGY);
            
            TreeCapitator.destroyLeaves = Config.getBoolean(config, "destroyLeaves", GENERAL, TreeCapitator.destroyLeaves, TreeCapitator.destroyLeavesDesc);
            Config.setFromOldCtgy(config, "destroyLeaves", GENERAL, LEAF_CTGY);
            TreeCapitator.shearLeaves = Config.getBoolean(config, "shearLeaves", GENERAL, TreeCapitator.shearLeaves, TreeCapitator.shearLeavesDesc);
            Config.setFromOldCtgy(config, "shearLeaves", GENERAL, LEAF_CTGY);
            TreeCapitator.shearVines = Config.getBoolean(config, "shearVines", GENERAL, TreeCapitator.shearVines, TreeCapitator.shearVinesDesc);
            Config.setFromOldCtgy(config, "shearVines", GENERAL, LEAF_CTGY);
            
            TreeCapitator.logHardnessNormal = Config.getFloat(config, "logHardnessNormal", GENERAL, TreeCapitator.logHardnessNormal, 0F, 100F, TreeCapitator.logHardnessNormalDesc);
            Config.setFromOldCtgy(config, "logHardnessNormal", GENERAL, BLOCK_CTGY);
            TreeCapitator.logHardnessModified = Config.getFloat(config, "logHardnessModified", GENERAL, TreeCapitator.logHardnessModified, 0F, 100F, TreeCapitator.logHardnessModifiedDesc);
            Config.setFromOldCtgy(config, "logHardnessModified", GENERAL, BLOCK_CTGY);
            
            config.get(GENERAL, HAS_BEEN_CONVERTED, true);
        }
        
        TreeCapitator.allowDebugOutput = Config.getBoolean(config, "allowDebugOutput", MISC, TreeCapitator.allowDebugOutput, TreeCapitator.allowDebugOutputDesc);
        TreeCapitator.allowDebugLogging = Config.getBoolean(config, "allowDebugLogging", MISC, TreeCapitator.allowDebugLogging, TreeCapitator.allowDebugLoggingDesc);
        TreeCapitator.maxLeafIDDist = Config.getInt(config, "maxLeafIDDist", MISC, TreeCapitator.maxLeafIDDist, 1, 8, TreeCapitator.maxLeafIDDistDesc);
        TreeCapitator.minLeavesToID = Config.getInt(config, "minLeavesToID", MISC, TreeCapitator.minLeavesToID, 0, 8, TreeCapitator.minLeavesToIDDesc);
        // TreeCapitator.maxLeafBreakDist = Config.getInt(config, "maxLeafBreakDist", MISC, TreeCapitator.maxLeafBreakDist, 0, 6,
        // TreeCapitator.maxLeafBreakDistDesc);
        
        TreeCapitator.allowGetRemoteTreeConfig = Config.getBoolean(config, "allowGetRemoteTreeConfig", BLOCK_CTGY, TreeCapitator.allowGetRemoteTreeConfig, TreeCapitator.allowGetRemoteTreeConfigDesc);
        TreeCapitator.remoteTreeConfigURL = Config.getString(config, "remoteTreeConfigURL", BLOCK_CTGY, TreeCapitator.remoteTreeConfigURL, TreeCapitator.remoteTreeConfigURLDesc);
        TreeCapitator.remoteTreeConfig = TreeCapitator.getRemoteConfig();
        TreeCapitator.localTreeConfig = Config.getString(config, "localTreeConfig", BLOCK_CTGY, TreeCapitator.localTreeConfig, TreeCapitator.localTreeConfigDesc);
        TreeCapitator.useRemoteTreeConfig = Config.getBoolean(config, "useRemoteTreeConfig", BLOCK_CTGY, TreeCapitator.useRemoteTreeConfig, TreeCapitator.useRemoteTreeConfigDesc);
        TreeCapitator.useStrictBlockPairing = Config.getBoolean(config, "useStrictBlockPairing", BLOCK_CTGY, TreeCapitator.useStrictBlockPairing, TreeCapitator.useStrictBlockPairingDesc);
        
        /*
         * Get / Set Block ID config lists
         */
        if (!config.hasCategory(BLOCK_ID_CTGY))
        {
            for (String key : TreeCapitator.configBlockList.keySet())
            {
                HashMap<String, String> entry = TreeCapitator.configBlockList.get(key);
                for (String blockType : entry.keySet())
                    config.get(BLOCK_ID_CTGY + "." + key, blockType, entry.get(blockType));
            }
            TCLog.info("Default block config loaded.");
        }
        else
        {
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
            
            TreeCapitator.localTreeConfig = TreeCapitator.getConfigBlockListString();
            TCLog.info("File block config loaded: %s", TreeCapitator.localTreeConfig);
        }
        
        config.addCustomCategoryComment(BLOCK_ID_CTGY, TreeCapitator.configBlockIDDesc);
        
        /*
         * Get / Set Third Party block config
         */
        if (!config.hasCategory(THIRD_PARTY_CFG_CTGY))
        {
            for (String key : TreeCapitator.thirdPartyConfig.keySet())
            {
                HashMap<String, String> tpconfig = TreeCapitator.thirdPartyConfig.get(key);
                for (String entry : tpconfig.keySet())
                    config.get(THIRD_PARTY_CFG_CTGY + "." + key, entry, tpconfig.get(entry));
            }
        }
        else
        {
            TreeCapitator.thirdPartyConfig = new HashMap<String, HashMap<String, String>>();
            
            for (String ctgy : config.categories.keySet())
            {
                if (ctgy.indexOf(THIRD_PARTY_CFG_CTGY + ".") != -1)
                {
                    HashMap<String, String> entries = new HashMap<String, String>();
                    
                    if (config.getCategory(ctgy).containsKey(TreeCapitator.MOD_NAME))
                    {
                        for (String tpCfgEntry : config.getCategory(ctgy).keySet())
                            entries.put(tpCfgEntry, config.getCategory(ctgy).get(tpCfgEntry).value);
                        
                        if (entries.containsKey(TreeCapitator.ITEM_VALUES) && !entries.containsKey(TreeCapitator.SHIFT_INDEX))
                            entries.put(TreeCapitator.SHIFT_INDEX, "true");
                        
                        TreeCapitator.thirdPartyConfig.put(ctgy, entries);
                    }
                }
            }
        }
        
        TreeCapitator.localTreeConfig = TreeCapitator.getConfigBlockListString();
        
        config.addCustomCategoryComment(THIRD_PARTY_CFG_CTGY, TreeCapitator.thirdPartyConfigDesc);
        
        config.save();
        
        if (TreeCapitator.allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic, TCLog.INSTANCE.getLogger());
            versionChecker.checkVersionWithLogging();
        }
    }
    
    @Init
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new PlayerHandler());
        proxy.onLoad();
    }
    
    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        getReplacementTagListFromThirdPartyConfigs();
        TreeCapitator.localTreeConfig = TreeCapitator.replaceThirdPartyBlockTags(TreeCapitator.localTreeConfig);
        TreeCapitator.axeIDList = TreeCapitator.replaceThirdPartyBlockTags(TreeCapitator.axeIDList);
        TreeCapitator.shearIDList = TreeCapitator.replaceThirdPartyBlockTags(TreeCapitator.shearIDList);
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
                proxy.debugString("BlockID " + blockID + " is a log.");
                
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
                else
                    proxy.debugString("Item is too damaged to chop.");
            }
        }
    }
    
    public static boolean isItemInWorldManagerReplaced(EntityPlayerMP player)
    {
        return !player.theItemInWorldManager.getClass().getSimpleName().equals(ItemInWorldManager.class.getSimpleName());
    }
    
    public static void getReplacementTagListFromThirdPartyConfigs()
    {
        Loader loader = Loader.instance();
        
        TCLog.info("Getting Block ID Lists from 3rd party mod configs...");
        
        TreeCapitator.tagMap = new HashMap<String, String>();
        
        for (String key : TreeCapitator.thirdPartyConfig.keySet())
        {
            TreeCapitator.debugString("Processing key " + key);
            HashMap<String, String> tpCfgKey = TreeCapitator.thirdPartyConfig.get(key);
            
            if (loader.isModLoaded(tpCfgKey.get(TreeCapitator.MOD_NAME)))
            {
                File file = new File(loader.getConfigDir(), tpCfgKey.get(TreeCapitator.CONFIG_PATH).trim());
                if (file.exists())
                {
                    Configuration thirdPartyConfig = new Configuration(file);
                    thirdPartyConfig.load();
                    boolean useShiftedIndex = true;
                    if (tpCfgKey.containsKey(TreeCapitator.SHIFT_INDEX))
                        useShiftedIndex = Boolean.valueOf(tpCfgKey.get(TreeCapitator.SHIFT_INDEX));
                    
                    for (String prop : tpCfgKey.keySet())
                        if (!prop.equals(TreeCapitator.MOD_NAME) && !prop.equals(TreeCapitator.CONFIG_PATH) && !prop.equals(TreeCapitator.SHIFT_INDEX))
                        {
                            TCLog.info("Getting tags from %s...", prop);
                            
                            for (String configID : tpCfgKey.get(prop).trim().split(";"))
                            {
                                String[] subString = configID.trim().split(":");
                                String configValue = thirdPartyConfig.get(/* ctgy */subString[0].trim(), /* prop name */subString[1].trim(), 0).value;
                                String tagID = "<" + tpCfgKey.get(TreeCapitator.MOD_NAME) + "." + subString[1].trim() + ">";
                                if (!TreeCapitator.tagMap.containsKey(tagID))
                                {
                                    if (prop.equals(TreeCapitator.ITEM_VALUES) && useShiftedIndex)
                                        configValue = String.valueOf(CommonUtils.parseInt(configValue, -256) + 256);
                                    
                                    if (!configValue.equals("0"))
                                    {
                                        TreeCapitator.tagMap.put(tagID, configValue);
                                        TreeCapitator.debugString("Third Party Config Tag " + tagID + " will map to " + configValue);
                                    }
                                }
                                else
                                    TCLog.warning("Duplicate Third Party Config Tag detected: " + tagID + " is already mapped to " + TreeCapitator.tagMap.get(tagID));
                            }
                        }
                }
                else
                    TreeCapitator.debugString("Mod config file " + tpCfgKey.get(TreeCapitator.CONFIG_PATH) + " does not exist.");
            }
            else
                TreeCapitator.debugString("Mod " + tpCfgKey.get(TreeCapitator.MOD_NAME) + " is not loaded.");
        }
        
    }
}
