package dev.offlical.mccd;

import java.util.HashMap;

import java.util.Random;
import java.util.UUID;

import net.minecraft.server.v1_16_R2.IChatBaseComponent;
import net.minecraft.server.v1_16_R2.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin implements Listener {

	public HashMap<UUID,Cooldowns> CDmap;
	public Double cooldown;
	public static boolean cd;
	private static Main instance;
	public static boolean msgs;

	@Override
	public void onEnable() {
		instance = this;
		CDmap = new HashMap<UUID,Cooldowns>();
		loadConfig();
		cooldown = this.getConfig().getDouble("cooldown");
		cd = this.getConfig().getBoolean("enabled");
		msgs = this.getConfig().getBoolean("msgs");
		for(Player p: Bukkit.getOnlinePlayers()) {
			this.CDmap.put(p.getUniqueId(), new Cooldowns(p.getUniqueId()));
		}
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("mccooldown").setExecutor(new Cmds());
	}
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		this.CDmap.put(event.getPlayer().getUniqueId(), new Cooldowns(event.getPlayer().getUniqueId()));
		if(event.getPlayer().isOp()) {
			event.getPlayer().sendMessage("§aThanks for installing minecraft cooldown!");
			event.getPlayer().sendMessage("§aTo toggle it on/off do /mccooldown toggle");
			event.getPlayer().sendMessage("§aIf you're a youtuber that happens to be using this for a video, please credit my youtube in the §adescription!§a Thanks! §c(https://bit.ly/2CCbV4u)");
			event.getPlayer().sendMessage("§aPlugin made by Offlical.");
		}
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		this.CDmap.remove(event.getPlayer().getUniqueId());
	}
	
	public void loadConfig() {
		this.reloadConfig();
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}
	
	public void onDisable() {

	}
	public void sendActiobar(Player p,String msg) {
		IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg + "\"}");
		PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR,comp,0,2,0);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
	}

	public static boolean isMsgs() {
		return msgs;
	}

	public static void setMsgs(boolean msgs) {
		Main.msgs = msgs;
	}

	/*
	 * 
	 * SHIT TON OF EVENTS HOLY SHIT
	 * 
	 */
	@EventHandler
	public void onConsume(PlayerItemConsumeEvent event) {
		if(!this.getConfig().getBoolean("Events.CONSUME")) return;
		Cooldowns cd = CDmap.get(event.getPlayer().getUniqueId());
		if(cd.isCooldownOver("CONSUME")) {
			double i = cooldown;
			if(this.getConfig().getBoolean("randomCooldown")) {
				i = new Random().nextInt(cooldown.intValue());
				if(i == 0) i = 1;
			}
			cd.setConsumeCD((long) (System.currentTimeMillis() + (i * 1000)));
		}else {
			event.setCancelled(true);
			this.sendActiobar(event.getPlayer(), "§5You can't consume items for another " + ((double)(cd.getConsumeCD()  - System.currentTimeMillis())  / 1000 + " seconds!"));
		}
		
	}

	@EventHandler
	public void onBlockDamage(BlockDamageEvent event) {
		if(!this.getConfig().getBoolean("Events.BLOCK_DAMAGE")) return;
		Cooldowns cd = CDmap.get(event.getPlayer().getUniqueId());
		if(cd.isCooldownOver("BLOCK_DAMAGE")) {
			double i = cooldown;
			if(this.getConfig().getBoolean("randomCooldown")) {
				i = new Random().nextInt(cooldown.intValue());
				if(i == 0) i = 1;
			}
			cd.setBlockdmgCD((long) (System.currentTimeMillis() + (i * 1000)));
		}else {
			event.setCancelled(true);
			this.sendActiobar(event.getPlayer(), "§2You can't damage blocks for another " + (double)(cd.getBlockdmgCD() - System.currentTimeMillis()) / 1000 + " seconds!");
		}
	}
	
	@EventHandler
	public void onPickup(EntityPickupItemEvent event) {
		if(!this.getConfig().getBoolean("Events.PICKUP")) return;
		if(event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			Cooldowns cd = CDmap.get(p.getUniqueId());
			if(cd.isCooldownOver("PICKUP_ITEM")) {
				double i = cooldown;
				if(this.getConfig().getBoolean("randomCooldown")) {
					i = new Random().nextInt(cooldown.intValue());
					if(i == 0) i = 1;
				}
				cd.setPickupItemCD((long) (System.currentTimeMillis() + (i * 1000)));
			}else {
				event.setCancelled(true);
				this.sendActiobar(p, "§aYou can't pick up items for another " + (double)(cd.getPickupItemCD() - System.currentTimeMillis()) / 1000  + " seconds!");
			}
			}
	}
	
	@EventHandler
	public void onIgnite(BlockIgniteEvent event) {
		if(event.getPlayer() == null) return;
		if(!this.getConfig().getBoolean("Events.IGNITE")) return;
		Cooldowns cd = CDmap.get(event.getPlayer().getUniqueId());
		if(cd.isCooldownOver("IGNITE")) {
			double i = cooldown;
			if(this.getConfig().getBoolean("randomCooldown")) {
				i = new Random().nextInt(cooldown.intValue());
				if(i == 0) i = 1;
			}
			cd.setIgniteCD((long) (System.currentTimeMillis() + (i * 1000)));
		}else {
			event.setCancelled(true);
			this.sendActiobar(event.getPlayer(), "§8You can't ignite blocks for another " + (double)(cd.getIgniteCD() - System.currentTimeMillis()) / 1000  + " seconds!");
		}
		
	}
	
	@EventHandler
	public void onRegen(EntityRegainHealthEvent event) {
		if(!this.getConfig().getBoolean("Events.REGEN")) return;
		if(event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			Cooldowns cd = CDmap.get(p.getUniqueId());
			if(cd.isCooldownOver("REGENHP")) {
				double i = cooldown;
				if(this.getConfig().getBoolean("randomCooldown")) {
					i = new Random().nextInt(cooldown.intValue());
					i++;
				}
				cd.setRegenCD((long) (System.currentTimeMillis() + (i * 1000)));
			}else {
				event.setCancelled(true);
				long dif = cd.getRegenCD() - System.currentTimeMillis();
				this.sendActiobar(p, "§cYou can't regenerate health for another " + (double) dif / 1000  + " seconds!");
				if(!msgs) return;
				System.out.println(p.getName() + " can't regenerate health for " + (double)dif / 1000 + " seconds!");
			}
			}
	}
	
	
	@EventHandler
	public void onBucket(PlayerBucketFillEvent event) {
		if(!this.getConfig().getBoolean("Events.BUCKET")) return;
		Cooldowns cd = CDmap.get(event.getPlayer().getUniqueId());
		if(cd.isCooldownOver("BUCKET")) {
			double i = cooldown;
			if(this.getConfig().getBoolean("randomCooldown")) {
				i = new Random().nextInt(cooldown.intValue());
				if(i == 0) i = 1;
			}
			cd.setBucketCD((long) (System.currentTimeMillis() + (i * 1000)));
		}else {
			event.setCancelled(true);
			long dif = cd.getBucketCD() - System.currentTimeMillis();
			this.sendActiobar(event.getPlayer(), "§fYou can't fill a bucket for another " + (double)dif / 1000  + " seconds!");
			if(!msgs) return;
			System.out.println(event.getPlayer().getName() + " can't fill a bucket for " + (double)(cd.getBucketCD()  - System.currentTimeMillis()) / 1000 + " seconds!");
		}
	}
	
	
	@EventHandler
	public void onEmptyBucket(PlayerBucketEmptyEvent event) {
		if(!this.getConfig().getBoolean("Events.BUCKET_EMPTY")) return;
		Cooldowns cd = CDmap.get(event.getPlayer().getUniqueId());
		if(cd.isCooldownOver("BUCKET_EMPTY")) {
			double i = cooldown;
			if(this.getConfig().getBoolean("randomCooldown")) {
				i = new Random().nextInt(cooldown.intValue());
				if(i == 0) i = 1;
			}
			cd.setBucketEmptyCD((long) (System.currentTimeMillis() + (i * 1000)));
		}else {
			event.setCancelled(true);
			this.sendActiobar(event.getPlayer(), "§bYou you can't empty a bucket for another " + (double)(cd.getBucketEmptyCD()  - System.currentTimeMillis()) / 1000 + " seconds!");
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if(!this.getConfig().getBoolean("Events.DROP")) return;
		Cooldowns cd = CDmap.get(event.getPlayer().getUniqueId());
		if(cd.isCooldownOver("DROP_ITEM")) {
			double i = cooldown;
			if(this.getConfig().getBoolean("randomCooldown")) {
				i = new Random().nextInt(cooldown.intValue());
				if(i == 0) i = 1;
			}
			cd.setDropItemCD((long) (System.currentTimeMillis() + (i * 1000)));
		}else {
			event.setCancelled(true);

			this.sendActiobar(event.getPlayer(), "§dYou can't drop items for another " + (double)(cd.getDropItemCD() - System.currentTimeMillis()) / 1000  + " seconds!");

		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Cooldowns cd = CDmap.get(event.getPlayer().getUniqueId());
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
		if(!this.getConfig().getBoolean("Events.INTERACT_RIGHT")) return;
		if(cd.isCooldownOver("INTERACT_RIGHT")) {
			double i = cooldown;
			if(this.getConfig().getBoolean("randomCooldown")) {
				i = new Random().nextInt(cooldown.intValue());
				if(i == 0) i = 1;
			}
			cd.setInteractLeftCD((long) (System.currentTimeMillis() + (i * 1000)));
		}else {
			event.setCancelled(true);

			this.sendActiobar(event.getPlayer(), "§2You can't right click for another " + (double)(cd.getInteractRightCD()  - System.currentTimeMillis()) / 1000  + " seconds!");
		}
		}else if(event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_AIR)){
			if(!this.getConfig().getBoolean("Events.INTERACT_LEFT")) return;
			if(cd.isCooldownOver("INTERACT_LEFT")) {
				double i = cooldown;
				if(this.getConfig().getBoolean("randomCooldown")) {
					i = new Random().nextInt(cooldown.intValue());
					if(i == 0) i = 1;
				}
				cd.setInteractRightCD((long) (System.currentTimeMillis() + (i * 1000)));
			}else {
				event.setCancelled(true);

				this.sendActiobar(event.getPlayer(), "§3You can't left click for another " + (double)(cd.getInteractLeftCD() - System.currentTimeMillis()) / 1000  + " seconds!");
			}
		}
	}
	
	@EventHandler
	public void onBowShot(EntityShootBowEvent event) {
		if(!this.getConfig().getBoolean("Events.BOW_SHOT")) return;
		if(event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			Cooldowns cd = CDmap.get(p.getUniqueId());
			if(cd.isCooldownOver("BOW_SHOT")) {
				double i = cooldown;
				if(this.getConfig().getBoolean("randomCooldown")) {
					i = new Random().nextInt(cooldown.intValue());
					if(i == 0) i = 1;
				}
				cd.setBowShotCD((long) (System.currentTimeMillis() + (i * 1000)));
			}else {
				event.setCancelled(true);
				this.sendActiobar(p, "§6You can't shoot from a bow for another " + (double)(cd.getBowShotCD()  - System.currentTimeMillis()) / 1000 + " seconds!");
			}
			}
	}
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			Player p = (Player) event.getDamager();
			if(!this.getConfig().getBoolean("Events.DAMAGE")) return;
			Cooldowns cd = CDmap.get(p.getUniqueId());
			if(cd.isCooldownOver("DAMAGE_ENTITY")) {
				double i = cooldown;
				if(this.getConfig().getBoolean("randomCooldown")) {
					i = new Random().nextInt(cooldown.intValue());
					if(i == 0) i = 1;
				}
				cd.setDamageCD((long) (System.currentTimeMillis() + (i * 1000)));
			}else {
				event.setCancelled(true);
				this.sendActiobar(p, "§4You can't damage entities for another " + (double)(cd.getDamageCD() - System.currentTimeMillis()) / 1000 + " seconds!");
			}
			}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if(!this.getConfig().getBoolean("Events.BLOCK_PLACE")) return;

		Cooldowns cd = CDmap.get(event.getPlayer().getUniqueId());
		if(cd.isCooldownOver("PLACE")) {
			double i = cooldown;
			if(this.getConfig().getBoolean("randomCooldown")) {
				i = new Random().nextInt(cooldown.intValue());
				if(i == 0) i = 1;
			}
			cd.setPlaceCD((long) (System.currentTimeMillis() + (i * 1000)));
		}else {
			event.setCancelled(true);
			this.sendActiobar(event.getPlayer(), "§9You can't place blocks for another " + (double)(cd.getPlaceCD()  - System.currentTimeMillis()) / 1000 + " seconds!");
		}
	}
	
	@EventHandler
	public void onCraft(CraftItemEvent event) {
		if(!this.getConfig().getBoolean("Events.CRAFT")) return;
		Cooldowns cd = CDmap.get(event.getWhoClicked().getUniqueId());
		if(cd.isCooldownOver("CRAFT")) {
			double i = cooldown;
			if(this.getConfig().getBoolean("randomCooldown")) {
				i = new Random().nextInt(cooldown.intValue());
				if(i == 0) i = 1;
			}
			cd.setCraftCD((long) (System.currentTimeMillis() + (i * 1000)));
		}else {
			event.setCancelled(true);
			this.sendActiobar((Player) event.getWhoClicked(), "§3You can't craft for another " + (double)(cd.getCraftCD() - System.currentTimeMillis()) / 1000  + " seconds!");
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if(!this.getConfig().getBoolean("Events.BLOCK_BREAK")) return;
		Cooldowns cd = CDmap.get(event.getPlayer().getUniqueId());
		if(cd.isCooldownOver("BREAK")) {
			double i = cooldown;
			if(this.getConfig().getBoolean("randomCooldown")) {
				i = new Random().nextInt(cooldown.intValue());
				if(i == 0) i = 1;
			}
			cd.setBreakCD((long) (System.currentTimeMillis() + (i * 1000)));
		}else {
			event.setCancelled(true);
			this.sendActiobar(event.getPlayer(), "§7You can't break blocks for another " + (double)(cd.getBreakCD()  - System.currentTimeMillis()) / 1000+ " seconds!");
			if(!msgs) return;
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		
		Location from = event.getFrom();
		Location to = event.getTo();
		
		
		Player p = event.getPlayer();
		UUID uuid = p.getUniqueId();
		Cooldowns cd = CDmap.get(uuid);
		
		
		if(from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
			if(!this.getConfig().getBoolean("Events.MOVE_HORZ")) return;
			if(cd.isCooldownOver("MOVE_HORZ")) {
				double i = cooldown;
				if(this.getConfig().getBoolean("randomCooldown")) {
					i = new Random().nextInt(cooldown.intValue());
					if(i == 0) i = 1;
				}
				cd.setMoveHorzCD((long) (System.currentTimeMillis() + (i * 1000)));
			}else {
				event.setCancelled(true);
				this.sendActiobar(event.getPlayer(), "§cYou can't move horizontally for another " + (double)(cd.getMoveHorzCD()  - System.currentTimeMillis()) / 1000 + " seconds!");
			}
		}
		if(from.getBlockY() < to.getBlockY()) {
			if(!this.getConfig().getBoolean("Events.MOVE_UP")) return;
			if(cd.isCooldownOver("MOVE_UP")) {
				double i = cooldown;
				if(this.getConfig().getBoolean("randomCooldown")) {
					i = new Random().nextInt(cooldown.intValue());
					if(i == 0) i = 1;
				}
				cd.setMoveUpCD((long) (System.currentTimeMillis() + (i * 1000)));
			}else {
				event.setCancelled(true);
				this.sendActiobar(event.getPlayer(), "§cYou can't move upwards for another " + (double)(cd.getMoveUpCD()  - System.currentTimeMillis()) / 1000 + " seconds!");
			}
		}
		
	}


	public static boolean isCd() {
		return cd;
	}


	public static void setCd(boolean cd) {
		Main.cd = cd;
	}


	public Double getCooldown() {
		return cooldown;
	}


	public void setCooldown(Double cooldown) {
		this.cooldown = cooldown;
	}


	public static Main getInstance() {
		return instance;
	}


	public static void setInstance(Main instance) {
		Main.instance = instance;
	}
	
	
}
