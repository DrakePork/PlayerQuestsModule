package net.drakepork.PlayerQuestsModule;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Map;

public class PlayerKillObjective extends CustomObjective implements Listener {
	Quests qp = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");

	public PlayerKillObjective() {
		this.setName("Player Kill Objective");
		this.setAuthor("DrakePork");
		this.setShowCount(true);
		this.setCountPrompt("Enter how many times to kill this/these players");
		this.addStringPrompt("Kill Type", "Write \"Permission\" or \"Player\" to define how to select kill target(s)", null);
		this.addStringPrompt("Permission/Player Name", "Set permission or player to check for on player killed", null);
		this.addStringPrompt("Objective Name", "Set name of objective", "Kill Specified Player(s)");
		this.setDisplay("%Objective Name%: %count%");
	}

	@EventHandler
	public void PlayerDeath(PlayerDeathEvent event){
		if(event.getEntity().getKiller() instanceof Player) {
			Player killer = event.getEntity().getKiller();
			Player killed = event.getEntity().getPlayer();
			for (Quest quest : qp.getQuester(killer.getUniqueId()).getCurrentQuests().keySet()) {
				Map<String, Object> map = getDataForPlayer(killer, this, quest);
				if(map.get("Kill Type") != null) {
					String killType = (String) map.get("Kill Type");
					if (killType.equalsIgnoreCase("permission")) {
						String permission = (String) map.get("Permission/Player Name");
						if (killed.hasPermission(permission)) {
							incrementObjective(killer, this, 1, quest);
						}
					} else if (killType.equalsIgnoreCase("player")) {
						Player killedP = Bukkit.getPlayer((String) map.get("Permission/Player Name"));
						if (killed.equals(killedP)) {
							incrementObjective(killer, this, 1, quest);
						}
					}
				}
			}
		}
	}
}