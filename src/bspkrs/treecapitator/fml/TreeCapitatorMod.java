package bspkrs.treecapitator.fml;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import sharose.mods.idresolver.IDResolverBasic;
import bspkrs.fml.util.Config;
import bspkrs.fml.util.bspkrsCoreProxy;
import bspkrs.treecapitator.Strings;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TreeBlockBreaker;
import bspkrs.treecapitator.TreeCapitator;
import bspkrs.treecapitator.TreeRegistry;
import bspkrs.util.BlockID;
import bspkrs.util.CommonUtils;
import bspkrs.util.ConfigCategory;
import bspkrs.util.Configuration;
import bspkrs.util.Coord;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.IMCCallback;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;

@Mod(name = "TreeCapitator", modid = "TreeCapitator", version = "Forge " + TreeCapitator.VERSION_NUMBER, dependencies = "required-after:mod_bspkrsCore", useMetadata = true)
@NetworkMod(clientSideRequired = false, serverSideRequired = false,
        clientPacketHandlerSpec = @SidedPacketHandler(channels = { "TreeCapitator" }, packetHandler = ClientPacketHandler.class),
        serverPacketHandlerSpec = @SidedPacketHandler(channels = { "TreeCapitator" }, packetHandler = ServerPacketHandler.class),
        connectionHandler = ConnectionHandler.class)
public class TreeCapitatorMod extends DummyModContainer
{
    public static ModVersionChecker versionChecker;
    private final String            versionURL          = "http://bspk.rs/Minecraft/1.5.1/treeCapitatorForge.version";
    private final String            mcfTopic            = "http://www.minecraftforum.net/topic/1009577-";
    
    public static boolean           isCoreModLoaded     = false;
    
    @Metadata(value = "TreeCapitator")
    public static ModMetadata       metadata;
    public Configuration            config;
    
    private static String           idResolverModIDDesc = "The mod ID value for ID Resolver.";
    private static String           idResolverModID     = "IDResolver";
    
    @SidedProxy(clientSide = "bspkrs.treecapitator.fml.ClientProxy", serverSide = "bspkrs.treecapitator.fml.CommonProxy")
    public static CommonProxy       proxy;
    
    @Instance(value = "TreeCapitator")
    public static TreeCapitatorMod  instance;
    
    private static Loader           loader;
    
    public TreeCapitatorMod()
    {
        loader = Loader.instance();
        new bspkrsCoreProxy();
    }
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        TreeCapitator.preInit(true);
        metadata = event.getModMetadata();
        
        File file = event.getSuggestedConfigurationFile();
        
        //        if (Block.class.getSimpleName().equalsIgnoreCase("Block"))
        //        { // debug settings for deobfuscated execution
        //            TreeCapitator.allowDebugLogging = false;
        //            TreeCapitator.onlyDestroyUpwards = true;
        //            TreeCapitator.sneakAction = "disable";
        //            TreeCapitator.maxBreakDistance = 16;
        //            TreeCapitator.allowSmartTreeDetection = true;
        //            if (file.exists())
        //                file.delete();
        //        }
        
        Configuration config = new Configuration(file);
        
        config.load();
        TreeCapitator.onlyDestroyUpwards = Config.getBoolean(config, "onlyDestroyUpwards", Strings.MISC_CTGY, TreeCapitator.onlyDestroyUpwards, TreeCapitator.onlyDestroyUpwardsDesc);
        TreeCapitator.disableInCreative = Config.getBoolean(config, "disableInCreative", Strings.MISC_CTGY, TreeCapitator.disableInCreative, TreeCapitator.disableInCreativeDesc);
        TreeCapitator.disableCreativeDrops = Config.getBoolean(config, "disableCreativeDrops", Strings.MISC_CTGY, TreeCapitator.disableCreativeDrops, TreeCapitator.disableCreativeDropsDesc);
        TreeCapitator.sneakAction = Config.getString(config, "sneakAction", Strings.MISC_CTGY, TreeCapitator.sneakAction, TreeCapitator.sneakActionDesc);
        TreeCapitator.maxBreakDistance = Config.getInt(config, "maxBreakDistance", Strings.MISC_CTGY, TreeCapitator.maxBreakDistance, -1, 100, Strings.maxBreakDistanceDesc);
        TreeCapitator.allowSmartTreeDetection = Config.getBoolean(config, "allowSmartTreeDetection", Strings.MISC_CTGY, TreeCapitator.allowSmartTreeDetection, TreeCapitator.allowSmartTreeDetectionDesc);
        
        TreeCapitator.axeIDList = Config.getString(config, "axeIDList", Strings.ITEM_CTGY, TreeCapitator.axeIDList, TreeCapitator.axeIDListDesc);
        TreeCapitator.shearIDList = Config.getString(config, "shearIDList", Strings.ITEM_CTGY, TreeCapitator.shearIDList, TreeCapitator.shearIDListDesc);
        TreeCapitator.needItem = Config.getBoolean(config, "needItem", Strings.ITEM_CTGY, TreeCapitator.needItem, TreeCapitator.needItemDesc);
        TreeCapitator.allowItemDamage = Config.getBoolean(config, "allowItemDamage", Strings.ITEM_CTGY, TreeCapitator.allowItemDamage, TreeCapitator.allowItemDamageDesc);
        TreeCapitator.allowMoreBlocksThanDamage = Config.getBoolean(config, "allowMoreBlocksThanDamage", Strings.ITEM_CTGY, TreeCapitator.allowMoreBlocksThanDamage, TreeCapitator.allowMoreBlocksThanDamageDesc);
        TreeCapitator.damageMultiplier = Config.getFloat(config, "damageMultiplier", Strings.ITEM_CTGY, TreeCapitator.damageMultiplier, 0.1F, 50.0F, TreeCapitator.damageMultiplierDesc);
        TreeCapitator.useIncreasingItemDamage = Config.getBoolean(config, "useIncreasingItemDamage", Strings.ITEM_CTGY, TreeCapitator.useIncreasingItemDamage, TreeCapitator.useIncreasingItemDamageDesc);
        TreeCapitator.increaseDamageEveryXBlocks = Config.getInt(config, "increaseDamageEveryXBlocks", Strings.ITEM_CTGY, TreeCapitator.increaseDamageEveryXBlocks, 1, 500, TreeCapitator.increaseDamageEveryXBlocksDesc);
        TreeCapitator.damageIncreaseAmount = Config.getFloat(config, "damageIncreaseAmount", Strings.ITEM_CTGY, TreeCapitator.damageIncreaseAmount, 0.1F, 100.0F, TreeCapitator.damageIncreaseAmountDesc);
        
        TreeCapitator.destroyLeaves = Config.getBoolean(config, "destroyLeaves", Strings.LEAF_CTGY, TreeCapitator.destroyLeaves, TreeCapitator.destroyLeavesDesc);
        TreeCapitator.requireLeafDecayCheck = Config.getBoolean(config, "requireLeafDecayCheck", Strings.LEAF_CTGY, TreeCapitator.requireLeafDecayCheck, TreeCapitator.requireLeafDecayCheckDesc);
        TreeCapitator.shearLeaves = Config.getBoolean(config, "shearLeaves", Strings.LEAF_CTGY, TreeCapitator.shearLeaves, TreeCapitator.shearLeavesDesc);
        TreeCapitator.shearVines = Config.getBoolean(config, "shearVines", Strings.LEAF_CTGY, TreeCapitator.shearVines, TreeCapitator.shearVinesDesc);
        
        TreeCapitator.logHardnessNormal = Config.getFloat(config, "logHardnessNormal", Strings.BLOCK_CTGY, TreeCapitator.logHardnessNormal, 0F, 100F, TreeCapitator.logHardnessNormalDesc);
        TreeCapitator.logHardnessModified = Config.getFloat(config, "logHardnessModified", Strings.BLOCK_CTGY, TreeCapitator.logHardnessModified, 0F, 100F, TreeCapitator.logHardnessModifiedDesc);
        
        if (config.hasCategory(Strings.GENERAL))
        {
            TreeCapitator.onlyDestroyUpwards = Config.getBoolean(config, "onlyDestroyUpwards", Strings.GENERAL, TreeCapitator.onlyDestroyUpwards, TreeCapitator.onlyDestroyUpwardsDesc);
            Config.setFromOldCtgy(config, "onlyDestroyUpwards", Strings.GENERAL, Strings.MISC_CTGY);
            TreeCapitator.disableInCreative = Config.getBoolean(config, "disableInCreative", Strings.GENERAL, TreeCapitator.disableInCreative, TreeCapitator.disableInCreativeDesc);
            Config.setFromOldCtgy(config, "disableInCreative", Strings.GENERAL, Strings.MISC_CTGY);
            TreeCapitator.disableCreativeDrops = Config.getBoolean(config, "disableCreativeDrops", Strings.GENERAL, TreeCapitator.disableCreativeDrops, TreeCapitator.disableCreativeDropsDesc);
            Config.setFromOldCtgy(config, "disableCreativeDrops", Strings.GENERAL, Strings.MISC_CTGY);
            TreeCapitator.sneakAction = Config.getString(config, "sneakAction", Strings.GENERAL, TreeCapitator.sneakAction, TreeCapitator.sneakActionDesc);
            Config.setFromOldCtgy(config, "sneakAction", Strings.GENERAL, Strings.MISC_CTGY);
            TreeCapitator.maxBreakDistance = Config.getInt(config, "maxBreakDistance", Strings.GENERAL, TreeCapitator.maxBreakDistance, -1, 100, Strings.maxBreakDistanceDesc);
            Config.setFromOldCtgy(config, "maxBreakDistance", Strings.GENERAL, Strings.MISC_CTGY);
            
            TreeCapitator.axeIDList = Config.getString(config, "axeIDList", Strings.GENERAL, TreeCapitator.axeIDList, TreeCapitator.axeIDListDesc);
            Config.setFromOldCtgy(config, "axeIDList", Strings.GENERAL, Strings.ITEM_CTGY);
            TreeCapitator.shearIDList = Config.getString(config, "shearIDList", Strings.GENERAL, TreeCapitator.shearIDList, TreeCapitator.shearIDListDesc);
            Config.setFromOldCtgy(config, "shearIDList", Strings.GENERAL, Strings.ITEM_CTGY);
            TreeCapitator.needItem = Config.getBoolean(config, "needItem", Strings.GENERAL, TreeCapitator.needItem, TreeCapitator.needItemDesc);
            Config.setFromOldCtgy(config, "needItem", Strings.GENERAL, Strings.ITEM_CTGY);
            TreeCapitator.allowItemDamage = Config.getBoolean(config, "allowItemDamage", Strings.GENERAL, TreeCapitator.allowItemDamage, TreeCapitator.allowItemDamageDesc);
            Config.setFromOldCtgy(config, "allowItemDamage", Strings.GENERAL, Strings.ITEM_CTGY);
            TreeCapitator.allowMoreBlocksThanDamage = Config.getBoolean(config, "allowMoreBlocksThanDamage", Strings.GENERAL, TreeCapitator.allowMoreBlocksThanDamage, TreeCapitator.allowMoreBlocksThanDamageDesc);
            Config.setFromOldCtgy(config, "allowMoreBlocksThanDamage", Strings.GENERAL, Strings.ITEM_CTGY);
            
            TreeCapitator.destroyLeaves = Config.getBoolean(config, "destroyLeaves", Strings.GENERAL, TreeCapitator.destroyLeaves, TreeCapitator.destroyLeavesDesc);
            Config.setFromOldCtgy(config, "destroyLeaves", Strings.GENERAL, Strings.LEAF_CTGY);
            TreeCapitator.shearLeaves = Config.getBoolean(config, "shearLeaves", Strings.GENERAL, TreeCapitator.shearLeaves, TreeCapitator.shearLeavesDesc);
            Config.setFromOldCtgy(config, "shearLeaves", Strings.GENERAL, Strings.LEAF_CTGY);
            TreeCapitator.shearVines = Config.getBoolean(config, "shearVines", Strings.GENERAL, TreeCapitator.shearVines, TreeCapitator.shearVinesDesc);
            Config.setFromOldCtgy(config, "shearVines", Strings.GENERAL, Strings.LEAF_CTGY);
            
            TreeCapitator.logHardnessNormal = Config.getFloat(config, "logHardnessNormal", Strings.GENERAL, TreeCapitator.logHardnessNormal, 0F, 100F, TreeCapitator.logHardnessNormalDesc);
            Config.setFromOldCtgy(config, "logHardnessNormal", Strings.GENERAL, Strings.BLOCK_CTGY);
            TreeCapitator.logHardnessModified = Config.getFloat(config, "logHardnessModified", Strings.GENERAL, TreeCapitator.logHardnessModified, 0F, 100F, TreeCapitator.logHardnessModifiedDesc);
            Config.setFromOldCtgy(config, "logHardnessModified", Strings.GENERAL, Strings.BLOCK_CTGY);
            
            Config.renameCtgy(config, Strings.GENERAL, "z_converted_" + Strings.GENERAL);
            config.addCustomCategoryComment("z_converted_" + Strings.GENERAL, "Your old config settings have been migrated to their new homes.  Except for logIDList.  It's not convertible. :p");
        }
        
        TreeCapitator.allowDebugOutput = Config.getBoolean(config, "allowDebugOutput", Strings.MISC_CTGY, TreeCapitator.allowDebugOutput, TreeCapitator.allowDebugOutputDesc);
        TreeCapitator.allowDebugLogging = Config.getBoolean(config, "allowDebugLogging", Strings.MISC_CTGY, TreeCapitator.allowDebugLogging, TreeCapitator.allowDebugLoggingDesc);
        TreeCapitator.maxLeafIDDist = Config.getInt(config, "maxLeafIDDist", Strings.MISC_CTGY, TreeCapitator.maxLeafIDDist, 1, 8, TreeCapitator.maxLeafIDDistDesc);
        TreeCapitator.minLeavesToID = Config.getInt(config, "minLeavesToID", Strings.MISC_CTGY, TreeCapitator.minLeavesToID, 0, 8, TreeCapitator.minLeavesToIDDesc);
        
        TreeCapitator.allowGetRemoteTreeConfig = Config.getBoolean(config, "allowGetRemoteTreeConfig", Strings.BLOCK_CTGY, TreeCapitator.allowGetRemoteTreeConfig, TreeCapitator.allowGetRemoteTreeConfigDesc);
        TreeCapitator.remoteTreeConfigURL = Config.getString(config, "remoteTreeConfigURL", Strings.BLOCK_CTGY, TreeCapitator.remoteTreeConfigURL, TreeCapitator.remoteTreeConfigURLDesc);
        TreeCapitator.remoteBlockIDConfig = TreeCapitator.getRemoteConfig();
        TreeCapitator.useRemoteTreeConfig = Config.getBoolean(config, "useRemoteTreeConfig", Strings.BLOCK_CTGY, TreeCapitator.useRemoteTreeConfig, TreeCapitator.useRemoteTreeConfigDesc);
        TreeCapitator.useStrictBlockPairing = Config.getBoolean(config, "useStrictBlockPairing", Strings.BLOCK_CTGY, TreeCapitator.useStrictBlockPairing, TreeCapitator.useStrictBlockPairingDesc);
        
        idResolverModID = Config.getString(config, "idResolverModID", Strings.ID_RES_CTGY, idResolverModID, idResolverModIDDesc);
        config.addCustomCategoryComment(Strings.ID_RES_CTGY, "If you are not using ID Resolver, you can safely ignore this section.");
        //                "If you ARE using ID Resolver and your log file does not show any warnings\n" +
        //                "pertaining to ID Resolver, you can still ignore this section. In fact, the\n" +
        //                "only reason you should mess with this section if ShaRose decides to change\n" +
        //                "the Mod ID for ID Resolver."
        
        /*
         * Get / Set Block ID config lists
         */
        if (config.hasCategory("2_block_id"))
            Config.renameCtgy(config, "2_block_id", Strings.TREE_BLOCK_CTGY);
        
        if (!config.hasCategory(Strings.TREE_BLOCK_CTGY))
        {
            for (String key : TreeCapitator.configBlockList.keySet())
            {
                HashMap<String, String> entry = TreeCapitator.configBlockList.get(key);
                for (String blockType : entry.keySet())
                    config.get(Strings.TREE_BLOCK_CTGY + "." + key, blockType, entry.get(blockType));
            }
            TCLog.info("Default block config loaded.");
        }
        else
        {
            TreeCapitator.configBlockList = new HashMap<String, HashMap<String, String>>();
            
            for (String ctgy : config.getCategoryNames())
            {
                if (ctgy.indexOf(Strings.TREE_BLOCK_CTGY + ".") != -1)
                {
                    HashMap<String, String> blocks = new HashMap<String, String>();
                    
                    if (config.getCategory(ctgy).containsKey(Strings.LOGS))
                    {
                        blocks.put(Strings.LOGS, config.getCategory(ctgy).get(Strings.LOGS).getString());
                        
                        if (config.getCategory(ctgy).containsKey(Strings.LEAVES))
                            blocks.put(Strings.LEAVES, config.getCategory(ctgy).get(Strings.LEAVES).getString());
                        
                        TreeCapitator.configBlockList.put(ctgy, blocks);
                    }
                }
            }
            
            TCLog.info("File block config loaded.");
        }
        
        config.addCustomCategoryComment(Strings.TREE_BLOCK_CTGY, TreeCapitator.configBlockIDDesc);
        
        /*
         * Get / Set Third Party block config
         */
        if (!config.hasCategory(Strings.THIRD_PARTY_CFG_CTGY))
        {
            for (String key : TreeCapitator.thirdPartyConfig.keySet())
            {
                HashMap<String, String> tpconfig = TreeCapitator.thirdPartyConfig.get(key);
                for (String entry : tpconfig.keySet())
                    config.get(Strings.THIRD_PARTY_CFG_CTGY + "." + key, entry, tpconfig.get(entry));
                
            }
        }
        else
        {
            TreeCapitator.thirdPartyConfig = new HashMap<String, HashMap<String, String>>();
            
            for (String ctgy : config.getCategoryNames())
            {
                if (ctgy.indexOf(Strings.THIRD_PARTY_CFG_CTGY + ".") != -1)
                {
                    HashMap<String, String> entries = new HashMap<String, String>();
                    ConfigCategory currentCtgy = config.getCategory(ctgy);
                    
                    // fixed issue with old configs not having the right prop name
                    if (currentCtgy.containsKey("modName"))
                        Config.renameProperty(config, ctgy, "modName", Strings.MOD_ID);
                    
                    if (currentCtgy.containsKey(Strings.MOD_ID))
                    {
                        for (String tpCfgEntry : currentCtgy.keySet())
                            entries.put(tpCfgEntry, currentCtgy.get(tpCfgEntry).getString());
                        
                        if (entries.containsKey(Strings.ITEM_VALUES) && !entries.containsKey(Strings.SHIFT_INDEX))
                            entries.put(Strings.SHIFT_INDEX, "true");
                        
                        TreeCapitator.thirdPartyConfig.put(ctgy, entries);
                    }
                }
            }
        }
        
        TreeCapitator.localBlockIDList = TreeCapitator.getStringFromConfigBlockList();
        config.get(Strings.BLOCK_CTGY, "localTreeConfig", "", TreeCapitator.localBlockIDListDesc).set(TreeCapitator.localBlockIDList);
        
        config.addCustomCategoryComment(Strings.THIRD_PARTY_CFG_CTGY, TreeCapitator.thirdPartyConfigDesc);
        
        config.save();
        
        if (bspkrsCoreProxy.instance.allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic);
            versionChecker.checkVersionWithLoggingBySubStringAsFloat(metadata.version.length() - 1, metadata.version.length());
        }
        
        TreeRegistry.instance();
    }
    
    @Init
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new PlayerHandler());
        proxy.onLoad();
    }
    
    @IMCCallback
    public void processIMCMessages(IMCEvent event)
    {   
        
    }
    
    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        getReplacementTagListFromThirdPartyConfigs();
        TreeCapitator.localBlockIDList = TreeCapitator.replaceThirdPartyBlockTags(TreeCapitator.localBlockIDList);
        TreeCapitator.axeIDList = TreeCapitator.replaceThirdPartyBlockTags(TreeCapitator.axeIDList);
        TreeCapitator.shearIDList = TreeCapitator.replaceThirdPartyBlockTags(TreeCapitator.shearIDList);
    }
    
    @ServerStarted
    public void serverStarted(FMLServerStartedEvent event)
    {
        new TreeCapitatorServer();
        TreeCapitator.parseConfigBlockList(TreeCapitator.localBlockIDList);
    }
    
    public void onBlockHarvested(World world, int x, int y, int z, Block block, int metadata, EntityPlayer entityPlayer)
    {
        if (proxy.isEnabled())
        {
            BlockID blockID = new BlockID(block, metadata);
            
            if (TreeRegistry.instance().isRegistered(blockID))
            {
                Coord blockPos = new Coord(x, y, z);
                if (!TreeCapitator.blocksBeingChopped.contains(blockPos))
                {
                    proxy.debugString("BlockID " + blockID + " is a log.");
                    
                    if (TreeBlockBreaker.isBreakingPossible(world, entityPlayer))
                    {
                        TreeCapitator.blocksBeingChopped.add(blockPos);
                        
                        TreeBlockBreaker breaker;
                        
                        if (TreeCapitator.useStrictBlockPairing)
                            breaker = new TreeBlockBreaker(entityPlayer, TreeRegistry.instance().get(blockID));
                        else
                            breaker = new TreeBlockBreaker(entityPlayer, TreeRegistry.instance().genericDefinition());
                        
                        breaker.onBlockHarvested(world, x, y, z, metadata, entityPlayer);
                        
                        TreeCapitator.blocksBeingChopped.remove(blockPos);
                    }
                }
            }
        }
    }
    
    public static boolean isItemInWorldManagerReplaced(EntityPlayerMP player)
    {
        return !player.theItemInWorldManager.getClass().getSimpleName().equals(ItemInWorldManager.class.getSimpleName());
    }
    
    public static void getReplacementTagListFromThirdPartyConfigs()
    {
        
        TCLog.info("Getting Block ID Lists from 3rd party mod configs...");
        
        IDResolverMappingList idrMappings = new IDResolverMappingList();
        
        /*
         * Get IDs from ID Resolver if it's loaded
         */
        if (loader.isModLoaded(idResolverModID))
        {
            TCLog.info("ID Resolver has been detected.  Processing ID config...");
            
            Properties idrKnownIDs = null;
            
            try
            {
                idrKnownIDs = ObfuscationReflectionHelper.getPrivateValue(IDResolverBasic.class, null, "knownIDs");
            }
            catch (Throwable e)
            {
                TreeCapitator.debugString("Error getting knownIDs from ID Resolver: %s", e.getMessage());
                e.printStackTrace();
            }
            
            if (idrKnownIDs != null)
            {
                for (String key : idrKnownIDs.stringPropertyNames())
                {
                    String value = idrKnownIDs.getProperty(key);
                    try
                    {
                        if (!key.startsWith("ItemID.") && !key.startsWith("BlockID."))
                            continue;
                        
                        IDResolverMapping mapping = new IDResolverMapping(key + "=" + value);
                        
                        if (mapping.oldID != 0 && mapping.newID != 0 && !mapping.isStaticMapping())
                        {
                            // IDs are not the same, add to the list of managed IDs
                            idrMappings.add(mapping);
                            TreeCapitator.debugString("Adding entry: %s", key + "=" + value);
                        }
                        else
                            TreeCapitator.debugString("Ignoring entry: %s", key + "=" + value);
                    }
                    catch (Throwable e)
                    {
                        TCLog.severe("Exception caught for line: %s", key + "=" + value);
                    }
                }
            }
        }
        else
            TCLog.info("ID Resolver (Mod ID \"%s\") is not loaded.", idResolverModID);
        
        TreeCapitator.tagMap = new HashMap<String, String>();
        
        for (String key : TreeCapitator.thirdPartyConfig.keySet())
        {
            TreeCapitator.debugString("Processing key " + key);
            HashMap<String, String> tpCfgKey = TreeCapitator.thirdPartyConfig.get(key);
            
            if (loader.isModLoaded(tpCfgKey.get(Strings.MOD_ID)))
            {
                File file = new File(loader.getConfigDir(), tpCfgKey.get(Strings.CONFIG_PATH).trim());
                if (file.exists())
                {
                    Configuration thirdPartyConfig = new Configuration(file);
                    String idrClassName = loader.getIndexedModList().get(tpCfgKey.get(Strings.MOD_ID)).getMod().getClass().getName();
                    thirdPartyConfig.load();
                    boolean useShiftedIndex = true;
                    if (tpCfgKey.containsKey(Strings.SHIFT_INDEX))
                        useShiftedIndex = Boolean.valueOf(tpCfgKey.get(Strings.SHIFT_INDEX));
                    
                    for (String prop : tpCfgKey.keySet())
                        if (!prop.equals(Strings.MOD_ID) && !prop.equals(Strings.CONFIG_PATH) && !prop.equals(Strings.SHIFT_INDEX))
                        {
                            TreeCapitator.debugString("Getting tags from %s...", prop);
                            
                            for (String configID : tpCfgKey.get(prop).trim().split(";"))
                            {
                                String[] subString = configID.trim().split(":");
                                String configValue = thirdPartyConfig.get(/* ctgy */subString[0].trim(), /* prop name */subString[1].trim(), 0).getString();
                                String tagID = "<" + tpCfgKey.get(Strings.MOD_ID) + "." + subString[1].trim() + ">";
                                
                                if (!TreeCapitator.tagMap.containsKey(tagID))
                                {
                                    // TreeCapitator.debugString("configValue: %s", configValue);
                                    IDResolverMapping mapping = idrMappings.getMappingForModAndOldID(idrClassName, CommonUtils.parseInt(configValue));
                                    
                                    if (mapping != null)
                                        configValue = String.valueOf(mapping.newID);
                                    // TreeCapitator.debugString("configValue: %s", configValue);
                                    
                                    if (prop.equals(Strings.ITEM_VALUES) && useShiftedIndex)
                                        configValue = String.valueOf(CommonUtils.parseInt(configValue, -256) + 256);
                                    
                                    // TreeCapitator.debugString("configValue: %s", configValue);
                                    
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
                    TCLog.warning("Mod config file %s does not exist when processing config key %s.", tpCfgKey.get(Strings.CONFIG_PATH), key);
            }
            else
                TreeCapitator.debugString("Mod " + tpCfgKey.get(Strings.MOD_ID) + " is not loaded.");
        }
    }
}
