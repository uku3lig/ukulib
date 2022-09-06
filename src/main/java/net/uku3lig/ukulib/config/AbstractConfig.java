package net.uku3lig.ukulib.config;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public abstract class AbstractConfig {
    protected final Logger logger = LogManager.getLogger(getClass());

    public AbstractConfig readConfig(File file) {
        if (!file.exists()) {
            try {
                defaultConfig().writeConfig(file);
            } catch (IOException e) {
                logger.warn("Could not write default configuration file", e);
            }
        } else {
            try {
                return new TomlMapper().readValue(file, getClass());
            } catch (IOException e) {
                logger.warn("Could not read configuration file", e);
            }
        }
        return defaultConfig();
    }

    protected abstract AbstractConfig defaultConfig();

    public void writeConfig(File file) throws IOException {
        new TomlMapper().writeValue(file, this);
    }
}
