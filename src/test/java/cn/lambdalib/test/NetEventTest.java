/**
* Copyright (c) Lambda Innovation, 2013-2016
* This file is part of LambdaLib modding library.
* https://github.com/LambdaInnovation/LambdaLib
* Licensed under MIT, see project root for more information.
*/
package cn.lambdalib.test;

import cn.lambdalib.annoreg.core.LoadStage;
import cn.lambdalib.annoreg.core.Registrant;
import cn.lambdalib.annoreg.mc.RegCallback;
import cn.lambdalib.core.LambdaLib;
import cn.lambdalib.s11n.network.NetworkEvent;
import cn.lambdalib.s11n.network.NetworkS11n.NetworkS11nType;
import cn.lambdalib.s11n.SerializeNullable;
import cn.lambdalib.template.command.LICommandBase;
import cn.lambdalib.util.generic.RandUtils;
import cn.lambdalib.util.key.KeyHandler;
import cn.lambdalib.util.key.KeyHandlerRegistration.RegKeyHandler;
import cn.lambdalib.util.mc.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

// @Registrant
public class NetEventTest {

    @Registrant
    @NetworkS11nType
    public static class TestMessage {

        @SerializeNullable
        public String who;
        @SerializeNullable
        public String greetings;
        @SerializeNullable
        public String wtf;

        public int pp;

        @Override
        public String toString() {
            return String.format("OSU! Player %s with point %d pp says %s", who, pp,
                    greetings == null ? "Nothing" : greetings + "|" + wtf);
        }

    }

    static String[] players = { "FatNAT", "WeAthFolD", "kongren", "lyt99", null, null, null };
    static String[] greetings = { null, "23333333", "Hello World", "344444" };
    static String[] wtfs = { null, null, null, "?" };

    static TestMessage newMessage() {
        TestMessage ret = new TestMessage();

        ret.who = players[RandUtils.rangei(0, players.length)];
        ret.greetings = greetings[RandUtils.rangei(0, greetings.length)];
        ret.wtf = wtfs[RandUtils.rangei(0, wtfs.length)];
        ret.pp = RandUtils.rangei(50, 240);

        return ret;
    }

    // Server test: via command

    public static class TestCommand extends LICommandBase {

        @Override
        public String getName() {
            return "test_net";
        }

        @Override
        public String getUsage(ICommandSender ics) {
            return "/test_net: Test network message server->client";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
        {
            TestMessage msg = newMessage();
            LambdaLib.log.info("Server send " + msg);
            NetworkEvent.sendToAll(msg);
        }

    }

    // Client test: via key
    public static class TestKey extends KeyHandler {
        @SideOnly(Side.CLIENT)
        @Override
        public void onKeyDown() {
            TestMessage msg = newMessage();
            LambdaLib.log.info("Client send " + msg);
            NetworkEvent.sendToServer(msg);
        }
    }

    @SideOnly(Side.CLIENT)
    @RegKeyHandler(name = "testNetMsg", keyID = Keyboard.KEY_HOME)
    public static TestKey test;

    @RegCallback(stage= LoadStage.INIT)
    public static void init(FMLInitializationEvent evt) {
        NetworkEvent.listen(TestMessage.class, Side.SERVER, (msg, ctx) -> {
            PlayerUtils.sendChat(ctx.getServerHandler().player, "Server received: " + msg);
        });
    }

    @SideOnly(Side.CLIENT)
    @RegCallback(stage= LoadStage.INIT)
    public static void initClient(FMLInitializationEvent evt) {
        NetworkEvent.listen(TestMessage.class, Side.CLIENT, (msg, ctx) -> {
            EntityPlayer player = Minecraft.getMinecraft().player;
            PlayerUtils.sendChat(player, "Client received: " + msg);
        });
    }

}
