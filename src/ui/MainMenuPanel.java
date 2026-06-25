package ui;
import Game.Manager;
import javax.swing.*;
import java.awt.*;

import User.User;
import User.UserManager;
import ui.SettingsDialog;


public class MainMenuPanel extends JPanel {
    private GameFrame gameFrame;
    private Manager gameManager;
    private JLabel welcomeLabel;
    private JLabel highestScoreLabel;
    private UserManager userManager;

    public MainMenuPanel(GameFrame gameFrame, Manager gameManager, UserManager userManager) {
        this.gameFrame = gameFrame;
        this.userManager = userManager;
        this.gameManager = gameManager;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // 标题
        JLabel titleLabel = new JLabel("连连看");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 72));
        titleLabel.setForeground(new Color(224, 32, 64));
        add(titleLabel, gbc);

        gbc.gridy = 1;
        welcomeLabel = new JLabel("欢迎，游客");
        welcomeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 24));
        add(welcomeLabel, gbc);

        gbc.gridy = 2;
        highestScoreLabel = new JLabel("最高分: --");
        highestScoreLabel.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        add(highestScoreLabel, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        buttonPanel.setOpaque(false);
        JButton easyButton = createMenuButton("简单模式");
        JButton hardButton = createMenuButton("困难模式");
        JButton levelButton = createMenuButton("关卡模式");
        JButton logoutButton = createMenuButton("登出");

        easyButton.addActionListener(e -> startGame("easy"));
        hardButton.addActionListener(e -> startGame("hard"));
        levelButton.addActionListener(e -> startLevelGame());
        logoutButton.addActionListener(e -> gameFrame.logout());

        buttonPanel.add(easyButton);
        buttonPanel.add(hardButton);
        buttonPanel.add(levelButton);
        buttonPanel.add(logoutButton);
        gbc.gridy = 3;
        add(buttonPanel, gbc);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("微软雅黑", Font.BOLD, 28));
        btn.setBackground(new Color(224, 32, 64));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(255, 60, 90));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(224, 32, 64));
            }
        });
        return btn;
    }

    private void startGame(String difficulty) {
        gameManager.startANewGame(difficulty);
        gameFrame.showGamePanel();
    }

    private void startLevelGame() {
        gameManager.startLevelGame();
        gameFrame.showGamePanel();
    }

    public void updateUserInfo(String username, int highestScore) {
        if (username == null || username.isEmpty()) {
            welcomeLabel.setText("欢迎，游客");
            highestScoreLabel.setText("最高分: --");
        } else {
            welcomeLabel.setText("欢迎，" + username);
            highestScoreLabel.setText("最高分: " + highestScore);
        }
    }
}
