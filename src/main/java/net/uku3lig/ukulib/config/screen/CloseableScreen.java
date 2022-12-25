package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class CloseableScreen extends Screen {
    protected final Screen parent;

    protected CloseableScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    public void close() {
        if (this.client != null) this.client.setScreen(parent);
    }
}
