package me.ampayne2.DropParty.command.interfaces;

import org.bukkit.command.CommandSender;

/**
 * The base layout for a command.
 */
public interface DropPartyCommand {

	/**
	 * The command executor
	 * 
	 * @param sender
	 *            The sender of the command
	 * @param args
	 *            The arguments sent with the command
	 */
	public void execute(CommandSender sender, String[] newArgs);
}
