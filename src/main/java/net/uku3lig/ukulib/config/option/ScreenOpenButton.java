package net.uku3lig.ukulib.config.option;

import lombok.AllArgsConstructor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.function.UnaryOperator;

@AllArgsConstructor
public class ScreenOpenButton implements ButtonCreator {
    private final String key;
    private final UnaryOperator<Screen> opener;

    @Override
    public ClickableWidget createButton(int x, int y, int width, int height) {
        return ButtonWidget.builder(Text.translatable(key), b -> openScreen())
                .dimensions(x, y, width, height)
                .build();
    }

    private void openScreen() {
        MinecraftClient.getInstance().setScreen(opener.apply(MinecraftClient.getInstance().currentScreen));
    }
}
