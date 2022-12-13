package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.uku3lig.ukulib.config.ConfigManager;
import org.lwjgl.glfw.GLFW;

import java.util.function.BiConsumer;

/**
 * Simple screen used to select a custom position on the screen for an element.
 */
public abstract class PositionSelectScreen extends Screen {
    private final Screen parent;
    /**
     * The x position of the element.
     */
    protected int x;
    /**
     * The y position of the element.
     */
    protected int y;
    private final ConfigManager<?> manager;
    private final BiConsumer<Integer, Integer> callback;

    /**
     * Creates a position select screen.
     * @param parent The parent screen
     * @param x The initial x position
     * @param y The initial y position
     * @param manager The config manager, used to save the config
     * @param callback The action to be performed when the position is changed
     */
    protected PositionSelectScreen(Screen parent, int x, int y, ConfigManager<?> manager, BiConsumer<Integer, Integer> callback) {
        super(new LiteralText("Position Select"));
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.manager = manager;
        this.callback = callback;
    }

    @Override
    protected void init() {
        int textWidth = font.getStringWidth(I18n.translate("gui.done"));
        this.addButton(new ButtonWidget(this.width - 20 - textWidth, 10, 10 + textWidth, 20, I18n.translate("gui.done"), b -> onClose()));
    }

    @Override
    public void onClose() {
        MinecraftClient.getInstance().openScreen(parent);
    }

    @Override
    public void removed() {
        callback.accept(x, y);
        manager.saveConfig();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!super.mouseClicked(mouseX, mouseY, button)) {
            this.x = (int) mouseX;
            this.y = (int) mouseY;
        }

        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!super.keyPressed(keyCode, scanCode, modifiers)) {
            int amount = modifiers == GLFW.GLFW_MOD_SHIFT ? 10 : 1;

            switch (keyCode) {
                case GLFW.GLFW_KEY_RIGHT:
                    x += amount;
                    break;
                case GLFW.GLFW_KEY_LEFT:
                    x -= amount;
                    break;
                case GLFW.GLFW_KEY_DOWN:
                    y += amount;
                    break;
                case GLFW.GLFW_KEY_UP:
                    y -= amount;
                    break;
                default:
                    return false;
            }
        }

        return true;
    }

    /**
     * Draws the screen and all the components in it.
     * Called in {@link PositionSelectScreen#render(int, int, float)}.
     * @param mouseX The x position of the mouse
     * @param mouseY The y position of the mouse
     * @param delta The delta time
     */
    public abstract void draw(int mouseX, int mouseY, float delta);

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 20, 0xFFFFFF);
        draw(mouseX, mouseY, delta);
        super.render(mouseX, mouseY, delta);
    }
}
