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
	//保存行数
	private int num;
	//行间隔
	private int gap;
	//落子坐标
	private int x=0;
	private int y=0;
	//保存之前下过的全部棋子坐标,0表示该点无棋子，1黑子，2白子
	int[][]allChess;
	//标识当前下的是黑棋还是白棋
	boolean isBlack=true;
	//标记游戏是否可以继续
	boolean canPlay=true;
	//保存显示信息
	String message="黑方先行";
	BufferedImage bgImage=null;
	public FiveChessFrame()
	{
		String rownumber=JOptionPane.showInputDialog("输入五子棋行数(范围5~20)：");
        num = Integer.parseInt(rownumber);//得到五子棋行数
        System.out.print(num);
        while(num<5||num>20)
        {
        	rownumber=JOptionPane.showInputDialog("行数错误，输入五子棋行数(范围5~20)：");
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
		this.setLocationRelativeTo(null);//窗体居中
		this.setResizable(false);//设置大小后不可更改
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//在窗体加入监听器
		this.addMouseListener(this);
		
		try 
		{
			bgImage=ImageIO.read(new File("D:\\新建文件夹\\gomoku\\luckkkkkkkky\\background.png"));
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
		//双缓冲技术防止屏幕闪烁
		 BufferedImage bi=new BufferedImage(645,484,BufferedImage.TYPE_INT_ARGB);
		 Graphics g2=bi.createGraphics();
		//绘制背景
		super.paint(g); 
		g2.drawImage(bgImage, 0, 0,this);
		//输出标题信息
		g2.setColor(Color.black);
		g2.setFont(new Font("黑体",Font.BOLD,20));
		g2.drawString("游戏信息:"+message,50,55);
		//输出时间信息
		g2.setFont(new Font("宋体",Font.BOLD,15));
		g2.drawString("黑方时间：无限制",455,70);
		g2.drawString("白方时间：无限制",455,100);
		//绘制棋盘
		Graphics2D g2d=(Graphics2D) g;
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
			((Graphics2D) g2).setStroke(new BasicStroke(1));//设置棋盘线条粗细
			if(i==0||i==num-1)
			{
				((Graphics2D) g2).setStroke(new BasicStroke(2));
			}
			g2.drawLine(42+gap,60+(i*gap)+gap,42+(gap*num),60+(i*gap)+gap);//画横线
			g2.drawLine(42+(i*gap)+gap,60+gap,42+(i*gap)+gap,60+(gap*num));
		}
		//绘制落子
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
		System.out.println("X"+e.getX());//获取鼠标点击的位置
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
				{//当前要下的棋子是什么颜色
					if(isBlack)
					{
						allChess[x][y]=1;
						isBlack=false;
						message="轮到白方";
					}
					else
					{
						allChess[x][y]=2;
						isBlack=true;
						message="轮到黑方";
					}
					this.repaint();//表示重新调用paint方法
					boolean isWin=this.checkWin();
					if(isWin)
					{
						JOptionPane.showMessageDialog(this, "游戏结束，"+(allChess[x][y]==1?"黑方":"白方")+"获胜");
						canPlay=false;
						message="轮到黑方";
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this, "当前位置已有棋子，请重新落子：");
				}
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
	
	public boolean checkWin() 
	{
	    boolean flag = false; // 判断默认未结束
	    int color = allChess[x][y];
	    
	    // 判断横向
	    // 保存共有几个相同颜色的棋子相连
	    int count = 1;
	    // 判断横向是否有五个棋子相连
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
	    
	    // 判断纵向是否有五个棋子相连
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
	    
	    // 判断斜向是否有五个棋子相连
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
	
}
 