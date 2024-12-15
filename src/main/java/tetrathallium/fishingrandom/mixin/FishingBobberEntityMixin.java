package tetrathallium.fishingrandom.mixin;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;

import java.rmi.UnexpectedException;
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
		if (!GamblingEnchantment.isGambler(rod)) {return;} // Unenchanted fishing_rod // TODO MOVE

		// Gambling time !
		if (!GamblingEnchantment.successGambling(rod, player)) {return;} // FAIL Gambling

		// Success
		player.sendMessage(Text.of("You have fished a random item !"), true);
		successfullyGambled((FishingRodItem) rod.getItem(), player);
	}

	private static void successfullyGambled(FishingRodItem rod, PlayerEntity player)
	{
		giveRandomitem(player);
		upgradeEnchant(rod);
	}

	private static void giveRandomitem(PlayerEntity player)
	{
		List<Item> allItems = Registries.ITEM.stream().toList();
		if (allItems.isEmpty())
		{return;}

		player.giveItemStack(new ItemStack(allItems.get(random.nextInt(allItems.size()))));
	}

	private static void upgradeEnchant(FishingRodItem rod)
	{
		// TODO
	}
}