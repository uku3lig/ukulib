package net.uku3lig.ukulib.config;

import com.mojang.serialization.Codec;
import lombok.Getter;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.util.TranslatableOption;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter // lombok generates the methods that have to be implemented
public enum Position implements TranslatableOption {
    TOP_LEFT(0, "ukulib.position.topLeft"),
    TOP_RIGHT(1, "ukulib.position.topRight"),
    BOTTOM_LEFT(2, "ukulib.position.bottomLeft"),
    BOTTOM_RIGHT(3, "ukulib.position.bottomRight"),
    MIDDLE(4, "ukulib.position.middle"),
    ;

    private final int id;
    private final String translationKey;

    Position(int id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
    }

    public boolean isRight() {
        return Arrays.asList(TOP_RIGHT, BOTTOM_RIGHT).contains(this);
    }

    public boolean isBottom() {
        return Arrays.asList(BOTTOM_LEFT, BOTTOM_RIGHT).contains(this);
    }

    public static SimpleOption<Position> getOption(Supplier<Position> getter, Consumer<Position> setter) {
        return new SimpleOption<>("ukulib.position", SimpleOption.emptyTooltip(), SimpleOption.enumValueText(),
                new SimpleOption.PotentialValuesBasedCallbacks<>(Arrays.asList(Position.values()), Codec.STRING.xmap(Position::valueOf, Position::name)),
                getter.get(), setter);
    }
}
