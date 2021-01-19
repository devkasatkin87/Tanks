package com.devkasatkin87.game.tanks;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.devkasatkin87.game.tanks.unit.BotTank;
import com.devkasatkin87.game.tanks.unit.PlayerTank;
import com.devkasatkin87.game.tanks.unit.Tank;

public class TanksMainClass extends ApplicationAdapter {
	private SpriteBatch batch;
	private BitmapFont font24;
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
		font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
		batch = new SpriteBatch();
		playerTank = new PlayerTank(this, atlas);
		bulletsEmitter = new BulletsEmitter(atlas);
		map = new Map(atlas);
		botEmitter = new BotEmitter(this, atlas);
		botEmitter.activate(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
		gameTimer = 6.0f;

	}

	public Map getMap() {
		return map;
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
		playerTank.humanHUD(batch, font24);
		batch.end();
	}

	public void update(float dt) {
		gameTimer += dt;
		if (gameTimer > 10.0f) {
			gameTimer = 0.0f;

			float coordX, coordY;

			do {
				coordX = MathUtils.random(0, Gdx.graphics.getWidth());
				coordY = MathUtils.random(0, Gdx.graphics.getHeight());

			} while (!map.isAreaClear(coordX, coordY, 20));


			botEmitter.activate(coordX, coordY);
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

				map.checkWallAndBulletsCollision(bullet);
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
