package bspkrs.treecapitator.fml;

import java.util.EnumSet;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLeavesBase;
import net.minecraft.src.BlockVine;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import bspkrs.fml.util.Config;
import bspkrs.treecapitator.TreeBlockBreaker;
import bspkrs.treecapitator.TreeCapitator;
import bspkrs.util.CommonUtils;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;

@Mod(name = "TreeCapitator", modid = "TreeCapitator", version = "Forge 1.4.5.r01", useMetadata = true)
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class TreeCapitatorMod
{
    private static ModVersionChecker versionChecker;
    private final String             versionURL = "https://dl.dropbox.com/u/20748481/Minecraft/1.4.5/treeCapitatorForge.version";
    private final String             mcfTopic   = "http://www.minecraftforum.net/topic/1009577-";
    
    private HashMap                  leafClasses;
    private String                   idList     = "17;";
    private final static String      idListDesc = "Add the ID of log blocks (and optionally leaf blocks) that you want to be able to TreeCapitate. Format is \"<logID>[|<leafID>];\" ([] indicates optional elements). Example: 17|18; 209; 210; 211; 212; 213; 243|242;";
    
    @SideOnly(Side.CLIENT)
    public static Minecraft          mcClient;
    
    public ModMetadata               metadata;
    
    @Instance(value = "TreeCapitator")
    public static TreeCapitatorMod   instance;
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        TreeCapitator.init(true);
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
        idList = Config.getString(config, "logIDList", ctgyGen, idList, idListDesc);
        config.save();
    }
    
    @Init
    public void init(FMLInitializationEvent event)
    {
        if (event.getSide().equals(Side.CLIENT))
        {
            TickRegistry.registerTickHandler(new TreeCapitatorTicker(EnumSet.of(TickType.CLIENT)), Side.CLIENT);
            this.mcClient = FMLClientHandler.instance().getClient();
            MinecraftForge.EVENT_BUS.register(new PlayerHandler());
        }
    }
    
    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        /*
         * Handle parsing of blocks list...
         */
        leafClasses = new HashMap();
        
        if (idList.trim().length() > 0)
        {
            String[] groups = idList.trim().split(";");
            for (String group : groups)
            {
                if (group.trim().length() > 0)
                {
                    String[] ids = group.trim().split("\\|");
                    int logID = CommonUtils.parseInt(ids[0].trim());
                    int leafID = 18;
                    
                    if (ids.length > 1)
                        leafID = CommonUtils.parseInt(ids[1].trim());
                    
                    if (logID > 0)
                    {
                        Block log = Block.blocksList[logID];
                        if (log != null && !TreeCapitator.logClasses.contains(log.getClass()))
                        {
                            TreeCapitator.logClasses.add(log.getClass());
                            
                            Block leaf = Block.blocksList[leafID];
                            if (leaf != null)
                            {
                                if (leaf instanceof BlockLeavesBase)
                                    leafClasses.put(log.getClass(), BlockLeavesBase.class);
                                else
                                    leafClasses.put(log.getClass(), leaf.getClass());
                            }
                            else
                                leafClasses.put(log.getClass(), BlockLeavesBase.class);
                        }
                    }
                }
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public static boolean onTick(TickType tick, boolean isStart)
    {
        if (isStart)
        {
            return true;
        }
        
        if (mcClient != null && mcClient.thePlayer != null)
        {
            if (TreeCapitator.allowUpdateCheck)
                if (!versionChecker.isCurrentVersion())
                    for (String msg : versionChecker.getInGameMessage())
                        mcClient.thePlayer.addChatMessage(msg);
            return false;
        }
        
        return true;
    }
    
    public void onBlockHarvested(World world, int x, int y, int z, Block block, int metadata, EntityPlayer entityPlayer)
    {
        if (TreeCapitator.logClasses.contains(block.getClass()))
        {
            TreeBlockBreaker breaker = new TreeBlockBreaker(entityPlayer, block.blockID, block.getClass(), (Class<?>) leafClasses.get(block.getClass()), BlockVine.class);
            breaker.onBlockHarvested(world, x, y, z, metadata, entityPlayer);
        }
    }
}
