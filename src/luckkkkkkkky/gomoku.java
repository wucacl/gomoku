package luckkkkkkkky;
import javax.swing.JFrame;
import java.awt.Toolkit;
public class gomoku 
{
	public static void main(String[] args)
	{
		JFrame frame =new JFrame();
		frame.setTitle("gomoku");
		frame.setSize(1000, 700);
		frame.setVisible(true);
		frame.setResizable(true);//设置窗口大小是否可以改变
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭窗口时结束进程
		frame.setLocationRelativeTo(null);//居中
		int width=Toolkit.getDefaultToolkit().getScreenSize().width;
		int height=Toolkit.getDefaultToolkit().getScreenSize().height;//得到屏幕高度宽度保证窗口在正中央
		//frame.setLocation((width-600)/2 , (height-400)/2);
	}
	
}
