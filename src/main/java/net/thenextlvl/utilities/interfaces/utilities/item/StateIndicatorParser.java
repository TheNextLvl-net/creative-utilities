package net.thenextlvl.utilities.interfaces.utilities.item;

import com.google.gson.JsonObject;
import net.thenextlvl.interfaces.RenderContext;
import net.thenextlvl.interfaces.reader.DynamicItemParser;
import net.thenextlvl.interfaces.reader.ParserContext;
import net.thenextlvl.interfaces.reader.ParserException;
import net.thenextlvl.utilities.interfaces.utilities.Utilities;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

public final class StateIndicatorParser implements DynamicItemParser<JsonObject> {
    @Override
    public BiFunction<ItemStack, RenderContext, ItemStack> parse(final JsonObject element, final ParserContext context) throws ParserException {
        final var permission = element.get("permission").getAsString();
        final var key = element.get("setting").getAsString();
        final var setting = Utilities.SETTINGS.get(key);
        if (setting == null) throw new ParserException("Invalid setting: %s", key);
        return (itemStack, renderContext) -> {
            final var hasPermission = renderContext.player().hasPermission(permission);
            final var enabled = setting.get(renderContext.player());
            final var material = !hasPermission ? Material.ORANGE_STAINED_GLASS_PANE
                    : enabled ? Material.GREEN_STAINED_GLASS_PANE
                      : Material.RED_STAINED_GLASS_PANE;
            return itemStack.withType(material);
        };
    }
}
