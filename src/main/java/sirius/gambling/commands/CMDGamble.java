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

import java.util.List;


public class CMDGamble implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;

            if (args.length == 0) {
                return help(sender);
            }

            if (args[0].equalsIgnoreCase("stats")) {
                return printstats(sender);
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
                        Cooldown cd = new Cooldown(player.getUniqueId(), "Gamble", 5);
                        cd.start();
                        SiriusGambling.econ.withdrawPlayer(player, bet);
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

    public boolean printstats(CommandSender sender){

        Player player = (Player) sender;

        if (!SiriusGambling.siriusGambling.getSQLManager().PlayerExists(player.getUniqueId().toString())) {
            sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.GREEN + "You have yet to gamble!");
            return true;
        }

        List<Number> data = SiriusGambling.siriusGambling.getSQLManager().GetData(player.getUniqueId().toString());
        int Games_Played = (int) data.get(1);
        int Games_Won = (int) data.get(2);
        int Amount_Won = (int) data.get(3);
        int Amount_Lost = (int) data.get(4);
        int Total_Bet = (int) data.get(5);

        double WinPercent = Math.round((((double)Games_Won / (double)Games_Played) * 100d));

        sender.sendMessage(ChatColor.YELLOW + "---------------[" + ChatColor.LIGHT_PURPLE + "SiriusGamble Stats" + ChatColor.YELLOW + "]---------------");
        sender.sendMessage(ChatColor.GOLD + " Games Played: " + ChatColor.GREEN + Games_Played);
        sender.sendMessage(ChatColor.GOLD + " Games Won: " + ChatColor.GREEN + Games_Won);
        sender.sendMessage(ChatColor.GOLD + " Games Lost: " + ChatColor.GREEN + (Games_Played - Games_Won));
        sender.sendMessage(ChatColor.GOLD + " Win Percentage: " + ChatColor.GREEN + WinPercent + "%");
        sender.sendMessage(ChatColor.GOLD + " Amount Won: " + ChatColor.GREEN + "$" + Amount_Won);
        sender.sendMessage(ChatColor.GOLD + " Amount Lost: " + ChatColor.GREEN + "$" + Amount_Lost);
        sender.sendMessage(ChatColor.GOLD + " Total Spent: " + ChatColor.GREEN + "$" + Total_Bet);
        sender.sendMessage(ChatColor.GOLD + " Profit/Loss: " + ChatColor.GREEN + "$" + (Amount_Won - Amount_Lost));
        sender.sendMessage(ChatColor.YELLOW + "-----------------------------------------------");

        return true;
    }
}
