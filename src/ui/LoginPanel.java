package ui;

import User.UserManager;
import javax.swing.*;
import java.awt.*;
import java.io.File;
//此类用于登陆界面交互与展示
public class LoginPanel extends JPanel {
    private GameFrame gameFrame;
    private UserManager userManager;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Image bannerImage;

    public LoginPanel(GameFrame gameFrame, UserManager userManager ){
        this.gameFrame = gameFrame;
        this.userManager = userManager;
        this.setLayout(new GridBagLayout());
        // P5R标志性黑色背景
        this.setBackground(new Color(255, 255, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        // ==================== 1. 顶部横幅图片 ====================
        loadBannerImage();
        JLabel bannerLabel = new JLabel();
        if (bannerImage != null) {
            // 缩放图片到合适大小
            Image scaledBanner = bannerImage.getScaledInstance(700, 200, Image.SCALE_SMOOTH);
            bannerLabel.setIcon(new ImageIcon(scaledBanner));
        }
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // 跨3列
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(bannerLabel, gbc);

        // ==================== 2. 游戏标题 ====================
        JLabel titleLabel = new JLabel("连 连 看");
        // P5R标志性红色粗体大字体
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 72));
        titleLabel.setForeground(new Color(220, 20, 60, 255)); // 深红色
        gbc.gridy = 1;
        gbc.insets = new Insets(30, 15, 40, 15);
        this.add(titleLabel, gbc);

        // ==================== 3. 用户名输入 ====================
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        usernameLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.NONE;
        this.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 24));
        usernameField.setBackground(new Color(255, 255, 255));
        usernameField.setForeground(Color.BLACK);
        usernameField.setCaretColor(Color.BLACK);
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(220, 20, 60), 2));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(usernameField, gbc);

        // ==================== 4. 密码输入 ====================
        JLabel passwordLabel = new JLabel("密  码:");
        passwordLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        passwordLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        this.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 24));
        passwordField.setBackground(new Color(255, 255, 255));
        passwordField.setForeground(Color.BLACK);
        passwordField.setCaretColor(Color.BLACK);
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(220, 20, 60), 2));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(passwordField, gbc);

        // ==================== 5. 三个按钮（同一行，四等分点） ====================
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 40, 0));
        buttonPanel.setOpaque(false); // 透明背景

        // 登录按钮
        JButton loginButton = createP5RButton("登 录");
        loginButton.addActionListener(e -> handleLogin());

        // 注册按钮
        JButton registerButton = createP5RButton("注 册");
        registerButton.addActionListener(e -> handleGoToRegister());

        // 游客登录按钮
        JButton touristButton = createP5RButton("游客登录");
        touristButton.addActionListener(e -> handleTouristLogin());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(touristButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(50, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(buttonPanel, gbc);
    }

    /**
     * 创建P5R风格的按钮
     */
    private JButton createP5RButton(String text) {
        JButton button = new JButton(text);
        // 大字体加粗
        button.setFont(new Font("微软雅黑", Font.BOLD, 28));
        // P5R红色背景
        button.setBackground(new Color(220, 20, 60));
        button.setForeground(Color.WHITE);
        // 去掉默认边框
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        // 鼠标悬停效果
        button.setFocusPainted(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 20, 60));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 20, 60));
            }
        });
        return button;
    }

    /**
     * 加载顶部横幅图片
     */
    private void loadBannerImage() {
        try {
            File bannerFile = new File("resource/p5r_banner.png");
            if (bannerFile.exists()) {
                ImageIcon icon = new ImageIcon(bannerFile.getPath());
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    bannerImage = icon.getImage();
                }
            }
        } catch (Exception e) {
            System.err.println("横幅图片加载失败：" + e.getMessage());
        }
    }

    private void handleLogin(){
        String username=usernameField.getText().trim();
        String password=new String(passwordField.getPassword()).trim();
        if(username.isEmpty()||password.isEmpty()){
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空！", "输入错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (userManager.login(username, password)) {
            JOptionPane.showMessageDialog(this, "登录成功！欢迎回来，" + username, "成功", JOptionPane.INFORMATION_MESSAGE);
            usernameField.setText("");
            passwordField.setText("");
            gameFrame.showMainMenu();
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码错误！", "登录失败", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void handleGoToRegister() {
        usernameField.setText("");
        passwordField.setText("");
        gameFrame.showRegisterPanel();
    }

    private void handleTouristLogin() {
        userManager.touristLogin();
        JOptionPane.showMessageDialog(this, "游客登录成功！您的进度将不会被保存。", "提示", JOptionPane.INFORMATION_MESSAGE);
        usernameField.setText("");
        passwordField.setText("");
        gameFrame.showMainMenu();
    }
}
