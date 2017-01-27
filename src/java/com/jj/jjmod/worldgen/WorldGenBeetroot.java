package com.jj.jjmod.worldgen;

import java.util.Random;

import com.jj.jjmod.init.ModBlocks;
import com.jj.jjmod.worldgen.abstracts.WorldGenCrop;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class WorldGenBeetroot extends WorldGenCrop {

    public WorldGenBeetroot(World world, Random rand) {
        
        super(world, rand, ModBlocks.beetroot.getFullgrown(), 20, 3);
    }
}
