package com.oym.five.chess.client;

import java.io.IOException;
import java.util.StringTokenizer;

//�ͻ��˶��߳�

public class ClientThread extends Thread {
    private ClientChess clientChess;

    public ClientThread(ClientChess clientChess) {
        this.clientChess = clientChess;
    }

    public void dealWithMsg(String msgReceived) {
        // ��ȡ�õ���ϢΪ�û��б�
        if (msgReceived.startsWith("/userlist ")) {  
            StringTokenizer userToken = new StringTokenizer(msgReceived, " ");
            int userNumber = 0;
            // ��տͻ����û��б� 
            clientChess.userListPad.userList.removeAll();
            // ��տͻ����û������� 
            clientChess.userInputPad.userChoice.removeAll();
            // ���ͻ����û����������һ��ѡ�� 
            clientChess.userInputPad.userChoice.addItem("�����û�");
            // ���յ����û���Ϣ�б��д�������ʱ
            while (userToken.hasMoreTokens()) {  
                // ȡ���û���Ϣ
                String user = userToken.nextToken(" ");  
                // �û���Ϣ��Чʱ 
                if (userNumber > 0 && !user.startsWith("[inchess]")) { 
                    // ���û���Ϣ��ӵ��û��б���
                    clientChess.userListPad.userList.add(user); 
                    // ���û���Ϣ��ӵ��û��������� 
                    clientChess.userInputPad.userChoice.addItem(user); 
                }
                userNumber++;
            }
            // ������Ĭ��ѡ�������� 
            clientChess.userInputPad.userChoice.setSelectedIndex(0);
            // �յ�����ϢΪ�û�����ʱ
        } else if (msgReceived.startsWith("/yourname ")) {
            // ȡ���û����� 
            clientChess.chessClientName = msgReceived.substring(10); 
            // ���ó���ı��� 
            clientChess.setTitle("������ " + "�û���:" + clientChess.chessClientName);
            // �յ�����ϢΪ�ܾ��û�ʱ
        } else if (msgReceived.equals("/reject")) {  
            try {
                clientChess.chessBoard.statusText.setText("���ܼ�����Ϸ!");
                clientChess.userControlPad.cancelButton.setEnabled(false);
                clientChess.userControlPad.joinButton.setEnabled(true);
                clientChess.userControlPad.createButton.setEnabled(true);
            } catch (Exception ef) {
                clientChess.userChatPad.chatTextArea
                        .setText("Cannot close!");
            }
            clientChess.userControlPad.joinButton.setEnabled(true);
            // �յ���ϢΪ��Ϸ�еĵȴ�ʱ
        } else if (msgReceived.startsWith("/peer ")) {  
            clientChess.chessBoard.chessPeerName = msgReceived.substring(6);
            // ���û�Ϊ��Ϸ������
            if (clientChess.isCreator) {  
                // �趨��Ϊ��������
                clientChess.chessBoard.chessColor = 1;  
                clientChess.chessBoard.isMouseEnabled = true;
                clientChess.chessBoard.statusText.setText("�ڷ���...");
                // ���û�Ϊ��Ϸ������ 
            } else if (clientChess.isParticipant) { 
                // �趨��Ϊ�������
                clientChess.chessBoard.chessColor = -1; 
                clientChess.chessBoard.statusText.setText("��Ϸ��ʼ���ȴ�����...");
            }
            // �յ���ϢΪʤ����Ϣ 
        } else if (msgReceived.equals("/youwin")) { 
            clientChess.isOnChess = false;
            clientChess.chessBoard.setVicStatus(clientChess.chessBoard.chessColor);
            clientChess.chessBoard.statusText.setText("�������ˣ�����");
            clientChess.chessBoard.isMouseEnabled = false;
            // �յ���ϢΪ�ɹ�������Ϸ
        } else if (msgReceived.equals("/OK")) {  
            clientChess.chessBoard.statusText.setText("��Ϸ�Ѵ������ȴ�������...");
            // �յ���Ϣ����
        } else if (msgReceived.equals("/error")) {  
            clientChess.userChatPad.chatTextArea.append("���ʹ����˳�����...\n");
        } else {
            clientChess.userChatPad.chatTextArea.append(msgReceived + "\n");
            clientChess.userChatPad.chatTextArea.setCaretPosition(
                    clientChess.userChatPad.chatTextArea.getText().length());
        }
    }

    @Override
    public void run() {
        String message = "";
        try {
            while (true) {
                // �ȴ�������Ϣ������wait״̬ 
                message = clientChess.inputStream.readUTF();
                dealWithMsg(message);
            }
        }
        catch (IOException es){}
    }
}
