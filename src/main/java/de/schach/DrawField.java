package de.schach;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;


public class DrawField extends JPanel
{

    public static final Color WHITE_FIELD_COLOR = new Color( 235, 235, 208 );
    public static final Color BLACK_FIELD_COLOR = new Color( 119, 148, 85 );

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
        drawBoard( g );
        drawFigures( g );

    }

    private void drawBoard( Graphics g )
    {
        boolean white = isWhiteOnBot;
        for ( int y = 0; y < 8; y++ )
        {
            for ( int x = 0; x < 8; x++ )
            {
                g.setColor( white ? WHITE_FIELD_COLOR : BLACK_FIELD_COLOR );
                g.fillRect( x * 64, y * 64, 64, 64 );
                white = !white;
            }
            white = !white;
        }
    }

    private void drawFigures( Graphics g )
    {
        //Figuren zeichnen:
        forEachCell( pos ->
        {
            if ( Board.getInstance().isPieceAt( pos ) )
            {
                g.drawImage( imgs[Board.getInstance().getPiece( pos ).getSpriteIndex()], pos.getScreenX() * 64, pos.getScreenY() * 64, this );
            }
        } );
    }

    private void forEachCell( Consumer<Position> forEach )
    {
        for ( int i = 0; i < 8; i++ )
        {
            for ( int j = 0; j < 8; j++ )
            {
                forEach.accept( invertPlayingSide( Position.ofScreen( i, j ) ) );
            }
        }
    }

    private Position invertPlayingSide( Position position )
    {
        return isWhiteOnBot ? Position.ofScreen( position.getScreenX(), 7 - position.getScreenY() ) : Position.ofScreen( position.getScreenX(), position.getScreenY() );
    }

}
