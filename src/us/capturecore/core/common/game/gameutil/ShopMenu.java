package us.capturecore.core.common.game.gameutil;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.capturecore.core.util.menu.GuiMenu;
import us.capturecore.core.util.menu.ItemStackBuilder;

public class ShopMenu implements Listener {

    private static ItemStack divider = new ItemStackBuilder(Material.STAINED_GLASS_PANE).withName("&7⇧ Shop Categories")
            .withLore("&7⇩ Items for Purchase").withData(15);
    private static ItemStack active = new ItemStackBuilder(Material.STAINED_GLASS_PANE).withName("&aActive Category")
            .withLore("&7Currently showing contents.").withData(5);

    private static ItemStack[] categories = new ItemStack[] {
            new ItemStackBuilder(Material.WOOL).withName("&aBlocks").withLore("&eClick to Browse!").withData(14),
            new ItemStackBuilder(Material.GOLD_SWORD).withName("&aMelee Weapons").withLore("&eClick to Browse!").build(),
            new ItemStackBuilder(Material.GOLD_CHESTPLATE).withName("&aArmor").withLore("&eClick to Browse!").build(),
            new ItemStackBuilder(Material.GOLD_PICKAXE).withName("&aTools").withLore("&eClick to Browse!").build(),
            new ItemStackBuilder(Material.BOW).withName("&aArchery").withLore("&eClick to Browse!").build(),
            new ItemStackBuilder(Material.BREWING_STAND_ITEM).withName("&aPotions").withLore("&eClick to Browse!").build(),
            new ItemStackBuilder(Material.TNT).withName("&aMiscellaneous").withLore("&eClick to Browse!").build()
    };

    private static GuiMenu blockTab;
    public static GuiMenu getBlockTab() { return blockTab; }

    private static GuiMenu weaponTab;
    public static GuiMenu getWeaponTab() { return weaponTab; }

    private static GuiMenu armorTab;
    public static GuiMenu getArmorTab() { return armorTab; }

    private static GuiMenu toolsTab;
    public static GuiMenu getToolsTab() { return toolsTab; }

    private static GuiMenu archeryTab;
    public static GuiMenu getArcheryTab() { return archeryTab; }

    private static GuiMenu potionTab;
    public static GuiMenu getPotionTab() { return potionTab; }

    private static GuiMenu miscTab;
    public static GuiMenu getMiscTab() { return miscTab; }

    public static void initialize() {
        initBlockTab();
        initWeaponTab();
        initArmorTab();
        initToolsTab();
        initArcheryTab();
        initPotionTab();
        initMiscTab();
    }

    private static void initBlockTab() {
        blockTab = new GuiMenu("CTF Shop - Blocks", 6);
        blockTab.lockSlot(0).lockSlot(8);

        blockTab.fillRow(2, divider);
        blockTab.set(10, active);

        for (ItemStack tab : categories) blockTab.add(tab);

        blockTab.set(20, new ItemStackBuilder(Material.WOOL).withAmount(16).withName("&bWool").build());
        blockTab.set(21, new ItemStackBuilder(Material.STAINED_CLAY).withAmount(16).withName("&bStained Clay").withData(14));
        blockTab.set(22, new ItemStackBuilder(Material.ENDER_STONE).withAmount(12).withName("&bEnd Stone").build());
        blockTab.set(23, new ItemStackBuilder(Material.WOOD).withAmount(16).withName("&bWood Planks").build());
        blockTab.set(24, new ItemStackBuilder(Material.LADDER).withAmount(16).withName("&bLadders").build());

        blockTab.freezeItems(true);
    }

    public static void initWeaponTab() {
        weaponTab = new GuiMenu("CTF Shop - Melee Weapons", 6);
        weaponTab.lockSlot(0).lockSlot(8);

        weaponTab.fillRow(2, divider);
        weaponTab.set(11, active);

        for (ItemStack tab : categories) weaponTab.add(tab);

        weaponTab.set(19, new ItemStackBuilder(Material.STONE_SWORD).withName("&bStone Sword").build());
        weaponTab.set(20, new ItemStackBuilder(Material.IRON_SWORD).withName("&bIron Sword").build());
        weaponTab.set(21, new ItemStackBuilder(Material.DIAMOND_SWORD).withName("&bDiamond Sword").build());
        weaponTab.set(22, new ItemStackBuilder(Material.STICK).withName("&bKnockback Stick")
                .withEnchantment(Enchantment.KNOCKBACK).build());

        weaponTab.freezeItems(true);
    }

    public static void initArmorTab() {
        armorTab = new GuiMenu("CTF Shop - Armor", 6);
        armorTab.lockSlot(0).lockSlot(8);

        armorTab.fillRow(2, divider);
        armorTab.set(12, active);

        for (ItemStack tab : categories) armorTab.add(tab);

        armorTab.set(19, new ItemStackBuilder(Material.CHAINMAIL_CHESTPLATE).withName("&bPermanent Chainmail Armor").build());
        armorTab.set(20, new ItemStackBuilder(Material.IRON_CHESTPLATE).withName("&bPermanent Iron Armor").build());
        armorTab.set(21, new ItemStackBuilder(Material.DIAMOND_CHESTPLATE).withName("&bPermanent Diamond Armor").build());

        armorTab.freezeItems(true);
    }

    public static void initToolsTab() {
        toolsTab = new GuiMenu("CTF Shop - Tools", 6);
        toolsTab.lockSlot(0).lockSlot(8);

        toolsTab.fillRow(2, divider);
        toolsTab.set(13, active);

        for (ItemStack tab : categories) toolsTab.add(tab);

        toolsTab.set(19, new ItemStackBuilder(Material.IRON_PICKAXE).withName("&bIron Pickaxe")
                .withEnchantment(Enchantment.DIG_SPEED, 2).build());
        toolsTab.set(20, new ItemStackBuilder(Material.IRON_AXE).withName("&bIron Axe").build());
        toolsTab.set(21, new ItemStackBuilder(Material.SHEARS).withName("&bShears").build());

        toolsTab.freezeItems(true);
    }

    public static void initArcheryTab() {
        archeryTab = new GuiMenu("CTF Shop - Archery", 6);
        archeryTab.lockSlot(0).lockSlot(8);

        archeryTab.fillRow(2, divider);
        archeryTab.set(14, active);

        for (ItemStack tab : categories) archeryTab.add(tab);

        archeryTab.set(19, new ItemStackBuilder(Material.BOW).withName("&bPower Bow")
                .withEnchantment(Enchantment.ARROW_DAMAGE, 2).build());
        archeryTab.set(20, new ItemStackBuilder(Material.BOW).withName("&bPunch Bow")
                .withEnchantment(Enchantment.ARROW_KNOCKBACK).build());
        archeryTab.set(21, new ItemStackBuilder(Material.BOW).withName("&bSuper Bow")
                .withEnchantment(Enchantment.ARROW_DAMAGE, 2).withEnchantment(Enchantment.ARROW_KNOCKBACK).build());
        archeryTab.set(22, new ItemStackBuilder(Material.ARROW).withName("&bArrows").withAmount(8).build());

        archeryTab.freezeItems(true);
    }

    public static void initPotionTab() {
        potionTab = new GuiMenu("CTF Shop - Potions", 6);
        potionTab.lockSlot(0).lockSlot(8);

        potionTab.fillRow(2, divider);
        potionTab.set(15, active);

        for (ItemStack tab : categories) potionTab.add(tab);

        potionTab.set(19, new ItemStackBuilder(Material.POTION).withData(8235));
        potionTab.set(20, new ItemStackBuilder(Material.POTION).withData(8226));
        potionTab.set(21, new ItemStackBuilder(Material.POTION).withData(8238));

        potionTab.freezeItems(true);
    }

    public static void initMiscTab() {
        miscTab = new GuiMenu("CTF Shop - Potions", 6);
        miscTab.lockSlot(0).lockSlot(8);

        miscTab.fillRow(2, divider);
        miscTab.set(16, active);

        for (ItemStack tab : categories) miscTab.add(tab);

        miscTab.set(19, new ItemStackBuilder(Material.TNT).withName("&bTNT").build());
        miscTab.set(20, new ItemStackBuilder(Material.GOLDEN_APPLE).build());
        miscTab.set(21, new ItemStackBuilder(Material.ENDER_PEARL).withName("&bEnder Pearl").build());
        miscTab.set(22, new ItemStackBuilder(Material.FLINT_AND_STEEL).withName("&bFlint n' Steel").build());
        miscTab.set(23, new ItemStackBuilder(Material.FISHING_ROD).withName("&bFishing Rod").build());

        miscTab.freezeItems(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (inv.getTitle() == null) return;
        if (!inv.getTitle().contains("CTF Shop")) return;

        if (event.getSlot() > 16) {
            player.getInventory().addItem(item);
            return;
        }

        Material tab = item.getType();

        // Switching between tabs
        if (tab.equals(Material.WOOL)) getBlockTab().display(player);
        if (tab.equals(Material.GOLD_SWORD)) getWeaponTab().display(player);
        if (tab.equals(Material.GOLD_CHESTPLATE)) getArmorTab().display(player);
        if (tab.equals(Material.GOLD_PICKAXE)) getToolsTab().display(player);
        if (tab.equals(Material.BOW)) getArcheryTab().display(player);
        if (tab.equals(Material.BREWING_STAND_ITEM)) getPotionTab().display(player);
        if (tab.equals(Material.TNT)) getMiscTab().display(player);
    }

}
