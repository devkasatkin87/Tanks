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
    private Weapon weapon;
    private Vector2 position;
    private float speed;
    private float angle;
    private float turretAngle;
    private float fireTimer;

    private int width;
    private int height;

    public Tank(TanksMainClass game) {
        this.game = game;
        this.texture = new Texture("Player_tank_base.png");
        this.turretTexture = new Texture("Player_tank_gun.png");
        this.weapon = new Weapon();
        this.position = new Vector2(100.0f, 100.0f);
        this.speed = 100.0f;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width/2, position.y - height/2, width/2, height/2, width, height, 1, 1, angle, 0, 0, width, height, false, false);
        batch.draw(turretTexture, position.x - width/2, position.y - height/2, width/2, height/2, width, height, 1, 1, turretAngle, 0, 0, width, height, false, false);
    }

    public void update(float dt) {
        checkMovement(dt);
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        float angleTo = Utils.getAngel(position.x, position.y, mx, my);
        turretAngle = Utils.makeRotation(turretAngle, angleTo, 180.0f, dt);
        turretAngle = Utils.angleToFromNegPiToPosPi(turretAngle);

        if (Gdx.input.isTouched()) {
            fire(dt);
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

    public void fire(float dt) {
        fireTimer += dt;
        if (fireTimer >= weapon.getFirePeriod()) {
            fireTimer = 0.0f;
            float angelRad = (float) Math.toRadians(turretAngle);
            game.getBulletsEmitter().activate(position.x, position.y, 320.0f * (float)Math.cos(angelRad), 320.0f * (float) Math.sin(angelRad));
        }
    }
}
