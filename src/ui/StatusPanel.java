package ui;

import javax.swing.*;
import java.awt.*;
import Game.CalculateScore;
import Game.Manager;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class StatusPanel extends JPanel {
    private JLabel statusLabel;
    private JLabel timeLabel;
    private JLabel usedTimeLabel;
    // 分数标签
    private JLabel scoreLabel;
    // 剩余可消除对数标签
    private JLabel pairsLabel;
    // 关卡进度条
    private JProgressBar progressBar;
    // 上一步操作记录标签
    private JLabel operationLabel;
    // 暂停/继续按钮
    private JButton pauseButton;
    private JLabel highestScoreLabel;
    private JLabel usernameLabel;
    private JLabel addScoreLabel;   // 本次得分
    private JLabel comboLabel;      // 连击数
    private JLabel assessment;
    // 重新开始按钮
    private JButton restartButton;
    private JLabel levelLabel;
    private final int width;
    private final int height;
    private Manager gameManager;
    public StatusPanel(int offSetX, int offSetY,int width, int height) {
        this.width = width;
        this.height = height;
        this.setLayout(new BorderLayout(10,0));
        this.setBounds(offSetX, offSetY, width, height);
        this.setBorder(new EmptyBorder(5,10,5,10));
        this.setBackground(Color.WHITE);
        initComponents();
    }


    private void initComponents() {
        statusLabel = new JLabel("准备中：");
        statusLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        statusLabel.setForeground(new Color(224, 32, 64));

        // 剩余时间标签
        timeLabel = new JLabel("剩余: 00:00");
        timeLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        timeLabel.setForeground(Color.BLACK);

        // 已用时间标签
        usedTimeLabel = new JLabel("已用: 00:00");
        usedTimeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        usedTimeLabel.setForeground(new Color(80, 80, 80));
        // 分数标签
        scoreLabel = new JLabel("分数: 0");
        scoreLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        scoreLabel.setForeground(Color.BLACK);

        // 剩余可消除对数标签
        pairsLabel = new JLabel("剩余可消除: 0");
        pairsLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        pairsLabel.setForeground(new Color(80, 80, 80));

        // 进度条
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true); // 显示百分比文字
        progressBar.setFont(new Font("微软雅黑", Font.BOLD, 12));
        progressBar.setPreferredSize(new Dimension(100, 25));
        //
        usernameLabel = new JLabel("用户: 未登录");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        usernameLabel.setForeground(new Color(80, 80, 80));

        highestScoreLabel = new JLabel("最高分: --");
        highestScoreLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        highestScoreLabel.setForeground(new Color(255, 140, 0));
        // 操作记录标签
        operationLabel = new JLabel("上一步: 无");
        operationLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        operationLabel.setForeground(Color.GRAY);

        levelLabel = new JLabel("关卡: 1");
        levelLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("微软雅黑", Font.BOLD, 12));
        progressBar.setForeground(new Color(0, 0, 0));
        progressBar.setBackground(new Color(230, 230, 230)); // 浅灰色背景
        progressBar.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        progressBar.setPreferredSize(new Dimension(100, 25));

        operationLabel = new JLabel("上一步: 无");
        operationLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        operationLabel.setForeground(new Color(120, 120, 120)); // 灰色

        // 暂停/继续按钮
        pauseButton = createApexButton("暂停", 14);
        pauseButton.addActionListener(e -> {
            if (gameManager != null) {
                if (gameManager.getGameState() == Manager.stateRUNNING) {
                    gameManager.pauseGame();
                    pauseButton.setText("继续");
                    statusLabel.setText("已暂停");
                } else if (gameManager.getGameState() == Manager.statePAUSE) {
                    gameManager.resumeGame();
                    pauseButton.setText("暂停");
                    statusLabel.setText("运行中");
                }
            }
        });
        highestScoreLabel = new JLabel("最高分: --");
        highestScoreLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        highestScoreLabel.setForeground(new Color(255, 140, 0)); // 橙色


        addScoreLabel = new JLabel("+0");
        addScoreLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        addScoreLabel.setForeground(new Color(255, 100, 0));

        comboLabel = new JLabel("连击: 0");
        comboLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        comboLabel.setForeground(new Color(0, 150, 0));

        assessment = new JLabel("连击评价: 普通");
        assessment.setFont(new Font("微软雅黑", Font.BOLD, 16));
        assessment.setForeground(Color.DARK_GRAY);
        assessment.setBackground(Color.LIGHT_GRAY);
        assessment.setOpaque(true);                     // 使背景色生效
        assessment.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        assessment.setPreferredSize(new Dimension(150, 35));


        // 重新开始按钮
        restartButton = createApexButton("重新开始", 14);
        restartButton.addActionListener(e -> {
            if (gameManager != null) {
                gameManager.restartCurrentGame();
            }
        });

        // 顶部面板（纯白背景）
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(statusLabel);
        topPanel.add(timeLabel);
        topPanel.add(usedTimeLabel);
        topPanel.add(scoreLabel);
        topPanel.add(usernameLabel);
        topPanel.add(pairsLabel);
        topPanel.add(scoreLabel);
        topPanel.add(addScoreLabel);
        topPanel.add(comboLabel);
        // 底部面板（纯白背景）
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(operationLabel);
        bottomPanel.add(assessment);
        bottomPanel.add(progressBar);
        bottomPanel.add(pauseButton);
        bottomPanel.add(restartButton);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(topPanel);
        centerPanel.add(bottomPanel);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    public void updateLevel(int level) {
        levelLabel.setText("关卡: " + level);
    }
    /**
     * 设置游戏管理器引用
     * @param gameManager 游戏管理器对象
     */
    public void setGameManager(Manager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * 更新时间显示
     * @param remainingTime 剩余时间（秒）
     * @param usedTime 已用时间（秒）
     */
    public void updateTime(int remainingTime, int usedTime){
        int rMin = remainingTime / 60;
        int rSec = remainingTime % 60;
        timeLabel.setText(String.format("剩余：%02d:%02d", rMin, rSec));
        timeLabel.setForeground(remainingTime <= 10? Color.RED : Color.BLACK);

        int uMin = usedTime / 60;
        int uSec = usedTime % 60;
        usedTimeLabel.setText(String.format("已使用:%02d:%02d", uMin, uSec));
    }



    /**
     * 更新分数、剩余可消除对数和进度
     * @param score 当前分数
     * @param validPairs 剩余可消除对数
     * @param eliminatedPairs 已消除对数
     * @param totalPairs 总对数
     * @param lastOperation 上一步操作记录
     */
    public void updateScore(int score,int validPairs,int eliminatedPairs,int totalPairs,String lastOperation, int addedScore, int combo){
        this.scoreLabel.setText("分数:"+ score);
        this.pairsLabel.setText("剩余可消除:"+validPairs);
        int progress = (int)((double)eliminatedPairs/totalPairs*100);
        progressBar.setValue(progress);
        progressBar.setString(progress + "%");
        if(lastOperation != null && !lastOperation.isEmpty()){
            operationLabel.setText("上一步"+lastOperation);
        }
        addScoreLabel.setText("+" + addedScore);
        comboLabel.setText("连击: " + combo);
        updateAssessmentByCombo(combo);
    }



    public void updateHighestScore(int highestScore) {
        highestScoreLabel.setText("最高分: " + highestScore);
    }

    // 添加公共方法：清空最高分显示（游客模式）
    public void clearHighestScore() {
        highestScoreLabel.setText("最高分: --");
    }


    /**
     * 重置所有状态
     * 开始新游戏时调用
     */
    public void reset() {
        statusLabel.setText("运行中");
        timeLabel.setText("剩余: 00:00");
        timeLabel.setForeground(Color.BLACK);
        usedTimeLabel.setText("已用: 00:00");
        scoreLabel.setText("分数: 0");
        pairsLabel.setText("剩余可消除: 0");
        progressBar.setValue(0);
        progressBar.setString("0%");
        operationLabel.setText("上一步: 无");
        pauseButton.setText("暂停");
    }
    public void setStatus(String text) {
        statusLabel.setText(text);
    }
    private JButton createApexButton(String text, int fontSize) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
        button.setBackground(new Color(224, 32, 64)); // 亮红色背景
        button.setForeground(Color.WHITE); // 白色文字
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 60, 90)); // 悬停变亮
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(224, 32, 64)); // 恢复原色
            }
        });
        return button;
    }
    public void updateUserInfo(String username, int highestScore) {
        if (username == null || username.isEmpty()) {
            usernameLabel.setText("用户: 游客");
        } else {
            usernameLabel.setText("用户: " + username);
        }
        highestScoreLabel.setText("最高分: " + highestScore);
    }
    private void updateAssessmentByCombo(int combo) {
        String text;
        Font font;
        Color foreground;
        Color background;
        Border border;

        if (combo <= 0) {
            text = "拉完了";
            font = new Font("微软雅黑", Font.PLAIN, 14);
            foreground = Color.DARK_GRAY;
            background = Color.LIGHT_GRAY;
            border = BorderFactory.createLineBorder(Color.GRAY);
        } else if (combo == 1) {
            text = "NPC";
            font = new Font("微软雅黑", Font.BOLD, 16);
            foreground = Color.WHITE;
            background = new Color(66, 133, 244);
            border = BorderFactory.createRaisedBevelBorder();
        } else if (combo == 2) {
            text = "人上人!!";
            font = new Font("微软雅黑", Font.BOLD, 18);
            foreground = Color.YELLOW;
            background = new Color(219, 68, 55);
            border = BorderFactory.createLineBorder(Color.YELLOW, 2);
        } else if (combo == 3) {
            text = "顶级!!!";
            font = new Font("微软雅黑", Font.BOLD, 20);
            foreground = Color.WHITE;
            background = new Color(234, 67, 53);
            border = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.ORANGE, 2),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            );
        } else if (combo >= 4 && combo <= 6) {
            text = "夯!!";
            font = new Font("微软雅黑", Font.BOLD | Font.ITALIC, 22);
            foreground = Color.WHITE;
            background = new Color(255, 87, 34);
            border = BorderFactory.createLineBorder(Color.YELLOW, 3);
        } else {
            text = "无敌";
            font = new Font("微软雅黑", Font.BOLD | Font.ITALIC, 26);
            foreground = Color.BLACK;
            background = new Color(255, 215, 0);
            border = BorderFactory.createLineBorder(Color.RED, 4);
        }

        assessment.setText("热度: " + text);
        assessment.setFont(font);
        assessment.setForeground(foreground);
        assessment.setBackground(background);
        assessment.setBorder(border);
    }

}
