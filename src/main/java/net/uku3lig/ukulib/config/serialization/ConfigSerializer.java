package net.uku3lig.ukulib.config.serialization;

/**
 * Manages the serialization of a configuration file.
 * @param <T> The type of the config
 */
public interface ConfigSerializer<T> {
    /**
     * Deserializes the config.
     * @return The deserialized config
     */
    T deserialize();

    /**
     * Serializes the config.
     * @param config The config to be serialized.
     */
    void serialize(T config);
}
