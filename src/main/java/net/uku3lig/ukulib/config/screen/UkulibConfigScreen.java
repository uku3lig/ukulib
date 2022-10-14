package net.uku3lig.ukulib.config.screen;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.api.UkulibAPI;

import java.util.List;
import java.util.stream.IntStream;

public final class UkulibConfigScreen extends GameOptionsScreen {
    public UkulibConfigScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.of("Ukulib Config"));
    }

    @Override
    protected void init() {
        super.init();
        if (client == null) return;

        List<EntrypointContainer<UkulibAPI>> containers = FabricLoader.getInstance().getEntrypointContainers("ukulib", UkulibAPI.class);
        IntStream.range(0, containers.size()).forEach(i -> {
            EntrypointContainer<UkulibAPI> container = containers.get(i);
            UkulibAPI api = container.getEntrypoint();
            addDrawableChild(new ButtonWidget(width / 2 - 155 + i % 2 * 160, height / 6 + 24 * (i >> 1), 150, 20,
                    Text.of(container.getProvider().getMetadata().getName()), button -> client.setScreen(api.getConfigScreen())));
        });

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 168, 200, 20, ScreenTexts.DONE, button -> this.client.setScreen(this.parent)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, textRenderer, title, width / 2, 20, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
