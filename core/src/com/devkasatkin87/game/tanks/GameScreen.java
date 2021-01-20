package com.devkasatkin87.game.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.devkasatkin87.game.tanks.unit.BotTank;
import com.devkasatkin87.game.tanks.unit.PlayerTank;
import com.devkasatkin87.game.tanks.unit.Tank;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font24;
    private PlayerTank playerTank;
    private BulletsEmitter bulletsEmitter;
    private Map map;
    private BotEmitter botEmitter;
    private float gameTimer;
    private TextureAtlas atlas;
    private Stage stage;
    private boolean paused;

    private static final boolean FRIENDLY_FIRE = false;


    public Map getMap() {
        return map;
    }

    public BulletsEmitter getBulletsEmitter() {
        return bulletsEmitter;
    }

    public PlayerTank getPlayerTank() {
        return playerTank;
    }

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas("game.pack");
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        playerTank = new PlayerTank(this, atlas);
        bulletsEmitter = new BulletsEmitter(atlas);
        map = new Map(atlas);
        botEmitter = new BotEmitter(this, atlas);
        botEmitter.activate(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
        gameTimer = 6.0f;

        stage = new Stage();
        Skin skin = new Skin();
        skin.add("simpleButton", new TextureRegion(atlas.findRegion("SimpleButton")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;

        Group group = new Group();
        final TextButton pauseButton = new TextButton("Pause", textButtonStyle);
        final TextButton exitButton = new TextButton("Exit", textButtonStyle);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = !paused;
            }
        });
        pauseButton.setPosition(0, 40);
        exitButton.setPosition(0, 0);
        group.addActor(pauseButton);
        group.addActor(exitButton);
        group.setPosition(1140, 640);
        stage.addActor(group);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0.6f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        map.render(batch);
        playerTank.render(batch);
        botEmitter.render(batch);
        bulletsEmitter.render(batch);
        playerTank.humanHUD(batch, font24);
        stage.draw();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        atlas.dispose();
    }

    public void update(float dt) {
        if (!paused) {
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
        stage.act(dt);
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
}
