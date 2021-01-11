package com.devkasatkin87.game.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Tank {
    private TanksMainClass game;
    private Texture texture;
    private Texture turretTexture;
    private Vector2 position;
    private float speed;
    private float angle;
    private float turretAngle;

    public Tank(TanksMainClass game) {
        this.game = game;
        this.texture = new Texture("Player_tank_base.png");
        this.turretTexture = new Texture("Player_tank_gun.png");
        this.position = new Vector2(100.0f, 100.0f);
        this.speed = 100.0f;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 20, position.y - 20, 20, 20, 40, 40, 1, 1, angle, 0, 0, 40, 40, false, false);
        batch.draw(turretTexture, position.x - 20, position.y - 20, 20, 20, 40, 40, 1, 1, turretAngle, 0, 0, 40, 40, false, false);
    }

    public void update(float dt) {
        checkMovement(dt);
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        float angleTo = Utils.getAngel(position.x, position.y, mx, my);
        turretAngle = Utils.makeRotation(turretAngle, angleTo, 180.0f, dt);
        turretAngle = Utils.angleToFromNegPiToPosPi(turretAngle);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            fire();
        }
    }

    private void checkMovement(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            position.x += speed * dt;
            angle = 0.0f;
            return;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            position.x -= speed * dt;
            angle = 180.0f;
            return;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            position.y += speed * dt;
            angle = 90.0f;
            return;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            position.y -= speed * dt;
            angle = 270.0f;
            return;
        }
    }

    public void fire() {
        if (!game.getBullet().isActive()) {
            float angelRad = (float) Math.toRadians(turretAngle);
            game.getBullet().activate(position.x, position.y, 320.0f * (float)Math.cos(angelRad), 320.0f * (float) Math.sin(angelRad));
        }
    }
}
