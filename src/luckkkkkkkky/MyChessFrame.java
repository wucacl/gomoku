package luckkkkkkkky;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MyChessFrame extends JFrame implements MouseListener{
	MyChessFrame()
	{
		this.setTitle("gomoku");
		this.setSize(4096, 2304);
		this.setResizable(false);//设置窗口大小是否可以改变
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭窗口时结束进程
		this.setLocationRelativeTo(null);//居中
		//int width=Toolkit.getDefaultToolkit().getScreenSize().width;
		//int height=Toolkit.getDefaultToolkit().getScreenSize().height;//得到屏幕高度宽度保证窗口在正中央
		//frame.setLocation((width-600)/2 , (height-400)/2);
		
		this.addMouseListener(this);
		
		this.setVisible(true);
	}
    @Override
    public void mouseClicked(MouseEvent e) {

    }
    public void paint(Graphics g)
    {
    	BufferedImage image=null;
    	try {
    		image=ImageIO.read(new File("C:\\Users\\Alf乂\\Desktop\\图\\t3.jpg"));
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    	}
    			
    	g.drawImage(image,10,10,this);
    }
    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
}
