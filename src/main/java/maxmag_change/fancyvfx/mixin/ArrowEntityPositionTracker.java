package maxmag_change.fancyvfx.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import maxmag_change.fancyvfx.util.PositionTrackedEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.util.List;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityPositionTracker extends PersistentProjectileEntity implements PositionTrackedEntity {
    @Unique
    public final TrailPointBuilder trailPointBuilder = TrailPointBuilder.create(20);

    protected ArrowEntityPositionTracker(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }


    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (!((ArrowEntity)(Object)this).isOnGround()){
            Vec3d position = this.getCameraPosVec(MinecraftClient.getInstance().getTickDelta()).add(0, -.1f, 0f);
            trailPointBuilder.addTrailPoint(position);
            trailPointBuilder.tickTrailPoints();
        }
    }

    @Override
    public List<TrailPoint> getPastPositions() {
        return trailPointBuilder.getTrailPoints();
    }
}
