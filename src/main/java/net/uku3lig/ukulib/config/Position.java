package net.uku3lig.ukulib.config;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * An enum used to represent various positions.
 * The actual position is implementation specific.
 */
@Getter // lombok generates the methods that have to be implemented
@SuppressWarnings("unused")
public enum Position {
    /**
     * The top left of the screen.
     */
    TOP_LEFT(0, "Top Left"),
    /**
     * The top right of the screen.
     */
    TOP_RIGHT(1, "Top Right"),
    /**
     * The bottom left of the screen.
     */
    BOTTOM_LEFT(2, "Bottom Left"),
    /**
     * The bottom right of the screen.
     */
    BOTTOM_RIGHT(3, "Bottom Right"),
    /**
     * Above the experience bar, usually between the health and hunger bars.
     */
    MIDDLE(4, "Above XP Bar"),
    ;

    private final int id;
    private final String text;

    Position(int id, String text) {
        this.id = id;
        this.text = text;
    }

    /**
     * Checks if the position is on the right of the screen.
     * @return <code>true</code> if the position is on the right of the screen
     */
    public boolean isRight() {
        return Arrays.asList(TOP_RIGHT, BOTTOM_RIGHT).contains(this);
    }

    /**
     * Checks if the position is at the bottom of the screen.
     * @return <code>true</code> if the position is at the bottom of the screen
     */
    public boolean isBottom() {
        return Arrays.asList(BOTTOM_LEFT, BOTTOM_RIGHT).contains(this);
    }

    /**
     * Creates a {@link ConfigOption} that allows to choose between all of this enum's values.
     *
     * @param getter Gets the default value for the option
     * @param setter The action to be performed when the value changes
     * @return The generated option
     * @see Position#getOption(int, Collection, Supplier, Consumer)
     */
    public static ConfigOption<Position> getOption(int id, Supplier<Position> getter, Consumer<Position> setter) {
        return getOption(id, EnumSet.allOf(Position.class), getter, setter);
    }

    /**
     * Creates a {@link ConfigOption} that allows to choose between all the <code>allowedValues</code>.
     *
     * @param allowedValues The values the option will cycle through
     * @param getter Gets the default value for the option
     * @param setter The action to be performed when the value changes
     * @return The generated option
     */
    public static ConfigOption<Position> getOption(int id, Collection<Position> allowedValues, Supplier<Position> getter, Consumer<Position> setter) {
        return new ConfigOption<>(id, "Position", false, false, getter, p -> p.text,
                f -> setter.accept(fromId(getter.get().id + (f.intValue() & allowedValues.size()))));

    }

    private static Position fromId(int id) {
        return Arrays.stream(values()).filter(p -> p.id == id).findFirst().orElse(TOP_LEFT);
    }
}
