package org.chat.machine.client.RoomManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chat.machine.client.RequestServer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddRoom {
    protected static final Logger log = LogManager.getLogger(AddRoom.class);
    private static JFrame frame;
    private JPanel jp;
    private JLabel 创建房间Label;
    private JTextField RoomNameTextField;
    private JButton 创建Button;

    public AddRoom() {
        创建Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (RequestServer.send("addroom:" + RoomNameTextField.getText()).equals("true")) {
                    log.info("添加成功");
                    JOptionPane.showMessageDialog(null, "添加成功");
                    frame.dispose();
                } else {
                    log.info("添加失败");
                    JOptionPane.showMessageDialog(null, "添加失败");
                }
            }
        });
    }

    public static void main() {
        frame = new JFrame("聊天器 - 房间: 创建");
        frame.setContentPane(new AddRoom().jp);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
