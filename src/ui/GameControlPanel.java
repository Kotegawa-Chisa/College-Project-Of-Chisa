package ui;

import Game.Manager;
import SaveAndLoad.SaveManager;
import User.UserManager;
import model.Position;

import javax.swing.*;
import java.awt.*;

public class GameControlPanel extends JPanel {
    private GameFrame gameFrame;
    private Manager gameManager;
    private UserManager userManager;
    private SaveManager saveManager;
    private JButton nextLevelButton;
    private BoardPanel boardPanel;
    private JButton hintButton;
    public GameControlPanel(GameFrame gameFrame, Manager gameManager, UserManager userManager, SaveManager saveManager,
                            BoardPanel boardPanel,int offSetX, int offSetY, int width, int height) {
        this.gameFrame = gameFrame;
        this.gameManager = gameManager;
        this.userManager = userManager;
        this.saveManager = saveManager;
        this.boardPanel = boardPanel;
        setBounds(offSetX, offSetY, width, height);
        setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton rankingButton = createGameButton("排行榜");
        nextLevelButton = createGameButton("下一关");
        JButton settingsButton = createGameButton("设置");
        hintButton = createGameButton("提示");

        nextLevelButton.addActionListener(e -> gameManager.nextLevel());
        settingsButton.addActionListener(e -> new SettingsDialog(gameFrame, gameManager, saveManager, userManager).setVisible(true));
        rankingButton.addActionListener(e -> new RankingDialog(gameFrame, userManager).setVisible(true));
        hintButton.addActionListener(e -> {
            if (gameManager.getGameState() == Manager.stateRUNNING) {
                Position[] pair = gameManager.getHintPair();
                if (pair != null) {
                    boardPanel.showHint(pair[0], pair[1]);
                } else {
                    JOptionPane.showMessageDialog(this, "当前没有可消除的对！");
                }
            } else {
                JOptionPane.showMessageDialog(this, "游戏未运行，无法使用提示！");
            }
        });

        add(nextLevelButton);
        add(hintButton);
        add(settingsButton);
        add(rankingButton);

        nextLevelButton.setVisible(false);

    }
    public void setLevelMode(boolean isLevelMode) {
        nextLevelButton.setVisible(isLevelMode);
        revalidate();
        repaint();
    }

    private JButton createGameButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 24));
        button.setBackground(new Color(224, 32, 64));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 55));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 60, 90));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(224, 32, 64));
            }
        });
        return button;
    }
}