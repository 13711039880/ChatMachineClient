package org.chat.machine.client.settings;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chat.machine.client.ConfigOperation;
import org.chat.machine.client.close;
import org.chat.machine.client.timing;
import org.util.file.DeleteFile;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import static org.chat.machine.client.Main.*;
import static org.chat.machine.client.Main.resource.look.html.*;
import static org.chat.machine.client.Main.set.*;
import static org.chat.machine.client.Main.set.AutoLogin.*;

public class settings {
    protected static final Logger log = LogManager.getLogger(settings.class);
    private static JFrame frame;
    public static boolean open = true;
    private JPanel jp;
    private JButton 关闭Button;
    private JScrollPane ListJsp;
    private JPanel ListJp;
    private JPanel UISetJp;
    private JLabel UILabel;
    private JLabel 背景颜色Label;
    private JComboBox 背景颜色ComboBox;
    private JComboBox 按钮颜色ComboBox;
    private JLabel 按钮颜色Label;
    private JLabel 设置Label;
    private JPanel AutoLoginSetJp;
    private JCheckBox 启用自动登录CheckBox;
    private JLabel 自动登录Label;
    private JLabel 用户名Label;
    private JLabel 密码Label;
    private JTextField 用户名TextField;
    private JTextField 密码TextField;
    private JPanel MoreSetJp;
    private JButton 最终用户许可协议Button;
    private JButton 隐私政策Button;
    private JLabel 协议Label;
    private JButton 清除缓存Button;
    private JButton 关于Button;
    private JButton 日志Button;
    private JButton 退出Button;
    private JPanel ContentSetJp;
    private JLabel 聊天Label;
    private JCheckBox 发送替换CheckBox;
    private JCheckBox 接收替换CheckBox;
    private JComboBox 聊天消息更新间隔ComboBox;
    private JLabel 聊天消息更新间隔Label;
    private JButton 发送替换帮助Button;
    private JButton 接收替换帮助Button;
    private JCheckBox 简洁启动界面CheckBox;
    private JButton 重载设置Button;
    private JPanel LoadSetJp;
    private JLabel 启动Label;
    private JCheckBox 简洁启动_快速启动CheckBox;
    private JCheckBox 简洁启动_立体LOGOCheckBox;

    public settings() {
        启用自动登录CheckBox.setSelected(enable);
        用户名TextField.setText(username);
        密码TextField.setText(password);
        用户名TextField.setEnabled(启用自动登录CheckBox.isSelected());
        密码TextField.setEnabled(启用自动登录CheckBox.isSelected());
        背景颜色ComboBox.setSelectedItem(ui.background);
        按钮颜色ComboBox.setSelectedItem(ui.buttoncolor);
        发送替换CheckBox.setSelected(chat.SendReplace);
        接收替换CheckBox.setSelected(chat.ReceiveReplace);
        简洁启动界面CheckBox.setSelected(load.ConciseLoad);
        简洁启动_快速启动CheckBox.setSelected(load.ConciseLoadFast);
        简洁启动_立体LOGOCheckBox.setSelected(load.StereoscopicLogo);
        简洁启动_快速启动CheckBox.setEnabled(简洁启动界面CheckBox.isSelected());
        简洁启动_立体LOGOCheckBox.setEnabled(简洁启动界面CheckBox.isSelected());
        聊天消息更新间隔ComboBox.setSelectedItem(String.valueOf(chat.UpdateInterval));
        关闭Button.setBackground(ui.buttoncolor != null ? ui.buttoncolor : Color.WHITE);
        ListJsp.getVerticalScrollBar().setUnitIncrement(30);

        关闭Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "需要重启才能生效");
                frame.dispose();
            }
        });

        背景颜色ComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigOperation.SetSet("ui.background", String.valueOf(背景颜色ComboBox.getSelectedItem()));
            }
        });

        按钮颜色ComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigOperation.SetSet("ui.buttoncolor", String.valueOf(按钮颜色ComboBox.getSelectedItem()));
            }
        });

        启用自动登录CheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigOperation.SetSet("AutoLogin.enable", String.valueOf(启用自动登录CheckBox.isSelected()));
                用户名TextField.setEnabled(启用自动登录CheckBox.isSelected());
                密码TextField.setEnabled(启用自动登录CheckBox.isSelected());
            }
        });

        用户名TextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                ConfigOperation.SetSet("AutoLogin.username", 用户名TextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                ConfigOperation.SetSet("AutoLogin.username", 用户名TextField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                ConfigOperation.SetSet("AutoLogin.username", 用户名TextField.getText());
            }
        });

        密码TextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                ConfigOperation.SetSet("AutoLogin.password", 密码TextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                ConfigOperation.SetSet("AutoLogin.password", 密码TextField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                ConfigOperation.SetSet("AutoLogin.password", 密码TextField.getText());
            }
        });

        最终用户许可协议Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                org.chat.machine.client.settings.EULA.main();
            }
        });

        隐私政策Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                org.chat.machine.client.settings.PrivacyPolicy.main();
            }
        });

        清除缓存Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("[清除缓存] 清除缓存...");
                long StartTime = System.currentTimeMillis();
                清除缓存Button.setEnabled(false);
                清除缓存Button.setText("正在清除...");

                long size = FileUtils.sizeOfDirectory(new File(".\\temp"));
                log.info("[清除缓存] 大小: {}bytes", size);

                for(File f:new File(".\\temp").listFiles()){
                    if(!f.isDirectory()) {
                        log.info("[清除缓存] 删除: {}", f);
                        DeleteFile.DeleteFile(String.valueOf(f));
                    }
                }

                log.info("[清除缓存] 完成({}ms, {}bytes)", System.currentTimeMillis() - StartTime, size);
                清除缓存Button.setText("清除缓存");
                清除缓存Button.setEnabled(true);
                JOptionPane.showMessageDialog(null, "清除缓存完成");
            }
        });

        关于Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (about != null) {
                    LookHtml.main(about, "关于");
                } else {
                    LookHtml.main("资源未加载", "关于");
                }
            }
        });

        退出Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close.main();
            }
        });

        日志Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                org.chat.machine.client.settings.log.main();
            }
        });

        接收替换CheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigOperation.SetSet("chat.send.replace", String.valueOf(接收替换CheckBox.isSelected()));
            }
        });

        接收替换CheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigOperation.SetSet("chat.receive.replace", String.valueOf(接收替换CheckBox.isSelected()));
            }
        });

        聊天消息更新间隔ComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigOperation.SetSet("chat.update.interval", String.valueOf(聊天消息更新间隔ComboBox.getSelectedItem()));
            }
        });

        发送替换帮助Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LookHtml.main(SendReplaceHelp, "发送替换帮助");
            }
        });

        接收替换帮助Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LookHtml.main(ReceiveReplaceHelp, "接收替换帮助");
            }
        });

        简洁启动界面CheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                简洁启动_快速启动CheckBox.setEnabled(简洁启动界面CheckBox.isSelected());
                简洁启动_立体LOGOCheckBox.setEnabled(简洁启动界面CheckBox.isSelected());
                ConfigOperation.SetSet("load.concise.enable", String.valueOf(简洁启动界面CheckBox.isSelected()));
            }
        });

        重载设置Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("[重载] 重载设置...");
                timing timing = new timing();
                GetSet.main();
                long time = timing.stop();
                log.info("[重载] 重载设置完成({}ms)", time);
                JOptionPane.showMessageDialog(null, "重载设置完成(" + time + "ms)");
            }
        });

        简洁启动_快速启动CheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigOperation.SetSet("load.concise.fast", String.valueOf(简洁启动_快速启动CheckBox.isSelected()));
            }
        });

        简洁启动_立体LOGOCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigOperation.SetSet("load.concise.StereoscopicLogo", String.valueOf(简洁启动_立体LOGOCheckBox.isSelected()));
            }
        });
    }

    public static void main() {
        frame = new JFrame("聊天器 - 设置");
        frame.setContentPane(new settings().jp);
        frame.setSize(400, 500);

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                GetSet.main();
                JOptionPane.showMessageDialog(null, "部分设置需要重启才能生效");
                open = false;
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
