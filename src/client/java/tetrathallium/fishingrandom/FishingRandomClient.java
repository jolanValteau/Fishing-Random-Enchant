package tetrathallium.fishingrandom;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.enchantment.Enchantment;

import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;


public class FishingRandomClient implements ClientModInitializer {

	public static final Enchantment GAMBLING = new GamblingEnchantment();

	@Override
	public void onInitializeClient() {
		Registry.register(Registries.ENCHANTMENT, new Identifier("fishingrandom", "gambling"), GAMBLING);
	}
}
