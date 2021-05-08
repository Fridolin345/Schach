

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class DrawField extends JPanel{

//	static LinkedList<Piece> ps=new LinkedList<>();
		
	BufferedImage all=ImageIO.read(new File("C:\\Users\\dempf\\Downloads\\chess.png"));
	Image imgs[]=new Image[12];
	int ind = 0;
	
	public DrawField() throws IOException {
		for(int y=0;y<400;y+=200){   //Hier wird das Bild in die einzelnen Figuren unterstückelt
			for(int x=0;x<1200;x+=200) {
				imgs[ind]=all.getSubimage(x, y, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH); 
				ind++;
			}    
		}

	}


	public void paint(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;
		boolean white=true;
		for(int y= 0;y<8;y++){ //Board
			for(int x= 0;x<8;x++){
				if(white){
					g.setColor(new Color(235,235, 208));
				}else{
					g.setColor(new Color(119, 148, 85));
				}
				g.fillRect(x*64, y*64, 64, 64);
				white=!white;
			}
			white=!white;
		}
		
		ArrayList<Integer[]> myDrawBoard = ChessGame.mainBoard.PrepareToDraw();
		
		for(Integer[] piece : myDrawBoard){
			g.drawImage(imgs[piece[0]], piece[1]*64, piece[2]*64, this);
		}
	}
	
	

	
	


}
