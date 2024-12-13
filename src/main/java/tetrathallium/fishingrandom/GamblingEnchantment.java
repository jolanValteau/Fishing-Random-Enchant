package tetrathallium.fishingrandom;

import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.EnchantmentHelper;
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

    private static int getLevel(ItemStack stack) {
        return EnchantmentHelper.getLevel(FishingRandom.GAMBLING, stack);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() == Items.FISHING_ROD;
    }

    public static boolean SuccessGambling(ItemStack rod) {
        boolean isSuccess = false;
        Random random = new Random();

        int chance = random.nextInt(100);
        int level = getLevel(rod);

        isSuccess = chance > getMinLevelSuccessRate(level);

        return isSuccess;
    }

    private static int getMinLevelSuccessRate(int level)
    {
        // Pourcentage de succes selon le niveau de l'enchantement
        return switch (level) {
            case 0 -> 0;
            case 1 -> 10;
            case 2 -> 15;
            case 3 -> 20;
            case 4 -> 25;
            case 5 -> 27;
            case 6 -> 30;
            case 7 -> 40;
            case 8 -> 60;
            case 9 -> 90;
            case 10 -> 100;
            default -> 100;
        };
    }
    
}
