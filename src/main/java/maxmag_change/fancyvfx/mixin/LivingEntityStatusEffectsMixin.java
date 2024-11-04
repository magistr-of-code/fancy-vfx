package maxmag_change.fancyvfx.mixin;

import maxmag_change.fancyvfx.FancyVFXConfig;
import maxmag_change.fancyvfx.particles.FancyVFXParticleRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potions;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.DirectionalBehaviorComponent;

import java.awt.*;

@Mixin(LivingEntity.class)
public class LivingEntityStatusEffectsMixin {
    @Inject(method = "onStatusEffectApplied(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)V",at = @At("HEAD"))
    protected void onStatusEffectApplied(StatusEffectInstance effect, Entity source, CallbackInfo ci) {
        if (FancyVFXConfig.statusEffectVFX) {
            LivingEntity entity = ((LivingEntity) (Object) this);

            Vec3d direction;

            Box boundingBox = entity.getBoundingBox();

            double max = Math.max(boundingBox.getXLength(),Math.max(boundingBox.getYLength(),boundingBox.getZLength()));

            if (max==boundingBox.getZLength()) {
                direction = new Vec3d(0, 0, boundingBox.getZLength());
            }else if(max==boundingBox.getYLength()){
                direction = new Vec3d(0, boundingBox.getYLength(),0);
            } else {
                direction = new Vec3d(boundingBox.getXLength(),0,0);
            }

            Vec3d pos = entity.getBoundingBox().getCenter().subtract(direction);

            int startColor = effect.getEffectType().getColor();

            float startR = (float)((startColor >> 16 & 0xFF)) / 255.0f;
            float startG = (float)((startColor >> 8 & 0xFF)) / 255.0f;
            float startB = (float)((startColor & 0xFF)) / 255.0f;

            WorldParticleBuilder.create(FancyVFXParticleRegistry.RING_PARTICLE)
                    .setScaleData(GenericParticleData.create(1).build())
                    .setTransparencyData(GenericParticleData.create(1f, 0f).build())
                    .setFullBrightLighting()
                    .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((entity.getWorld().getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                    .setLifetime(10)
                    .setBehavior(new DirectionalBehaviorComponent(direction))
                    .setMotion(direction.multiply(0.5))
                    .disableNoClip()
                    .setColorData(ColorParticleData.create(new Color(startR,startG,startB), new Color(44, 109, 193)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                    .spawn(entity.getWorld(),pos.getX(),pos.getY(),pos.getZ());

            WorldParticleBuilder.create(FancyVFXParticleRegistry.RING_PARTICLE)
                    .setScaleData(GenericParticleData.create(1).build())
                    .setTransparencyData(GenericParticleData.create(1f, 0f).build())
                    .setFullBrightLighting()
                    .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((entity.getWorld().getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                    .setLifetime(12)
                    .setBehavior(new DirectionalBehaviorComponent(direction))
                    .setMotion(direction.multiply(0.25))
                    .disableNoClip()
                    .setColorData(ColorParticleData.create(new Color(startR,startG,startB), new Color(44, 109, 193)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                    .spawn(entity.getWorld(),pos.getX(),pos.getY(),pos.getZ());

            WorldParticleBuilder.create(FancyVFXParticleRegistry.RING_PARTICLE)
                    .setScaleData(GenericParticleData.create(1).build())
                    .setTransparencyData(GenericParticleData.create(1f, 0f).build())
                    .setFullBrightLighting()
                    .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((entity.getWorld().getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                    .setLifetime(15)
                    .setBehavior(new DirectionalBehaviorComponent(direction))
                    .setMotion(direction.multiply(0.1))
                    .disableNoClip()
                    .setColorData(ColorParticleData.create(new Color(startR,startG,startB), new Color(44, 109, 193)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                    .spawn(entity.getWorld(),pos.getX(),pos.getY(),pos.getZ());
        }
    }
}
