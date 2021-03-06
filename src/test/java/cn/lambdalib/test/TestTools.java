package cn.lambdalib.test;

import cn.lambdalib.annoreg.core.LoadStage;
import cn.lambdalib.annoreg.core.Registrant;
import cn.lambdalib.annoreg.mc.RegCallback;
import cn.lambdalib.annoreg.mc.RegChestContent;
import cn.lambdalib.core.LambdaLib;
import cn.lambdalib.util.mc.Raytrace;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
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

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        RayTraceResult result= Raytrace.traceLiving(playerIn,10);
        if(result==null)
            LambdaLib.log.info("trace nothing.");
        else if(result.typeOfHit== RayTraceResult.Type.BLOCK)
            LambdaLib.log.info("result block = " + result.getBlockPos());
        else if(result.typeOfHit== RayTraceResult.Type.ENTITY)
            LambdaLib.log.info("result entity = " + result.entityHit);
        else
            LambdaLib.log.info("result nothing");
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }
}
