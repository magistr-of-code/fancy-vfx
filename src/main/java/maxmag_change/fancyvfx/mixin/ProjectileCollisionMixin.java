package maxmag_change.fancyvfx.mixin;

import maxmag_change.fancyvfx.FancyVFXConfig;
import maxmag_change.fancyvfx.particles.FancyVFXParticleRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import java.awt.*;

@Environment(EnvType.CLIENT)
@Mixin(ProjectileEntity.class)
public class ProjectileCollisionMixin {

    @Inject(method = "onCollision",at = @At("HEAD"))
    protected void onCollision(HitResult hitResult, CallbackInfo ci) {
        if (FancyVFXConfig.projectileCollision) {
            ProjectileEntity projectile = (ProjectileEntity) (Object) this;

            HitResult.Type type = hitResult.getType();

            World world = projectile.getWorld();

            Vec3d pos = projectile.getPos();

            if (type!=HitResult.Type.MISS){

                if (projectile instanceof EnderPearlEntity){
                    for (int i = 0; i <= 16; i++) {
                        WorldParticleBuilder.create(FancyVFXParticleRegistry.PIXEL_PARTICLE)
                                .setScaleData(GenericParticleData.create(0.1f*Random.create().nextFloat(), 0).build())
                                .setTransparencyData(GenericParticleData.create(0.75f, 0f).build())
                                .setFullBrightLighting()
                                .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((world.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                                .setLifetime(20)
                                .setRandomOffset(0.5)
                                .setMotion(projectile.getVelocity().multiply(-0.2).multiply(Random.create().nextFloat()))
                                .setGravityStrength(0.5f)
                                .disableNoClip()
                                .setColorData(ColorParticleData.create(new Color(102 + Random.create().nextBetween(0, 40), 0, 255), new Color(142, 3, 255)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                                .createCircle(world, pos.getX(), pos.getY(), pos.getZ(), 1, 0, 360)
                                .surroundVoxelShape(world, BlockPos.ofFloored(projectile.getPos()), VoxelShapes.cuboid(4, 0, 4, 12, 32, 12), 20);
                    }
                }

                for (int i = 0; i <= 16; i++){
                    WorldParticleBuilder.create(FancyVFXParticleRegistry.PIXEL_PARTICLE)
                            .setScaleData(GenericParticleData.create(0.1f* Random.create().nextFloat(), 0).build())
                            .setTransparencyData(GenericParticleData.create(0.75f, 0f).build())
                            .setFullBrightLighting()
                            .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((world.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                            .setLifetime(20)
                            .setRandomOffset(0.5)
                            .setMotion(projectile.getVelocity().multiply(-0.2))
                            .addMotion((double) Random.create().nextBetween(-100, 100) /500, (double) Random.create().nextBetween(-100, 100) /500, (double) Random.create().nextBetween(-100, 100) /500)
                            .setGravityStrength(0.5f)
                            .disableNoClip()
                            .setColorData(ColorParticleData.create(new Color(255, 99+Random.create().nextBetween(0,80), 0), new Color(255, 178, 3, 255)).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                            .spawn(world, pos.getX(),pos.getY(),pos.getZ());
                }
            }
        }
    }
}
