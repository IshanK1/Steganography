public class Point {
    private int row;
    private int col;

    public Point(int newRow, int newCol) {
        row = newRow;
        col = newCol;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int newRow) {
        row = newRow;
    }

    public void setCol(int newCol) {
        col = newCol;
    }

    public String toString() {
        return "row: " + row + " col: " + col;
    }
}
