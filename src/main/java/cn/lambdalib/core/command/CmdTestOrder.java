package cn.lambdalib.core.command;

import cn.lambdalib.annoreg.core.LoadStage;
import cn.lambdalib.annoreg.core.Registrant;
import cn.lambdalib.annoreg.mc.RegCallback;
import cn.lambdalib.core.LambdaLib;
import cn.lambdalib.s11n.network.NetworkS11n;
import cn.lambdalib.s11n.network.NetworkS11n.NetworkS11nType;
import cn.lambdalib.s11n.network.NetworkMessage;
import cn.lambdalib.template.command.LICommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Paindar on 17/10/22.
 */
@Registrant
@NetworkS11nType
public class CmdTestOrder extends LICommandBase
{
    @RegCallback(stage= LoadStage.PRE_INIT)
    public static void preInit(FMLPreInitializationEvent event){
        NetworkS11n.register(CmdTestOrder.class);
    }

    @RegCallback(stage= LoadStage.START_SERVER)
    public static void init(FMLServerStartingEvent event){
        CommandHandler cm = (CommandHandler) event.getServer().getCommandManager();
        if (LambdaLib.DEBUG) {
            cm.registerCommand(new CmdTestOrder());
        }
    }
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
        return "";
    }

    @NetworkMessage.Listener(channel="testChannel",side= Side.CLIENT)
    public void onReceive(String s){
        LambdaLib.log.info("from client receive message: "+s);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        LambdaLib.log.info("argument: "+args+" sender is "+sender);
        NetworkMessage.sendTo((EntityPlayer) sender.getCommandSenderEntity(),this,"testChannel","234");
    }
}
