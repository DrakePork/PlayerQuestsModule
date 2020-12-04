package net.drakepork.PlayerQuestsModule;

import com.github.drakepork.royalasylumcore.Core;
import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TreeChopObjective extends CustomObjective {
	Quests qp = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");
	Core ra = JavaPlugin.getPlugin(Core.class);

	public TreeChopObjective() {
		this.setName("Tree Chop Objective");
		this.setAuthor("DrakePork");
		this.setShowCount(true);
		this.setCountPrompt("Enter how many times one has to chop wood");
		this.setDisplay("Chop " + ChatColor.WHITE + "Birch Log"+ ChatColor.GREEN + ": %count%");
	}

	@EventHandler
	public void treeChopper(BlockBreakEvent event) {
		Block b = event.getBlock();
		TreeChopObjective cObjective = this;
		if(b.getType() == Material.BIRCH_LOG || b.getType() == Material.AIR) {
			Player player = event.getPlayer();
			for (Quest quest : qp.getQuester(player.getUniqueId()).getCurrentQuests().keySet()) {
				ra.getServer().getScheduler().scheduleSyncDelayedTask(ra, new Runnable() {
					public void run() {
						Integer blockChopped = ra.getBlocksChopped(player);
						incrementObjective(player, cObjective, blockChopped, quest);
					}
				},20L);
			}
		}
	}
}
