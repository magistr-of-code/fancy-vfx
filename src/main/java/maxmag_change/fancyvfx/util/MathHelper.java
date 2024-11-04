package maxmag_change.fancyvfx.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class MathHelper {
    public static boolean inRange(Vec3d value,float min, float max){
        return
                value.getX() > min &&
                value.getX() < max &&
                value.getY() > min &&
                value.getY() < max &&
                value.getZ() > min &&
                value.getZ() < max;
    }

    public static boolean inRange(float value,float min, float max){
        return value > min && value < max;
    }

    public static Vec3d determineDirection(Entity shooter, float pitch, float yaw, float roll, float speed, float divergence, Random random) {
        Vec3d direction;
        float f = -net.minecraft.util.math.MathHelper.sin(yaw * ((float)Math.PI / 180)) * net.minecraft.util.math.MathHelper.cos(pitch * ((float)Math.PI / 180));
        float g = -net.minecraft.util.math.MathHelper.sin((pitch + roll) * ((float)Math.PI / 180));
        float h = net.minecraft.util.math.MathHelper.cos(yaw * ((float)Math.PI / 180)) * net.minecraft.util.math.MathHelper.cos(pitch * ((float)Math.PI / 180));
        direction = new Vec3d(f,g,h).normalize().add(random.nextTriangular(0.0, 0.0172275 * (double)divergence), random.nextTriangular(0.0, 0.0172275 * (double)divergence), random.nextTriangular(0.0, 0.0172275 * (double)divergence)).multiply(speed);
        Vec3d vec3d = shooter.getVelocity();
        direction= direction.add(vec3d.x, shooter.isOnGround() ? 0.0 : vec3d.y, vec3d.z);
        return direction;
    }
}
