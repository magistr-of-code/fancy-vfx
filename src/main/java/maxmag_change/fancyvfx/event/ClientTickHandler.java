package maxmag_change.fancyvfx.event;

import maxmag_change.fancyvfx.FancyVFXConfig;
import maxmag_change.fancyvfx.particles.FancyVFXParticleRegistry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.HorseEntityRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import java.awt.*;
import java.util.List;

public class ClientTickHandler implements ClientTickEvents.StartTick{

    @Override
    public void onStartTick(MinecraftClient client) {
        ClientWorld world = client.world;
        ClientPlayerEntity player = client.player;

        if (player != null&&world != null && FancyVFXConfig.damageVFX) {
            List<Entity> entities =  world.getOtherEntities(null,new Box(player.getBlockPos()).expand(50));

            entities.removeIf(entity -> !(entity instanceof LivingEntity));

            entities.forEach(entity->{
                LivingEntity livingEntity = ((LivingEntity) entity);
                int hurtTime = livingEntity.hurtTime;

                if (hurtTime==9){
                    Box bounds = livingEntity.getBoundingBox();

                    Vec3d pos = bounds.getCenter();

                    float size = (float) new Vec3d(bounds.maxX, bounds.maxY, bounds.maxZ).distanceTo(new Vec3d(bounds.minX, bounds.minY, bounds.minZ));

                    for (int i = 0; i <= 8*size; i++){

                        WorldParticleBuilder spark = WorldParticleBuilder.create(FancyVFXParticleRegistry.PIXEL_PARTICLE)
                                .setScaleData(GenericParticleData.create(0.1f*Random.create().nextFloat(), 0).build())
                                .setTransparencyData(GenericParticleData.create(0.75f, 0f).build())
                                .setColorData(ColorParticleData.create(new Color(255, 99+livingEntity.getRandom().nextBetween(0,80), 3), new Color(255, 178, 3, 255)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                                .setFullBrightLighting()
                                .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((world.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                                .setLifetime(20)
                                .setRandomOffset(size/4)
                                .setGravityStrength(1)
                                .disableNoClip();

                        spark
                                .setRandomMotion(0.25*size)
                                .spawn(world,pos.getX(),pos.getY(),pos.getZ());
                        spark
                                .setMotion(entity.getVelocity().multiply(-0.5f))
                                .addMotion((double) Random.create().nextBetween(-100, 100) /100, (double) Random.create().nextBetween(-100, 100) /100, (double) Random.create().nextBetween(-100, 100) /100)
                                .spawn(world, pos.getX(),pos.getY(),pos.getZ());
                    }
                }
            });
        }
    }
}

