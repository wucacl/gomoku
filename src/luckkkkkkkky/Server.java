package luckkkkkkkky;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
//�����û�
class User {
    //�û���Ϣ
    private String name;                //�û���
    //ͨѶ
    private DataInputStream dis;        //�û���������
    private DataOutputStream dos;       //�û��������
    //�вι���
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
    Vector<User> userList =null; //�û��б�
    static HashSet<String> usersState = new HashSet<String>(); //��¼����׼��״̬���û�

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
                //�жϿͻ����Ƿ�����Ϣ�����¸���λ��
                //���̶���
                if (operateFlag.equals("���̶���")) {
                    it = userList.iterator();
                    System.out.println("�������ӳɹ�");
                    String me = user.getDis().readUTF();
                    String opponentsName = user.getDis().readUTF();
                    String step = user.getDis().readUTF();
                    System.out.println("�Լ�=>"+me);
                    System.out.println("����=>"+opponentsName);
                    System.out.println("����=>"+step);
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(me)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("���̶���");
                            uu.getDos().writeUTF(step);
                        }
                        if(uu.getName().equals(opponentsName)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("���̶���");
                            uu.getDos().writeUTF(step);
                        }
                    }
                }
                //������Ӯ
                if (operateFlag.equals("������Ӯ")) {
                    it = userList.iterator();
                    System.out.println("�ж����ӳɹ�");
                    String me = user.getDis().readUTF();
                    String opponentsName = user.getDis().readUTF();
                    String winPeople = user.getDis().readUTF();
                    System.out.println("ʤ��=>"+winPeople);
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(me)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("������Ӯ");
                            uu.getDos().writeUTF(winPeople);
                        }
                        if(uu.getName().equals(opponentsName)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("������Ӯ");
                            uu.getDos().writeUTF(winPeople);
                        }
                    }
                }
                //��ʼ��Ϸ
                if (operateFlag.equals("��ʼ��Ϸ")) {
                    it = userList.iterator();
                    System.out.println("�ж����ӳɹ�");
                    String me = user.getDis().readUTF();
                    String opponentsName = user.getDis().readUTF();
                    usersState.add(me);
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(opponentsName)) {
                            uu.getDos().writeUTF("׼��");
                        }
                    }
                    for(String i:usersState) {
                        if (i.equals(opponentsName)) {
                            it = userList.iterator();
                            while(it.hasNext()) {
                                User uu = (User) it.next();
                                if(uu.getName().equals(me)) {
                                    System.out.println(uu.getName());
                                    uu.getDos().writeUTF("��ʼ��Ϸ");
                                }
                                if(uu.getName().equals(opponentsName)) {
                                    System.out.println(uu.getName());
                                    uu.getDos().writeUTF("��ʼ��Ϸ");
                                }
                            }
                        }
                    }
                }
                //�����˳���Ϸ
                if(operateFlag.equals("�˳���Ϸ")) {
                    it = userList.iterator();
                    System.out.println("�ж����ӳɹ�");
                    String me = user.getDis().readUTF();
                    String opponentsName = user.getDis().readUTF();
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(opponentsName)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("�˳���Ϸ");
                            uu.getDos().writeUTF("���֣�"+me+"���˳�����Ϸ��������ѡ����");
                        }
                    }
                    //�˳������Ƴ�������������������
                    it = userList.iterator();
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(me)) {
                            it.remove();
                        }
                    }
                    break;
                }
                //���ֻ���
                if(operateFlag.equals("����")) {
                    it = userList.iterator();
                    System.out.println("�ж����ӳɹ�");
                    String me = user.getDis().readUTF();
                    String opponentsName = user.getDis().readUTF();
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(opponentsName)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("�������");
                        }
                    }
                }
                //�Ƿ����
                if(operateFlag.equals("ȷ�ϻ���")) {
                    it = userList.iterator();
                    System.out.println("�ж����ӳɹ�");
                    String me = user.getDis().readUTF();
                    String opponentsName = user.getDis().readUTF();
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(me)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("ȷ�ϻ���");
                        }
                        if(uu.getName().equals(opponentsName)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("ȷ�ϻ���");
                        }
                    }
                }
                //ȷ�Ϻ���
                if(operateFlag.equals("����")) {
                    it = userList.iterator();
                    System.out.println("�ж����ӳɹ�");
                    String me = user.getDis().readUTF();
                    String opponentsName = user.getDis().readUTF();
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(opponentsName)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("������");
                        }
                    }
                }
                //����
                if(operateFlag.equals("����")) {
                    it = userList.iterator();
                    System.out.println("�ж����ӳɹ�");
                    String me = user.getDis().readUTF();
                    String opponentsName = user.getDis().readUTF();
                    while(it.hasNext()) {
                        User uu = (User) it.next();
                        if(uu.getName().equals(opponentsName)) {
                            System.out.println(uu.getName());
                            uu.getDos().writeUTF("����");
                        }
                    }
                }
                //�˳�׼��״̬
                if(operateFlag.equals("�˳�׼��״̬")) {
                    it = userList.iterator();
                    System.out.println("�ж����ӳɹ�");
                    String me = user.getDis().readUTF();
                    usersState.remove(me);
                }
                //�����˳���Ϸ
                if(operateFlag.equals("�ر���Ϸ")) {
                    //�˳������Ƴ�������������������
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
    Vector<User> users;        //�õ��û��б�
    Socket socket;             //�õ�����ʱ��socke�����ڶ�ȡ��Ϣ
    String checkName;          //�õ��û�������û���
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
                    System.out.println("��һ��");
                    dos.writeUTF("�ǳ���ȷ");
                    flag = true;
                    break outer;
                }
                ;
                while (it.hasNext()) {
                    System.out.println(123123);
                    User uu = (User) it.next();
                    if (uu.getName().equals(checkName) && !checkName.equals("")) {
                        System.out.println("�ظ�");
                        dos.writeUTF("�ǳƴ���");
                        continue outer;
                    }
                }
                if (checkName.equals("")) break;
                dos.writeUTF("�ǳ���ȷ");
                flag = true;
                break outer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(flag) {
            String userName = checkName;
            System.out.println("�û���-" + userName);
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