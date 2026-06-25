package model;
//本类专门用来实现关卡模式的单个关卡内容

public class Level {
    private int levelId;
    private int rows;
    private int cols;
    private int[][] boardLayout;
//其包含了，某一关卡的布局，棋子，与  id
    public Level(int levelId, int rows, int cols, int[][] boardLayout) {
        this.levelId = levelId;
        this.rows = rows;
        this.cols = cols;
        this.boardLayout = boardLayout;
    }
    //这里完全没用上，本来要生成墙体的，太难了
    public Cell[][] toCells() {
        Cell[][] cells = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int value = boardLayout[i][j];
                Position pos = new Position(i, j);
                if (value == -2) {  // 墙
                    cells[i][j] = new Cell(pos, false, -1);
                } else {
                    cells[i][j] = new Cell(pos, false, value);
                }
            }
        }
        return cells;
    }



}
