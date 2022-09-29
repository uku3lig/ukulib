package net.uku3lig.ukulib.config;

/**
 * An interface which represents a sub-config.
 *
 * @param <T> The class itself
 * @param <P> The parent config
 */
public interface ISubConfig<T extends ISubConfig<T, P>, P extends IConfig<P>> extends IConfig<T> {

}
