/**
* Copyright (c) Lambda Innovation, 2013-2016
* This file is part of LambdaLib modding library.
* https://github.com/LambdaInnovation/LambdaLib
* Licensed under MIT, see project root for more information.
*/
package cn.lambdalib.multiblock;

import java.util.List;

import cn.lambdalib.multiblock.BlockMulti.SubBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * @author WeathFolD
 */
public class ItemBlockMulti extends ItemBlock {

    /**
     * @param block
     */
    public ItemBlockMulti(Block block) {
        super(block);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        Block block = world.getBlockState(pos).getBlock();

        if (block == Blocks.SNOW_LAYER && (world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)) & 7) < 1) {
            side = EnumFacing.UP;
        } else if (block != Blocks.VINE && block != Blocks.TALLGRASS && block != Blocks.DEADBUSH
                && !block.isReplaceable(world,pos)) {
            pos.add(side.getDirectionVec());
        }
        ItemStack stack = player.getHeldItem(hand);

        if (stack.getCount() == 0)
            return EnumActionResult.FAIL;
        if (!player.canPlayerEdit(pos, side, stack))
            return EnumActionResult.FAIL;
        if (pos.getY() == 255 && this.block.getMaterial(null).isSolid())
            return EnumActionResult.FAIL;
        if (world.mayPlace(this.block,pos, false, side, player)) {
            int i = this.getMetadata(stack.getMetadata());
            /*
             * Called by ItemBlocks after a block is set in the world, to allow post-place logic
             */
            this.block.onBlockPlacedBy(world,pos,this.block.getDefaultState(),player,stack);

            // Further validation with BlockMulti logic
            BlockMulti bm = (BlockMulti) this.block;
            int l = MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
            List<SubBlockPos> list = bm.buffer[bm.getRotation(l).ordinal()];
            for (SubBlockPos s : list) {
                Block t = world.getBlockState(pos.add(s.dx, s.dy,  s.dz)).getBlock();
                if (!t.isReplaceable(world, pos)) {
                    return EnumActionResult.FAIL;
                }
            }
            IBlockState iblockstate1 = this.block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, i, player, hand);
            if (placeBlockAt(stack, player, world,pos, side, hitX,hitY, hitZ,block.getDefaultState())) {
                SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, world, pos, player);
                world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

                stack.setCount(stack.getCount()-1);
            }

            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

}
