package com.lenis0012.bukkit.npc;

import net.minecraft.server.v1_7_R3.*;
import org.bukkit.*;

public class NPCPath
{
    private final NPCEntity entity;
    private final PathEntity nmsPath;
    private final double speed;
    private double progress;
    private Vec3D currentPoint;
    
    protected NPCPath(final NPCEntity entity, final PathEntity nmsPath, final double speed) {
        super();
        this.entity = entity;
        this.nmsPath = nmsPath;
        this.speed = speed;
        this.progress = 0.0;
        this.currentPoint = nmsPath.a((Entity)entity);
    }
    
    public boolean update() {
        final int current = MathHelper.floor(this.progress);
        final double d = this.progress - current;
        final double d2 = 1.0 - d;
        if (d + this.speed < 1.0) {
            final double dx = (this.currentPoint.a - this.entity.locX) * this.speed;
            final double dz = (this.currentPoint.c - this.entity.locZ) * this.speed;
            this.entity.move(dx, 0.0, dz);
            this.entity.checkMovement(dx, 0.0, dz);
            this.progress += this.speed;
        }
        else {
            final double bx = (this.currentPoint.a - this.entity.locX) * d2;
            final double bz = (this.currentPoint.c - this.entity.locZ) * d2;
            this.nmsPath.a();
            if (this.nmsPath.b()) {
                this.entity.move(bx, 0.0, bz);
                this.entity.checkMovement(bx, 0.0, bz);
                return false;
            }
            this.currentPoint = this.nmsPath.a((Entity)this.entity);
            final double d3 = this.speed - d2;
            final double dx2 = bx + (this.currentPoint.a - this.entity.locX) * d3;
            final double dy = this.currentPoint.b - this.entity.locY;
            final double dz2 = bz + (this.currentPoint.c - this.entity.locZ) * d3;
            this.entity.move(dx2, dy, dz2);
            this.entity.checkMovement(dx2, dy, dz2);
            this.progress += this.speed;
        }
        return true;
    }
    
    public static NPCPath find(final NPCEntity entity, final Location to, final double range, final double speed) {
        if (speed > 1.0) {
            throw new IllegalArgumentException("Speed cannot be higher than 1!");
        }
        try {
            final PathEntity path = entity.world.a((Entity)entity, to.getBlockX(), to.getBlockY(), to.getBlockZ(), (float)range, true, false, false, true);
            if (path != null) {
                return new NPCPath(entity, path, speed);
            }
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }
}
