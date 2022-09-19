package net.uku3lig.ukulib.config;

import java.io.Serializable;

public interface IConfig<T extends IConfig<T>> extends Serializable {
    T defaultConfig();
}
