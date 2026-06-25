package model;
//本类是棋盘相关，包含了棋盘上的参数
public class GameBoard {
    int rowCnt;
    int colCnt;
    Cell[][] board;
//所有的棋子都存放在本类的 Cell数组里面，可以从中存取
    public GameBoard(int rowCnt, int colCnt, Cell[][] border) {
        this.rowCnt = rowCnt;
        this.colCnt = colCnt;
        this.board = border;
    }

    public int getRowCnt() {
        return rowCnt;
    }

    public int getColCnt() {
        return colCnt;
    }
    public Cell getCell(int row, int col) {
        return board[row][col];
    }

    public void clearAllChosen() {
        for (int i = 0; i < rowCnt; i++) {
            for (int j = 0; j < colCnt; j++) {
                board[i][j].setChosen(false);
            }
        }
    }
}
