package com.oym.five.chess.user;

import javax.swing.*;
import java.awt.*;

/*
 * �û�����ҳ��
 * �������Ϊһ��TextArea��ͼ�ؼ���ӵ��һ����ֱ����Ĺ�������
 * ��TextArea����ӵ�����У�ʹ�� "BorderLayout" ���ָ�ʽ��
 */
public class UserChat extends JPanel {
    public JTextArea chatTextArea=new JTextArea("  �����ҵ��˶������嶼�в�ͬ�İ��ƣ����磺\n" +
            "   �����˰��������Ϊ�������塱����ʾ����֮����������������������еĽ���" +
            "��\n    ŷ���˳���Ϊ����ʿ�塱��������������ľ��ӷ��ʤ����ʿ��\n" +
            "   �ձ��������Ϊ���������塱��˵���������ʺ��������˵������ص��˼ά��ʽ��",15,18);
    public UserChat() {
        setLayout(new BorderLayout());
        chatTextArea.setAutoscrolls(true);
        chatTextArea.setSelectionColor(Color.PINK);
        chatTextArea.setCaretColor(Color.PINK);
        chatTextArea.setDisabledTextColor(Color.PINK);
        chatTextArea.setLineWrap(true);
        add(chatTextArea,BorderLayout.CENTER);
    }
}
