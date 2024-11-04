package maxmag_change.fancyvfx;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public class FancyVFXSounds {
    protected static final List<SoundEvent> SOUND_EVENTS = new LinkedList<>();

    /* SURFACE */
    public static final SoundEvent WATER_SURFACE_EXPLOSION = create("waterblast");

    protected static SoundEvent create(String name) {
        SoundEvent soundEvent = SoundEvent.of(new Identifier(FancyVFX.MOD_ID,name));
        SOUND_EVENTS.add(soundEvent);
        return soundEvent;
    }

    public static void initialize() {
        SOUND_EVENTS.forEach(soundEvent -> Registry.register(Registries.SOUND_EVENT, soundEvent.getId(), soundEvent));
    }
}
