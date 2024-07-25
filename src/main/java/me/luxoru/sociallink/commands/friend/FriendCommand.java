package me.luxoru.sociallink.commands.friend;

import me.luxoru.sociallink.commands.SocialLinkCommand;
import me.luxoru.sociallink.commands.SocialLinkCommandInfo;
import me.luxoru.sociallink.util.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@SocialLinkCommandInfo(name = "friend", aliases = {"fr"})
public class FriendCommand extends SocialLinkCommand {

    public FriendCommand() {
        registerSubCommand(new AddFriendCommand());
        registerSubCommand(new AcceptFriendCommand());
        registerSubCommand(new RemoveFriendCommand());
        registerSubCommand(new ListFriendCommand());


    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1){
            return getCommand("add").execute(sender, command, label, args);
        }
        String commandName = args[0];

        SocialLinkCommand cmd = getCommand(commandName);
        if(cmd == null) {
            return getCommand("add").execute(sender, command, label, args);
        }


        return cmd.execute(sender, command, label, ArrayUtils.removeFirstElement(args));

    }



}
