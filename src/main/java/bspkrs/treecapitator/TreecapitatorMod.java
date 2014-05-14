package bspkrs.treecapitator;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.treecapitator.compat.MultiMineCompat;
import bspkrs.treecapitator.config.TCConfigHandler;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.forge.ForgeEventHandler;
import bspkrs.treecapitator.registry.ModConfigRegistry;
import bspkrs.treecapitator.registry.RegistryNBTManager;
import bspkrs.treecapitator.registry.ThirdPartyModConfig;
import bspkrs.treecapitator.registry.TreeDefinition;
import bspkrs.treecapitator.registry.TreeRegistry;
import bspkrs.treecapitator.util.Reference;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.BlockID;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;
import bspkrs.util.Coord;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.common.FMLCommonHandler;
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

@Mod(modid = Reference.MODID, name = Reference.NAME, version = "@MOD_VERSION@", dependencies = "required-after:bspkrsCore@[@BSCORE_VERSION@,)",
        useMetadata = true, guiFactory = Reference.GUI_FACTORY)
public class TreecapitatorMod
{
    public static ModVersionChecker versionChecker;
    private final String            versionURL = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/treeCapitatorForge.version";
    private final String            mcfTopic   = "http://www.minecraftforum.net/topic/1009577-";
    
    private RegistryNBTManager      nbtManager;
    
    @Metadata(value = Reference.MODID)
    public static ModMetadata       metadata;
    
    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_COMMON)
    public static CommonProxy       proxy;
    
    @Instance(value = Reference.MODID)
    public static TreecapitatorMod  instance;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
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
        
        TCConfigHandler.setInstance(file);
        
        if (!CommonUtils.isObfuscatedEnv())
        {
            TCSettings.allowDebugLogging = true;
        }
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
        FMLCommonHandler.instance().bus().register(TCConfigHandler.instance());
        proxy.init(event);
        
        if (bspkrsCoreMod.instance.allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic);
            versionChecker.checkVersionWithLogging();
        }
    }
    
    @EventHandler
    public void processIMCMessages(IMCEvent event)
    {
        for (IMCMessage msg : event.getMessages().asList())
            if (msg.isNBTMessage())
            {
                TCLog.info("Received IMC message from mod %s.", msg.getSender());
                ModConfigRegistry.instance().registerIMCModConfig(msg.getSender(), new ThirdPartyModConfig(msg.getNBTValue()));
            }
            else
                TCLog.warning("Mod %s send an IMC message, but it is not an NBT object message. The message will be ignored.", msg.getSender());
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        ModConfigRegistry.instance().applyPrioritizedModConfigs();
        
        // Multi-Mine stuff
        if (Loader.isModLoaded(TCSettings.multiMineModID))
        {
            TCLog.info("Initializing MultiMine compatibility...");
            new MultiMineCompat(TreeRegistry.instance().getMultiMineExclusionString());
        }
        
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
    
    public RegistryNBTManager nbtManager()
    {
        if (nbtManager == null)
            nbtManager = new RegistryNBTManager();
        
        return nbtManager;
    }
}
