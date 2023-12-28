package com.oym.five.chess.client;

import com.oym.five.chess.fivechess.ChessBoard;
import com.oym.five.chess.user.UserChat;
import com.oym.five.chess.user.UserController;
import com.oym.five.chess.user.UserInput;
import com.oym.five.chess.user.UserList;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static java.awt.Color.*;

// �ͻ���

public class ClientChess extends Frame implements ActionListener, KeyListener {

    // �ͻ����׽ӿ�
    public Socket clientSocket;
    // ����������
    public DataInputStream inputStream;
    // ���������
    public DataOutputStream outputStream;
    // �û���
    public String chessClientName = null;
    // ������ַ 
    public String host = null;
    // �����˿�
    public int port = 1234;
    //�Ƿ�������
    public boolean isOnChat = false;
    //�Ƿ�������
    public boolean isOnChess = false;
    //��Ϸ�Ƿ������
    public boolean isGameConnected = false;
    // �Ƿ�Ϊ��Ϸ������
    public boolean isCreator = false;
    //�Ƿ�Ϊ��Ϸ������
    public boolean isParticipant = false;
    //�û��б���
    public UserList userListPad = new UserList();
    //�û�������
    protected UserChat userChatPad = new UserChat();
    //�û�������
    public UserController userControlPad = new UserController();
    //�û�������
    protected UserInput userInputPad = new UserInput();
    //������
    ChessBoard chessBoard = new ChessBoard();
    // �����
    private Panel southPanel = new Panel();
    private Panel centerPanel = new Panel();
    private Panel eastPanel = new Panel();
    // ���췽������������
    public ClientChess() {
        super("������");
        setLayout(new BorderLayout());
        host = userControlPad.ipInputted.getText();
        
        eastPanel.setLayout(new BorderLayout());
        eastPanel.add(userListPad, BorderLayout.NORTH);
        eastPanel.add(userChatPad, BorderLayout.CENTER);
        eastPanel.setBackground(new Color(238, 154, 73));

        userInputPad.contentInputted.addKeyListener(this);

        chessBoard.host =  (userControlPad.ipInputted.getText());
        centerPanel.add(chessBoard, BorderLayout.CENTER);
        centerPanel.add(userInputPad, BorderLayout.SOUTH);
        centerPanel.setBackground(new Color(238, 154, 73));
        userControlPad.connectButton.addActionListener(this);
        userControlPad.createButton.addActionListener(this);
        userControlPad.joinButton.addActionListener(this);
        userControlPad.cancelButton.addActionListener(this);
        userControlPad.exitButton.addActionListener(this);
        userControlPad.createButton.setEnabled(false);
        userControlPad.joinButton.setEnabled(false);
        userControlPad.cancelButton.setEnabled(false);

        southPanel.add(userControlPad, BorderLayout.CENTER);
        southPanel.setBackground(PINK);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // ������
                if (isOnChat) {
                    // �رտͻ����׽ӿ�
                    try {
                        clientSocket.close();
                    }
                    catch (Exception ed){}
                }

                if (isOnChess || isGameConnected) {
                    // ������ 
                    try {
                        // �ر�����˿� 
                        chessBoard.chessSocket.close();
                    }
                    catch (Exception ee){}
                }
                System.exit(0);
            }
        });

        add(eastPanel, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        pack();
        setSize(1000, 700);
        setVisible(true);
        setResizable(false);
        this.validate();
    }

    // ��ָ����IP��ַ�Ͷ˿����ӵ�������
    public boolean connectToServer(String serverIP, int serverPort) throws Exception {
        try {
            // �����ͻ����׽ӿ� 
            clientSocket = new Socket(serverIP, serverPort);
            // ���������� 
            inputStream = new DataInputStream(clientSocket.getInputStream());
            // ��������� 
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
            // �����ͻ����߳� 
            ClientThread clientThread = new ClientThread(this);
            // �����̣߳��ȴ�������Ϣ 
            clientThread.start();
            isOnChat = true;
            return true;
        } catch (IOException ex) {
            userChatPad.chatTextArea.setText("Sorry,�޷�����!!!\n");
        }
        return false;
    }

    // �ͻ����¼�����
    @Override
    public void actionPerformed(ActionEvent e) {

        // ���ӵ�������ť�����¼�
        if (e.getSource() == userControlPad.connectButton) {
            // ȡ��������ַ
            host = chessBoard.host = userControlPad.ipInputted.getText();
            try {
                // �ɹ����ӵ�����ʱ�����ÿͻ�����Ӧ�Ľ���״̬
                if (connectToServer(host, port)) {
                    userChatPad.chatTextArea.setText("");
                    userControlPad.connectButton.setEnabled(false);
                    userControlPad.createButton.setEnabled(true);
                    userControlPad.joinButton.setEnabled(true);
                    chessBoard.statusText.setText("���ӳɹ�����ȴ�!!!");
                }
            } catch (Exception ei) {
                userChatPad.chatTextArea.setText("Sorry,��������!!!\n");
            }
        }

        // �뿪��Ϸ��ť�����¼�
        if (e.getSource() == userControlPad.exitButton) {
            // ���û���������״̬��
            if (isOnChat) {
                try {
                    // �رտͻ����׽ӿ� 
                    clientSocket.close();
                }
                catch (Exception ed){}
            }

            // ���û�������Ϸ״̬�� 
            if (isOnChess || isGameConnected) {
                try {
                    // �ر���Ϸ�˿� 
                    chessBoard.chessSocket.close();
                }
                catch (Exception ee){}
            }
            System.exit(0);
        }

        // ������Ϸ��ť�����¼�
        if (e.getSource() == userControlPad.joinButton) {
            // ȡ��Ҫ�������Ϸ
            String selectedUser = userListPad.userList.getSelectedItem();
            // ��δѡ��Ҫ������û�����ѡ�е��û��Ѿ�����Ϸ���������ʾ��Ϣ 
            if (selectedUser == null || selectedUser.startsWith("[inchess]") ||
                    selectedUser.equals(chessClientName)) {
                chessBoard.statusText.setText("����ѡ��һ���û�!");
            } else {
                // ִ�м�����Ϸ�Ĳ��� 
                try {
                    // ����Ϸ�׽ӿ�δ����
                    if (!isGameConnected) {
                        // �����ӵ������ɹ�
                        if (chessBoard.connectServer(chessBoard.host, chessBoard.port)) {
                            isGameConnected = true;
                            isOnChess = true;
                            isParticipant = true;
                            userControlPad.createButton.setEnabled(false);
                            userControlPad.joinButton.setEnabled(false);
                            userControlPad.cancelButton.setEnabled(true);
                            chessBoard.chessThread.sendMessage("/joingame "
                                    + userListPad.userList.getSelectedItem() + " "
                                    + chessClientName);
                        }
                    } else {
                        // ����Ϸ�˿������� 
                        isOnChess = true;
                        isParticipant = true;
                        userControlPad.createButton.setEnabled(false);
                        userControlPad.joinButton.setEnabled(false);
                        userControlPad.cancelButton.setEnabled(true);
                        chessBoard.chessThread.sendMessage("/joingame "
                                + userListPad.userList.getSelectedItem() + " "
                                + chessClientName);
                    }
                } catch (Exception ee) {
                    isGameConnected = false;
                    isOnChess = false;
                    isParticipant = false;
                    userControlPad.createButton.setEnabled(true);
                    userControlPad.joinButton.setEnabled(true);
                    userControlPad.cancelButton.setEnabled(false);
                    userChatPad.chatTextArea.setText("��������: \n" + ee);
                }
            }
        }

        // ������Ϸ��ť�����¼� 
        if (e.getSource() == userControlPad.createButton) {
            try {
                // ����Ϸ�˿�δ����
                if (!isGameConnected) {
                    if (chessBoard.connectServer(chessBoard.host, chessBoard.port)) {
                        // �����ӵ������ɹ� 
                        isGameConnected = true;
                        isOnChess = true;
                        isCreator = true;
                        userControlPad.createButton.setEnabled(false);
                        userControlPad.joinButton.setEnabled(false);
                        userControlPad.cancelButton.setEnabled(true);
                        chessBoard.chessThread.sendMessage("/creatgame " + "[inchess]" + chessClientName);
                    }
                } else {
                    // ����Ϸ�˿������� 
                    isOnChess = true;
                    isCreator = true;
                    userControlPad.createButton.setEnabled(false);
                    userControlPad.joinButton.setEnabled(false);
                    userControlPad.cancelButton.setEnabled(true);
                    chessBoard.chessThread.sendMessage("/creatgame "
                            + "[inchess]" + chessClientName);
                }
            } catch (Exception ec) {
                isGameConnected = false;
                isOnChess = false;
                isCreator = false;
                userControlPad.createButton.setEnabled(true);
                userControlPad.joinButton.setEnabled(true);
                userControlPad.cancelButton.setEnabled(false);
                ec.printStackTrace();
                userChatPad.chatTextArea.setText("Sorry,��������: \n" + ec);
            }
        }

        // �˳���Ϸ��ť�����¼� 
        if (e.getSource() == userControlPad.cancelButton) {
            // ��Ϸ��
            if (isOnChess) {
                chessBoard.chessThread.sendMessage("/giveup " + chessClientName);
                chessBoard.setVicStatus(-1 * chessBoard.chessColor);
                userControlPad.createButton.setEnabled(true);
                userControlPad.joinButton.setEnabled(true);
                userControlPad.cancelButton.setEnabled(false);
                chessBoard.statusText.setText("��ѡ�񴴽�����������Ϸ!!!");
            } if (!isOnChess) {
                // ����Ϸ�� 
                userControlPad.createButton.setEnabled(true);
                userControlPad.joinButton.setEnabled(true);
                userControlPad.cancelButton.setEnabled(false);
                chessBoard.statusText.setText("��ѡ�񴴽�����������Ϸ!!!");
            }
            isParticipant = isCreator = false;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        TextField inputwords = (TextField) e.getSource();
        // ����س������¼�
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            // �������˷���Ϣ 
            if (userInputPad.userChoice.getSelectedItem().equals("�����û�")) {
                try {
                    // ������Ϣ 
                    outputStream.writeUTF(inputwords.getText());
                    inputwords.setText("");
                } catch (Exception ea) {
                    userChatPad.chatTextArea.setText("Sorry,�������ӵ�������!\n");
                    userListPad.userList.removeAll();
                    userInputPad.userChoice.removeAll();
                    inputwords.setText("");
                    userControlPad.connectButton.setEnabled(true);
                }
            } else {
                // ��ָ���˷���Ϣ 
                try {
                    outputStream.writeUTF("/" + userInputPad.userChoice.getSelectedItem()
                            + " " + inputwords.getText());
                    inputwords.setText("");
                } catch (Exception ea) {
                    userChatPad.chatTextArea.setText("Sorry,�������ӵ�������!\n");
                    userListPad.userList.removeAll();
                    userInputPad.userChoice.removeAll();
                    inputwords.setText("");
                    userControlPad.connectButton.setEnabled(true);
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        new ClientChess();
    }
}
