package maxmag_change.fancyvfx;

import eu.midnightdust.lib.config.MidnightConfig;

public class FancyVFXConfig extends MidnightConfig {
    public static final String visuals = "visuals";

    @Comment(category = visuals, centered = true) public static Comment screenShakeEffects;
    @Entry(category = visuals, min = 0, max = 5, isSlider = true)
    public static float screenShakeIntensity = 1;
    @Entry(category = visuals)
    public static boolean lightningScreenShake = true;
    @Entry(category = visuals)
    public static boolean explosionScreenShake = true;

    @Comment(category = visuals, centered = true) public static Comment improvedEffects;
    @Entry(category = visuals)
    public static boolean improvedExplosions = true;
    @Entry(category = visuals)
    public static float explosionMultiplier = 1F;
    @Entry(category = visuals)
    public static boolean underWaterExplosions = true;
    @Entry(category = visuals)
    public static boolean improvedLightning = true;
    @Entry(category = visuals)
    public static BowOptions improvedBow = BowOptions.ENABLE;
    @Entry(category = visuals)
    public static boolean damageVFX = true;
    @Entry(category = visuals)
    public static boolean projectileCollision = true;
    @Entry(category = visuals)
    public static boolean statusEffectVFX = true;

    @Comment(category = visuals, centered = true) public static Comment lightAuraC;
    @Entry(category = visuals)
    public static boolean lightAura = false;
    @Entry(category = visuals)
    public static boolean connectingLightAura = true;
    @Entry(category = visuals, min = 0, max = 40, isSlider = true)
    public static int maxConnectingLightAura = 3;

    @Comment(category = visuals, centered = true) public static Comment trails;
    @Entry(category = visuals)
    public static boolean expTrail = true;
    @Entry(category = visuals)
    public static boolean arrowTrail = true;

    @Comment(category = visuals, centered = true) public static Comment smoke;
    @Entry(category = visuals)
    public static boolean smokeFromLightnings = true;
    @Entry(category = visuals)
    public static boolean smokeFromExplosions = true;
    @Entry(category = visuals)
    public static boolean smokeFromSprinting = true;
    @Entry(category = visuals, min = 0,max = 2, isSlider = true)
    public static float smokeFromSprintingSizeMultiplier = 1;

    public enum BowOptions {
        ENABLE, WHEN_FIRED, DISABLE
    }
}
