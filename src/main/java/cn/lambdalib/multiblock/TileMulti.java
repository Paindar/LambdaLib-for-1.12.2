/**
* Copyright (c) Lambda Innovation, 2013-2016
* This file is part of LambdaLib modding library.
* https://github.com/LambdaInnovation/LambdaLib
* Licensed under MIT, see project root for more information.
*/
package cn.lambdalib.multiblock;

import cn.lambdalib.annoreg.mc.RegTileEntity;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import cn.lambdalib.annoreg.core.Registrant;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author WeathFolD
 *
 */
@Registrant
@RegTileEntity
public class TileMulti extends TileEntity implements IMultiTile {

    InfoBlockMulti info = new InfoBlockMulti(this);


    public void updateEntity() {
        if (info != null)
            info.update();
    }

    @Override
    public InfoBlockMulti getBlockInfo() {
        return info;
    }

    @Override
    public void setBlockInfo(InfoBlockMulti i) {
        info = i;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        info = new InfoBlockMulti(this, nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        info.save(nbt);
        return nbt;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        Block block = getBlockType();
        if (block instanceof BlockMulti) {
            return ((BlockMulti) block).getRenderBB(pos.getX(),pos.getY(),pos.getZ(), info.getDir());
        } else {
            return super.getRenderBoundingBox();
        }
    }

}
