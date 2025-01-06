package maxmag_change.fancyvfx.particles;

import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;

import java.awt.*;

public class ParticlesImpl {

    public static WorldParticleBuilder GenericExpandingRingParticle = WorldParticleBuilder.create(FancyVFXParticleRegistry.RING_PARTICLE)
            .setScaleData(GenericParticleData.create(1f, 10f).build())
            .setTransparencyData(GenericParticleData.create(0.2f, 0f).build())
            .setColorData(ColorParticleData.create(new Color(0xFFFFFF)).build())
            .setFullBrightLighting()
            .setLifetime(20);

}
