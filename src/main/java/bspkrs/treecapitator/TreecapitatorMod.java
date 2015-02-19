package bspkrs.treecapitator;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.treecapitator.config.TCConfigHandler;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.forge.ForgeEventHandler;
import bspkrs.treecapitator.registry.ModConfigRegistry;
import bspkrs.treecapitator.registry.RegistryNBTManager;
import bspkrs.treecapitator.registry.ThirdPartyModConfig;
import bspkrs.treecapitator.util.Reference;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = "@MOD_VERSION@", dependencies = "required-after:bspkrsCore@[@BSCORE_VERSION@,)",
        useMetadata = true, guiFactory = Reference.GUI_FACTORY)
public class TreecapitatorMod
{
    public static ModVersionChecker versionChecker;
    private final String            versionURL = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/treecapitator.version";
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
            if (msg.isNBTMessage() && msg.key.equals(Reference.EYE_NOTIFICATION))
            {
                // TODO: something with this message
            }
            else if (msg.isNBTMessage() /*&& msg.key.equals(Reference.THIRD_PARTY_MOD_CONFIG)*/)
            {
                TCLog.info("Received IMC message from mod %s.", msg.getSender());
                if (ThirdPartyModConfig.isValidNBT(msg.getNBTValue()))
                    ModConfigRegistry.instance().registerIMCModConfig(msg.getSender(), ThirdPartyModConfig.readFromNBT(msg.getNBTValue()));
                else
                    TCLog.severe("Validation failed for IMC message sent by %s", msg.getSender());

            }
            else
                TCLog.warning("Mod %s sent an IMC message, but it is not an NBT object message. The message will be ignored.", msg.getSender());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        ModConfigRegistry.instance().applyPrioritizedModConfigs();
        // Make sure the NBT manager is initialized while we can still be sure of the values in our local objects
        nbtManager();
    }

    public RegistryNBTManager nbtManager()
    {
        if (nbtManager == null)
            nbtManager = new RegistryNBTManager();

        return nbtManager;
    }
}
