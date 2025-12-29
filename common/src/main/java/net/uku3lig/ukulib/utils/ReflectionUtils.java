package net.uku3lig.ukulib.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.NoSuchElementException;

/**
 * Utility class for reflection
 */
@Slf4j
public class ReflectionUtils {
    private ReflectionUtils() {
    }

    /**
     * Creates an instance of a given config class. Only works if the class has a public, no-arg constructor.
     *
     * @param klass The class to be instantiated
     * @param <T>   The type of the class
     * @return An instance of the class
     * @see net.uku3lig.ukulib.config.ConfigManager#createDefault(Class, String) ConfigManager#create(Class, String)
     */
    public static <T extends Serializable> T newInstance(Class<T> klass) {
        try {
            Constructor<T> constructor = klass.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InstantiationException e) {
            log.error("{} does not have a public no-arg constructor!", klass.getName());
            throw new NoSuchElementException(e);
        } catch (Exception e) {
            log.error("Could not instantiate class {}", klass.getName());
            throw new IllegalArgumentException(e);
        }
    }
}
