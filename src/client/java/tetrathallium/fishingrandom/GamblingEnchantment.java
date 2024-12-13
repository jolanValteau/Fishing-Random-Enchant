package tetrathallium.fishingrandom;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class GamblingEnchantment extends Enchantment {
    public GamblingEnchantment() {
        super(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.FISHING_ROD, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() == Items.FISHING_ROD;
    }
    
}
