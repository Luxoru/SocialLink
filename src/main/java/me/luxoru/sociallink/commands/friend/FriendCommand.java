package me.luxoru.sociallink.commands.friend;

import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.commands.SocialLinkCommand;
import me.luxoru.sociallink.commands.SocialLinkCommandInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@SocialLinkCommandInfo(name = "friend")
public class FriendCommand extends SocialLinkCommand {

    public FriendCommand(SocialLink plugin) {
        registerSubCommand("add", new AddFriendCommand(plugin));
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Invalid number of args");
        return false;
    }
}
