package net.uku3lig.ukulib.config;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public abstract class AbstractConfig {
    private static final Gson gson = new Gson();
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
            try (FileReader reader = new FileReader(file)) {
                return gson.fromJson(reader, getClass());
            } catch (IOException e) {
                logger.warn("Could not read config file {}", file);
                return defaultConfig();
            }
        }
    }

    protected abstract AbstractConfig defaultConfig();

    public void writeConfig(File file) throws IOException {
        gson.toJson(this, new FileWriter(file));
    }
}
