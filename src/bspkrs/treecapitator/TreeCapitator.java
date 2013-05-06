package bspkrs.treecapitator;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTextureTile;
import net.minecraft.item.ItemStack;
import bspkrs.util.BlockID;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;
import bspkrs.util.Coord;

public final class TreeCapitator
{
    
    public final static String                         idResolverModIDDesc                = "The mod ID value for ID Resolver.";
    public static String                               idResolverModID                    = "IDResolver";
    public final static String                         remoteTreeConfigURLDesc            = "Incomplete - do not use";
    // "Leave this URL as is to get the latest tree definitions from my master list.\nFeel free to start your own remote list to share with your friends or send your suggestions to me for the master list!";
    @Deprecated
    public static String                               remoteTreeConfigURL                = "http://bspk.rs/Minecraft/" + Const.MCVERSION + "/treeCapitatorTreeConfig.txt";
    public final static String                         remoteBlockIDConfigDesc            = "Incomplete - do not use";
    // "Values downloaded from: " + remoteTreeConfigURL;
    public static String                               remoteBlockIDConfig                = "";
    public final static String                         localBlockIDListDesc               = "Automatically generated:";
    public static String                               localBlockIDList                   = "";
    public final static String                         useRemoteTreeConfigDesc            = "Incomplete - do not use";
    // "Set to true to use the remote block ID list (must also set allowGetOnlineTreeConfig to true), false to use local config.";
    @Deprecated
    public static boolean                              useRemoteTreeConfig                = false;
    public final static String                         allowGetRemoteTreeConfigDesc       = "Incomplete - do not use";
    // "Set to true to allow TreeCapitator to retrieve the remote block ID list, false to disable.";
    @Deprecated
    public static boolean                              allowGetRemoteTreeConfig           = false;
    
    public final static String                         enableEnchantmentModeDesc          = "Toggle for whether or not to use the Treecapitating enchantment as opposed to requiring an item to be in the axeIDList to chop a tree.";
    public static boolean                              enableEnchantmentMode              = false;
    public final static String                         requireItemInAxeListForEnchantDesc = "Whether or not to check axeIDList for an item when determining if a given item can be imbued with the Treecapitating enchantment.\n" +
                                                                                                  "NOTE: when set to false, any ItemTool type item (pickaxes, shovels, etc) with a high enough enchantability level can get the enchantment, not just axes.";
    public static boolean                              requireItemInAxeListForEnchant     = false;
    public final static String                         axeIDListDesc                      = "IDs of items that can chop down trees. Use ',' to split item id from metadata and ';' to split items.";
    public static String                               axeIDList                          = Item.axeWood.itemID + "; " + Item.axeStone.itemID + "; " + Item.axeIron.itemID + "; " + Item.axeGold.itemID + "; " + Item.axeDiamond.itemID;
    public final static String                         needItemDesc                       = "Whether you need an item from the axeIDList to chop down a tree. Disabling will let you chop trees with any item.";
    public static boolean                              needItem                           = true;
    public final static String                         onlyDestroyUpwardsDesc             = "Setting this to false will allow the chopping to move downward as well as upward (and blocks below the one you break will be chopped)";
    public static boolean                              onlyDestroyUpwards                 = true;
    public final static String                         destroyLeavesDesc                  = "Enabling this will make leaves be destroyed when trees are chopped.";
    public static boolean                              destroyLeaves                      = true;
    public final static String                         requireLeafDecayCheckDesc          = "When true TreeCapitator will only instantly decay leaves that have actually been marked for decay.\n" +
                                                                                                  "Set to false if you want leaves to be destroyed regardless of their decay status (hint: or for \"leaf\" blocks that are not really leaves).";
    public static boolean                              requireLeafDecayCheck              = true;
    public final static String                         shearLeavesDesc                    = "Enabling this will cause destroyed leaves to be sheared when a shearing item is in the hotbar (ignored if destroyLeaves is false).";
    public static boolean                              shearLeaves                        = false;
    public final static String                         shearVinesDesc                     = "Enabling this will shear /some/ of the vines on a tree when a shearing item is in the hotbar (ignored if destroyLeaves is false).";
    public static boolean                              shearVines                         = false;
    public final static String                         shearIDListDesc                    = "IDs of items that when placed in the hotbar will allow leaves to be sheared when shearLeaves is true.\n" +
                                                                                                  "Use ',' to split item id from metadata and ';' to split items.";
    public static String                               shearIDList                        = Item.shears.itemID + "";
    public final static String                         logHardnessNormalDesc              = "The hardness of Strings.LOGS for when you are using items that won't chop down the trees.";
    public static float                                logHardnessNormal                  = 2.0F;
    public final static String                         logHardnessModifiedDesc            = "The hardness of Strings.LOGS for when you are using items that can chop down trees.";
    public static float                                logHardnessModified                = 4.0F;
    public final static String                         disableInCreativeDesc              = "Flag to disable tree chopping in Creative mode";
    public static boolean                              disableInCreative                  = false;
    public final static String                         disableCreativeDropsDesc           = "Flag to disable drops in Creative mode";
    public static boolean                              disableCreativeDrops               = false;
    public final static String                         allowItemDamageDesc                = "Enable to cause item damage based on number of blocks destroyed";
    public static boolean                              allowItemDamage                    = true;
    public final static String                         allowMoreBlocksThanDamageDesc      = "Enable to allow chopping down the entire tree even if your item does not have enough damage remaining to cover the number of blocks.";
    public static boolean                              allowMoreBlocksThanDamage          = false;
    public final static String                         damageMultiplierDesc               = "Axes and shears will take damage this many times for each log broken.\n" +
                                                                                                  "Remaining damage is rounded and applied to tools when a tree is finished.";
    public static float                                damageMultiplier                   = 1.0F;
    public final static String                         useIncreasingItemDamageDesc        = "Set to true to have the per-block item damage amount increase after every increaseDamageEveryXBlocks blocks are broken.";
    public static boolean                              useIncreasingItemDamage            = false;
    public final static String                         increaseDamageEveryXBlocksDesc     = "When useIncreasingItemDamage=true the damage applied per block broken will increase each time this many blocks are broken in a tree.";
    public static int                                  increaseDamageEveryXBlocks         = 15;
    public final static String                         damageIncreaseAmountDesc           = "When useIncreasingItemDamage=true the damage applied per block broken will increase by this amount every increaseDamageEveryXBlocks blocks broken in a tree.";
    public static float                                damageIncreaseAmount               = 1.0F;
    public final static String                         sneakActionDesc                    = "Set sneakAction = \"disable\" to disable tree chopping while sneaking,\n" +
                                                                                                  "set sneakAction = \"enable\" to only enable tree chopping while sneaking,\n" +
                                                                                                  "set sneakAction = \"none\" to have tree chopping enabled regardless of sneaking.";
    public static String                               sneakAction                        = "disable";
    public static int                                  maxBreakDistance                   = 16;
    public final static String                         allowSmartTreeDetectionDesc        = "Set to false to disable TreeCapitator Smart Tree Detection.\n" +
                                                                                                  "Smart Tree Detection counts the number of leaf blocks that are adjacent to the\n" +
                                                                                                  "top-most connected log block at the x, z location of a log you've broken. If\n" +
                                                                                                  "there are at least minLeavesToID leaf blocks within maxLeafIDDist blocks then\n" +
                                                                                                  "TreeCapitator considers it a tree and allows chopping.\n" +
                                                                                                  "WARNING: Disabling Smart Tree Detection will remove the only safeguard against\n" +
                                                                                                  "accidentally destroying a log structure.  Make sure you know what you're doing!";
    public static boolean                              allowSmartTreeDetection            = true;
    public final static String                         maxLeafIDDistDesc                  = "If a tree's top log is not close enough to leaf blocks, the tree will not be chopped.\n" +
                                                                                                  "Increasing this value will search further.  I would try to keep it below 3.";
    public static int                                  maxLeafIDDist                      = 1;
    public final static String                         maxLeafBreakDistDesc               = "The maximum distance to instantly decay leaves from any log block that is removed by TreeCapitator.";
    public static int                                  maxLeafBreakDist                   = 4;
    public final static String                         minLeavesToIDDesc                  = "The minimum number of leaves within maxLeafIDDist of the top log block required to identify a tree.";
    public static int                                  minLeavesToID                      = 3;
    public final static String                         useStrictBlockPairingDesc          = "Set to true if you want only the leaf blocks listed with each log in blockIDList\n"
                                                                                                  + "to break when that log type is chopped.  When set to false it will break\n"
                                                                                                  + "any leaf type within range of the tree, not just the type for that tree.";
    public static boolean                              useStrictBlockPairing              = true;
    
    public final static String                         allowDebugOutputDesc               = "Set to true if you want TreeCapitator to tell you what kind of block you have clicked when sneaking, false to disable.";
    public static boolean                              allowDebugOutput                   = false;
    public final static String                         allowDebugLoggingDesc              = "Set to true if you want TreeCapitator to log info about what it's doing, false to disable";
    public static boolean                              allowDebugLogging                  = false;
    
    public static boolean                              isForge                            = false;
    public static Block                                wood;
    
    @Deprecated
    public static ArrayList<BlockID>                   logIDList                          = new ArrayList<BlockID>();
    @Deprecated
    public static ArrayList<BlockID>                   leafIDList                         = new ArrayList<BlockID>();
    @Deprecated
    public static Map<BlockID, ArrayList<BlockID>>     logToLeafListMap                   = new HashMap<BlockID, ArrayList<BlockID>>();
    @Deprecated
    public static Map<BlockID, ArrayList<BlockID>>     logToLogListMap                    = new HashMap<BlockID, ArrayList<BlockID>>();
    @Deprecated
    public static Map<String, HashMap<String, String>> configBlockList                    = new HashMap<String, HashMap<String, String>>();
    @Deprecated
    public static Map<String, HashMap<String, String>> thirdPartyConfig                   = new HashMap<String, HashMap<String, String>>();
    @Deprecated
    public static Map<String, String>                  tagMap                             = new HashMap<String, String>();
    public static ArrayList<Coord>                     blocksBeingChopped                 = new ArrayList<Coord>();
    
    // TODO: write a new config category description
    public static final String                         configBlockIDDesc                  = "Add the log and leaf block IDs for all trees you want to be able to chop down.\n" +
                                                                                                  "Each section below represents a type of tree.  Each list may contain block IDs\n" +
                                                                                                  "and/or third-party config replacement tags. You can change it to be more or\n" +
                                                                                                  "less granular as long as all sections follow the basic structure.  Do not use\n" +
                                                                                                  "spaces or periods in your section names.  Otherwise you can call them anything\n" +
                                                                                                  "you like.\n\n" +
                                                                                                  "EACH LOG ID MAY ONLY APPEAR IN EXACTLY ONE SECTION.\n\n" +
                                                                                                  "NOTE: Some mod trees use vanilla log blocks as well as custom blocks.  If a tree\n" +
                                                                                                  "contains more than 1 type of log, all Strings.LOGS must be included in the same section.\n" +
                                                                                                  "Examples of this are the default entries for vanilla_ebxl_oaks and vanilla_ebxl_spruces.\n\n" +
                                                                                                  "Simple Example (all Strings.LOGS and leaves are grouped in one section, no metadata is specified):\n" +
                                                                                                  "    trees {\n" +
                                                                                                  "        S:leaves=18; <Forestry.leaves>; <ExtrabiomesXL.autumnleaves.id>; <ExtrabiomesXL.greenleaves.id>\n" +
                                                                                                  "        S:Strings.LOGS=17; <Forestry.log1>; <Forestry.log2>; <Forestry.log3>; <Forestry.log4>; <ExtrabiomesXL.customlog.id>; <ExtrabiomesXL.quarterlog0.id>; <ExtrabiomesXL.quarterlog1.id>; <ExtrabiomesXL.quarterlog2.id>;<ExtrabiomesXL.quarterlog3.id>\n" +
                                                                                                  "    }\n\n" +
                                                                                                  "Advanced Example (each mod tree has its own section, metadata is included):\n" +
                                                                                                  "    vanilla_ebxl_oaks {\n" +
                                                                                                  "        S:leaves=18,0\n" +
                                                                                                  "        S:Strings.LOGS=17,0; 17,4; 17,8; <ExtrabiomesXL.quarterlog0.id>,2; <ExtrabiomesXL.quarterlog1.id>,2; <ExtrabiomesXL.quarterlog2.id>,2;<ExtrabiomesXL.quarterlog3.id>,2;\n" +
                                                                                                  "    }\n\n" +
                                                                                                  "    birches {\n" +
                                                                                                  "        S:leaves=18,2\n" +
                                                                                                  "        S:Strings.LOGS=17,2; 17,6; 17,10\n" +
                                                                                                  "    }\n\n" +
                                                                                                  "    vanilla_ebxl_spruces {\n" +
                                                                                                  "        S:leaves=18,1; <ExtrabiomesXL.autumnleaves.id>\n" +
                                                                                                  "        S:Strings.LOGS=17,1; 17,5; 17,9\n" +
                                                                                                  "    }\n\n" +
                                                                                                  "    jungle_trees {\n" +
                                                                                                  "        S:leaves=18,3\n" +
                                                                                                  "        S:Strings.LOGS=17,3; 17,7; 17,11\n" +
                                                                                                  "    }\n\n" +
                                                                                                  "    ic2_rubber {\n" +
                                                                                                  "        S:leaves=<IC2.blockRubLeaves>\n" +
                                                                                                  "        S:Strings.LOGS=<IC2.blockRubWood>\n" +
                                                                                                  "    }\n\n" +
                                                                                                  "    ebxl_acacia {\n" +
                                                                                                  "        S:leaves=<ExtrabiomesXL.greenleaves.id>,2\n" +
                                                                                                  "        S:Strings.LOGS=<ExtrabiomesXL.customlog.id>,1\n" +
                                                                                                  "    }\n\n" +
                                                                                                  "    ebxl_firs {\n" +
                                                                                                  "        S:leaves=<ExtrabiomesXL.greenleaves.id>,0\n" +
                                                                                                  "        S:Strings.LOGS=<ExtrabiomesXL.customlog.id>,0; <ExtrabiomesXL.quarterlog0.id>,1; <ExtrabiomesXL.quarterlog1.id>,1; <ExtrabiomesXL.quarterlog2.id>,1; <ExtrabiomesXL.quarterlog3.id>,1\n" +
                                                                                                  "    }\n\n" +
                                                                                                  "    ebxl_redwoods {\n" +
                                                                                                  "        S:leaves=<ExtrabiomesXL.greenleaves.id>,1\n" +
                                                                                                  "        S:Strings.LOGS=<ExtrabiomesXL.quarterlog0.id>,0; <ExtrabiomesXL.quarterlog1.id>,0; <ExtrabiomesXL.quarterlog2.id>,0; <ExtrabiomesXL.quarterlog3.id>,0\n" +
                                                                                                  "    }";
    
    // TODO: write a new config category description
    public static final String                         thirdPartyConfigDesc               = "Third-Party config entries tell TreeCapitator how to find the block IDs from\n" +
                                                                                                  "other mods' config files.  These values are case-sensitive!\n\n" +
                                                                                                  "Format:\n" +
                                                                                                  "    <section_name> {\n" +
                                                                                                  "        S:modID=<modID from mcmod.info>\n" +
                                                                                                  "        S:configPath=<path to config file relative to .minecraft/config/>\n" +
                                                                                                  "        S:blockValues=<block config section>:<config property name>; <mod config section>:<config property name>\n" +
                                                                                                  "        S:itemValues=<item config section>:<property name>; <item config section>:<property name>\n" +
                                                                                                  "        B:useShiftedItemID=<whether or not to use the +256 shifted item ID> (true/false)\n" +
                                                                                                  "    }\n\n" +
                                                                                                  "Example:\n" +
                                                                                                  "    extrabiomesxl {\n" +
                                                                                                  "        S:modID=ExtrabiomesXL\n" +
                                                                                                  "        S:configPath=extrabiomes/extrabiomes.cfg\n" +
                                                                                                  "        S:blockValues=block:customlog.id; block:quarterlog0.id; block:quarterlog1.id; block:quarterlog2.id; block:quarterlog3.id; block:autumnleaves.id; block:greenleaves.id\n" +
                                                                                                  "        S:itemValues=items.world:axeRuby.id; items.world:axeGreenSapphire.id; items.world:axeSapphire.id\n" +
                                                                                                  "        B:useShiftedItemID=true\n" +
                                                                                                  "    }\n\n" +
                                                                                                  "Once your third-party config entries are setup, you can use replacement\n" +
                                                                                                  "tags in your tree, axe, and shears ID configs.  Replacement tags are structured like this:\n" +
                                                                                                  "<ModName.ConfigPropName>";
    
    static
    {
        HashMap<String, String> vanilla_oak;
        HashMap<String, String> vanilla_birch;
        HashMap<String, String> vanilla_spruce;
        HashMap<String, String> vanilla_jungle;
        
        /*
         * Third-Party config defaults
         */
        
        HashMap<String, String> biomesoplenty = new HashMap<String, String>();
        biomesoplenty.put(Strings.MOD_ID, "BiomesOPlenty");
        biomesoplenty.put(Strings.CONFIG_PATH, "BiomesOPlenty.cfg");
        biomesoplenty.put(Strings.BLOCK_VALUES, "block:Acacia Leaves ID; block:Acacia Log ID; block:Apple Leaves ID; " +
                "block:Fruitless Apple Leaves ID; block:Bamboo ID; block:Bamboo Leaves ID; block:Cherry Log ID; " +
                "block:Dark Leaves ID; block:Dark Log ID; block:Dying Leaves ID; block:Dead Log ID; block:Fir Leaves ID; " +
                "block:Fir Log ID; block:Magic Log ID; block:Magic Leaves ID; block:Mangrove Leaves ID; block:Mangrove Log ID; " +
                "block:Maple Leaves ID; block:Orange Autumn Leaves ID; block:Origin Leaves ID; block:Palm Leaves ID; " +
                "block:Palm Log ID; block:Pink Cherry Leaves ID; block:Redwood Leaves ID; block:Redwood Log ID; " +
                "block:White Cherry Leaves ID; block:Willow Leaves ID; block:Willow Log ID; block:Yellow Autumn Leaves ID");
        biomesoplenty.put(Strings.ITEM_VALUES, "item:Muddy Axe ID");
        biomesoplenty.put(Strings.SHIFT_INDEX, "true");
        vanilla_oak = new HashMap<String, String>();
        vanilla_oak.put(Strings.LEAVES, "<block:Dying Leaves ID>; <block:Origin Leaves ID>; " +
                "<block:Apple Leaves ID>; <block:Fruitless Apple Leaves ID>; <block:Orange Autumn Leaves ID>; " +
                "<block:Maple Leaves ID>");
        vanilla_birch = new HashMap<String, String>();
        vanilla_birch.put(Strings.LEAVES, "<block:Yellow Autumn Leaves ID>");
        
        HashMap<String, String> biomesoplenty_dead = new HashMap<String, String>();
        biomesoplenty_dead.put(Strings.LOGS, "<block:Dead Log ID>");
        configBlockList.put("biomesoplenty_dead", biomesoplenty_dead);
        
        HashMap<String, String> biomesoplenty_acacia = new HashMap<String, String>();
        biomesoplenty_acacia.put(Strings.LOGS, "<block:Acacia Log ID>");
        biomesoplenty_acacia.put(Strings.LEAVES, "<block:Acacia Leaves ID>");
        configBlockList.put("biomesoplenty_acacia", biomesoplenty_acacia);
        
        HashMap<String, String> biomesoplenty_bamboo = new HashMap<String, String>();
        biomesoplenty_bamboo.put(Strings.LOGS, "<block:Bamboo ID>");
        biomesoplenty_bamboo.put(Strings.LEAVES, "<block:Bamboo Leaves ID>");
        configBlockList.put("biomesoplenty_bamboo", biomesoplenty_bamboo);
        
        HashMap<String, String> biomesoplenty_cherry = new HashMap<String, String>();
        biomesoplenty_cherry.put(Strings.LOGS, "<block:Cherry Log ID>");
        biomesoplenty_cherry.put(Strings.LEAVES, "<block:Pink Cherry Leaves ID>; <block:White Cherry Leaves ID>");
        configBlockList.put("biomesoplenty_cherry", biomesoplenty_cherry);
        
        HashMap<String, String> biomesoplenty_dark = new HashMap<String, String>();
        biomesoplenty_dark.put(Strings.LOGS, "<block:Dark Log ID>");
        biomesoplenty_dark.put(Strings.LEAVES, "<block:Dark Leaves ID>; <block:White Cherry Leaves ID>");
        configBlockList.put("biomesoplenty_darkwood", biomesoplenty_dark);
        
        HashMap<String, String> biomesoplenty_fir = new HashMap<String, String>();
        biomesoplenty_fir.put(Strings.LOGS, "<block:Fir Log ID>");
        biomesoplenty_fir.put(Strings.LEAVES, "<block:Fir Leaves ID>");
        configBlockList.put("biomesoplenty_fir", biomesoplenty_fir);
        
        HashMap<String, String> biomesoplenty_magic = new HashMap<String, String>();
        biomesoplenty_magic.put(Strings.LOGS, "<block:Magic Log ID>");
        biomesoplenty_magic.put(Strings.LEAVES, "<block:Magic Leaves ID>");
        configBlockList.put("biomesoplenty_magic", biomesoplenty_magic);
        
        HashMap<String, String> biomesoplenty_mangrove = new HashMap<String, String>();
        biomesoplenty_mangrove.put(Strings.LOGS, "<block:Mangrove Log ID>");
        biomesoplenty_mangrove.put(Strings.LEAVES, "<block:Mangrove Leaves ID>");
        configBlockList.put("biomesoplenty_mangrove", biomesoplenty_mangrove);
        
        HashMap<String, String> biomesoplenty_palm = new HashMap<String, String>();
        biomesoplenty_palm.put(Strings.LOGS, "<block:Palm Log ID>");
        biomesoplenty_palm.put(Strings.LEAVES, "<block:Palm Leaves ID>");
        configBlockList.put("biomesoplenty_palm", biomesoplenty_palm);
        
        HashMap<String, String> biomesoplenty_redwood = new HashMap<String, String>();
        biomesoplenty_redwood.put(Strings.LOGS, "<block:Redwood Log ID>");
        biomesoplenty_redwood.put(Strings.LEAVES, "<block:Redwood Leaves ID>");
        configBlockList.put("biomesoplenty_redwood", biomesoplenty_redwood);
        
        HashMap<String, String> biomesoplenty_willow = new HashMap<String, String>();
        biomesoplenty_willow.put(Strings.LOGS, "<block:Willow Log ID>");
        biomesoplenty_willow.put(Strings.LEAVES, "<block:Willow Leaves ID>");
        configBlockList.put("biomesoplenty_willow", biomesoplenty_willow);
        thirdPartyConfig.put("biomesoplenty", biomesoplenty);
        
        HashMap<String, String> divinerpg = new HashMap<String, String>();
        divinerpg.put(Strings.MOD_ID, "DivineRPG");
        divinerpg.put(Strings.CONFIG_PATH, "DivineRPG.cfg");
        divinerpg.put(Strings.BLOCK_VALUES, "block:eucalyptus");
        divinerpg.put(Strings.ITEM_VALUES, "item:Bedrock Axe; item:Crystal Axe; item:Realmite Axe; item:azuriteaxe; item:corruptedaxe; " +
                "item:denseaxe; item:divineaxe; item:donatoraxe; item:energyaxe; item:mythrilaxe; item:plasmaaxe; item:serenityaxe; item:twilightaxe");
        
        divinerpg.put(Strings.axeIDList, "<item:Bedrock Axe>; <item:Crystal Axe>; <item:Realmite Axe>; <item:azuriteaxe>; <item:corruptedaxe>; " +
                "<item:denseaxe>; <item:divineaxe>; <item:donatoraxe>; <item:energyaxe>; <item:mythrilaxe>; " +
                "<item:plasmaaxe>; <item:serenityaxe>; <item:twilightaxe>");
        divinerpg.put(Strings.SHIFT_INDEX, "true");
        
        HashMap<String, String> divinerpg_eucalyptus = new HashMap<String, String>();
        divinerpg_eucalyptus.put(Strings.LOGS, "<block:eucalyptus>");
        divinerpg_eucalyptus.put(Strings.LEAVES, "18"); // not sure on this? haven't found any of them yet and no sapling
        thirdPartyConfig.put("divinerpg", divinerpg);
        
        HashMap<String, String> extrabiomesxl = new HashMap<String, String>();
        extrabiomesxl.put(Strings.MOD_ID, "ExtrabiomesXL");
        extrabiomesxl.put(Strings.CONFIG_PATH, "extrabiomes/extrabiomes.cfg");
        extrabiomesxl.put(Strings.BLOCK_VALUES, "block:customlog.id; block:quarterlog0.id; block:quarterlog1.id; block:quarterlog2.id; block:quarterlog3.id; " +
                "block:autumnleaves.id; block:greenleaves.id");
        vanilla_oak = new HashMap<String, String>();
        vanilla_oak.put(Strings.LOGS, "<block:quarterlog0.id>,2; <block:quarterlog1.id>,2; <block:quarterlog2.id>,2; <block:quarterlog3.id>,2;");
        vanilla_oak.put(Strings.LEAVES, "<block:autumnleaves.id>");
        vanilla_spruce = new HashMap<String, String>();
        vanilla_spruce.put(Strings.LEAVES, "<block:autumnleaves.id>");
        HashMap<String, String> ebxl_redwoods = new HashMap<String, String>();
        ebxl_redwoods.put(Strings.LOGS, "<block:quarterlog0.id>,0; <block:quarterlog1.id>,0; <ExtrabiomesXL.quarterlog2.id>,0; " +
                "<ExtrabiomesXL.quarterlog3.id>,0");
        ebxl_redwoods.put(Strings.LEAVES, "<ExtrabiomesXL.greenleaves.id>,1");
        HashMap<String, String> ebxl_firs = new HashMap<String, String>();
        ebxl_firs.put(Strings.LOGS, "<ExtrabiomesXL.customlog.id>,0; <ExtrabiomesXL.quarterlog0.id>,1; <ExtrabiomesXL.quarterlog1.id>,1; " +
                "<ExtrabiomesXL.quarterlog2.id>,1; <ExtrabiomesXL.quarterlog3.id>,1");
        ebxl_firs.put(Strings.LEAVES, "<ExtrabiomesXL.greenleaves.id>,0");
        HashMap<String, String> ebxl_acacia = new HashMap<String, String>();
        ebxl_acacia.put(Strings.LOGS, "<ExtrabiomesXL.customlog.id>,1");
        ebxl_acacia.put(Strings.LEAVES, "<ExtrabiomesXL.greenleaves.id>,2");
        thirdPartyConfig.put("extrabiomesxl", extrabiomesxl);
        
        HashMap<String, String> forestry = new HashMap<String, String>();
        forestry.put(Strings.MOD_ID, "Forestry");
        forestry.put(Strings.CONFIG_PATH, "forestry/base.conf");
        forestry.put(Strings.BLOCK_VALUES, "block:log1; block:log2; block:log3; block:log4; block:leaves");
        
        HashMap<String, String> forestry_larch = new HashMap<String, String>();
        forestry_larch.put(Strings.LOGS, "<block:log1>,0; <block:log1>,4; <block:log1>,8");
        forestry_larch.put(Strings.LEAVES, "<block:leaves>,0; <block:leaves>,8");
        
        HashMap<String, String> forestry_teak = new HashMap<String, String>();
        forestry_teak.put(Strings.LOGS, "<block:log1>,1; <block:log1>,5; <block:log1>,9");
        forestry_teak.put(Strings.LEAVES, "<block:leaves>,0; <block:leaves>,8");
        
        HashMap<String, String> forestry_acacia = new HashMap<String, String>();
        forestry_acacia.put(Strings.LOGS, "<block:log1>,2; <block:log1>,6; <block:log1>,10");
        forestry_acacia.put(Strings.LEAVES, "<block:leaves>,0; <block:leaves>,8");
        
        HashMap<String, String> forestry_lime = new HashMap<String, String>();
        forestry_lime.put(Strings.LOGS, "<block:log1>,3; <block:log1>,7; <block:log1>,11");
        forestry_lime.put(Strings.LEAVES, "<block:leaves>,0; <block:leaves>,8");
        
        HashMap<String, String> forestry_chestnut = new HashMap<String, String>();
        forestry_chestnut.put(Strings.LOGS, "<block:log2>,0; <block:log2>,4; <block:log2>,8");
        forestry_chestnut.put(Strings.LEAVES, "<block:leaves>,0; <block:leaves>,8");
        
        HashMap<String, String> forestry_wenge = new HashMap<String, String>();
        forestry_wenge.put(Strings.LOGS, "<block:log2>,1; <block:log2>,5; <block:log2>,9");
        forestry_wenge.put(Strings.LEAVES, "<block:leaves>,0; <block:leaves>,8");
        
        HashMap<String, String> forestry_baobab = new HashMap<String, String>();
        forestry_baobab.put(Strings.LOGS, "<block:log2>,2; <block:log2>,6; <block:log2>,10");
        forestry_baobab.put(Strings.LEAVES, "<block:leaves>,0; <block:leaves>,8");
        
        HashMap<String, String> forestry_sequoia = new HashMap<String, String>();
        forestry_sequoia.put(Strings.LOGS, "<block:log2>,3; <block:log2>,7; <block:log2>,11");
        forestry_sequoia.put(Strings.LEAVES, "<block:leaves>,0; <block:leaves>,8");
        
        HashMap<String, String> forestry_kapok = new HashMap<String, String>();
        forestry_kapok.put(Strings.LOGS, "<block:log3>,0; <block:log3>,4; <block:log3>,8");
        forestry_kapok.put(Strings.LEAVES, "<block:leaves>,0; <block:leaves>,8");
        
        HashMap<String, String> forestry_ebony = new HashMap<String, String>();
        forestry_ebony.put(Strings.LOGS, "<block:log3>,1; <block:log3>,5; <block:log3>,9");
        forestry_ebony.put(Strings.LEAVES, "<block:leaves>,0; <block:leaves>,8");
        
        HashMap<String, String> forestry_mahogany = new HashMap<String, String>();
        forestry_mahogany.put(Strings.LOGS, "<block:log3>,2; <block:log3>,6; <block:log3>,10");
        forestry_mahogany.put(Strings.LEAVES, "<block:leaves>,0; <block:leaves>,8");
        
        HashMap<String, String> forestry_balsa = new HashMap<String, String>();
        forestry_balsa.put(Strings.LOGS, "<block:log3>,3; <block:log3>,7; <block:log3>,11");
        forestry_balsa.put(Strings.LEAVES, "<block:leaves>,0; <block:leaves>,8");
        
        HashMap<String, String> forestry_willow = new HashMap<String, String>();
        forestry_willow.put(Strings.LOGS, "<block:log4>,0; <block:log4>,4; <block:log4>,8");
        forestry_willow.put(Strings.LEAVES, "<block:leaves>,0; <block:leaves>,8");
        
        HashMap<String, String> forestry_walnut = new HashMap<String, String>();
        forestry_walnut.put(Strings.LOGS, "<block:log4>,1; <block:log4>,5; <block:log4>,9");
        forestry_walnut.put(Strings.LEAVES, "<block:leaves>,0; <block:leaves>,8");
        
        HashMap<String, String> forestry_boojum = new HashMap<String, String>();
        forestry_boojum.put(Strings.LOGS, "<block:log4>,2; <block:log4>,6; <block:log4>,10");
        forestry_boojum.put(Strings.LEAVES, "<block:leaves>,0; <block:leaves>,8");
        
        HashMap<String, String> forestry_cherry = new HashMap<String, String>();
        forestry_cherry.put(Strings.LOGS, "<block:log4>,3; <block:log4>,7; <block:log4>,11");
        forestry_cherry.put(Strings.LEAVES, "<block:leaves>,0; <block:leaves>,8");
        thirdPartyConfig.put("forestry", forestry);
        
        HashMap<String, String> ic2 = new HashMap<String, String>();
        ic2.put(Strings.MOD_ID, "IC2");
        ic2.put(Strings.CONFIG_PATH, "IC2.cfg");
        ic2.put(Strings.BLOCK_VALUES, "block:blockRubWood; block:blockRubLeaves");
        ic2.put(Strings.ITEM_VALUES, "item:itemToolBronzeAxe; item:itemToolChainsaw");
        ic2.put(Strings.axeIDList, "<item:itemToolBronzeAxe>; <item:itemToolChainsaw>");
        ic2.put(Strings.shearIDList, "<item:itemToolChainsaw>");
        ic2.put(Strings.SHIFT_INDEX, "true");
        
        HashMap<String, String> ic2_rubber = new HashMap<String, String>();
        ic2_rubber.put(Strings.LOGS, "<block:blockRubWood>");
        ic2_rubber.put(Strings.LEAVES, "<block:blockRubLeaves>");
        thirdPartyConfig.put("ic2", ic2);
        
        HashMap<String, String> inficraft = new HashMap<String, String>();
        inficraft.put(Strings.MOD_ID, "Flora Trees");
        inficraft.put(Strings.CONFIG_PATH, "InfiCraft/FloraSoma.txt");
        inficraft.put(Strings.BLOCK_VALUES, "block:Bloodwood Block; block:Flora Leaves; block:Redwood Block; block:Sakura Leaves; block:Wood Block");
        
        HashMap<String, String> inficraft_bloodwood = new HashMap<String, String>();
        inficraft_bloodwood.put(Strings.LOGS, "<block:Bloodwood Block>");
        inficraft_bloodwood.put(Strings.LEAVES, "<block:Sakura Leaves>,2");
        
        HashMap<String, String> inficraft_eucalyptus = new HashMap<String, String>();
        inficraft_eucalyptus.put(Strings.LOGS, "<block:Wood Block>,0; <block:Wood Block>,4; <block:Wood Block>,8");
        inficraft_eucalyptus.put(Strings.LEAVES, "<block:Flora Leaves>,1");
        
        HashMap<String, String> inficraft_ghostwood = new HashMap<String, String>();
        inficraft_ghostwood.put(Strings.LOGS, "<block:Wood Block>,2; <block:Wood Block>, 6; <block:Wood Block>, 10");
        inficraft_ghostwood.put(Strings.LEAVES, "<block:Sakura Leaves>,1");
        
        HashMap<String, String> inficraft_hopseed = new HashMap<String, String>();
        inficraft_hopseed.put(Strings.LOGS, "<block:Wood Block>,3; <block:Wood Block>, 7; <block:Wood Block>, 11");
        inficraft_hopseed.put(Strings.LEAVES, "<block:Flora Leaves>,2");
        
        HashMap<String, String> inficraft_redwood = new HashMap<String, String>();
        inficraft_redwood.put(Strings.LOGS, "<block:Redwood Block>");
        inficraft_redwood.put(Strings.LEAVES, "<block:Flora Leaves>,0");
        
        HashMap<String, String> inficraft_sakura = new HashMap<String, String>();
        inficraft_sakura.put(Strings.LOGS, "<block:Wood Block>, 1; <block:Wood Block>, 5; <block:Wood Block>, 9");
        inficraft_sakura.put(Strings.LEAVES, "<block:Sakura Leaves>,0");
        thirdPartyConfig.put("inficraft", inficraft);
        
        HashMap<String, String> mfreloaded = new HashMap<String, String>();
        mfreloaded.put(Strings.MOD_ID, "MFReloaded");
        mfreloaded.put(Strings.CONFIG_PATH, "MFReloaded.cfg");
        mfreloaded.put(Strings.BLOCK_VALUES, "block:ID.RubberWood; block:ID.RubberLeaves; block:ID.RubberSapling");
        
        HashMap<String, String> mfr_rubber = new HashMap<String, String>();
        mfr_rubber.put(Strings.LOGS, "<block:ID.RubberWood>");
        mfr_rubber.put(Strings.LEAVES, "<block:ID.RubberLeaves>");
        thirdPartyConfig.put("mfreloaded", mfreloaded);
        
        HashMap<String, String> redpower = new HashMap<String, String>();
        redpower.put(Strings.MOD_ID, "RedPowerWorld");
        redpower.put(Strings.CONFIG_PATH, "redpower/redpower.cfg");
        redpower.put(Strings.BLOCK_VALUES, "blocks.world:log.id; blocks.world:leaves.id");
        redpower.put(Strings.ITEM_VALUES, "items.world:axeRuby.id; items.world:axeGreenSapphire.id; items.world:axeSapphire.id");
        redpower.put(Strings.SHIFT_INDEX, "true");
        ic2.put(Strings.axeIDList, "<items.world:axeRuby.id>; <items.world:axeGreenSapphire.id>; <items.world:axeSapphire.id>");
        HashMap<String, String> rp2_rubber = new HashMap<String, String>();
        rp2_rubber.put(Strings.LOGS, "<blocks.world:log.id>");
        rp2_rubber.put(Strings.LEAVES, "<blocks.world:leaves.id>");
        thirdPartyConfig.put("redpower", redpower);
        
        HashMap<String, String> thaumcraft = new HashMap<String, String>();
        thaumcraft.put(Strings.MOD_ID, "Thaumcraft");
        thaumcraft.put(Strings.CONFIG_PATH, "Thaumcraft.cfg");
        thaumcraft.put(Strings.BLOCK_VALUES, "block:BlockMagicalLog; block:BlockMagicalLeaves");
        thaumcraft.put(Strings.ITEM_VALUES, "item:Thaumaxe");
        ic2.put(Strings.axeIDList, "<item:Thaumaxe>");
        thaumcraft.put(Strings.SHIFT_INDEX, "true");
        
        HashMap<String, String> thaum_greatwood = new HashMap<String, String>();
        thaum_greatwood.put(Strings.LOGS, "<block:BlockMagicalLog>,0; <block:BlockMagicalLog>,4; <block:BlockMagicalLog>,8");
        thaum_greatwood.put(Strings.LEAVES, "<block:BlockMagicalLeaves>,0; <block:BlockMagicalLeaves>,8");
        
        HashMap<String, String> thaum_silverwood = new HashMap<String, String>();
        thaum_silverwood.put(Strings.LOGS, "<Thaumcraft.BlockMagicalLog>,1; <Thaumcraft.BlockMagicalLog>,5; <Thaumcraft.BlockMagicalLog>,9");
        thaum_silverwood.put(Strings.LEAVES, "<Thaumcraft.BlockMagicalLeaves>,1");
        thirdPartyConfig.put("thaumcraft", thaumcraft);
        
        HashMap<String, String> twilightforest = new HashMap<String, String>();
        twilightforest.put(Strings.MOD_ID, "TwilightForest");
        twilightforest.put(Strings.CONFIG_PATH, "TwilightForest.cfg");
        twilightforest.put(Strings.BLOCK_VALUES, "block:Log; block:MagicLog; block:MagicStrings.LOGSpecial; block:Leaves; block:MagicLeaves; block:Hedge");
        twilightforest.put(Strings.ITEM_VALUES, "item:IronwoodAxe; item:SteeleafAxe; item:MinotaurAxe");
        twilightforest.put(Strings.axeIDList, "<item:IronwoodAxe>; <item:SteeleafAxe>; <item:MinotaurAxe>; ");
        twilightforest.put(Strings.SHIFT_INDEX, "true");
        
        HashMap<String, String> twilight_oaks = new HashMap<String, String>();
        twilight_oaks.put(Strings.LOGS, "<block:Log>,0; <block:Log>,4; <block:Log>,8; <block:Log>,12");
        twilight_oaks.put(Strings.LEAVES, "<block:Leaves>,0; <block:Leaves>,3; <block:Leaves>,8; <block:Leaves>,11");
        
        HashMap<String, String> twilight_canopy = new HashMap<String, String>();
        twilight_canopy.put(Strings.LOGS, "<block:Log>,1; <block:Log>,5; <block:Log>,9; <block:Log>,13");
        twilight_canopy.put(Strings.LEAVES, "<block:Leaves>, 1; <block:Leaves>,9");
        
        HashMap<String, String> twilight_mangrove = new HashMap<String, String>();
        twilight_mangrove.put(Strings.LOGS, "<block:Log>,2; <block:Log>,6; <block:Log>,10; <block:Log>,14");
        twilight_mangrove.put(Strings.LEAVES, "<block:Leaves>, 1; <block:Leaves>,9");
        
        HashMap<String, String> twilight_darkwood = new HashMap<String, String>();
        twilight_darkwood.put(Strings.LOGS, "<block:Log>,3; <block:Log>,7; <block:Log>,11;  <block:Log>,15");
        twilight_darkwood.put(Strings.LEAVES, "<block:Hedge>,1");
        
        HashMap<String, String> twilight_time = new HashMap<String, String>();
        twilight_time.put(Strings.LOGS, "<block:MagicLog>,0; <block:Log>,4; <block:Log>,8; <block:MagicLog>,12");
        twilight_time.put(Strings.LEAVES, "<block:MagicLeaves>,0; <block:MagicLeaves>,8");
        thirdPartyConfig.put("twilightforest", twilightforest);
        
        HashMap<String, String> zapapples = new HashMap<String, String>();
        zapapples.put(Strings.MOD_ID, "ZapApples");
        zapapples.put(Strings.CONFIG_PATH, "ZapApples.cfg");
        zapapples.put(Strings.BLOCK_VALUES, "block:zapAppleLogID; block:zapAppleLeavesID; block:zapAppleFlowersID");
        
        HashMap<String, String> zapapple = new HashMap<String, String>();
        zapapple.put(Strings.LOGS, "<block:zapAppleLogID>");
        zapapple.put(Strings.LEAVES, "<block:zapAppleLeavesID>; <block:zapAppleFlowersID>");
        thirdPartyConfig.put("zapapples", zapapples);
        
        /*
         * Default local config
         */
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
    
    public static void preInit()
    {
        preInit(false);
    }
    
    public static void preInit(boolean isForgeVersion)
    {
        isForge = isForgeVersion;
        
        if (!isForge)
        {
            Block.blocksList[Block.wood.blockID] = null;
            wood = new BlockTree(Block.wood.blockID);
            Block.blocksList[wood.blockID] = wood;
            Item.itemsList[wood.blockID] = null;
            Item.itemsList[wood.blockID] = (new ItemMultiTextureTile(wood.blockID - 256, wood, BlockLog.woodType)).setUnlocalizedName("log");
            
            logIDList.add(new BlockID(wood.blockID));
        }
        else
        {}
    }
    
    @Deprecated
    public static boolean isLogBlock(BlockID blockID)
    {
        return logIDList.contains(blockID);
    }
    
    public static boolean isAxeItem(ItemStack itemStack)
    {
        return itemStack != null && itemStack.stackSize > 0 && CommonUtils.isItemInList(itemStack.itemID, itemStack.getItemDamage(), TreeCapitator.axeIDList);
    }
    
    @Deprecated
    public static String getStringFromConfigBlockList()
    {
        String list = "";
        for (HashMap<String, String> group : configBlockList.values())
            list += " ! " + group.get(Strings.LOGS) + (group.containsKey(Strings.LEAVES) ? "|" + group.get(Strings.LEAVES) : "");
        return replaceThirdPartyBlockTags(list.replaceFirst(" ! ", ""));
    }
    
    @Deprecated
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
    
    @Deprecated
    public static void parseConfigBlockList(String list)
    {
        logIDList = new ArrayList<BlockID>();
        leafIDList = new ArrayList<BlockID>();
        logToLogListMap = new HashMap<BlockID, ArrayList<BlockID>>();
        logToLeafListMap = new HashMap<BlockID, ArrayList<BlockID>>();
        
        TCLog.debug("Parsing Tree Block Config string: %s", list);
        
        if (list.trim().length() > 0)
        {
            String[] entries = list.trim().split("!");
            for (String entry : entries)
            {
                if (entry.trim().length() > 0)
                {
                    TCLog.debug("  Parsing Tree entry: %s", entry);
                    if (entry.trim().length() > 0)
                    {
                        String[] blockTypes = entry.trim().split("\\|");
                        
                        // parse log ids [0]
                        ArrayList<BlockID> logIDs = new ArrayList<BlockID>();
                        String[] logBlocks = blockTypes[0].trim().split(";");
                        
                        TCLog.debug("    Found log ID list: %s", blockTypes[0].trim());
                        
                        for (String logBlockStr : logBlocks)
                        {
                            String[] logBlock = logBlockStr.trim().split(",");
                            
                            TCLog.debug("    Found log ID: %s", logBlockStr);
                            int blockID = CommonUtils.parseInt(logBlock[0].trim(), -1);
                            
                            if (blockID != -1)
                            {
                                int metadata = -1;
                                
                                if (logBlock.length > 1)
                                    metadata = CommonUtils.parseInt(logBlock[1].trim(), -1);
                                TCLog.debug("    ++Configured log: %s, %s", blockID, metadata);
                                
                                BlockID logID = new BlockID(blockID, metadata);
                                if (!logIDList.contains(logID))
                                {
                                    logIDList.add(logID);
                                    logIDs.add(logID);
                                }
                            }
                            else
                                TCLog.debug("Block ID %s could not be parsed as an integer.  Ignoring entry.", logBlock[0].trim());
                        }
                        
                        for (BlockID logID : logIDs)
                            logToLogListMap.put(logID, logIDs);
                        
                        ArrayList<BlockID> pairedLeaves = new ArrayList<BlockID>();
                        
                        // parse leaf ids [1]
                        if (blockTypes.length > 1)
                        {
                            String[] leafBlocks = blockTypes[1].trim().split(";");
                            
                            TCLog.debug("    Found leaf ID list: %s", blockTypes[1].trim());
                            
                            for (String block : leafBlocks)
                            {
                                if (block.trim().length() > 0)
                                {
                                    TCLog.debug("    Found leaf ID: %s", block.trim());
                                    String[] leafBlock = block.trim().split(",");
                                    int blockID = CommonUtils.parseInt(leafBlock[0].trim(), -1);
                                    
                                    if (blockID != -1)
                                    {
                                        int metadata = -1;
                                        
                                        if (leafBlock.length > 1)
                                            metadata = CommonUtils.parseInt(leafBlock[1].trim(), -1);
                                        
                                        TCLog.debug("    ++Configured leaf: %s, %s", blockID, metadata);
                                        
                                        BlockID leafID = new BlockID(blockID, metadata);
                                        if (!leafIDList.contains(leafID))
                                            leafIDList.add(leafID);
                                        
                                        if (!pairedLeaves.contains(leafID))
                                            pairedLeaves.add(leafID);
                                    }
                                    else
                                        TCLog.debug("Block ID %s could not be parsed as an integer.  Ignoring entry.", leafBlock[0].trim());
                                }
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
}
