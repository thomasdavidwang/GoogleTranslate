import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ImageProcesser {
	int[][][] read;
	int w;
	int h;
	ArrayList<int[][]> draw;
	int[][] global;
	int[][] used;
	
	public ImageProcesser() throws IOException{
		BufferedImage img = ImageIO.read(new File("image.jpg"));
		w = img.getWidth();
		h = img.getHeight();
		used=new int[w][h];
		read = convert(img);
		draw=new ArrayList<int[][]>();
		
		for (int i=0; i<w; i++){
			for (int j=0; j<h; j++){
				if (used[i][j]==0){
					boolean dark = true;
					for (int k=0; k<3; k++){
						if (read[i][j][k]>30){dark=false;}
					}
					if (dark){
						System.out.println("checking for region at "+i+","+j);
						int[] sp={i,j};
						global=new int[w][h];
						create(read[i][j],read[i][j],sp,90,60);
						draw.add(global);
					}
				}
			}
		}
		System.out.println(draw.size());
	}
	
	private int[][][] convert(BufferedImage image) {
		byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		boolean alpha = image.getAlphaRaster() != null;
		int off = alpha ? 1 : 0;
		int[][][] result = new int[w][h][3];
		for (int pixel = 0; pixel < pixels.length; pixel += 3+off) {
		    result[pixel/(3+off)%w][pixel/(3+off)/w][2]=((int) pixels[pixel+off] & 0xff);
		    result[pixel/(3+off)%w][pixel/(3+off)/w][1]=((int) pixels[pixel+off+1] & 0xff);
		    result[pixel/(3+off)%w][pixel/(3+off)/w][0]=((int) pixels[pixel+off+2] & 0xff);
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	private void create(int[] init, int[] ref, int[] here, int initerr, int referr){
		if (global[here[0]][here[1]]==0){
			boolean checkinit = true;
			for (int i=0; i<3; i++){
				if (Math.abs(init[i]-read[here[0]][here[1]][i])>initerr){checkinit=false;}
			}
			boolean checkref = true;
			for (int i=0; i<3; i++){
				if (Math.abs(ref[i]-read[here[0]][here[1]][i])>referr){checkref=false;}
			}
			if (checkinit && checkref){
				//System.out.println(""+init[0]+","+init[1]+","+init[2]+" is close to "+read[here[0]][here[1]][0]+","+read[here[0]][here[1]][1]+","+read[here[0]][here[1]][2]);
				global[here[0]][here[1]]=1;
				used[here[0]][here[1]]=1;
				for (int i=-1; i<2; i++){
					if (0<=here[0]+i && here[0]+i<w){
						for (int j=-1; j<2; j++){
							if (0<=here[1]+j && here[1]+j<h){
								if (!(i==0&&j==0)){
									int[] place = {here[0]+i,here[1]+j};
									create(init,read[here[0]][here[1]],place,initerr,referr);
								}
							}
						}
					}
				}
			}
		}
	}
}
