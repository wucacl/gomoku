package com.oym.five.chess.user;

import javax.swing.*;
import java.awt.*;

/*   �û��������
 * ��������Ϸ,������Ϸ�ȹ���ʵ�֣�*/
public class UserController extends JPanel {
    private JLabel ipLabel = new JLabel("IP��ַ:",JLabel.LEFT);
    public JTextField ipInputted = new JTextField("localhost",8);
    public JButton connectButton = new JButton("����");
    public JButton createButton = new JButton("�ȴ���ս");
    public JButton joinButton = new JButton("������ս");
    public JButton cancelButton = new JButton("Ͷ��~");
    public JButton exitButton = new JButton("��~~");
    public UserController() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(Color.PINK);
        add(ipLabel);
        add(ipInputted);
        add(connectButton);
        add(createButton);
        add(joinButton);
        add(cancelButton);
        add(exitButton);
    }
}
