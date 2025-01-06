package maxmag_change.fancyvfx.mixin;

import maxmag_change.fancyvfx.FancyVFXConfig;
import maxmag_change.fancyvfx.particles.FancyVFXParticleRegistry;
import maxmag_change.fancyvfx.util.MathHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.DirectionalBehaviorComponent;

import java.awt.*;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {


    @Inject(method = "shootAll(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;FF)V", at = @At("HEAD"))
    private static void shootAll(World world, LivingEntity entity, Hand hand, ItemStack stack, float speed, float divergence, CallbackInfo ci) {
        if (FancyVFXConfig.improvedBow == FancyVFXConfig.BowOptions.ENABLE || FancyVFXConfig.improvedBow == FancyVFXConfig.BowOptions.WHEN_FIRED) {
            Vec3d pos = new Vec3d(entity.getX(), entity.getEyeY() - (double)0.1f, entity.getZ());

            Vec3d direction = MathHelper.determineDirection(entity,entity.getPitch(20),entity.getYaw(20),0.0f,1/2f,1.0f,entity.getRandom());

            pos=pos.add(direction);

            WorldParticleBuilder.create(FancyVFXParticleRegistry.RING_PARTICLE)
                    .setScaleData(GenericParticleData.create(1).build())
                    .setTransparencyData(GenericParticleData.create(1f, 0.1f).build())
                    .setFullBrightLighting()
                    .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((world.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                    .setLifetime(1)
                    .setBehavior(new DirectionalBehaviorComponent(direction))
                    .disableNoClip()
                    .setMotion(direction)
                    .setFrictionStrength(0.8f)
                    .setColorData(ColorParticleData.create(new Color(115, 206, 255), new Color(162, 201, 251)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                    .setScaleData(GenericParticleData.create(0.01f,0.5f,0.5f).build())
                    .setLifetime(10)
                    .spawn(world,pos.getX(),pos.getY(),pos.getZ());
        }
    }
}
