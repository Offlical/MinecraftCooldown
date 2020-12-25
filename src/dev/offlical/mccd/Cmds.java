package dev.offlical.mccd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Cmds implements CommandExecutor{

	public Main plugin = Main.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("mccooldown") || cmd.getName().equalsIgnoreCase("mccd")) {
			if(!sender.isOp()) return true;
			if(args.length <= 0) {
				sender.sendMessage("§8-=- §bMinecraft Cooldown §8-=- ");
				sender.sendMessage("§7Cooldown is set to " + plugin.getCooldown());
				sender.sendMessage("§b/mccd set <number> - Sets the cooldown (in seconds)");
				sender.sendMessage("§b/mccd toggle - Toggles the plugin on/off");
				sender.sendMessage("§b/mccd togglemsgs - Toggles the messages on/off");
				sender.sendMessage("§b/mccd togglerandom - Toggles random cooldowns (from 0- the cooldown number set with the command/in the config)");
				sender.sendMessage("§bPlugin made by Offlical!");
			}else {
				switch(args[0].toLowerCase()) {
					case "set":
						if(args.length <= 1) {
							sender.sendMessage("§cUsage: /mccd set <number>");
							break;
						}else {
							Double cd = Double.parseDouble(args[1]);
							if(cd.equals(null)) {
								sender.sendMessage("§cInput a valid number!");
								break;
							}
							plugin.setCooldown(cd);
								plugin.getConfig().set("cooldown", cd);
								plugin.saveConfig();
								sender.sendMessage("§aCooldwon changed to " + cd + "!");
						}
						break;
					case "toggle":
						if(Main.isCd()) sender.sendMessage("§aCooldowns are now turned off!");
						if(!Main.isCd()) sender.sendMessage("§aCooldowns are now turned on!");
						Main.setCd(!Main.isCd());
						plugin.getConfig().set("enabled", Main.isCd());
						plugin.saveConfig();
						break;
					case "togglerandom":
						boolean toggled = plugin.getConfig().getBoolean("randomCooldown");
						if(toggled) sender.sendMessage("§aCooldowns are no longer random.");
						if(!toggled) sender.sendMessage("§aCooldowns are now random.");
						plugin.getConfig().set("randomCooldown", !toggled);
						plugin.saveConfig();
						break;
					case "help":
						sender.sendMessage("§8-=- §bMinecraft Cooldown §8-=- ");
						sender.sendMessage("§7Cooldown is set to " + plugin.getCooldown());
						sender.sendMessage("§b/mccd set <number> - Sets the cooldown (in seconds)");
						sender.sendMessage("§b/mccd toggle - Toggles the plugin on/off");
						sender.sendMessage("§b/mccd togglemsgs - Toggles the messages on/off");
						sender.sendMessage("§b/mccd togglerandom - Toggles random cooldowns (from 0- the cooldown number set with the command/in the config)");
						sender.sendMessage("§bPlugin made by Offlical!");
						break;
				}
			}
			
			
			
			
		}
		
		
		
		
		
		
		
		return true;
	}

}
