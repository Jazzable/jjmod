package com.jayavery.jjmod.blocks;

import com.jayavery.jjmod.utilities.BlockMaterial;
import com.jayavery.jjmod.utilities.IBuildingBlock;
import com.jayavery.jjmod.utilities.ToolType;
import net.minecraft.block.material.MapColor;
import net.minecraft.creativetab.CreativeTabs;

/** Fence block. */
public class BlockFence extends net.minecraft.block.BlockFence
        implements IBuildingBlock {
    
    public BlockFence() {
        
        super(BlockMaterial.WOOD_FURNITURE, MapColor.WOOD);
        BlockNew.setupBlock(this, "fence",CreativeTabs.BUILDING_BLOCKS,
                2, ToolType.AXE);
    }

    @Override
    public boolean isLight() {

        return true;
    }

    @Override
    public boolean isHeavy() {

        return false;
    }

    @Override
    public boolean isDouble() {

        return false;
    }
    
    @Override
    public boolean supportsBeam() {
        
        return false;
    }
    
    @Override
    public boolean isShelter() {
        
        return false;
    }
}
