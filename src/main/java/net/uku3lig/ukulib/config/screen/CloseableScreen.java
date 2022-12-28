package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * A screen that can be closed, returning to a parent screen.
 */
public abstract class CloseableScreen extends Screen {
    /**
     * The parent screen.
     */
    protected final Screen parent;

    /**
     * Constructs a closeable screen
     * @param title The title of the screen
     * @param parent The parent screen
     */
    protected CloseableScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    public void close() {
        if (this.client != null) this.client.setScreen(parent);
    }
}
