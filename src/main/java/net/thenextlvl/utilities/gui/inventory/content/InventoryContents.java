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
package net.thenextlvl.utilities.gui.inventory.content;

import net.thenextlvl.utilities.gui.inventory.ClickableItem;
import net.thenextlvl.utilities.gui.inventory.SmartInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

@NullMarked
public interface InventoryContents {

    SmartInventory inventory();

    ClickableItem[][] all();

    Optional<ClickableItem> get(int row, int column);

    Optional<ClickableItem> get(SlotPos slotPos);

    InventoryContents set(int row, int column, ClickableItem item);

    InventoryContents set(SlotPos slotPos, ClickableItem item);

    InventoryContents add(ClickableItem item);

    InventoryContents fill(ClickableItem item);

    class Impl implements InventoryContents {

        private final SmartInventory inv;
        private final Player player;

        private final ClickableItem[][] contents;

        public Impl(SmartInventory inv, Player player) {
            this.inv = inv;
            this.player = player;
            this.contents = new ClickableItem[inv.getRows()][inv.getColumns()];
        }

        @Override
        public SmartInventory inventory() {
            return inv;
        }

        @Override
        public ClickableItem[][] all() {
            return contents;
        }

        @Override
        public Optional<ClickableItem> get(int row, int column) {
            if (row >= contents.length) {
                return Optional.empty();
            }
            if (column >= contents[row].length) {
                return Optional.empty();
            }

            return Optional.ofNullable(contents[row][column]);
        }

        @Override
        public Optional<ClickableItem> get(SlotPos slotPos) {
            return get(slotPos.getRow(), slotPos.getColumn());
        }

        @Override
        public InventoryContents set(int row, int column, ClickableItem item) {
            if (row >= contents.length) {
                return this;
            }
            if (column >= contents[row].length) {
                return this;
            }

            contents[row][column] = item;
            update(row, column, item != null ? item.getItem() : null);
            return this;
        }

        @Override
        public InventoryContents set(SlotPos slotPos, ClickableItem item) {
            return set(slotPos.getRow(), slotPos.getColumn(), item);
        }

        @Override
        public InventoryContents add(ClickableItem item) {
            for (int row = 0; row < contents.length; row++) {
                for (int column = 0; column < contents[0].length; column++) {
                    if (contents[row][column] == null) {
                        set(row, column, item);
                        return this;
                    }
                }
            }

            return this;
        }

        @Override
        public InventoryContents fill(ClickableItem item) {
            for (int row = 0; row < contents.length; row++) {
                for (int column = 0; column < contents[row].length; column++) {
                    set(row, column, item);
                }
            }

            return this;
        }

        private void update(int row, int column, ItemStack item) {
            if (!inv.getManager().getOpenedPlayers(inv).contains(player)) {
                return;
            }

            Inventory topInventory = player.getOpenInventory().getTopInventory();
            topInventory.setItem(inv.getColumns() * row + column, item);
        }

    }

}
