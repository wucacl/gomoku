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
		frame.setResizable(true);//���ô��ڴ�С�Ƿ���Ըı�
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//�رմ���ʱ��������
		frame.setLocationRelativeTo(null);//����
		int width=Toolkit.getDefaultToolkit().getScreenSize().width;
		int height=Toolkit.getDefaultToolkit().getScreenSize().height;//�õ���Ļ�߶ȿ�ȱ�֤������������
		//frame.setLocation((width-600)/2 , (height-400)/2);
	}
	
}
