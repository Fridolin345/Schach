package de.schach;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;


public class DrawField extends JPanel
{

    public static final Color WHITE_FIELD_COLOR = new Color( 235, 235, 208 );
    public static final Color BLACK_FIELD_COLOR = new Color( 119, 148, 85 );
    public static final Color HIGHLIGHTED_FIELD_COLOR = new Color( 250, 200, 15 );

    private boolean isWhiteOnBot = true;
    private Position highlighted = null;

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

    private static boolean[] possMovesField = new boolean[8 * 8];
    RenderingHints hints = new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
    private static Position moveStartpos = null;

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
        highlighted = position;

        if ( moveStartpos == null )
        { //Keine Figur ausgewählt zum fahren
            for ( int i = 0; i < possMovesField.length; i++ )
            {
                possMovesField[i] = false;
            }
            if ( getBoard().isPieceAt( position ) )
            {
                Set<Position> pMoves = new HashSet<>();
                //pMoves = board.getPiece(position).getPieceType().getAllPossibleMoves(position, board);
                pMoves.removeAll( pMoves );
                pMoves.add( new Position( position.getRow() - 1, position.getColumn() ) );

                moveStartpos = position;

                for ( Position p : pMoves )
                {
                    possMovesField[p.getRow() * 8 + p.getColumn()] = true;
                }
            }
        }
        else
        {
            if ( getBoard().pieceColorAt( position ) == )
            {

            }
            else if ( possMovesField[position.getRow() * 8 + position.getColumn()] )
            {
                getBoard().move( moveStartpos.getRow(), moveStartpos.getColumn(), position.getRow(), position.getColumn() );
            }
            else
            {
                for ( int i = 0; i < possMovesField.length; i++ )
                {
                    possMovesField[i] = false;
                }
                moveStartpos = null;
            }
        }
        repaint();
    }

    public void move( Position from, Position to )
    {
        Board.getInstance().movePiece( isWhiteOnBot ? from : from.inverted(), to );
    }

    public Vector getOffensiveDirection( PieceColor color )
    {
        return isWhiteOnBot == ( color == PieceColor.WHITE ) ? new Vector( 0, -1 ) : new Vector( 0, 1 );
    }

    public void drawPossibleMoves( Position position, Board board )
    {

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
        drawPossibleMoves( graphics2D );
    }

    private void drawBoard( Graphics2D g )
    {
        boolean white = isWhiteOnBot;
        for ( int y = 0; y < 8; y++ )
        {
            for ( int x = 0; x < 8; x++ )
            {
                g.setColor( white ? WHITE_FIELD_COLOR : BLACK_FIELD_COLOR );
                if ( highlighted != null && highlighted.getScreenX() == x && highlighted.getScreenY() == y )
                    g.setColor( HIGHLIGHTED_FIELD_COLOR );
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

    private void drawPossibleMoves( Graphics2D g )
    {
        if ( moveStartpos != null )
        { //Methode muss nur wenn nötig aufgerufen werden... eigentlich
            for ( int row = 0; row < 8; row++ )
            {
                for ( int col = 0; col < 8; col++ )
                {
                    if ( possMovesField[row * 8 + col] )
                    {
                        g.setRenderingHints( hints );
                        g.setStroke( new BasicStroke( 5 ) );
                        g.setColor( new Color( 100, 100, 100, 150 ) );
                        g.drawOval( col * 64, row * 64, 64, 64 );
                    }
                }
            }
        }
    }

    private void forEachCell( Consumer<Position> forEach )
    {
        Position start = Position.ofScreen( 0, 0 );
        Position end = Position.ofScreen( 7, 7 );
        start.iterateTo( end, forEach );
    }

}
