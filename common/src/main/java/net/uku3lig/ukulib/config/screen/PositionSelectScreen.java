package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.uku3lig.ukulib.config.ConfigManager;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.function.BiConsumer;

/**
 * Simple screen used to select a custom position on the screen for an element.
 */
public abstract class PositionSelectScreen extends CloseableScreen {
    private int x;
    private int y;
    private final ConfigManager<?> manager;
    private final BiConsumer<Integer, Integer> callback;

    private Button doneButton;
    private Button defaultButton;

    /**
     * Creates a position select screen. If any of <code>x</code> or <code>y</code> is passed as <code>-1</code>,
     * the screen will use the default values.
     *
     * @param key      The translation key of the title
     * @param parent   The parent screen
     * @param x        The initial x position
     * @param y        The initial y position
     * @param manager  The config manager, used to save the config
     * @param callback The action to be performed when the position is changed
     */
    protected PositionSelectScreen(String key, Screen parent, int x, int y, ConfigManager<?> manager, BiConsumer<Integer, Integer> callback) {
        super(key, parent);
        this.x = x;
        this.y = y;
        this.manager = manager;
        this.callback = callback;
    }

    @Override
    protected void init() {
        this.doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, _ -> onClose())
                .size(50, 20)
                .build());

        this.defaultButton = this.addRenderableWidget(Button.builder(Component.translatable("ukulib.option.default"), _ -> {
                    this.x = -1;
                    this.y = -1;
                })
                .size(50, 20)
                .build());

        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        this.doneButton.setPosition(this.width - 60, 10);
        this.defaultButton.setPosition(this.width - 60, 35);
    }

    @Override
    public void removed() {
        callback.accept(x, y);
        manager.saveConfig();
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent click, boolean doubleClick) {
        if (!super.mouseClicked(click, doubleClick)) {
            this.x = (int) Mth.clamp(click.x(), 0, this.width);
            this.y = (int) Mth.clamp(click.y(), 0, this.height);
        }

        return true;
    }

    @Override
    public boolean keyPressed(@NotNull KeyEvent input) {
        if (!super.keyPressed(input)) {
            int amount = input.modifiers() == GLFW.GLFW_MOD_SHIFT ? 10 : 1;

            switch (input.key()) {
                case GLFW.GLFW_KEY_RIGHT -> x += amount;
                case GLFW.GLFW_KEY_LEFT -> x -= amount;
                case GLFW.GLFW_KEY_DOWN -> y += amount;
                case GLFW.GLFW_KEY_UP -> y -= amount;
                default -> {
                    return false;
                }
            }

            // make sure they are within bounds
            this.x = Mth.clamp(x, 0, this.width);
            this.y = Mth.clamp(y, 0, this.height);
        }

        return true;
    }

    /**
     * Draws the screen and all the components in it.
     * Called in {@link PositionSelectScreen#render(GuiGraphics, int, int, float)}.
     *
     * @param graphics The drawable helper
     * @param mouseX   The x position of the mouse
     * @param mouseY   The y position of the mouse
     * @param delta    The delta time
     * @param x        The x position of the element
     * @param y        The y position of the element
     */
    protected abstract void draw(GuiGraphics graphics, int mouseX, int mouseY, float delta, int x, int y);

    /**
     * Draws the screen and all the components in it, in their default position.
     * Called in {@link PositionSelectScreen#render(GuiGraphics, int, int, float)}.
     *
     * @param graphics The drawable helper
     * @param mouseX   The x position of the mouse
     * @param mouseY   The y position of the mouse
     * @param delta    The delta time
     */
    protected void drawDefault(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        draw(graphics, mouseX, mouseY, delta, 5, 5);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFFFF);
        graphics.drawCenteredString(this.font, Component.translatable("ukulib.position.desc"), this.width / 2, this.height / 2 - 80, 0xFFFFFF55);
        graphics.drawCenteredString(this.font, Component.translatable("ukulib.position.desc.move"), this.width / 2, this.height / 2 - 65, 0xFFFFFFFF);

        if (x == -1 || y == -1) {
            drawDefault(graphics, mouseX, mouseY, delta);
        } else {
            draw(graphics, mouseX, mouseY, delta, x, y);
        }
    }
}
