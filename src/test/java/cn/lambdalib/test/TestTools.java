package cn.lambdalib.test;

import cn.lambdalib.annoreg.core.LoadStage;
import cn.lambdalib.annoreg.core.Registrant;
import cn.lambdalib.annoreg.mc.RegCallback;
import cn.lambdalib.annoreg.mc.RegChestContent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Paindar on 17/10/19.
 */
@Registrant
public class TestTools extends Item
{

    public TestTools(){
        setUnlocalizedName("am.test_tools");
        setMaxDamage(0);
        setCreativeTab(CreativeTabs.TOOLS);
        setRegistryName("test_tools");

    }

    @RegChestContent(prob=100)
    public static Item testTools=new TestTools();

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(testTools);
        MinecraftForge.EVENT_BUS.unregister(testTools);
    }

    @RegCallback(stage= LoadStage.PRE_INIT)
    public static void preInit(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(testTools);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker){
        return true;
    }
}
