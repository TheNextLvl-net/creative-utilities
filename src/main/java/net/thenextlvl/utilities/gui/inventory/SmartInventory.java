/*
 * Builder's Utilities is a collection of a lot of tiny features that help with building.
 * Copyright (C) Arcaniax-Development
 * Copyright (C) Arcaniax team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package net.thenextlvl.utilities.gui.inventory;

import lombok.Getter;
import net.thenextlvl.utilities.gui.inventory.content.InventoryContents;
import net.thenextlvl.utilities.gui.inventory.content.InventoryProvider;
import net.thenextlvl.utilities.gui.inventory.opener.InventoryOpener;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

@NullMarked
@SuppressWarnings("unchecked")
public class SmartInventory {

    @Getter
    private final InventoryManager manager;
    @Getter
    private String title;
    @Getter
    private InventoryType type;
    @Getter
    private int rows, columns;
    @Getter
    private boolean closeable;
    private InventoryProvider provider;

    private SmartInventory(InventoryManager manager) {
        this.manager = manager;
    }

    public static Builder builder(InventoryManager manager, InventoryProvider provider) {
        return new Builder(manager, provider);
    }

    public Inventory open(Player player) {
        Optional<SmartInventory> oldInv = this.manager.getInventory(player);

        oldInv.ifPresent(inv -> this.manager.setInventory(player, null));

        InventoryContents contents = new InventoryContents.Impl(this, player);

        this.manager.setContents(player, contents);
        this.provider.init(player, contents);

        InventoryOpener opener = this.manager.findOpener(type)
                .orElseThrow(() -> new IllegalStateException(
                        "No opener found for the inventory type " + type.name()));
        Inventory handle = opener.open(this, player);

        this.manager.setInventory(player, this);

        return handle;
    }

    public void close(Player player) {
        this.manager.setInventory(player, null);
        player.closeInventory();

        this.manager.setContents(player, null);
    }

    public static final class Builder {

        private String title = "";
        private InventoryType type = InventoryType.CHEST;
        private int rows = 6, columns = 9;
        private boolean closeable = true;
        private final InventoryManager manager;
        private final InventoryProvider provider;

        private Builder(InventoryManager manager, InventoryProvider provider) {
            this.manager = manager;
            this.provider = provider;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder type(InventoryType type) {
            this.type = type;
            return this;
        }

        public Builder size(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;
            return this;
        }

        public Builder closeable(boolean closeable) {
            this.closeable = closeable;
            return this;
        }

        public SmartInventory build() {
            SmartInventory inv = new SmartInventory(manager);
            inv.title = this.title;
            inv.type = this.type;
            inv.rows = this.rows;
            inv.columns = this.columns;
            inv.closeable = this.closeable;
            inv.provider = this.provider;
            return inv;
        }

    }

}
