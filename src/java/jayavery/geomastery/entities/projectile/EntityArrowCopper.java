package jayavery.geomastery.entities.projectile;

import jayavery.geomastery.main.GeoItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityArrowCopper extends EntityProjectile {
    
    /** Damager done by this arrow. */
    private static final double DAMAGE = 1.33D;

    public EntityArrowCopper(World world) {

        super(world, DAMAGE);
    }

    public EntityArrowCopper(World world, double x, double y, double z) {

        super(world, x, y, z, DAMAGE);
    }

    public EntityArrowCopper(World world, EntityLivingBase shooter) {

        super(world, shooter, DAMAGE);
    }

    @Override
    protected ItemStack getArrowStack() {

        return new ItemStack(GeoItems.arrowCopper);
    }
}