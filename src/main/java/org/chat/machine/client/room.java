package org.chat.machine.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Random;

import static org.chat.machine.client.Main.*;

public class room {
    protected static final Logger log = LogManager.getLogger(room.class);
    public static boolean HaveRoom = false;
    private static String name;
    private static JList chat;
    private static JFrame frame;
    private JPanel jp;
    private JScrollPane ListJsp;
    private JList ChatList;
    private JTextField ChatTextField;
    private JButton 发送Button;
    private JLabel NameLabel;

    public room() {
        HaveRoom = true;
        chat = ChatList;
        NameLabel.setText(name);

        chat.setListData(RequestServer.send("getroomdata:" + name).split(","));
        new update().start();

        发送Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = "[" + info.UserName + "] " + ChatTextField.getText();
                ChatTextField.setText("");

                String RealText;
                if (set.chat.SendReplace) {
                    RealText = text
                        .replace("{timestamp}", String.valueOf(System.currentTimeMillis()))
                        .replace("{time}", new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()))
                        .replace("{date}", new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()))
                        .replace("{username}", info.UserName)
                        .replace("{useruuid}", info.UserUUID)
                        .replace("{random}", String.valueOf(new Random().nextInt(100000000)))
                        .replace(",", "{%&%**comma**%&%}")
                        .replace(":", "{%&%**colon**%&%}");
//                            .replace("{time}", )
//                            .replace("{time}", )
//                            .replace("{time}", )
//                            .replace("{time}", )
//                            .replace("{time}", )
//                            .replace("{time}", );
                } else {
                    RealText = text
                        .replace(",", "{%&%**comma**%&%}")
                        .replace(":", "{%&%**colon**%&%}");
                }

                if (RequestServer.send("addroomdata:" + name + "," + RealText).equals("true")) {
                    log.info("发送成功: " + name + "," + text);
                } else {
                    log.info("发送失败: " + name + "," + text);
                    JOptionPane.showMessageDialog(null, "发送失败");
                }
            }
        });

        ChatTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (ChatTextField.getText().substring(ChatTextField.getText().length() - 1).equals("{")) {
                    JFrame tab = new JFrame();
                    tab.setSize(100, 60);

                    JScrollPane TabListJsp = new JScrollPane();
                    JList TabList = new JList();
                    TabList.setListData(new String[]{"{%date}", "{%time}", "{%username}", "{%random}"});
                    TabListJsp.setViewportView(TabList);
                    tab.setContentPane(TabListJsp);

                    TabList.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            ChatTextField.setText(ChatTextField.getText() + ((String) TabList.getSelectedValue()).substring(1));
                            tab.dispose();
                        }
                    });

                    ChatTextField.getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            ChatTextField.getDocument().removeDocumentListener(this);
                            tab.dispose();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            ChatTextField.getDocument().removeDocumentListener(this);
                            tab.dispose();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            ChatTextField.getDocument().removeDocumentListener(this);
                            tab.dispose();
                        }
                    });

                    tab.setLocation(new Point(ChatTextField.getLocationOnScreen().x + ChatTextField.getWidth(), ChatTextField.getLocationOnScreen().y));
                    tab.setAlwaysOnTop(true);
                    tab.setUndecorated(true);
                    tab.setVisible(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                PopupMenu pop = new PopupMenu();
                pop.add("aaa");
                pop.setEnabled(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                PopupMenu pop = new PopupMenu();
                pop.add("aaa");
                pop.setEnabled(true);
            }
        });
    }

    public static void main(String name) {
        if (HaveRoom) {
            update.stop = true;
        }

        HaveRoom = true;
        org.chat.machine.client.room.name = name;

        frame = new JFrame("聊天器 - 房间: " + name);
        frame.setContentPane(new room().jp);
        frame.setSize(600, 400);

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                tray.message(name, "窗口已隐藏");
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

    static class update extends Thread {
        protected static final Logger log = LogManager.getLogger(update.class);
        private static String[] OldData;
        public static boolean stop;

        @Override
        public void run() {
            OldData = RequestServer.send("getroomdata:" + name).split(",");

            while (true) {
                if (stop) {
                    log.info("房间: " + name + " 更新停止");
                    stop = false;
                    break;
                }

                try {
                    sleep(set.chat.UpdateInterval);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                log.info("列表更新");
                String[] NowData = RequestServer.send("getroomdata:" + name).split(",");

                String[] LookData = new String[NowData.length];
                int ldi = 0;
                for (String str : NowData) {
                    LookData[ldi] = str
                        .replace("{%&%**comma**%&%}", ",")
                        .replace("{%&%**colon**%&%}", ":");

                    if (set.chat.ReceiveReplace) {
                        LookData[ldi] = LookData[ldi]
                            .replace("{%date}", new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()))
                            .replace("{%time}", new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()))
                            .replace("{%username}", info.UserName)
                            .replace("{%random}", String.valueOf(new Random().nextInt(100000000)));
                    }

                    ldi++;
                }
                chat.setListData(LookData);

                if (NowData.length != OldData.length) {
                    PlayMessageMusic pmm = new PlayMessageMusic();
                    pmm.start();

                    tray.message(
                        name,
                        NowData[NowData.length - 1]
                            .replace("{%&%**comma**%&%}", ",")
                            .replace("{%&%**colon**%&%}", ":")
                    );
                }

                OldData = NowData;
            }
        }

        static class PlayMessageMusic extends Thread {
            protected static final Logger log = LogManager.getLogger(PlayMessageMusic.class);

            @Override
            public void run() {
                log.info("播放MessageMusic");

                try {
                    URL cb = resource.music.message.toURL();
                    AudioClip aau = Applet.newAudioClip(cb);
                    aau.play();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void visible() {
        frame.setVisible(true);
    }
}
