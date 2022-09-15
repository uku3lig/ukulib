package net.uku3lig.ukulib.config;

public interface IConfig<T extends IConfig<T>> {
    T defaultConfig();
}
