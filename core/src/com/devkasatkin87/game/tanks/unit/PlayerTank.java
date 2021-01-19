package com.devkasatkin87.game.tanks.unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.devkasatkin87.game.tanks.TanksMainClass;
import com.devkasatkin87.game.tanks.Weapon;
import com.devkasatkin87.game.tanks.utilits.Direction;
import com.devkasatkin87.game.tanks.utilits.TankOwner;

public class PlayerTank extends Tank{
    private int lifes;
    private int score;

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

    public void addScore (int amount) {
        score += amount;
    }

    private void checkMovement(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            move(Direction.RIGHT, dt);
            return;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            move(Direction.LEFT, dt);
            return;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            move(Direction.UP, dt);
            return;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            move(Direction.DOWN, dt);
        }
    }

    public void update(float dt) {
        checkMovement(dt);
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();

        rotateTurretToPoint(mx, my, dt);

        if (Gdx.input.isTouched()) {
            fire();
        }
        super.update(dt);
    }

    @Override
    public void destroy() {
        lifes--;
        hp = hpMax;
    }

    public void humanHUD(SpriteBatch batch, BitmapFont font24) {
        font24.draw(batch, "Score: " + score, 20,700);
    }
}
