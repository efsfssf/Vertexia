package io.github.vertexia.src.gui;

public class SelectionState {

    private int x = -1;
    private int y = -1;
    private boolean clickedTwice;

    public void set(int nx, int ny) {
        clickedTwice = (x == nx && y == ny);
        x = nx;
        y = ny;
    }

    public void reset() {
        x = -1;
        y = -1;
        clickedTwice = false;
    }

    public boolean isValid(int gridSize) {
        return x >= 0 && y >= 0 && x < gridSize && y < gridSize;
    }

    public boolean highlightInGridBounds(int gridSize) {
        return isValid(gridSize);
    }

    public boolean clickedTwice() {
        return clickedTwice;
    }

    public int x() { return x; }
    public int y() { return y; }
}
