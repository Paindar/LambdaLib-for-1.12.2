/**
* Copyright (c) Lambda Innovation, 2013-2016
* This file is part of LambdaLib modding library.
* https://github.com/LambdaInnovation/LambdaLib
* Licensed under MIT, see project root for more information.
*/
package cn.lambdalib.util.mc;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSelectors {
    
    public static final IBlockSelector

    filNothing = (world, pos, block) -> block != Blocks.AIR ,

    filNormalPos = (world,pos,block)-> world.getBlockState(pos).getBlock().isCollidable(),
    
    filEverything = (world, pos, block) -> false,

    filReplacable = (world,pos, block) -> !block.isReplaceable(world,pos);

}
