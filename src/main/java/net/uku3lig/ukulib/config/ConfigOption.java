package net.uku3lig.ukulib.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.resource.language.TranslationStorage;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@AllArgsConstructor
public class ConfigOption<T> {
    private static final TranslationStorage tl = TranslationStorage.getInstance();
    public static final Function<Boolean, String> boolToString = b -> tl.translate(Boolean.TRUE.equals(b) ? "options.on" : "options.off");
    public static final Function<Float, String> floatToString = f -> f == 0 ? tl.translate("options.off") : (int)(f * 100.0F) + "%";

    private final int id;
    private final String text;
    private final boolean isSlider;
    private final boolean isToggle;
    private final Supplier<T> getter;
    private final Function<T, String> textGetter;
    private final Consumer<Float> setter;

    public String getTranslatedValue() {
        return this.text + ": " + this.textGetter.apply(this.getter.get());
    }
}
