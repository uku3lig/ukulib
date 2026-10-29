package net.uku3lig.ukulibtest;

import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.utils.Position;

import java.io.Serializable;

public class TestConfig implements Serializable {
    public static final ConfigManager<TestConfig> manager = ConfigManager.createDefault(TestConfig.class, "ukulib-test");

    public boolean enable = true;
    public Position position = Position.MIDDLE;
    public int color = 0xFF0000;
    public int colorAlpha = 0x8000FF00;
    public String input = "Hi";
    public double doubleInput = 1.55;
    public int intSlider = 5;
    public double doubleSlider = 6.5;
    public int x = -1;
    public int y = -1;

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
