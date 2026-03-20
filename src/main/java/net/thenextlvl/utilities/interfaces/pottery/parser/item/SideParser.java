package net.thenextlvl.utilities.interfaces.pottery.parser.item;

import com.google.gson.JsonPrimitive;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotDecorations;
import net.thenextlvl.interfaces.RenderContext;
import net.thenextlvl.interfaces.reader.DynamicItemParser;
import net.thenextlvl.interfaces.reader.ParserContext;
import net.thenextlvl.interfaces.reader.ParserException;
import net.thenextlvl.utilities.interfaces.pottery.PotteryDesignerGUI;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

public final class SideParser implements DynamicItemParser<JsonPrimitive> {
    @Override
    public BiFunction<ItemStack, RenderContext, ItemStack> parse(final JsonPrimitive element, final ParserContext context) throws ParserException {
        final var side = PotteryDesignerGUI.Side.fromName(element.getAsString());
        return (itemStack, renderContext) -> {
            return renderContext.getState("pot_data", PotDecorations.class).map(data -> {
                final var itemType = switch (side) {
                    case BACK -> data.back();
                    case FRONT -> data.front();
                    case LEFT -> data.left();
                    case RIGHT -> data.right();
                };
                if (itemType == null) return itemStack;
                final var stack = itemType.createItemStack();
                stack.copyDataFrom(itemStack, type -> !type.equals(DataComponentTypes.ITEM_MODEL));
                return stack;
            }).orElse(itemStack);
        };
    }
}
