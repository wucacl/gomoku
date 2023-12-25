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
	private int num;
	BufferedImage bgImage=null;
	public FiveChessFrame()
	{
		String rownumber=JOptionPane.showInputDialog("��������������(��Χ5~21)��");
        num = Integer.parseInt(rownumber);//�õ�����������
        System.out.print(num);
        while(num<5||num>21)
        {
        	rownumber=JOptionPane.showInputDialog("����������������������(��Χ5~21)��");
        	num = Integer.parseInt(rownumber);
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
		/*g.drawLine(42,60,447,60);
		g.drawLine(42,60,42,465);
		g.drawLine(42,465,447,465);
		g.drawLine(447,60,447,465);*/
		int gap=405/num;
		double hue = 138.0;
		double saturation = 0.38;
		double brightness = 0.68;
		float hueFloat = (float) hue;
		float saturationFloat = (float) saturation;
		float brightnessFloat = (float) brightness;
		g2d.setColor(Color.getHSBColor(hueFloat, saturationFloat, brightnessFloat));
		g2d.fillRect(42, 60, gap*num, gap*num);
		for(int i=0;i<=this.num;i++)
		{
			g2d.setColor(Color.black);
			g2d.setStroke(new BasicStroke(2));
			g.drawLine(42,60+(i*gap),42+(gap*num),60+(i*gap));//������
			g.drawLine(42+(i*gap),60,42+(i*gap),60+(gap*num));
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
