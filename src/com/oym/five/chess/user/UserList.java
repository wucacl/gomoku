package com.oym.five.chess.user;

import java.awt.*;

/* �û��б�
 * ���������֧��
 * 8���û�ͬʱ���� 
 * ���б���ӵ�����У�ʹ�� "BorderLayout" ���ָ�ʽ */
public class UserList extends Panel {
    
    public List userList = new List(10);

    public UserList(){
        setLayout(new BorderLayout());
        for(int i = 0;i < 10;i++) {
            userList.add(i + "." + "���ҿ�");
        }
        add(userList,BorderLayout.CENTER);
    }
}
