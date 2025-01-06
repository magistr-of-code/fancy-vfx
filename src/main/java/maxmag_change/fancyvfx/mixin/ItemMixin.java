package maxmag_change.fancyvfx.mixin;

import maxmag_change.fancyvfx.FancyVFXConfig;
import maxmag_change.fancyvfx.particles.FancyVFXParticleRegistry;
import maxmag_change.fancyvfx.util.MathHelper;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "usageTick(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;I)V",at=@At("HEAD"))
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {

        Item item = (Item) (Object) this;
        if (item instanceof BowItem && FancyVFXConfig.improvedBow == FancyVFXConfig.BowOptions.ENABLE){
            Vec3d pos = new Vec3d(user.getX(), user.getEyeY() - (double)0.1f, user.getZ());

            for (int i = 0; i<=10; i++) {
                Vec3d spawnPos = pos.add((double) Random.create().nextBetween(-100, 100) /200, (double) Random.create().nextBetween(-100, 100) /200, (double) Random.create().nextBetween(-100, 100) /200);

                Vec3d motion = pos.subtract(spawnPos);

                WorldParticleBuilder.create(FancyVFXParticleRegistry.PIXEL_PARTICLE)
                        .setScaleData(GenericParticleData.create(0.05f* Random.create().nextFloat(), 0).build())
                        .setTransparencyData(GenericParticleData.create(0.75f, 0f).build())
                        .setFullBrightLighting()
                        .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((world.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                        .setLifetime(20)
                        .setMotion(motion.multiply(BowItem.getPullProgress(item.getMaxUseTime(stack) - remainingUseTicks)*0.5))
                        .disableNoClip()
                        .setColorData(ColorParticleData.create(new Color(88, 255-Random.create().nextBetween(0,100), 241), new Color(44, 109, 193+Random.create().nextBetween(0,20))).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                        .spawn(world,spawnPos.getX(),spawnPos.getY(),spawnPos.getZ());
            }
            float f;

            if ((double)(f = BowItem.getPullProgress(item.getMaxUseTime(stack) - remainingUseTicks)) > 0.5) {
                Vec3d direction = MathHelper.determineDirection(user,user.getPitch(20),user.getYaw(20),0.0f,f/2f,1.0f,user.getRandom());

                pos=pos.add(direction);

                WorldParticleBuilder.create(FancyVFXParticleRegistry.RING_PARTICLE)
                        .setScaleData(GenericParticleData.create(f).build())
                        .setTransparencyData(GenericParticleData.create(0.8f, 0f).build())
                        .setFullBrightLighting()
                        .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((world.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                        .setLifetime(1)
                        .setBehavior(new DirectionalBehaviorComponent(direction))
                        .disableNoClip()
                        .setColorData(ColorParticleData.create(new Color(115, 206, 255), new Color(162, 201, 251)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                        .spawn(world,pos.getX(),pos.getY(),pos.getZ());
            }
        }
    }
}
