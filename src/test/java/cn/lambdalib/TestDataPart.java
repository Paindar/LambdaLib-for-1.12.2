package cn.lambdalib;

/**
 * Created by Paindar on 17/10/23.
 */

import cn.lambdalib.annoreg.core.Registrant;
import cn.lambdalib.util.datapart.DataPart;
import cn.lambdalib.util.datapart.EntityData;
import cn.lambdalib.util.datapart.RegDataPart;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;

@Registrant
@RegDataPart(value=EntityLivingBase.class,side= Side.SERVER)
public class TestDataPart extends DataPart<EntityLivingBase>
{
    public static TestDataPart get(EntityLivingBase target) {
        return EntityData.get(target).getPart(TestDataPart.class);
    }
    public TestDataPart(){}

    private int value=2333;
    public void add(){value++;}
    public int get(){return value;}
}
