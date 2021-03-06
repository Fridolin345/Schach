package de.schach.gui;

import de.schach.board.*;
import de.schach.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;


public class DrawField extends JPanel
{

    //Boardvariables
    public static final Color WHITE_FIELD_COLOR = new Color( 235, 235, 208 );
    public static final Color BLACK_FIELD_COLOR = new Color( 119, 148, 85 );
    public static final Color HIGHLIGHTED_FIELD_COLOR = new Color( 250, 200, 15 );
    public static final Color HIGHLIGHTED_ENEMY_CIRCLE = new Color( 255, 50, 50, 150 );
    public static final Color HIGHLIGHTED_MOVE_CIRCLE = new Color( 100, 100, 100, 150 );

    private Position highlighted = null;

    private Board currentGameBoard;
    private Board currentViewBoard;

    //chess Piece-images
    private BufferedImage spritesheet = ImageIO.read( new File( "assets\\chess.png" ) );
    private Image sprites[] = new Image[12];

    //Piece moving
    private static boolean[] possMovesField = new boolean[8 * 8];
    private RenderingHints hints = new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
    private static Position moveStartpos = null;
    private Position position;

    //Drag and Drop
    private Point imageCorner = new Point( 0, 0 );                            //Bildecke
    private Point previousPoint = new Point( 0, 0 );                    //letzter Mauszeigerpunkt
    private boolean mouseIsPressed;

    public DrawField( Board gameBoard ) throws IOException
    {
        this.currentGameBoard = gameBoard;
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
        DragListener dragListener = new DragListener();
        this.addMouseMotionListener( dragListener );
    }

    public void setCurrentView( Board board )
    {
        currentViewBoard = board;
    }

    public boolean isCurrentlyViewing()
    {
        return currentViewBoard != null;
    }

    public Board getBoard()
    {
        return isCurrentlyViewing() ? currentViewBoard : currentGameBoard;
    }

    public boolean isWhiteOnBot()
    {
        return getBoard().topColor() == PieceColor.BLACK;
    }

    public void fieldClicked( Position position )
    {
        highlighted = position;

        if ( moveStartpos == null )//Keine Figur ausgewählt zum fahren
        {
            Arrays.fill( possMovesField, false );
        }

        if ( moveStartpos == null ) //Kein Feld ausgewählt
        {
            //only figures that are currently at turn
            if ( getBoard().isPieceAt( position ) && getBoard().whosTurn() == getBoard().pieceColorAt( position ) )
            {
                //Noch prüfen ob deine Farbe aber es gibt noch keine Variable
                //"OpponentRow" muss auch noch dementsprechend angepasst werden
                Set<Position> pMoves = getBoard().getLogic().getAllValidMoves( position );
                for ( Position p : pMoves )
                {
                    possMovesField[p.getRow() * 8 + p.getColumn()] = true;
                }
                moveStartpos = position;
            }
        }
        else //Schon Figur ausgewählt
        {
            //Kann auf ausgewähltes Feld fahren
            //wenn es nur eine temporäre Ansicht ist, soll die Figur nicht bewegt werden
            if ( !isCurrentlyViewing() && possMovesField[position.getRow() * 8 + position.getColumn()] )
            {
                move( moveStartpos, position );
                Arrays.fill( possMovesField, false );
                moveStartpos = null;
                repaint();
                return;
            }
            else //Kann nicht auf ausgewähltes Feld fahren
            {
                Arrays.fill( possMovesField, false );
                moveStartpos = null;
                fieldClicked( position );
            }
        }
        repaint();
    }


    public void move( Position from, Position to )
    {
        GUI.notation.addPlayMove( from, to, Board.createCopy( getBoard() ) );
        currentGameBoard.movePiece( from, to );
        //enables changing turns between color
        currentGameBoard.changeTurn();
    }

    public Vector getOffensiveDirection( PieceColor color )
    {
        return isWhiteOnBot() == ( color == PieceColor.WHITE ) ? new Vector( 0, -1 ) : new Vector( 0, 1 );
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
        boolean white = isWhiteOnBot();
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
        if ( !mouseIsPressed )
        {
            forEachCell( pos ->
            {
                if ( getBoard().isPieceAt( pos ) )
                {
                    Image img = sprites[getBoard().getPiece( pos ).getSpriteIndex()];
                    g.drawImage( img, pos.getScreenX() * 64, pos.getScreenY() * 64, this );
                }
            } );
        }
        else
        {
            forEachCell( pos ->
            {
                if ( getBoard().isPieceAt( pos ) )
                    if ( pos.getColumn() == position.getColumn() && pos.getRow() == position.getRow() )
                    {
                        Image img = sprites[getBoard().getPiece( pos ).getSpriteIndex()];
                        g.drawImage( img, (int) imageCorner.getX(), (int) imageCorner.getY(), this );
                    }
                    else
                    {
                        Image img = sprites[getBoard().getPiece( pos ).getSpriteIndex()];
                        g.drawImage( img, pos.getScreenX() * 64, pos.getScreenY() * 64, this );
                    }
            } );
        }
    }

    private void drawPossibleMoves( Graphics2D g )
    {
        if ( moveStartpos != null ) //Methode nur aufrufen, wenn Figur ausgewählt wurde
        {
            for ( int row = 0; row < 8; row++ ) //Jedes Feld durchgehen
            {
                for ( int col = 0; col < 8; col++ )
                {
                    if ( possMovesField[row * 8 + col] )
                    {
                        g.setRenderingHints( hints ); //Damit Kreise nicht kacke aussehen
                        g.setStroke( new BasicStroke( 5 ) ); //Linienbreite
                        if ( getBoard().isPieceAt( Position.ofBoard( row, col ) ) ) //Wenn Figur geschlagen werden kann
                        {
                            g.setColor( HIGHLIGHTED_ENEMY_CIRCLE ); //dann roter kreis
                        }
                        else
                        {
                            g.setColor( HIGHLIGHTED_MOVE_CIRCLE ); //sonst grauer Kreis
                        }
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

    //Drag and Drop
    private class DragListener extends MouseMotionAdapter
    {

        public void mouseDragged( MouseEvent e )
        {
            if ( mouseIsPressed )
            {
                Point currentPt = e.getPoint();
                imageCorner.translate(
                        (int) ( currentPt.getX() - previousPoint.getX() ),    //Mausverschiebung auf
                        (int) ( currentPt.getY() - previousPoint.getY() ) ); //Figurenverschiebung übertragen
                previousPoint = currentPt; //Beide Variablen um Mausverschiebung zu messen
                repaint();

            }
        }
    }

    //Beide Arten von Figurenbewegung
    private MouseListener mouse = new MouseAdapter()
    {
        @Override
        public void mousePressed( MouseEvent e )
        {
            position = Position.ofScreen( e.getX() / 64, e.getY() / 64 ); //Startfeld
            imageCorner.setLocation( position.getScreenX() * 64, position.getScreenY() * 64 );
            previousPoint = e.getPoint(); //Letzter Mauspunkt (initatiolisierung)
            mouseIsPressed = true; //wenn der Nutzer die Maus gedrückt hält

            fieldClicked( position ); //, Optional.ofNullable( getBoard().getPiece( position ) ) );

            repaint();
        }

        @Override
        public void mouseReleased( MouseEvent e )
        {
            position = Position.ofScreen( e.getX() / 64, e.getY() / 64 );
            if ( possMovesField[position.getRow() * 8 + position.getColumn()] ) //Wenn sich feld geändert hat
            {
                move( moveStartpos, position ); //dann -wenn möglich- auf neues feld ziehen
                for ( int i = 0; i < possMovesField.length; i++ ) //und possible Moves reseten
                {
                    possMovesField[i] = false;
                }
                moveStartpos = null;
                repaint();
            }
            mouseIsPressed = false;
            repaint();
        }
    };
}
