package bspkrs.treecapitator.fml;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import bspkrs.fml.util.bspkrsCoreProxy;
import bspkrs.treecapitator.RegistryNBTManager;
import bspkrs.treecapitator.Strings;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TCSettings;
import bspkrs.treecapitator.ToolRegistry;
import bspkrs.treecapitator.TreeDefinition;
import bspkrs.treecapitator.TreeRegistry;
import bspkrs.treecapitator.Treecapitator;
import bspkrs.util.BlockID;
import bspkrs.util.CommonUtils;
import bspkrs.util.Configuration;
import bspkrs.util.Const;
import bspkrs.util.Coord;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;

@Mod(name = "TreeCapitator", modid = "TreeCapitator", version = "Forge " + Strings.VERSION_NUMBER,
        dependencies = "required-after:mod_bspkrsCore", useMetadata = true)
@NetworkMod(clientSideRequired = true, serverSideRequired = false,
        clientPacketHandlerSpec = @SidedPacketHandler(channels = { "TreeCapitator" }, packetHandler = TreeCapitatorClient.class),
        serverPacketHandlerSpec = @SidedPacketHandler(channels = { "TreeCapitator" }, packetHandler = TreeCapitatorServer.class),
        connectionHandler = ConnectionHandler.class)
public class TreeCapitatorMod
{
    public static ModVersionChecker versionChecker;
    private final String            versionURL      = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/treeCapitatorForge.version";
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
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        TCSettings.preInit(true);
        metadata = event.getModMetadata();
        
        File file = event.getSuggestedConfigurationFile();
        
        if (!CommonUtils.isObfuscatedEnv())
        {
            // debug settings for deobfuscated execution
            //            TCLog.info("*** Deobfuscated environment detected... using debug settings ***");
            //            TCSettings.allowDebugLogging = true;
            //            TCSettings.onlyDestroyUpwards = true;
            //            TCSettings.sneakAction = "disable";
            //            TCSettings.maxHorLogBreakDist = 16;
            //            TCSettings.allowSmartTreeDetection = true;
            //            TCSettings.useStrictBlockPairing = true;
            //            TCSettings.enableEnchantmentMode = true;
            //            if (file.exists())
            //                file.delete();
        }
        
        IDResolverMappingList.instance();
        TCConfigHandler.setInstance(file);
        
        if (bspkrsCoreProxy.instance.allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic);
            versionChecker.checkVersionWithLoggingBySubStringAsFloat(metadata.version.length() - 2, metadata.version.length());
        }
        
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new PlayerHandler());
        proxy.onLoad();
        
    }
    
    @EventHandler
    public void processIMCMessages(IMCEvent event)
    {
        for (IMCMessage msg : event.getMessages().asList())
            if (msg.isNBTMessage())
            {
                TCLog.info("Received IMC message from mod %s.", msg.getSender());
                ModConfigRegistry.instance().registerIMCModConfig(new ThirdPartyModConfig(msg.getNBTValue()));
            }
            else
                TCLog.warning("Mod %s send an IMC message, but it is not an NBT object message. The message will be ignored.", msg.getSender());
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        // As opposed to the block blacklist, the item blacklist is read before registering tools 
        // to prevent them from being registered in the first place.
        ToolRegistry.instance().readBlacklistFromDelimitedString(TCSettings.itemIDBlacklist);
        ModConfigRegistry.instance().applyPrioritizedModConfigs();
        
        OreDictionaryHandler.instance().generateAndRegisterOreDictionaryTreeDefinitions();
        
        // Multi-Mine stuff
        if (Loader.isModLoaded(TCSettings.multiMineModID))
        {
            String s = TreeRegistry.instance().getMultiMineExclusionString();
            TCLog.info("For Multi-Mine compatibility you should put this list in the S:\"Excluded Block IDs\" config setting in AS_MultiMine.cfg: \"%s\"", s);
            TCConfigHandler.instance().config.get(Strings.TREE_MOD_CFG_CTGY, Strings.MM_EXCL_LIST, "", Strings.MM_EXCL_LIST_DESC).set(s);
            TCConfigHandler.instance().config.save();
        }
        
        // This must be done after all trees are registered to avoid screwing up the registration process
        // TODO: refactor TreeRegistry registration code to prevent blacklisted blocks from being registered
        TreeRegistry.instance().readBlacklistFromDelimitedString(TCSettings.blockIDBlacklist);
        
        // Make sure the NBT manager is initialized while we can still be sure of the values in our local objects
        nbtManager();
    }
    
    public void onBlockHarvested(World world, int x, int y, int z, Block block, int metadata, EntityPlayer entityPlayer)
    {
        if (proxy.isEnabled() && !world.isRemote)
        {
            BlockID blockID = new BlockID(block, metadata);
            
            if (TreeRegistry.instance().isRegistered(blockID))
            {
                Coord blockPos = new Coord(x, y, z);
                if (TreeRegistry.instance().trackTreeChopEventAt(blockPos))
                {
                    TCLog.debug("BlockID " + blockID + " is a log.");
                    
                    if (Treecapitator.isBreakingEnabled(entityPlayer) && Treecapitator.isBreakingPossible(world, entityPlayer, true))
                    {
                        TreeDefinition treeDef = TreeRegistry.instance().get(blockID);
                        
                        if (treeDef != null)
                        {
                            Treecapitator breaker = new Treecapitator(entityPlayer, treeDef);
                            breaker.onBlockHarvested(world, x, y, z, metadata, entityPlayer);
                        }
                        else
                            TCLog.severe("TreeRegistry reported block ID %s is a log, but TreeDefinition lookup failed! " +
                                    "Please report this to bspkrs (include a copy of this log file and your config).", blockID);
                        
                    }
                    else
                        TCLog.debug("Chopping disabled due to player state or gamemode.");
                    
                    TreeRegistry.instance().endTreeChopEventAt(blockPos);
                }
                else
                    TCLog.debug("Previous chopping event detected for block @%s", blockPos.toString());
                
            }
        }
    }
    
    public static boolean isItemInWorldManagerReplaced(EntityPlayerMP player)
    {
        if (player != null)
            return !player.theItemInWorldManager.getClass().getSimpleName().equals(ItemInWorldManager.class.getSimpleName());
        else
            return false;
    }
    
    public RegistryNBTManager nbtManager()
    {
        if (nbtManager == null)
            nbtManager = new RegistryNBTManager();
        
        return nbtManager;
    }
}
