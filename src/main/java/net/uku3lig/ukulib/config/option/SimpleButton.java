package net.uku3lig.ukulib.config.option;

import lombok.AllArgsConstructor;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

@AllArgsConstructor
public class SimpleButton implements ButtonCreator {
    protected final Text text;
    protected final ButtonWidget.PressAction action;

    public SimpleButton(String key, ButtonWidget.PressAction action) {
        this(Text.translatable(key), action);
    }

    @Override
    public ClickableWidget createButton(int x, int y, int width, int height) {
        return ButtonWidget.builder(text, action)
                .dimensions(x, y, width, height)
                .build();
    }
}
