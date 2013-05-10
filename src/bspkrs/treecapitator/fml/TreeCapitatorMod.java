package bspkrs.treecapitator.fml;

import java.io.File;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import bspkrs.fml.util.bspkrsCoreProxy;
import bspkrs.treecapitator.Strings;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TreeCapitator;
import bspkrs.treecapitator.TCSettings;
import bspkrs.treecapitator.TreeRegistry;
import bspkrs.util.BlockID;
import bspkrs.util.ConfigCategory;
import bspkrs.util.Configuration;
import bspkrs.util.Const;
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
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;

@Mod(name = "TreeCapitator", modid = "TreeCapitator", version = "Forge " + Strings.VERSION_NUMBER, dependencies = "required-after:mod_bspkrsCore", useMetadata = true)
@NetworkMod(clientSideRequired = false, serverSideRequired = false,
        clientPacketHandlerSpec = @SidedPacketHandler(channels = { "TreeCapitator" }, packetHandler = ClientPacketHandler.class),
        serverPacketHandlerSpec = @SidedPacketHandler(channels = { "TreeCapitator" }, packetHandler = ServerPacketHandler.class),
        connectionHandler = ConnectionHandler.class)
public class TreeCapitatorMod extends DummyModContainer
{
    public static ModVersionChecker versionChecker;
    private final String            versionURL      = "http://bspk.rs/Minecraft/" + Const.MCVERSION + "/treeCapitatorForge.version";
    private final String            mcfTopic        = "http://www.minecraftforum.net/topic/1009577-";
    
    public static boolean           isCoreModLoaded = false;
    
    @Metadata(value = "TreeCapitator")
    public static ModMetadata       metadata;
    private Configuration           config;
    
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
        TCSettings.preInit(true);
        metadata = event.getModMetadata();
        
        File file = event.getSuggestedConfigurationFile();
        
        if (Block.class.getSimpleName().equals("Block"))
        { // debug settings for deobfuscated execution
            TCSettings.allowDebugLogging = true;
            TCSettings.onlyDestroyUpwards = true;
            TCSettings.sneakAction = "disable";
            TCSettings.maxHorLogBreakDist = 16;
            TCSettings.allowSmartTreeDetection = true;
            if (file.exists())
                file.delete();
        }
        
        config = new Configuration(file);
        
        config.load();
        TCSettings.onlyDestroyUpwards = config.getBoolean("onlyDestroyUpwards", Strings.MISC_CTGY, TCSettings.onlyDestroyUpwards, Strings.onlyDestroyUpwardsDesc);
        TCSettings.disableInCreative = config.getBoolean("disableInCreative", Strings.MISC_CTGY, TCSettings.disableInCreative, Strings.disableInCreativeDesc);
        TCSettings.disableCreativeDrops = config.getBoolean("disableCreativeDrops", Strings.MISC_CTGY, TCSettings.disableCreativeDrops, Strings.disableCreativeDropsDesc);
        TCSettings.sneakAction = config.getString("sneakAction", Strings.MISC_CTGY, TCSettings.sneakAction, Strings.sneakActionDesc);
        TCSettings.maxHorLogBreakDist = config.getInt("maxHorLogBreakDist", Strings.MISC_CTGY, TCSettings.maxHorLogBreakDist, -1, 100, Strings.maxHorBreakDistDesc);
        TCSettings.maxVerLogBreakDist = config.getInt("maxVerLogBreakDist", Strings.MISC_CTGY, TCSettings.maxVerLogBreakDist, -1, 255, Strings.maxVerLogBreakDistDesc);
        TCSettings.allowSmartTreeDetection = config.getBoolean("allowSmartTreeDetection", Strings.MISC_CTGY, TCSettings.allowSmartTreeDetection, Strings.allowSmartTreeDetectionDesc);
        
        TCSettings.axeIDList = config.getString("axeIDList", Strings.ITEM_CTGY, TCSettings.axeIDList, Strings.axeIDListDesc);
        TCSettings.shearIDList = config.getString("shearIDList", Strings.ITEM_CTGY, TCSettings.shearIDList, Strings.shearIDListDesc);
        TCSettings.needItem = config.getBoolean("needItem", Strings.ITEM_CTGY, TCSettings.needItem, Strings.needItemDesc);
        TCSettings.allowItemDamage = config.getBoolean("allowItemDamage", Strings.ITEM_CTGY, TCSettings.allowItemDamage, Strings.allowItemDamageDesc);
        TCSettings.allowMoreBlocksThanDamage = config.getBoolean("allowMoreBlocksThanDamage", Strings.ITEM_CTGY, TCSettings.allowMoreBlocksThanDamage, Strings.allowMoreBlocksThanDamageDesc);
        TCSettings.damageMultiplier = config.getFloat("damageMultiplier", Strings.ITEM_CTGY, TCSettings.damageMultiplier, 0.1F, 50.0F, Strings.damageMultiplierDesc);
        TCSettings.useIncreasingItemDamage = config.getBoolean("useIncreasingItemDamage", Strings.ITEM_CTGY, TCSettings.useIncreasingItemDamage, Strings.useIncreasingItemDamageDesc);
        TCSettings.increaseDamageEveryXBlocks = config.getInt("increaseDamageEveryXBlocks", Strings.ITEM_CTGY, TCSettings.increaseDamageEveryXBlocks, 1, 500, Strings.increaseDamageEveryXBlocksDesc);
        TCSettings.damageIncreaseAmount = config.getFloat("damageIncreaseAmount", Strings.ITEM_CTGY, TCSettings.damageIncreaseAmount, 0.1F, 100.0F, Strings.damageIncreaseAmountDesc);
        
        TCSettings.destroyLeaves = config.getBoolean("destroyLeaves", Strings.LEAF_CTGY, TCSettings.destroyLeaves, Strings.destroyLeavesDesc);
        TCSettings.requireLeafDecayCheck = config.getBoolean("requireLeafDecayCheck", Strings.LEAF_CTGY, TCSettings.requireLeafDecayCheck, Strings.requireLeafDecayCheckDesc);
        TCSettings.shearLeaves = config.getBoolean("shearLeaves", Strings.LEAF_CTGY, TCSettings.shearLeaves, Strings.shearLeavesDesc);
        TCSettings.shearVines = config.getBoolean("shearVines", Strings.LEAF_CTGY, TCSettings.shearVines, Strings.shearVinesDesc);
        
        TCSettings.logHardnessNormal = config.getFloat("logHardnessNormal", Strings.BLOCK_CTGY, TCSettings.logHardnessNormal, 0F, 100F, Strings.logHardnessNormalDesc);
        TCSettings.logHardnessModified = config.getFloat("logHardnessModified", Strings.BLOCK_CTGY, TCSettings.logHardnessModified, 0F, 100F, Strings.logHardnessModifiedDesc);
        
        //        Stop working around old configs
        //        if (config.hasCategory(Strings.GENERAL))
        //        {
        //            TreeCapitator.onlyDestroyUpwards = config.getBoolean("onlyDestroyUpwards", Strings.GENERAL, TreeCapitator.onlyDestroyUpwards, TreeCapitator.onlyDestroyUpwardsDesc);
        //            config.setFromOldCtgy("onlyDestroyUpwards", Strings.GENERAL, Strings.MISC_CTGY);
        //            TreeCapitator.disableInCreative = config.getBoolean("disableInCreative", Strings.GENERAL, TreeCapitator.disableInCreative, TreeCapitator.disableInCreativeDesc);
        //            config.setFromOldCtgy("disableInCreative", Strings.GENERAL, Strings.MISC_CTGY);
        //            TreeCapitator.disableCreativeDrops = config.getBoolean("disableCreativeDrops", Strings.GENERAL, TreeCapitator.disableCreativeDrops, TreeCapitator.disableCreativeDropsDesc);
        //            config.setFromOldCtgy("disableCreativeDrops", Strings.GENERAL, Strings.MISC_CTGY);
        //            TreeCapitator.sneakAction = config.getString("sneakAction", Strings.GENERAL, TreeCapitator.sneakAction, TreeCapitator.sneakActionDesc);
        //            config.setFromOldCtgy("sneakAction", Strings.GENERAL, Strings.MISC_CTGY);
        //            TreeCapitator.maxBreakDistance = config.getInt("maxBreakDistance", Strings.GENERAL, TreeCapitator.maxBreakDistance, -1, 100, Strings.maxBreakDistanceDesc);
        //            config.setFromOldCtgy("maxBreakDistance", Strings.GENERAL, Strings.MISC_CTGY);
        //            
        //            TreeCapitator.axeIDList = config.getString("axeIDList", Strings.GENERAL, TreeCapitator.axeIDList, TreeCapitator.axeIDListDesc);
        //            config.setFromOldCtgy("axeIDList", Strings.GENERAL, Strings.ITEM_CTGY);
        //            TreeCapitator.shearIDList = config.getString("shearIDList", Strings.GENERAL, TreeCapitator.shearIDList, TreeCapitator.shearIDListDesc);
        //            config.setFromOldCtgy("shearIDList", Strings.GENERAL, Strings.ITEM_CTGY);
        //            TreeCapitator.needItem = config.getBoolean("needItem", Strings.GENERAL, TreeCapitator.needItem, TreeCapitator.needItemDesc);
        //            config.setFromOldCtgy("needItem", Strings.GENERAL, Strings.ITEM_CTGY);
        //            TreeCapitator.allowItemDamage = config.getBoolean("allowItemDamage", Strings.GENERAL, TreeCapitator.allowItemDamage, TreeCapitator.allowItemDamageDesc);
        //            config.setFromOldCtgy("allowItemDamage", Strings.GENERAL, Strings.ITEM_CTGY);
        //            TreeCapitator.allowMoreBlocksThanDamage = config.getBoolean("allowMoreBlocksThanDamage", Strings.GENERAL, TreeCapitator.allowMoreBlocksThanDamage, TreeCapitator.allowMoreBlocksThanDamageDesc);
        //            config.setFromOldCtgy("allowMoreBlocksThanDamage", Strings.GENERAL, Strings.ITEM_CTGY);
        //            
        //            TreeCapitator.destroyLeaves = config.getBoolean("destroyLeaves", Strings.GENERAL, TreeCapitator.destroyLeaves, TreeCapitator.destroyLeavesDesc);
        //            config.setFromOldCtgy("destroyLeaves", Strings.GENERAL, Strings.LEAF_CTGY);
        //            TreeCapitator.shearLeaves = config.getBoolean("shearLeaves", Strings.GENERAL, TreeCapitator.shearLeaves, TreeCapitator.shearLeavesDesc);
        //            config.setFromOldCtgy("shearLeaves", Strings.GENERAL, Strings.LEAF_CTGY);
        //            TreeCapitator.shearVines = config.getBoolean("shearVines", Strings.GENERAL, TreeCapitator.shearVines, TreeCapitator.shearVinesDesc);
        //            config.setFromOldCtgy("shearVines", Strings.GENERAL, Strings.LEAF_CTGY);
        //            
        //            TreeCapitator.logHardnessNormal = config.getFloat("logHardnessNormal", Strings.GENERAL, TreeCapitator.logHardnessNormal, 0F, 100F, TreeCapitator.logHardnessNormalDesc);
        //            config.setFromOldCtgy("logHardnessNormal", Strings.GENERAL, Strings.BLOCK_CTGY);
        //            TreeCapitator.logHardnessModified = config.getFloat("logHardnessModified", Strings.GENERAL, TreeCapitator.logHardnessModified, 0F, 100F, TreeCapitator.logHardnessModifiedDesc);
        //            config.setFromOldCtgy("logHardnessModified", Strings.GENERAL, Strings.BLOCK_CTGY);
        //            
        //            config.renameCtgy(Strings.GENERAL, "z_converted_" + Strings.GENERAL);
        //            config.addCustomCategoryComment("z_converted_" + Strings.GENERAL, "Your old config settings have been migrated to their new homes.  Except for logIDList.  It's not convertible. :p");
        //        }
        
        TCSettings.allowDebugOutput = config.getBoolean("allowDebugOutput", Strings.MISC_CTGY, TCSettings.allowDebugOutput, Strings.allowDebugOutputDesc);
        TCSettings.allowDebugLogging = config.getBoolean("allowDebugLogging", Strings.MISC_CTGY, TCSettings.allowDebugLogging, Strings.allowDebugLoggingDesc);
        TCSettings.maxLeafIDDist = config.getInt("maxLeafIDDist", Strings.MISC_CTGY, TCSettings.maxLeafIDDist, 1, 8, Strings.maxLeafIDDistDesc);
        TCSettings.minLeavesToID = config.getInt("minLeavesToID", Strings.MISC_CTGY, TCSettings.minLeavesToID, 0, 8, Strings.minLeavesToIDDesc);
        
        TCSettings.useStrictBlockPairing = config.getBoolean("useStrictBlockPairing", Strings.BLOCK_CTGY, TCSettings.useStrictBlockPairing, Strings.useStrictBlockPairingDesc);
        
        TCSettings.idResolverModID = config.getString("idResolverModID", Strings.ID_RES_CTGY, TCSettings.idResolverModID, Strings.idResolverModIDDesc);
        config.addCustomCategoryComment(Strings.ID_RES_CTGY, "If you are not using ID Resolver, you can safely ignore this section.");
        //                "If you ARE using ID Resolver and your log file does not show any warnings\n" +
        //                "pertaining to ID Resolver, you can still ignore this section. In fact, the\n" +
        //                "only reason you should mess with this section if ShaRose decides to change\n" +
        //                "the Mod ID for ID Resolver."
        IDResolverMappingList.instance();
        
        /*
         * Get / Set Block ID config lists
         */
        //        if (config.hasCategory("2_block_id"))
        //            config.renameCtgy("2_block_id", Strings.TREE_BLOCK_CTGY);
        
        if (!config.hasCategory(Strings.TREE_BLOCK_CTGY))
        {
            for (String key : TCSettings.configBlockList.keySet())
            {
                HashMap<String, String> entry = TCSettings.configBlockList.get(key);
                for (String blockType : entry.keySet())
                    config.get(Strings.TREE_BLOCK_CTGY + "." + key, blockType, entry.get(blockType));
            }
            TCLog.info("Default block config loaded.");
        }
        else
        {
            TCSettings.configBlockList = new HashMap<String, HashMap<String, String>>();
            
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
                        
                        TCSettings.configBlockList.put(ctgy, blocks);
                    }
                }
            }
            
            TCLog.info("File block config loaded.");
        }
        
        config.addCustomCategoryComment(Strings.TREE_BLOCK_CTGY, Strings.configBlockIDDesc);
        
        /*
         * Get / Set Third Party block config
         */
        if (!config.hasCategory(Strings.THIRD_PARTY_CFG_CTGY))
        {
            for (String key : TCSettings.thirdPartyConfig.keySet())
            {
                HashMap<String, String> tpconfig = TCSettings.thirdPartyConfig.get(key);
                for (String entry : tpconfig.keySet())
                    config.get(Strings.THIRD_PARTY_CFG_CTGY + "." + key, entry, tpconfig.get(entry));
                
            }
        }
        else
        {
            TCSettings.thirdPartyConfig = new HashMap<String, HashMap<String, String>>();
            
            for (String ctgy : config.getCategoryNames())
            {
                if (ctgy.indexOf(Strings.THIRD_PARTY_CFG_CTGY + ".") != -1)
                {
                    HashMap<String, String> entries = new HashMap<String, String>();
                    ConfigCategory currentCtgy = config.getCategory(ctgy);
                    
                    // fixed issue with old configs not having the right prop name
                    if (currentCtgy.containsKey("modName"))
                        config.renameProperty(ctgy, "modName", Strings.MOD_ID);
                    
                    if (currentCtgy.containsKey(Strings.MOD_ID))
                    {
                        for (String tpCfgEntry : currentCtgy.keySet())
                            entries.put(tpCfgEntry, currentCtgy.get(tpCfgEntry).getString());
                        
                        if (entries.containsKey(Strings.ITEM_VALUES) && !entries.containsKey(Strings.SHIFT_INDEX))
                            entries.put(Strings.SHIFT_INDEX, "true");
                        
                        TCSettings.thirdPartyConfig.put(ctgy, entries);
                    }
                }
            }
        }
        
        //Strings.localBlockIDList = TreeCapitator.getStringFromConfigBlockList();
        //config.get(Strings.BLOCK_CTGY, "localTreeConfig", "", TreeCapitator.localBlockIDListDesc).set(Strings.localBlockIDList);
        
        config.addCustomCategoryComment(Strings.THIRD_PARTY_CFG_CTGY, Strings.thirdPartyConfigDesc);
        
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
        //Strings.localBlockIDList = TreeCapitator.replaceThirdPartyBlockTags(Strings.localBlockIDList);
        TCSettings.axeIDList = TCSettings.replaceThirdPartyBlockTags(TCSettings.axeIDList);
        TCSettings.shearIDList = TCSettings.replaceThirdPartyBlockTags(TCSettings.shearIDList);
        
        // Multi-Mine stuff
        if (Loader.instance().isModLoaded(TCSettings.multiMineID))
        {
            TCLog.info("It looks like you're using Multi-Mine.  You should put this list in the S:\"Excluded Block IDs\" config setting in AS_MultiMine.cfg:\n\"%s\"",
                    TreeRegistry.instance().getMultiMineExclusionString());
        }
    }
    
    @ServerStarted
    public void serverStarted(FMLServerStartedEvent event)
    {
        new TreeCapitatorServer();
        //TreeCapitator.parseConfigBlockList(Strings.localBlockIDList);
    }
    
    public void onBlockHarvested(World world, int x, int y, int z, Block block, int metadata, EntityPlayer entityPlayer)
    {
        if (proxy.isEnabled())
        {
            BlockID blockID = new BlockID(block, metadata);
            
            if (TreeRegistry.instance().isRegistered(blockID))
            {
                Coord blockPos = new Coord(x, y, z);
                if (!TCSettings.blocksBeingChopped.contains(blockPos))
                {
                    TCLog.debug("BlockID " + blockID + " is a log.");
                    
                    if (TreeCapitator.isBreakingPossible(world, entityPlayer))
                    {
                        TCSettings.blocksBeingChopped.add(blockPos);
                        
                        TreeCapitator breaker;
                        
                        if (TCSettings.useStrictBlockPairing)
                            breaker = new TreeCapitator(entityPlayer, TreeRegistry.instance().get(blockID));
                        else
                            breaker = new TreeCapitator(entityPlayer, TreeRegistry.instance().genericDefinition());
                        
                        breaker.onBlockHarvested(world, x, y, z, metadata, entityPlayer);
                        
                        TCSettings.blocksBeingChopped.remove(blockPos);
                    }
                }
            }
        }
    }
    
    public static boolean isItemInWorldManagerReplaced(EntityPlayerMP player)
    {
        return !player.theItemInWorldManager.getClass().getSimpleName().equals(ItemInWorldManager.class.getSimpleName());
    }
    
    @Deprecated
    public void getReplacementTagListFromThirdPartyConfigs()
    {   
        
    }
}
