package maxmag_change.fancyvfx;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public class FancyVFXSounds {

    /* SURFACE */
    public static final SoundEvent WATER_SURFACE_EXPLOSION = registerSound("waterblast");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = new Identifier(FancyVFX.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void initialize() {
        FancyVFX.LOGGER.info("Registering Sounds for " + FancyVFX.MOD_ID);
    }
}
