package us.capturecore.core.util.menu;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import us.capturecore.core.util.text.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class ItemStackBuilder {

    private final ItemStack ITEM_STACK;
    private boolean durability = false;

    public ItemStackBuilder(Material mat) {
        ITEM_STACK = new ItemStack(mat);
    }

    public ItemStackBuilder(ItemStack item) {
        ITEM_STACK = item;
    }

    public ItemStackBuilder withAmount(int amount) {
        ITEM_STACK.setAmount(amount);
        return this;
    }

    public ItemStackBuilder withName(String name) {
        final ItemMeta meta = ITEM_STACK.getItemMeta();
        meta.setDisplayName(ChatUtil.format(name));
        ITEM_STACK.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder withLore(String name) {
        final ItemMeta meta = ITEM_STACK.getItemMeta();
        List<String> lore = meta.getLore();

        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.add(ChatUtil.format("&7" + name));
        meta.setLore(lore);
        ITEM_STACK.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder withLore(String[] name) {
        final ItemMeta meta = ITEM_STACK.getItemMeta();
        List<String> lore = meta.getLore();

        if (lore == null) {
            lore = new ArrayList<>();
        }

        for (String s : name)
            lore.add(ChatUtil.format("&7" + s));

        meta.setLore(lore);
        ITEM_STACK.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder withDurability(int durability) {
        ITEM_STACK.setDurability((short) durability);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemStack withData(int data) {
        ItemStack item = new ItemStack(ITEM_STACK.getType(), ITEM_STACK.getAmount(), (byte) data);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ITEM_STACK.getItemMeta().getDisplayName());
        meta.setLore(ITEM_STACK.getItemMeta().getLore());
        for (Enchantment e : ITEM_STACK.getItemMeta().getEnchants().keySet()) {
            meta.addEnchant(e, ITEM_STACK.getItemMeta().getEnchants().get(e), true);
        }
        item.setItemMeta(meta);
        return item;
    }

    public ItemStackBuilder withEnchantment(Enchantment enchantment, final int level) {
        ITEM_STACK.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemStackBuilder withEnchantment(Enchantment enchantment) {
        ITEM_STACK.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemStackBuilder withType(Material material) {
        ITEM_STACK.setType(material);
        return this;
    }

    public ItemStackBuilder clearLore() {
        final ItemMeta meta = ITEM_STACK.getItemMeta();
        meta.setLore(new ArrayList<String>());
        ITEM_STACK.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder clearEnchantments() {
        for (Enchantment enchantment : ITEM_STACK.getEnchantments().keySet()) {
            ITEM_STACK.removeEnchantment(enchantment);
        }
        return this;
    }

    public ItemStackBuilder withColor(Color color) {
        Material type = ITEM_STACK.getType();
        if (type == Material.LEATHER_BOOTS || type == Material.LEATHER_CHESTPLATE || type == Material.LEATHER_HELMET
                || type == Material.LEATHER_LEGGINGS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) ITEM_STACK.getItemMeta();
            meta.setColor(color);
            ITEM_STACK.setItemMeta(meta);
            return this;
        } else {
            throw new IllegalArgumentException("withColor is only applicable for leather armor!");
        }
    }

    public ItemStackBuilder withOwner(String name) {
        Material type = ITEM_STACK.getType();
        if (type == Material.SKULL_ITEM) {
            SkullMeta meta = (SkullMeta) ITEM_STACK.getItemMeta();
            meta.setOwner(name);
            ITEM_STACK.setItemMeta(meta);
            return this;
        } else {
            throw new IllegalArgumentException("withOwner is only applicable to skull items!");
        }
    }

    public ItemStackBuilder enchantIf(Enchantment enchantment, boolean condition) {
        if (condition) {
            return withEnchantment(enchantment);
        } else return this;
    }

    public ItemStackBuilder hideEnchantments(boolean hide) {
        ItemMeta meta = ITEM_STACK.getItemMeta();
        if (hide) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        else meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        ITEM_STACK.setItemMeta(meta);

        return this;
    }

    public ItemStackBuilder withDurability() {
        this.durability = true;
        return this;
    }

    public ItemStack build() {
        if (!durability) {
            ItemMeta meta = ITEM_STACK.getItemMeta();
            meta.spigot().setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            ITEM_STACK.setItemMeta(meta);
        }

        return ITEM_STACK;
    }

}