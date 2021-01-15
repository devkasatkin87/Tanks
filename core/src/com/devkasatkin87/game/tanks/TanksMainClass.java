package com.devkasatkin87.game.tanks;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.devkasatkin87.game.tanks.unit.BotTank;
import com.devkasatkin87.game.tanks.unit.PlayerTank;
import com.devkasatkin87.game.tanks.unit.Tank;

public class TanksMainClass extends ApplicationAdapter {
	private SpriteBatch batch;
	private PlayerTank playerTank;
	private BulletsEmitter bulletsEmitter;
	private Map map;
	private BotEmitter botEmitter;
	private float gameTimer;
	private TextureAtlas atlas;

	private static final boolean FRIENDLY_FIRE = false;

	@Override
	public void create () {
		atlas = new TextureAtlas("game.pack");
		batch = new SpriteBatch();
		playerTank = new PlayerTank(this, atlas);
		bulletsEmitter = new BulletsEmitter(atlas);
		map = new Map(atlas);
		botEmitter = new BotEmitter(this, atlas);
		botEmitter.activate(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));

	}

	public BulletsEmitter getBulletsEmitter() {
		return bulletsEmitter;
	}

	public PlayerTank getPlayerTank() {
		return playerTank;
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		update(dt);
		Gdx.gl.glClearColor(0, 0.6f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		map.render(batch);
		playerTank.render(batch);
		botEmitter.render(batch);
		bulletsEmitter.render(batch);
		batch.end();
	}

	public void update(float dt) {
		gameTimer += dt;
		if (gameTimer > 10.0f) {
			gameTimer = 0.0f;
			botEmitter.activate(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
		}
		playerTank.update(dt);
		botEmitter.update(dt);
		bulletsEmitter.update(dt);
		checkCollisions();
	}

	public void checkCollisions() {
		for (int i = 0; i < bulletsEmitter.getBullets().length; i++) {
			Bullet bullet = bulletsEmitter.getBullets()[i];
			if (bullet.isActive()) {
				for (int j = 0; j < botEmitter.getBots().length; j++) {
					BotTank bot = botEmitter.getBots()[j];
					if (bot.isActive()) {
						if (checkBulletOwner(bot, bullet) && bot.getCircle().contains(bullet.getPosition())) {
							bullet.deactivate();
							bot.takeDamage(bullet.getDamage());
							break;
						}
					}
				}
				if (checkBulletOwner(playerTank, bullet) && playerTank.getCircle().contains(bullet.getPosition())) {
					bullet.deactivate();
					playerTank.takeDamage(bullet.getDamage());
				}
			}
		}
	}

	public boolean checkBulletOwner(Tank tank, Bullet bullet) {
		if (!FRIENDLY_FIRE) {
			return tank.getOwnerType() != bullet.getOwner().getOwnerType();
		} else {
			return tank != bullet.getOwner();
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		atlas.dispose();
	}
}
