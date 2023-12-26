package luckkkkkkkky;



import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FiveChessFrame extends JFrame implements MouseListener ,Runnable
{
	//��������
	private int num;
	//�м��
	private int gap;
	//��������
	private int x=0;
	private int y=0;
	//�����������ʾ����˳��
	int arrX[];
	int arrY[];
	//���Ӹ���
	int chessNumber=0;
	//����֮ǰ�¹���ȫ����������,0��ʾ�õ������ӣ�1���ӣ�2����
	int[][]allChess;
	//��ʶ��ǰ�µ��Ǻ��廹�ǰ���
	boolean isBlack=true;
	//�����Ϸ�Ƿ���Լ���
	boolean canPlay=true;
	//������ʾ��Ϣ
	String message="�ڷ�����";
	//�������ӵ�ж���ʱ�䣨�룩
	int maxTime=0;
	//���߳����󵹼�ʱ
	Thread t=new Thread(this);
	//����ڷ����׷���ʣ��ʱ��
	int blackTime=0;
	int whiteTime=0;
	//����˫��ʣ��ʱ�����ʾ��Ϣ
	String blackMessage="������";
	String whiteMessage="������";
	BufferedImage bgImage=null;
	public FiveChessFrame()
	{
		String rownumber=JOptionPane.showInputDialog("��������������(��Χ5~20)��");
        num = Integer.parseInt(rownumber);//�õ�����������
        System.out.print(num);
        while(num<5||num>20)
        {
        	rownumber=JOptionPane.showInputDialog("����������������������(��Χ5~20)��");
        	num = Integer.parseInt(rownumber);
        }
        allChess=new int[num][num];
        arrX=new int [num];
        arrY=new int [num];
        for(int i=0;i<num;i++)
        {
        	for(int j=0;j<num;j++)
        	{
        		allChess[i][j]=0;
        	}
        }
		this.setTitle("gomoku");
		this.setSize(645,484);
		this.setLocationRelativeTo(null);//�������
		this.setResizable(false);//���ô�С�󲻿ɸ���
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//�ڴ�����������
		this.addMouseListener(this);
        
		t.start();
		t.suspend();
		//ˢ����Ļ����ֹ��ʼ��Ϸʱ����
		this.repaint();
		try 
		{
			bgImage=ImageIO.read(new File("D:\\�½��ļ���\\gomoku\\luckkkkkkkky\\background.png"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		this.setVisible(true);
	}
	@Override
	public void paint(Graphics g)
	{
		//˫���弼����ֹ��Ļ��˸
		 BufferedImage bi=new BufferedImage(645,484,BufferedImage.TYPE_INT_ARGB);
		 Graphics g2=bi.createGraphics();
		//���Ʊ���
		super.paint(g2); 
		g2.drawImage(bgImage, 0, 0,this);
		//���������Ϣ
		g2.setColor(Color.black);
		g2.setFont(new Font("����",Font.BOLD,20));
		g2.drawString("��Ϸ��Ϣ:"+message,50,55);
		//���ʱ����Ϣ
		g2.setFont(new Font("����",Font.BOLD,15));
		g2.drawString("�ڷ�ʱ�䣺"+blackMessage,455,70);
		g2.drawString("�׷�ʱ�䣺"+whiteMessage,455,100);
		//��������
		gap=405/(num+1);
		double hue = 158.0;
		double saturation = 0.30;
		double brightness = 0.68;
		float hueFloat = (float) hue;
		float saturationFloat = (float) saturation;
		float brightnessFloat = (float) brightness;
		g2.setColor(Color.getHSBColor(hueFloat, saturationFloat, brightnessFloat));
		g2.fillRect(42, 60, gap*(num+1), gap*(num+1));
		for(int i=0;i<this.num;i++)
		{
			g2.setColor(Color.black);
			((Graphics2D) g2).setStroke(new BasicStroke(1));//��������������ϸ
			if(i==0||i==num-1)
			{
				((Graphics2D) g2).setStroke(new BasicStroke(2));
			}
			g2.drawLine(42+gap,60+(i*gap)+gap,42+(gap*num),60+(i*gap)+gap);//������
			g2.drawLine(42+(i*gap)+gap,60+gap,42+(i*gap)+gap,60+(gap*num));
		}
		//��������
		int chessPieceRadius=(int) (gap*0.85);
		for(int i=0;i<num;i++)
		{
			for(int j=0;j<num;j++)
			{
				if(allChess[i][j]==1)
				{
					g2.setColor(Color.black);
					g2.fillOval(42+gap/2+(gap*i)+1,60+gap/2+(gap*j)+1 , chessPieceRadius, chessPieceRadius);
				}
				if(allChess[i][j]==2)
				{
					g2.setColor(Color.white);
					g2.fillOval(42+gap/2+(gap*i)+1, 60+gap/2+(gap*j)+1, chessPieceRadius, chessPieceRadius);
				}
			}
		}
		g.drawImage(bi,0,0,this);
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("X"+e.getX());//��ȡ�������λ��
		System.out.println("Y"+e.getY());
		if(canPlay)
		{
			x=e.getX();
			y=e.getY();
			if(x>=42+(gap/2)&&x<=42+(gap*(num+1))&&y>60+(gap/2)&&y<=60+(gap*(num+1)))
			{		
				x=(x-42+(gap/2)+1)/gap-1;
				y=(y-60+(gap/2)+1)/gap-1;
				if(allChess[x][y]==0)
				{//��ǰҪ�µ�������ʲô��ɫ
					if(isBlack)
					{
						allChess[x][y]=1;
						isBlack=false;
						message="�ֵ��׷�";
					}
					else
					{
						allChess[x][y]=2;
						isBlack=true;
						message="�ֵ��ڷ�";
					}
					this.repaint();//��ʾ���µ���paint����
					boolean isWin=this.checkWin();
					if(isWin)
					{
						t.suspend();
						JOptionPane.showMessageDialog(this, "��Ϸ������"+(allChess[x][y]==1?"�ڷ�":"�׷�")+"��ʤ");
						canPlay=false;
						message="�ֵ��ڷ�";
					}
					boolean isDogfall=this.checkDogfall();
					if(isDogfall)
					{
						t.suspend();
						JOptionPane.showMessageDialog(this, "��Ϸ����������������ƽ�֡�");
						canPlay=false;
						message="�ֵ��ڷ�";
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this, "��ǰλ���������ӣ����������ӣ�");
				}
			}
		}
		//��� ��ʼ ��ť ���¿�ʼ����Ϸ
		if(e.getX()>=453&&e.getX()<=585&&e.getY()>=133&&e.getY()<=171)
		{
			int result=JOptionPane.showConfirmDialog(this, "�Ƿ����¿�ʼ��Ϸ��");
			if(result==0)
			{
				if(maxTime>0)
				{
					blackMessage= Integer.toString(maxTime);
					whiteMessage= Integer.toString(maxTime);
					t.resume();
				}
				else
				{
					blackMessage="������";
					whiteMessage="������";
				}
				restartGame();
			}
		}
		//��� ���� ��ť ���õ���ʱ
		if(e.getX()>=453&&e.getX()<=585&&e.getY()>=187&&e.getY()<=226)
		{
			String input=JOptionPane.showInputDialog("�������������ʱ�䣨�룩,�������0�������ʱ������:");
			try
			{
				maxTime=Integer.parseInt(input);
				if(maxTime<0)
				{
					JOptionPane.showMessageDialog(this, "��������ȷ����Ϣ,�����Ǹ���");
				}
				if(maxTime==0)
				{
					int result=JOptionPane.showConfirmDialog(this, "������ɣ��Ƿ����¿�ʼ��Ϸ��");
					if(result==0)
					{
						restartGame();
						blackMessage="������";
						whiteMessage="������";
					}
				}
				if(maxTime>0)
				{
					int result=JOptionPane.showConfirmDialog(this, "������ɣ��Ƿ����¿�ʼ��Ϸ��");
					if(result==0)
					{
						restartGame();
						blackMessage= Integer.toString(maxTime);
						whiteMessage= Integer.toString(maxTime);
						t.resume();
					}
				}
			}
			catch(NumberFormatException e1)
			{
				JOptionPane.showMessageDialog(this, "��������ȷ����Ϣ");
			}
		}
		//��� ˵�� ��ť ����˵����Ϸ����Ͳ���
		if(e.getX()>=453&&e.getX()<=585&&e.getY()>=244&&e.getY()<=279)
		{
			JOptionPane.showMessageDialog(this, "��Ϸ����\r\n"
					+ "����˳�򣺺ڷ����£��������ӡ�\r\n"
					+ "���ӵ�λ�ã�ÿһ�����Լ������ӷ������̵����⽻����ϣ�һ��ֻ����һ�����ӡ�\r\n"
					+ "Ŀ�꣺�����������γ������ͬ��ɫ����������һ���ߣ��ᡢ����б�߽Կɣ���һ����ʤ��\r\n"
					+ "ƽ�֣��������������û�дﵽ�������ߵ�ʤ���������������ƽ�ֽ�����");
		}
		//��� ���� ��ť
		if(e.getX()>=453&&e.getX()<=585&&e.getY()>=293&&e.getY()<=330)
		{
			int result=JOptionPane.showConfirmDialog(this, "�Ƿ�ȷ�����䣿");
			if(result==0)
			{
				if(isBlack)
				{
					t.suspend();
					JOptionPane.showMessageDialog(this, "�ڷ��Ѿ����䣬��Ϸ����");
				}
				else
				{
					t.suspend();
					JOptionPane.showMessageDialog(this, "�׷��Ѿ����䣬��Ϸ����");
				}
				canPlay=false;
			}
		}
		//��� ���� ��ť ��ʾ��������߻��߱�д�ĵ�λ��Ϣ
		if(e.getX()>=453&&e.getX()<=585&&e.getY()>=342&&e.getY()<=378)
		{
			JOptionPane.showMessageDialog(this, "����Ϸ����һ������2024�꿴���մ��̶�ʮ�����׳��ݳ���");
		}
		//��� �˳� ��ť ��������
		if(e.getX()>=453&&e.getX()<=585&&e.getY()>=388&&e.getY()<=426)
		{
			JOptionPane.showMessageDialog(this, "��Ϸ�����ˣ����ϴϴ˯����");
			System.exit(0);
		}
		//��� ���� ��ť
		if(e.getX()>=453&&e.getX()<=585&&e.getY()>=435&&e.getY()<=471)
		{
			JOptionPane.showMessageDialog(this, "����");
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public boolean checkWin() 
	{
	    boolean flag = false; // �ж�Ĭ��δ����
	    int color = allChess[x][y];
	    
	    // �жϺ���
	    // ���湲�м�����ͬ��ɫ����������
	    int count = 1;
	    // �жϺ����Ƿ��������������
	    int i = 1;
	    while (x + i < num && color == allChess[x + i][y]) {
	        count++;
	        i++;
	    }
	    i = 1;
	    while (x - i >= 0 && color == allChess[x - i][y]) {
	        count++;
	        i++;
	    }
	    if (count >= 5) {
	        flag = true;
	    }
	    
	    // �ж������Ƿ��������������
	    int count2 = 1;
	    int i2 = 1;
	    while (y + i2 < num && color == allChess[x][y + i2]) {
	        count2++;
	        i2++;
	    }
	    i2 = 1;
	    while (y - i2 >= 0 && color == allChess[x][y - i2]) {
	        count2++;
	        i2++;
	    }
	    if (count2 >= 5) {
	        flag = true;
	    }
	    
	    // �ж�б���Ƿ��������������
	    int count3 = 1;
	    int i3 = 1;
	    while (x + i3 < num && y + i3 < num && color == allChess[x + i3][y + i3]) {
	        count3++;
	        i3++;
	    }
	    i3 = 1;
	    while (x - i3 >= 0 && y - i3 >= 0 && color == allChess[x - i3][y - i3]) {
	        count3++;
	        i3++;
	    }
	    if (count3 >= 5) {
	        flag = true;
	    }
	    
	    int count4 = 1;
	    int i4 = 1;
	    while (x + i4 < num && y - i4 >= 0 && color == allChess[x + i4][y - i4]) {
	        count4++;
	        i4++;
	    }
	    i4 = 1;
	    while (x - i4 >= 0 && y + i4 < num && color == allChess[x - i4][y + i4]) {
	        count4++;
	        i4++;
	    }
	    if (count4 >= 5) {
	        flag = true;
	    }
	    return flag;
	}
	public boolean checkDogfall()
	{
	    for(int m=0;m<num;m++)
	    {
	    	for(int n=0;n<num;n++)
	    	{
	    		if(allChess[n][m]==0)
	    		{
	    			return false;
	    		}
	    	}
	    }
	    return true;
	}
    public void run()
    {
    	//�ж��Ƿ���ʱ�������
    	if(maxTime>0)
    	{
    		while(true)
    		{
    			if(isBlack)
    			{
    				whiteTime=maxTime;
    				blackTime--;
    				if(blackTime==0)
    				{
    					JOptionPane.showMessageDialog(this, "�ڷ���ʱ����Ϸ����");
    					t.suspend();
    				}
    			}
    			else
    			{
    				blackTime=maxTime;
    				whiteTime--;
    				if(whiteTime==0)
    				{
    					JOptionPane.showMessageDialog(this, "�׷���ʱ����Ϸ����");
    					t.suspend();
    				}
    			}
				blackMessage= Integer.toString(blackTime);
				whiteMessage= Integer.toString(whiteTime);
				this.repaint();
    			try {
					Thread.sleep(1000);//��Ϣ1000ms��1��
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }
    public void restartGame()
    {
    	//�������¿�ʼ��Ϸ
		//�������
		 for(int m=0;m<num;m++)
		    {
		    	for(int n=0;n<num;n++)
		    	{
		    		allChess[n][m]=0;
		    	}
		    	arrX[m]=0;
		    	arrY[m]=0;
		    }
		//����Ϸ��Ϣ����ʾ�ûص���ʼλ��
		message="�ڷ�����";
		 //����һ������ĸ�Ϊ�ڷ�
		isBlack=true;
		//�����������
		chessNumber=0;
		blackTime=maxTime;
		whiteTime=maxTime;
		canPlay=true;
		this.repaint();
    }
}
 