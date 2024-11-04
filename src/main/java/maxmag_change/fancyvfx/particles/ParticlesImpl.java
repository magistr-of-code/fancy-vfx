package maxmag_change.fancyvfx.particles;

import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;

import java.awt.*;

public class ParticlesImpl {

    public static WorldParticleBuilder GenericSmokeParticle = WorldParticleBuilder.create(FancyVFXParticleRegistry.SMOKE_PARTICLE)
            .setScaleData(GenericParticleData.create(3f, 1.5f).build())
            .setTransparencyData(GenericParticleData.create(0.2f, 0f).build())
            .setNaturalLighting()
            .setColorData(ColorParticleData.create(new Color(156, 156, 156,255).darker(), new Color(147, 147, 147, 255).darker()).setEasing(Easing.BOUNCE_IN_OUT).build())
            .setLifetime(168)
            .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT.withDepthFade())
            .setRandomOffset(1)
            .setGravityStrength(0.05f)
            .disableNoClip();

    public static WorldParticleBuilder GenericExpandingRingParticle = WorldParticleBuilder.create(FancyVFXParticleRegistry.RING_PARTICLE)
            .setScaleData(GenericParticleData.create(1f, 10f).build())
            .setTransparencyData(GenericParticleData.create(0.2f, 0f).build())
            .setColorData(ColorParticleData.create(new Color(0xFFFFFF)).build())
            .setFullBrightLighting()
            .setLifetime(20);

}
