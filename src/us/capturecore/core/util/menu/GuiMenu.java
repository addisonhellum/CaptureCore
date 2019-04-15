package us.capturecore.core.util.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GuiMenu {

    private final Inventory MENU;

    private ArrayList<Integer> lockedSlots = new ArrayList<>();
    public Map<ItemStack, String> commandBindsLeft = new HashMap<>();
    public Map<ItemStack, String> commandBindsRight = new HashMap<>();

    public GuiMenu(Inventory inv) {
        MENU = inv;
    }
    public GuiMenu(String title) { MENU = Bukkit.createInventory(null, 27, title); }
    public GuiMenu(String title, int lines) { MENU = Bukkit.createInventory(null, lines * 9, title); }

    private boolean isSlotLocked(int slot) {
        return lockedSlots.contains(slot);
    }

    public GuiMenu set(int slot, ItemStack item) {
        if (!isSlotLocked(slot))
            MENU.setItem(slot, item);
        return this;
    }

    public GuiMenu set(int slot, ItemStack item, boolean override) {
        if (!isSlotLocked(slot) || override)
            MENU.setItem(slot, item);
        return this;
    }

    public GuiMenu set(int line, int slot, ItemStack item) {
        int vanillaSlot = (line * 9) + slot;
        set(vanillaSlot, item);
        return this;
    }

    public GuiMenu set(int line, int slot, ItemStack item, boolean override) {
        int vanillaSlot = (line * 9) + slot;
        set(vanillaSlot, item, override);
        return this;
    }

    public GuiMenu add(ItemStack item) {
        for (int i = 0; i < MENU.getSize(); i++) {
            try {
                ItemStack itemAt = MENU.getItem(i);
                if (itemAt.getType().equals(Material.AIR) && !isSlotLocked(i)) {
                    MENU.setItem(i, item);
                    return this;
                }
            } catch (Exception e) {
                if (!isSlotLocked(i)) {
                    MENU.setItem(i, item);
                    return this;
                }
            }
        } return this;
    }

    public GuiMenu fill(ItemStack item) {
        for (int i = 0; i < MENU.getSize(); i++) {
            if (!isSlotLocked(i)) MENU.setItem(i, item);
        } return this;
    }

    public GuiMenu fillRow(int row, ItemStack item) {
        int orig = (row - 1) * 9;
        for (int i = orig; i < (orig + 9); i++) {
            if (!isSlotLocked(i)) MENU.setItem(i, item);
        } return this;
    }

    public GuiMenu fillColumn(int column, ItemStack item) {
        for (int i = 0; i < (MENU.getSize() / 9); i++) {
            int slot = (column - 1) + (i * 9);
            if (!isSlotLocked(slot)) MENU.setItem(slot, item);
        } return this;
    }

    public GuiMenu fillFrom(int start, int finish, ItemStack item) {
        for (int i = start; i <= finish; i++)
            if (!isSlotLocked(i))
                MENU.setItem(i, item);
        return this;
    }

    public GuiMenu lockSlot(int slot) {
        lockedSlots.add(slot);
        return this;
    }

    public GuiMenu lockSlots(Integer[] slots) {
        for (int slot : slots)
            lockedSlots.add(slot);
        return this;
    }

    public GuiMenu lockRow(int row) {
        int orig = (row - 1) * 9;
        for (int i = orig; i < (orig + 9); i++) {
            lockedSlots.add(i);
        } return this;
    }

    public GuiMenu lockColumn(int column) {
        for (int i = 0; i < (MENU.getSize() / 9); i++) {
            int slot = (column - 1) + (i * 9);
            lockedSlots.add(slot);
        } return this;
    }

    public GuiMenu lockFrom(int start, int finish) {
        for (int i = start; i <= finish; i++)
            lockedSlots.add(i);
        return this;
    }

    public GuiMenu bindLeft(ItemStack item, String command) {
        commandBindsLeft.putIfAbsent(item, command);
        if (!GuiHandler.boundMenus.contains(this))
            GuiHandler.boundMenus.add(this);
        return this;
    }

    public GuiMenu bindLeft(int slot, String command) {
        ItemStack item = MENU.getItem(slot);
        commandBindsLeft.putIfAbsent(item, command);
        if (!GuiHandler.boundMenus.contains(this))
            GuiHandler.boundMenus.add(this);
        return this;
    }

    public GuiMenu bindRight(ItemStack item, String command) {
        commandBindsRight.putIfAbsent(item, command);
        if (!GuiHandler.boundMenus.contains(this))
            GuiHandler.boundMenus.add(this);
        return this;
    }

    public GuiMenu bindRight(int slot, String command) {
        ItemStack item = MENU.getItem(slot);
        commandBindsRight.putIfAbsent(item, command);
        if (!GuiHandler.boundMenus.contains(this))
            GuiHandler.boundMenus.add(this);
        return this;
    }

    public GuiMenu freezeItems(boolean value) {
        Inventory inv = build();
        if (value) GuiHandler.lockedInventories.add(inv);
        else GuiHandler.lockedInventories.remove(inv);
        return this;
    }

    public void display(Player player) {
        player.openInventory(build());
    }

    public Inventory build() {
        return MENU;
    }

}
