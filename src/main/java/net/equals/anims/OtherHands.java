package net.equals.anims;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.network.ClientPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OtherHands implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static double deltatick = 0;
	public static ClientPlayerEntity player = null;

	@Override
	public void onInitialize() {
		LOGGER.info("Rust animations mod launched!");
	}
}
