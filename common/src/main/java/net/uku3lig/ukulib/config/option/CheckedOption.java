package net.uku3lig.ukulib.config.option;

/**
 * An option that can be checked for validity.
 */
public interface CheckedOption {
    /**
     * Checks if the option is valid.
     *
     * @return true if the option is valid, false otherwise
     */
    boolean isValid();
}
