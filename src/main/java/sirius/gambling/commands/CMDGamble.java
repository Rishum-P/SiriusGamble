package sirius.gambling.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sirius.gambling.SiriusGambling;
import sirius.gambling.Slots;
import sirius.gambling.Cooldown;


public class CMDGamble implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;

            if (args.length == 0) {
                return help(sender);
            }


            if (Cooldown.isInCooldown(player.getUniqueId(),"Gamble")){
                int TimeLeft = Cooldown.getTimeLeft(player.getUniqueId(),"Gamble");

                if (TimeLeft <= 60) {
                    player.sendMessage("§e[§dSiriusGamble§e]§a You may use this again in:§e " + TimeLeft +  " seconds");
                }
                else {
                    long sec = TimeLeft % 60;
                    long minutes = TimeLeft % 3600 / 60;
                    player.sendMessage("§e[§dSiriusGamble§e]§a You may use this again in:§e " + minutes +  "m"+ sec + "s");
                }


            } else {

                if (args.length == 1) {
                    double bet = 0.0D;
                    try {
                        bet = Double.parseDouble(args[0]);
                    } catch (Exception e) {
                        return help(sender);
                    }
                    if (SiriusGambling.econ.has((OfflinePlayer) player, bet)) {
                        if (bet > 20000.0D) {
                            player.sendMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.GREEN + "Maximum bet is $20,000!");
                            return true;
                        }
                        if (bet < 50.0D) {
                            player.sendMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.GREEN + "Minimum bet is $50!");
                            return true;
                        }
                        Cooldown cd = new Cooldown(player.getUniqueId(), "Gamble", 600);
                        cd.start();
                        SiriusGambling.econ.withdrawPlayer((OfflinePlayer) player, bet);
                        Slots slot = new Slots(player, bet);
                        slot.createInv();
                    } else {
                        player.sendMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.GREEN + "You do not have enough money!");
                    }
                } else {
                    return help(sender);
                }


                return true;
            }
        } else {
            sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.GREEN + "Only players can use this command!");
        }

        return true;
    }

    public boolean help(CommandSender sender){
        sender.sendMessage(ChatColor.YELLOW + "---------------[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "]---------------");
        sender.sendMessage(ChatColor.GOLD + " Usage: " + ChatColor.GREEN + "/gamble (bet)");
        sender.sendMessage(ChatColor.RED + " How it works:");
        sender.sendMessage(ChatColor.GREEN + " Match 3 Colors = Win 3x Bet");
        sender.sendMessage(ChatColor.GREEN + " Match 2 Colors = Win 2x Bet");
        sender.sendMessage(ChatColor.GREEN + " Match No Colors = No Winnings");
        sender.sendMessage(ChatColor.GOLD + " Minimum Bet is: " + ChatColor.GREEN + "$50");
        sender.sendMessage(ChatColor.GOLD + " Maximum Bet is: " + ChatColor.GREEN + "$20,000");
        sender.sendMessage(ChatColor.GOLD + " Usage Cooldown: " + ChatColor.GREEN + "10 Minutes");
        sender.sendMessage(ChatColor.YELLOW + "--------------------------------------------");
        return true;
    }
}
