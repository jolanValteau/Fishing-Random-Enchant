package tetrathallium.fishingrandom;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.random.Random;

import java.lang.reflect.Field;

import java.util.List;

public class FishingEventHandler
{
	public static void registerFishingEvent()
	{
		UseItemCallback.EVENT.register((player, world, hand) ->
		{
			// // not spectator or adventure mode
			// if (player.isSpectator())
			// {return TypedActionResult.pass(player.getMainHandStack());}

			ItemStack mainHandItem = player.getMainHandStack();
			// if (mainHandItem.getItem() != Items.FISHING_ROD)
			// {
			// 	player.sendMessage(Text.of("player clicked with this Item: " + mainHandItem.getItem().toString()), false);
			// 	return TypedActionResult.pass(mainHandItem);
			// }
			
			// if (world.isClient)
			// {
			// 	handleFishingClient((ClientWorld) world, player);
			// 	player.sendMessage(Text.of("CLIENT player clicked with fishing rod success"), false);
			// }
			return TypedActionResult.pass(mainHandItem);
		});
	}

	private static void handleFishingClient(ClientWorld world, Entity entity)
	{
		if (!(entity instanceof PlayerEntity player))
		{return;}

		for (FishingBobberEntity fishingBobber : world.getEntitiesByClass(FishingBobberEntity.class, player.getBoundingBox().expand(20), bober -> true))
		{
			boolean caughtFishBool = false;
			try{
			Field caughtFishField = FishingBobberEntity.class.getDeclaredField("caughtFish");
			caughtFishField.setAccessible(true);
			caughtFishBool = (boolean) caughtFishField.get(fishingBobber); }
			catch (NoSuchFieldException | IllegalAccessException e)
			{
				player.sendMessage(Text.of("caughtFish field not found Exceotion is: " + e.toString()), false);
				continue;
			}

			if (!caughtFishBool)
			{
				player.sendMessage(Text.of("fishing bobber not caughted by fishy"), false);
				continue;
			}

			giveRandomItem(player);
			player.sendMessage(Text.of("gived random"), false);
		}
	}

	private static void giveRandomItem(PlayerEntity player)
	{
		player.sendMessage(Text.of("receiving random item"), false);
		List<Item> allItems = Registries.ITEM.stream().toList();
		if (allItems.isEmpty())
		{return;}

		Random random = Random.create();
		Item randomItem = allItems.get(random.nextInt(allItems.size()));

		ItemStack randomStack = new ItemStack(randomItem);

		player.giveItemStack(randomStack);
	}
}