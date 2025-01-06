package maxmag_change.fancyvfx.mixin;

import maxmag_change.fancyvfx.FancyVFX;
import maxmag_change.fancyvfx.FancyVFXConfig;
import maxmag_change.fancyvfx.particles.ColoredParticleInitialData;
import maxmag_change.fancyvfx.util.PositionTrackedEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
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

@Mixin(ProjectileEntityRenderer.class)
public abstract class ArrowEntityRendererMixin<T extends PersistentProjectileEntity> extends EntityRenderer<T> {
    private static final RenderLayer TRAIL_TYPE = LodestoneRenderTypeRegistry.ADDITIVE_TEXTURE_TRIANGLE.apply(RenderTypeToken.createCachedToken(new Identifier(FancyVFX.MOD_ID,"textures/vfx/light_trail.png")));

    public RenderLayer getTrailRenderType() {
        return TRAIL_TYPE;
    }

    protected ArrowEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "render(Lnet/minecraft/entity/projectile/PersistentProjectileEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
    public void render(T entity, float entityYaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
        // new render
        if (entity instanceof ArrowEntity arrow && FancyVFXConfig.arrowTrail){

            matrixStack.push();
            List<TrailPoint> positions = ((PositionTrackedEntity) arrow).getPastPositions();
            VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setRenderType(getTrailRenderType()).setLight(entity.getWorld().getLightLevel(entity.getBlockPos()));

            float size = 0.15f;
            float alpha = 1f;

            float x = (float) MathHelper.lerp(tickDelta, arrow.prevX, arrow.getX());
            float y = (float) MathHelper.lerp(tickDelta, arrow.prevY, arrow.getY());
            float z = (float) MathHelper.lerp(tickDelta, arrow.prevZ, arrow.getZ());

            matrixStack.translate(-x, -y, -z);
            builder.setColor(new Color(4, 244, 222))
                    .setAlpha(alpha)
                    .renderTrail(matrixStack,
                            positions,
                            f -> MathHelper.sqrt(f) * size,
                            f -> builder.setAlpha((float) Math.cbrt(Math.max(0, (alpha * f) - 0.1f)))
                    )
                    .setLight(15728880)
                    .renderTrail(matrixStack,
                            positions,
                            f -> (MathHelper.sqrt(f) * size) / 1.5f,
                            f -> builder.setAlpha((float) Math.cbrt(Math.max(0, (((alpha * f) / 1.5f) - 0.1f))))
                    );

            matrixStack.pop();
        }
    }
}
