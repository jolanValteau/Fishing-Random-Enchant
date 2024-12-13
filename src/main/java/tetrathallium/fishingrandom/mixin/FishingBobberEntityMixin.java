package tetrathallium.fishingrandom.mixin;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;

import java.util.List;

import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.random.Random;
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
    
    @Inject(method = "use", at = @At("HEAD"))
    private void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> ci) {
        ItemStack rod = user.getStackInHand(hand);
        handleFishingGambling(world, user, rod);
    }

    private static void handleFishingGambling(World world, PlayerEntity player, ItemStack rod) {
		boolean isGamblingTime = false;
        for (FishingBobberEntity fishingBobber : world.getEntitiesByClass(FishingBobberEntity.class, player.getBoundingBox().expand(32), bobber -> true)) {
            boolean caughtFishBool = ((FishingBobberEntityAccessor) fishingBobber).getCaughtFish();
            if (!caughtFishBool) {continue;}
			isGamblingTime = true;
			break;
        }
        if (!isGamblingTime) {return;}

		// Gambling time !
        if (!GamblingEnchantment.SuccessGambling(rod, player)) {return;}

		// Success
		// player.sendMessage(Text.of("You got a random item !"), true); // Todo uncomment
		giveRandomItem(player);
    }

    private static void giveRandomItem(PlayerEntity player)
    {
        List<Item> allItems = Registries.ITEM.stream().toList();
        if (allItems.isEmpty())
        {return;}

        Random random = Random.create();
        Item randomItem = allItems.get(random.nextInt(allItems.size()));

        ItemStack randomStack = new ItemStack(randomItem);

        player.giveItemStack(randomStack);
    }
}