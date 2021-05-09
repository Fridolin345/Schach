package de.schach;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;


public class DrawField extends JPanel
{

    public static final Color WHITE_FIELD_COLOR = new Color( 235, 235, 208 );
    public static final Color BLACK_FIELD_COLOR = new Color( 119, 148, 85 );

    private boolean isWhiteOnBot = true;

    private BufferedImage spritesheet = ImageIO.read( new File( "assets\\chess.png" ) );
    private Image sprites[] = new Image[12];
    private MouseListener mouse = new MouseAdapter()
    {
        @Override
        public void mousePressed( MouseEvent e )
        {
            Position position = Position.ofScreen( e.getX() / 64, e.getY() / 64 );
            fieldClicked( position, Optional.ofNullable( getBoard().getPiece( position ) ) );
        }
    };

    public DrawField() throws IOException
    {
        int ind = 0;
        for ( int y = 0; y < 400; y += 200 )
        {   //Hier wird das Bild in die einzelnen Figuren unterstückelt
            for ( int x = 0; x < 1200; x += 200 )
            {
                sprites[ind] = spritesheet.getSubimage( x, y, 200, 200 ).getScaledInstance( 64, 64, BufferedImage.SCALE_SMOOTH );
                ind++;
            }
        }
        this.addMouseListener( mouse );
    }

    public Board getBoard()
    {
        return isWhiteOnBot ? Board.getInstance() : Board.getInstance().getInvertedCopy();
    }

    public void fieldClicked( Position position, Optional<Piece> piece )
    {
        System.out.println( position + " -> " + piece.map( Enum::name ).orElse( "EMPTY FIELD" ) );
    }

    public void setWhiteOnBot( boolean whiteOnBot )
    {
        isWhiteOnBot = whiteOnBot;
    }

    public void paint( Graphics g )
    {
        Graphics2D graphics2D = (Graphics2D) g;
        drawBoard( graphics2D );
        drawFigures( graphics2D );
    }

    private void drawBoard( Graphics2D g )
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

    private void drawFigures( Graphics2D g )
    {
        //Figuren zeichnen:
        forEachCell( pos ->
        {
            if ( getBoard().isPieceAt( pos ) )
            {
                Image img = sprites[getBoard().getPiece( pos ).getSpriteIndex()];
                g.drawImage( img, pos.getScreenX() * 64, pos.getScreenY() * 64, this );
            }
        } );
    }

    private void forEachCell( Consumer<Position> forEach )
    {
        Position start = Position.ofScreen( 0, 0 );
        Position end = Position.ofScreen( 8, 8 );
        start.iterateTo( end, forEach );
    }

}
