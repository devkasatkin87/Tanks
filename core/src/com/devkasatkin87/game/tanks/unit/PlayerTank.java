package com.devkasatkin87.game.tanks.unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.devkasatkin87.game.tanks.TanksMainClass;
import com.devkasatkin87.game.tanks.Weapon;
import com.devkasatkin87.game.tanks.utilits.TankOwner;

public class PlayerTank extends Tank{
    private int lifes;

    public PlayerTank(TanksMainClass game, TextureAtlas atlas) {
        super(game);
        this.ownerType = TankOwner.PLAYER;
        this.weapon = new Weapon();
        this.weapon.setTexture(atlas.findRegion("Player_tank_gun"));
        this.texture = atlas.findRegion("Player_tank_base");
        this.textureHp = atlas.findRegion("Bar");
        this.position = new Vector2(100.0f, 100.0f);
        this.speed = 100.0f;
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.hpMax = 10;
        this.hp = this.hpMax;
        this.circle = new Circle(position.x, position.y, (width + height) /2);
        this.lifes = 5;
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

    public void update(float dt) {
        checkMovement(dt);
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();

        rotateTurretToPoint(mx, my, dt);

        if (Gdx.input.isTouched()) {
            fire(dt);
        }
        super.update(dt);
    }

    @Override
    public void destroy() {
        lifes--;
        hp = hpMax;
    }
}
