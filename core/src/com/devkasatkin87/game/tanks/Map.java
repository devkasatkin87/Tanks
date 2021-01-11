package com.devkasatkin87.game.tanks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map {
    public static final int SIZE_X = 32;
    public static final int SIZE_Y = 18;
    public static final int CELL_SIZE = 40;

    private Texture textureGrass;

    public Map() {
        this.textureGrass = new Texture("Grass.png");
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                batch.draw(textureGrass, i * CELL_SIZE, j * CELL_SIZE);
            }
        }
    }
}
