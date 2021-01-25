package com.devkasatkin87.game.tanks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Map {
    private enum WallType {
        HARD(0, 5, true, false, false),
        SOFT(1, 3, true, false, false),
        INDESTRUCTABLE(2,1,false, false, false),
        WATER(3, 1, false, false, true),
        NONE(0,0, false, true, true);

        int index;
        int maxHp;
        boolean isUnitPossible;
        boolean isProjectilePossible;
        boolean desctructable;

        WallType(int index, int maxHp, boolean desctructable, boolean isUnitPossible, boolean isProjectilePossible) {
            this.index = index;
            this.maxHp = maxHp;
            this.desctructable = desctructable;
            this.isUnitPossible = isUnitPossible;
            this.isProjectilePossible = isProjectilePossible;
        }
    }

    private class Cell {
        WallType type;
        int hp;

        public Cell(WallType type) {
            this.type = type;
            this.hp = type.maxHp;
        }

        public void damage() {
            if (type.desctructable) {
                hp--;
                if (hp <= 0) {
                    type = WallType.NONE;
                }
            }
        }

        public void changeType(WallType type) {
            this.type = type;
            hp = type.maxHp;
        }
    }

    public static final int SIZE_X = 64;
    public static final int SIZE_Y = 36;
    public static final int CELL_SIZE = 20;

    private TextureRegion textureGrass;
    private TextureRegion textureBlock;
    private TextureRegion[][] textureWalls;
    private Cell[][] cells;

    public Map(TextureAtlas atlas) {
        this.textureWalls = new TextureRegion(atlas.findRegion("Walls")).split(CELL_SIZE, CELL_SIZE);
        this.textureGrass = atlas.findRegion("Grass");
        this.cells = new Cell[SIZE_X][SIZE_Y];
        createMap();
    }

    private void createMap() {
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                cells[i][j] = new Cell(WallType.NONE);
                int cx = (int)(i / 4);
                int cy = (int)(j / 4);
                if (cx % 2 == 0 && cy % 2 == 0) {
                    if (MathUtils.random() < 0.8f) {
                        //cells[i][j].changeType(WallType.HARD);
                        cells[i][j].changeType(WallType.WATER);
                    } else {
                        cells[i][j].changeType(WallType.SOFT);
                    }
                }
            }
        }
        for (int i = 0; i < SIZE_X; i++) {
            cells[i][0].changeType(WallType.INDESTRUCTABLE);
            cells[i][SIZE_Y - 1].changeType(WallType.INDESTRUCTABLE);

        }
        for (int i = 0; i < SIZE_Y; i++) {
            cells[0][i].changeType(WallType.INDESTRUCTABLE);
            cells[SIZE_X - 1][i].changeType(WallType.INDESTRUCTABLE);
        }
    }

    public void checkWallAndBulletsCollision(Bullet bullet) {
        int cx = (int) (bullet.getPosition().x / CELL_SIZE);
        int cy = (int) (bullet.getPosition().y / CELL_SIZE);

        if (cx >= 0 && cy >= 0 && cx < SIZE_X && cy < SIZE_Y ) {
            if (!cells[cx][cy].type.isProjectilePossible) {
                cells[cx][cy].damage();
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
                if (!cells[i][j].type.isUnitPossible) {
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
                if (cells[i][j].type != WallType.NONE) {
                    batch.draw(textureWalls[cells[i][j].type.index][cells[i][j].hp - 1], i * CELL_SIZE, j * CELL_SIZE);
                }
            }
        }
    }


}
