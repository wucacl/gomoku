package com.oym.five.chess.fivechess;

import java.io.IOException;
import java.util.StringTokenizer;

//���̶��߳�
public class ChessThread extends Thread {

    // ��ǰ�̵߳����� 
    private ChessBoard thisBoard;

    public ChessThread(ChessBoard thisBoard) {
        this.thisBoard = thisBoard;
    }

    //����ȡ�õ���Ϣ
    public void dealWithMsg(String msgReceived) {
        // �յ�����ϢΪ����
        if (msgReceived.startsWith("/chess")) {
            StringTokenizer userMsgToken = new StringTokenizer(msgReceived, " ");
            // ��ʾ������Ϣ�����顢0����Ϊ��x���ꣻ1����λ��y���ꣻ2����λ��������ɫ 
            String[] chessInfo = { "-1", "-1", "0" };
            // ��־λ
            int i = 0;
            String chessInfoToken;
            while (userMsgToken.hasMoreTokens()) {
                chessInfoToken = userMsgToken.nextToken(" ");
                if (i >= 1 && i <= 3) {
                    chessInfo[i - 1] = chessInfoToken;
                }
                i++;
            }
            thisBoard.paintNetChessPoint(Integer.parseInt(chessInfo[0]), Integer
                    .parseInt(chessInfo[1]), Integer.parseInt(chessInfo[2]));
            // �յ�����ϢΪ����
        } else if (msgReceived.startsWith("/yourname ")) {
            thisBoard.chessSelfName = msgReceived.substring(10);
            // �յ���Ϊ������Ϣ
        } else if (msgReceived.equals("/error")) {
            thisBoard.statusText.setText("�û������ڣ������¼���!!!");
        }
    }

    //������Ϣ
    public void sendMessage(String sentMessage) {
        try {
            thisBoard.outputData.writeUTF(sentMessage);
        } catch (Exception ea) {
            ea.printStackTrace();;
        }
    }

    @Override
    public void run() {
        String msgReceived = "";
        try {
            // �ȴ���Ϣ����
            while (true) {
                msgReceived = thisBoard.inputData.readUTF();
                dealWithMsg(msgReceived);
            }
        } catch (IOException es){}
    }

}
