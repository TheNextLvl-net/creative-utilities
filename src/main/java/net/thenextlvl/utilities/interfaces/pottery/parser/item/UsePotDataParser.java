package net.thenextlvl.utilities.interfaces.pottery.parser.item;

import com.google.gson.JsonObject;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotDecorations;
import net.thenextlvl.interfaces.RenderContext;
import net.thenextlvl.interfaces.reader.DynamicItemParser;
import net.thenextlvl.interfaces.reader.ParserConditions;
import net.thenextlvl.interfaces.reader.ParserContext;
import net.thenextlvl.interfaces.reader.ParserException;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

public final class UsePotDataParser implements DynamicItemParser<JsonObject> {
    @Override
    public BiFunction<ItemStack, RenderContext, ItemStack> parse(final JsonObject element, final ParserContext context) throws ParserException {
        ParserConditions.checkState(element.isEmpty(), "Use pot data parser takes no arguments");
        return (itemStack, renderContext) -> {
            renderContext.state("pot_data", PotDecorations.class).ifPresent(data ->
                    itemStack.setData(DataComponentTypes.POT_DECORATIONS, data));
            return itemStack;
        };
    }
}
