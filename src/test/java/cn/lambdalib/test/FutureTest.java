package cn.lambdalib.test;

import cn.lambdalib.annoreg.core.LoadStage;
import cn.lambdalib.annoreg.core.Registrant;
import cn.lambdalib.annoreg.mc.RegCallback;
import cn.lambdalib.s11n.network.Future;
import cn.lambdalib.s11n.network.NetworkMessage;
import cn.lambdalib.s11n.network.NetworkMessage.Listener;
import cn.lambdalib.s11n.network.NetworkS11n.NetworkS11nType;
import cn.lambdalib.template.command.LICommandBase;
import cn.lambdalib.util.generic.RandUtils;
import cn.lambdalib.util.key.KeyHandler;
import cn.lambdalib.util.key.KeyManager;
import cn.lambdalib.util.mc.PlayerUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

@Registrant
@NetworkS11nType
public class FutureTest {
    static final String MSG_QUERY = "query";

    public static class Command extends LICommandBase {

        @Override
        public String getName() {
            return "testfut";
        }

        @Override
        public String getUsage(ICommandSender ics) {
            return "nepnepne";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
        {
            NetworkMessage.sendTo(getCommandSenderAsPlayer(ics),
                    NetworkMessage.staticCaller(FutureTest.class),
                    MSG_QUERY,
                    Future.create((Integer input) -> {
                        sendChat(ics, "Get the value in server! " + input);
                    }));
        }
    }

    @RegCallback(stage= LoadStage.START_SERVER)
    public static void init(FMLServerStartingEvent evt) {
        CommandHandler cm = (CommandHandler) evt.getServer().getCommandManager();
        cm.registerCommand(new Command());
        KeyManager.dynamic.addKeyHandler("future_test", Keyboard.KEY_P, new KeyHandler() {
            @Override
            public void onKeyDown() {
                NetworkMessage.sendToServer(
                        NetworkMessage.staticCaller(FutureTest.class),
                        MSG_QUERY,
                        Future.create((Integer input) -> {
                            PlayerUtils.sendChat(getPlayer(), "Get the value in client! " + input);
                        }));
            }
        });
    }

    @Listener(channel=MSG_QUERY, side={Side.CLIENT, Side.SERVER})
    static void onQuery(Future<Integer> future) {
        int result = RandUtils.rangei(232, 236);
        future.sendResult(result);
    }
}
