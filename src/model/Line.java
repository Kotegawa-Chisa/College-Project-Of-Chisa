package model;
//本类负责连线的逻辑，实际上就是简单的存储两个旗子，没了！
public class Line {
    Cell cell1;
    Cell cell2;

    public Line(Cell cell1, Cell cell2) {
        this.cell1 = cell1;
        this.cell2 = cell2;
    }

    public Cell getCell1() {
        return cell1;
    }

    public Cell getCell2() {
        return cell2;
    }
}
