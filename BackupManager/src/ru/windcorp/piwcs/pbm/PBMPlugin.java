/* 
 * PIWCS Backup Manager (PBM)
 * Copyright (C) 2019  PIWCS Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package ru.windcorp.piwcs.pbm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Timer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class PBMPlugin extends JavaPlugin {
	
	private static PBMPlugin inst = null;
	
	private final Timer timer = new Timer("PBM Timer", true);
	
	private static Path backupDirectory;
	
	private final PBMWorker worker = new PBMWorker();
	
	@Override
	public void onLoad() {
		inst = this;
	}

	@Override
	public void onEnable() {
		loadConfig();
		
		ZonedDateTime dateTime = ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS);
		
		int roundedHour = (dateTime.getHour() / 4 + 1) * 4;
		dateTime = dateTime.truncatedTo(ChronoUnit.DAYS).plusHours(roundedHour);
		
		timer.scheduleAtFixedRate(
				worker,
				Date.from(dateTime.toInstant()),
				4 * ChronoUnit.HOURS.getDuration().toMillis());
	}
	
	private void loadConfig() {
		saveDefaultConfig();
		
		backupDirectory = Paths.get(getConfig().getString("backup-directory"));
		
		if (!Files.isDirectory(backupDirectory)) {
			try {
				Files.createDirectories(backupDirectory);
			} catch (IOException e) {
				getLogger().warning("Could not create directory " + backupDirectory + ": " + e + ". Backups will fail when the directory does not exist");
			}
		}
	}

	@Override
	public void onDisable() {
		timer.cancel();
		inst = null;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.isOp()) {
			sender.sendMessage(ChatColor.RED + "Insufficient permissions");
			return true;
		}
		timer.schedule(new PBMWorker(), 0);
		return true;
	}
	
	public static PBMPlugin getInst() {
		return inst;
	}
	
	public static Path getBackupDirectory() {
		return backupDirectory;
	}
	
}
