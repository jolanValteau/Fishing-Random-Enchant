package tetrathallium.fishingrandom.mixin.client;

import net.minecraft.text.Text;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;

import java.util.List;

import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.registry.Registries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingRodItem.class)
public class FishingBobberEntityMixin {

    @Inject(method = "use", at = @At("HEAD"))
    private void injectUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> info) {
        user.sendMessage(Text.of("fishing rod used"), false);
        handleFishingClient(world, user);
        // info.setReturnValue(TypedActionResult.pass(user.getStackInHand(hand)));
    }

    private static void handleFishingClient(World world, Entity entity) {
        if (!(entity instanceof PlayerEntity player)) {
            return;
        }

        for (FishingBobberEntity fishingBobber : world.getEntitiesByClass(FishingBobberEntity.class, player.getBoundingBox().expand(20), bobber -> true)) {
            boolean caughtFishBool = ((FishingBobberEntityAccessor) fishingBobber).getCaughtFish();

            if (!caughtFishBool) {
                player.sendMessage(Text.of("fishing bobber not caughted by fishy"), false);
                continue;
            }

            giveRandomItem(player);
            player.sendMessage(Text.of("gived random"), false);
        }
    }

    private static void giveRandomItem(PlayerEntity player) {
        List<Item> allItems = Registries.ITEM.stream().toList();
        if (allItems.isEmpty()) {
            return;
        }

        Random random = Random.create();  // Tu peux aussi utiliser new Random() si nécessaire
        Item randomItem = allItems.get(random.nextInt(allItems.size()));

        ItemStack randomStack = new ItemStack(randomItem);
        player.giveItemStack(randomStack);
    }
}
