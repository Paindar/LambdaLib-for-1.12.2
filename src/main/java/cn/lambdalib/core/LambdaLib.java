/**
* Copyright (c) Lambda Innovation, 2013-2016
* This file is part of LambdaLib modding library.
* https://github.com/LambdaInnovation/LambdaLib
* Licensed under MIT, see project root for more information.
*/
package cn.lambdalib.core;

import cn.lambdalib.annoreg.core.LoadStage;
import cn.lambdalib.annoreg.core.RegistrationManager;
import cn.lambdalib.annoreg.core.RegistrationMod;
import cn.lambdalib.annoreg.mc.RegisterCallbackManager;
import cn.lambdalib.core.command.CmdMineStatistics;
import cn.lambdalib.multiblock.MsgBlockMulti;
import cn.lambdalib.s11n.network.NetworkEvent;
import cn.lambdalib.s11n.network.NetworkMessage;
import net.minecraft.command.CommandHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

@Mod(modid = "lambdalib", name = "LambdaLib", version = LambdaLib.VERSION, dependencies = "required-after:"+ LLModContainer.MODID)//
@RegistrationMod(pkg = "cn.lambdalib.", res = "lambdalib", prefix = "ll_")
public class LambdaLib {

    public static final String VERSION = "@VERSION@";

    /**
     * Whether we are in development (debug) mode.
     */
    public static final boolean DEBUG = VERSION.startsWith("@");

    public static final Logger log = FMLLog.log;

    private static Configuration config;

    // @RegMessageHandler.WrapperInstance
    public static final SimpleNetworkWrapper channel = NetworkRegistry.INSTANCE.newSimpleChannel("LambdaLib");

    public static Configuration getConfig() {
        return config;
    }

    @EventHandler()
    public void preInit(FMLPreInitializationEvent event) {
        log.info("Starting LambdaLib");
        log.info("Copyright (c) Lambda Innovation, 2013-2017");
        log.info("http://www.li-dev.cn/");

        //LIFMLGameEventDispatcher.init();

        config = new Configuration(event.getSuggestedConfigurationFile());

        // WrapperInstance causes bug, manual registering now
        /*
         * 考虑到这个严格的信道id....还是不要用RegCallback了比较好？
         * ——Paindar
         */
        channel.registerMessage(MsgBlockMulti.ReqHandler.class, MsgBlockMulti.Req.class, 0, Side.SERVER);
        channel.registerMessage(MsgBlockMulti.Handler.class, MsgBlockMulti.class, 1, Side.CLIENT);
        channel.registerMessage(NetworkEvent.MessageHandler.class, NetworkEvent.Message.class, 2, Side.CLIENT);
        channel.registerMessage(NetworkEvent.MessageHandler.class, NetworkEvent.Message.class, 3, Side.SERVER);
        channel.registerMessage(NetworkMessage.Handler.class, NetworkMessage.Message.class, 4, Side.CLIENT);
        channel.registerMessage(NetworkMessage.Handler.class, NetworkMessage.Message.class, 5, Side.SERVER);
        //

        RegistrationManager.INSTANCE.registerAll(this, "PreInit");
        RegisterCallbackManager.INSTANCE.registerAll(LoadStage.PRE_INIT,event);
    }

    @EventHandler()
    public void init(FMLInitializationEvent event) {
        RegistrationManager.INSTANCE.registerAll(this, "Init");
        RegisterCallbackManager.INSTANCE.registerAll(LoadStage.INIT,event);
    }

    @EventHandler()
    public void postInit(FMLPostInitializationEvent event) {
        RegistrationManager.INSTANCE.registerAll(this, "PostInit");
        RegisterCallbackManager.INSTANCE.registerAll(LoadStage.POST_INIT,event);
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        config.save();
    }

    @EventHandler()
    public void serverStarting(FMLServerStartingEvent event) {

        RegistrationManager.INSTANCE.registerAll(this, "StartServer");
        RegisterCallbackManager.INSTANCE.registerAll(LoadStage.START_SERVER,event);
    }

}
