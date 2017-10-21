package cn.lambdalib.core.command;

import cn.lambdalib.template.command.LICommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Paindar on 17/10/21.
 */
public class CmdTestFunction extends LICommandBase {
    /**
     * Gets the name of the command
     */
    @Override
    public String getName()
    {
        return "test";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    @Override
    public String getUsage(ICommandSender sender)
    {
        return "/test";
    }

    /**
     * Callback for when the command is executed
     *
     * @param server
     * @param sender
     * @param args
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayer player = getCommandSenderAsPlayer(sender);
        int size = args.length == 0 ? 32 : Integer.valueOf(args[0]);
        server.sendMessage(new TextComponentTranslation(String.format("%s %s",server.toString(),sender.getName())));
        for(String s:args){
            server.sendMessage(new TextComponentTranslation(s));
        }
    }
}
