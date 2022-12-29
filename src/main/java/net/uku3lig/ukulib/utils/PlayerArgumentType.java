package net.uku3lig.ukulib.utils;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class PlayerArgumentType implements ArgumentType<PlayerArgumentType.PlayerSelector> {
    public static final SimpleCommandExceptionType PLAYER_NOT_FOUND_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("name.entity.notfound.player"));

    public static PlayerArgumentType player() {
        return new PlayerArgumentType();
    }

    public static PlayerEntity getPlayer(String name, CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        PlayerSelector selector = context.getArgument(name, PlayerSelector.class);

        return context.getSource().getWorld().getPlayers().stream()
                .filter(p -> p.getEntityName().equalsIgnoreCase(selector.name) || p.getUuidAsString().equalsIgnoreCase(selector.name))
                .findFirst()
                .orElseThrow(PLAYER_NOT_FOUND_EXCEPTION::create);
    }

    @Override
    public PlayerSelector parse(StringReader reader) throws CommandSyntaxException {
        return new PlayerSelector(reader.readString());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof FabricClientCommandSource source) {
            return CommandSource.suggestMatching(source.getWorld().getPlayers().stream().map(PlayerEntity::getEntityName), builder);
        } else {
            return CommandSource.suggestMatching(Collections.emptyList(), builder);
        }
    }

    @Override
    public Collection<String> getExamples() {
        return Collections.singleton("uku3lig");
    }

    public record PlayerSelector(String name) {
    }
}
