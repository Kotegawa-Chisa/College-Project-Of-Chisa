package utils;

import model.Cell;
import model.GameBoard;
import model.Position;
import Game.Manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Utils {
    private static final Random random = new Random();
    public static List<Cell> getReachablePointsInFourDirections(GameBoard gameBoard, Position posA) {
        List<Cell> res = new ArrayList<>();
        for (int i = posA.getRow() + 1; i < gameBoard.getRowCnt(); i++) {
            if (gameBoard.getCell(i, posA.getCol()).isEmpty()) {
                res.add(gameBoard.getCell(i, posA.getCol()));
            } else {
                break;
            }
        }
        for (int i = posA.getRow() - 1; i >= 0; i--) {
            if (gameBoard.getCell(i, posA.getCol()).isEmpty()) {
                res.add(gameBoard.getCell(i, posA.getCol()));
            } else {
                break;
            }
        }
        for (int i = posA.getCol() + 1; i < gameBoard.getColCnt(); i++) {
            if (gameBoard.getCell(posA.getRow(), i).isEmpty()) {
                res.add(gameBoard.getCell(posA.getRow(), i));
            } else {
                break;
            }
        }
        for (int i = posA.getCol() - 1; i >= 0; i--) {
            if (gameBoard.getCell(posA.getRow(), i).isEmpty()) {
                res.add(gameBoard.getCell(posA.getRow(), i));
            } else {
                break;
            }
        }
        return res;
    }
    public static boolean findZeroTurn(GameBoard gameBoard, Position posA, Position posB) {
        boolean tmpRes0 = true;
        if(posA.equals(posB))
            return false;
        if (posA.getCol() == posB.getCol()) {
            int smallLine = Math.min(posA.getRow(), posB.getRow());
            int largeLine = Math.max(posA.getRow(), posB.getRow());
            for (int i = smallLine + 1; i < largeLine; i++) {
                if (!gameBoard.getCell(i, posA.getCol()).isEmpty()) {
                    tmpRes0 = false;
                    break;
                }
            }
            if (tmpRes0) {
                return true;
            }
        }

        if (posA.getRow() == posB.getRow()) {
            tmpRes0 = true;
            int smallCol = Math.min(posA.getCol(), posB.getCol());
            int largeCol = Math.max(posA.getCol(), posB.getCol());
            for (int i = smallCol + 1; i < largeCol; i++) {
                if (!gameBoard.getCell(posA.getRow(), i).isEmpty()) {
                    tmpRes0 = false;
                    break;
                }
            }
            if (tmpRes0) {
                return true;
            }
        }
        return false;
    }
    public static boolean findOneTurn(GameBoard gameBoard, Position posA, Position posB) {
        if (posA.getCol() != posB.getCol() && posA.getRow() != posB.getRow()) {
            Position cornerPoint1 = new Position(posA.getRow(), posB.getCol());
            Position cornerPoint2 = new Position(posB.getRow(), posA.getCol());
            if (cornerPoint1.isEmpty(gameBoard)&&findZeroTurn(gameBoard, posA, cornerPoint1) && findZeroTurn(gameBoard, posB, cornerPoint1)) {
                return true;
            }
            if (cornerPoint2.isEmpty(gameBoard) && findZeroTurn(gameBoard, posB, cornerPoint2)&&findZeroTurn(gameBoard,posA,cornerPoint2)) {
                return true;
            }
        }
        return false;
    }
    public static boolean findTwoTurn(GameBoard gameBoard, Position posA, Position posB) {
        List<Cell> reachablePoints = getReachablePointsInFourDirections(gameBoard, posA);
        for (Cell c: reachablePoints) {
            if (findOneTurn(gameBoard, c.getPos(), posB)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canLinkAB(GameBoard gameBoard, Position posA, Position posB){
        //空指针检查
        if (posA == null || posB == null || gameBoard == null) {
            return false;
        }
        //相同位置不能连线
        if (posA.equals(posB)) {
            return false;
        }
        if(gameBoard.getCell(posA.getRow(), posA.getCol()).getIconIndex()==gameBoard.getCell(posB.getRow(), posB.getCol()).getIconIndex()) {
            if (findZeroTurn(gameBoard, posA, posB)) {
                return true;
            }
            // 判断1折，检查两个拐点
            if (findOneTurn(gameBoard, posA, posB)) {
                return true;
            }
            // 判断2折
            if (findTwoTurn(gameBoard, posA, posB)) {
                return true;
            }
        }
        return false;

    }


    /**
     * 获取两个格子之间的连线路径（拐点序列）
     * @param gameBoard 棋盘
     * @param posA 起点
     * @param posB 终点
     * @return 路径上的点列表（包括起点和终点），若不可达返回 null
     */
    //返回三个点
    public static List<Position> getLinkPath (GameBoard gameBoard, Position posA, Position posB) {
        if(!canLinkAB(gameBoard, posA, posB)) {
            return null;
        }
        if(findZeroTurn(gameBoard, posA, posB)) {
            return buildZeroTurnPath(posA,posB);
        }
        Position corner = findOneTurnCorner(gameBoard,posA,posB);
        if(corner!=null) {
            return buildOneTurnPath(posA,corner,posB);
        }
        List<Position> twoTurnPath = findTwoTurnPath(gameBoard,posA,posB);
        if(twoTurnPath != null){
            return twoTurnPath;
        }
        return null;
    }

    // 辅助方法：直线路径，表示最基础的连线
    private static List<Position> buildZeroTurnPath(Position posA, Position posB) {
        List<Position> path = new ArrayList<>();
        path.add(posA);
        if(posA.getRow() == posB.getRow()) {
            int step = (posA.getCol() <  posB.getCol()) ? 1 : -1;
            for(int i = posA.getCol() + step;i != posB.getCol();i += step) {
                path.add(new Position(posA.getRow(), i));
            }
        }else if(posA.getCol() == posB.getCol()) {
            int step = (posA.getRow() <  posB.getRow()) ? 1 : -1;
            for(int i = posA.getRow() + step; i != posB.getRow();i += step) {
                path.add(new Position(i,posA.getCol()));
            }
        }
        path.add(posB);
        return path;
    }

    //寻找1拐点
    private static Position findOneTurnCorner(GameBoard gameBoard, Position posA, Position posB) {
        Position corner1 = new Position(posA.getRow(), posB.getCol());
        if(corner1.isEmpty(gameBoard) && findZeroTurn(gameBoard, posA, corner1) && findZeroTurn(gameBoard, corner1, posB)) {
            return corner1;
        }
        Position corner2 = new Position(posB.getRow(), posA.getCol());
        if(corner2.isEmpty(gameBoard) && findZeroTurn(gameBoard,posA, corner2) && findZeroTurn(gameBoard, corner2, posB)) {
            return corner2;
        }
        return null;
    }

    //构造1拐点路径
    private static List<Position> buildOneTurnPath( Position posA, Position corner, Position posB) {
        List<Position> path = new ArrayList<>();
        path.add(posA);
        path.addAll(buildZeroTurnPath(posA,corner));
        path.remove(corner);
        path.addAll(buildZeroTurnPath(corner,posB));
        return path;
    }

    //寻找2拐点路径
    private static List<Position> findTwoTurnPath(GameBoard gameBoard, Position posA, Position posB) {
        List<Cell> reachablePoints = getReachablePointsInFourDirections(gameBoard, posA);
        for (Cell cell: reachablePoints) {
            Position corner = cell.getPos();
            if(findOneTurn(gameBoard, cell.getPos(), posB)) {
                Position midcorner = findOneTurnCorner(gameBoard, corner, posB);
                if(midcorner!=null) {
                    List<Position> path = new ArrayList<>();
                    path.addAll(buildZeroTurnPath(posA,corner));
                    path.remove(corner);
                    path.addAll(buildOneTurnPath(corner,midcorner,posB));
                    return path;
                }
            }
        }
        return null;
    }


    public static GameBoard generateBoard(int rows, int cols, int iconCount){
        int totalRows=rows;
        int totalCols=cols;
        Cell[][] boardData=new Cell[totalRows][totalCols];

        // 初始化边框
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                boardData[i][j] = new Cell(new Position(i, j), true, -1);
            }
        }

        // 2. 根据棋盘尺寸确定有效区域（非边框的填充区域）
        List<Position> validPositions = new ArrayList<>();
        if (rows == 11 && cols == 11) {               // 简单模式：两个 4×4 对角区域
            for (int i = 1; i <= 4; i++) {
                for (int j = 1; j <= 4; j++) {
                    validPositions.add(new Position(i, j));
                }
            }
            for (int i = 6; i <= 9; i++) {
                for (int j = 6; j <= 9; j++) {
                    validPositions.add(new Position(i, j));
                }
            }
        } else if (rows == 12 && cols == 12) {        // 困难模式：中央 10×10 区域
            for (int i = 1; i <= 10; i++) {
                for (int j = 1; j <= 10; j++) {
                    validPositions.add(new Position(i, j));
                }
            }
        } else {
            // 其他尺寸（后备）全填充
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    validPositions.add(new Position(i, j));
                }
            }
        }

        int totalCells = validPositions.size();
        if (totalCells % 2 != 0) {
            throw new IllegalArgumentException("有效格子数必须为偶数");
        }
        int totalPairs = totalCells / 2;

        // 3. 生成成对的图标列表（图标索引从 1 到 iconCount）
        List<Integer> iconList = new ArrayList<>();
        for (int i = 0; i < totalPairs; i++) {
            int icon = random.nextInt(iconCount) + 1;
            iconList.add(icon);
            iconList.add(icon);
        }
        Collections.shuffle(iconList);

        // 4. 填入棋盘
        for (int idx = 0; idx < validPositions.size(); idx++) {
            Position pos = validPositions.get(idx);
            boardData[pos.getRow()][pos.getCol()] = new Cell(pos, false, iconList.get(idx));
        }

        return new GameBoard(rows, cols, boardData);

    }
    //统计棋盘上剩余可消除的对数
    public static int countValidPairs(GameBoard gameBoard){
        if(gameBoard == null){
            return 0;
        }
        int rows = gameBoard.getRowCnt();
        int cols = gameBoard.getColCnt();
        ArrayList<Cell> nonEmptyCells = new ArrayList<>();
        for(int i=0; i < rows;i++){
            for(int j=0; j < cols;j++){
                Cell cell = gameBoard.getCell(i,j);
                if(cell != null && !cell.isEmpty()&& cell.getIconIndex() > 0){
                    nonEmptyCells.add(cell);
                }
            }
        }
        //统计不重复的可消除对
        boolean[] matched = new boolean[nonEmptyCells.size()];
        int count = 0;
        for(int i=0; i <  nonEmptyCells.size();i++){
            if(matched[i]){
                continue;//已配对就跳过
            }
            Cell cell1 = nonEmptyCells.get(i);
            for(int j =i+1; j < nonEmptyCells.size();j++){
                if(matched[j]){
                    continue;
                }
                Cell cell2 = nonEmptyCells.get(j);
                if(cell1.getIconIndex() == cell2.getIconIndex() && canLinkAB(gameBoard, cell1.getPos(), cell2.getPos())){
                    count++;
                    matched[i] = true;
                    matched[j] = true;
                    break;
                }
            }
        }
        return count;
    }


    public static boolean hasValidPairs(GameBoard gameBoard) {
        return countValidPairs(gameBoard) > 0;
    }
    public static boolean isBoardEmpty(GameBoard gameBoard){
        boolean isEmpty=true;
        for (int i = 0; i < gameBoard.getColCnt(); i++) {
            for (int j = 0; j < gameBoard.getRowCnt(); j++) {

                if (!gameBoard.getCell(j, i).isEmpty())
                    return false;
            }
        }
        return true;

    }



}