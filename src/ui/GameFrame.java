package ui;

import model.Cell;
import model.GameBoard;
import model.Position;

import javax.swing.*;
import java.awt.*;

// ========== 新增：导入新增的包 ==========
import Game.Manager;
import SaveAndLoad.SaveManager;
import User.UserManager;
//本类表示各个模型的导入，以及不同的管理器，都在此处
/**
 * 游戏主窗口类
 * 程序的主窗口，管理所有面板的切换和游戏的整体流程
 *
 * 设计思路：
 * 1. 继承JFrame，作为程序的主窗口
 * 2. 使用CardLayout管理不同的面板（登录、注册、游戏）
 * 3. 初始化所有管理器和面板
 * 4. 处理游戏状态变化
 */
public class GameFrame extends JFrame{
    // 窗口宽度
    private int width;
    // 窗口高度
    private int height;
    // 窗口标题
    private String title;
    // 状态栏面板
    private StatusPanel statusPanel;
    // 卡片布局：用于切换不同的面板（登录、注册、游戏）
    private CardLayout cardLayout;
    // 主面板：包含所有其他面板
    private JPanel mainPanel;
    // 棋盘面板
    private BoardPanel boardPanel;
    // 登录面板
    private LoginPanel loginPanel;
    // 注册面板
    private RegisterPanel registerPanel;
    private MainMenuPanel mainMenuPanel;

    // 管理器
    private UserManager userManager;    // 用户管理器
    private SaveManager saveManager;    // 存档管理器
    private Manager gameManager;

    private GameControlPanel controlPanel;
    // 游戏管理器
    //以上为各种引用类型
    /**
     * 构造方法：创建一个新的游戏主窗口
     * @param title 窗口标题
     * @param width 窗口宽度
     * @param height 窗口高度
     */
    public GameFrame(String title, int width, int height) {
        //以下为面板的创建全过程
        super(title);
        this.setResizable(false);
        this.title = title;
        this.width = width;
        this.height = height;



        // ========== 修改：使用卡片布局 ==========
        // 初始化卡片布局
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 初始化管理器
        userManager = new UserManager();
        saveManager = new SaveManager(userManager);
        gameManager = new Manager(userManager, saveManager);

        mainMenuPanel = new MainMenuPanel(this, gameManager,userManager);
        mainPanel.add(mainMenuPanel, "mainMenu");

        // 创建登录和注册面板
        loginPanel = new LoginPanel(this, userManager);
        registerPanel = new RegisterPanel(this, userManager);

        // 创建状态面板
        statusPanel = new StatusPanel(0, 0, 800, 100);
        statusPanel.setGameManager(gameManager);

        // 创建一个临时棋盘（会被游戏管理器替换）
        Cell[][] board = new Cell[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = new Cell(new Position(i, j), true, 0);
            }
        }
        // 创建棋盘面板
        boardPanel = new BoardPanel(new GameBoard(5, 5, board), 0, 100, 800, 800);

        // 创建控制面板
        controlPanel = new GameControlPanel(this, gameManager, userManager, saveManager,boardPanel,0, 900, 800, 100);

        // 创建游戏面板，包含状态面板、棋盘面板和控制面板
        JPanel gamePanel = new JPanel(null);
        gamePanel.add(statusPanel);
        gamePanel.add(boardPanel);
        gamePanel.add(controlPanel);

        // 将所有面板添加到主面板
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        mainPanel.add(gamePanel, "game");


        // 绑定游戏事件监听器
        //观察者模式：游戏逻辑层状态变化时主动通知  UI更新
        gameManager.setGameListener(new Manager.GameListener() {
            @Override
            public void Gamestart(GameBoard gameBoard, int score, int remainingTime, int usedTime, int validPairs, int eliminatedPairs, int totalPairs) {
                boardPanel.updateBoard(gameBoard);
                statusPanel.reset();
                statusPanel.updateTime(remainingTime, usedTime);
                statusPanel.updateScore(score, validPairs, eliminatedPairs, totalPairs, "",0,0);
                statusPanel.updateLevel(gameManager.getCurrentLevel());

                // 关键：根据当前模式显示/隐藏下一关按钮
                controlPanel.setLevelMode(gameManager.isLevelMode());
            }

            @Override
            public void GameOver(boolean isWin, int score) {
                //更新状态栏
                statusPanel.setStatus(isWin ? "WIN" : "LOSE");
                // 显示游戏结束对话框
                String message = isWin ? "恭喜你赢了！" : "游戏结束！";
                JOptionPane.showMessageDialog(GameFrame.this, message + "\n得分：" + score);
                refreshHighestScore();
            }

            @Override
            public void ScoreUpdate(int score, int validPairs, int eliminatedPairs, int totalPairs, String lastOperation, int addedScore, int combo) {
                //分数更新
                statusPanel.updateScore(score, validPairs, eliminatedPairs, totalPairs,
                        lastOperation, addedScore, combo);
            }
            @Override
            public void TimeUpdate(int remainingTime,int usedTime) {
                statusPanel.updateTime(remainingTime,usedTime);
            }
        });


        // 绑定棋盘回调：棋盘事件通知游戏管理器
        boardPanel.setGameCallBack(new BoardPanel.GameCallBack() {
            @Override
            public void onPairEliminated(int iconIndex){
                gameManager.eliminatePairs(iconIndex);
            }
            @Override
            public void onGameOver(boolean isWin) {
                // 现在由游戏管理器统一处理游戏结束
            }
            @Override
            public boolean isGameRunning() {
                return gameManager.getGameState() == Manager.stateRUNNING;
            }
        });

        this.add(mainPanel);
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        controlPanel.setLevelMode(gameManager.isLevelMode());
        showLoginPanel();
    }


    public void refreshHighestScore() {
        if (userManager.isOnline()) {
            int highest = userManager.getuserNow().getHighestSCORE();
            statusPanel.updateHighestScore(highest);
            statusPanel.updateUserInfo(userManager.getuserNow().getUserName(), highest);
        } else {
            statusPanel.clearHighestScore();
            statusPanel.updateUserInfo(null, 0);
        }
    }












    public void resetGameUI() {
        gameManager.resetToReadyState();  // 重置游戏逻辑
        // 强制刷新棋盘面板（resetToReadyState 中已通过回调更新，但确保 boardPanel 同步）
        boardPanel.updateBoard(gameManager.getGameBoard()); // 注意：需要给 Manager 添加 getGameBoard() 方法
        statusPanel.reset();               // 重置状态栏显示
    }
    public void showMainMenu() {
        // 刷新用户信息
        if (userManager.isOnline()) {
            mainMenuPanel.updateUserInfo(userManager.getuserNow().getUserName(), userManager.getuserNow().getHighestSCORE());
        } else {
            mainMenuPanel.updateUserInfo(null, 0);
        }
        cardLayout.show(mainPanel, "mainMenu");
    }

    public void showGamePanel() {
        refreshHighestScore();   // 更新状态栏中的用户信息和最高分
        cardLayout.show(mainPanel, "game");
    }
    public void showRegisterPanel(){
        cardLayout.show(mainPanel, "register");

    }

    public void showLoginPanel() {
        gameManager.resetToReadyState();
        resetGameUI();
        cardLayout.show(mainPanel, "login");
    }

    // 从游戏内“退出到主界面”时调用
    public void exitToMainMenu() {
        gameManager.resetToReadyState();   // 重置游戏逻辑
        resetGameUI();                     // 重置界面显示
        showMainMenu();
    }
    public void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "确定要登出吗？", "登出", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            userManager.logout();          // 清除登录用户
            showLoginPanel();              // 返回登录界面
        }
    }


}
