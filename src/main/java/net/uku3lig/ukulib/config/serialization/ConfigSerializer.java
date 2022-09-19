package net.uku3lig.ukulib.config.serialization;

public interface ConfigSerializer<T> {
    T deserialize();

    void serialize(T config);
}
