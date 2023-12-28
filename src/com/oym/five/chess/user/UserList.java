package com.oym.five.chess.user;

import java.awt.*;

/* 用户列表
 * 服务器最多支持
 * 8个用户同时在线 
 * 该列表被添加到面板中，使用 "BorderLayout" 布局格式 */
public class UserList extends Panel {
    
    public List userList = new List(10);

    public UserList(){
        setLayout(new BorderLayout());
        for(int i = 0;i < 10;i++) {
            userList.add(i + "." + "刘家凯");
        }
        add(userList,BorderLayout.CENTER);
    }
}
