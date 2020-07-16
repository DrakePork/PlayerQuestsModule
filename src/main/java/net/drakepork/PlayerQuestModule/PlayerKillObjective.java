package net.drakepork.PlayerQuestModule;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerKillObjective extends CustomObjective implements Listener {
	Quests qp = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");

	public PlayerKillObjective() {
		this.setName("Kill Guards");
		this.setAuthor("DrakePork");
		this.setShowCount(true);
		this.setCountPrompt("Enter how many guards a player must kill");
		this.setDisplay("Kill Guards: %count%");
	}

	@EventHandler
	public void OnPlayerDeath(PlayerDeathEvent event){
		if(event.getEntity().getKiller() instanceof Player) {
			Player killer = event.getEntity().getKiller();
			Player killed = event.getEntity().getPlayer();
			for (Quest quest : qp.getQuester(killer.getUniqueId()).getCurrentQuests().keySet()) {
				if (killed.hasPermission("cmi.command.list.group.9")) {
					incrementObjective(killer, this, 1, quest);
				}
			}
		}
	}
}