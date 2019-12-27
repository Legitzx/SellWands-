package sellwand.sellwand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    private int numberOfSellwands = 0;
    private int uses = 0;
    private Player target;
    private boolean infinite = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("sellwand")) {
            if(args[0].equalsIgnoreCase("getid")) {
                if(sender instanceof Player) {
                    if(sender.hasPermission("sellwand.admin.give")) {
                        sender.sendMessage(((Player) sender).getItemInHand().getType().toString());
                        return true;
                    }
                }
                return true;
            }
            if(sender instanceof Player) {
                if(args.length < 1) {
                    sender.sendMessage(ChatColor.GREEN + "Usages for SellWand: ");
                    sender.sendMessage(ChatColor.GREEN + " - /sellwand give <ign> <#of sellwands> <uses/infinite>");
                } else {
                    if(args[0].equalsIgnoreCase("give")) {
                        if(sender.hasPermission("sellwand.admin.give")) {
                            Player player = (Player) sender;

                            try {
                                Bukkit.getPlayer(args[1]).isOnline();
                                target = Bukkit.getPlayer(args[1]);
                            } catch (NullPointerException ex) {
                                player.sendMessage(ChatColor.RED + "Targeted player is not online!");
                                return true;
                            }

                            // Player is online: PROCEED
                            try {
                                numberOfSellwands = Integer.parseInt(args[2]);
                            } catch (Exception e) {
                                sender.sendMessage(ChatColor.GREEN + "Usages for SellWand: ");
                                sender.sendMessage(ChatColor.GREEN + " - /sellwand give <ign> <#of sellwands> <uses/infinite>");
                                return true;
                            }


                            if(args[3].equalsIgnoreCase("infinite")) {
                                // Do if infinite
                                infinite = true;
                                //player.sendMessage("# of sellwands: " + numberOfSellwands);
                                //player.sendMessage("INFINITE");
                                // TODO: LOGIC FOR INFINITE SELL WAND
                                for(int x = 0; x < numberOfSellwands; x++) {
                                    target.getInventory().addItem(SellWand.plugin.getSellwand(uses, infinite));
                                }
                                return true;
                            } else {
                                try {
                                    uses = Integer.parseInt(args[3]);
                                } catch (Exception e) {
                                    sender.sendMessage(ChatColor.GREEN + "Usages for SellWand: ");
                                    sender.sendMessage(ChatColor.GREEN + " - /sellwand give <ign> <#of sellwands> <uses/infinite>");
                                    return true;
                                }
                                infinite = false;
                                //player.sendMessage("# of sellwands: " + numberOfSellwands);
                                //player.sendMessage("Uses: " + uses);
                                // TODO: LOGIC FOR USES SELL WAND
                                for(int x = 0; x < numberOfSellwands; x++) {
                                    target.getInventory().addItem(SellWand.plugin.getSellwand(uses, infinite));
                                }
                                return true;
                            }

                        }
                    }
                }
            }
        }


        return true;
    }
}
