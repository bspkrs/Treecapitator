package bspkrs.treecapitator;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;

public class EnchantmentTreecapitating extends Enchantment
{
    protected EnchantmentTreecapitating(int par1, int par2)
    {
        super(par1, par2, EnumEnchantmentType.digger);
    }
    
    @Override
    public boolean canApply(ItemStack itemStack)
    {
        return itemStack.isItemEnchantable() && TCSettings.enableEnchantmentMode &&
                TCSettings.requireItemInAxeListForEnchant ? ToolRegistry.instance().isAxe(itemStack) : type.canEnchantItem(itemStack.getItem());
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack itemStack)
    {
        return itemStack.isItemEnchantable() && TCSettings.enableEnchantmentMode &&
                TCSettings.requireItemInAxeListForEnchant ? ToolRegistry.instance().isAxe(itemStack) : type.canEnchantItem(itemStack.getItem());
    }
    
    @Override
    public int getMinEnchantability(int par1)
    {
        return 20;
    }
    
    @Override
    public int getMaxEnchantability(int par1)
    {
        return 60;
    }
    
    @Override
    public int getMaxLevel()
    {
        return 1;
    }
    
    @Override
    public boolean canApplyTogether(Enchantment enchantment)
    {
        return super.canApplyTogether(enchantment) && enchantment.effectId != fortune.effectId;
    }
    
}
