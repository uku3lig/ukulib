package net.uku3lig.ukulib.config.option.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.uku3lig.ukulib.config.option.ButtonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ButtonCreatorList extends ElementListWidget<ButtonCreatorList.ButtonEntry> {

    public ButtonCreatorList(MinecraftClient minecraftClient, int i, int j, int k, int l, int m) {
        super(minecraftClient, i, j, k, l, m);
        this.centerListVertically = false;
    }

    public void addEntries(ButtonCreator first, @Nullable ButtonCreator other) {
        this.addEntry(new ButtonEntry(this.getRowWidth(), first, other));
    }

    public void addAll(ButtonCreator[] options) {
		for(int i = 0; i < options.length; i += 2) {
			this.addEntries(options[i], i < options.length - 1 ? options[i + 1] : null);
		}
	}

    @Override
	public int getRowWidth() {
		return 400;
	}

	@Override
	protected int getScrollbarPositionX() {
		return super.getScrollbarPositionX() + 32;
	}

    public static class ButtonEntry extends ElementListWidget.Entry<ButtonEntry> {
        private final List<ClickableWidget> widgets;

        public ButtonEntry(int width, ButtonCreator first, @Nullable ButtonCreator other) {
            ClickableWidget firstButton = first.createButton(width / 2 - 155, 0, 150, 20);
            this.widgets = other == null ? List.of(firstButton) : List.of(firstButton, other.createButton(width / 2 + 5, 0, 150, 20));
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return widgets;
        }

        @Override
        public List<? extends Element> children() {
            return widgets;
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            widgets.forEach(w -> {
                w.setY(y);
                w.render(matrices, mouseX, mouseY, tickDelta);
            });
        }
    }
}
