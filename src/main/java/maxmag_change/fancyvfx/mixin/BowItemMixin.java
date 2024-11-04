package maxmag_change.fancyvfx.mixin;

import maxmag_change.fancyvfx.FancyVFXConfig;
import maxmag_change.fancyvfx.particles.FancyVFXParticleRegistry;
import maxmag_change.fancyvfx.util.MathHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
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

@Mixin(BowItem.class)
public class BowItemMixin {

    @Inject(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;", at = @At("HEAD"))
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (FancyVFXConfig.improvedBow) {
            Vec3d pos = new Vec3d(user.getX(), user.getEyeY() - (double)0.1f, user.getZ());

            for (int i = 0; i<=10; i++) {
                Vec3d spawnPos = pos.add((double) Random.create().nextBetween(-100, 100) /500, (double) Random.create().nextBetween(-100, 100) /500, (double) Random.create().nextBetween(-100, 100) /500);

                Vec3d motion = spawnPos.subtract(pos);

                WorldParticleBuilder.create(FancyVFXParticleRegistry.PIXEL_PARTICLE)
                        .setScaleData(GenericParticleData.create(0.05f* Random.create().nextFloat(), 0).build())
                        .setTransparencyData(GenericParticleData.create(0.75f, 0f).build())
                        .setFullBrightLighting()
                        .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((world.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                        .setLifetime(20)
                        .setRandomOffset(0.5)
                        .setMotion(motion.multiply(-0.6))
                        .disableNoClip()
                        .setColorData(ColorParticleData.create(new Color(88, 198-Random.create().nextBetween(0,80), 241), new Color(44, 109, 193)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                        .spawn(world,spawnPos.getX(),spawnPos.getY(),spawnPos.getZ());
            }
        }
    }

    @Inject(method = "onStoppedUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V",at = @At("HEAD"))
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (FancyVFXConfig.improvedBow) {
            Vec3d pos = new Vec3d(user.getX(), user.getEyeY() - (double)0.1f, user.getZ());

            BowItem bow = (BowItem) (Object) this;

            int i;
            float f;

            if ((double)(f = BowItem.getPullProgress(i = bow.getMaxUseTime(stack) - remainingUseTicks)) < 0.1) {
                return;
            }

            Vec3d direction = MathHelper.determineDirection(user,user.getPitch(),user.getYaw(),0.0f,f/2f,1.0f,user.getRandom());

            pos = pos.add(direction);

            WorldParticleBuilder.create(FancyVFXParticleRegistry.RING_PARTICLE)
                    .setScaleData(GenericParticleData.create(f,f*2).build())
                    .setTransparencyData(GenericParticleData.create(1f, 0f).build())
                    .setFullBrightLighting()
                    .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((world.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                    .setLifetime(10)
                    .setBehavior(new DirectionalBehaviorComponent(direction))
                    .disableNoClip()
                    .setColorData(ColorParticleData.create(new Color(88, 198-Random.create().nextBetween(0,80), 241), new Color(44, 109, 193)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                    .spawn(world,pos.getX(),pos.getY(),pos.getZ());
        }
    }
}
