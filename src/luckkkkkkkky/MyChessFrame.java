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
		this.setResizable(false);//���ô��ڴ�С�Ƿ���Ըı�
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//�رմ���ʱ��������
		this.setLocationRelativeTo(null);//����
		//int width=Toolkit.getDefaultToolkit().getScreenSize().width;
		//int height=Toolkit.getDefaultToolkit().getScreenSize().height;//�õ���Ļ�߶ȿ�ȱ�֤������������
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
    		image=ImageIO.read(new File("C:\\Users\\Alf�V\\Desktop\\ͼ\\t3.jpg"));
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
