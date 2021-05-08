package de.schach;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class DrawField extends JPanel
{

    boolean isWhiteOnBot = false;

//	static LinkedList<de.schach.Piece> ps=new LinkedList<>();

    BufferedImage all = ImageIO.read( new File( "assets\\chess.png" ) );
    Image imgs[] = new Image[12];
    int ind = 0;
    MouseListener m = new MouseListener()
    {
        @Override
        public void mouseClicked( MouseEvent e )
        {
        }

        @Override
        public void mousePressed( MouseEvent e )
        {

            //System.out.println( GetField( (int) ( e.getX() / 64 ), (int) ( e.getY() ) / 64 ) );
        }

        @Override
        public void mouseReleased( MouseEvent e )
        {

        }

        @Override
        public void mouseEntered( MouseEvent e )
        {

        }

        @Override
        public void mouseExited( MouseEvent e )
        {

        }
    };


    public DrawField() throws IOException
    {
        for ( int y = 0; y < 400; y += 200 )
        {   //Hier wird das Bild in die einzelnen Figuren unterst�ckelt
            for ( int x = 0; x < 1200; x += 200 )
            {
                imgs[ind] = all.getSubimage( x, y, 200, 200 ).getScaledInstance( 64, 64, BufferedImage.SCALE_SMOOTH );
                ind++;
            }
        }
        this.addMouseListener( m );
    }

    public void paint( Graphics g )
    {
        //Schachfelder (schwarz/weiß) zeichnen
        Graphics2D g2 = (Graphics2D) g;
        boolean white;
        if ( isWhiteOnBot )
        {
            white = true;
        }
        else
        {
            white = false;
        }
        for ( int y = 0; y < 8; y++ )
        { //
            for ( int x = 0; x < 8; x++ )
            {
                if ( white )
                {
                    g.setColor( new Color( 235, 235, 208 ) );
                }
                else
                {
                    g.setColor( new Color( 119, 148, 85 ) );
                }
                g.fillRect( x * 64, y * 64, 64, 64 );
                white = !white;
            }
            white = !white;
        }

        //Figuren zeichnen:
        for ( int i = 0; i < 8; i++ )
        {
            for ( int j = 0; j < 8; j++ )
            {
                int drawX, drawY;
                if ( isWhiteOnBot )
                {
                    drawX = i;
                    drawY = j + 7;
                }
                else
                {
                    drawX = 7 - i;
                    drawY = j;
                }
                Position position = Position.ofScreen( drawX, drawY );
                if ( Board.getInstance().isPieceAt( position ) )
                {
                    g.drawImage( imgs[Board.getInstance().getPiece( position ).getSpriteIndex()], drawX * 64, drawY * 64, this );
                }
            }
        }
        //ArrayList<Integer[]> myDrawBoard = ChessGame.mainBoard.PrepareToDraw();

        //for ( Integer[] piece : myDrawBoard )
        //{
        //g.drawImage( imgs[piece[0]], piece[1] * 64, piece[2] * 64, this ); //NOCH FALSCH
        //}
    }

    private Point GetField( int drawX, int drawY )
    {
        return isWhiteOnBot ? new Point( drawX, 7 - drawY ) : new Point( 7 - drawX, drawY );
    }


    private Point TransformToField( int drawX, int drawY )
    {
        return isWhiteOnBot ? new Point( drawX, drawY + 7 ) : new Point( 7 - drawX, drawY );
    }
}
