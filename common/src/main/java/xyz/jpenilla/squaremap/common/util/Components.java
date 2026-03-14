package xyz.jpenilla.squaremap.common.util;

import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import xyz.jpenilla.squaremap.common.data.MapWorldInternal;

@DefaultQualifier(NonNull.class)
public final class Components {
    private static final Pattern SPECIAL_CHARACTERS_PATTERN = Pattern.compile("[^\\s\\w\\-]");

    public static Component miniMessage(final String text) {
        return Component.text(text);
    }

    public static Component miniMessage(final String text, final Placeholder... placeholders) {
        String result = text;

        for (Placeholder placeholder : placeholders) {
            result = result.replace("<" + placeholder.name() + ">", placeholder.value());
        }

        return Component.text(result);
    }

    public static Placeholder placeholder(final String name, final Object value) {
        return new Placeholder(name, String.valueOf(value));
    }

    public static Placeholder worldPlaceholder(final MapWorldInternal mapWorld) {
        return placeholder("world", mapWorld.identifier().asString());
    }

    public static Placeholder worldPlaceholder(final ServerLevel level) {
        return placeholder("world", level.dimension().location());
    }

    public static Placeholder playerPlaceholder(final ServerPlayer player) {
        return placeholder("player", player.getGameProfile().getName());
    }
    public static Component highlightSpecialCharacters(final Component component, final TextColor highlightColor) {
        return highlight(component, SPECIAL_CHARACTERS_PATTERN, highlightColor);
    }

    public static Component highlight(final Component component, final Pattern highlight, final TextColor highlightColor) {
        return component.replaceText(config -> {
            config.match(highlight);
            config.replacement(match -> match.color(highlightColor));
        });
    }

    public record Placeholder(String name, String value) {}
}
