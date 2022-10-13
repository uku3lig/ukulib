package net.uku3lig.ukulib.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public class MinecraftAccessor {
    @Accessor("instance")
    public static Minecraft getInstance() {
        throw new UnsupportedOperationException();
    }

    private MinecraftAccessor() {}
}
