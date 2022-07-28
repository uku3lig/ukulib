package net.uku3lig.ukulib.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
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
            return defaultConfig();
        } else {
            return new Toml().read(file).to(getClass());
        }
    }

    protected abstract AbstractConfig defaultConfig();

    public void writeConfig(File file) throws IOException {
        new TomlWriter().write(this, file);
    }
}
