package net.thenextlvl.utilities.interfaces.utilities.item;

import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.thenextlvl.interfaces.RenderContext;
import net.thenextlvl.interfaces.reader.DynamicItemParser;
import net.thenextlvl.interfaces.reader.ParserContext;
import net.thenextlvl.interfaces.reader.ParserException;
import net.thenextlvl.utilities.interfaces.utilities.Utilities;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiFunction;

public final class FeatureLoreParser implements DynamicItemParser<JsonObject> {
    @Override
    public BiFunction<ItemStack, RenderContext, ItemStack> parse(final JsonObject element, final ParserContext context) throws ParserException {
        final var permission = element.get("permission").getAsString();
        final var description = element.get("description").getAsString();
        final var key = element.get("setting").getAsString();
        final var setting = Utilities.SETTINGS.get(key);
        if (setting == null) throw new ParserException("Invalid setting: %s", key);
        return (itemStack, renderContext) -> {
            final var player = renderContext.player();
            if (!player.hasPermission(permission)) {
                itemStack.lore(List.of(context.renderText(player, "gui.item.permission")));
                return itemStack;
            }
            itemStack.lore(List.of(
                    context.renderText(player, "gui.state", Formatter.booleanChoice("state", setting.get(player))),
                    Component.empty(),
                    context.renderText(player, "gui.toggle.click"),
                    Component.empty(),
                    context.renderText(player, description)
                            .applyFallbackStyle(TextDecoration.ITALIC.withState(false))
                            .colorIfAbsent(NamedTextColor.DARK_GRAY)
            ));
            return itemStack;
        };
    }
}
