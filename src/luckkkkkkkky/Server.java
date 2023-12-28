package luckkkkkkkky;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
//定义用户
class User {
    //用户信息
    private String name;                //用户名
    //通讯
    private DataInputStream dis;        //用户的输入流
    private DataOutputStream dos;       //用户的输出流
    //有参构造
    public User(String name, DataInputStream dis, DataOutputStream dos) {
        this.name = name;
        this.dis = dis;
        this.dos = dos;
    }
    public String getName() {
        return name;
    }
    public DataInputStream getDis() {
        return dis;
    }
    public DataOutputStream getDos() {
        return dos;
    }
}
class MsgReader extends Thread{
    User user;
    Vector<User> userList =null; //用户列表
    static HashSet<String> usersState = new HashSet<String>(); //记录进入准备状态的用户

    public MsgReader(User user, Vector<User> userList) {
        this.user = user;
        this.userList= userList;
    }

    public void run() {
        Iterator<User> it;
              while (true) {
            String info = null;
            String imgInfo = null;
            try {
                String operateFlag = user.getDis().readUTF();
                //判断客户端是否传入信息，重新更新位置
                //棋盘对弈
                if (operateFlag.equals("棋盘对弈")) {
                    it = userList.iterator();
                    System.out.println("对弈连接成功");
                    String me = user.getDis().readUTF();
                    String opponentsName = user.getDis().readUTF();
                    String step = user.getDis().readUTF();
                    System.out.println("自己=>"+me);
                    System.out.println("对手=>"+opponentsName);
                    System.out.println("棋子=>"+step);
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(me)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("棋盘对弈");
                            uu.getDos().writeUTF(step);
                        }
                        if(uu.getName().equals(opponentsName)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("棋盘对弈");
                            uu.getDos().writeUTF(step);
                        }
                    }
                }
                //定断输赢
                if (operateFlag.equals("定断输赢")) {
                    it = userList.iterator();
                    System.out.println("判断连接成功");
                    String me = user.getDis().readUTF();
                    String opponentsName = user.getDis().readUTF();
                    String winPeople = user.getDis().readUTF();
                    System.out.println("胜利=>"+winPeople);
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(me)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("定断输赢");
                            uu.getDos().writeUTF(winPeople);
                        }
                        if(uu.getName().equals(opponentsName)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("定断输赢");
                            uu.getDos().writeUTF(winPeople);
                        }
                    }
                }
                //开始游戏
                if (operateFlag.equals("开始游戏")) {
                    it = userList.iterator();
                    System.out.println("判断连接成功");
                    String me = user.getDis().readUTF();
                    String opponentsName = user.getDis().readUTF();
                    usersState.add(me);
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(opponentsName)) {
                            uu.getDos().writeUTF("准备");
                        }
                    }
                    for(String i:usersState) {
                        if (i.equals(opponentsName)) {
                            it = userList.iterator();
                            while(it.hasNext()) {
                                User uu = (User) it.next();
                                if(uu.getName().equals(me)) {
                                    System.out.println(uu.getName());
                                    uu.getDos().writeUTF("开始游戏");
                                }
                                if(uu.getName().equals(opponentsName)) {
                                    System.out.println(uu.getName());
                                    uu.getDos().writeUTF("开始游戏");
                                }
                            }
                        }
                    }
                }
                //对手退出游戏
                if(operateFlag.equals("退出游戏")) {
                    it = userList.iterator();
                    System.out.println("判断连接成功");
                    String me = user.getDis().readUTF();
                    String opponentsName = user.getDis().readUTF();
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(opponentsName)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("退出游戏");
                            uu.getDos().writeUTF("对手（"+me+"）退出了游戏，请重新选座！");
                        }
                    }
                    //退出程序，移除集合容器里的自身对象
                    it = userList.iterator();
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(me)) {
                            it.remove();
                        }
                    }
                    break;
                }
                //对手悔棋
                if(operateFlag.equals("悔棋")) {
                    it = userList.iterator();
                    System.out.println("判断连接成功");
                    String me = user.getDis().readUTF();
                    String opponentsName = user.getDis().readUTF();
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(opponentsName)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("请求悔棋");
                        }
                    }
                }
                //是否悔棋
                if(operateFlag.equals("确认悔棋")) {
                    it = userList.iterator();
                    System.out.println("判断连接成功");
                    String me = user.getDis().readUTF();
                    String opponentsName = user.getDis().readUTF();
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(me)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("确认悔棋");
                        }
                        if(uu.getName().equals(opponentsName)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("确认悔棋");
                        }
                    }
                }
                //确认和棋
                if(operateFlag.equals("悔棋")) {
                    it = userList.iterator();
                    System.out.println("判断连接成功");
                    String me = user.getDis().readUTF();
                    String opponentsName = user.getDis().readUTF();
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(opponentsName)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("不悔棋");
                        }
                    }
                }
                //认输
                if(operateFlag.equals("认输")) {
                    it = userList.iterator();
                    System.out.println("判断连接成功");
                    String me = user.getDis().readUTF();
                    String opponentsName = user.getDis().readUTF();
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(opponentsName)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("认输");
                        }
                    }
                }
                //退出准备状态
                if(operateFlag.equals("退出准备状态")) {
                    it = userList.iterator();
                    System.out.println("判断连接成功");
                    String me = user.getDis().readUTF();
                    usersState.remove(me);
                }
                //对手退出游戏
                if(operateFlag.equals("关闭游戏")) {
                    //退出程序，移除集合容器里的自身对象
                    String me = user.getDis().readUTF();
                    it = userList.iterator();
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(me)) {
                            it.remove();
                        }
                    }
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
class MsgVerify extends Thread {
    Vector<User> users;        //拿到用户列表
    Socket socket;             //拿到连接时的socke，用于读取信息
    String checkName;          //拿到用户输入的用户名
    public MsgVerify( Vector<User> userList ,Socket socket) {
        this.users= userList;
        this.socket = socket;
    }
    public void run() {
        boolean flag = false;
        DataInputStream dis = null;
        DataOutputStream dos = null;
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            outer:
            while (true) {
                checkName = dis.readUTF();
                Iterator<User> it = users.iterator();
                System.out.println(users.size());
                if (users.size() == 0 && !checkName.equals("")) {
                    System.out.println("第一人");
                    dos.writeUTF("昵称正确");
                    flag = true;
                    break outer;
                }
                ;
                while (it.hasNext()) {
                    System.out.println(123123);
                    User uu = (User) it.next();
                    if (uu.getName().equals(checkName) && !checkName.equals("")) {
                        System.out.println("重复");
                        dos.writeUTF("昵称错误");
                        continue outer;
                    }
                }
                if (checkName.equals("")) break;
                dos.writeUTF("昵称正确");
                flag = true;
                break outer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(flag) {
            String userName = checkName;
            System.out.println("用户名-" + userName);
            User user = new User(userName, dis, dos);
            users.add(user);
            MsgReader userReader = new MsgReader(user, users);
            userReader.start();
        }
    }

}
public class Server {
    Vector<User> users = new Vector<User>();
    public void waitConnect() throws Exception {
        ServerSocket sSocket = new ServerSocket(8080);
        while (true) {
            Socket socket = sSocket.accept();
            MsgVerify msgVerify = new MsgVerify(users,socket);
            msgVerify.start();
        }
    }

    public static void main(String args[]) {
        Server chessServer = new Server();
        try {
            chessServer.waitConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}