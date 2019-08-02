package view;

import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Model {
    private final int FIELD_WIDTH;
    private Tile[][] gameTiles;
    int score;
    int maxTile;
    private Stack<Tile[][]> previousStates = new Stack();
    private Stack<Integer> previousScores = new Stack();
    private boolean isSaveNeeded = true;
    private SimpleMatrix matrix;

    public Model(final int FIEELD_WIDTH, SimpleMatrix matrix){
        this.FIELD_WIDTH = FIEELD_WIDTH;
        this.matrix = matrix;
        resetGameTiles();
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public Model(SimpleMatrix matrix){
        this.FIELD_WIDTH = matrix.numRows();
        this.matrix = matrix;
        resetGameTiles();
    }

    public void resetGameTiles() {
        this.gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
    }

    private void addTile() {
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                Tile tile = new Tile();
                tile.value = (int) matrix.get(i, j);
                gameTiles[i][j] = tile;
            }
        }
    }




}
