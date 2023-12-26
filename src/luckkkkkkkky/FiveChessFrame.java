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
	//保存行数
	private int num;
	//行间隔
	private int gap;
	//落子坐标
	private int x=0;
	private int y=0;
	//用两个数组表示落子顺序
	int arrX[];
	int arrY[];
	//落子个数
	int chessNumber=0;
	//保存之前下过的全部棋子坐标,0表示该点无棋子，1黑子，2白子
	int[][]allChess;
	//标识当前下的是黑棋还是白棋
	boolean isBlack=true;
	//标记游戏是否可以继续
	boolean canPlay=true;
	//保存显示信息
	String message="黑方先行";
	//保存最多拥有多少时间（秒）
	int maxTime=0;
	//用线程类左倒计时
	Thread t=new Thread(this);
	//保存黑方、白方的剩余时间
	int blackTime=0;
	int whiteTime=0;
	//保存双方剩余时间的显示信息
	String blackMessage="无限制";
	String whiteMessage="无限制";
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
		this.setLocationRelativeTo(null);//窗体居中
		this.setResizable(false);//设置大小后不可更改
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//在窗体加入监听器
		this.addMouseListener(this);
        
		t.start();
		t.suspend();
		//刷新屏幕，防止开始游戏时黑屏
		this.repaint();
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
		super.paint(g2); 
		g2.drawImage(bgImage, 0, 0,this);
		//输出标题信息
		g2.setColor(Color.black);
		g2.setFont(new Font("黑体",Font.BOLD,20));
		g2.drawString("游戏信息:"+message,50,55);
		//输出时间信息
		g2.setFont(new Font("宋体",Font.BOLD,15));
		g2.drawString("黑方时间："+blackMessage,455,70);
		g2.drawString("白方时间："+whiteMessage,455,100);
		//绘制棋盘
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
						t.suspend();
						JOptionPane.showMessageDialog(this, "游戏结束，"+(allChess[x][y]==1?"黑方":"白方")+"获胜");
						canPlay=false;
						message="轮到黑方";
					}
					boolean isDogfall=this.checkDogfall();
					if(isDogfall)
					{
						t.suspend();
						JOptionPane.showMessageDialog(this, "游戏结束，棋盘已满，平局。");
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
		//点击 开始 按钮 重新开始新游戏
		if(e.getX()>=453&&e.getX()<=585&&e.getY()>=133&&e.getY()<=171)
		{
			int result=JOptionPane.showConfirmDialog(this, "是否重新开始游戏？");
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
					blackMessage="无限制";
					whiteMessage="无限制";
				}
				restartGame();
			}
		}
		//点击 设置 按钮 设置倒计时
		if(e.getX()>=453&&e.getX()<=585&&e.getY()>=187&&e.getY()<=226)
		{
			String input=JOptionPane.showInputDialog("请输入下棋最大时间（秒）,如果输入0则代表无时间限制:");
			try
			{
				maxTime=Integer.parseInt(input);
				if(maxTime<0)
				{
					JOptionPane.showMessageDialog(this, "请输入正确的信息,不能是负数");
				}
				if(maxTime==0)
				{
					int result=JOptionPane.showConfirmDialog(this, "设置完成，是否重新开始游戏？");
					if(result==0)
					{
						restartGame();
						blackMessage="无限制";
						whiteMessage="无限制";
					}
				}
				if(maxTime>0)
				{
					int result=JOptionPane.showConfirmDialog(this, "设置完成，是否重新开始游戏？");
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
				JOptionPane.showMessageDialog(this, "请输入正确的信息");
			}
		}
		//点击 说明 按钮 用来说明游戏规则和操作
		if(e.getX()>=453&&e.getX()<=585&&e.getY()>=244&&e.getY()<=279)
		{
			JOptionPane.showMessageDialog(this, "游戏规则：\r\n"
					+ "落子顺序：黑方先下，轮流落子。\r\n"
					+ "落子的位置：每一方将自己的棋子放在棋盘的任意交叉点上，一次只能下一个棋子。\r\n"
					+ "目标：先在棋盘上形成五个相同颜色的棋子连成一条线（横、竖、斜线皆可）的一方获胜。\r\n"
					+ "平局：如果棋盘填满而没有达到五子连线的胜利条件，则比赛以平局结束。");
		}
		//点击 认输 按钮
		if(e.getX()>=453&&e.getX()<=585&&e.getY()>=293&&e.getY()<=330)
		{
			int result=JOptionPane.showConfirmDialog(this, "是否确认认输？");
			if(result==0)
			{
				if(isBlack)
				{
					t.suspend();
					JOptionPane.showMessageDialog(this, "黑方已经认输，游戏结束");
				}
				else
				{
					t.suspend();
					JOptionPane.showMessageDialog(this, "白方已经认输，游戏结束");
				}
				canPlay=false;
			}
		}
		//点击 关于 按钮 显示程序的作者或者编写的单位信息
		if(e.getX()>=453&&e.getX()<=585&&e.getY()>=342&&e.getY()<=378)
		{
			JOptionPane.showMessageDialog(this, "本游戏作者一定能在2024年看到苏打绿二十周年首场演唱会");
		}
		//点击 退出 按钮 结束程序
		if(e.getX()>=453&&e.getX()<=585&&e.getY()>=388&&e.getY()<=426)
		{
			JOptionPane.showMessageDialog(this, "游戏结束了，早点洗洗睡哈。");
			System.exit(0);
		}
		//点击 悔棋 按钮
		if(e.getX()>=453&&e.getX()<=585&&e.getY()>=435&&e.getY()<=471)
		{
			JOptionPane.showMessageDialog(this, "悔棋");
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
    	//判断是否有时间的限制
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
    					JOptionPane.showMessageDialog(this, "黑方超时，游戏结束");
    					t.suspend();
    				}
    			}
    			else
    			{
    				blackTime=maxTime;
    				whiteTime--;
    				if(whiteTime==0)
    				{
    					JOptionPane.showMessageDialog(this, "白方超时，游戏结束");
    					t.suspend();
    				}
    			}
				blackMessage= Integer.toString(blackTime);
				whiteMessage= Integer.toString(whiteTime);
				this.repaint();
    			try {
					Thread.sleep(1000);//休息1000ms即1秒
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }
    public void restartGame()
    {
    	//现在重新开始游戏
		//清空棋盘
		 for(int m=0;m<num;m++)
		    {
		    	for(int n=0;n<num;n++)
		    	{
		    		allChess[n][m]=0;
		    	}
		    	arrX[m]=0;
		    	arrY[m]=0;
		    }
		//将游戏信息的显示该回到开始位置
		message="黑方先行";
		 //将下一步下棋的改为黑方
		isBlack=true;
		//下棋个数归零
		chessNumber=0;
		blackTime=maxTime;
		whiteTime=maxTime;
		canPlay=true;
		this.repaint();
    }
}
 