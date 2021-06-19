package sirius.gambling.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sirius.gambling.SiriusGambling;
import sirius.gambling.Slots;

public class CMDGamble implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player player = (Player)sender;
            if (args.length > 0) {
                if (args.length == 1) {
                    double bet = 0.0D;
                    try {
                        bet = Double.parseDouble(args[0]);
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.YELLOW + "---------------[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "]---------------");
                        player.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.GREEN + "/gamble (bet)");
                        player.sendMessage(ChatColor.RED + "How it works:");
                        player.sendMessage(ChatColor.GREEN + "Match 3 Colors = Win 3x Bet");
                        player.sendMessage(ChatColor.GREEN + "Match 2 Colors = Win 2x Bet");
                        player.sendMessage(ChatColor.GREEN + "Match No Colors = No Winnings");
                        player.sendMessage(ChatColor.GOLD + "Minimum Bet is: " + ChatColor.GREEN + "$50");
                        player.sendMessage(ChatColor.GOLD + "Maximum Bet is: " + ChatColor.GREEN + "$5000");
                        player.sendMessage(ChatColor.GOLD + "Usage Cooldown: " + ChatColor.GREEN + "10 Minutes");
                        player.sendMessage(ChatColor.YELLOW + "------------------------------------------");
                        return false;
                    }
                    if (SiriusGambling.econ.has((OfflinePlayer)player, bet)) {
                        if (bet > 5000.0D) {
                            player.sendMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.GREEN + "Maximum bet is $5000!");
                            return true;
                        }
                        if (bet < 50.0D) {
                            player.sendMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.GREEN + "Minimum bet is $50!");
                            return true;
                        }
                        SiriusGambling.econ.withdrawPlayer((OfflinePlayer)player, bet);
                        Slots slot = new Slots(player, bet);
                        slot.createInv();
                    } else {
                        player.sendMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.GREEN + "You do not have enough money!");
                    }
                } else {
                    return false;
                }
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /gamble slots (bet)");
                return true;
            }
            return true;
        }
        return false;
    }
}
