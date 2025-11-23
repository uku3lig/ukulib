package net.uku3lig.ukulib.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.uku3lig.ukulib.config.option.CyclingOption;
import net.uku3lig.ukulib.config.option.StringTranslatable;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * An enum used to represent various positions.
 * The actual position is implementation specific.
 */
@Getter
@AllArgsConstructor
public enum Position implements StringTranslatable {
    /**
     * The top left of the screen.
     */
    TOP_LEFT("top_left", "ukulib.position.topLeft"),
    /**
     * The top right of the screen.
     */
    TOP_RIGHT("top_right", "ukulib.position.topRight"),
    /**
     * The bottom left of the screen.
     */
    BOTTOM_LEFT("bottom_left", "ukulib.position.bottomLeft"),
    /**
     * The bottom right of the screen.
     */
    BOTTOM_RIGHT("bottom_right", "ukulib.position.bottomRight"),
    /**
     * Above the experience bar, usually between the health and hunger bars.
     */
    MIDDLE("middle", "ukulib.position.middle"),
    ;

    /**
     * Translation key for the position
     */
    public static final String KEY = "ukulib.position";

    private final String name;
    private final String translationKey;

    /**
     * Checks if the position is on the right of the screen.
     *
     * @return <code>true</code> if the position is on the right of the screen
     */
    public boolean isRight() {
        return Arrays.asList(TOP_RIGHT, BOTTOM_RIGHT).contains(this);
    }

    /**
     * Checks if the position is at the bottom of the screen.
     *
     * @return <code>true</code> if the position is at the bottom of the screen
     */
    public boolean isBottom() {
        return Arrays.asList(BOTTOM_LEFT, BOTTOM_RIGHT).contains(this);
    }

    /**
     * Creates a {@link CyclingOption} that allows to choose between all of this enum's values.
     *
     * @param initialValue The initial
     * @param setter The action to be performed when the value changes
     * @return The generated option
     * @see CyclingOption#ofTranslatableEnum(String, Class, Enum, Consumer) 
     */
    public static CyclingOption<Position> getOption(Position initialValue, Consumer<Position> setter) {
        return CyclingOption.ofTranslatableEnum(KEY, Position.class, initialValue, setter);
    }

    /**
     * Creates a {@link CyclingOption} that allows to choose between all the <code>allowedValues</code>.
     *
     * @param allowedValues The values the option will cycle through
     * @param initialValue  The initial value
     * @param setter        The action to be performed when the value changes
     * @return The generated option
     * @see CyclingOption#ofTranslatable(String, Collection, StringTranslatable, Consumer)
     */
    public static CyclingOption<Position> getOption(Collection<Position> allowedValues, Position initialValue, Consumer<Position> setter) {
        return CyclingOption.ofTranslatable(KEY, allowedValues, initialValue, setter);
    }
}
