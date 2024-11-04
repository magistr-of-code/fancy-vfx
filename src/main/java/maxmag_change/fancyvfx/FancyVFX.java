package maxmag_change.fancyvfx;

import eu.midnightdust.lib.config.MidnightConfig;
import maxmag_change.fancyvfx.event.ClientTickHandler;
import maxmag_change.fancyvfx.event.WorldRenderHandler;
import maxmag_change.fancyvfx.particles.FancyVFXParticleRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FancyVFX implements ClientModInitializer {
	public static final String MOD_ID = "fancy-vfx";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {

		MidnightConfig.init(MOD_ID, FancyVFXConfig.class);

		FancyVFXSounds.initialize();

		FancyVFXParticleRegistry.register();

		ClientTickEvents.START_CLIENT_TICK.register(new ClientTickHandler());

		WorldRenderEvents.START.register(new WorldRenderHandler());
	}
}