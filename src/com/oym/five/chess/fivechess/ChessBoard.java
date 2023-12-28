package com.oym.five.chess.fivechess;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

// ����
public class ChessBoard extends Panel implements MouseListener, ActionListener {
    //public int gap = 30;
  //��������
  	private int num;
  	//�м��
  	private int gap;
    // ����Ƿ���ʹ�� 
    public boolean isMouseEnabled = false;
    // �Ƿ�ʤ�� 
    public boolean isWon = false;
    // ���ӵ�x������λ 
    public int chessX_POS = -1;
    // ���ӵ�y������λ 
    public int chessY_POS = -1;
    // ���ӵ���ɫ 
    public int chessColor = 1;
    // ����x������λ���� 
    public int chessBlack_XPOS[] = new int[200];
    // ����y������λ���� 
    public int chessBlack_YPOS[] = new int[200];
    // ����x������λ���� 
    public int chessWhite_XPOS[] = new int[200];
    // ����y������λ���� 
    public int chessWhite_YPOS[] = new int[200];
    // �������� 
    public int chessBlackCount = 0;
    // �������� 
    public int chessWhiteCount = 0;
    // �����ʤ���� 
    public int chessBlackVicTimes = 0;
    // �����ʤ���� 
    public int chessWhiteVicTimes = 0;
    // �׽ӿ� 
    public Socket chessSocket;
    protected DataInputStream inputData;
    protected DataOutputStream outputData;
    public String chessSelfName = null;
    public String chessPeerName = null;
    public String host = null;
    public int port = 1234;
    public TextField statusText = new TextField("�������ӷ�����������");
    public ChessThread chessThread = new ChessThread(this);

    public ChessBoard() {
    	String rownumber=JOptionPane.showInputDialog("��������������(��Χ5~20)��");
        num = Integer.parseInt(rownumber);//�õ�����������
        System.out.print(num);
        while(num<5||num>20)
        {
        	rownumber=JOptionPane.showInputDialog("����������������������(��Χ5~20)��");
        	num = Integer.parseInt(rownumber);
        }
        gap=580/(num+1);
        setSize(600, 600);
        setLayout(null);
        setBackground(new Color(205, 133, 63));
        addMouseListener(this);
        add(statusText);
        statusText.setBounds(new Rectangle(80, 5, 440, 24));
        statusText.setEditable(false);
    }

    //���ӵ�����
    public boolean connectServer(String ServerIP, int ServerPort) throws Exception {
        try {
            // ȡ�������˿� 
            chessSocket = new Socket(ServerIP, ServerPort);
            // ȡ�������� 
            inputData = new DataInputStream(chessSocket.getInputStream());
            // ȡ������� 
            outputData = new DataOutputStream(chessSocket.getOutputStream());
            chessThread.start();
            return true;
        } catch (IOException ex) {
            statusText.setText("sorry������ʧ��!!! \n");
        }
        return false;
    }

    // �趨ʤ��ʱ������״̬
    public void setVicStatus(int vicChessColor) {
        // ������� 
        this.removeAll();
        // �������λ�����õ���� 
        for (int i = 0; i <= chessBlackCount; i++) {
            chessBlack_XPOS[i] = 0;
            chessBlack_YPOS[i] = 0;
        }
        // �������λ�����õ���� 
        for (int i = 0; i <= chessWhiteCount; i++) {
            chessWhite_XPOS[i] = 0;
            chessWhite_YPOS[i] = 0;
        }
        // ��������ϵĺ����� 
        chessBlackCount = 0;
        // ��������ϵİ����� 
        chessWhiteCount = 0;
        add(statusText);
        statusText.setBounds(40, 5, 200, 24);
        // ����ʤ
        if (vicChessColor == 1) {
            chessBlackVicTimes++;
            statusText.setText("��ϲ�ڷ�ʤ��������VS�� " + chessBlackVicTimes + ":" + chessWhiteVicTimes
                    + ".��Ϸ�������ȴ��׷�...");
            // ����ʤ
        } else if (vicChessColor == -1) {
            chessWhiteVicTimes++;
            statusText.setText("��ϲ�׷�ʤ��������VS�� " + chessBlackVicTimes + ":" + chessWhiteVicTimes
                    + ".��Ϸ����,�ȴ��ڷ�...");
        }
    }

    // ȡ��ָ�����ӵ�λ��
    public void setLocation(int xPos, int yPos, int chessColor) {
        // ����Ϊ����ʱ
        if (chessColor == 1) {
            chessBlack_XPOS[chessBlackCount] = xPos * gap;
            chessBlack_YPOS[chessBlackCount] = yPos * gap;
            chessBlackCount++;
        // ����Ϊ����ʱ
        } else if (chessColor == -1) {
            chessWhite_XPOS[chessWhiteCount] = xPos * gap;
            chessWhite_YPOS[chessWhiteCount] = yPos * gap;
            chessWhiteCount++;
        }
    }
    public int getgap()
    {
    	return gap;
    }
    //����������㷨
    public boolean checkVicStatus(int xPos, int yPos, int chessColor) {
        // ����������
        int chessLinkedCount = 1;
        // ���ڱȽ��Ƿ�Ҫ��������һ�����ӵ��������� 
        int chessLinkedCompare = 1;
        // Ҫ�Ƚϵ������������е�����λ��
        int chessToCompareIndex = 0;
        // ���������λ��
        int closeGrid = 1;
        // ����ʱ
        if (chessColor == 1) {
            // ����������������Ļ�����ʼ������Ϊ1
            chessLinkedCount = 1;
            //����ÿ��forѭ�����Ϊһ�飬��Ϊ�����λ����λ���м��������
            
            // ��������4������
            // �жϵ�ǰ�µ����ӵ��ұ�4�������Ƿ�Ϊ����
            for (closeGrid = 1; closeGrid <= 4; closeGrid++) {
                // �������������к�����
                for (chessToCompareIndex = 0; chessToCompareIndex <= chessBlackCount; chessToCompareIndex++) {
                    if (((xPos + closeGrid) * gap == chessBlack_XPOS[chessToCompareIndex])
                            && ((yPos * gap) == chessBlack_YPOS[chessToCompareIndex])) {
                        // ��������1 
                        chessLinkedCount = chessLinkedCount + 1;
                        // ��������ʱ��ʤ�� 
                        if (chessLinkedCount == 5) {
                            return true;
                        }
                    }
                }
                if (chessLinkedCount == (chessLinkedCompare + 1)) {
                    chessLinkedCompare++;
                    // ���м���һ�����ӷǺ��壬������˷�֧����ʱ�����ٱ���
                } else {
                    break;
                }
            }
            
            // �жϵ�ǰ�µ����ӵ����4�������Ƿ�Ϊ����
            for (closeGrid = 1; closeGrid <= 4; closeGrid++) {
                for (chessToCompareIndex = 0; chessToCompareIndex <= chessBlackCount; chessToCompareIndex++) {
                    if (((xPos - closeGrid) * gap == chessBlack_XPOS[chessToCompareIndex])
                            && (yPos * gap == chessBlack_YPOS[chessToCompareIndex])) {
                        chessLinkedCount++;
                        if (chessLinkedCount == 5) {
                            return true;
                        }
                    }
                }
                if (chessLinkedCount == (chessLinkedCompare + 1)) {
                    chessLinkedCompare++;
                } else {
                    break;
                }
            }

            // �����µ�һ��forѭ��ʱҪ�������������� 
            chessLinkedCount = 1;
            chessLinkedCompare = 1;
            // �жϵ�ǰ�µ����ӵ��ϱ�4�������Ƿ�Ϊ����
            for (closeGrid = 1; closeGrid <= 4; closeGrid++) {
                for (chessToCompareIndex = 0; chessToCompareIndex <= chessBlackCount; chessToCompareIndex++) {
                    if ((xPos * gap == chessBlack_XPOS[chessToCompareIndex])
                            && ((yPos + closeGrid) * gap == chessBlack_YPOS[chessToCompareIndex])) {
                        chessLinkedCount++;
                        if (chessLinkedCount == 5) {
                            return true;
                        }
                    }
                }
                if (chessLinkedCount == (chessLinkedCompare + 1)) {
                    chessLinkedCompare++;
                } else {
                    break;
                }
            }
            
            // �жϵ�ǰ�µ����ӵ��±�4�������Ƿ�Ϊ����
            for (closeGrid = 1; closeGrid <= 4; closeGrid++) {
                for (chessToCompareIndex = 0; chessToCompareIndex <= chessBlackCount; chessToCompareIndex++) {
                    if ((xPos * gap == chessBlack_XPOS[chessToCompareIndex])
                            && ((yPos - closeGrid) * gap == chessBlack_YPOS[chessToCompareIndex])) {
                        chessLinkedCount++;
                        if (chessLinkedCount == 5) {
                            return true;
                        }
                    }
                }
                if (chessLinkedCount == (chessLinkedCompare + 1)) {
                    chessLinkedCompare++;
                } else {
                    break;
                }
            }

            chessLinkedCount = 1;
            chessLinkedCompare = 1;
            for (closeGrid = 1; closeGrid <= 4; closeGrid++) {
                for (chessToCompareIndex = 0; chessToCompareIndex <= chessBlackCount; chessToCompareIndex++) {
                    // �жϵ�ǰ�µ����ӵ����Ϸ���4�������Ƿ�Ϊ���� 
                    if (((xPos - closeGrid) * gap == chessBlack_XPOS[chessToCompareIndex])
                            && ((yPos + closeGrid) * gap == chessBlack_YPOS[chessToCompareIndex])) {
                        chessLinkedCount++;
                        if (chessLinkedCount == 5) {
                            return true;
                        }
                    }
                }
                if (chessLinkedCount == (chessLinkedCompare + 1)) {
                    chessLinkedCompare++;
                } else {
                    break;
                }
            }
            for (closeGrid = 1; closeGrid <= 4; closeGrid++) {
                for (chessToCompareIndex = 0; chessToCompareIndex <= chessBlackCount; chessToCompareIndex++) {
                    // �жϵ�ǰ�µ����ӵ����·���4�������Ƿ�Ϊ���� 
                    if (((xPos + closeGrid) * gap == chessBlack_XPOS[chessToCompareIndex])
                            && ((yPos - closeGrid) * gap == chessBlack_YPOS[chessToCompareIndex])) {
                        chessLinkedCount++;
                        if (chessLinkedCount == 5) {
                            return true;
                        }
                    }
                }
                if (chessLinkedCount == (chessLinkedCompare + 1)) {
                    chessLinkedCompare++;
                } else {
                    break;
                }
            }

            chessLinkedCount = 1;
            chessLinkedCompare = 1;
            // �жϵ�ǰ�µ����ӵ����Ϸ���4�������Ƿ�Ϊ����
            for (closeGrid = 1; closeGrid <= 4; closeGrid++) {
                for (chessToCompareIndex = 0; chessToCompareIndex <= chessBlackCount; chessToCompareIndex++) {
                    if (((xPos + closeGrid) * gap == chessBlack_XPOS[chessToCompareIndex])
                            && ((yPos + closeGrid) * gap == chessBlack_YPOS[chessToCompareIndex])) {
                        chessLinkedCount++;
                        if (chessLinkedCount == 5) {
                            return true;
                        }
                    }
                }
                if (chessLinkedCount == (chessLinkedCompare + 1)) {
                    chessLinkedCompare++;
                } else {
                    break;
                }
            }
            
            // �жϵ�ǰ�µ����ӵ����·���4�������Ƿ�Ϊ����
            for (closeGrid = 1; closeGrid <= 4; closeGrid++) {
                for (chessToCompareIndex = 0; chessToCompareIndex <= chessBlackCount; chessToCompareIndex++) {
                    if (((xPos - closeGrid) * gap == chessBlack_XPOS[chessToCompareIndex])
                            && ((yPos - closeGrid) * gap == chessBlack_YPOS[chessToCompareIndex])) {
                        chessLinkedCount++;
                        if (chessLinkedCount == 5) {
                            return true;
                        }
                    }
                }
                if (chessLinkedCount == (chessLinkedCompare + 1)) {
                    chessLinkedCompare++;
                } else {
                    break;
                }
            }
        // �������� 
        } else if (chessColor == -1) {
            chessLinkedCount = 1;
            // �жϵ�ǰ�µ����ӵ��ұ�4�������Ƿ�Ϊ����
            for (closeGrid = 1; closeGrid <= 4; closeGrid++) {
                for (chessToCompareIndex = 0; chessToCompareIndex <= chessWhiteCount; chessToCompareIndex++) {
                    if (((xPos + closeGrid) * gap == chessWhite_XPOS[chessToCompareIndex])
                            && (yPos * gap == chessWhite_YPOS[chessToCompareIndex])) {
                        chessLinkedCount++;
                        if (chessLinkedCount == 5) {
                            return true;
                        }
                    }
                }
                if (chessLinkedCount == (chessLinkedCompare + 1)) {
                    chessLinkedCompare++;
                } else {
                    break;
                }
            }

            // �жϵ�ǰ�µ����ӵ����4�������Ƿ�Ϊ����
            for (closeGrid = 1; closeGrid <= 4; closeGrid++) {
                for (chessToCompareIndex = 0; chessToCompareIndex <= chessWhiteCount; chessToCompareIndex++) {
                    if (((xPos - closeGrid) * gap == chessWhite_XPOS[chessToCompareIndex])
                            && (yPos * gap == chessWhite_YPOS[chessToCompareIndex])) {
                        chessLinkedCount++;
                        if (chessLinkedCount == 5) {
                            return true;
                        }
                    }
                }
                if (chessLinkedCount == (chessLinkedCompare + 1)) {
                    chessLinkedCompare++;
                } else {
                    break;
                }
            }

            chessLinkedCount = 1;
            chessLinkedCompare = 1;
            // �жϵ�ǰ�µ����ӵ��ϱ�4�������Ƿ�Ϊ����
            for (closeGrid = 1; closeGrid <= 4; closeGrid++) {
                for (chessToCompareIndex = 0; chessToCompareIndex <= chessWhiteCount; chessToCompareIndex++) {
                    if ((xPos * gap == chessWhite_XPOS[chessToCompareIndex])
                            && ((yPos + closeGrid) * gap == chessWhite_YPOS[chessToCompareIndex])) {
                        chessLinkedCount++;
                        if (chessLinkedCount == 5) {
                            return true;
                        }
                    }
                }
                if (chessLinkedCount == (chessLinkedCompare + 1)) {
                    chessLinkedCompare++;
                } else {
                    break;
                }
            }

            // �жϵ�ǰ�µ����ӵ��±�4�������Ƿ�Ϊ����
            for (closeGrid = 1; closeGrid <= 4; closeGrid++) {
                for (chessToCompareIndex = 0; chessToCompareIndex <= chessWhiteCount; chessToCompareIndex++) {
                    if ((xPos * gap == chessWhite_XPOS[chessToCompareIndex])
                            && ((yPos - closeGrid) * gap == chessWhite_YPOS[chessToCompareIndex])) {
                        chessLinkedCount++;
                        if (chessLinkedCount == 5) {
                            return true;
                        }
                    }
                }
                if (chessLinkedCount == (chessLinkedCompare + 1)) {
                    chessLinkedCompare++;
                } else {
                    break;
                }
            }

            chessLinkedCount = 1;
            chessLinkedCompare = 1;
            // �жϵ�ǰ�µ����ӵ����Ϸ���4�������Ƿ�Ϊ����
            for (closeGrid = 1; closeGrid <= 4; closeGrid++) {
                for (chessToCompareIndex = 0; chessToCompareIndex <= chessWhiteCount; chessToCompareIndex++) {
                    if (((xPos - closeGrid) * gap == chessWhite_XPOS[chessToCompareIndex])
                            && ((yPos + closeGrid) * gap == chessWhite_YPOS[chessToCompareIndex])) {
                        chessLinkedCount++;
                        if (chessLinkedCount == 5) {
                            return true;
                        }
                    }
                }
                if (chessLinkedCount == (chessLinkedCompare + 1)) {
                    chessLinkedCompare++;
                } else {
                    break;
                }
            }

            // �жϵ�ǰ�µ����ӵ����·���4�������Ƿ�Ϊ����
            for (closeGrid = 1; closeGrid <= 4; closeGrid++) {
                for (chessToCompareIndex = 0; chessToCompareIndex <= chessWhiteCount; chessToCompareIndex++) {
                    if (((xPos + closeGrid) * gap == chessWhite_XPOS[chessToCompareIndex])
                            && ((yPos - closeGrid) * gap == chessWhite_YPOS[chessToCompareIndex])) {
                        chessLinkedCount++;
                        if (chessLinkedCount == 5) {
                            return true;
                        }
                    }
                }
                if (chessLinkedCount == (chessLinkedCompare + 1)) {
                    chessLinkedCompare++;
                } else {
                    break;
                }
            }

            chessLinkedCount = 1;
            chessLinkedCompare = 1;
            // �жϵ�ǰ�µ����ӵ����Ϸ���4�������Ƿ�Ϊ����
            for (closeGrid = 1; closeGrid <= 4; closeGrid++) {
                for (chessToCompareIndex = 0; chessToCompareIndex <= chessWhiteCount; chessToCompareIndex++) {
                    if (((xPos + closeGrid) * gap == chessWhite_XPOS[chessToCompareIndex])
                            && ((yPos + closeGrid) * gap == chessWhite_YPOS[chessToCompareIndex])) {
                        chessLinkedCount++;
                        if (chessLinkedCount == 5) {
                            return true;
                        }
                    }
                }
                if (chessLinkedCount == (chessLinkedCompare + 1)) {
                    chessLinkedCompare++;
                } else {
                    break;
                }
            }

            // �жϵ�ǰ�µ����ӵ����·���4�������Ƿ�Ϊ����
            for (closeGrid = 1; closeGrid <= 4; closeGrid++) {
                for (chessToCompareIndex = 0; chessToCompareIndex <= chessWhiteCount; chessToCompareIndex++) {
                    if (((xPos - closeGrid) * gap == chessWhite_XPOS[chessToCompareIndex])
                            && ((yPos - closeGrid) * gap == chessWhite_YPOS[chessToCompareIndex])) {
                        chessLinkedCount++;
                        if (chessLinkedCount == 5) {
                            return (true);
                        }
                    }
                }
                if (chessLinkedCount == (chessLinkedCompare + 1)) {
                    chessLinkedCompare++;
                } else {
                    break;
                }
            }
        }
        return false;
    }

    //������
    @Override
    public void paint(Graphics g) {
    	gap=580/(num+1);

        for(int i=0;i<this.num;i++)
		{
			g.setColor(Color.black);
			((Graphics2D) g).setStroke(new BasicStroke(1));//��������������ϸ
			if(i==0||i==num-1)
			{
				((Graphics2D) g).setStroke(new BasicStroke(2));
			}
			g.drawLine(40+gap,40+(i*gap)+gap,40+(gap*num),40+(i*gap)+gap);//������
			g.drawLine(40+(i*gap)+gap,40+gap,40+(i*gap)+gap,40+(gap*num));
		}

    }

    // ������ 
    public void paintChessPoint(int xPos, int yPos, int chessColor) {
        ChessBlack chessBlack = new ChessBlack(this);
        ChessWhite chessWhite = new ChessWhite(this);
        // ����
        if (chessColor == 1 && isMouseEnabled) {
            // �������ӵ�λ�� 
            setLocation(xPos, yPos, chessColor);
            // ȡ�õ�ǰ����״̬ 
            isWon = checkVicStatus(xPos, yPos, chessColor);
            // ��ʤ��״̬
            if (isWon == false) {
                chessThread.sendMessage("/" + chessPeerName + " /chess "
                        + xPos + " " + yPos + " " + chessColor);
                // ��������ӵ�������
                this.add(chessBlack);
                // �������ӱ߽� 
                chessBlack.setBounds(xPos * gap -4, yPos * gap -3, gap, gap);
                statusText.setText("��(��" + chessBlackCount + "��)" + xPos + " " + yPos + ",�ֵ��׷�.");
                // �������Ϊ������ 
                isMouseEnabled = false;
                // ʤ��״̬
            } else {
                chessThread.sendMessage("/" + chessPeerName + " /chess "
                        + xPos + " " + yPos + " " + chessColor);
                this.add(chessBlack);
                chessBlack.setBounds(xPos * gap -4, yPos * gap -3, gap, gap);
                // ����ʤ���������������Ϊ����ʤ��
                setVicStatus(1);
                isMouseEnabled = false;
            }
            // ���� 
        } else if (chessColor == -1 && isMouseEnabled) {
            setLocation(xPos, yPos, chessColor);
            isWon = checkVicStatus(xPos, yPos, chessColor);
            if (isWon == false) {
                chessThread.sendMessage("/" + chessPeerName + " /chess "
                        + xPos + " " + yPos + " " + chessColor);
                this.add(chessWhite);
                chessWhite.setBounds(xPos * gap -4, yPos * gap-3 , gap, gap);
                statusText.setText("��(��" + chessWhiteCount + "��)" + xPos + " " + yPos + ",�ֵ��ڷ�.");
                isMouseEnabled = false;
            } else {
                chessThread.sendMessage("/" + chessPeerName + " /chess "
                        + xPos + " " + yPos + " " + chessColor);
                this.add(chessWhite);
                chessWhite.setBounds(xPos * gap-4 , yPos * gap -3, gap, gap);
                // ����ʤ���������������Ϊ���� 
                setVicStatus(-1);
                isMouseEnabled = false;
            }
        }
    }

    // ����������
    public void paintNetChessPoint(int xPos, int yPos, int chessColor) {
        ChessBlack chessBlack = new ChessBlack(this);
        ChessWhite chessWhite = new ChessWhite(this);
        setLocation(xPos, yPos, chessColor);
        if (chessColor == 1) {
            isWon = checkVicStatus(xPos, yPos, chessColor);
            if (isWon == false) {
                this.add(chessBlack);
                chessBlack.setBounds(xPos * gap-4 , yPos * gap -3, 30, 30);
                statusText.setText("��(��" + chessBlackCount + "��)" + xPos + " " + yPos + ",�ֵ��׷�.");
                isMouseEnabled = true;
            } else {
                chessThread.sendMessage("/" + chessPeerName + " /victory " + chessColor);
                this.add(chessBlack);
                chessBlack.setBounds(xPos * gap -4, yPos *gap-3, 30, 30);
                setVicStatus(1);
                isMouseEnabled = true;
            }
        } else if (chessColor == -1) {
            isWon = checkVicStatus(xPos, yPos, chessColor);
            if (isWon == false) {
                this.add(chessWhite);
                chessWhite.setBounds(xPos * gap-4, yPos * gap-3, 30, 30);
                statusText.setText("��(��" + chessWhiteCount + "��)" + xPos + " " + yPos + ",�ֵ��ڷ�.");
                isMouseEnabled = true;
            } else {
                chessThread.sendMessage("/" + chessPeerName + " /victory " + chessColor);
                this.add(chessWhite);
                chessWhite.setBounds(xPos * gap-4, yPos * gap-3, 30, 30);
                setVicStatus(-1);
                isMouseEnabled = true;
            }
        }
    }

    //���������¼�
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getModifiers() == InputEvent.BUTTON1_MASK) {
            chessX_POS = e.getX();
            System.out.println(chessX_POS);
            chessY_POS = e.getY();
            System.out.println(chessY_POS);
            int a = (chessX_POS-40-(gap/2))/gap, b = (chessY_POS-40-(gap/2))/gap;
            System.out.println("a="+a);
            System.out.println("b="+b);
            // ����λ�ò���ȷʱ����ִ���κβ��� 
            if (chessX_POS <(40+gap/2)|| chessX_POS >40+(gap*(num+1))
                    || chessY_POS <=40+(gap/2) || chessY_POS < 40+(gap*(num+1))) {
            } else {
                // ������
                paintChessPoint(a, b, chessColor);
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
