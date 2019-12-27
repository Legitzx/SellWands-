package sellwand.sellwand;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Events implements Listener {
    private int amount = 0;

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {
        if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if(e.getPlayer().getItemInHand().getEnchantments().toString().equalsIgnoreCase("{Enchantment[34, DURABILITY]=1312}") || e.getPlayer().getItemInHand().getEnchantments().toString().equalsIgnoreCase("{Enchantment[minecraft:unbreaking, DURABILITY]=1312}")) { // If Unlimited full buffer version
                Material m = e.getClickedBlock().getType();
                if(m == Material.CHEST || m == Material.TRAPPED_CHEST) {
                    amount = 0;
                    //e.getPlayer().sendMessage("SELLWAND");
                    Chest c = (Chest) e.getClickedBlock().getState();
                    ItemStack[] cInv = c.getInventory().getContents();

                    for(ItemStack i : cInv) {
                        if(i != null) {
                            try {
                                SellWand.plugin.getConfig().getString(i.getType().toString()).isEmpty();
                                //e.getPlayer().sendMessage("Found in config - " + i.getType().toString());

                                amount += SellWand.plugin.getConfig().getInt(i.getType().toString()) * i.getAmount();
                                c.getInventory().remove(i);

                            } catch (NullPointerException ex) {
                                //e.getPlayer().sendMessage("Not Found in Config - " + i.getType().toString());
                            }

                        }
                    }
                    EconomyResponse r = SellWand.econ.depositPlayer(e.getPlayer(), amount);
                    if(r.transactionSuccess()) {
                        e.getPlayer().sendMessage(String.format(ChatColor.GREEN + "You were given %s and now have %s", SellWand.econ.format(r.amount), SellWand.econ.format(r.balance)));
                        return;
                    } else {
                        e.getPlayer().sendMessage(String.format(ChatColor.RED + "An error occured: %s", r.errorMessage));
                        return;
                    }

                }
                return;
            }

            if(e.getPlayer().getItemInHand().getEnchantments().toString().contains("Enchantment[minecraft:silk_touch, SILK_TOUCH]=214,")) { // If Limited 1.14ish
                String[] usesArr = e.getPlayer().getItemInHand().getEnchantments().toString().split(" ");
                int uses = 0;
                for(int x = 0; x < usesArr.length; x++) {
                    if(usesArr[x].contains("DIG_SPEED")) {
                        uses = Integer.parseInt(usesArr[x].replaceAll("\\D", ""));
                    }
                }

                uses = uses - 1;
                //System.out.println(uses);

                Material m = e.getClickedBlock().getType();
                if(m == Material.CHEST || m == Material.TRAPPED_CHEST) {
                    if(uses >= 0) {
                        if(uses == 0) {
                            e.getPlayer().getInventory().remove(e.getPlayer().getItemInHand());
                        } else {
                            ItemMeta newMeta = e.getPlayer().getItemInHand().getItemMeta();
                            newMeta.removeEnchant(Enchantment.DIG_SPEED);
                            newMeta.addEnchant(Enchantment.DIG_SPEED, uses, true);
                            newMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "*" + ChatColor.YELLOW + ChatColor.BOLD + "*" + ChatColor.GOLD + ChatColor.BOLD + "*" + ChatColor.YELLOW +  " " + ChatColor.BOLD + ChatColor.UNDERLINE + SellWand.plugin.getConfig().getString("limited-sellwand") + " [" + uses + "]" +  ChatColor.RESET + " " + ChatColor.GOLD + ChatColor.BOLD + "*" + ChatColor.YELLOW + ChatColor.BOLD + "*" + ChatColor.WHITE + ChatColor.BOLD + "*");
                            e.getPlayer().getItemInHand().setItemMeta(newMeta);



                            amount = 0;
                            //e.getPlayer().sendMessage("SELLWAND");
                            Chest c = (Chest) e.getClickedBlock().getState();
                            ItemStack[] cInv = c.getInventory().getContents();

                            for(ItemStack i : cInv) {
                                if(i != null) {
                                    try {
                                        SellWand.plugin.getConfig().getString(i.getType().toString()).isEmpty();
                                        //e.getPlayer().sendMessage("Found in config - " + i.getType().toString());

                                        amount += SellWand.plugin.getConfig().getInt(i.getType().toString()) * i.getAmount();
                                        c.getInventory().remove(i);

                                    } catch (NullPointerException ex) {
                                        //e.getPlayer().sendMessage("Not Found in Config - " + i.getType().toString());
                                    }

                                }
                            }
                            EconomyResponse r = SellWand.econ.depositPlayer(e.getPlayer(), amount);
                            if(r.transactionSuccess()) {
                                e.getPlayer().sendMessage(String.format(ChatColor.GREEN + "You were given %s and now have %s", SellWand.econ.format(r.amount), SellWand.econ.format(r.balance)));
                                return;
                            } else {
                                e.getPlayer().sendMessage(String.format(ChatColor.RED + "An error occured: %s", r.errorMessage));
                                return;
                            }
                        }
                    }
                }
                return;
            }

            if(e.getPlayer().getItemInHand().getEnchantments().toString().contains("SILK_TOUCH]=214")) { // If Limited 1.8ish
                String[] usesArr = e.getPlayer().getItemInHand().getEnchantments().toString().split(" ");

                int uses = Integer.parseInt(usesArr[1].replaceAll("\\D", ""));
                uses = uses - 1;
                //System.out.println(uses);

                Material m = e.getClickedBlock().getType();
                if(m == Material.CHEST || m == Material.TRAPPED_CHEST) {
                    if(uses >= 0) {
                        if(uses == 0) {
                            e.getPlayer().getInventory().remove(e.getPlayer().getItemInHand());
                        } else {
                            ItemMeta newMeta = e.getPlayer().getItemInHand().getItemMeta();
                            newMeta.removeEnchant(Enchantment.DIG_SPEED);
                            newMeta.addEnchant(Enchantment.DIG_SPEED, uses, true);
                            newMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "*" + ChatColor.YELLOW + ChatColor.BOLD + "*" + ChatColor.GOLD + ChatColor.BOLD + "*" + ChatColor.YELLOW +  " " + ChatColor.BOLD + ChatColor.UNDERLINE + SellWand.plugin.getConfig().getString("limited-sellwand") + " [" + uses + "]" +  ChatColor.RESET + " " + ChatColor.GOLD + ChatColor.BOLD + "*" + ChatColor.YELLOW + ChatColor.BOLD + "*" + ChatColor.WHITE + ChatColor.BOLD + "*");
                            e.getPlayer().getItemInHand().setItemMeta(newMeta);



                            amount = 0;
                            //e.getPlayer().sendMessage("SELLWAND");
                            Chest c = (Chest) e.getClickedBlock().getState();
                            ItemStack[] cInv = c.getInventory().getContents();

                            for(ItemStack i : cInv) {
                                if(i != null) {
                                    try {
                                        SellWand.plugin.getConfig().getString(i.getType().toString()).isEmpty();
                                        //e.getPlayer().sendMessage("Found in config - " + i.getType().toString());

                                        amount += SellWand.plugin.getConfig().getInt(i.getType().toString()) * i.getAmount();
                                        c.getInventory().remove(i);

                                    } catch (NullPointerException ex) {
                                        //e.getPlayer().sendMessage("Not Found in Config - " + i.getType().toString());
                                    }

                                }
                            }
                            EconomyResponse r = SellWand.econ.depositPlayer(e.getPlayer(), amount);
                            if(r.transactionSuccess()) {
                                e.getPlayer().sendMessage(String.format(ChatColor.GREEN + "You were given %s and now have %s", SellWand.econ.format(r.amount), SellWand.econ.format(r.balance)));
                                return;
                            } else {
                                e.getPlayer().sendMessage(String.format(ChatColor.RED + "An error occured: %s", r.errorMessage));
                                return;
                            }
                        }
                    }
                }
                return;
            }
        }
    }
}
