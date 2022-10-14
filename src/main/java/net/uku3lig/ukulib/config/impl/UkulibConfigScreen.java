package net.uku3lig.ukulib.config.impl;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.OrderableTooltip;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.api.UkulibAPI;

import java.util.List;
import java.util.Optional;

/**
 * Ukulib's config screen. Shows all the mods that have integrated with Ukulib.
 */
public final class UkulibConfigScreen extends GameOptionsScreen {
    private EntrypointList entrypointList;

    /**
     * Creates a config screen.
     * @param parent The parent screen
     */
    public UkulibConfigScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.of("Ukulib Config"));
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void init() {
        super.init();

        List<EntrypointContainer<UkulibAPI>> containers = FabricLoader.getInstance().getEntrypointContainers("ukulib", UkulibAPI.class);
        entrypointList = new EntrypointList(this.client, this.width, this.height, 32, this.height - 32, 25);
        entrypointList.addAll(containers, this);
        this.addChild(entrypointList);

        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, button -> this.client.openScreen(this.parent)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.entrypointList.render(matrices, mouseX, mouseY, delta);
        DrawableHelper.drawCenteredText(matrices, textRenderer, title, width / 2, 20, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);

        Optional<List<OrderedText>> list = getHoveredButtonTooltip(mouseX, mouseY);
        list.ifPresent(orderedTexts -> this.renderOrderedTooltip(matrices, orderedTexts, mouseX, mouseY));
    }

    private Optional<List<OrderedText>> getHoveredButtonTooltip(int mouseX, int mouseY) {
        Optional<ClickableWidget> optional = entrypointList.getHoveredButton(mouseX, mouseY);
        return optional.isPresent() && optional.get() instanceof OrderableTooltip ? ((OrderableTooltip) optional.get()).getOrderedTooltip() : Optional.empty();
    }
}
