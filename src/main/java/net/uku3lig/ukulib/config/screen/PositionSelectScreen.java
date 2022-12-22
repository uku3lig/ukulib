package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.utils.Ukutils;
import org.lwjgl.glfw.GLFW;

import java.util.function.BiConsumer;

/**
 * Simple screen used to select a custom position on the screen for an element.
 */
public abstract class PositionSelectScreen extends Screen {
    private final Screen parent;
    /**
     * The x position of the element
     */
    protected int x;
    /**
     * The y position of the element
     */
    protected int y;
    private final ConfigManager<?> manager;
    private final BiConsumer<Integer, Integer> callback;

    /**
     * Creates a position select screen. If any of <code>x</code> or <code>y</code> is passed as <code>-1</code>,
     * the screen will use the default values.
     *
     * @param parent   The parent screen
     * @param x        The initial x position
     * @param y        The initial y position
     * @param manager  The config manager, used to save the config
     * @param callback The action to be performed when the position is changed
     */
    protected PositionSelectScreen(Screen parent, int x, int y, ConfigManager<?> manager, BiConsumer<Integer, Integer> callback) {
        super(Text.of("Position Select"));
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.manager = manager;
        this.callback = callback;
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, b -> close())
                .dimensions(this.width - 40, 10, 30, 20)
                .build());
        this.addDrawableChild(ButtonWidget.builder(Text.of("Default"), b -> {
                    this.x = -1;
                    this.y = -1;
                })
                .dimensions(this.width - 40, 35, 30, 20)
                .build());
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    @Override
    public void removed() {
        callback.accept(x, y);
        manager.saveConfig();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!super.mouseClicked(mouseX, mouseY, button)) {
            this.x = (int) Ukutils.bound(mouseX, 0, this.width);
            this.y = (int) Ukutils.bound(mouseY, 0, this.height);
        }

        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!super.keyPressed(keyCode, scanCode, modifiers)) {
            int amount = modifiers == GLFW.GLFW_MOD_SHIFT ? 10 : 1;

            switch (keyCode) {
                case GLFW.GLFW_KEY_RIGHT -> x += amount;
                case GLFW.GLFW_KEY_LEFT -> x -= amount;
                case GLFW.GLFW_KEY_DOWN -> y += amount;
                case GLFW.GLFW_KEY_UP -> y -= amount;
                default -> {
                    return false;
                }
            }

            // make sure they are within bounds
            this.x = Ukutils.bound(x, 0, this.width);
            this.y = Ukutils.bound(y, 0, this.height);
        }

        return true;
    }

    /**
     * Draws the screen and all the components in it.
     * Called in {@link PositionSelectScreen#render(MatrixStack, int, int, float)}.
     *
     * @param matrices The matrix stack
     * @param mouseX   The x position of the mouse
     * @param mouseY   The y position of the mouse
     * @param delta    The delta time
     */
    protected abstract void draw(MatrixStack matrices, int mouseX, int mouseY, float delta);

    /**
     * Draws the screen and all the components in it, in their default position.
     * Called in {@link PositionSelectScreen#render(MatrixStack, int, int, float)}.
     *
     * @param matrices The matrix stack
     * @param mouseX   The x position of the mouse
     * @param mouseY   The y position of the mouse
     * @param delta    The delta time
     */
    protected void drawDefault(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        draw(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        if (x == -1 || y == -1) {
            drawDefault(matrices, mouseX, mouseY, delta);
        } else {
            draw(matrices, mouseX, mouseY, delta);
        }
        super.render(matrices, mouseX, mouseY, delta);
    }
}
