package com.devkasatkin87.game.tanks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon {
    private TextureRegion texture;
    private float fireTimer;
    private float firePeriod;
    private int damage;
    private float radius;
    private float projectileSpeed;
    private float projectileLifeTime;

    public Weapon() {
        this.firePeriod = 0.4f;
        this.damage = 1;
        this.radius = 300.0f;
        this.projectileSpeed = 320.0f;
        this.projectileLifeTime = radius / projectileSpeed;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getDamage() {
        return damage;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public float getProjectileLifeTime() {
        return projectileLifeTime;
    }

    public float getProjectileSpeed() {
        return projectileSpeed;
    }
    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }
}
