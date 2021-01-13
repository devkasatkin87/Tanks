package com.devkasatkin87.game.tanks.unit;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.devkasatkin87.game.tanks.TanksMainClass;
import com.devkasatkin87.game.tanks.Weapon;
import com.devkasatkin87.game.tanks.utilits.Direction;

public class BotTank extends Tank{

    private Direction preferredDirection;
    private float aiTimer;
    private float aiTimerTo;
    private boolean isActive;

    public BotTank(TanksMainClass game, TextureAtlas atlas) {
        super(game);
        this.weapon = new Weapon();
        this.weapon.setTexture(atlas.findRegion("Bot_tank_gun"));
        this.texture = atlas.findRegion("Bot_tank_base");
        this.textureHp = atlas.findRegion("Bar");
        this.position = new Vector2(100.0f, 100.0f);
        this.speed = 100.0f;
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.hpMax = 3;
        this.hp = this.hpMax;
        this.aiTimer = 3.0f;
        this.preferredDirection = Direction.UP;
        this.isActive = false;
    }

    public void activate(float x, float y) {
        this.isActive = true;
        this.hpMax = 3;
        this.hp = this.hpMax;
        position.set(x, y);
        this.preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
        this.angle = preferredDirection.getAngle();
        this.aiTimer = 0.0f;

    }

    public boolean isActive() {
        return isActive;
    }

    public void update(float dt) {
        this.aiTimer += dt;
        if (this.aiTimer >= this.aiTimerTo) {
            this.aiTimer = 0.0f;
            this.aiTimerTo = MathUtils.random(2.5f, 4.0f);
            this.preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
            this.angle = preferredDirection.getAngle();
        }
        position.add(speed * preferredDirection.getVx() * dt, speed * preferredDirection.getVy() * dt);
        super.update(dt);
    }
}
