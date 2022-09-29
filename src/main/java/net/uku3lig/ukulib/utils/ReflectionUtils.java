package net.uku3lig.ukulib.utils;

import lombok.extern.log4j.Log4j2;
import net.uku3lig.ukulib.config.IConfig;

import java.lang.reflect.Constructor;

/**
 * Utility class for reflection
 */
@Log4j2
public class ReflectionUtils {
    private ReflectionUtils() {}

    /**
     * Creates an instance of a given config class. Only works if the class has a public, no-arg constructor.
     *
     * @param klass The class to be instantiated
     * @return An instance of the class
     * @param <T> The type of the class
     * @see net.uku3lig.ukulib.config.ConfigManager#create(Class, String) ConfigManager#create(Class, String)
     */
    public static <T extends IConfig<T>> T newInstance(Class<T> klass) {
        try {
            Constructor<T> constructor = klass.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InstantiationException e) {
            log.error("{} does not have a public no-arg constructor!", klass.getName());
            throw new IllegalArgumentException(e);
        } catch (Exception e) {
            log.error("Could not instantiate class {}", klass.getName());
            throw new IllegalArgumentException(e);
        }
    }
}
