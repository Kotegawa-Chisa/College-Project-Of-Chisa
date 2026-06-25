package model;
//本类储存位置
public class Position {
    private int row;
    private int col;
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    @Override
    public boolean equals(Object obj) {
        //此处直接比较内存是否一致，这是第一层判断，最快最直接
        //所以这样比较很合理！
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        //此时，已经能够保证 obj类相同，且并不代表完全一样的东西
        Position other = (Position) obj;
        //强制数据转换
        //转换后，再判断是否行 /列一致
        return this.row == other.row && this.col == other.col;
    }
    public int hashCode() {
        return 31 * row + col;   // 标准实现
    }
    //此处重写了 hashCode，这是由于，后续的方法中调用了 HashMap进行快速检索与储存，
    //而此处如果不重写 哈希码，就会导致 hashSet出现问题

    public boolean isEmpty(GameBoard gameBoard) {
        return gameBoard.getCell(this.row, this.col).isEmpty();
    }
}
