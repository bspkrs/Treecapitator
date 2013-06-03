package bspkrs.treecapitator;

import bspkrs.util.Configuration;
import bspkrs.util.Const;

public class Strings
{
    public static final String VERSION_NUMBER                     = Const.MCVERSION + ".r10";
    
    public static final String OAK                                = "vanilla_oak";
    public static final String SPRUCE                             = "vanilla_spruce";
    public static final String BIRCH                              = "vanilla_birch";
    public static final String JUNGLE                             = "vanilla_jungle";
    public static final String MUSH_BROWN                         = "vanilla_huge_brown_mushroom";
    public static final String MUSH_RED                           = "vanilla_huge_red_mushroom";
    public static final String VAN_TREES_ITEMS_CTGY               = "1_vanilla_trees_and_items";
    
    public static final String LOGS                               = "logs";
    public static final String TREES                              = "trees";
    public static final String TREE_DEFS                          = "treeDefs";
    public static final String TREE_NAME                          = "treeName";
    public static final String MASTER_DEF                         = "masterDefinition";
    public static final String LOG_STR_MAP                        = "logToStringMap";
    public static final String LOG_CFG_KEYS                       = "logConfigKeys";
    public static final String LEAVES                             = "leaves";
    public static final String LEAF_CFG_KEYS                      = "leafConfigKeys";
    public static final String MOD_ID                             = "modID";
    public static final String CONFIG_PATH                        = "configPath";
    public static final String BLOCK_CFG_KEYS                     = "blockConfigKeys";
    public static final String ITEM_CFG_KEYS                      = "itemConfigKeys";
    public static final String AXE_ID_LIST                        = "axeIDList";
    public static final String SHEARS_ID_LIST                     = "shearsIDList";
    public static final String SHIFT_INDEX                        = "useShiftedItemID";
    public static final String OVERRIDE_IMC                       = "overrideIMC";
    public static final String IDR_MOD_ID                         = "idResolverModID";
    public static final String MM_EXCL_LIST                       = "Excluded Block IDs";
    public static final String MM_EXCL_LIST_DESC                  = "For Multi-Mine compatibility you should put this list in the S:\"Excluded Block IDs\" config setting in AS_MultiMine.cfg";
    public static final String TREE_MOD_CFG_CTGY                  = "tree_and_mod_configs";
    public static final String SETTINGS_CTGY                      = "general_settings";
    public static final String GLOBAL_SETTINGS                    = "global_settings";
    public static final String PER_TREE_DEFAULTS                  = "per_tree_defaults";
    public static final String GLOBALS_SETTINGS_CTGY              = Strings.SETTINGS_CTGY + "." + GLOBAL_SETTINGS;
    public static final String PER_TREE_DEFAULTS_CTGY             = Strings.SETTINGS_CTGY + "." + PER_TREE_DEFAULTS;
    public static final String GLOBAL                             = "Global";
    public static final String PER_TREE                           = "PerTree";
    public static final String BLOCK_CTGY                         = "block_settings";
    public static final String ITEM_CTGY                          = "item_settings";
    public static final String LEAF_CTGY                          = "leaf_and_vine_settings";
    public static final String MISC_CTGY                          = "miscellaneous_settings";
    public static final String GENERAL                            = Configuration.CATEGORY_GENERAL;
    public static final String ONLY_DESTROY_UPWARDS               = "onlyDestroyUpwards";
    public static final String REQ_DECAY_CHECK                    = "requireLeafDecayCheck";
    public static final String MAX_H_LOG_DIST                     = "maxHorLogBreakDist";
    public static final String MAX_V_LOG_DIST                     = "maxVerLogBreakDist";
    public static final String MAX_LEAF_ID_DIST                   = "maxLeafIDDist";
    public static final String MAX_H_LEAF_DIST                    = "maxHorLeafBreakDist";
    public static final String MIN_LEAF_ID                        = "minLeavesToID";
    public static final String BREAK_SPEED_MOD                    = "breakSpeedModifier";
    
    public static final String COREMOD_WARNING                    = "TreeCapitator CoreMod code has not been injected. Ensure the downloaded .jar file is in the coremods folder and not mods.";
    
    //public static final String COMMENT_SEPARATOR                = "#--------------------------------------------------------------------------------------------------------#";
    //public static final String COMMENT_SEPARATOR_2              = "      #--------------------------------------------------------------------------------------------------------#";
    public static final String GLOBALS_SETTINGS_CTGY_DESC         = "These are the general preference settings. They are used globally to tune how TreeCapitator works.";
    public static final String PER_TREE_DEFAULTS_CTGY_DESC        = "These are the default values of settings that can be defined on a per-tree basis. If a \n" +
                                                                          "user-/mod-defined tree sets one of these values it will override the default value here.";
    public static final String maxHorLeafBreakDistDesc            = "[Global, PerTree] The maximum horizontal distance that the leaf breaking effect will travel from the tree (use -1 for no limit).";
    public static final String maxHorLogBreakDistDesc             = "[Global, PerTree] The maximum horizontal distance that the log breaking effect will travel (use -1 for no limit).";
    public static final String maxVerLogBreakDistDesc             = "[Global, PerTree] The maximum vertical distance that the log breaking effect will travel (use -1 for no limit).";
    public static final String idResolverModIDDesc                = "The mod ID value for ID Resolver.";
    public static final String multiMineIDDesc                    = "The mod ID value for Multi-Mine.";
    public static final String userConfigOverridesIMCDesc         = "This setting controls the default behavior when a mod is both configured manually (in the config file) and \n" +
                                                                          "by the mod itself via IMC (inter-mod communication).";
    public static final String enableEnchantmentModeDesc          = "[Global] Toggle for whether or not to use the Treecapitating enchantment as opposed to requiring an item \n" +
                                                                          "to be in the axeIDList to chop a tree.";
    public static final String enchantmentIDDesc                  = "[Global] The internal ID for the Treecapitating enchantment. Change this if the default ID is conflicting \n" +
                                                                          "with another mod.";
    public static final String requireItemInAxeListForEnchantDesc = "[Global] Whether or not to check the axe ID list for an item when determining if a given item can be \n" +
                                                                          "imbued with the Treecapitating enchantment.\n" +
                                                                          "NOTE: when set to false, any ItemTool type item (pickaxes, shovels, etc) with a high enough \n" +
                                                                          "enchantability level can get the enchantment, not just axes.";
    public static final String axeIDListDesc                      = "[Global] IDs of items that can chop down trees. Use ',' to split item id from metadata and ';' to split items.";
    public static final String needItemDesc                       = "[Global] Whether you need an item from the axeIDList to chop down a tree. Disabling will let you chop \n" +
                                                                          "trees with any item.";
    public static final String onlyDestroyUpwardsDesc             = "[Global, PerTree] Setting this to false will allow the chopping to move downward as well as upward (and \n" +
                                                                          "blocks below the one you break will be chopped)";
    public static final String destroyLeavesDesc                  = "[Global] Enabling this will make leaves be destroyed when trees are chopped.";
    public static final String requireLeafDecayCheckDesc          = "[Global, PerTree] When true TreeCapitator will only instantly decay leaves that have actually been marked \n" +
                                                                          "for decay. Set to false if you want leaves to be destroyed regardless of their decay status \n" +
                                                                          "(hint: or for \"leaf\" blocks that are not really leaves).";
    public static final String shearLeavesDesc                    = "[Global] Enabling this will cause destroyed leaves to be sheared when a shearing item is in the hotbar \n" +
                                                                          "(ignored if destroyLeaves is false).";
    public static final String shearVinesDesc                     = "[Global] Enabling this will shear /some/ of the vines on a tree when a shearing item is in the hotbar \n" +
                                                                          "(ignored if destroyLeaves is false).";
    public static final String shearIDListDesc                    = "[Global] IDs of items that when placed in the hotbar will allow leaves to be sheared when shearLeaves \n" +
                                                                          "is true. Use ',' to split item id from metadata and ';' to split items.";
    //public static final String COMMENT_SEPARATOR                = "#--------------------------------------------------------------------------------------------------------#";
    //public static final String COMMENT_SEPARATOR_2              = "      #--------------------------------------------------------------------------------------------------------#";
    public static final String logHardnessNormalDesc              = "[Global] The hardness of logs for when you are using items that won't chop down the trees.";
    public static final String logHardnessModifiedDesc            = "[Global] The hardness of logs for when you are using items that can chop down trees.";
    public static final String breakSpeedModifierDesc             = "[Global, PerTree] When using an item that can chop trees, the break speed will by multiplied by this value.";
    public static final String disableInCreativeDesc              = "[Global] Flag to disable tree chopping in Creative mode";
    public static final String disableCreativeDropsDesc           = "[Global] Flag to disable drops in Creative mode";
    public static final String allowItemDamageDesc                = "[Global] Enable to cause item damage based on number of blocks destroyed";
    public static final String allowMoreBlocksThanDamageDesc      = "[Global] Enable to allow chopping down the entire tree even if your item does not have enough damage \n" +
                                                                          "remaining to cover the number of blocks.";
    public static final String damageMultiplierDesc               = "[Global] Axes and shears will take damage this many times for each log broken. Remaining damage is \n" +
                                                                          "rounded and applied to tools when a tree is finished.";
    public static final String useIncreasingItemDamageDesc        = "[Global] Set to true to have the per-block item damage amount increase after every \n" +
                                                                          "increaseDamageEveryXBlocks blocks are broken.";
    public static final String increaseDamageEveryXBlocksDesc     = "[Global] When useIncreasingItemDamage=true the damage applied per block broken will increase each time \n" +
                                                                          "this many blocks are broken in a tree.";
    public static final String damageIncreaseAmountDesc           = "[Global] When useIncreasingItemDamage=true the damage applied per block broken will increase by this \n" +
                                                                          "amount every increaseDamageEveryXBlocks blocks broken in a tree.";
    public static final String sneakActionDesc                    = "[Global] Set sneakAction = \"disable\" to disable tree chopping while sneaking,\n" +
                                                                          "set sneakAction = \"enable\" to only enable tree chopping while sneaking,\n" +
                                                                          "set sneakAction = \"none\" to have tree chopping enabled regardless of sneaking.";
    //public static final String COMMENT_SEPARATOR                = "#--------------------------------------------------------------------------------------------------------#";
    //public static final String COMMENT_SEPARATOR_2              = "      #--------------------------------------------------------------------------------------------------------#";
    public static final String allowSmartTreeDetectionDesc        = "[Global] Set to false to disable TreeCapitator Smart Tree Detection.\n" +
                                                                          "Smart Tree Detection counts the number of leaf blocks that are adjacent to the\n" +
                                                                          "top-most connected log block at the x, z location of a log you've broken. If\n" +
                                                                          "there are at least minLeavesToID leaf blocks within maxLeafIDDist blocks then\n" +
                                                                          "TreeCapitator considers it a tree and allows chopping.\n" +
                                                                          "WARNING: Disabling Smart Tree Detection will remove the only safeguard against\n" +
                                                                          "accidentally destroying a log structure.  Make sure you know what you're doing!";
    public static final String maxLeafIDDistDesc                  = "[Global, PerTree] If a tree's top log is not close enough to leaf blocks, the tree will not be chopped.\n" +
                                                                          "Increasing this value will search further.  I would try to keep it below 3.";
    public static final String maxLeafBreakDistDesc               = "[Global, PerTree] The maximum distance to instantly decay leaves from any log block that is removed by TreeCapitator.";
    public static final String minLeavesToIDDesc                  = "[Global, PerTree] The minimum number of leaves within maxLeafIDDist of the top log block required to identify a tree.";
    public static final String useStrictBlockPairingDesc          = "[Global] Set to true if you want only the log/leaf blocks listed with each log in a tree\n"
                                                                          + "to break when that log type is chopped.  When set to false it will break\n"
                                                                          + "any log/leaf type blocks connected to the tree, not just the types for that tree.";
    public static final String allowDebugOutputDesc               = "[Global] Set to true if you want TreeCapitator to tell you what kind of block you have clicked when \n" +
                                                                          "sneaking, false to disable.";
    public static final String allowDebugLoggingDesc              = "[Global] Set to true if you want TreeCapitator to log info about what it's doing, false to disable.\n" +
                                                                          "If you are having an issue with the mod, set this option to true and post the resulting log to the\n" +
                                                                          "TreeCapitator Minecraftforum.net thread along with a detailed description of the issue.";
    public static final String OPTIONAL                           = "Optional";
    //public static final String COMMENT_SEPARATOR                = "#--------------------------------------------------------------------------------------------------------#";
    //public static final String COMMENT_SEPARATOR_2              = "      #--------------------------------------------------------------------------------------------------------#";
    public static final String TREE_MOD_CFG_CTGY_DESC             = "This category is where all your settings live that are related to 3rd party mods. There are two methods \n" +
                                                                          "to set up a 3rd party mod's trees and items: \n\n" +
                                                                          "(1) Config Method: uses the mod's config file to lookup block ID and item ID values. 3rd party config \n" +
                                                                          "settings tell TreeCapitator how to find a mod's config, what config values we need (log/leaf blocks, \n" +
                                                                          "axes, etc), how to use those values to define the mod's trees (if applicable), and what kind of tools the \n" +
                                                                          "items are (if applicable).\n\n" +
                                                                          "(2) Integer IDs Method: alternatively you can always just use the integer block and item IDs to define what \n" +
                                                                          "a tree or axe is.\n\n" +
                                                                          "Keep in mind that you can also include certain settings on a per-tree basis to override the global values.\n\n" +
                                                                          "Format:\n" +
                                                                          "    <section_name> { (typically same as modID)\n" +
                                                                          "        S:modID=<modID> (this can be found on the Mods screen in game or in mcmod.info)\n" +
                                                                          "        S:configPath=<path to config file relative to .minecraft/config/> (most of the time this is the same as <modID>.cfg)\n" +
                                                                          "        S:blockConfigKeys=<block config category>:<property name>; block:customLogBlockID; block:customLeafBlockID (config category is usually \"block\")\n" +
                                                                          "        S:itemConfigKeys=<item config category>:<property name>; item:superAwesomeAxeShearsID (config category is usually \"item\")\n" +
                                                                          "        S:axeIDList=<<item config category>:<property name>>; <item:superAwesomeAxeShearsID>\n" +
                                                                          "        S:shearsIDList=<item:superAwesomeAxeShearsID>\n" +
                                                                          "        B:useShiftedItemID=<(optional, defaults to true) whether or not to use the +256 shifted item ID> (true/false, almost always true)\n" +
                                                                          "        B:overrideIMC=<optional, defaults to false) whether or not a mod's user config (this file) should override a mod's IMC config (IMC allows mods to send messages to each other for compatibility)\n\n" +
                                                                          "        <tree_name> { (the tree name is just for organization and clarity)\n" +
                                                                          "            # logConfigKeys/leafConfigKeys: list of config key tags or raw integer block ID values. \",\" separates ID and metadata, \";\" separates block entries\n" +
                                                                          "            S:logConfigKeys=<<block config category>:<property name>>; <block:customLogBlockID>,0; 17,0\n" +
                                                                          "            S:leafConfigKeys=<<block config category>:<property name>>; <block:customLeafBlockID>,0; 18\n" +
                                                                          "            (per-tree settings)\n\n" +
                                                                          "        }\n" +
                                                                          "    }\n\n" +
                                                                          "Examples:\n" +
                                                                          "    ic2_using_config_method {\n" +
                                                                          "        S:modID=IC2\n" +
                                                                          "        S:configPath=IC2.cfg\n" +
                                                                          "        S:blockConfigKeys=block:blockRubWood; block:blockRubLeaves\n" +
                                                                          "        S:itemConfigKeys=item:itemToolBronzeAxe; item:itemToolChainsaw\n" +
                                                                          "        S:axeIDList=<item:itemToolBronzeAxe>; <item:itemToolChainsaw>\n" +
                                                                          "        S:shearsIDList=<item:itemToolChainsaw>\n" +
                                                                          "        B:useShiftedItemID=true\n\n" +
                                                                          "        rubber {\n" +
                                                                          "            S:logConfigKeys=<block:blockRubWood>\n" +
                                                                          "            S:leafConfigKeys=<block:blockRubLeaves>\n" +
                                                                          "        }\n" +
                                                                          "    }\n\n" +
                                                                          "    ic2_using_integer_method {\n" +
                                                                          "        S:modID=IC2\n" +
                                                                          "        S:configPath=IC2.cfg\n" +
                                                                          "        S:blockConfigKeys=\n" +
                                                                          "        S:itemConfigKeys=\n" +
                                                                          "        S:axeIDList=30199; 30233\n" +
                                                                          "        S:shearsIDList=30233\n\n" +
                                                                          "        rubber {\n" +
                                                                          "            S:logConfigKeys=243\n" +
                                                                          "            S:leafConfigKeys=242\n" +
                                                                          "        }\n" +
                                                                          "    }";
    public static final String VAN_TREES_ITEMS_CTGY_DESC          = "This special category is the home of the vanilla tree block and item configurations. You can change the \n" +
                                                                          "values in this category to suit your preferences.\n\n" +
                                                                          "WARNING: This config category must not be removed! If this category is renamed or removed TreeCapitator \n" +
                                                                          "will assume your config file is new and reload the default user mod config settings!";
    
}
