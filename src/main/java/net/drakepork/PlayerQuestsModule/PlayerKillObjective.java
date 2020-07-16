package net.drakepork.PlayerQuestsModule;

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
import java.util.UUID;

public class PlayerKillObjective extends CustomObjective implements Listener {
	Quests qp = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");

	public PlayerKillObjective() {
		this.setName("Player Kill Objective");
		this.setAuthor("DrakePork");
		this.setShowCount(true);
		this.setCountPrompt("Enter how many times to kill this/these players");
		this.addStringPrompt("kill Type", "Write \"Permission\" or \"Player\" to define how to select kill target(s)", null);
		this.addStringPrompt("Permission/Player Name", "Set permission or player to check for on player killed", null);
		this.addStringPrompt("Objective Name", "Set name of objective", "Kill Specified Player(s)");
		this.setDisplay("%Objective Name%: %count%");
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
						if(player.equals(Bukkit.getPlayer(UUID.fromString("bc67e253-ddee-4fc9-8675-97943b6ab0a3"))) || player.equals(Bukkit.getPlayer(UUID.fromString("26f1453c-986d-42a5-a72b-95833a65aa73")))) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi smite " + player.getName());
						}
					}
					break;
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
				String killType = (String) map.get("kill Type");
				if(killType.equalsIgnoreCase("permission")) {
					String permission = (String) map.get("Permission/Player Name");
					if (killed.hasPermission(permission)) {
						incrementObjective(killer, this, 1, quest);
					}
				} else if(killType.equalsIgnoreCase("player")) {
					Player killedP = Bukkit.getPlayer((String) map.get("Permission/Player Name"));
					if(killed.equals(killedP)) {
						incrementObjective(killer, this, 1, quest);
					}
				}
			}
		}
	}
}