package com.devkasatkin87.game.tanks;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TanksMainClass extends ApplicationAdapter {
	private SpriteBatch batch;
	private Tank tank;
	private BulletsEmitter bulletsEmitter;
	private Map map;

	@Override
	public void create () {
		batch = new SpriteBatch();
		tank = new Tank(this);
		bulletsEmitter = new BulletsEmitter();
		map = new Map();

	}

	public BulletsEmitter getBulletsEmitter() {
		return bulletsEmitter;
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		update(dt);
		Gdx.gl.glClearColor(0, 0.6f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		map.render(batch);
		tank.render(batch);
		bulletsEmitter.render(batch);
		batch.end();
	}

	public void update(float dt) {
		tank.update(dt);
		bulletsEmitter.update(dt);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
