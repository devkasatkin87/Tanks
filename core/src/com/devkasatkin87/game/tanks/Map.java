package com.devkasatkin87.game.tanks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Map {
    public static final int SIZE_X = 64;
    public static final int SIZE_Y = 36;
    public static final int CELL_SIZE = 20;

    private TextureRegion textureGrass;
    private TextureRegion textureBlock;
    private int[][] obstacles;

    public Map(TextureAtlas atlas) {
        this.textureGrass = atlas.findRegion("Grass");
        this.textureBlock = atlas.findRegion("Block");
        this.obstacles = new int[SIZE_X][SIZE_Y];
        createMap();
    }

    private void createMap() {
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                int cx = (int)(i / 4);
                int cy = (int)(j / 4);
                if (cx % 2 == 0 && cy % 2 == 0) {
                    obstacles[i][j] = 5;
                }
            }
        }
    }

    public void checkWallAndBulletsCollision(Bullet bullet) {
        int cx = (int) (bullet.getPosition().x / CELL_SIZE);
        int cy = (int) (bullet.getPosition().y / CELL_SIZE);

        if (cx >= 0 && cy >= 0 && cx < SIZE_X && cy < SIZE_Y ) {
            if (obstacles[cx][cy] > 0) {
                obstacles[cx][cy] -= bullet.getDamage();
                bullet.deactivate();
            }
        }
    }

    public boolean isAreaClear(float x, float y, float halfSize) {
        int leftX = (int) ((x - halfSize) / CELL_SIZE);
        int rightX = (int) ((x + halfSize) / CELL_SIZE);

        int bottomY = (int) ((y - halfSize) / CELL_SIZE);
        int topY = (int) ((y + halfSize) / CELL_SIZE);

        if (leftX < 0) {
            leftX = 0;
        }

        if (rightX >= SIZE_X) {
            rightX = SIZE_X - 1;
        }

        if (bottomY < 0) {
            bottomY = 0;
        }

        if (topY >= SIZE_Y) {
            topY = SIZE_Y - 1;
        }

        for (int i = leftX; i <= rightX; i++) {
            for (int j = bottomY; j <= topY ; j++) {
                if (obstacles[i][j] > 0) {
                    return false;
                }
            }
        }

          return true;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < 1280 / 40; i++) {
            for (int j = 0; j < 720 / 40; j++) {
                batch.draw(textureGrass, i * 40, j * 40);
            }
        }

        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                if (obstacles[i][j] > 0) {
                    batch.draw(textureBlock, i * CELL_SIZE, j * CELL_SIZE);
                }
            }
        }
    }


}
