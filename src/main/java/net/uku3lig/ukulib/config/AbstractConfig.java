package net.uku3lig.ukulib.config;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public abstract class AbstractConfig {
    protected final Logger logger = LogManager.getLogger(getClass());
    protected final File file;

    protected AbstractConfig(File file) {
        this.file = file;
    }

    public AbstractConfig readConfig() {
        if (!file.exists()) {
            try {
                defaultConfig().writeConfig();
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

    public void writeConfig() throws IOException {
        new TomlMapper().writeValue(file, this);
    }
}
