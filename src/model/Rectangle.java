package model;

import java.awt.*;
//本类用于实现一个矩形的东西，毕竟棋盘是矩形的
public class  Rectangle {
    int x;
    int y;
    int width;
    int height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    //下面的方法都用于确定某一矩形的一些参数
    public Point getCenterPosition() {
        return new Point(x + width / 2, y + height / 2);
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
