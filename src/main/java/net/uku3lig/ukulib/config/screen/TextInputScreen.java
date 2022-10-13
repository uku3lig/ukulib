package net.uku3lig.ukulib.config.screen;

import lombok.extern.log4j.Log4j2;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.gui.widgets.Textbox;
import net.minecraft.client.resource.language.TranslationStorage;
import net.uku3lig.ukulib.config.ConfigManager;
import org.lwjgl.input.Keyboard;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A screen used to input a value.
 * Instances of this class should NOT be reused.
 * @param <T> The type of the value.
 */
@Log4j2
@SuppressWarnings("unused")
public abstract class TextInputScreen<T> extends Screen {
    private final Screen parent;
    private final String title;
    private final String label;
    private final Consumer<T> callback;
    private final T last;
    private final ConfigManager<?> manager;

    private Textbox textbox;

    /**
     * Creates an input screen.
     *
     * @param parent The parent screen
     * @param title The title of the screen
     * @param label The label to be shown above the text input field
     * @param callback The action to be performed when the value is changed
     * @param last The last known value
     * @param manager The config manager, used to save the config
     */
    protected TextInputScreen(Screen parent, String title, String label, Consumer<T> callback, T last, ConfigManager<?> manager) {
        this.parent = parent;
        this.title = title;
        this.label = label;
        this.callback = callback;
        this.last = last;
        this.manager = manager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        TranslationStorage storage = TranslationStorage.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4 + 96 + 12, storage.translate("gui.done")));
        textbox = new Textbox(this, this.minecraft.textRenderer, this.width / 2 - 100, 116, 200, 20, String.valueOf(last));
        ((Button)this.buttons.get(0)).active = convert(String.valueOf(last)).isPresent();
    }

    @Override
    protected void buttonClicked(Button button) {
        if (button.active && button.id == 0) {
            String content = this.textbox.method_1876().trim();
            Optional<T> value = convert(content);
            if (value.isPresent()) {
                callback.accept(value.get());
                manager.saveConfig();
                this.minecraft.openScreen(parent);
            }
        }
    }

    @Override
    protected void keyPressed(char c, int i) {
        this.textbox.method_1877(c, i);
        if (c == 13) {
            this.buttonClicked((Button)this.buttons.get(0));
        }

        ((Button)this.buttons.get(0)).active = convert(this.textbox.method_1876()).isPresent();
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        this.textbox.method_1879(i, j, k);
    }

    /**
     * Converts the contents of the text field to the type needed.
     * @param value the value of the text field, given by the user
     * @return an empty optional is the value is incorrect, else the converted value
     */
    public abstract Optional<T> convert(String value);

    @Override
    public final void onClose() {
        Keyboard.enableRepeatEvents(false);
        // method_1876 = getText
        convert(textbox.method_1876()).ifPresent(t -> {
            callback.accept(t);
            manager.saveConfig();
        });
    }

    @Override
    public void tick() {
        super.tick();
        textbox.tick();
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        drawTextWithShadowCentred(this.minecraft.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        drawTextWithShadow(this.minecraft.textRenderer, label, this.width / 2 - 100, 100, 0xA0A0A0);
        this.textbox.method_1883(); // render
        super.render(mouseX, mouseY, delta);
    }
}
