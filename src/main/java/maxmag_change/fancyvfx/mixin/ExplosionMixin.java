package maxmag_change.fancyvfx.mixin;

import maxmag_change.fancyvfx.FancyVFX;
import maxmag_change.fancyvfx.FancyVFXConfig;
import maxmag_change.fancyvfx.FancyVFXSounds;
import maxmag_change.fancyvfx.particles.FancyVFXParticleRegistry;
import maxmag_change.fancyvfx.particles.ParticlesImpl;
import maxmag_change.fancyvfx.util.EffectiveCreateSplash;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.RepeatingAudioStream;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
import team.lodestar.lodestone.systems.particle.world.behaviors.components.DirectionalBehaviorComponent;
import team.lodestar.lodestone.systems.screenshake.PositionedScreenshakeInstance;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import java.awt.*;

@Environment(EnvType.CLIENT)
@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    public ScreenshakeInstance explosionScreenShake;

    @Unique
    WorldParticleBuilder GenericSmokeParticle() {
        Random random = Random.create();

        int dark = random.nextBetween(0,80);
        int dark1 = random.nextBetween(0,80);

       return WorldParticleBuilder.create(FancyVFXParticleRegistry.SMOKE_PARTICLE)
                .setScaleData(GenericParticleData.create(0.2f,3f, 1.5f).build())
                .setTransparencyData(GenericParticleData.create(0.2f, 0f).build())
                .setNaturalLighting()
                .setColorData(ColorParticleData.create(new Color(96-dark1, 96-dark1, 96-dark1, 255).darker(), new Color(147-dark, 147-dark, 147-dark, 255).darker()).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setLifetime(168)
                .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT.withDepthFade())
                .setRandomOffset(1)
                .setRandomMotion(0.1,0.1,0.1)
                .addMotion(0, (double) random.nextBetween(0, 50) /100,0)
                .setGravityStrength(0.2f)
                .disableNoClip();
    }

    @Shadow @Final private World world;
    @Shadow @Final private double x;
    @Shadow @Final private double y;
    @Shadow @Final private double z;
    @Shadow @Final private float power;
    @Shadow public abstract boolean shouldDestroy();

    @Inject(method = "affectWorld(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"), cancellable = true)
    public void norix$spawnExplosiveParticles(boolean particles, CallbackInfo ci) {

        float genericPower = Math.round(power * FancyVFXConfig.explosionMultiplier);

        if (explosionScreenShake == null && FancyVFXConfig.explosionScreenShake) {
            explosionScreenShake = new PositionedScreenshakeInstance(30, new Vec3d(x,y,z).add(0,1,0), 10f, 20f* genericPower, Easing.CIRC_IN_OUT).setIntensity(0f,FancyVFXConfig.screenShakeIntensity* genericPower,0f);
            ScreenshakeHandler.addScreenshake(explosionScreenShake);
        }
        if (particles && FancyVFXConfig.improvedExplosions) {

            ParticlesImpl.GenericExpandingRingParticle
                    .setScaleData(GenericParticleData.create(1f,5f* genericPower,5f* genericPower).setEasing(Easing.CUBIC_IN).setCoefficient(3f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 74, 58), new Color(255, 255, 255)).setEasing(Easing.BOUNCE_IN_OUT).build())
                    .spawn(world, x, y, z);

            ParticlesImpl.GenericExpandingRingParticle
                    .setBehavior(new DirectionalBehaviorComponent(new Vec3d(0,-1,0)))
                    .setScaleData(GenericParticleData.create(1f,5f* genericPower,5f* genericPower).setEasing(Easing.CUBIC_IN).setCoefficient(3f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 74, 58), new Color(255, 255, 255)).setEasing(Easing.BOUNCE_IN_OUT).build())
                    .spawn(world, x, y, z);

            if (world.getFluidState(BlockPos.ofFloored(x,y,z)).isIn(FluidTags.WATER)){
                if (FancyVFXConfig.underWaterExplosions) {
                    genericPower = genericPower/2;

                    for (int i = 0; i != 15* genericPower; i++) {
                        WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                                .setScaleData(GenericParticleData.create(2f* genericPower +Random.create().nextBetween(-1,1), 0).build())
                                .setTransparencyData(GenericParticleData.create(0.75f, 0f).build())
                                .setColorData(ColorParticleData.create(new Color(255, 99, 0), new Color(255, 178, 3, 255)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                                .setFullBrightLighting()
                                .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((world.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                                .setLifetime(25)
                                .setRandomOffset(0.3)
                                .setRandomMotion(0.05* genericPower)
                                .disableNoClip()
                                .enableForcedSpawn()
                                .spawn(world, x, y, z);

                        if (FancyVFXConfig.smokeFromExplosions) {
                            GenericSmokeParticle()
                                    .setScaleData(GenericParticleData.create(0.2f,3f* genericPower,2f* genericPower).build())
                                    .setColorData(ColorParticleData.create(new Color(255, 255, 255, 255).darker()).build())
                                    .setRandomOffset(1.5* genericPower)
                                    .setLifetime(35)
                                    .setTransparencyData(GenericParticleData.create(0.1f, 0f).build())
                                    .setMotion(0,0,0)
                                    .setFrictionStrength(9.999f)
                                    .spawn(world, x, y, z);
                        }
                    }

                    int yi = 0;
                    for (; world.getFluidState(BlockPos.ofFloored(x,y+yi,z)).isIn(FluidTags.WATER); yi++){}

                    MinecraftClient.getInstance().getSoundManager().play(new PositionedSoundInstance(FancyVFXSounds.WATER_SURFACE_EXPLOSION,SoundCategory.HOSTILE,1-((float) yi /(genericPower*4)),1,Random.create(),x,y+yi,z));

                    //world.playSound(null,x,y+yi,z,FancyVFXSounds.WATER_SURFACE_EXPLOSION,SoundCategory.AMBIENT,1,1);

                    if (FabricLoader.getInstance().isModLoaded("effective")) try{
                        if (yi< genericPower *4){
                            EffectiveCreateSplash.create(world, genericPower *4-yi, genericPower *3-yi+0.3f,x,y+yi,z);
                        }
                    } catch (LinkageError error) {
                        FancyVFX.LOGGER.error("Failed to setup effective integration. Underwater explosions will look boring.", error);
                    }
                    else {
                        FancyVFX.LOGGER.info("Effective is not installed. Underwater explosions will look boring.");
                    }
                }
            } else {
                for (int i = 0; i != 15* genericPower; i++) {

                    float fl = world.getRandom().nextFloat();

                    if (FancyVFXConfig.smokeFromExplosions) {
                        WorldParticleBuilder.create(FancyVFXParticleRegistry.SMOKE_PARTICLE)
                                .setScaleData(GenericParticleData.create(0.7f,0.9f,0.6f).setEasing(Easing.CUBIC_IN,Easing.BOUNCE_OUT).setCoefficient(3f).build())
                                .setTransparencyData(GenericParticleData.create(0.8f, 0.5f).build())
                                .setFullBrightLighting()
                                .setColorData(ColorParticleData.create(new Color(255, 74, 58), new Color(115, 115, 115)).setEasing(Easing.BOUNCE_IN_OUT).build())
                                .setLifetime(56)
                                .setRandomOffset(0.5,0.1,0.5)
                                .setLightLevel(20)
                                .setRandomMotion(0.5)
                                .setRenderType(ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT)
                                .setGravityStrength(0.5f)
                                .disableNoClip()
                                .spawn(world, x,y,z);

                        GenericSmokeParticle()
                                .setScaleData(GenericParticleData.create(0.2f,4f* genericPower,2f* genericPower).build())
                                .setRandomOffset(2* genericPower)
                                //.multiplyLifetime(fl!=0 ? fl : 1)
                                .spawn(world, x, y, z);
                    }

                    //adding blast 😍
                    WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                            .setScaleData(GenericParticleData.create(2f* genericPower +Random.create().nextBetween(-1,1), 0).build())
                            .setTransparencyData(GenericParticleData.create(0.75f, 0f).build())
                            .setColorData(ColorParticleData.create(new Color(255, 99, 0), new Color(255, 178, 3, 255)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                            .setFullBrightLighting()
                            .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((world.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                            .setLifetime(25)
                            .setRandomOffset(0.3)
                            .setRandomMotion(0.2* genericPower)
                            .disableNoClip()
                            .enableForcedSpawn()
                            .spawn(world, x, y, z);
                    WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                            .setScaleData(GenericParticleData.create(1.2f* genericPower +Random.create().nextBetween(-1,1), 0).build())
                            .setTransparencyData(GenericParticleData.create(0.75f, 0f).build())
                            .setColorData(ColorParticleData.create(new Color(255, 95, 49), new Color(129, 52, 0, 255)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                            .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((world.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                            .setLifetime(25)
                            .setRandomOffset(0.3)
                            .setRandomMotion(0.7* genericPower)
                            .disableNoClip()
                            .spawn(world, x, y, z);
                }
            }
        }
        ci.cancel();
    }
}