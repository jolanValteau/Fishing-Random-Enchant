package tetrathallium.fishingrandom.mixin;

import net.minecraft.text.Text;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;

import java.util.List;
import net.minecraft.util.math.random.Random;
import net.minecraft.registry.Registries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {
    
    @Inject(method = "onFishingSuccess", at = @At("HEAD"))
    private void onFishingSuccess(ItemStack caughtStack, CallbackInfo ci) {

        FishingBobberEntity bobber = (FishingBobberEntity) (Object) this;
        Entity owerEntity = bobber.getOwner();

        if (!(owerEntity instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) owerEntity;

        if (player != null) {
            player.sendMessage(Text.literal("Tu as pêché : " + caughtStack.getName().getString()), false);

            randomFishingFunction(player, caughtStack);
        }
    }

    private static void randomFishingFunction(PlayerEntity player, ItemStack caughtStack) {
        player.sendMessage(Text.of("Custom called"), false);
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