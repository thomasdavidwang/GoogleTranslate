import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.TimerTask;
import java.util.concurrent.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
 
public class DisplayImage extends JFrame{
	String path;
	ImagePanel panel;
	Container content;
	BufferedImage picture;
	int w;
	int h;
	
	public DisplayImage(String title, int width, int height){
		setTitle(title);
		panel = new ImagePanel();
		panel.setBackground(Color.WHITE);
		
		w=width;
		h=height;
		picture = new BufferedImage(8*w,8*h,BufferedImage.TYPE_INT_ARGB);
		
		setMinimumSize(new Dimension(picture.getWidth()+100, picture.getHeight() + 100));
		setMaximumSize(new Dimension(picture.getWidth()+100, picture.getHeight() + 100));
		setResizable(true);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		content = getContentPane();
		content.setLayout(new BorderLayout());
		content.setBackground(Color.BLACK);
		content.add(panel, BorderLayout.CENTER);
		
		setVisible(true);
	}
	
	public BufferedImage ReadArray(int[][] array){
		BufferedImage grid = new BufferedImage(8*w, 8*h, BufferedImage.TYPE_INT_ARGB);
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				Color useColor=Color.white;
				if (array[i][j]==1){useColor=Color.black;}
				for(int k=0;k<8;k++){
					for(int l=0;l<8;l++){grid.setRGB(8*i+k, 8*j+l, useColor.getRGB());
					}
				}
			}
		}
		return grid;
	}
	
	public BufferedImage ReadArray3d(int[][][] array){
		BufferedImage grid = new BufferedImage(8*w, 8*h, BufferedImage.TYPE_INT_ARGB);
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				Color useColor = new Color(array[i][j][0] << 16 | array[i][j][1] << 8 | array[i][j][2]);
				for(int k=0;k<8;k++){
					for(int l=0;l<8;l++){grid.setRGB(8*i+k, 8*j+l, useColor.getRGB());
					}
				}
			}
		}
		return grid;
	}
	
	public class ImagePanel extends JPanel{
		protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(picture, 0, 0, this);          
	    }
	}
	
	public void repaint(){
		super.repaint();
		panel.repaint();
	}
	
	public static void main(String[] args) throws IOException{
		/*
		double start=10;
		int upd=20000;
		int buffer=20;
		ArrayTest test = new ArrayTest(n,start,start/upd);
		*/
		
		ImageProcesser test = new ImageProcesser();
		DisplayImage window = new DisplayImage("Test",test.w,test.h);
		//window.picture=window.ReadArray3d(test.read);
		window.picture=window.ReadArray(test.draw.get(0));
		window.repaint();
		
		int n=test.draw.size();
		
		/*
		for (int i=0; i<test.w; i++){
			for (int j=0; j<test.h; j++){
				if (test.draw.get(0)[i][j]==1){
					System.out.println(""+i+","+j);
				}
			}
		}
		*/
		
		for (int i=0;i<200*n;i++){
			window.picture=window.ReadArray(test.draw.get(i%n));
			window.repaint();
	    	
			try {
			Thread.sleep(200);
			} catch (InterruptedException e){
			e.printStackTrace();
			}
		}
	}
}
