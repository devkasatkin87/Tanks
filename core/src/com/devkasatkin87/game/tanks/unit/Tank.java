package com.devkasatkin87.game.tanks.unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.devkasatkin87.game.tanks.GameScreen;
import com.devkasatkin87.game.tanks.TanksMainClass;
import com.devkasatkin87.game.tanks.utilits.Direction;
import com.devkasatkin87.game.tanks.utilits.TankOwner;
import com.devkasatkin87.game.tanks.utilits.Utils;
import com.devkasatkin87.game.tanks.Weapon;

public abstract class Tank {
    GameScreen game;
    TextureRegion texture;
    TextureRegion textureHp;
    Weapon weapon;
    Vector2 position;
    Vector2 tmp;
    float speed;
    float angle;
    float turretAngle;
    float fireTimer;
    Circle circle;
    TankOwner ownerType;

    int width;
    int height;

    int hp;
    int hpMax;

    public Tank(GameScreen game) {
        this.game = game;
        this.tmp = new Vector2(0.0f,0.0f);
    }

    public Circle getCircle() {
        return circle;
    }

    public TankOwner getOwnerType() {
        return ownerType;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width/2, position.y - height/2, width/2, height/2, width, height, 1, 1, angle);
        batch.draw(weapon.getTexture(), position.x - width/2, position.y - height/2, width/2, height/2, width, height, 1, 1, turretAngle);

        if (hp < hpMax) {
            batch.setColor(0,0,0,0.8f);
            batch.draw(textureHp, position.x - width / 2, position.y + height / 2 - 6, 44, 12);
            batch.setColor(0,1,0,0.8f);
            batch.draw(textureHp, position.x - width / 2, position.y + height / 2 - 4, ((float) hp / hpMax) * 40, 8);
            batch.setColor(1,1,1,1);

        }
    }

    public void update(float dt) {
        this.fireTimer += dt;
        if (position.x < 0.0f) {
            position.x = 0.0f;
        }
        if (position.x > Gdx.graphics.getWidth()) {
            position.x = Gdx.graphics.getWidth();
        }
        if (position.y < 0.0f) {
            position.y = 0.0f;
        }
        if (position.y > Gdx.graphics.getHeight()) {
            position.y = Gdx.graphics.getHeight();
        }
        circle.setPosition(position);
    }

    public void rotateTurretToPoint(float pointX, float pointY, float dt) {
        float angleTo = Utils.getAngel(position.x, position.y, pointX, pointY);
        turretAngle = Utils.makeRotation(turretAngle, angleTo, 180.0f, dt);
        turretAngle = Utils.angleToFromNegPiToPosPi(turretAngle);
    }

    public void fire() {
        if (fireTimer >= weapon.getFirePeriod()) {
            fireTimer = 0.0f;
            float angelRad = (float) Math.toRadians(turretAngle);
            game.getBulletsEmitter().activate(this, position.x, position.y, weapon.getProjectileSpeed() * (float)Math.cos(angelRad), weapon.getProjectileSpeed() * (float) Math.sin(angelRad), weapon.getDamage(), weapon.getProjectileLifeTime());
        }
    }

    public void move(Direction direction, float dt) {
        tmp.set(position);
        tmp.add(speed * direction.getVx() * dt, speed * direction.getVy() * dt);
        if (game.getMap().isAreaClear(tmp.x, tmp.y, width / 2)) {
            angle = direction.getAngle();
            position.set(tmp);
        }
    }

    public void takeDamage(int damage) {
        hp -= 1;
        if (hp <= 0) {
            destroy();
        }
    }

    public abstract void destroy();
}
