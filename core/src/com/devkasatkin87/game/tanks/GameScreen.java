package com.devkasatkin87.game.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.devkasatkin87.game.tanks.unit.BotTank;
import com.devkasatkin87.game.tanks.unit.PlayerTank;
import com.devkasatkin87.game.tanks.unit.Tank;

public class GameScreen extends AbstractScreen {
    private SpriteBatch batch;
    private BitmapFont font24;
    private PlayerTank playerTank;
    private BulletsEmitter bulletsEmitter;
    private ItemEmitter itemEmitter;
    private Map map;
    private BotEmitter botEmitter;
    private float gameTimer;
    private float worldTimer;
    private TextureAtlas atlas;
    private Stage stage;
    private boolean paused;
    private Vector2 mousePosition;
    private TextureRegion cursor;

    private Sound sound;
    private Music music;

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

    public Vector2 getMousePosition() {
        return mousePosition;
    }

    public ItemEmitter getItemEmitter() {
        return itemEmitter;
    }

    @Override
    public void show() {

        //Пример инициализации и запуска музыки и звуков (из папки asssets)
//        sound = Gdx.audio.newSound(Gdx.files.internal("boom.wav"));
//        sound.play();
//
//        music = Gdx.audio.newMusic(Gdx.files.internal("song.mp3"));
//        music.isLooping();
//        music.play();

        atlas = new TextureAtlas("game.pack");
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        cursor = new TextureRegion(atlas.findRegion("cursor"));
        playerTank = new PlayerTank(this, atlas);
        bulletsEmitter = new BulletsEmitter(atlas);
        itemEmitter = new ItemEmitter(atlas);
        map = new Map(atlas);
        botEmitter = new BotEmitter(this, atlas);
        botEmitter.activate(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
        gameTimer = 6.0f;

        stage = new Stage();
        mousePosition = new Vector2();
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
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.MENU);
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
        Gdx.input.setCursorCatched(true);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //The camera follows the player
//        ScreenManager.getInstance().getCamera().position.set(playerTank.getPosition().x, playerTank.getPosition().y, 0);
//        ScreenManager.getInstance().getCamera().update();

        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);
        batch.begin();
        map.render(batch);
        playerTank.render(batch);
        botEmitter.render(batch);
        bulletsEmitter.render(batch);
        itemEmitter.render(batch);
        playerTank.humanHUD(batch, font24);
        stage.draw();
        batch.draw(cursor, mousePosition.x - cursor.getRegionWidth() / 2, mousePosition.y - cursor.getRegionHeight() / 2, cursor.getRegionWidth() / 2, cursor.getRegionHeight() / 2, cursor.getRegionWidth(), cursor.getRegionHeight(), 1, 1, -worldTimer * 25);
        batch.end();
    }

    @Override
    public void dispose() {
        font24.dispose();
        atlas.dispose();
    }

    public void update(float dt) {
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
        ScreenManager.getInstance().getViewport().unproject(mousePosition);
        if (!paused) {
            gameTimer += dt;
            worldTimer += dt;
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
            itemEmitter.update(dt);
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

        for (int i = 0; i < itemEmitter.getItems().length; i++) {
            if (itemEmitter.getItems()[i].isActive()) {
                Item item = itemEmitter.getItems()[i];
                if (playerTank.getCircle().contains(item.getPosition())) {
                    playerTank.consumePowerUp(item);
                    item.deactivate();
                    break;
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
}
