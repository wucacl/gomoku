package com.oym.five.chess.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

//���������߳�
public class ServerThread extends Thread{

    //����ͻ����׽ӿ���Ϣ
    Socket clientSocket;
    // ����ͻ��˶˿����������Ӧ��Hash
    Hashtable clientDataHash;
    //����ͻ����׽ӿںͿͻ�����Ӧ��Hash
    Hashtable clientNameHash;
    //������Ϸ�����ߺ���Ϸ�����߶�Ӧ��Hash 
    Hashtable chessPeerHash;
    
    //�жϿͻ����Ƿ�ر�
    boolean isClientClosed = false;  
    ServerMsgPanel serverMsgPanel;

    public ServerThread(Socket clientSocket, Hashtable clientDataHash, Hashtable clientNameHash, Hashtable chessPeerHash, ServerMsgPanel server) {
        this.clientSocket = clientSocket;
        this.clientDataHash = clientDataHash;
        this.clientNameHash = clientNameHash;
        this.chessPeerHash = chessPeerHash;
        this.serverMsgPanel = server;
    }

    public void dealWithMsg(String msgReceived) {
        String peerName;
        if (msgReceived.startsWith("/")) {
            // �յ�����ϢΪ�����û��б�
            if (msgReceived.equals("/list")) {  
                Feedback(getUserList());
                // �յ�����ϢΪ������Ϸ
            } else if (msgReceived.startsWith("/creatgame [inchess]")) {
                //ȡ�÷�������
                String gameCreaterName = msgReceived.substring(20);
                // ���û��˿ڷŵ��û��б��� 
                synchronized (clientNameHash) { 
                    clientNameHash.put(clientSocket, msgReceived.substring(11));
                }
                // ����������Ϊ�ȴ�״̬
                synchronized (chessPeerHash) {  
                    chessPeerHash.put(gameCreaterName, "wait");
                }
                Feedback("/yourname " + clientNameHash.get(clientSocket));
                sendGamePeerMsg(gameCreaterName, "/OK");
                sendPublicMsg(getUserList());
                // �յ�����ϢΪ������Ϸʱ
            } else if (msgReceived.startsWith("/joingame ")) {  
                StringTokenizer userTokens = new StringTokenizer(msgReceived, " ");
                String userToken;
                String gameCreatorName;
                String gamePaticipantName;
                String[] playerNames = { "0", "0" };
                int nameIndex = 0;
                while (userTokens.hasMoreTokens()) {
                    userToken = userTokens.nextToken(" ");
                    if (nameIndex >= 1 && nameIndex <= 2) {
                        playerNames[nameIndex - 1] = userToken;  
                    }
                    nameIndex++;
                }
                gameCreatorName = playerNames[0];
                gamePaticipantName = playerNames[1];
                // ��Ϸ�Ѵ��� 
                if (chessPeerHash.containsKey(gameCreatorName)
                        && chessPeerHash.get(gameCreatorName).equals("wait")) {
                    // ������Ϸ�����ߵ��׽ӿ������ƵĶ�Ӧ
                    synchronized (clientNameHash) {  
                        clientNameHash.put(clientSocket,
                                ("[inchess]" + gamePaticipantName));
                    }
                    // ���ӻ��޸���Ϸ����������Ϸ�����ߵ����ƵĶ�Ӧ 
                    synchronized (chessPeerHash) { 
                        chessPeerHash.put(gameCreatorName, gamePaticipantName);
                    }
                    sendPublicMsg(getUserList());
                    // ������Ϣ����Ϸ������ 
                    sendGamePeerMsg(gamePaticipantName, ("/peer " + "[inchess]" + gameCreatorName));
                    // ������Ϸ����Ϸ������ 
                    sendGamePeerMsg(gameCreatorName, ("/peer " + "[inchess]" + gamePaticipantName));
                    // ����Ϸδ������ܾ�������Ϸ 
                } else { 
                    sendGamePeerMsg(gamePaticipantName, "/reject");
                    try {
                        closeClient();
                    } catch (Exception ez) {
                        ez.printStackTrace();
                    }
                }
                // �յ�����ϢΪ��Ϸ��ʱ
            } else if (msgReceived.startsWith("/[inchess]")) {  
                int firstLocation = 0, lastLocation;
                lastLocation = msgReceived.indexOf(" ", 0);
                peerName = msgReceived.substring((firstLocation + 1), lastLocation);
                msgReceived = msgReceived.substring((lastLocation + 1));
                if (sendGamePeerMsg(peerName, msgReceived)) {
                    Feedback("/error");
                }
                // �յ�����ϢΪ������Ϸʱ 
            } else if (msgReceived.startsWith("/giveup ")) { 
                String chessClientName = msgReceived.substring(8);
                // ʤ����Ϊ��Ϸ�����ߣ�����ʤ����Ϣ
                if (chessPeerHash.containsKey(chessClientName)
                        && !((String) chessPeerHash.get(chessClientName)).equals("wait")) {  
                    sendGamePeerMsg((String) chessPeerHash.get(chessClientName),
                            "/youwin");
                    // ɾ���˳���Ϸ���û� 
                    synchronized (chessPeerHash) { 
                        chessPeerHash.remove(chessClientName);
                    }
                }
                // ʤ����Ϊ��Ϸ�����ߣ�����ʤ����Ϣ
                if (chessPeerHash.containsValue(chessClientName)) {  
                    sendGamePeerMsg((String) getHashKey(chessPeerHash,
                            chessClientName), "/youwin");
                    // ɾ���˳���Ϸ���û� 
                    synchronized (chessPeerHash) {
                        chessPeerHash.remove(getHashKey(chessPeerHash, chessClientName));
                    }
                }
                // �յ�����ϢΪ������Ϣʱ 
            } else { 
                int lastLocation = msgReceived.indexOf(" ", 0);
                if (lastLocation == -1) {
                    Feedback("��Ч����");
                    return;
                }
            }
        } else {
            msgReceived = clientNameHash.get(clientSocket) + ">" + msgReceived;
            serverMsgPanel.msgTextArea.append(msgReceived + "\n");
            sendPublicMsg(msgReceived);
            serverMsgPanel.msgTextArea.setCaretPosition(serverMsgPanel.msgTextArea.getText().length());
        }
    }

    // ���͹�����Ϣ 
    public void sendPublicMsg(String publicMsg) {
        synchronized (clientDataHash) {
            for (Enumeration enu = clientDataHash.elements(); enu.hasMoreElements();) {
                DataOutputStream outputData = (DataOutputStream) enu.nextElement();
                try {
                    outputData.writeUTF(publicMsg);
                } catch (IOException es) {
                    es.printStackTrace();
                }
            }
        }
    }

    // ������Ϣ��ָ������Ϸ�е��û� 
    public boolean sendGamePeerMsg(String gamePeerTarget, String gamePeerMsg) {
        // ������ȡ����Ϸ�е��û����׽ӿ� 
        for (Enumeration enu = clientDataHash.keys(); enu.hasMoreElements();) { 
            Socket userClient = (Socket) enu.nextElement();
            // �ҵ�Ҫ������Ϣ���û�ʱ
            if (gamePeerTarget.equals((String) clientNameHash.get(userClient))
                    && !gamePeerTarget.equals((String) clientNameHash.get(clientSocket))) {  
                synchronized (clientDataHash) {
                    // ��������� 
                    DataOutputStream peerOutData = (DataOutputStream) clientDataHash
                            .get(userClient);
                    try {
                        // ������Ϣ 
                        peerOutData.writeUTF(gamePeerMsg);
                    } catch (IOException es) {
                        es.printStackTrace();
                    }
                }
                return false;
            }
        }
        return true;
    }

    //���ͷ�����Ϣ�����ӵ���������
    public void Feedback(String feedBackMsg) {
        synchronized (clientDataHash) {
            DataOutputStream outputData = (DataOutputStream) clientDataHash
                    .get(clientSocket);
            try {
                outputData.writeUTF(feedBackMsg);
            } catch (Exception eb) {
                eb.printStackTrace();
            }
        }
    }

    //ȡ���û��б�
    public String getUserList() {
        String userList = "/userlist";
        for (Enumeration enu = clientNameHash.elements(); enu.hasMoreElements();) {
            userList = userList + " " + enu.nextElement();
        }
        return userList;
    }

    //����valueֵ��Hashtable��ȡ����Ӧ��key
    public Object getHashKey(Hashtable targetHash, Object hashValue) {
        Object hashKey;
        for (Enumeration enu = targetHash.keys(); enu.hasMoreElements();) {
            hashKey = (Object) enu.nextElement();
            if (hashValue.equals((Object) targetHash.get(hashKey))) {
                return hashKey;
            }
        }
        return null;
    }

    // �����ӵ�����ʱִ�еķ��� 
    public void sendInitMsg() {
        sendPublicMsg(getUserList());
        Feedback("/yourname " + clientNameHash.get(clientSocket));
        Feedback("(1)�Ծ�˫����ִһɫ���ӡ�\n" +
                "(2)�����̿��֡�\n" +
                "(3)���ȡ��׺󣬽������ӣ�ÿ��ֻ����һ�ӡ�\n" +
                "(4)�����������̵Ŀհ׵��ϣ������¶��󣬲������������ƶ������ô��������õ�����������𴦡�\n" +
                "(5)�ڷ��ĵ�һö���ӿ������������⽻����ϡ�\n" +
                "   ��ܰ��ʾ��\n" +
                "1.����˫��˭���������������һ���߼�Ϊʤ�ߡ�\n" +
                "2.��������������һ��ֱ��ʱ����Ӧ��ȡ���أ���ס���ӵ�һ�ˣ�����ͻ����������\n" +
                "3.Ӧ�������ڱ����ڳ����������֡����Ľ��ֵ����������ͻ᲻С�����������");
    }

    public void closeClient() {
        serverMsgPanel.msgTextArea.append("�û��Ͽ�����:" + clientSocket + "\n");
        //�������Ϸ�ͻ�������
        synchronized (chessPeerHash) {  
            if (chessPeerHash.containsKey(clientNameHash.get(clientSocket))) {
                chessPeerHash.remove((String) clientNameHash.get(clientSocket));
            }
            if (chessPeerHash.containsValue(clientNameHash.get(clientSocket))) {
                chessPeerHash.put(getHashKey(chessPeerHash, clientNameHash.get(clientSocket)), "tobeclosed");
            }
        }
        // ɾ���ͻ�����
        synchronized (clientDataHash) { 
            clientDataHash.remove(clientSocket);
        }
        // ɾ���ͻ����� 
        synchronized (clientNameHash) { 
            clientNameHash.remove(clientSocket);
        }
        sendPublicMsg(getUserList());
        serverMsgPanel.statusLabel.setText("��ǰ������:" + clientDataHash.size());
        try {
            clientSocket.close();
        } catch (IOException exx) {
            exx.printStackTrace();
        }
        isClientClosed = true;
    }

    @Override
    public void run() {
        DataInputStream inputData;
        synchronized (clientDataHash) {
            serverMsgPanel.statusLabel.setText("��ǰ������:" + clientDataHash.size());
        }
        // �ȴ����ӵ���������Ϣ 
        try { 
            inputData = new DataInputStream(clientSocket.getInputStream());
            sendInitMsg();
            while (true)
            {
                String message = inputData.readUTF();
                dealWithMsg(message);
            }
        } catch (IOException esx){}
        finally {
            if (!isClientClosed)
            {
                closeClient();
            }
        }
    }
}
