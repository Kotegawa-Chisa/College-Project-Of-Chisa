package ui;
import User.User;
import User.UserManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
//本类负责分数排行榜
public class RankingDialog extends JDialog {
    private UserManager userManager;
    private JTable rankingTable;
    private DefaultTableModel tableModel;
//此处的JDialog表示模态对话框，可以用来弹出一个对话框
    public RankingDialog(Frame owner, UserManager userManager) {
        super(owner, "游戏排行榜", true); // 模态对话框（无法点击其他窗口）
        this.userManager = userManager;
        setSize(400, 500);
        setLocationRelativeTo(owner);//表示相对主窗口居中展示
        initComponents();//初始化
        loadRankingData();//从排行榜中加载
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // 创建表格模型（列名）
        String[] columnNames = {"排名", "用户名", "最高分"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 禁止编辑
            }
        };//表格是不可编辑的
        rankingTable = new JTable(tableModel);
        rankingTable.setRowHeight(25);
        rankingTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        rankingTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(rankingTable);
        add(scrollPane, BorderLayout.CENTER);
        //创建一个滚动的面板，其中的表格是刚才创建的  rankingTable

        //关闭按钮
        JButton closeButton = createApexButton("关闭",14);
        closeButton.addActionListener(e -> dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadRankingData() {
        tableModel.setRowCount(0); // 清空旧数据
        List<User> rankingList = userManager.getRankingList();
        int rank = 1;
        for (User user : rankingList) {
            //防止负分
            if (user.getHighestSCORE() >= 0) {
                tableModel.addRow(new Object[]{rank++, user.getUserName(), user.getHighestSCORE()});
            }
        }
        // 如果没有有效数据，显示提示行
        if (tableModel.getRowCount() == 0) {
            tableModel.addRow(new Object[]{"-", "暂无数据", "-"});
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










































