package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.uku3lig.ukulib.config.ConfigManager;
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

    private ButtonWidget doneButton;
    private ButtonWidget defaultButton;

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
        this.doneButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, b -> close())
                .size(50, 20)
                .build());

        this.defaultButton = this.addDrawableChild(ButtonWidget.builder(Text.of("Default"), b -> {
                    this.x = -1;
                    this.y = -1;
                })
                .size(50, 20)
                .build());
    }

    @Override
    protected void refreshWidgetPositions() {
        this.doneButton.setPosition(this.width - 60, 10);
        this.defaultButton.setPosition(this.width - 60, 35);
    }

    @Override
    public void removed() {
        callback.accept(x, y);
        manager.saveConfig();
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubleClick) {
        if (!super.mouseClicked(click, doubleClick)) {
            this.x = (int) MathHelper.clamp(click.x(), 0, this.width);
            this.y = (int) MathHelper.clamp(click.y(), 0, this.height);
        }

        return true;
    }

    @Override
    public boolean keyPressed(KeyInput input) {
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
            this.x = MathHelper.clamp(x, 0, this.width);
            this.y = MathHelper.clamp(y, 0, this.height);
        }

        return true;
    }

    /**
     * Draws the screen and all the components in it.
     * Called in {@link PositionSelectScreen#render(DrawContext, int, int, float)}.
     *
     * @param drawContext The drawable helper
     * @param mouseX      The x position of the mouse
     * @param mouseY      The y position of the mouse
     * @param delta       The delta time
     * @param x           The x position of the element
     * @param y           The y position of the element
     */
    protected abstract void draw(DrawContext drawContext, int mouseX, int mouseY, float delta, int x, int y);

    /**
     * Draws the screen and all the components in it, in their default position.
     * Called in {@link PositionSelectScreen#render(DrawContext, int, int, float)}.
     *
     * @param drawContext The drawable helper
     * @param mouseX      The x position of the mouse
     * @param mouseY      The y position of the mouse
     * @param delta       The delta time
     */
    protected void drawDefault(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        draw(drawContext, mouseX, mouseY, delta, 5, 5);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);
        drawContext.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFFFF);
        drawContext.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("ukulib.position.desc"), this.width / 2, this.height / 2 - 80, 0xFFFFFF55);
        drawContext.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("ukulib.position.desc.move"), this.width / 2, this.height / 2 - 65, 0xFFFFFFFF);

        if (x == -1 || y == -1) {
            drawDefault(drawContext, mouseX, mouseY, delta);
        } else {
            draw(drawContext, mouseX, mouseY, delta, x, y);
        }
    }
}
