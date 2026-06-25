package model;

public class Cell {
    private Position position;
    private boolean isEmpty;
    private int iconIndex;
    private boolean isChosen;
//cell类包含几个重要参数，分别是位置，是否为空，图片  编号，以及是否被选中


    public Cell(Position position, boolean isEmpty, int iconIndex) {
        this.position = position;
        this.isEmpty = isEmpty;
        this.iconIndex = iconIndex;
        this.isChosen = false;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setEmpty(boolean empty){
        isEmpty = empty;
    }


    public boolean isEmpty() {
        return isEmpty;
    }

    public int getIconIndex() {
        return iconIndex;
    }

    public Position getPos() {
        return position;
    }

    public boolean getIsChosen() {
        return isChosen;
    }

    public void setChosen(boolean isChosen) {
        this.isChosen = isChosen;
    }
    public void setIconIndex(int iconIndex) {
        this.iconIndex = iconIndex;
    }
}
