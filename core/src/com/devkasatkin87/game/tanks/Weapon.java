package com.devkasatkin87.game.tanks;

import com.badlogic.gdx.graphics.Texture;

public class Weapon {
    private Texture texture;
    private float fireTimer;
    private float firePeriod;
    private int damage;

    public Weapon() {
        this.texture = new Texture("Player_tank_gun.png");
        this.firePeriod = 0.4f;
        this.damage = 1;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getDamage() {
        return damage;
    }

    public Texture getTexture() {
        return texture;
    }
}
