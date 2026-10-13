package com.manbear.entity;

import net.minecraft.world.entity.EntityDimensions;

public enum Stance {
    STAND(EntityDimensions.scalable(1.3F, 5.0F)),
    CROUCH(EntityDimensions.scalable(1.3F, 3.3F)),
    CRAWL(EntityDimensions.scalable(0.9F, 0.9F));

    public final EntityDimensions dims;

    Stance(EntityDimensions dims) { this.dims = dims; }

    public static Stance byId(int id) {
        Stance[] v = values();
        return v[Math.floorMod(id, v.length)];
    }
}
