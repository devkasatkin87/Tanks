package com.devkasatkin87.game.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;

    public Bullet() {
        this.texture = new Texture("Shell.png");
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x-8, position.y-8);
    }

    public void activate(float x, float y, float vx, float vy) {
        this.active = true;
        this.position.set(x, y);
        this.velocity.set(vx, vy);
    }

    private void deactivate() {
        this.active = false;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x < 0.0f || position.x > Gdx.graphics.getWidth() || position.y < 0.0f || position.y > Gdx.graphics.getHeight()) {
            deactivate();
        }
    }

}
