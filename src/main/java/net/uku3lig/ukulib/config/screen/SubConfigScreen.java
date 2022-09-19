package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.IConfig;
import net.uku3lig.ukulib.config.ISubConfig;
import net.uku3lig.ukulib.config.serialization.SubConfigSerializer;

import java.util.function.Function;

public abstract class SubConfigScreen<T extends ISubConfig<T, P>, P extends IConfig<P>> extends AbstractConfigScreen<T> {
    private final ConfigManager<P> parentManager;

    protected SubConfigScreen(Screen parent, Text title, ConfigManager<P> manager, Function<P, T> getter) {
        super(parent, title, new ConfigManager<>(new SubConfigSerializer<>(manager, getter)));
        this.parentManager = manager;
    }

    @Override
    public void removed() {
        parentManager.saveConfig();
    }
}
