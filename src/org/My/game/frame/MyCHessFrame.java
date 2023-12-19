package org.My.game.frame;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import javax.imageio.ImageIO;

public class MyCHessFrame extends JFrame
{
	public MyCHessFrame() 
	{
		this.setTitle("gomoku");
		this.setLocationRelativeTo(null);
		this.setSize(1000,20);
		this.setVisible(true);
	}
	public void paint(Graphics g)
	{
		BufferedImage image=null;
		try {
			image = ImageIO.read(new File("C:/Users/Alf乂/Desktop/图/t2.jpg"));
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//可能会读不出文件
		g.drawImage(image,0,0,this);
	}
}
