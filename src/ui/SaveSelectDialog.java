package ui;
import SaveAndLoad.SaveManager;
import Game.Manager;
import javax.swing.*;
import java.awt.*;
import java.util.List;
public class SaveSelectDialog extends JDialog{
    private int selectedSlot=-1;
    private SaveManager saveManager;
    private Manager gameManager;

    public SaveSelectDialog(Frame owner, SaveManager saveManager, Manager gameManager, boolean isSaveMode){
        super(owner,isSaveMode? "选择存档位置":"选择存档加载", true);
        this.saveManager=saveManager;
        this.gameManager=gameManager;
        setSize(400,350);
        setLocationRelativeTo(owner);
        setBackground(Color.white);
        initComponents(isSaveMode);
    }

    private void initComponents(boolean isSaveMode){
        JPanel mainpanel = new JPanel(new BorderLayout(10,10));
        mainpanel.setBackground(Color.white);
        mainpanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        //标题
        JLabel titleLabel = new JLabel(isSaveMode?"选择要保存到的位置":"选择要加载的存档");
        titleLabel.setFont(new Font("微软雅黑",Font.BOLD,20));
        titleLabel.setForeground(new Color(224,32,64));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainpanel.add(titleLabel,BorderLayout.NORTH);

        JPanel saveListPanel = new JPanel(new GridLayout(3,1,10,10));
        saveListPanel.setBackground(Color.white);
        List<SaveManager.SaveInfo> saveList=saveManager.getSaveList();

        for(SaveManager.SaveInfo info:saveList){
            JButton saveButton = new JButton();
            saveButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
            saveButton.setBackground(new Color(224, 32, 64));
            saveButton.setForeground(Color.WHITE);
            saveButton.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));
            saveButton.setFocusPainted(false);
            if(info.saveTime.equals("空存档")){
                saveButton.setText("存档" + info.slot + "：空");
            }else{
                saveButton.setText("存档" + info.slot + " | " + info.saveTime + " | 分数：" + info.score);
            }

            saveButton.addActionListener(e -> {
                selectedSlot = info.slot;
                dispose();
            });

            saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    saveButton.setBackground(new Color(255, 60, 90));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    saveButton.setBackground(new Color(224, 32, 64));
                }
            });

            saveListPanel.add(saveButton);
        }
        mainpanel.add(saveListPanel, BorderLayout.CENTER);

        // 取消按钮
        JButton cancelButton = new JButton("取消");
        cancelButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(cancelButton);
        mainpanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainpanel);
    }
    public int getSelectedSlot(){
        return selectedSlot;
    }
}
