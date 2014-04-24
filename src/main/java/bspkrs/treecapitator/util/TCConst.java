package bspkrs.treecapitator.util;

import bspkrs.util.Const;

public class TCConst
{
    public static final String TCMODID                            = "TreeCapitator";
    public static final String VERSION_NUMBER                     = Const.MCVERSION + ".r02";
    
    public static final String OAK                                = "vanilla_oak";
    public static final String SPRUCE                             = "vanilla_spruce";
    public static final String BIRCH                              = "vanilla_birch";
    public static final String JUNGLE                             = "vanilla_jungle";
    public static final String ACACIA                             = "vanilla_acacia";
    public static final String DARK_OAK                           = "vanilla_dark_oak";
    public static final String FUTURE_TREE_1                      = "vanilla_future_tree_1";
    public static final String FUTURE_TREE_2                      = "vanilla_future_tree_2";
    public static final String MUSH_BROWN                         = "vanilla_huge_brown_mushroom";
    public static final String MUSH_RED                           = "vanilla_huge_red_mushroom";
    public static final String VAN_TREES_ITEMS_CTGY               = "1_vanilla_trees_and_items";
    
    public static final byte   PROTOCOL_VERSION                   = 0x01;
    public static final byte   PACKET_LOGIN                       = 0x00;
    public static final byte   PACKET_CONFIG                      = 0x01;
    
    public static final String LOGS                               = "logs";
    public static final String TREES                              = "trees";
    public static final String TREE_DEFS                          = "treeDefs";
    public static final String TREE_NAME                          = "treeName";
    public static final String MASTER_DEF                         = "masterDefinition";
    public static final String BLACKLIST                          = "blacklist";
    public static final String LOG_STR_MAP                        = "logToStringMap";
    public static final String LEAVES                             = "leaves";
    public static final String MOD_ID                             = "modID";
    public static final String AXE_ID_LIST                        = "axeIDList";
    public static final String SHEARS_ID_LIST                     = "shearsIDList";
    public static final String OVERRIDE_IMC                       = "overrideIMC";
    public static final String TREE_MOD_CFG_CTGY                  = "tree_and_mod_configs";
    public static final String SETTINGS_CTGY                      = "general_settings";
    public static final String BREAK_SPEED_CTGY                   = TCConst.SETTINGS_CTGY + "." + "break_speed_settings";
    public static final String ENCHANTMENT_MODE_CTGY              = TCConst.SETTINGS_CTGY + "." + "enchantment_mode_settings";
    public static final String ITEM_CTGY                          = TCConst.SETTINGS_CTGY + "." + "item_settings";
    public static final String TREE_CHOP_BEHAVIOR_CTGY            = TCConst.SETTINGS_CTGY + "." + "tree_chop_behavior_settings";
    public static final String MISC_CTGY                          = TCConst.SETTINGS_CTGY + "." + "miscellaneous_settings";
    public static final String ALLOW_SMART_TREE_DETECT            = "allowSmartTreeDetection";
    public static final String ONLY_DESTROY_UPWARDS               = "onlyDestroyUpwards";
    public static final String REQ_DECAY_CHECK                    = "requireLeafDecayCheck";
    public static final String MAX_H_LOG_DIST                     = "maxHorLogBreakDist";
    public static final String MAX_V_LOG_DIST                     = "maxVerLogBreakDist";
    public static final String MAX_LEAF_ID_DIST                   = "maxLeafIDDist";
    public static final String MAX_H_LEAF_DIST                    = "maxHorLeafBreakDist";
    public static final String MIN_LEAF_ID                        = "minLeavesToID";
    public static final String BREAK_SPEED_MOD                    = "breakSpeedModifier";
    public static final String THIRD_PARTY_MOD_CONFIG             = "ThirdPartyModConfig";
    
    //public static final String COMMENT_SEPARATOR                = "#--------------------------------------------------------------------------------------------------------#";
    //public static final String COMMENT_SEPARATOR_2              = "      #--------------------------------------------------------------------------------------------------------#";
    public static final String enabledDesc                        = "[Global] Set to true to enable Treecapitator, false to disable.";
    public static final String maxHorLeafBreakDistDesc            = "[Global, PerTree] The maximum horizontal distance that the leaf breaking effect will travel from the tree (use -1 for no limit).";
    public static final String maxHorLogBreakDistDesc             = "[Global, PerTree] The maximum horizontal distance that the log breaking effect will travel (use -1 for no limit).";
    public static final String maxVerLogBreakDistDesc             = "[Global, PerTree] The maximum vertical distance that the log breaking effect will travel (use -1 for no limit).";
    public static final String multiMineIDDesc                    = "The mod ID value for Multi-Mine.";
    public static final String userConfigOverridesIMCDesc         = "This setting controls the default behavior when a mod is both configured manually (in the config file) and \n" +
                                                                          "by the mod itself via IMC (inter-mod communication).";
    public static final String overrideIMCDesc                    = "This setting controls whether or not the mod config section it appears in will override an IMC message sent by that mod.";
    public static final String saveIMCConfigsToFileDesc           = "This setting controls whether or not IMC config messages sent by other mods will be saved to the local\n" +
                                                                          "config file when they are processed by Treecapitator. The message will only be saved if your local config\n" +
                                                                          "for a given mod is not set to override the IMC message.";
    public static final String enableEnchantmentModeDesc          = "[Global] Toggle for whether or not to use the Treecapitating enchantment as opposed to requiring an item \n" +
                                                                          "to be in the axeIDList to chop a tree.";
    public static final String enchantmentIDDesc                  = "[Global] The internal ID for the Treecapitating enchantment. Change this if the default ID is conflicting \n" +
                                                                          "with another mod.";
    public static final String requireItemInAxeListForEnchantDesc = "[Global] Whether or not to check the axe ID list for an item when determining if a given item can be \n" +
                                                                          "imbued with the Treecapitating enchantment.\n" +
                                                                          "NOTE: when set to false, any ItemTool type item (pickaxes, shovels, etc) with a high enough \n" +
                                                                          "enchantability level can get the enchantment, not just axes.";
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
    //public static final String COMMENT_SEPARATOR                = "#--------------------------------------------------------------------------------------------------------#";
    //public static final String COMMENT_SEPARATOR_2              = "      #--------------------------------------------------------------------------------------------------------#";
    public static final String treeHeightDecidesBreakSpeedDesc    = "[Global] When true, the log break speed is equal to original break speed / (tree height * <treeHeightModifier>)\n" +
                                                                          "When false, the original break speed is multiplied by the breakSpeedModifier value";
    
    public static final String treeHeightModifierDesc             = "[Global] See description for treeHeightDecidesBreakSpeed";
    public static final String allowOreDictionaryLookupDesc       = "[Global] When true, TreeCapitator will scan the Forge Ore Dictionary for blocks with an ore name matching\n" +
                                                                          "one of the strings in oreDictionaryLogStrings and generate a generic tree definition for them on the fly. \n" +
                                                                          "When false oreDictionaryLogStrings and oreDictionaryLeafStrings will be ignored.";
    public static final String oreDictionaryLogStringsDesc        = "[Global] The list of log type values to check for in the Forge Ore Dictionary. Entries are comma (,) separated.";
    public static final String oreDictionaryLeafStringsDesc       = "[Global] The list of leaf type values to check for in the Forge Ore Dictionary. Entries are comma (,) separated.";
    public static final String blockIDBlacklistDesc               = "[Global] Add unique block names to this list if you want to keep them from being registered as logs. This list will override\n" +
                                                                          "the local user configuration, inter-mod communication (IMC) configuration, and the Ore Dictionary scanning feature.\n" +
                                                                          "Use ',' to split block name from metadata and ';' to split entries.\n" +
                                                                          "Refer to the UniqueNames.txt file in the config folder for a list of values.";
    public static final String itemIDBlacklistDesc                = "[Global] Add unique item names to this list if you want to keep them from being registered as axes. This list will override\n" +
                                                                          "the local user configuration and inter-mod communication (IMC) configuration.\n" +
                                                                          "Use ',' to split item name from metadata and ';' to split entries.\n" +
                                                                          "Refer to the UniqueNames.txt file in the config folder for a list of values.";
    public static final String breakSpeedModifierDesc             = "[Global, PerTree] When using an item that can chop trees, the break speed will by multiplied by this value\n" +
                                                                          "THIS OPTION IS IGNORED WHEN treeHeightDecidesBreakSpeed=true";
    public static final String disableInCreativeDesc              = "[Global] Flag to disable tree chopping in Creative mode";
    public static final String disableCreativeDropsDesc           = "[Global] Flag to disable drops in Creative mode";
    public static final String allowItemDamageDesc                = "[Global] Enable to cause item damage based on number of blocks destroyed";
    public static final String allowMoreBlocksThanDamageDesc      = "[Global] Enable to allow chopping down the entire tree even if your item does not have enough damage \n" +
                                                                          "remaining to cover the number of blocks.";
    public static final String damageMultiplierDesc               = "[Global] Axes and shears will take damage this many times for each log broken. Remaining damage is \n" +
                                                                          "rounded and applied to tools when a tree is finished.";
    public static final String useAdvancedTopLogLogicDesc         = "[Global, PerTree] Set to false to use the older \"top log\" algorithm for finding the top log of a tree.\n" +
                                                                          "The old algorithm searches only the vertical column of blocks above the log you are chopping,\n" +
                                                                          "the newer algorithm is able to branch out to find the true top log of a tree.";
    public static final String useIncreasingItemDamageDesc        = "[Global] Set to true to have the per-block item damage amount increase after every \n" +
                                                                          "increaseDamageEveryXBlocks blocks are broken.";
    public static final String increaseDamageEveryXBlocksDesc     = "[Global] When useIncreasingItemDamage=true the damage applied per block broken will increase each time \n" +
                                                                          "this many blocks are broken in a tree.";
    public static final String damageIncreaseAmountDesc           = "[Global] When useIncreasingItemDamage=true the damage applied per block broken will increase by this \n" +
                                                                          "amount every increaseDamageEveryXBlocks blocks broken in a tree.";
    public static final String sneakActionDesc                    = "[Global] Set sneakAction = \"disable\" to disable tree chopping while sneaking,\n" +
                                                                          "set sneakAction = \"enable\" to only enable tree chopping while sneaking,\n" +
                                                                          "set sneakAction = \"none\" to have tree chopping enabled regardless of sneaking.";
    public static final String stackDropsDesc                     = "[Global] Set to true to enable the stacking of dropped items, false to disable.";
    public static final String itemsDropInPlaceDesc               = "[Global] Set to true to have items drop in place, false to have them drop at the player's position.";
    //public static final String COMMENT_SEPARATOR                = "#--------------------------------------------------------------------------------------------------------#";
    //public static final String COMMENT_SEPARATOR_2              = "      #--------------------------------------------------------------------------------------------------------#";
    public static final String allowSmartTreeDetectionDesc        = "[Global, PerTree] Set to false to disable TreeCapitator Smart Tree Detection.\n" +
                                                                          "Smart Tree Detection counts the number of leaf blocks that are adjacent to the\n" +
                                                                          "top-most connected log block at the x, z location of a log you've broken. If\n" +
                                                                          "there are at least minLeavesToID leaf blocks within maxLeafIDDist blocks then\n" +
                                                                          "TreeCapitator considers it a tree and allows chopping.\n" +
                                                                          "WARNING: Disabling Smart Tree Detection will remove the only safeguard against\n" +
                                                                          "accidentally destroying a log structure.  Make sure you know what you're doing!";
    public static final String maxLeafIDDistDesc                  = "[Global, PerTree] If a tree's top log is not close enough to leaf blocks, the tree will not be chopped.\n" +
                                                                          "Increasing this value will search further.  I would try to keep it at or below 3.";
    public static final String maxLeafBreakDistDesc               = "[Global, PerTree] The maximum distance to instantly decay leaves from any log block that is removed by TreeCapitator.";
    public static final String minLeavesToIDDesc                  = "[Global, PerTree] The minimum number of leaves within maxLeafIDDist of the top log block required to identify a tree.";
    public static final String useStrictBlockPairingDesc          = "[Global] Set to true if you want only the log/leaf blocks listed with each log in a tree\n"
                                                                          + "to break when that log type is chopped.  When set to false it will break\n"
                                                                          + "any log/leaf type blocks connected to the tree, not just the types for that tree.";
    public static final String allowDebugOutputDesc               = "[Global] Set to true if you want TreeCapitator to tell you what kind of block you have clicked when \n" +
                                                                          "sneaking, false to disable.";
    public static final String allowDebugLoggingDesc              = "[Global] Set to true if you want TreeCapitator to log info about what it's doing, false to disable.\n" +
                                                                          "If you are having an issue with the mod, set this option to true and post the resulting log to the\n" +
                                                                          "Treecapitator Minecraftforum.net thread along with a detailed description of the issue.";
    public static final String OPTIONAL                           = "Optional";
    //public static final String COMMENT_SEPARATOR                = "#--------------------------------------------------------------------------------------------------------#";
    //public static final String COMMENT_SEPARATOR_2              = "      #--------------------------------------------------------------------------------------------------------#";
    public static final String TREE_MOD_CFG_CTGY_DESC             = "This category is where all your settings live that are related to 3rd party mods.\n" +
                                                                          "NOTE: Using item or block number IDs WILL NOT WORK. Refer to the UniqueNames.txt file in the config folder for a list of values.\n\n" +
                                                                          "How to add new mods: \n\n" +
                                                                          "Keep in mind that you can also include settings marked with [PerTree] on a per-tree basis to override the global default values.\n\n" +
                                                                          "Format:\n" +
                                                                          "    <section_name> { (typically same as modID)\n" +
                                                                          "        S:modID=<modID> (this can be found on the Mods screen in game or in mcmod.info)\n" +
                                                                          "        S:axeIDList=<unique_item_identifier>,<optional metadata>; minecraft:wooden_axe; minecraft:stone_axe\n" +
                                                                          "        S:shearsIDList=<unique_item_identifier>,<optional metadata>\n" +
                                                                          "        B:overrideIMC=<optional, defaults to false) whether or not a mod's user config (this file) should override a mod's IMC config (IMC allows mods to send messages to each other for compatibility)\n\n" +
                                                                          "        <tree_name> { (the tree name is just for organization and clarity)\n" +
                                                                          "            # logs/leaves: list of unique block name values. \",\" separates name and metadata, \";\" separates block entries\n" +
                                                                          "            S:logs=<unique block identifier>,<optional metadata>; <unique_block_identifier>,0; minecraft:log,0\n" +
                                                                          "            S:leaves=<unique block identifier>,<optional metadata>; <unique_block_identifier>,0; minecraft:leaves,0\n" +
                                                                          "            [optionally add per-tree settings here]\n\n" +
                                                                          "        }\n" +
                                                                          "    }";
    public static final String VAN_TREES_ITEMS_CTGY_DESC          = "This special category is the home of the vanilla tree block and item configurations. You can change the \n" +
                                                                          "values in this category to suit your preferences.\n\n" +
                                                                          "WARNING: This config category must not be removed! If this category is renamed or removed TreeCapitator \n" +
                                                                          "will assume your config file is new and reload the default user mod config settings!";
}
