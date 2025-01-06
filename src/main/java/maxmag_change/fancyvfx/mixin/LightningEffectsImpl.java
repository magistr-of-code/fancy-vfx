package maxmag_change.fancyvfx.mixin;

import maxmag_change.fancyvfx.FancyVFXConfig;
import maxmag_change.fancyvfx.particles.FancyVFXParticleRegistry;
import maxmag_change.fancyvfx.particles.ParticlesImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.screenshake.PositionedScreenshakeInstance;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import java.awt.*;

@Environment(EnvType.CLIENT)
@Mixin(LightningEntity.class)
public abstract class LightningEffectsImpl extends Entity{

    @Unique
    WorldParticleBuilder GenericSmokeParticle() {
        Random random = Random.create();

        int dark = random.nextBetween(0,20);
        int dark1 = random.nextBetween(0,20);

        return WorldParticleBuilder.create(FancyVFXParticleRegistry.SMOKE_PARTICLE)
                .setScaleData(GenericParticleData.create(3f, 1.5f).build())
                .setTransparencyData(GenericParticleData.create(0.2f, 0f).build())
                .setNaturalLighting()
                .setColorData(ColorParticleData.create(new Color(96-dark1, 96-dark1, 96-dark1, 255).darker(), new Color(147-dark, 147-dark, 147-dark, 255).darker()).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setLifetime(168)
                .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT.withDepthFade())
                .setRandomOffset(1)
                .setGravityStrength(0.05f)
                .disableNoClip();
    }

    public ScreenshakeInstance lightningScreenShake;

    public LightningEffectsImpl(EntityType<?> type, World world) {
        super(type, world);
    }


    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        LightningEntity entity = ((LightningEntity) (Object) this);
        World world = entity.getWorld();
        Vec3d pos = entity.getPos();
        if (FancyVFXConfig.improvedLightning) {
            //adding center
            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(4.5f, 0).build())
                    .setTransparencyData(GenericParticleData.create(0.6f, 0f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 255, 255), new Color(197, 197, 197, 255)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                    .setSpinData(SpinParticleData.create(0.2f, 1f).setSpinOffset((world.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                    .setLifetime(15)
                    .setFullBrightLighting()
                    .setRandomOffset(0)
                    .disableNoClip()
                    .enableForcedSpawn()
                    .spawn(world, pos.getX(), pos.getY(), pos.getZ());
            WorldParticleBuilder.create(FancyVFXParticleRegistry.RING_PARTICLE)
                    .setScaleData(GenericParticleData.create(4f, 0).build())
                    .setTransparencyData(GenericParticleData.create(0.75f, 0f).build())
                    .setColorData(ColorParticleData.create(new Color(0, 255, 166), new Color(3, 255, 226, 255)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                    .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((world.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                    .setLifetime(12)
                    .setFullBrightLighting()
                    .setRandomOffset(0)
                    .disableNoClip()
                    .enableForcedSpawn()
                    .spawn(world, pos.getX(), pos.getY(), pos.getZ());

            for (int fl = 0; fl != 5; fl++) {
                //adding smoke

                if (FancyVFXConfig.smokeFromLightnings) {
                    GenericSmokeParticle()
                            .spawn(world, pos.getX(), pos.getY(), pos.getZ());
                }

                //adding swoosh 😍
                WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                        .setScaleData(GenericParticleData.create(2.6f + Random.create().nextBetween(-1, 1), 0).build())
                        .setTransparencyData(GenericParticleData.create(0.75f, 0f).build())
                        .setColorData(ColorParticleData.create(new Color(0, 255, 166), new Color(3, 255, 226, 255)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                        .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((world.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                        .setLifetime(20)
                        .setFullBrightLighting()
                        .setRandomOffset(1.2)
                        .setMotion((double) Random.create().nextBetween(-4, 4) / 10, 0.8 + (double) Random.create().nextBetween(-2, 2) / 10, (double) Random.create().nextBetween(-4, 4) / 10)
                        .disableNoClip()
                        .enableForcedSpawn()
                        .spawn(world, pos.getX(), pos.getY(), pos.getZ());
                WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                        .setScaleData(GenericParticleData.create(1.6f + Random.create().nextBetween(-1, 1), 0).build())
                        .setTransparencyData(GenericParticleData.create(0.75f, 0f).build())
                        .setColorData(ColorParticleData.create(new Color(102, 239, 244), new Color(0, 125, 129, 255)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                        .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((world.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                        .setLifetime(15)
                        .setFullBrightLighting()
                        .setRandomOffset(1.2)
                        .setMotion((double) Random.create().nextBetween(-4, 4) / 10, 0.8 + (double) Random.create().nextBetween(-2, 2) / 10, (double) Random.create().nextBetween(-4, 4) / 10)
                        .disableNoClip()
                        .spawn(world, pos.getX(), pos.getY(), pos.getZ());
                //adding flames
                WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                        .setScaleData(GenericParticleData.create(0.5f, 0).build())
                        .setTransparencyData(GenericParticleData.create(0.75f, 0f).build())
                        .setColorData(ColorParticleData.create(new Color(255, 99, 0), new Color(255, 178, 3, 255)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                        .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((world.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                        .setLifetime(25)
                        .setFullBrightLighting()
                        .setRandomOffset(0.5)
                        .setRandomMotion(0.15)
                        .disableNoClip()
                        .spawn(world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        //adding screen shake
        if (lightningScreenShake == null && FancyVFXConfig.lightningScreenShake) {
            lightningScreenShake = new PositionedScreenshakeInstance(40, pos.add(0,1,0), 10f, 50f, Easing.CIRC_IN_OUT).setIntensity(0f,FancyVFXConfig.screenShakeIntensity/2,0f);
            ScreenshakeHandler.addScreenshake(lightningScreenShake);
        }
    }
}
