package net.uku3lig.ukulib.config;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public abstract class AbstractConfig {
    protected final File file;

    public AbstractConfig readConfig() {
        if (!file.exists()) {
            try {
                defaultConfig().writeConfig();
            } catch (IOException e) {
                log.warn("Could not write default configuration file", e);
            }
        } else {
            try {
                return new TomlMapper().readValue(file, getClass());
            } catch (IOException e) {
                log.warn("Could not read configuration file", e);
            }
        }
        return defaultConfig();
    }

    protected abstract AbstractConfig defaultConfig();

    public void writeConfig() throws IOException {
        new TomlMapper().writeValue(file, this);
    }
}
