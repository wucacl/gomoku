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

public class FiveChessFrame extends JFrame implements MouseListener 
{
	//��������
	private int num;
	//�м��
	private int gap;
	//��������
	private int x=0;
	private int y=0;
	//����֮ǰ�¹���ȫ����������,0��ʾ�õ������ӣ�1���ӣ�2����
	int[][]allChess;
	//��ʶ��ǰ�µ��Ǻ��廹�ǰ���
	boolean isBlack=true;
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
		//���Ʊ���
		super.paint(g); 
		g.drawImage(bgImage, 0, 0,this);
		//���������Ϣ
		g.setFont(new Font("����",Font.BOLD,20));
		g.drawString("��Ϸ��Ϣ",50,55);
		//���ʱ����Ϣ
		g.setFont(new Font("����",Font.BOLD,15));
		g.drawString("�ڷ�ʱ�䣺������",455,70);
		g.drawString("�׷�ʱ�䣺������",455,100);
		//��������
		Graphics2D g2d=(Graphics2D) g;
		gap=405/(num+1);
		double hue = 158.0;
		double saturation = 0.30;
		double brightness = 0.68;
		float hueFloat = (float) hue;
		float saturationFloat = (float) saturation;
		float brightnessFloat = (float) brightness;
		g2d.setColor(Color.getHSBColor(hueFloat, saturationFloat, brightnessFloat));
		g2d.fillRect(42, 60, gap*(num+1), gap*(num+1));
		for(int i=0;i<this.num;i++)
		{
			g2d.setColor(Color.black);
			g2d.setStroke(new BasicStroke(1));//��������������ϸ
			if(i==0||i==num-1)
			{
				g2d.setStroke(new BasicStroke(2));
			}
			g.drawLine(42+gap,60+(i*gap)+gap,42+(gap*num),60+(i*gap)+gap);//������
			g.drawLine(42+(i*gap)+gap,60+gap,42+(i*gap)+gap,60+(gap*num));
		}
		//��������
		int chessPieceRadius=(int) (gap*0.85);
		for(int i=0;i<num;i++)
		{
			for(int j=0;j<num;j++)
			{
				if(allChess[i][j]==1)
				{
					g2d.setColor(Color.black);
					g.fillOval(42+gap/2+(gap*i)+1,60+gap/2+(gap*j)+1 , chessPieceRadius, chessPieceRadius);
				}
				if(allChess[i][j]==2)
				{
					g2d.setColor(Color.white);
					g.fillOval(42+gap/2+(gap*i)+1, 60+gap/2+(gap*j)+1, chessPieceRadius, chessPieceRadius);
				}
			}
		}
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
		x=e.getX();
		y=e.getY();
		if(x>=42+(gap/2)&&x<=42+(gap*(num+1))&&y>60+(gap/2)&&y<=60+(gap*(num+1)))
		{
			this.repaint();//��ʾ���µ���pait����
			int i,j;
			i=(x-42+(gap/2)+1)/gap-1;
			j=(y-60+(gap/2)+1)/gap-1;
			if(allChess[i][j]==0)
			{
				if(isBlack)
				{
					allChess[i][j]=1;
					isBlack=false;
				}
				else
				{
					allChess[i][j]=2;
					isBlack=true;
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this, "��ǰλ���������ӣ����������ӣ�");
			}
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

}
