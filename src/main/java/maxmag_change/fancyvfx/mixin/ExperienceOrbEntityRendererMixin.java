package maxmag_change.fancyvfx.mixin;

import maxmag_change.fancyvfx.FancyVFX;
import maxmag_change.fancyvfx.FancyVFXConfig;
import maxmag_change.fancyvfx.util.PositionTrackedEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ExperienceOrbEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;
import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;

import java.awt.*;
import java.util.List;

@Mixin(ExperienceOrbEntityRenderer.class)
public abstract class ExperienceOrbEntityRendererMixin extends EntityRenderer<ExperienceOrbEntity> {
    private static final RenderLayer TRAIL_TYPE = LodestoneRenderTypeRegistry.ADDITIVE_TEXTURE_TRIANGLE.apply(RenderTypeToken.createCachedToken(new Identifier(FancyVFX.MOD_ID,"textures/vfx/light_trail.png")));

    public RenderLayer getTrailRenderType() {
        return TRAIL_TYPE;
    }

    protected ExperienceOrbEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "render(Lnet/minecraft/entity/ExperienceOrbEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",at = @At("TAIL"))
    public void render(ExperienceOrbEntity experienceOrbEntity, float entityYaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
        if (FancyVFXConfig.expTrail) {
            matrixStack.push();
            List<TrailPoint> positions = ((PositionTrackedEntity) experienceOrbEntity).getPastPositions();
            VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setRenderType(getTrailRenderType());

            float size = 0.2f;
            float alpha = 1f;

            float x = (float) MathHelper.lerp(tickDelta, experienceOrbEntity.prevX, experienceOrbEntity.getX());
            float y = (float) MathHelper.lerp(tickDelta, experienceOrbEntity.prevY, experienceOrbEntity.getY());
            float z = (float) MathHelper.lerp(tickDelta, experienceOrbEntity.prevZ, experienceOrbEntity.getZ());

            matrixStack.translate(-x, -y, -z);
            builder.setColor(new Color(23, 248, 58))
                    .setAlpha(alpha)
                    .renderTrail(matrixStack,
                            positions,
                            f -> MathHelper.sqrt(f) * size,
                            f -> builder.setAlpha((float) Math.cbrt(Math.max(0, (alpha * f) - 0.1f)))
                    )
                    .setAlpha(alpha)
                    .renderTrail(matrixStack,
                            positions,
                            f -> (MathHelper.sqrt(f) * size) / 1.5f,
                            f -> builder.setAlpha((float) Math.cbrt(Math.max(0, (((alpha * f) / 1.5f) - 0.1f))))
                    );

            matrixStack.pop();
        }
    }
}
