package bspkrs.treecapitator.fml;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import bspkrs.fml.util.bspkrsCoreProxy;
import bspkrs.treecapitator.RegistryNBTManager;
import bspkrs.treecapitator.Strings;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TCSettings;
import bspkrs.treecapitator.TreeCapitator;
import bspkrs.treecapitator.TreeDefinition;
import bspkrs.treecapitator.TreeRegistry;
import bspkrs.util.BlockID;
import bspkrs.util.CommonUtils;
import bspkrs.util.Configuration;
import bspkrs.util.Const;
import bspkrs.util.Coord;
import bspkrs.util.ModVersionChecker;
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
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;

@Mod(name = "TreeCapitator", modid = "TreeCapitator", version = "Forge " + Strings.VERSION_NUMBER,
        dependencies = "required-after:mod_bspkrsCore", useMetadata = true)
@NetworkMod(clientSideRequired = false, serverSideRequired = false,
        clientPacketHandlerSpec = @SidedPacketHandler(channels = { "TreeCapitator" }, packetHandler = TreeCapitatorClient.class),
        serverPacketHandlerSpec = @SidedPacketHandler(channels = { "TreeCapitator" }, packetHandler = TreeCapitatorServer.class),
        connectionHandler = ConnectionHandler.class)
public class TreeCapitatorMod
{
    public static ModVersionChecker versionChecker;
    private final String            versionURL      = "http://bspk.rs/Minecraft/" + Const.MCVERSION + "/treeCapitatorForge.version";
    private final String            mcfTopic        = "http://www.minecraftforum.net/topic/1009577-";
    
    public static boolean           isCoreModLoaded = false;
    private RegistryNBTManager      nbtManager;
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
        
        if (!CommonUtils.isObfuscatedEnv())
        {
            // debug settings for deobfuscated execution
            TCLog.info("*** Deobfuscated environment detected... using debug settings ***");
            TCSettings.allowDebugLogging = true;
            TCSettings.onlyDestroyUpwards = true;
            TCSettings.sneakAction = "disable";
            TCSettings.maxHorLogBreakDist = 16;
            TCSettings.allowSmartTreeDetection = true;
            TCSettings.useStrictBlockPairing = true;
            // TCSettings.enableEnchantmentMode = true;
            if (file.exists())
                file.delete();
        }
        
        TreeRegistry.instance();
        IDResolverMappingList.instance();
        TCConfigHandler.setInstance(file);
        
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
    
    /**
     * This method is provided as an example for mods to use if they want to send an IMC message to TreeCapitator. The message should be
     * sent in the @Init mod event method.
     */
    protected void exampleIMCSendMessage()
    {
        if (Loader.isModLoaded("TreeCapitator"))
        {
            NBTTagCompound tpModCfg = new NBTTagCompound();
            tpModCfg.setString("modID", "ExtraBiomesXL");
            tpModCfg.setString("configPath", "extrabiomes/extrabiomes.cfg");
            tpModCfg.setString("blockConfigKeys", "block:customlog.id; block:quarterlog0.id; block:quarterlog1.id; block:quarterlog2.id; block:quarterlog3.id; " +
                    "block:autumnleaves.id; block:greenleaves.id");
            tpModCfg.setString("itemConfigKeys", "");
            tpModCfg.setString("axeIDList", "");
            tpModCfg.setString("shearsIDList", "");
            tpModCfg.setBoolean("useShiftedItemID", true);
            
            NBTTagList treeList = new NBTTagList();
            
            // Vanilla Oak additions
            NBTTagCompound tree = new NBTTagCompound();
            tree.setString("treeName", "vanilla_oak");
            tree.setString("logConfigKeys", "<block:quarterlog0.id>,2; <block:quarterlog1.id>,2; <block:quarterlog2.id>,2; <block:quarterlog3.id>,2;");
            tree.setString("leafConfigKeys", "<block:autumnleaves.id>");
            treeList.appendTag(tree);
            
            // Vanilla Spruce additions
            tree = new NBTTagCompound();
            tree.setString("treeName", "vanilla_spruce");
            tree.setString("logConfigKeys", "");
            tree.setString("leafConfigKeys", "<block:autumnleaves.id>");
            treeList.appendTag(tree);
            
            // EBXL fir
            tree = new NBTTagCompound();
            tree.setString("treeName", "fir");
            tree.setString("logConfigKeys", "<block:customlog.id>,0; <block:quarterlog0.id>,1; <block:quarterlog1.id>,1; <block:quarterlog2.id>,1; <block:quarterlog3.id>,1");
            tree.setString("leafConfigKeys", "<block:greenleaves.id>,0; <block:greenleaves.id>,8");
            tree.setInteger("maxHorLeafBreakDist", 10);
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            
            // EBXL redwood
            tree = new NBTTagCompound();
            tree.setString("treeName", "redwood");
            tree.setString("logConfigKeys", "<block:quarterlog0.id>,0; <block:quarterlog1.id>,0; <block:quarterlog2.id>,0; <block:quarterlog3.id>,0");
            tree.setString("leafConfigKeys", "<block:greenleaves.id>,1; <block:greenleaves.id>,9");
            tree.setInteger("maxHorLeafBreakDist", 10);
            tree.setBoolean("requireLeafDecayCheck", false);
            treeList.appendTag(tree);
            
            // EBXL acacia
            tree = new NBTTagCompound();
            tree.setString("treeName", "acacia");
            tree.setString("logConfigKeys", "<block:customlog.id>,1");
            tree.setString("leafConfigKeys", "<block:greenleaves.id>,2");
            treeList.appendTag(tree);
            
            tpModCfg.setTag("trees", treeList);
            
            FMLInterModComms.sendMessage("TreeCapitator", metadata.modId, tpModCfg);
        }
    }
    
    @IMCCallback
    public void processIMCMessages(IMCEvent event)
    {
        for (IMCMessage msg : event.getMessages().asList())
            if (msg.isNBTMessage())
            {
                TCLog.debug("Received IMC message from mod %s.", msg.getSender());
                ModConfigRegistry.instance().registerIMCModConfig(new ThirdPartyModConfig(msg.getNBTValue()));
            }
            else
                TCLog.warning("Mod %s send an IMC message, but it is not an NBT tag message. The message will be ignored.", msg.getSender());
    }
    
    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        ModConfigRegistry.instance().applyPrioritizedModConfigs();
        
        // Multi-Mine stuff
        if (Loader.isModLoaded(TCSettings.multiMineModID))
        {
            String s = TreeRegistry.instance().getMultiMineExclusionString();
            TCLog.info("For Multi-Mine compatibility you should put this list in the S:\"Excluded Block IDs\" config setting in AS_MultiMine.cfg: \"%s\"", s);
            TCConfigHandler.instance().config.get(Strings.TREE_MOD_CFG_CTGY, Strings.MM_EXCL_LIST, "", Strings.MM_EXCL_LIST_DESC).set(s);
            TCConfigHandler.instance().config.save();
        }
    }
    
    @ServerStarted
    public void serverStarted(FMLServerStartedEvent event)
    {
        // Make sure the NBT manager is initialized while we can still be sure of the values in our local objects
        nbtManager();
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
                        
                        TreeDefinition treeDef = TreeRegistry.instance().get(blockID);
                        
                        if (treeDef != null)
                        {
                            breaker = new TreeCapitator(entityPlayer, treeDef);
                            breaker.onBlockHarvested(world, x, y, z, metadata, entityPlayer);
                        }
                        else
                            TCLog.severe("TreeRegistry reported block ID %s is a log, but TreeDefinition lookup failed! " +
                                    "Please report this to bspkrs (include a copy of this log and your config).", blockID);
                        
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
    
    public RegistryNBTManager nbtManager()
    {
        if (nbtManager == null)
            nbtManager = new RegistryNBTManager();
        
        return nbtManager;
    }
}
