package maxmag_change.fancyvfx.particles;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import maxmag_change.fancyvfx.FancyVFX;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

public class FancyVFXParticleRegistry {
    public static LazyRegistrar<ParticleType<?>> PARTICLES = LazyRegistrar.create(Registries.PARTICLE_TYPE, FancyVFX.MOD_ID);


    public static final RegistryObject<LodestoneWorldParticleType> RING_PARTICLE = PARTICLES.register("ring", LodestoneWorldParticleType::new);
    public static final RegistryObject<LitLodestoneParticleType> AURA_PARTICLE = PARTICLES.register("aura", LitLodestoneParticleType::new);
    public static final RegistryObject<LitLodestoneParticleType> PIXEL_PARTICLE = PARTICLES.register("pixel", LitLodestoneParticleType::new);
    public static final RegistryObject<SmokeParticleType> SMOKE_PARTICLE = PARTICLES.register("smoke", SmokeParticleType::new);

    public static void register(){
        PARTICLES.register();

        FancyVFX.LOGGER.info("Registering Particles for " + FancyVFX.MOD_ID);

        ParticleFactoryRegistry.getInstance().register(RING_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(PIXEL_PARTICLE.get(), LitLodestoneParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(AURA_PARTICLE.get(), LitLodestoneParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SMOKE_PARTICLE.get(), SmokeParticleType.Factory::new);
    }
}

