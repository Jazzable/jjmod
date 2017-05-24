package com.jayavery.jjmod.utilities;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyInteger implements IUnlistedProperty<Integer> {

    private final String name;
    
    public UnlistedPropertyInteger(String name) {
        
        this.name = name;
    }
    
    @Override
    public String getName() {

        return this.name;
    }

    @Override
    public boolean isValid(Integer value) {

        return value < 8;
    }

    @Override
    public Class<Integer> getType() {

        return Integer.class;
    }

    @Override
    public String valueToString(Integer value) {

        return value.toString();
    }

}
