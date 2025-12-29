package net.uku3lig.ukulib.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ServiceLoader;

@Slf4j(topic = "ukulib (services)")
public class Services {
    // This code is used to load a service for the current environment. Your implementation of the service must be defined
    // manually by including a text file in META-INF/services named with the fully qualified class name of the service.
    // Inside the file you should write the fully qualified class name of the implementation to load for the platform.
    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        log.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
