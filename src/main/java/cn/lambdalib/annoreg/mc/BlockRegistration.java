/**
* Copyright (c) Lambda Innovation, 2013-2016
* This file is part of LambdaLib modding library.
* https://github.com/LambdaInnovation/LambdaLib
* Licensed under MIT, see project root for more information.
*/
package cn.lambdalib.annoreg.mc;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import cn.lambdalib.annoreg.base.RegistrationFieldSimple;
import cn.lambdalib.annoreg.core.LoadStage;
import cn.lambdalib.annoreg.core.RegistryTypeDecl;

import java.lang.reflect.Constructor;

@RegistryTypeDecl
public class BlockRegistration extends RegistrationFieldSimple<RegBlock, Block> {

    public BlockRegistration() {
        super(RegBlock.class, "Block");
        this.setLoadStage(LoadStage.PRE_INIT);
        
        this.addWork(RegBlock.OreDict.class, new PostWork<RegBlock.OreDict, Block>() {
            @Override
            public void invoke(RegBlock.OreDict anno, Block obj) throws Exception {
                OreDictionary.registerOre(anno.value(), obj);
            }
        });
        
        this.addWork(RegBlock.BTName.class, new PostWork<RegBlock.BTName, Block>() {
            @Override
            public void invoke(RegBlock.BTName anno, Block obj) throws Exception {
                obj.setUnlocalizedName(getCurrentMod().getPrefix() + anno.value());
                //obj.setBlockTextureName(getCurrentMod().getRes(anno.value()));
                //TODO set block texture
            }
        });
        
    }

    @Override
    protected void register(Block value, RegBlock anno, String field) throws Exception {
        value.setRegistryName(field);
        Constructor c=anno.item().getConstructor(Block.class);
        ItemBlock ib= (ItemBlock) c.newInstance(value);
        ib.setRegistryName(field);
        GameRegistry.findRegistry(Item.class).register(ib);
        GameRegistry.findRegistry(Block.class).register(value);
    }

}
