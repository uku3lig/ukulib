package net.uku3lig.ukulib.config.option;

import net.minecraft.client.gui.widget.ClickableWidget;

@FunctionalInterface
public interface ButtonCreator {
    ClickableWidget createButton(int x, int y, int width, int height);
}
