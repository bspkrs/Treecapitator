package bspkrs.treecapitator.fml;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import bspkrs.fml.util.bspkrsCoreProxy;
import bspkrs.treecapitator.Strings;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TCSettings;
import bspkrs.treecapitator.TreeCapitator;
import bspkrs.treecapitator.TreeRegistry;
import bspkrs.util.BlockID;
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
    public Configuration            config;
    
    @Metadata(value = "TreeCapitator")
    public static ModMetadata       metadata;
    
    @SidedProxy(clientSide = "bspkrs.treecapitator.fml.ClientProxy", serverSide = "bspkrs.treecapitator.fml.CommonProxy")
    public static CommonProxy       proxy;
    
    @Instance(value = "TreeCapitator")
    public static TreeCapitatorMod  instance;
    
    public TreeCapitatorMod()
    {
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
        
        TreeRegistry.instance();
        IDResolverMappingList.instance();
        
        config = new Configuration(file);
        config.load();
        
        TCSettings.allowDebugLogging = config.getBoolean("allowDebugLogging", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.allowDebugLogging, Strings.allowDebugLoggingDesc);
        TCSettings.allowDebugOutput = config.getBoolean("allowDebugOutput", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.allowDebugOutput, Strings.allowDebugOutputDesc);
        TCSettings.allowItemDamage = config.getBoolean("allowItemDamage", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.allowItemDamage, Strings.allowItemDamageDesc);
        TCSettings.allowMoreBlocksThanDamage = config.getBoolean("allowMoreBlocksThanDamage", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.allowMoreBlocksThanDamage, Strings.allowMoreBlocksThanDamageDesc);
        TCSettings.allowSmartTreeDetection = config.getBoolean("allowSmartTreeDetection", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.allowSmartTreeDetection, Strings.allowSmartTreeDetectionDesc);
        TCSettings.axeIDList = config.getString("axeIDList", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.axeIDList, Strings.axeIDListDesc);
        TCSettings.damageIncreaseAmount = config.getFloat("damageIncreaseAmount", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.damageIncreaseAmount, 0.1F, 100.0F, Strings.damageIncreaseAmountDesc);
        TCSettings.damageMultiplier = config.getFloat("damageMultiplier", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.damageMultiplier, 0.1F, 50.0F, Strings.damageMultiplierDesc);
        TCSettings.destroyLeaves = config.getBoolean("destroyLeaves", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.destroyLeaves, Strings.destroyLeavesDesc);
        TCSettings.disableCreativeDrops = config.getBoolean("disableCreativeDrops", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.disableCreativeDrops, Strings.disableCreativeDropsDesc);
        TCSettings.disableInCreative = config.getBoolean("disableInCreative", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.disableInCreative, Strings.disableInCreativeDesc);
        TCSettings.increaseDamageEveryXBlocks = config.getInt("increaseDamageEveryXBlocks", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.increaseDamageEveryXBlocks, 1, 500, Strings.increaseDamageEveryXBlocksDesc);
        TCSettings.needItem = config.getBoolean("needItem", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.needItem, Strings.needItemDesc);
        TCSettings.shearIDList = config.getString("shearIDList", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.shearIDList, Strings.shearIDListDesc);
        TCSettings.shearLeaves = config.getBoolean("shearLeaves", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.shearLeaves, Strings.shearLeavesDesc);
        TCSettings.shearVines = config.getBoolean("shearVines", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.shearVines, Strings.shearVinesDesc);
        TCSettings.sneakAction = config.getString("sneakAction", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.sneakAction, Strings.sneakActionDesc);
        TCSettings.useIncreasingItemDamage = config.getBoolean("useIncreasingItemDamage", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.useIncreasingItemDamage, Strings.useIncreasingItemDamageDesc);
        TCSettings.useStrictBlockPairing = config.getBoolean("useStrictBlockPairing", Strings.GLOBALS_SETTINGS_CTGY,
                TCSettings.useStrictBlockPairing, Strings.useStrictBlockPairingDesc);
        config.addCustomCategoryComment(Strings.GLOBALS_SETTINGS_CTGY, Strings.GLOBALS_SETTINGS_CTGY_DESC);
        
        TCSettings.breakSpeedModifier = config.getFloat("breakSpeedModifier", Strings.PER_TREE_DEFAULTS_CTGY,
                TCSettings.breakSpeedModifier, 0.01F, 1F, Strings.breakSpeedModifierDesc);
        TCSettings.maxHorLogBreakDist = config.getInt("maxHorLogBreakDist", Strings.PER_TREE_DEFAULTS_CTGY,
                TCSettings.maxHorLogBreakDist, -1, 100, Strings.maxHorBreakDistDesc);
        TCSettings.maxVerLogBreakDist = config.getInt("maxVerLogBreakDist", Strings.PER_TREE_DEFAULTS_CTGY,
                TCSettings.maxVerLogBreakDist, -1, 255, Strings.maxVerLogBreakDistDesc);
        TCSettings.maxLeafIDDist = config.getInt("maxLeafIDDist", Strings.PER_TREE_DEFAULTS_CTGY,
                TCSettings.maxLeafIDDist, 1, 8, Strings.maxLeafIDDistDesc);
        TCSettings.minLeavesToID = config.getInt("minLeavesToID", Strings.PER_TREE_DEFAULTS_CTGY,
                TCSettings.minLeavesToID, 0, 8, Strings.minLeavesToIDDesc);
        TCSettings.onlyDestroyUpwards = config.getBoolean("onlyDestroyUpwards", Strings.PER_TREE_DEFAULTS_CTGY,
                TCSettings.onlyDestroyUpwards, Strings.onlyDestroyUpwardsDesc);
        TCSettings.requireLeafDecayCheck = config.getBoolean("requireLeafDecayCheck", Strings.PER_TREE_DEFAULTS_CTGY,
                TCSettings.requireLeafDecayCheck, Strings.requireLeafDecayCheckDesc);
        config.addCustomCategoryComment(Strings.PER_TREE_DEFAULTS_CTGY, Strings.PER_TREE_DEFAULTS_CTGY_DESC);
        
        /*
         * Get / Set 3rd Party Mod configs
         */
        TCSettings.idResolverModID = config.getString("idResolverModID", Strings.TREE_MOD_CFG_CTGY,
                TCSettings.idResolverModID, Strings.idResolverModIDDesc);
        TCSettings.multiMineID = config.getString("multiMineID", Strings.TREE_MOD_CFG_CTGY,
                TCSettings.multiMineID, Strings.multiMineIDDesc);
        TCSettings.userConfigOverridesIMC = config.getBoolean("userConfigOverridesIMC", Strings.TREE_MOD_CFG_CTGY,
                TCSettings.userConfigOverridesIMC, Strings.userConfigOverridesIMCDesc);
        
        if (!config.hasCategory(Strings.TREE_MOD_CFG_CTGY + "." + Strings.VAN_TREES))
        {
            // Write default tree/mod settings to config
            Map<String, ThirdPartyModConfig> m = ModConfigRegistry.instance().defaultConfigs();
            for (Entry<String, ThirdPartyModConfig> e : m.entrySet())
                e.getValue().writeToConfiguration(config, Strings.TREE_MOD_CFG_CTGY + "." + e.getKey());
            
            TCLog.info("Looks like a fresh config; default config loaded.");
        }
        else
            TCLog.info("Proceeding to load tree/mod configs from file.");
        
        for (String ctgy : config.getCategoryNames())
        {
            if (ctgy.indexOf(Strings.TREE_MOD_CFG_CTGY + ".") != -1 && config.getCategory(ctgy).containsKey(Strings.MOD_ID))
            {
                if (Loader.isModLoaded(config.getCategory(ctgy).get(Strings.MOD_ID).getString()))
                    ModConfigRegistry.instance().registerUserModConfig(new ThirdPartyModConfig(config, ctgy));
            }
        }
        
        config.save();
        
        if (bspkrsCoreProxy.instance.allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic);
            versionChecker.checkVersionWithLoggingBySubStringAsFloat(metadata.version.length() - 1, metadata.version.length());
        }
        
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
        ModConfigRegistry.instance().refreshAllTagMaps();
        
        // Multi-Mine stuff
        if (Loader.isModLoaded(TCSettings.multiMineID))
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
                if (TreeRegistry.instance().trackTreeChopEventAt(blockPos))
                {
                    TCLog.debug("BlockID " + blockID + " is a log.");
                    
                    if (TreeCapitator.isBreakingPossible(world, entityPlayer))
                    {
                        TreeCapitator breaker;
                        
                        if (TCSettings.useStrictBlockPairing)
                            breaker = new TreeCapitator(entityPlayer, TreeRegistry.instance().get(blockID));
                        else
                            breaker = new TreeCapitator(entityPlayer, TreeRegistry.instance().masterDefinition());
                        
                        breaker.onBlockHarvested(world, x, y, z, metadata, entityPlayer);
                    }
                }
                TreeRegistry.instance().endTreeChopEventAt(blockPos);
            }
        }
    }
    
    public static boolean isItemInWorldManagerReplaced(EntityPlayerMP player)
    {
        return !player.theItemInWorldManager.getClass().getSimpleName().equals(ItemInWorldManager.class.getSimpleName());
    }
}
