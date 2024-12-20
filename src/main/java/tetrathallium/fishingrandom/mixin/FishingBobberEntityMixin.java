package tetrathallium.fishingrandom.mixin;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;

import java.util.List;

import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import java.util.Random;
import net.minecraft.world.World;
import tetrathallium.fishingrandom.GamblingEnchantment;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingRodItem.class)
public class FishingBobberEntityMixin {
	private static final Random random = new Random();
	
	@Inject(method = "use", at = @At("HEAD"))
	private void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> ci) {
		if (isPullingFish(world, user)) {handleFishingGambling(user, hand);}
	}

	private static boolean isPullingFish(World world, PlayerEntity player) {
		for (FishingBobberEntity fishingBobber : world.getEntitiesByClass(FishingBobberEntity.class, player.getBoundingBox().expand(32), bobber -> true)) {
			if (((FishingBobberEntityAccessor) fishingBobber).getCaughtFish()) {return true;}
		}
		return false;
	}
	
	private static void handleFishingGambling(PlayerEntity player, Hand hand) {
		ItemStack rod = player.getStackInHand(hand);
		boolean isGamblerRod = GamblingEnchantment.isGambler(rod); // Unenchanted fishing_rod stop

		boolean levelUped = GamblingEnchantment.tryUpgradeEnchant(rod);
		if (levelUped)
		{
			if (isGamblerRod)
			{
				if (GamblingEnchantment.isMaxedLevel(rod)) {player.sendMessage(Text.of("Your gambling dedication is now at his peak !"), true);}
				else {player.sendMessage(Text.of("Your gambling dedication just went stronger !"), true);}
			}
			else
			{
				player.sendMessage(Text.of("You just discovered gambling !"), true);
			}
		}

		// Gambling time !
		if (!GamblingEnchantment.successGambling(rod)) {return;} // FAIL Gambling

		// Success
		player.sendMessage(Text.of("You have gambled a random item !"), true); // TO REVIEW
		successfullyGambled(player);
	}

	private static void successfullyGambled(PlayerEntity player)
	{
		giveRandomitem(player);
	}

	private static void giveRandomitem(PlayerEntity player)
	{
		List<Item> allItems = Registries.ITEM.stream().toList();
		if (allItems.isEmpty())
		{return;}

		player.giveItemStack(new ItemStack(allItems.get(random.nextInt(allItems.size()))));
	}
}
