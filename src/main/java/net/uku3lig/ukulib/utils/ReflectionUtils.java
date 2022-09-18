package net.uku3lig.ukulib.utils;

import lombok.extern.slf4j.Slf4j;
import net.uku3lig.ukulib.config.IConfig;

import java.lang.reflect.Constructor;
import java.util.NoSuchElementException;

@Slf4j
public class ReflectionUtils {
    private ReflectionUtils() {}

    public static <T extends IConfig<T>> T newInstance(Class<T> klass) {
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
