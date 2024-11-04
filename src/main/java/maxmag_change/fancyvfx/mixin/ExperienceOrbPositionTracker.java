package maxmag_change.fancyvfx.mixin;

import maxmag_change.fancyvfx.util.PositionTrackedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.util.List;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbPositionTracker extends Entity implements PositionTrackedEntity {
    @Unique
    public final TrailPointBuilder trailPointBuilder = TrailPointBuilder.create(16);

    public ExperienceOrbPositionTracker(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        trailPointBuilder.addTrailPoint(this.getPos().add(0, .2, 0));
        trailPointBuilder.tickTrailPoints();
    }

    @Override
    public List<TrailPoint> getPastPositions() {
        return trailPointBuilder.getTrailPoints();
    }
}
