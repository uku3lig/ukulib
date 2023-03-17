package net.uku3lig.ukulib.config.option;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class TranslatableButton extends SimpleButton {
    public TranslatableButton(String key, ButtonWidget.PressAction action) {
        super(Text.translatable(key), action);
    }

    public TranslatableButton(String key) {
        super(Text.translatable(key));
    }
}
