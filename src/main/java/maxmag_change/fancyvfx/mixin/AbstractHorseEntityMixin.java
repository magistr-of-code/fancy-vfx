package maxmag_change.fancyvfx.mixin;

import maxmag_change.fancyvfx.FancyVFXConfig;
import maxmag_change.fancyvfx.particles.FancyVFXParticleRegistry;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;

import java.awt.*;

@Mixin(AbstractHorseEntity.class)
public class AbstractHorseEntityMixin{

    @Inject(method = "tickControlled(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/Vec3d;)V",at = @At("HEAD"))
    protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput, CallbackInfo ci){
        if (FancyVFXConfig.smokeFromSprinting) {

            float sizeMultiplier = FancyVFXConfig.smokeFromSprintingSizeMultiplier;

            AbstractHorseEntity entity = ((AbstractHorseEntity) (Object) this);

            Random random = Random.create();
            if (entity.getVelocity().x>0.15||entity.getVelocity().z>0.15||entity.getVelocity().x<-0.15||entity.getVelocity().z<-0.15){
                EntityDimensions dimensions = entity.getDimensions(entity.getPose());

                BlockPos blockPos = entity.getLandingPos();
                BlockState blockState = entity.getWorld().getBlockState(blockPos);
                if (blockState.getRenderType() != BlockRenderType.INVISIBLE) {
                    Vec3d vec3d = entity.getVelocity();
                    BlockPos blockPos2 = entity.getBlockPos();
                    double d = entity.getX() + (random.nextDouble() - 0.5) * (double)dimensions.width;
                    double e = entity.getZ() + (random.nextDouble() - 0.5) * (double)dimensions.height;
                    if (blockPos2.getX() != blockPos.getX()) {
                        d = MathHelper.clamp(d, (double)blockPos.getX(), (double)blockPos.getX() + 1.0);
                    }

                    if (blockPos2.getZ() != blockPos.getZ()) {
                        e = MathHelper.clamp(e, (double)blockPos.getZ(), (double)blockPos.getZ() + 1.0);
                    }

                    WorldParticleBuilder.create(FancyVFXParticleRegistry.SMOKE_PARTICLE)
                            .setScaleData(GenericParticleData.create(0.7f*sizeMultiplier,1.4f*sizeMultiplier,0.4f*sizeMultiplier).setEasing(Easing.CUBIC_IN,Easing.BOUNCE_OUT).setCoefficient(3f).build())
                            .setTransparencyData(GenericParticleData.create(0.8f, 0.5f).build())
                            .setNaturalLighting()
                            .setColorData(ColorParticleData.create(new Color(255, 253, 253,255).darker(), new Color(136, 136, 136, 255).darker()).setEasing(Easing.BOUNCE_IN_OUT).build())
                            .setLifetime(35)
                            .setRandomOffset(0.5,0.1,0.5)
                            .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT)
                            .setMotion(vec3d.x * -0.5, 0.15, vec3d.z * -0.5)
                            .setGravityStrength(0.5f)
                            .disableNoClip()
                            .spawn(entity.getWorld(), d, entity.getY()+0.2, e);
                }
            }
        }
    }
}
