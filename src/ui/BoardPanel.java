package ui;
//本类继承Jpanel，用于绘制棋盘，处理鼠标点击
import model.*;
import model.Rectangle;
import utils.Utils;

import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.AlphaComposite;
import java.util.HashMap;
import java.util.Map;


public class BoardPanel extends JPanel {
    private Map<Position, Float> fadingCells;   // 渐隐格子的透明度
    //此处使用了 Map，表示键 / 值对。用于搞笑寻找 position对应的透明度

    private Timer fadeTimer;// 渐变动画定时器
    int offSetX;
    int offSetY;
    private Timer hintTimer;
    private List<Position> hintPositions = new ArrayList<>();

    // 显示提示高亮（临时选中两个格子，3秒后自动清除）
    public void showHint(Position p1, Position p2) {
        clearHint(); // 清除之前的提示
        hintPositions.add(p1);
        hintPositions.add(p2);
        gameBoard.getCell(p1.getRow(), p1.getCol()).setChosen(true);
        gameBoard.getCell(p2.getRow(), p2.getCol()).setChosen(true);
        repaint();
        if (hintTimer != null) hintTimer.stop();
        hintTimer = new Timer(3000, e -> clearHint());
        hintTimer.setRepeats(false);
        hintTimer.start();
    }

    // 清除提示高亮
    public void clearHint() {
        for (Position pos : hintPositions) {
            Cell cell = gameBoard.getCell(pos.getRow(), pos.getCol());
            if (cell != null) cell.setChosen(false);
        }
        hintPositions.clear();
        if (hintTimer != null) hintTimer.stop();
        repaint();
    }

    List<Image> imageList = new ArrayList<>();
    GameBoard gameBoard;
    List<Line> lineList = new ArrayList<>();
    //此处添加了棋盘，图像等的引用
    int totalRow;
    int totalCol;
    int width;
    int height;
    int cellWidth;
    int cellHeight;
    //以上为基本参数
    Position firstSelected = null;
    Position secondSelected = null;
    boolean animating = false;
    boolean lineVisible;
    //以上为部分选择逻辑，以及状态判断
    private GameCallBack gameCallBack;
    /**
     * 游戏回调接口：用于将棋盘的事件通知给上层的GameManager
     */
    public BoardPanel(GameBoard gameBoard, int offSetX, int offSetY,int width, int height) {
        this.offSetX = offSetX;
        this.offSetY = offSetY;
        this.setBounds(offSetX, offSetY, width, height);
        this.totalRow = gameBoard.getRowCnt();
        this.totalCol = gameBoard.getColCnt();
        this.width = width;
        this.height = height;
        this.gameBoard = gameBoard;
        this.setPreferredSize(new Dimension(this.width, this.height));
        this.cellWidth = this.width / totalCol;
        this.cellHeight = this.height / totalRow;
        loadImages();
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
        fadingCells = new HashMap<>();
        //此处调用了HashMap


    }
    public interface GameCallBack {
        void onPairEliminated(int iconIdex);
        void onGameOver(boolean isWin);
        boolean isGameRunning();
    }
    //此处定义了接口，负责将棋盘汇报给 GameFrame


    public void setGameCallBack(GameCallBack gameCallBack) {
        this.gameCallBack = gameCallBack;
    }

    public Position getPositionByPoint(int x, int y) {

        int col = x / cellWidth;
        int row = y / cellHeight;
        if (row < 0 || row >= totalRow || col < 0 || col >= totalCol) {
            return null;
        }
        return new Position(row, col);
    }
    //此处的getPosition一步到位，直接通过 x和y锁定了对应的 position，

    public void showLine(Cell c1, Cell c2) {
        lineList.clear();
        lineList.add(new Line(c1, c2));
        lineVisible = true;
        repaint();
        //本段为调试性代码
    }
    /**
     * 根据路径点列表绘制连线（路径点包括起点、拐点、终点）
     * @param pathPoints 路径上的格子位置（按顺序）
     */

    //此处实现了路径显示，通过传入的分割点获得对应路径
    public void showLinePath(List<Position> pathPoints) {
        lineList.clear();
        if(pathPoints == null || pathPoints.size() < 2){
            return;
        }//确保传入无误
        for(int i = 0; i < pathPoints.size()-1; i++){
            Cell begin = gameBoard.getCell(pathPoints.get(i).getRow(), pathPoints.get(i).getCol());
            Cell end  = gameBoard.getCell(pathPoints.get(i+1).getRow(), pathPoints.get(i+1).getCol());
            lineList.add(new Line(begin, end));
        }
        lineVisible = true;
        repaint();
    }

    public void clearLine() {
        lineVisible = false;
        lineList.clear();
        repaint();
    }


    public void loadImages() {
        File dir = new File("resource");
        // 打印绝对路径，确认程序找到了正确的文件夹
        System.out.println("resource文件夹路径：" + dir.getAbsolutePath());

        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            System.err.println("错误：resource文件夹不存在或为空！");
            return;
        }
        //方便调试

        // 筛选出所有 png文件
        List<File> pngFiles = new ArrayList<>();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".png")) {
                pngFiles.add(file);
            }
        }

        // 按文件名数字大小排序（0.png,1.png,...,12.png）
        pngFiles.sort((f1, f2) -> {
            try {
                int n1 = Integer.parseInt(f1.getName().replace(".png", ""));
                int n2 = Integer.parseInt(f2.getName().replace(".png", ""));
                return Integer.compare(n1, n2);
            } catch (NumberFormatException e) {
                // 非数字命名的文件排到最后
                return f1.getName().compareTo(f2.getName());
            }
        });
        //同样的 lambda表达式，替代匿名内部类，快速实现  sort功能

        // 加载图片并验证
        imageList.clear();
        for (int i = 0; i < pngFiles.size(); i++) {
            File file = pngFiles.get(i);
            ImageIcon icon = new ImageIcon(file.getPath());

            // 验证图片是否加载成功
            if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                System.err.println("警告：图片加载失败 - " + file.getName());
                continue;
            }

            // 缩放图片到统一大小（64x64）
            Image scaledImage = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            imageList.add(scaledImage);
            System.out.println("成功加载：" + file.getName() + " → 索引" + i);
        }

        System.out.println("总共加载成功 " + imageList.size() + " 张图片");
    }
    public void handleClick(int x, int y) {
        if (animating) {
            return;
        }
        if (gameCallBack != null && !gameCallBack.isGameRunning()) {
            return;  // 游戏未运行（暂停/结束），禁止任何点击操作
        }
        //负责处理每一次点击及其是否消除


        Position pos = getPositionByPoint(x, y);
        if (pos == null) {
            return;
        }

        Cell clickedCell = gameBoard.getCell(pos.getRow(), pos.getCol());
        if (clickedCell == null || clickedCell.isEmpty()) {
            return;
        }


        if (firstSelected == null) {
            gameBoard.clearAllChosen();
            clickedCell.setChosen(true);
            firstSelected = pos;
            repaint();
            return;
        }

        if (firstSelected.equals(pos)) {
            gameBoard.clearAllChosen();
            firstSelected = null;
            secondSelected = null;
            repaint();
            return;
        }
        Cell firstCell = gameBoard.getCell(firstSelected.getRow(), firstSelected.getCol());
        secondSelected = pos;
        Cell secondCell = clickedCell;

        // 检查两个格子的图案是否相同
        if(firstCell.getIconIndex() == secondCell.getIconIndex()) {
            // 检查两个格子是否可以连线
            if (Utils.canLinkAB(gameBoard,firstSelected,secondSelected)) {
                List<Position> path = Utils.getLinkPath(gameBoard,firstSelected,secondSelected);
                if (path != null) {
                    gameBoard.clearAllChosen();
                    firstCell.setChosen(true);
                    secondCell.setChosen(true);
                    repaint();
                    final int iconIndexForCallback = clickedCell.getIconIndex();// 保存图标索引
                    Timer highlightTimer = new Timer(100, e -> {
                        animating = true;
                        showLinePath(path);
                        // 连线显示后启动渐隐动画（替代原来的直接消除）
                        startFadeAnimation(firstSelected, secondSelected, iconIndexForCallback);
                    });
                    //创建一个100ms的timer，不重复，调用 showLinePath的同时，启动渐隐动画
                    highlightTimer.setRepeats(false);
                    highlightTimer.start();
                    return;

                }else {
                    // 理论上 canLinkAB 为 true 则路径一定存在，这里防止有神秘  bug
                    gameBoard.clearAllChosen();
                    secondCell.setChosen(true);
                    firstSelected = secondSelected;
                    secondSelected = null;
                    repaint();
                }
            }else{
                // 图案相同但不能连线：取消第一个，将第二个设为选中（高亮）
                gameBoard.clearAllChosen();
                secondCell.setChosen(true);
                firstSelected = secondSelected;
                secondSelected = null;
                repaint();
            }
        } else {
            // 图案不同：取消第一个，将第二个设为选中
            gameBoard.clearAllChosen();
            secondCell.setChosen(true);
            firstSelected = secondSelected;
            secondSelected = null;
            repaint();
        }
    }
    public Rectangle getRectangle(Position position) {
        int x = position.getCol() * cellWidth;
        int y = position.getRow() * cellHeight;
        return new Rectangle(x, y, cellWidth, cellHeight);
    }
    //这里负责获取屏幕上对应的区域，其返回的是一个矩形，方便绘图

    //下面这一块是绘图的，直接用了原版的方法
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < gameBoard.getRowCnt(); i++) {
            for (int j = 0; j < gameBoard.getColCnt(); j++) {
                Rectangle rec = getRectangle(new Position(i, j));
                Cell cell = gameBoard.getCell(i, j);
                if (!cell.isEmpty() && cell.getIconIndex() < imageList.size()) {
                    Image img = imageList.get(cell.getIconIndex());

                    //此处插入了透明度相关的内容，
                    Float alpha = fadingCells.get(cell.getPos());
                    //此处调用的了对应cell的透明度
                    if (alpha != null) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                        g2d.drawImage(img, rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight(), this);
                        g2d.dispose();
                    } else {
                        g.drawImage(img, rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight(), this);
                    }
                }//这个表示如何绘制，AlphaComposite表示按照透明度与背景混合

                if (cell.getIsChosen()) {
                    g2.setColor(Color.RED);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawRect(
                            rec.getX() + 1,
                            rec.getY() + 1,
                            rec.getWidth() - 3,
                            rec.getHeight() - 3
                    );
                } else {
                    g2.setColor(Color.GRAY);
                    g2.setStroke(new BasicStroke(1));
                    g2.drawRect(
                            rec.getX(),
                            rec.getY(),
                            rec.getWidth() - 1,
                            rec.getHeight() - 1
                    );
                }
            }
        }
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(3));
        if (lineVisible) {
            for (Line line: lineList) {
                Rectangle rec1 = getRectangle(line.getCell1().getPos());
                Rectangle rec2 = getRectangle(line.getCell2().getPos());
                g.drawLine((int) rec1.getCenterPosition().getX(), (int) rec1.getCenterPosition().getY(), (int) rec2.getCenterPosition().getX(), (int) rec2.getCenterPosition().getY());
            }
        }
    }

    /**
     * 启动两个格子的图片渐隐动画
     * @param pos1     第一个格子位置
     * @param pos2     第二个格子位置
     * @param iconIndex 消除的图标索引（用于回调）
     */
    //以下是可以使用的渐隐方法
    private void startFadeAnimation(Position pos1, Position pos2, int iconIndex) {
        // 标记需要渐隐的格子，初始透明度为 1.0
        fadingCells.put(pos1, 1.0f);
        fadingCells.put(pos2, 1.0f);
        //重置timer
        if (fadeTimer != null && fadeTimer.isRunning()) {
            fadeTimer.stop();
        }

        final long startTime = System.currentTimeMillis();
        final int duration = 300; // 渐隐时长 300 毫秒
        //线性调整隐藏度
        fadeTimer = new Timer(30, e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float alpha = 1.0f - (float) elapsed / duration;

            if (alpha <= 0) {
                // 动画结束：清除渐隐标记、真正消除格子、重置状态
                fadeTimer.stop();
                fadingCells.clear();

                Cell cell1 = gameBoard.getCell(pos1.getRow(), pos1.getCol());
                Cell cell2 = gameBoard.getCell(pos2.getRow(), pos2.getCol());
                cell1.setEmpty(true);
                cell2.setEmpty(true);
                cell1.setChosen(false);
                cell2.setChosen(false);
                clearLine();
                firstSelected = null;
                secondSelected = null;
                animating = false;
                repaint();
                //调用删除对

                if (gameCallBack != null) {
                    gameCallBack.onPairEliminated(iconIndex);
                }
            } else {
                // 更新透明度并重绘（防 bug）
                fadingCells.put(pos1, alpha);
                fadingCells.put(pos2, alpha);
                repaint();
            }
        });
        //正经执行fadeTimer
        fadeTimer.start();
    }



    /**
     * 更新棋盘
     * 开始新游戏或加载存档时调用
     * @param newBoard 新的棋盘对象
     */
    //传参，更新棋盘（把所有东西重新长传一次）
    public void updateBoard(GameBoard newBoard) {
        this.gameBoard = newBoard;
        this.totalRow = newBoard.getRowCnt();
        this.totalCol = newBoard.getColCnt();
        this.cellWidth = this.width / this.totalCol;
        this.cellHeight = this.height / this.totalRow;
        this.firstSelected = null;
        this.secondSelected = null;
        this.animating = false;
        fadingCells.clear();
        if (fadeTimer != null && fadeTimer.isRunning()) {
            fadeTimer.stop();
        }
        clearLine();
        repaint();
    }
}
