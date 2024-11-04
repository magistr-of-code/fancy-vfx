package maxmag_change.fancyvfx.util;

import net.minecraft.world.World;
import org.ladysnake.effective.core.Effective;
import org.ladysnake.effective.core.particle.contracts.SplashParticleInitialData;

public class EffectiveCreateSplash {
    public static void create(World world,float width,float velocityY,double x, double y, double z){
        world.addParticle(Effective.SPLASH.setData(new SplashParticleInitialData(width,velocityY)), x, y, z, 0, 0, 0);
    }
}
