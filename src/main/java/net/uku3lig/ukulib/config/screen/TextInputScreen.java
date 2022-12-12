package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.utils.Ukutils;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A screen used to input a value.
 * Instances of this class should NOT be reused.
 *
 * @param <T> The type of the value.
 */
public abstract class TextInputScreen<T> extends Screen {
    private final Screen parent;
    private final Text label;
    private final Consumer<T> callback;
    private final T last;
    private final ConfigManager<?> manager;

    private TextFieldWidget textField;

    /**
     * Creates an input screen.
     *
     * @param parent   The parent screen
     * @param title    The title of the screen
     * @param label    The label to be shown above the text input field
     * @param callback The action to be performed when the value is changed
     * @param last     The last known value
     * @param manager  The config manager, used to save the config
     */
    protected TextInputScreen(Screen parent, Text title, Text label, Consumer<T> callback, T last, ConfigManager<?> manager) {
        super(title);
        this.parent = parent;
        this.label = label;
        this.callback = callback;
        this.last = last;
        this.manager = manager;
    }

    @Override
    protected void init() {
        final ButtonWidget doneButton = Ukutils.doneButton(this.width, this.height, this.parent);
        textField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 116, 200, 20, label);
        textField.setText(String.valueOf(last));
        textField.setChangedListener(s -> doneButton.active = convert(s).isPresent());

        this.addButton(doneButton);
        this.children.add(textField);
        this.setInitialFocus(textField);
    }

    /**
     * Converts the contents of the text field to the type needed.
     *
     * @param value the value of the text field, given by the user
     * @return an empty optional is the value is incorrect, else the converted value
     */
    public abstract Optional<T> convert(String value);

    @Override
    public final void removed() {
        convert(textField.getText()).ifPresent(t -> {
            callback.accept(t);
            manager.saveConfig();
        });
    }

    @Override
    public void onClose() {
        MinecraftClient.getInstance().openScreen(parent);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        drawTextWithShadow(matrices, this.textRenderer, label, this.width / 2 - 100, 100, 0xA0A0A0);
        this.textField.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
