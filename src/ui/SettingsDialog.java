package ui;

import Game.Manager;
import SaveAndLoad.SaveManager;
import User.UserManager;
import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends JDialog {
    private GameFrame gameFrame;
    private Manager gameManager;
    private SaveManager saveManager;
    private UserManager userManager;

    public SettingsDialog(GameFrame gameFrame, Manager gameManager, SaveManager saveManager, UserManager userManager) {
        super(gameFrame, "设置", true);
        this.gameFrame = gameFrame;
        this.gameManager = gameManager;
        this.saveManager = saveManager;
        this.userManager = userManager;
        setSize(400, 300);
        setLocationRelativeTo(gameFrame);
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton saveButton = createApexButton("保存游戏", 16);
        JButton loadButton = createApexButton("加载游戏", 16);
        JButton logoutButton = createApexButton("登出", 16);
        JButton exitButton = createApexButton("退出到主界面", 16);

        saveButton.addActionListener(e -> saveGame());
        loadButton.addActionListener(e -> loadGame());
        logoutButton.addActionListener(e -> logout());
        exitButton.addActionListener(e -> exitToMainMenu());

        add(saveButton, gbc);
        gbc.gridy++;
        add(loadButton, gbc);
        gbc.gridy++;
        add(logoutButton, gbc);
        gbc.gridy++;
        add(exitButton, gbc);
    }

    private void saveGame() {
        if (!userManager.isOnline()) {
            JOptionPane.showMessageDialog(this, "请先登录！");
            return;
        }
        SaveSelectDialog dialog = new SaveSelectDialog(gameFrame, saveManager, gameManager, true);
        dialog.setVisible(true);
        int slot = dialog.getSelectedSlot();
        if (slot != -1) {
            if (gameManager.saveGame(slot)) {
                JOptionPane.showMessageDialog(this, "保存成功！");
            } else {
                JOptionPane.showMessageDialog(this, "保存失败！");
            }
        }
    }

    private void loadGame() {
        if (!userManager.isOnline()) {
            JOptionPane.showMessageDialog(this, "请先登录！");
            return;
        }
        SaveSelectDialog dialog = new SaveSelectDialog(gameFrame, saveManager, gameManager, false);
        dialog.setVisible(true);
        int slot = dialog.getSelectedSlot();
        if (slot != -1) {
            if (gameManager.continueGame(slot)) {
                JOptionPane.showMessageDialog(this, "加载成功！");
                dispose();
                gameFrame.showGamePanel();
            } else {
                JOptionPane.showMessageDialog(this, "存档无效！");
            }
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "确定要登出吗？", "登出", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            userManager.logout();
            gameFrame.showLoginPanel();
        }
    }

    private void exitToMainMenu() {
        int confirm = JOptionPane.showConfirmDialog(this, "返回主菜单？当前游戏进度将丢失。", "确认", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            gameFrame.exitToMainMenu();
        }
    }
    private JButton createApexButton(String text, int fontSize) {
        JButton button = new JButton(text);
        // 1. 字体设置：微软雅黑 加粗 指定字号
        button.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
        // 2. 基础样式：亮红色背景 白色文字
        button.setBackground(new Color(224, 32, 64));
        button.setForeground(Color.WHITE);
        // 3. 内边距：统一按钮大小
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        // 4. 去掉默认的焦点虚线框
        button.setFocusPainted(false);
        // 5. 鼠标悬停变色效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 60, 90)); // 悬停变亮红色
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(224, 32, 64)); // 离开恢复原色
            }
        });
        return button;
    }
}