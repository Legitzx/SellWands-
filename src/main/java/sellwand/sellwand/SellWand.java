package sellwand.sellwand;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class SellWand extends JavaPlugin {
    public static SellWand plugin;

    private static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;

    public ItemStack sellwand = new ItemStack(Material.DIAMOND_HOE);

    @Override
    public void onEnable() {
        // Economy
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // plugin essentials
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "SellWands Plugin Successfully Enabled!");
        plugin = this;

        // Commands
        this.getCommand("sellwand").setExecutor(new Commands());

        // Events
        getServer().getPluginManager().registerEvents(new Events(), this);

        // Config
        loadConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "SellWands Plugin Disabled!");
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public ItemStack getSellwand(int uses, boolean infinite) {
        ArrayList<String> sellwandLore = new ArrayList<>();
        ItemMeta sellwandMeta = sellwand.getItemMeta();
        try {
            sellwandMeta.removeEnchant(Enchantment.DIG_SPEED);
            sellwandMeta.removeEnchant(Enchantment.SILK_TOUCH);
        } catch (NullPointerException ex) { // YAY
        }

        if(infinite) {
            String infiniteSellWandName = plugin.getConfig().getString("infinite-sellwand");
            sellwandMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "*" + ChatColor.YELLOW + ChatColor.BOLD + "*" + ChatColor.GOLD + ChatColor.BOLD + "*" + ChatColor.YELLOW +  " " + ChatColor.BOLD + ChatColor.UNDERLINE + infiniteSellWandName + ChatColor.RESET + " " + ChatColor.GOLD + ChatColor.BOLD + "*" + ChatColor.YELLOW + ChatColor.BOLD + "*" + ChatColor.WHITE + ChatColor.BOLD + "*");
            sellwandLore.add(ChatColor.RED + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "Infinite");
        }
        if(!infinite){
            String limitedSellWandName = plugin.getConfig().getString("limited-sellwand");
            sellwandMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "*" + ChatColor.YELLOW + ChatColor.BOLD + "*" + ChatColor.GOLD + ChatColor.BOLD + "*" + ChatColor.YELLOW +  " " + ChatColor.BOLD + ChatColor.UNDERLINE + limitedSellWandName + " [" + uses + "]" +  ChatColor.RESET + " " + ChatColor.GOLD + ChatColor.BOLD + "*" + ChatColor.YELLOW + ChatColor.BOLD + "*" + ChatColor.WHITE + ChatColor.BOLD + "*");
            sellwandMeta.addEnchant(Enchantment.SILK_TOUCH, 214, true);
            sellwandMeta.addEnchant(Enchantment.DIG_SPEED, uses, true);
        }

        sellwandLore.add(ChatColor.AQUA + "Left Click on a chest to sell");
        sellwandMeta.setLore(sellwandLore);

        sellwandMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        sellwandMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        sellwandMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        sellwandMeta.addEnchant(Enchantment.DURABILITY, 1312, true);

        sellwand.setItemMeta(sellwandMeta);
        //Bukkit.broadcastMessage(sellwandMeta.toString());
        return sellwand;
    }
}
