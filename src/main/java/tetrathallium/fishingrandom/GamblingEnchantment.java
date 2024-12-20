package tetrathallium.fishingrandom;

import java.util.Map;
import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class GamblingEnchantment extends Enchantment {
	private static final Random random = new Random();

	private static final int MIN_LEVEL = 1;
	private static final int MAX_LEVEL = 10;

	public GamblingEnchantment() {
		super(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.FISHING_ROD, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
	}

	@Override
	public int getMinLevel() {
		return MIN_LEVEL;
	}

	@Override
	public int getMaxLevel() {
		return getStaticMaxLevel();
	}

	public static int getStaticMaxLevel() {
		return MAX_LEVEL;
	}

	public static boolean isGambler(ItemStack stack)
	{
		return getLevel(stack) != 0;
	}

	public static boolean isMaxedLevel(ItemStack stack)
	{
		return getLevel(stack) == getStaticMaxLevel();
	}

	private static int getLevel(ItemStack stack) {
		return EnchantmentHelper.getLevel(FishingRandom.GAMBLING, stack);
	}

	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return stack.getItem() == Items.FISHING_ROD;
	}

	public static boolean successGambling(ItemStack rod) {
		boolean isSuccess = false;

		int chance = random.nextInt(100);
		int level = getLevel(rod);

		int minLevelSuccessRate = getMinLevelSuccessRate(level);

		isSuccess = chance < minLevelSuccessRate;

		return isSuccess;
	}

	public static boolean tryUpgradeEnchant(ItemStack rod)
	{
		boolean isSuccess = false;
		int chance = random.nextInt(100);
		int level = getLevel(rod);

		if (level >= getStaticMaxLevel()) {return false;}

		int minLevelSuccessRate = getMinLevelUpgradeRate(level);

		isSuccess = chance < minLevelSuccessRate;

		if (!isSuccess) {return false;}

		Map<Enchantment, Integer> rodEnchants = EnchantmentHelper.get(rod);
		rodEnchants.put(FishingRandom.GAMBLING, level + 1);
		EnchantmentHelper.set(rodEnchants, rod);
		return true;
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

	private static int getMinLevelUpgradeRate(int level)
	{
		// Pourcentage de succes de passer au niveau suivant de l'enchantement
		return switch (level) {
			case 0 -> 5;
			case 1 -> 3;
			case 2 -> 3;
			case 3 -> 2;
			case 4 -> 2;
			case 5 -> 2;
			case 6 -> 1;
			case 7 -> 1;
			case 8 -> 1;
			case 9 -> 1;
			case 10 -> 1;
			default -> 1;
		};
	}
}
