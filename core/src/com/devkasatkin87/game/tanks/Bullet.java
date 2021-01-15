package com.devkasatkin87.game.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.devkasatkin87.game.tanks.unit.Tank;

public class Bullet {
    private Tank owner;
    private Vector2 position;
    private Vector2 velocity;
    private int damage;
    private boolean active;

    public Bullet() {
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.active = false;
    }

    public int getDamage() {
        return damage;
    }

    public Tank getOwner() {
        return owner;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isActive() {
        return active;
    }

    public void activate(Tank owner, float x, float y, float vx, float vy, int damage) {
        this.owner = owner;
        this.active = true;
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.damage = damage;
    }

    public void deactivate() {
        this.active = false;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x < 0.0f || position.x > Gdx.graphics.getWidth() || position.y < 0.0f || position.y > Gdx.graphics.getHeight()) {
            deactivate();
        }
    }

}
