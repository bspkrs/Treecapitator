package bspkrs.treecapitator;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import bspkrs.util.BlockID;
import bspkrs.util.CommonUtils;

public final class TreeCapitator
{
    public final static String                         VERSION_NUMBER                 = "1.4.6.r04";
    public static final String                         LOGS                           = "logs";
    public static final String                         LEAVES                         = "leaves";
    public static final String                         MOD_ID                         = "modID";
    public static final String                         CONFIG_PATH                    = "configPath";
    public static final String                         BLOCK_VALUES                   = "blockValues";
    public static final String                         ITEM_VALUES                    = "itemValues";
    public static final String                         SHIFT_INDEX                    = "useShiftedItemID";
    public static final String                         IDR_MOD_ID                     = "idResolverModID";
    
    public final static String                         remoteTreeConfigURLDesc        = "Leave this URL as is to get the latest tree definitions from my master list.\nFeel free to start your own remote list to share with your friends or send your suggestions to me for the master list!";
    public static String                               remoteTreeConfigURL            = "http://dl.dropbox.com/u/20748481/Minecraft/1.4.6/treeCapitatorTreeConfig.txt";
    public final static String                         allowUpdateCheckDesc           = "Set to true to allow checking for mod updates, false to disable";
    public static boolean                              allowUpdateCheck               = true;
    public final static String                         remoteBlockIDConfigDesc        = "Values downloaded from: " + remoteTreeConfigURL;
    public static String                               remoteBlockIDConfig            = "";
    public final static String                         localBlockIDListDesc           = "Automatically generated:";
    public static String                               localBlockIDList               = "";
    public final static String                         useRemoteTreeConfigDesc        = "Set to true to use the remote block ID list (must also set allowGetOnlineTreeConfig to true), false to use local config.";
    public static boolean                              useRemoteTreeConfig            = false;
    public final static String                         allowGetRemoteTreeConfigDesc   = "Set to true to allow TreeCapitator to retrieve the remote block ID list, false to disable.";
    public static boolean                              allowGetRemoteTreeConfig       = false;
    
    public final static String                         axeIDListDesc                  = "IDs of items that can chop down trees. Use ',' to split item id from metadata and ';' to split items.";
    public static String                               axeIDList                      = Item.axeWood.itemID + "; " + Item.axeStone.itemID + "; " + Item.axeSteel.itemID + "; " + Item.axeGold.itemID + "; " + Item.axeDiamond.itemID;
    public final static String                         needItemDesc                   = "Whether you need an item from the axeIDList to chop down a tree. Disabling will let you chop trees with any item.";
    public static boolean                              needItem                       = true;
    public final static String                         onlyDestroyUpwardsDesc         = "Setting this to false will allow the chopping to move downward as well as upward (and blocks below the one you break will be chopped)";
    public static boolean                              onlyDestroyUpwards             = true;
    public final static String                         destroyLeavesDesc              = "Enabling this will make leaves be destroyed when trees are chopped.";
    public static boolean                              destroyLeaves                  = true;
    public final static String                         shearLeavesDesc                = "Enabling this will cause destroyed leaves to be sheared when a shearing item is in the hotbar (ignored if destroyLeaves is false).";
    public static boolean                              shearLeaves                    = false;
    public final static String                         shearVinesDesc                 = "Enabling this will shear /some/ of the vines on a tree when a shearing item is in the hotbar (ignored if destroyLeaves is false).";
    public static boolean                              shearVines                     = false;
    public final static String                         shearIDListDesc                = "IDs of items that when placed in the hotbar will allow leaves to be sheared when shearLeaves is true.\n"
                                                                                              + "Use ',' to split item id from metadata and ';' to split items.";
    public static String                               shearIDList                    = Item.shears.itemID + "";
    public final static String                         logHardnessNormalDesc          = "The hardness of logs for when you are using items that won't chop down the trees.";
    public static float                                logHardnessNormal              = 2.0F;
    public final static String                         logHardnessModifiedDesc        = "The hardness of logs for when you are using items that can chop down trees.";
    public static float                                logHardnessModified            = 4.0F;
    public final static String                         disableInCreativeDesc          = "Flag to disable tree chopping in Creative mode";
    public static boolean                              disableInCreative              = false;
    public final static String                         disableCreativeDropsDesc       = "Flag to disable drops in Creative mode";
    public static boolean                              disableCreativeDrops           = false;
    public final static String                         allowItemDamageDesc            = "Enable to cause item damage based on number of blocks destroyed";
    public static boolean                              allowItemDamage                = true;
    public final static String                         allowMoreBlocksThanDamageDesc  = "Enable to allow chopping down the entire tree even if your item does not have enough damage remaining to cover the number of blocks.";
    public static boolean                              allowMoreBlocksThanDamage      = false;
    public final static String                         damageMultiplierDesc           = "Axes and shears will take damage this many times for each log broken.\n" +
                                                                                              "Remaining damage is rounded and applied to tools when a tree is finished.";
    public static float                                damageMultiplier               = 1.0F;
    public final static String                         useIncreasingItemDamageDesc    = "Set to true to have the per-block item damage amount increase after every increaseDamageEveryXBlocks blocks are broken.";
    public static boolean                              useIncreasingItemDamage        = false;
    public final static String                         increaseDamageEveryXBlocksDesc = "When useIncreasingItemDamage=true the damage applied per block broken will increase each time this many blocks are broken in a tree.";
    public static int                                  increaseDamageEveryXBlocks     = 15;
    public final static String                         damageIncreaseAmountDesc       = "When useIncreasingItemDamage=true the damage applied per block broken will increase by this amount every increaseDamageEveryXBlocks blocks broken in a tree.";
    public static float                                damageIncreaseAmount           = 1.0F;
    public final static String                         sneakActionDesc                = "Set sneakAction = \"disable\" to disable tree chopping while sneaking,\n" +
                                                                                              "set sneakAction = \"enable\" to only enable tree chopping while sneaking,\n" +
                                                                                              "set sneakAction = \"none\" to have tree chopping enabled regardless of sneaking.";
    public static String                               sneakAction                    = "disable";
    public final static String                         maxBreakDistanceDesc           = "The maximum horizontal distance that the log breaking effect will travel (use -1 for no limit).";
    public static int                                  maxBreakDistance               = 16;
    public final static String                         allowSmartTreeDetectionDesc    = "Set to false to disable TreeCapitator Smart Tree Detection.\n" +
                                                                                              "Smart Tree Detection counts the number of leaf blocks that are adjacent to the\n" +
                                                                                              "top-most connected log block at the x, z location of a log you've broken. If\n" +
                                                                                              "there are at least minLeavesToID leaf blocks within maxLeafIDDist blocks then\n" +
                                                                                              "TreeCapitator considers it a tree and allows chopping.\n" +
                                                                                              "WARNING: Disabling Smart Tree Detection will remove the only safeguard against\n" +
                                                                                              "accidentally destroying a log structure.  Make sure you know what you're doing!";
    public static boolean                              allowSmartTreeDetection        = true;
    public final static String                         maxLeafIDDistDesc              = "If a tree's top log is not close enough to leaf blocks, the tree will not be chopped.\n" +
                                                                                              "Increasing this value will search further.  I would try to keep it below 3.";
    public static int                                  maxLeafIDDist                  = 1;
    public final static String                         maxLeafBreakDistDesc           = "The maximum distance to instantly decay leaves from any log block that is removed by TreeCapitator.";
    public static int                                  maxLeafBreakDist               = 4;
    public final static String                         minLeavesToIDDesc              = "The minimum number of leaves within maxLeafIDDist of the top log block required to identify a tree.";
    public static int                                  minLeavesToID                  = 3;
    public final static String                         useStrictBlockPairingDesc      = "Set to true if you want only the leaf blocks listed with each log in blockIDList\n"
                                                                                              + "to break when that log type is chopped.  When set to false it will break\n"
                                                                                              + "any leaf type within range of the tree, not just the type for that tree.";
    public static boolean                              useStrictBlockPairing          = false;
    
    public final static String                         allowDebugOutputDesc           = "Set to true if you want TreeCapitator to tell you what kind of block you have clicked when sneaking, false to disable.";
    public static boolean                              allowDebugOutput               = false;
    public final static String                         allowDebugLoggingDesc          = "Set to true if you want TreeCapitator to log info about what it's doing, false to disable";
    public static boolean                              allowDebugLogging              = true;
    
    public static boolean                              isForge                        = false;
    
    public static ArrayList<BlockID>                   logIDList                      = new ArrayList<BlockID>();
    public static ArrayList<BlockID>                   leafIDList                     = new ArrayList<BlockID>();
    public static Map<BlockID, ArrayList<BlockID>>     logToLeafListMap               = new HashMap<BlockID, ArrayList<BlockID>>();
    public static Map<BlockID, ArrayList<BlockID>>     logToLogListMap                = new HashMap<BlockID, ArrayList<BlockID>>();
    public static Map<String, HashMap<String, String>> configBlockList                = new HashMap<String, HashMap<String, String>>();
    public static Map<String, HashMap<String, String>> thirdPartyConfig               = new HashMap<String, HashMap<String, String>>();
    public static Map<String, String>                  tagMap                         = new HashMap<String, String>();
    
    public static Map<String, String>                  idResolverModIdMap             = new HashMap<String, String>();
    public static final String                         configBlockIDDesc              = "Add the log and leaf block IDs for all trees you want to be able to chop down.\n" +
                                                                                              "Each section below represents a type of tree.  Each list may contain block IDs\n" +
                                                                                              "and/or third-party config replacement tags. You can change it to be more or\n" +
                                                                                              "less granular as long as all sections follow the basic structure.  Do not use\n" +
                                                                                              "spaces or periods in your section names.  Otherwise you can call them anything\n" +
                                                                                              "you like.\n\n" +
                                                                                              "EACH LOG ID MAY ONLY APPEAR IN EXACTLY ONE SECTION.\n\n" +
                                                                                              "NOTE: Some mod trees use vanilla log blocks as well as custom blocks.  If a tree\n" +
                                                                                              "contains more than 1 type of log, all logs must be included in the same section.\n" +
                                                                                              "Examples of this are the default entries for vanilla_ebxl_oaks and vanilla_ebxl_spruces.\n\n" +
                                                                                              "Simple Example (all logs and leaves are grouped in one section, no metadata is specified):\n" +
                                                                                              "    trees {\n" +
                                                                                              "        S:leaves=18; <Forestry.leaves>; <ExtrabiomesXL.autumnleaves.id>; <ExtrabiomesXL.greenleaves.id>\n" +
                                                                                              "        S:logs=17; <Forestry.log1>; <Forestry.log2>; <Forestry.log3>; <Forestry.log4>; <ExtrabiomesXL.customlog.id>; <ExtrabiomesXL.quarterlog0.id>; <ExtrabiomesXL.quarterlog1.id>; <ExtrabiomesXL.quarterlog2.id>;<ExtrabiomesXL.quarterlog3.id>\n" +
                                                                                              "    }\n\n" +
                                                                                              "Advanced Example (each mod tree has its own section, metadata is included):\n" +
                                                                                              "    vanilla_ebxl_oaks {\n" +
                                                                                              "        S:leaves=18,0\n" +
                                                                                              "        S:logs=17,0; 17,4; 17,8; <ExtrabiomesXL.quarterlog0.id>,2; <ExtrabiomesXL.quarterlog1.id>,2; <ExtrabiomesXL.quarterlog2.id>,2;<ExtrabiomesXL.quarterlog3.id>,2;\n" +
                                                                                              "    }\n\n" +
                                                                                              "    birches {\n" +
                                                                                              "        S:leaves=18,2\n" +
                                                                                              "        S:logs=17,2; 17,6; 17,10\n" +
                                                                                              "    }\n\n" +
                                                                                              "    vanilla_ebxl_spruces {\n" +
                                                                                              "        S:leaves=18,1; <ExtrabiomesXL.autumnleaves.id>\n" +
                                                                                              "        S:logs=17,1; 17,5; 17,9\n" +
                                                                                              "    }\n\n" +
                                                                                              "    jungle_trees {\n" +
                                                                                              "        S:leaves=18,3\n" +
                                                                                              "        S:logs=17,3; 17,7; 17,11\n" +
                                                                                              "    }\n\n" +
                                                                                              "    ic2_rubber {\n" +
                                                                                              "        S:leaves=<IC2.blockRubLeaves>\n" +
                                                                                              "        S:logs=<IC2.blockRubWood>\n" +
                                                                                              "    }\n\n" +
                                                                                              "    ebxl_acacia {\n" +
                                                                                              "        S:leaves=<ExtrabiomesXL.greenleaves.id>,2\n" +
                                                                                              "        S:logs=<ExtrabiomesXL.customlog.id>,1\n" +
                                                                                              "    }\n\n" +
                                                                                              "    ebxl_firs {\n" +
                                                                                              "        S:leaves=<ExtrabiomesXL.greenleaves.id>,0\n" +
                                                                                              "        S:logs=<ExtrabiomesXL.customlog.id>,0; <ExtrabiomesXL.quarterlog0.id>,1; <ExtrabiomesXL.quarterlog1.id>,1; <ExtrabiomesXL.quarterlog2.id>,1; <ExtrabiomesXL.quarterlog3.id>,1\n" +
                                                                                              "    }\n\n" +
                                                                                              "    ebxl_redwoods {\n" +
                                                                                              "        S:leaves=<ExtrabiomesXL.greenleaves.id>,1\n" +
                                                                                              "        S:logs=<ExtrabiomesXL.quarterlog0.id>,0; <ExtrabiomesXL.quarterlog1.id>,0; <ExtrabiomesXL.quarterlog2.id>,0; <ExtrabiomesXL.quarterlog3.id>,0\n" +
                                                                                              "    }";
    
    public static final String                         thirdPartyConfigDesc           = "Third-Party config entries tell TreeCapitator how to find the block IDs from\n" +
                                                                                              "other mods' config files.  These values are case-sensitive!\n\n" +
                                                                                              "Format:\n" +
                                                                                              "    <section_name> {\n" +
                                                                                              "        S:modName=<modID from mcmod.info>\n" +
                                                                                              "        S:configPath=<path to config file relative to .minecraft/config/>\n" +
                                                                                              "        S:blockValues=<block config section>:<config property name>; <mod config section>:<config property name>\n" +
                                                                                              "        S:itemValues=<item config section>:<property name>; <item config section>:<property name>\n" +
                                                                                              "        B:useShiftedItemID=<whether or not to use the +256 shifted item ID> (true/false)\n" +
                                                                                              "    }\n\n" +
                                                                                              "Example:\n" +
                                                                                              "    extrabiomesxl {\n" +
                                                                                              "        S:modName=ExtrabiomesXL\n" +
                                                                                              "        S:configPath=extrabiomes/extrabiomes.cfg\n" +
                                                                                              "        S:blockValues=block:customlog.id; block:quarterlog0.id; block:quarterlog1.id; block:quarterlog2.id; block:quarterlog3.id; block:autumnleaves.id; block:greenleaves.id\n" +
                                                                                              "        S:itemValues=items.world:axeRuby.id; items.world:axeGreenSapphire.id; items.world:axeSapphire.id\n" +
                                                                                              "        B:useShiftedItemID=true\n" +
                                                                                              "    }\n\n" +
                                                                                              "Once your third-party config entries are setup, you can use replacement\n" +
                                                                                              "tags in your block, axe, and shears ID configs.  Replacement tags are structured like this:\n" +
                                                                                              "<ModName.ConfigPropName>";
    
    static
    {
        /*
         * Third-Party config defaults
         */
        HashMap<String, String> biomesoplenty = new HashMap<String, String>();
        biomesoplenty.put(MOD_ID, "BiomesOPlenty");
        biomesoplenty.put(CONFIG_PATH, "BiomesOPlenty.cfg");
        biomesoplenty.put(IDR_MOD_ID, "tdwp_ftw.biomesop.mod_BiomesOPlenty");
        idResolverModIdMap.put(biomesoplenty.get(MOD_ID), biomesoplenty.get(IDR_MOD_ID));
        biomesoplenty.put(BLOCK_VALUES, "terrainblock:BlockAcaciaLeaves; terrainblock:BlockAcaciaLog; terrainblock:BlockAppleLeaves; " +
                "terrainblock:BlockAppleLeavesFruitless; terrainblock:BlockAutumnLeaves; terrainblock:BlockBamboo; terrainblock:BlockBambooLeaves; " +
                "terrainblock:BlockBlueLeaves; terrainblock:BlockCherryLog; terrainblock:BlockDarkLeaves; terrainblock:BlockDarkLog; " +
                "terrainblock:BlockDeadLeaves; terrainblock:BlockDeadLog; terrainblock:BlockFirLeaves; terrainblock:BlockFirLog; " +
                "terrainblock:BlockMagicLog; terrainblock:BlockOrangeLeaves; terrainblock:BlockOriginLeaves; terrainblock:BlockPinkLeaves; " +
                "terrainblock:BlockRedLeaves; terrainblock:BlockRedwoodLeaves; terrainblock:BlockRedwoodLog; terrainblock:BlockWhiteLeaves; " +
                "terrainblock:BlockWillowLeaves; terrainblock:BlockWillowLog");
        thirdPartyConfig.put("biomesoplenty", biomesoplenty);
        
        HashMap<String, String> extrabiomesxl = new HashMap<String, String>();
        extrabiomesxl.put(MOD_ID, "ExtrabiomesXL");
        extrabiomesxl.put(CONFIG_PATH, "extrabiomes/extrabiomes.cfg");
        extrabiomesxl.put(IDR_MOD_ID, "extrabiomes.Extrabiomes");
        idResolverModIdMap.put(extrabiomesxl.get(MOD_ID), extrabiomesxl.get(IDR_MOD_ID));
        extrabiomesxl.put(BLOCK_VALUES, "block:customlog.id; block:quarterlog0.id; block:quarterlog1.id; block:quarterlog2.id; block:quarterlog3.id; " +
                "block:autumnleaves.id; block:greenleaves.id");
        thirdPartyConfig.put("extrabiomesxl", extrabiomesxl);
        
        HashMap<String, String> forestry = new HashMap<String, String>();
        forestry.put(MOD_ID, "Forestry");
        forestry.put(CONFIG_PATH, "forestry/base.conf");
        forestry.put(IDR_MOD_ID, "forestry.Forestry");
        idResolverModIdMap.put(forestry.get(MOD_ID), forestry.get(IDR_MOD_ID));
        forestry.put(BLOCK_VALUES, "block:log1; block:log2; block:log3; block:log4; block:leaves");
        thirdPartyConfig.put("forestry", forestry);
        
        HashMap<String, String> ic2 = new HashMap<String, String>();
        ic2.put(MOD_ID, "IC2");
        ic2.put(CONFIG_PATH, "IC2.cfg");
        ic2.put(IDR_MOD_ID, "ic2.core.IC2");
        idResolverModIdMap.put(ic2.get(MOD_ID), ic2.get(IDR_MOD_ID));
        ic2.put(BLOCK_VALUES, "block:blockRubWood; block:blockRubLeaves");
        ic2.put(ITEM_VALUES, "item:itemToolBronzeAxe; item:itemToolChainsaw");
        ic2.put(SHIFT_INDEX, "true");
        thirdPartyConfig.put("ic2", ic2);
        
        HashMap<String, String> inficraft = new HashMap<String, String>();
        inficraft.put(MOD_ID, "Flora Trees");
        inficraft.put(CONFIG_PATH, "InfiCraft/FloraTrees.txt");
        inficraft.put(IDR_MOD_ID, "florasoma.trees.FloraTrees");
        idResolverModIdMap.put(inficraft.get(MOD_ID), inficraft.get(IDR_MOD_ID));
        inficraft.put(BLOCK_VALUES, "block:Bloodwood Block; block:Flora Leaves; block:Redwood Block; block:Sakura Leaves; block:Wood Block");
        thirdPartyConfig.put("inficraft", inficraft);
        
        HashMap<String, String> mfreloaded = new HashMap<String, String>();
        mfreloaded.put(MOD_ID, "MFReloaded");
        mfreloaded.put(CONFIG_PATH, "MFReloaded.cfg");
        mfreloaded.put(IDR_MOD_ID, "powercrystals.minefactoryreloaded.MineFactoryReloadedCore");
        idResolverModIdMap.put(mfreloaded.get(MOD_ID), mfreloaded.get(IDR_MOD_ID));
        mfreloaded.put(BLOCK_VALUES, "block:ID.RubberWood; block:ID.RubberLeaves");
        thirdPartyConfig.put("mfreloaded", mfreloaded);
        
        HashMap<String, String> redpower = new HashMap<String, String>();
        redpower.put(MOD_ID, "RedPowerWorld");
        redpower.put(CONFIG_PATH, "redpower/redpower.cfg");
        redpower.put(IDR_MOD_ID, "com.eloraam.redpower.RedPowerWorld");
        idResolverModIdMap.put(redpower.get(MOD_ID), redpower.get(IDR_MOD_ID));
        redpower.put(BLOCK_VALUES, "blocks.world:log.id; blocks.world:leaves.id");
        redpower.put(ITEM_VALUES, "items.world:axeRuby.id; items.world:axeGreenSapphire.id; items.world:axeSapphire.id");
        redpower.put(SHIFT_INDEX, "true");
        thirdPartyConfig.put("redpower", redpower);
        
        HashMap<String, String> thaumcraft = new HashMap<String, String>();
        thaumcraft.put(MOD_ID, "Thaumcraft");
        thaumcraft.put(CONFIG_PATH, "Thaumcraft.cfg");
        thaumcraft.put(IDR_MOD_ID, "thaumcraft.common.Thaumcraft");
        idResolverModIdMap.put(thaumcraft.get(MOD_ID), thaumcraft.get(IDR_MOD_ID));
        thaumcraft.put(BLOCK_VALUES, "block:BlockMagicalLog; block:BlockMagicalLeaves");
        thaumcraft.put(ITEM_VALUES, "item:Thaumaxe");
        thaumcraft.put(SHIFT_INDEX, "true");
        thirdPartyConfig.put("thaumcraft", thaumcraft);
        
        HashMap<String, String> twilightforest = new HashMap<String, String>();
        twilightforest.put(MOD_ID, "TwilightForest");
        twilightforest.put(CONFIG_PATH, "TwilightForest.cfg");
        twilightforest.put(IDR_MOD_ID, "twilightforest.TwilightForestMod");
        idResolverModIdMap.put(twilightforest.get(MOD_ID), twilightforest.get(IDR_MOD_ID));
        twilightforest.put(BLOCK_VALUES, "block:Log; block:MagicLog; block:MagicLogSpecial; block:Leaves; block:MagicLeaves; block:Hedge");
        twilightforest.put(ITEM_VALUES, "item:IronwoodAxe; item:SteeleafAxe; item:MinotaurAxe");
        twilightforest.put(SHIFT_INDEX, "true");
        thirdPartyConfig.put("twilightforest", twilightforest);
        
        HashMap<String, String> zapapples = new HashMap<String, String>();
        zapapples.put(MOD_ID, "ZapApples");
        zapapples.put(CONFIG_PATH, "ZapApples.cfg");
        zapapples.put(IDR_MOD_ID, "com.jsn_man.ZapApples.ZapApples");
        idResolverModIdMap.put(zapapples.get(MOD_ID), zapapples.get(IDR_MOD_ID));
        zapapples.put(BLOCK_VALUES, "block:zapAppleLogID; block:zapAppleLeavesID; block:zapAppleFlowersID");
        thirdPartyConfig.put("zapapples", zapapples);
        
        axeIDList = axeIDList + "; " +
                "<IC2.itemToolBronzeAxe>; <IC2.itemToolChainsaw>; " +
                "<RedPowerWorld.axeRuby.id>; <RedPowerWorld.axeGreenSapphire.id>; <RedPowerWorld.axeSapphire.id>; " +
                "<Thaumcraft.Thaumaxe>; " +
                "<TwilightForest.IronwoodAxe>; <TwilightForest.SteeleafAxe>; <TwilightForest.MinotaurAxe>; ";
        
        /*
         * Default local config
         */
        HashMap<String, String> vanilla_ebxl_oaks = new HashMap<String, String>();
        vanilla_ebxl_oaks.put(LOGS, "17,0; 17,4; 17,8; <ExtrabiomesXL.quarterlog0.id>,2; <ExtrabiomesXL.quarterlog1.id>,2; " +
                "<ExtrabiomesXL.quarterlog2.id>,2; <ExtrabiomesXL.quarterlog3.id>,2;");
        vanilla_ebxl_oaks.put(LEAVES, "18,0; <ExtrabiomesXL.autumnleaves.id>; <BiomesOPlenty.BlockDeadLeaves>");
        configBlockList.put("vanilla_ebxl_oaks", vanilla_ebxl_oaks);
        
        HashMap<String, String> vanilla_ebxl_spruces = new HashMap<String, String>();
        vanilla_ebxl_spruces.put(LOGS, "17,1; 17,5; 17,9");
        vanilla_ebxl_spruces.put(LEAVES, "18,1; <ExtrabiomesXL.autumnleaves.id>");
        configBlockList.put("vanilla_ebxl_spruces", vanilla_ebxl_spruces);
        
        HashMap<String, String> birches = new HashMap<String, String>();
        birches.put(LOGS, "17,2; 17,6; 17,10");
        birches.put(LEAVES, "18,2");
        configBlockList.put("birches", birches);
        
        HashMap<String, String> jungle_trees = new HashMap<String, String>();
        jungle_trees.put(LOGS, "17,3; 17,7; 17,11");
        jungle_trees.put(LEAVES, "18,3");
        configBlockList.put("jungle_trees", jungle_trees);
        
        /*
         * biomesoplenty.put(MOD_NAME, "BiomesOPlenty"); biomesoplenty.put(CONFIG_PATH, "BiomesOPlenty.cfg");
         * biomesoplenty.put(BLOCK_VALUES, "terrainblock:BlockAcaciaLeaves; terrainblock:BlockAcaciaLog; terrainblock:BlockAppleLeaves; " +
         * "terrainblock:BlockAppleLeavesFruitless; terrainblock:BlockAutumnLeaves; terrainblock:BlockBamboo; terrainblock:BlockBambooLeaves; "
         * + "terrainblock:BlockBlueLeaves; terrainblock:BlockCherryLog; terrainblock:BlockDarkLeaves; terrainblock:BlockDarkLog; " +
         * "terrainblock:BlockDeadLeaves; terrainblock:BlockDeadLog; terrainblock:BlockFirLeaves; terrainblock:BlockFirLog; " +
         * "terrainblock:BlockMagicLog; terrainblock:BlockOrangeLeaves; terrainblock:BlockOriginLeaves; terrainblock:BlockPinkLeaves; " +
         * "terrainblock:BlockRedLeaves; terrainblock:BlockRedwoodLeaves; terrainblock:BlockRedwoodLog; terrainblock:BlockWhiteLeaves; " +
         * "terrainblock:BlockWillowLeaves; terrainblock:BlockWillowLog");
         */
        
        HashMap<String, String> biomesoplenty_acacia = new HashMap<String, String>();
        biomesoplenty_acacia.put(LOGS, "<BiomesOPlenty.BlockAcaciaLog>");
        biomesoplenty_acacia.put(LEAVES, "<BiomesOPlenty.BlockAcaciaLeaves>");
        configBlockList.put("biomesoplenty_acacia", biomesoplenty_acacia);
        
        HashMap<String, String> biomesoplenty_cherry = new HashMap<String, String>();
        biomesoplenty_cherry.put(LOGS, "<BiomesOPlenty.BlockCherryLog>");
        biomesoplenty_cherry.put(LEAVES, "<BiomesOPlenty.BlockPinkLeaves>");
        configBlockList.put("biomesoplenty_cherry", biomesoplenty_cherry);
        
        HashMap<String, String> biomesoplenty_dark = new HashMap<String, String>();
        biomesoplenty_dark.put(LOGS, "<BiomesOPlenty.BlockDarkLog>");
        biomesoplenty_dark.put(LEAVES, "<BiomesOPlenty.BlockDarkLeaves>; <BiomesOPlenty.BlockWhiteLeaves>");
        configBlockList.put("biomesoplenty_dark", biomesoplenty_dark);
        
        HashMap<String, String> biomesoplenty_fir = new HashMap<String, String>();
        biomesoplenty_fir.put(LOGS, "<BiomesOPlenty.BlockFirLog>");
        biomesoplenty_fir.put(LEAVES, "<BiomesOPlenty.BlockFirLeaves>");
        configBlockList.put("biomesoplenty_fir", biomesoplenty_fir);
        
        /*
         * not sure on this one... biomesoplenty_magic.put(LOGS, "<BiomesOPlenty.BlockMagicLog>"); biomesoplenty_magic.put(LEAVES,
         * "<BiomesOPlenty.BlockAcaciaLeaves>"); configBlockList.put("biomesoplenty_magic", biomesoplenty_magic);
         */
        
        HashMap<String, String> biomesoplenty_redwood = new HashMap<String, String>();
        biomesoplenty_redwood.put(LOGS, "<BiomesOPlenty.BlockRedwoodLog>");
        biomesoplenty_redwood.put(LEAVES, "<BiomesOPlenty.BlockRedwoodLeaves>");
        configBlockList.put("biomesoplenty_redwood", biomesoplenty_redwood);
        
        HashMap<String, String> biomesoplenty_willow = new HashMap<String, String>();
        biomesoplenty_willow.put(LOGS, "<BiomesOPlenty.BlockWillowLog>");
        biomesoplenty_willow.put(LEAVES, "<BiomesOPlenty.BlockWillowLeaves>");
        configBlockList.put("biomesoplenty_willow", biomesoplenty_willow);
        
        HashMap<String, String> ebxl_redwoods = new HashMap<String, String>();
        ebxl_redwoods.put(LOGS, "<ExtrabiomesXL.quarterlog0.id>,0; <ExtrabiomesXL.quarterlog1.id>,0; <ExtrabiomesXL.quarterlog2.id>,0; " +
                "<ExtrabiomesXL.quarterlog3.id>,0");
        ebxl_redwoods.put(LEAVES, "<ExtrabiomesXL.greenleaves.id>,1");
        configBlockList.put("ebxl_redwoods", ebxl_redwoods);
        
        HashMap<String, String> ebxl_firs = new HashMap<String, String>();
        ebxl_firs.put(LOGS, "<ExtrabiomesXL.customlog.id>,0; <ExtrabiomesXL.quarterlog0.id>,1; <ExtrabiomesXL.quarterlog1.id>,1; " +
                "<ExtrabiomesXL.quarterlog2.id>,1; <ExtrabiomesXL.quarterlog3.id>,1");
        ebxl_firs.put(LEAVES, "<ExtrabiomesXL.greenleaves.id>,0");
        configBlockList.put("ebxl_firs", ebxl_firs);
        
        HashMap<String, String> ebxl_acacia = new HashMap<String, String>();
        ebxl_acacia.put(LOGS, "<ExtrabiomesXL.customlog.id>,1");
        ebxl_acacia.put(LEAVES, "<ExtrabiomesXL.greenleaves.id>,2");
        configBlockList.put("ebxl_acacia", ebxl_acacia);
        
        HashMap<String, String> forestry_larch = new HashMap<String, String>();
        forestry_larch.put(LOGS, "<Forestry.log1>,0; <Forestry.log1>,4; <Forestry.log1>,8");
        forestry_larch.put(LEAVES, "<Forestry.leaves>,0; <Forestry.leaves>,8");
        configBlockList.put("forestry_larch", forestry_larch);
        
        HashMap<String, String> forestry_teak = new HashMap<String, String>();
        forestry_teak.put(LOGS, "<Forestry.log1>,1; <Forestry.log1>,5; <Forestry.log1>,9");
        forestry_teak.put(LEAVES, "<Forestry.leaves>,0; <Forestry.leaves>,8");
        configBlockList.put("forestry_teak", forestry_teak);
        
        HashMap<String, String> forestry_acacia = new HashMap<String, String>();
        forestry_acacia.put(LOGS, "<Forestry.log1>,2; <Forestry.log1>,6; <Forestry.log1>,10");
        forestry_acacia.put(LEAVES, "<Forestry.leaves>,0; <Forestry.leaves>,8");
        configBlockList.put("forestry_acacia", forestry_acacia);
        
        HashMap<String, String> forestry_lime = new HashMap<String, String>();
        forestry_lime.put(LOGS, "<Forestry.log1>,3; <Forestry.log1>,7; <Forestry.log1>,11");
        forestry_lime.put(LEAVES, "<Forestry.leaves>,0; <Forestry.leaves>,8");
        configBlockList.put("forestry_lime", forestry_lime);
        
        HashMap<String, String> forestry_chestnut = new HashMap<String, String>();
        forestry_chestnut.put(LOGS, "<Forestry.log2>,0; <Forestry.log2>,4; <Forestry.log2>,8");
        forestry_chestnut.put(LEAVES, "<Forestry.leaves>,0; <Forestry.leaves>,8");
        configBlockList.put("forestry_chestnut", forestry_chestnut);
        
        HashMap<String, String> forestry_wenge = new HashMap<String, String>();
        forestry_wenge.put(LOGS, "<Forestry.log2>,1; <Forestry.log2>,5; <Forestry.log2>,9");
        forestry_wenge.put(LEAVES, "<Forestry.leaves>,0; <Forestry.leaves>,8");
        configBlockList.put("forestry_wenge", forestry_wenge);
        
        HashMap<String, String> forestry_baobab = new HashMap<String, String>();
        forestry_baobab.put(LOGS, "<Forestry.log2>,2; <Forestry.log2>,6; <Forestry.log2>,10");
        forestry_baobab.put(LEAVES, "<Forestry.leaves>,0; <Forestry.leaves>,8");
        configBlockList.put("forestry_baobab", forestry_baobab);
        
        HashMap<String, String> forestry_sequoia = new HashMap<String, String>();
        forestry_sequoia.put(LOGS, "<Forestry.log2>,3; <Forestry.log2>,7; <Forestry.log2>,11");
        forestry_sequoia.put(LEAVES, "<Forestry.leaves>,0; <Forestry.leaves>,8");
        configBlockList.put("forestry_sequoia", forestry_sequoia);
        
        HashMap<String, String> forestry_kapok = new HashMap<String, String>();
        forestry_kapok.put(LOGS, "<Forestry.log3>,0; <Forestry.log3>,4; <Forestry.log3>,8");
        forestry_kapok.put(LEAVES, "<Forestry.leaves>,0; <Forestry.leaves>,8");
        configBlockList.put("forestry_kapok", forestry_kapok);
        
        HashMap<String, String> forestry_ebony = new HashMap<String, String>();
        forestry_ebony.put(LOGS, "<Forestry.log3>,1; <Forestry.log3>,5; <Forestry.log3>,9");
        forestry_ebony.put(LEAVES, "<Forestry.leaves>,0; <Forestry.leaves>,8");
        configBlockList.put("forestry_ebony", forestry_ebony);
        
        HashMap<String, String> forestry_mahogany = new HashMap<String, String>();
        forestry_mahogany.put(LOGS, "<Forestry.log3>,2; <Forestry.log3>,6; <Forestry.log3>,10");
        forestry_mahogany.put(LEAVES, "<Forestry.leaves>,0; <Forestry.leaves>,8");
        configBlockList.put("forestry_mahogany", forestry_mahogany);
        
        HashMap<String, String> forestry_balsa = new HashMap<String, String>();
        forestry_balsa.put(LOGS, "<Forestry.log3>,3; <Forestry.log3>,7; <Forestry.log3>,11");
        forestry_balsa.put(LEAVES, "<Forestry.leaves>,0; <Forestry.leaves>,8");
        configBlockList.put("forestry_balsa", forestry_balsa);
        
        HashMap<String, String> forestry_palm = new HashMap<String, String>();
        forestry_palm.put(LOGS, "<Forestry.log4>,0; <Forestry.log4>,4; <Forestry.log4>,8");
        forestry_palm.put(LEAVES, "<Forestry.leaves>,0; <Forestry.leaves>,8");
        configBlockList.put("forestry_palm", forestry_palm);
        
        HashMap<String, String> forestry_walnut = new HashMap<String, String>();
        forestry_walnut.put(LOGS, "<Forestry.log4>,1; <Forestry.log4>,5; <Forestry.log4>,9");
        forestry_walnut.put(LEAVES, "<Forestry.leaves>,0; <Forestry.leaves>,8");
        configBlockList.put("forestry_walnut", forestry_walnut);
        
        HashMap<String, String> forestry_boojum = new HashMap<String, String>();
        forestry_boojum.put(LOGS, "<Forestry.log4>,2; <Forestry.log4>,6; <Forestry.log4>,10");
        forestry_boojum.put(LEAVES, "<Forestry.leaves>,0; <Forestry.leaves>,8");
        configBlockList.put("forestry_boojum", forestry_boojum);
        
        HashMap<String, String> forestry_cherry = new HashMap<String, String>();
        forestry_cherry.put(LOGS, "<Forestry.log4>,3; <Forestry.log4>,7; <Forestry.log4>,11");
        forestry_cherry.put(LEAVES, "<Forestry.leaves>,0; <Forestry.leaves>,8");
        configBlockList.put("forestry_cherry", forestry_cherry);
        
        HashMap<String, String> ic2_rubber = new HashMap<String, String>();
        ic2_rubber.put(LOGS, "<IC2.blockRubWood>");
        ic2_rubber.put(LEAVES, "<IC2.blockRubLeaves>");
        configBlockList.put("ic2_rubber", ic2_rubber);
        
        HashMap<String, String> inficraft_tree = new HashMap<String, String>();
        inficraft_tree.put(LOGS, "<Flora Trees.Wood Block>");
        inficraft_tree.put(LEAVES, "<Flora Trees.Flora Leaves>");
        configBlockList.put("inficraft_tree", inficraft_tree);
        
        HashMap<String, String> mfr_rubber = new HashMap<String, String>();
        mfr_rubber.put(LOGS, "<MFReloaded.ID.RubberWood>");
        mfr_rubber.put(LEAVES, "<MFReloaded.ID.RubberSapling>");
        configBlockList.put("mfr_rubber", mfr_rubber);
        
        HashMap<String, String> rp2_rubber = new HashMap<String, String>();
        rp2_rubber.put(LOGS, "<RedPowerWorld.log.id>");
        rp2_rubber.put(LEAVES, "<RedPowerWorld.leaves.id>");
        configBlockList.put("rp2_rubber", rp2_rubber);
        
        HashMap<String, String> thaum_greatwood = new HashMap<String, String>();
        thaum_greatwood.put(LOGS, "<Thaumcraft.BlockMagicalLog>,0; <Thaumcraft.BlockMagicalLog>,4; <Thaumcraft.BlockMagicalLog>,8");
        thaum_greatwood.put(LEAVES, "<Thaumcraft.BlockMagicalLeaves>,0; <Thaumcraft.BlockMagicalLeaves>,8");
        configBlockList.put("thaum_greatwood", thaum_greatwood);
        
        HashMap<String, String> thaum_silverwood = new HashMap<String, String>();
        thaum_silverwood.put(LOGS, "<Thaumcraft.BlockMagicalLog>,1; <Thaumcraft.BlockMagicalLog>,5; <Thaumcraft.BlockMagicalLog>,9");
        thaum_silverwood.put(LEAVES, "<Thaumcraft.BlockMagicalLeaves>,1");
        configBlockList.put("thaum_silverwood", thaum_silverwood);
        
        HashMap<String, String> twilight_oaks = new HashMap<String, String>();
        twilight_oaks.put(LOGS, "<TwilightForest.Log>,0; <TwilightForest.Log>,4; <TwilightForest.Log>,8; <TwilightForest.Log>,12");
        twilight_oaks.put(LEAVES, "<TwilightForest.Leaves>,0; <TwilightForest.Leaves>,3; <TwilightForest.Leaves>,8; <TwilightForest.Leaves>,11");
        configBlockList.put("twilight_oaks", twilight_oaks);
        
        HashMap<String, String> twilight_canopy = new HashMap<String, String>();
        twilight_canopy.put(LOGS, "<TwilightForest.Log>,1; <TwilightForest.Log>,5; <TwilightForest.Log>,9; <TwilightForest.Log>,13");
        twilight_canopy.put(LEAVES, "<TwilightForest.Leaves>, 1; <TwilightForest.Leaves>,9");
        configBlockList.put("twilight_canopy", twilight_canopy);
        
        HashMap<String, String> twilight_mangrove = new HashMap<String, String>();
        twilight_mangrove.put(LOGS, "<TwilightForest.Log>,2; <TwilightForest.Log>,6; <TwilightForest.Log>,10; <TwilightForest.Log>,14");
        twilight_mangrove.put(LEAVES, "<TwilightForest.Leaves>, 1; <TwilightForest.Leaves>,9");
        configBlockList.put("twilight_mangrove", twilight_mangrove);
        
        HashMap<String, String> twilight_darkwood = new HashMap<String, String>();
        twilight_darkwood.put(LOGS, "<TwilightForest.Log>,3; <TwilightForest.Log>,7; <TwilightForest.Log>,11;  <TwilightForest.Log>,15");
        twilight_darkwood.put(LEAVES, "<TwilightForest.Hedge>,1");
        configBlockList.put("twilight_darkwood", twilight_darkwood);
        
        HashMap<String, String> twilight_time = new HashMap<String, String>();
        twilight_time.put(LOGS, "<TwilightForest.MagicLog>,0; <TwilightForest.Log>,4; <TwilightForest.Log>,8; <TwilightForest.MagicLog>,12");
        twilight_time.put(LEAVES, "<TwilightForest.MagicLeaves>,0; <TwilightForest.MagicLeaves>,8");
        configBlockList.put("twilight_time", twilight_time);
        
        HashMap<String, String> zapapple = new HashMap<String, String>();
        zapapple.put(LOGS, "<ZapApples.zapAppleLogID>");
        zapapple.put(LEAVES, "<ZapApples.zapAppleLeavesID>; <ZapApples.zapAppleFlowersID>");
        configBlockList.put("zapapple", zapapple);
        
        // localTreeConfig = getConfigBlockListString();
    }
    
    public static void debugString(String msg, Object... args)
    {
        if (allowDebugLogging)
            TCLog.info("[DEBUG] " + msg, args);
    }
    
    public static String replaceThirdPartyBlockTags(String input)
    {
        for (String tag : tagMap.keySet())
            input = input.replace(tag, tagMap.get(tag));
        
        return input;
    }
    
    public static String getRemoteConfig()
    {
        if (isForge && allowGetRemoteTreeConfig)
        {
            try
            {
                return CommonUtils.loadTextFromURL(new URL(remoteTreeConfigURL), TCLog.INSTANCE.getLogger(), TreeCapitator.remoteBlockIDConfig)[0];
            }
            catch (Throwable e)
            {
                TCLog.warning("Error retrieving remote tree config! Defaulting to cached copy if available or local config.");
            }
        }
        return TreeCapitator.remoteBlockIDConfig;
    }
    
    public static void init()
    {
        init(false);
    }
    
    public static void init(boolean isForgeVersion)
    {
        isForge = isForgeVersion;
        
        if (!isForge)
        {
            Block.blocksList[Block.wood.blockID] = null;
            Block.blocksList[Block.wood.blockID] = new BlockTree(Block.wood.blockID);
        }
    }
    
    public static boolean isLogBlock(BlockID blockID)
    {
        return TreeCapitator.logIDList.contains(blockID);
    }
    
    public static String getStringFromConfigBlockList()
    {
        String list = "";
        for (HashMap<String, String> group : configBlockList.values())
            list += " ! " + group.get(LOGS) + (group.containsKey(LEAVES) ? "|" + group.get(LEAVES) : "");
        return replaceThirdPartyBlockTags(list.replaceFirst(" ! ", ""));
    }
    
    public static String getStringFromParsedLists()
    {
        String list = "";
        List<ArrayList<BlockID>> processed = new ArrayList<ArrayList<BlockID>>();
        
        for (BlockID key : logIDList)
        {
            String logPart = "";
            
            if (!processed.contains(logToLogListMap.get(key)))
            {
                processed.add(logToLogListMap.get(key));
                
                for (BlockID logID : logToLogListMap.get(key))
                    logPart += "; " + logID.id + (logID.metadata != -1 ? "," + logID.metadata : "");
                logPart = logPart.replaceFirst("; ", "");
                
                if (logPart.trim().length() > 0)
                {
                    String leafPart = "";
                    
                    for (BlockID leafID : logToLeafListMap.get(key))
                        leafPart += "; " + leafID.id + (leafID.metadata != -1 ? "," + leafID.metadata : "");
                    leafPart = leafPart.replaceFirst("; ", "");
                    
                    list += " ! " + logPart + " | " + leafPart;
                }
            }
        }
        return list.replaceFirst(" ! ", "");
    }
    
    public static void parseConfigBlockList(String list)
    {
        logIDList = new ArrayList<BlockID>();
        leafIDList = new ArrayList<BlockID>();
        logToLogListMap = new HashMap<BlockID, ArrayList<BlockID>>();
        logToLeafListMap = new HashMap<BlockID, ArrayList<BlockID>>();
        
        TCLog.info("Parsing Tree Block Config string: %s", list);
        
        if (list.trim().length() > 0)
        {
            String[] entries = list.trim().split("!");
            for (String entry : entries)
            {
                if (entry.trim().length() > 0)
                {
                    TreeCapitator.debugString("  Parsing Tree entry: %s", entry);
                    if (entry.trim().length() > 0)
                    {
                        String[] blockTypes = entry.trim().split("\\|");
                        
                        // parse log ids [0]
                        ArrayList<BlockID> logIDs = new ArrayList<BlockID>();
                        String[] logBlocks = blockTypes[0].trim().split(";");
                        
                        TreeCapitator.debugString("    Found log ID list: %s", blockTypes[0].trim());
                        
                        for (String logBlockStr : logBlocks)
                        {
                            String[] logBlock = logBlockStr.trim().split(",");
                            
                            TreeCapitator.debugString("    Found log ID: %s", logBlockStr);
                            int blockID = CommonUtils.parseInt(logBlock[0].trim(), -1);
                            
                            if (blockID != -1)
                            {
                                int metadata = -1;
                                
                                if (logBlock.length > 1)
                                    metadata = CommonUtils.parseInt(logBlock[1].trim(), -1);
                                TCLog.info("    ++Configured log: %s, %s", blockID, metadata);
                                
                                BlockID logID = new BlockID(blockID, metadata);
                                if (!logIDList.contains(logID))
                                {
                                    logIDList.add(logID);
                                    logIDs.add(logID);
                                }
                            }
                            else
                                TreeCapitator.debugString("Block ID %s could not be parsed as an integer.  Ignoring entry.", logBlock[0].trim());
                        }
                        
                        for (BlockID logID : logIDs)
                            logToLogListMap.put(logID, logIDs);
                        
                        // parse leaf ids [1]
                        ArrayList<BlockID> pairedLeaves = new ArrayList<BlockID>();
                        String[] leafBlocks = blockTypes[1].trim().split(";");
                        
                        TreeCapitator.debugString("    Found leaf ID list: %s", blockTypes[1].trim());
                        
                        for (String block : leafBlocks)
                        {
                            if (block.trim().length() > 0)
                            {
                                TreeCapitator.debugString("    Found leaf ID: %s", block.trim());
                                String[] leafBlock = block.trim().split(",");
                                int blockID = CommonUtils.parseInt(leafBlock[0].trim(), -1);
                                
                                if (blockID != -1)
                                {
                                    int metadata = -1;
                                    
                                    if (leafBlock.length > 1)
                                        metadata = CommonUtils.parseInt(leafBlock[1].trim(), -1);
                                    
                                    TCLog.info("    ++Configured leaf: %s, %s", blockID, metadata);
                                    
                                    BlockID leafID = new BlockID(blockID, metadata);
                                    if (!leafIDList.contains(leafID))
                                        leafIDList.add(leafID);
                                    
                                    if (!pairedLeaves.contains(leafID))
                                        pairedLeaves.add(leafID);
                                }
                                else
                                    TreeCapitator.debugString("Block ID %s could not be parsed as an integer.  Ignoring entry.", leafBlock[0].trim());
                            }
                        }
                        
                        for (BlockID logID : logIDs)
                            if (!logToLeafListMap.containsKey(logID))
                                logToLeafListMap.put(logID, pairedLeaves);
                    }
                }
            }
        }
        TCLog.info("Block ID list parsing complete.");
    }
    /*
     * public static void parseBlockIDList(String list, ArrayList<BlockID> blockList) { TCLog.info("Parsing TreeCapitator Block ID list: " +
     * list); if (list.trim().length() > 0) { String[] blocks = list.trim().split(";"); for (String block : blocks) { if
     * (block.trim().length() > 0) { String[] blockEntry = block.trim().split(","); TCLog.info("Found Block entry: " + block); int blockID =
     * CommonUtils.parseInt(blockEntry[0].trim()); int metadata = -1; if (blockEntry.length > 1) metadata =
     * CommonUtils.parseInt(blockEntry[1].trim(), -1); TCLog.info("Interpretted: " + blockID + ", " + metadata); if (blockID > 0) { Block
     * testBlock = Block.blocksList[blockID]; if (testBlock != null) { List<ItemStack> subBlocks = new ArrayList<ItemStack>();
     * testBlock.getSubBlocks(blockID, CreativeTabs.tabBlock, subBlocks); if (metadata != -1 && subBlocks.size() == 1) {
     * TCLog.warning("Block " + blockID + " has no sub-blocks. You should probably remove the metadata..."); } else if (metadata == -1 &&
     * subBlocks.size() > 1) { TCLog.info("Block " + blockID +
     * " has sub-blocks, but no metadata was specified. This will match all sub-blocks."); } BlockID newID = new BlockID(blockID, metadata);
     * if (!blockList.contains(newID)) blockList.add(newID); TCLog.info("Configured Block: " + blockID + ", " + metadata); } else
     * TCLog.warning("Block could not be found: " + blockID + ", " + metadata); } } } }
     * TCLog.info("Completed parsing TreeCapitator Block ID list."); }
     */
}
