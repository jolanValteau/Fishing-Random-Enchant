package tetrathallium.fishingrandom.mixin;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.advancement.Advancement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;

import java.util.List;

import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import java.util.Random;
import net.minecraft.world.World;
import tetrathallium.fishingrandom.GamblingEnchantment;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
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
			Text displayTitle;
			Text displaySubtile;
			if (isGamblerRod)
			{
				if (GamblingEnchantment.isMaxedLevel(rod))
				{
					displayTitle = Text.of("Gambling is now");
					displaySubtile = Text.of("at his peak !");
				}
				else
				{
					displayTitle = Text.of("Gambling dedication");
					displaySubtile = Text.of("upgraded !");

				}
			}
			else
			{
				displayTitle = Text.of("You just discovered");
				displaySubtile = Text.of("Gambling !");
			}

			if (player instanceof ServerPlayerEntity serverPlayer)
			{
				// Envoi du titre principal
				serverPlayer.networkHandler.sendPacket(new TitleS2CPacket(displayTitle));
				// Envoi du sous-titre
				serverPlayer.networkHandler.sendPacket(new SubtitleS2CPacket(displaySubtile));
			}
		}

		// Gambling time !
		if (!GamblingEnchantment.successGambling(rod)) {return;} // FAIL Gambling

		grantAdvancement(player, "use_gambling_enchant_achievement");
		// Success
		player.sendMessage(Text.of("You have gambled a random item !"), true);
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

	// Méthode pour accorder un achievement
	private static void grantAdvancement(PlayerEntity player, String advancementName)
	{
		player.sendMessage(Text.of("begin grant new achievement !"), false);
		MinecraftServer server = player.getServer();
		if (server != null) {
			// Identifier pour l'advancement
			Identifier advancementId = new Identifier("fishingrandom", advancementName);
			Advancement advancement = server.getAdvancementLoader().get(advancementId);

			if (advancement != null && player instanceof ServerPlayerEntity serverPlayer)
			{
				serverPlayer.getAdvancementTracker().grantCriterion(advancement, "trigger");
				player.sendMessage(Text.of("You have unlocked a new achievement !"), false);
			}	
   		}
	}
}
