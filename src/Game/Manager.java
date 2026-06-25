package Game;
//这个类包括了关系游戏状态的大多数内容，例如游戏的启动，输赢，游戏类型等等
//同时，后续在 ui部分看到的东西大部分来自于此！
import model.Cell;
import model.GameBoard;
import SaveAndLoad.SaveData;
import SaveAndLoad.SaveManager;
import User.UserManager;
import model.LevelManager;
import model.Position;
import utils.Utils;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Manager {

    //几种游戏状态
    public static final int stateREADY = 0;//准备
    public static final int stateRUNNING = 1;//运行
    public static final int statePAUSE = 2;//暂停
    public static final int stateWIN = 3;//赢
    public static final int stateLOSE = 4;//输
    //简单模式
    public static final int easyROWS = 11;
    public static final int easyCOLUMNS = 11;
    public static final int easyPATTERNS = 5;
    public static final int easyTIME = 120;
    //困难模式
    public static final int hardROWS = 12;
    public static final int hardCOLUMNS = 12;
    public static final int hardPATTERNS = 12;
    public static final int hardTIME = 300;

    //以上为游戏状态方面的参数

    private UserManager userManager;//用户管理
    private SaveManager saveManager;//存档管理
    private GameBoard gameBoard;//棋盘
    private LevelManager levelManager;
    private GameListener gameListener;//监听器

    //以上为声明的用户类对象与棋盘，监听器类引用

    private int remainingTime;// 剩余时间
    private int usedTime;//已用时间
    private int totalPairs;// 总对数
    private int eliminatedPairs;// 已消除对数
    private int score;// 当前分数
    private int validPairs;// 剩余可消除对数
    private Timer gameTimer;// 游戏计时器
    private String lastOperation; // 上一步操作记录
    private String assessment;

    //以上为计算得分，游戏计时，可消除对数等等数据的成员变量


    private boolean isLevelMode = false;
    private int gameState;//游戏当前状态

    //以上为单独的用于判断 state的变量



    public Manager(UserManager userManger, SaveManager saveManager){
        this.userManager = userManger;
        this.saveManager = saveManager;
        this.gameState = stateREADY;
        this.levelManager = new LevelManager();
        // 初始化游戏计时器
        initGameTimer();
        //将各个成员变量传入，并创建一个 Swing Timer

    }
    //下方代码用于将游戏状态重置到 ready

    public void resetToReadyState() {
        // 停止计时器
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        // 创建一个空棋盘（所有格子为空）
        int rows = easyROWS;
        int cols = easyCOLUMNS;
        Cell[][] emptyCells = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                emptyCells[i][j] = new Cell(new Position(i, j), true, -1);
            }
        }
        //将各个数值归零，重置一个 gameBoard
        this.gameBoard = new GameBoard(rows, cols, emptyCells);
        this.gameState = stateREADY;
        this.remainingTime = 0;
        this.usedTime = 0;
        this.eliminatedPairs = 0;
        this.score = 0;
        this.validPairs = 0;
        this.totalPairs = 0;
        this.lastOperation = "";

        // 通知 UI 更新为空白棋盘
        if (gameListener != null) {
            gameListener.Gamestart(gameBoard, score, remainingTime, usedTime, validPairs, eliminatedPairs, totalPairs);
        }
        //此处表示，用 gameListener向GameStart里面传入游戏初始化状态
    }

    private void initGameTimer(){
        gameTimer = new Timer(1000,e->{
            //这是一种 Lambda写法，ActionListener中有唯一方法，这样写相当于直接传参
            //最终效果为，每隔 1000ms，就执行一次 e，而e是通过Lambda传参进去的，也就是下方的 if语句
            if(gameState == stateRUNNING){
                remainingTime--;
                usedTime++;
                if(gameListener != null){
                    gameListener.TimeUpdate(remainingTime,usedTime);
                }
                //时间到
                if(remainingTime <= 0){
                    gameOver(false);
                }
            }
        });
        gameTimer.setRepeats(true);//表示对上面的 Timer，要重复执行，直到其他的·地方调用 .stop
    }
    public void startANewGame(String difficulty){

        isLevelMode=false;
        //表示当前不是 levelMode
        if (gameTimer.isRunning()) {
            gameTimer.stop();
        }
        //停止上面的 gameTimer

        String actualDifficulty = "easy".equals(difficulty) ? "easy" : "hard";
        //判断难度

        // 根据难度生成对应大小的棋盘
        if ("easy".equals(actualDifficulty)) {
            this.gameBoard = Utils.generateBoard(easyROWS, easyCOLUMNS, easyPATTERNS);
            this.remainingTime = easyTIME;
            this.totalPairs = 16;
        }
        //这里调用了 Util里的generateBoard，区分开了！

        else {
            this.gameBoard = Utils.generateBoard(hardROWS, hardCOLUMNS, hardPATTERNS);
            this.remainingTime = hardTIME;
            this.totalPairs = 50;
        }

        //确保生成的棋盘一定有解，避免死局
        while (!Utils.hasValidPairs(gameBoard)) {
            if ("easy".equals(actualDifficulty)) {
                this.gameBoard = Utils.generateBoard(easyROWS, easyCOLUMNS, easyPATTERNS);
            } else {
                this.gameBoard = Utils.generateBoard(hardROWS, hardCOLUMNS, hardPATTERNS);
            }
        }

        //初始化游戏状态
        this.usedTime = 0;
        this.eliminatedPairs = 0;
        this.score = 0;
        this.validPairs = Utils.countValidPairs(gameBoard);
        this.lastOperation = "";



        // 设置游戏状态为运行
        gameState = stateRUNNING;
        gameTimer.start();
        // 通知UI层：游戏开始了，更新界面
        if (gameListener != null) {
            //为什么有！= null？这是为了防止空指针
            gameListener.Gamestart(gameBoard, score, remainingTime,usedTime,validPairs,eliminatedPairs,totalPairs);
        }
    }


    //重新开始
    public void restartCurrentGame(){
        if(gameBoard == null){
            return;
        }
        String difficulty = (gameBoard.getRowCnt() == easyROWS) ?  "easy" : "hard";
        startANewGame(difficulty);
        //这里就是调用了上面的方法而已
    }

    //下面的代码负责消除的得分与是否消除部分

    public void eliminatePairs(int iconIndex){
        if(gameState != stateRUNNING){
            return;
        }
        int addedScore = CalculateScore.calculatePairScore();   // 本次消除得分
        int combo = CalculateScore.getCurrentCombo();


        score+=addedScore;
        //加分


        eliminatedPairs++;
        validPairs = Utils.countValidPairs(gameBoard);
        //重置可消除数量（可以多可以少）
        lastOperation = "消除了[" + iconIndex + "]x2";
        if(gameListener != null){
            gameListener.ScoreUpdate(score,validPairs,eliminatedPairs,totalPairs,lastOperation,addedScore,combo);
        }
        if(eliminatedPairs == totalPairs){
            gameOver(true);
        } else if(validPairs == 0) {
            gameOver(false);
        }


    }

    //成功true 失败false 保存游戏
    public boolean saveGame(int slot){
        if (gameState != stateRUNNING && gameState != statePAUSE) {
            return false;
        }
        String difficulty;
        if(gameBoard.getRowCnt() == 6){
            difficulty = "easy";
        }else {
            difficulty = "hard";
        }
        SaveData saveData = new SaveData(gameBoard, score, remainingTime, usedTime, difficulty, totalPairs, eliminatedPairs);
        return saveManager.saveGame(saveData,slot);
    }




    //成功返回true 失败返回false 从存档继续游戏
    public boolean continueGame(int slot){
        SaveData saveData = saveManager.loadGame(slot);
        if(saveData == null){
            return false;
        }

        this.gameBoard = saveData.getGameBoard();
        this.remainingTime = saveData.getTime();
        this.usedTime = saveData.getUsedTime();
        this.totalPairs = saveData.getTotalPairs();
        this.eliminatedPairs = saveData.getEliminatedPairs();
        this.score = saveData.getScore();
        this.validPairs = Utils.countValidPairs(gameBoard);
        this.gameState = stateRUNNING;
        gameTimer.start();
        if(gameListener != null){
            gameListener.Gamestart(gameBoard,score,remainingTime,usedTime,validPairs,eliminatedPairs,totalPairs);
        }
        return true;
    }




    //结束之后更新战绩
    public void gameOver(boolean isWin, int score){
        if(isWin){
            gameState = stateWIN;
        }else{
            gameState = stateLOSE;
        }
        gameTimer.stop();
        userManager.newHighestSCORE(score);
        if(gameListener != null){
            gameListener.GameOver(isWin,score);
        }
    }
    public void gameOver(boolean isWin){
        gameOver(isWin,this.score);
    }

    public void setGameListener(GameListener gameListener) {
        this.gameListener = gameListener;
    }
    public int getGameState() {
        return gameState;
    }
    public void pauseGame(){
        if(gameState == stateRUNNING){
            gameState = statePAUSE;
            gameTimer.stop();
        }
    }
    public void resumeGame(){
        if(gameState == statePAUSE){
            gameState = stateRUNNING;
            gameTimer.start();
        }
    }
    public GameBoard getGameBoard() {
        return gameBoard;
    }
    //以上是一系列的 setter/getter，以及暂停游戏，取消赞同的简单方法




    //以下为关卡模式的方法，与普通模式区分开了
    public void startLevelGame() {
        isLevelMode=true;
        if (gameTimer.isRunning()) gameTimer.stop();
        GameBoard board = levelManager.generateCurrentLevelBoard();
        this.gameBoard = board;


        int nonEmptyCount = 0;
        for (int i = 0; i < board.getRowCnt(); i++) {
            for (int j = 0; j < board.getColCnt(); j++) {
                Cell cell = board.getCell(i, j);
                if (cell != null && !cell.isEmpty() && cell.getIconIndex() > 0) {
                    nonEmptyCount++;
                }
            }
        }
        this.totalPairs = nonEmptyCount / 2;
        //这里又重新的调用了一次计算所有格子数量

        this.remainingTime = board.getRowCnt() * board.getColCnt(); // 时间可自定义
        this.usedTime = 0;
        this.eliminatedPairs = 0;
        this.score = 0;
        this.validPairs = Utils.countValidPairs(board);
        this.lastOperation = "";
        this.gameState = stateRUNNING;
        gameTimer.start();
        if (gameListener != null) {
            gameListener.Gamestart(gameBoard, score, remainingTime, usedTime, validPairs, eliminatedPairs, totalPairs);
        }
    }//相似的初始化部分


    //负责nextLevel部分
    public void nextLevel() {
        if(!isLevelMode){
            JOptionPane.showMessageDialog(null,"当前不是关卡模式！");
            return;
        }
        if (gameState != stateWIN) {
            JOptionPane.showMessageDialog(null, "请先完成当前关卡！");
            return;
        }

        if (levelManager.hasNextLevel()) {
            levelManager.nextLevel();
            startLevelGame();
        } else {
            JOptionPane.showMessageDialog(null, "恭喜通关所有关卡！");
            gameOver(true);
        }
    }
    public boolean isLevelMode() {
        return isLevelMode;
    }




    public int getCurrentLevel() {
        return levelManager.getCurrentLevel();
    }




   //这一部分为接收器，这是专门用来将核心逻辑与界面解耦的方法，上述的大部分方法都接入了这个接口
    //当游戏状态发生变化，统一通过这个接口向界面传递信息，非常好用
    public interface GameListener{
        void Gamestart(GameBoard gameBoard, int score,int remainingTime,int usedTime,int validPairs,int eliminatedPairs,int totalPairs);//游戏开始
        void GameOver(boolean isWin, int score);//游戏结束
        void TimeUpdate(int remainingTime, int usedTime);//时间
        void ScoreUpdate(int score, int validPairs, int eliminatedPairs, int totalPairs, String lastOperation,int ScoreUpdate,int combo);
    }
    public Position[] getHintPair(){
        if( gameBoard==null || gameState!= stateRUNNING)
            return null;
        int rows=gameBoard.getRowCnt();
        int cols=gameBoard.getColCnt();
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = gameBoard.getCell(i, j);
                if (cell != null && !cell.isEmpty()) {
                    positions.add(new Position(i, j));
                }
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            Position p1 = positions.get(i);
            Cell c1 = gameBoard.getCell(p1.getRow(), p1.getCol());
            for (int j = i + 1; j < positions.size(); j++) {
                Position p2 = positions.get(j);
                Cell c2 = gameBoard.getCell(p2.getRow(), p2.getCol());
                if (c1.getIconIndex() == c2.getIconIndex() && Utils.canLinkAB(gameBoard, p1, p2)) {
                    return new Position[]{p1, p2};
                }
            }
        }
        return null;




    }








}
