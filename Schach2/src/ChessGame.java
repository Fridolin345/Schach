import java.io.IOException;
import java.util.ArrayList;

public class ChessGame {
	
	public static Board mainBoard = new Board();
	
	
	public static void main(String[] args) throws IOException {
		mainBoard.startstellung();
		System.out.println(mainBoard.getAt('a', 1));
		new GUI();
	}
		
}