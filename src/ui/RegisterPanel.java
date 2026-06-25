package ui;

import User.UserManager;
import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private GameFrame gameFrame;
    private UserManager userManager;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public RegisterPanel(GameFrame gameFrame, UserManager userManager) {
        this.gameFrame = gameFrame;
        this.userManager = userManager;
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(255, 255, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        // 标题
        JLabel titleLabel = new JLabel("用户注册");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 64));
        titleLabel.setForeground(new Color(220, 20, 60));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(50, 15, 60, 15);
        this.add(titleLabel, gbc);

        // 用户名
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        usernameLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 15, 10, 15);
        this.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 24));
        usernameField.setBackground(new Color(255, 255, 255));
        usernameField.setForeground(Color.BLACK);
        usernameField.setCaretColor(Color.BLACK);
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(220, 20, 60), 2));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(usernameField, gbc);

        // 密码
        JLabel passwordLabel = new JLabel("密  码:");
        passwordLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        passwordLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        this.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 24));
        passwordField.setBackground(new Color(255, 255, 255));
        passwordField.setForeground(Color.BLACK);
        passwordField.setCaretColor(Color.BLACK);
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(220, 20, 60), 2));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(passwordField, gbc);

        // 确认密码
        JLabel confirmPasswordLabel = new JLabel("确认密码:");
        confirmPasswordLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        confirmPasswordLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        this.add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("微软雅黑", Font.PLAIN, 24));
        confirmPasswordField.setBackground(new Color(255, 255, 255));
        confirmPasswordField.setForeground(Color.BLACK);
        confirmPasswordField.setCaretColor(Color.BLACK);
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(new Color(220, 20, 60), 2));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(confirmPasswordField, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        buttonPanel.setOpaque(false);

        JButton registerButton = createP5RButton("注 册");
        registerButton.addActionListener(e -> handleRegister());

        JButton backButton = createP5RButton("返回登录");
        backButton.addActionListener(e -> handleGoToLogin());

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(50, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(buttonPanel, gbc);
    }

    private JButton createP5RButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 28));
        button.setBackground(new Color(220, 20, 60));
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setFocusPainted(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 50, 80));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 20, 60));
            }
        });
        return button;
    }

    private void handleRegister () {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "所有字段都不能为空！", "输入错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "两次输入的密码不一致！", "密码错误", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            confirmPasswordField.setText("");
            return;
        }
        if (userManager.register(username, password)) {
            JOptionPane.showMessageDialog(this, "注册成功！请使用新账号登录。", "成功", JOptionPane.INFORMATION_MESSAGE);
            usernameField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            gameFrame.showLoginPanel();
        } else {
            JOptionPane.showMessageDialog(this, "用户名已存在！请换一个用户名。", "注册失败", JOptionPane.ERROR_MESSAGE);
            usernameField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
        }
    }

    private void handleGoToLogin() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        gameFrame.showLoginPanel();
    }
}