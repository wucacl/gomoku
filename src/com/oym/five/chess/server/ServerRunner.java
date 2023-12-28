package com.oym.five.chess.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

//��������
public class ServerRunner extends Frame implements ActionListener {
    JButton clearMsgButton = new JButton("���");
    JButton serverStatusButton = new JButton("״̬");
    JButton closeServerButton = new JButton("�ر�");
    Panel buttonPanel = new Panel();
    ServerMsgPanel serverMsgPanel = new ServerMsgPanel();
    ServerSocket serverSocket;//�������˵ȴ����������Կͻ��˵�����
    int clientAccessNumber = 1;

    //���ͻ����׽ӿں��������
    Hashtable clientDataHash = new Hashtable(50);//һ�ּ�-ֵ�Ե����ݽṹ��������ͨ�������ٲ��Һͷ���ֵ
    // ���ͻ����׽ӿںͿͻ�����
    Hashtable clientNameHash = new Hashtable(50);
    // ����Ϸ�����ߺ���Ϸ�����߰�
    Hashtable chessPeerHash = new Hashtable(50);

    public ServerRunner() {
        super("������Ϸ��սƽ̨����������ƽ̨");
        setBackground(Color.PINK);
        buttonPanel.setLayout(new FlowLayout());
        clearMsgButton.setSize(50, 30);
        buttonPanel.add(clearMsgButton);
        clearMsgButton.addActionListener(this);
        serverStatusButton.setSize(50, 30);
        buttonPanel.add(serverStatusButton);
        serverStatusButton.addActionListener(this);
        closeServerButton.setSize(50, 30);
        buttonPanel.add(closeServerButton);
        closeServerButton.addActionListener(this);
        add(serverMsgPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        pack();
        setVisible(true);
        setSize(600, 440);
        setResizable(false);
        validate();

        try {
            createServer(1234, serverMsgPanel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ��ָ���˿ں���崴��������
    public void createServer(int port, ServerMsgPanel serverMsgPanel) throws IOException {
        // �ͻ����׽ӿ�
        Socket clientSocket;//����һ�� socket ���󣬷���˿��Լ����ͽ������Կͻ��˵��������󣬲��������ݡ�
        // �趨��ǰ����
        this.serverMsgPanel = serverMsgPanel;
        try {
            serverSocket = new ServerSocket(port);
            serverMsgPanel.msgTextArea.setText("����������:"
                    + InetAddress.getLocalHost() + ":"
                    + serverSocket.getLocalPort() + "\n");
            while (true) {
                // �����ͻ����׽ӿڵ���Ϣ 
                clientSocket = serverSocket.accept();
                serverMsgPanel.msgTextArea.append("�������û�:" +
                        "���ҿ�" + clientAccessNumber +"\n" + clientSocket + "\n");
                // �����ͻ�������� 
                DataOutputStream outputData = new DataOutputStream(clientSocket.getOutputStream());
                // ���ͻ����׽ӿں�������� 
                clientDataHash.put(clientSocket, outputData);
                // ���ͻ����׽ӿںͿͻ����� 
                clientNameHash.put(clientSocket, ("���ҿ�" + clientAccessNumber++));
                // ���������з��������߳� 
                ServerThread thread = new ServerThread(clientSocket, clientDataHash, clientNameHash, chessPeerHash, serverMsgPanel);
                thread.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // ��շ�������Ϣ
        if (e.getSource() == clearMsgButton) {
            serverMsgPanel.msgTextArea.setText("");
        }

        // ��ʾ��������Ϣ
        if (e.getSource() == serverStatusButton) {
            try {
                serverMsgPanel.msgTextArea.append("�û���Ϣ��" + "���ҿ�"
                        + (clientAccessNumber - 1) + "\n��������Ϣ:"
                        + InetAddress.getLocalHost() + ":"
                        + serverSocket.getLocalPort() + "\n");
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new ServerRunner();
    }
    
}
