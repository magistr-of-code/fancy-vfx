package maxmag_change.fancyvfx.particles;

import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.world.ClientWorld;
import team.lodestar.lodestone.systems.particle.world.FrameSetParticle;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

public class AnimatedParticle extends FrameSetParticle {
    public AnimatedParticle(ClientWorld world, WorldParticleOptions data, FabricSpriteProviderImpl spriteSet, double x, double y, double z, double xd, double yd, double zd) {
        super(world, data, spriteSet, x, y, z, xd, yd, zd);

        this.setSprite(this.spriteSet.getSprite(world.random));
    }

    @Override
    public void tick() {
        this.setSpriteForAge(this.spriteSet);
        super.tick();
    }
}
