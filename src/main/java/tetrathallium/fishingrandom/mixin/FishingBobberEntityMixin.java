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
import net.minecraft.registry.Registries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingRodItem.class)
public class FishingBobberEntityMixin {
    
    @Inject(method = "use", at = @At("HEAD"))
    private void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> ci) {
        ItemStack rod = user.getStackInHand(hand);
        randomFishingFunction(user, rod);
    }

    private static void randomFishingFunction(PlayerEntity player, ItemStack rod) {
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