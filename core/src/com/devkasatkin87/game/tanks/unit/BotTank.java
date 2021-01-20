package com.devkasatkin87.game.tanks.unit;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.devkasatkin87.game.tanks.GameScreen;
import com.devkasatkin87.game.tanks.TanksMainClass;
import com.devkasatkin87.game.tanks.Weapon;
import com.devkasatkin87.game.tanks.utilits.Direction;
import com.devkasatkin87.game.tanks.utilits.TankOwner;

public class BotTank extends Tank{

    private Direction preferredDirection;
    private float aiTimer;
    private float aiTimerTo;
    private boolean isActive;
    private float pursuitRadius;
    private Vector3 lastPosition;

    public BotTank(GameScreen game, TextureAtlas atlas) {
        super(game);
        this.ownerType = TankOwner.AI;
        this.weapon = new Weapon();
        this.weapon.setTexture(atlas.findRegion("Bot_tank_gun"));
        this.texture = atlas.findRegion("Bot_tank_base");
        this.textureHp = atlas.findRegion("Bar");
        this.position = new Vector2(100.0f, 100.0f);
        this.lastPosition = new Vector3(0.0f, 0.0f, 0.0f);
        this.speed = 100.0f;
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.hpMax = 3;
        this.hp = this.hpMax;
        this.aiTimer = 3.0f;
        this.preferredDirection = Direction.UP;
        this.isActive = false;
        this.pursuitRadius = 300.f;
        this.circle = new Circle(position.x, position.y, (width + height) /2);
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
            this.aiTimerTo = MathUtils.random(3.5f, 6.0f);
            this.preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
            this.angle = preferredDirection.getAngle();
        }
        move(preferredDirection, dt);
        float dst = this.position.dst(game.getPlayerTank().getPosition());
        if (dst < pursuitRadius) {
            rotateTurretToPoint(game.getPlayerTank().getPosition().x, game.getPlayerTank().getPosition().y, dt);
            fire();
        }
        if (Math.abs(position.x - lastPosition.x) < 0.5f && Math.abs(position.y - lastPosition.y) < 0.5f) {
            lastPosition.z += dt;
            if (lastPosition.z > 0.3f) {
                aiTimer += 10.0f;
            }
        } else {
            lastPosition.x = position.x;
            lastPosition.y = position.y;
            lastPosition.z = 0.0f;

        }
        super.update(dt);
    }

    @Override
    public void destroy() {
        this.isActive = false;

    }
}
