package cn.lambdalib.test;

import cn.lambdalib.annoreg.core.LoadStage;
import cn.lambdalib.annoreg.core.Registrant;
import cn.lambdalib.annoreg.mc.RegCallback;
import cn.lambdalib.s11n.network.NetworkMessage;
import cn.lambdalib.s11n.network.NetworkMessage.Listener;
import cn.lambdalib.s11n.network.NetworkS11n.NetworkS11nType;
import cn.lambdalib.util.key.KeyHandler;
import cn.lambdalib.util.key.KeyManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

import static java.lang.System.*;

// @Registrant
@NetworkS11nType
public class StaticMessageTest {

    private static final String ID = "www";

    @Listener(channel=ID, side={ Side.CLIENT, Side.SERVER })
    private static void onMessage0() {
        out.println("Message0");
    }

    @Listener(channel=ID, side={ Side.CLIENT, Side.SERVER })
    private static void onMessage1(int a) {
        out.println("Message1: " + a);
    }

    @RegCallback(stage= LoadStage.INIT)
    public static void init(FMLInitializationEvent evt) {
        KeyManager.dynamic.addKeyHandler("StaticMessageTest", Keyboard.KEY_I, new KeyHandler() {
            @Override
            public void onKeyDown() {
                out.println("Blabla");
                NetworkMessage.sendToServer(NetworkMessage.staticCaller(StaticMessageTest.class), ID, 233);
            }
        });
    }

}
