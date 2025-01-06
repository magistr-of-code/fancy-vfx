package maxmag_change.fancyvfx.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

@Environment(EnvType.CLIENT)
public class LitLodestoneParticleType extends LodestoneWorldParticleType {

    public record Factory(SpriteProvider sprite) implements ParticleFactory<WorldParticleOptions> {
        @Override
        public Particle createParticle(WorldParticleOptions data, ClientWorld world, double x, double y, double z, double mx, double my, double mz) {
            data.renderType = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
            return new LodestoneWorldParticle(world, data, (FabricSpriteProviderImpl)this.sprite, x, y, z, mx, my, mz);
        }
    }
}
