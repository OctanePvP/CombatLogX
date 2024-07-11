package com.github.sirblobman.combatlogx.command.combatlogx;

import com.github.sirblobman.api.language.replacer.Replacer;
import com.github.sirblobman.api.language.replacer.StringReplacer;
import com.github.sirblobman.combatlogx.api.ICombatLogX;
import com.github.sirblobman.combatlogx.api.command.CombatLogCommand;
import com.github.sirblobman.combatlogx.api.manager.ICombatManager;
import com.github.sirblobman.combatlogx.api.object.UntagReason;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SubCommandUntagAll extends CombatLogCommand {

    public SubCommandUntagAll(@NotNull ICombatLogX plugin) {
        super(plugin, "untagall");
        setPermissionName("combatlogx.command.combatlogx.untagall");
    }

    @Override
    protected boolean execute(@NotNull CommandSender sender, String @NotNull [] args) {
        Collection<? extends Player> targets = Bukkit.getServer().getOnlinePlayers();

        Replacer replacer = new StringReplacer("{number}", String.valueOf(targets.size()));

        ICombatLogX plugin = getCombatLogX();
        ICombatManager combatManager = plugin.getCombatManager();

        for (Player target : targets) {
           combatManager.untag(target, UntagReason.EXPIRE);
        }
        sendMessageWithPrefix(sender, "command.combatlogx.untag-all", replacer);
        return true;
    }
}
