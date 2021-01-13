package com.devkasatkin87.game.tanks.unit;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.devkasatkin87.game.tanks.TanksMainClass;
import com.devkasatkin87.game.tanks.utilits.Utils;
import com.devkasatkin87.game.tanks.Weapon;

public abstract class Tank {
    TanksMainClass game;
    Texture texture;
    Weapon weapon;
    Vector2 position;
    float speed;
    float angle;
    float turretAngle;
    float fireTimer;

    int width;
    int height;

    int hp;
    int hpMax;

    public Tank(TanksMainClass game) {
        this.game = game;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width/2, position.y - height/2, width/2, height/2, width, height, 1, 1, angle, 0, 0, width, height, false, false);
        batch.draw(weapon.getTexture(), position.x - width/2, position.y - height/2, width/2, height/2, width, height, 1, 1, turretAngle, 0, 0, width, height, false, false);
    }

    public abstract void update(float dt);

    public void rotateTurretToPoint(float pointX, float pointY, float dt) {
        float angleTo = Utils.getAngel(position.x, position.y, pointX, pointY);
        turretAngle = Utils.makeRotation(turretAngle, angleTo, 180.0f, dt);
        turretAngle = Utils.angleToFromNegPiToPosPi(turretAngle);
    }

    public void fire(float dt) {
        if (fireTimer >= weapon.getFirePeriod()) {
            fireTimer = 0.0f;
            float angelRad = (float) Math.toRadians(turretAngle);
            game.getBulletsEmitter().activate(position.x, position.y, 320.0f * (float)Math.cos(angelRad), 320.0f * (float) Math.sin(angelRad), weapon.getDamage());
        }
    }
}
