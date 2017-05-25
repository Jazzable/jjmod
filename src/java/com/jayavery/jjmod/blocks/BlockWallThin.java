package com.jayavery.jjmod.blocks;

import java.util.List;
import javax.annotation.Nullable;
import com.google.common.collect.Lists;
import com.jayavery.jjmod.render.block.WallRenderer;
import com.jayavery.jjmod.render.block.WallRendererSingle;
import com.jayavery.jjmod.utilities.BlockMaterial;
import com.jayavery.jjmod.utilities.BlockWeight;
import com.jayavery.jjmod.utilities.ToolType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.property.IExtendedBlockState;

/** Wall block with no height or thickness variation. */
public class BlockWallThin extends BlockWall {
    
    /** Base angle for rendering sides. */
    private final int baseAngle;
    
    public BlockWallThin(BlockMaterial material, String name,
            float hardness, ToolType toolType) {
                
        this(material, name, hardness, toolType, 180);
    }

    public BlockWallThin(BlockMaterial material, String name,
            float hardness, ToolType toolType, int baseAngle) {
        
        super(material, name, hardness, toolType);
        this.baseAngle = baseAngle;
    }
    
    @Override
    public WallRenderer getLoader() {
        
        if (this.renderer == null) {
            
            this.renderer = new WallRendererSingle(this.getRegistryName(),
                    this.baseAngle);
        }
        
        return this.renderer;
    }
    
    @Override
    public BlockWeight getWeight() {
        
        return BlockWeight.LIGHT;
    }
    
    @Override
    public boolean isDouble() {
        
        return false;
    }
    
    @Override
    public boolean shouldDouble(IBlockState state, EnumFacing facing) {
        
        return false;
    }
    
    @Override
    public void addCollisionBoxToList(IBlockState state, World world,
            BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> list,
            @Nullable Entity entity, boolean unused) {
        
        state = this.getExtendedState(state, world, pos);
        
        if (!(state instanceof IExtendedBlockState)) {
            
            return;
        }
        
        IExtendedBlockState extState = (IExtendedBlockState) state; 
        
        addCollisionBoxToList(pos, entityBox, list, CENTRE_POST_THIN);
        
        if (extState.getValue(NORTH) != null) {
            
            addCollisionBoxToList(pos, entityBox, list, BRANCH_NORTH_THIN);
        }
        
        if (extState.getValue(EAST) != null) {
            
            addCollisionBoxToList(pos, entityBox, list, BRANCH_EAST_THIN);
        }
        
        if (extState.getValue(SOUTH) != null) {
            
            addCollisionBoxToList(pos, entityBox, list, BRANCH_SOUTH_THIN);
        }
        
        if (extState.getValue(WEST) != null) {
            
            addCollisionBoxToList(pos, entityBox, list, BRANCH_WEST_THIN);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world,
            BlockPos pos) {

        return CENTRE_POST;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos,
            IBlockState state, int fortune) {

        return Lists.newArrayList(new ItemStack(Item.getItemFromBlock(this)));
    }
}