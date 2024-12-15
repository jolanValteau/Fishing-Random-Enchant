package tetrathallium.fishingrandom;

import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FishingRandom implements ModInitializer {
	public static final String MOD_ID = "fishingrandom";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Enchantment GAMBLING = new GamblingEnchantment();

	@Override
	public void onInitialize() {
		Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "gambling"), GAMBLING);
	
	}
}