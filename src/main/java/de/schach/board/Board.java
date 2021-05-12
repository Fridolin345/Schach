package de.schach.board;

import de.schach.exception.FenSyntaxException;
import de.schach.logic.BoardLogic;
import de.schach.util.ArrayUtil;
import de.schach.util.Vector;

import java.util.Scanner;

public class Board
{

    private byte[] board;
    private PieceColor topColor;

    private Board()
    {
        loadFromFen( "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1" );
    }

    private Board( byte[] board )
    {
        this.board = board;
    }

    public static Board create( Board board )
    {
        return new Board( board.board );
    }

    public static Board create( byte[] board )
    {
        return new Board( board );
    }

    public static Board create()
    {
        return new Board();
    }

    public void reset()
    {
        board = new byte[8 * 8];
    }

    public void setBoard( byte[] board )
    {
        this.board = board;
    }

    public Board getInvertedCopy()
    {
        return new Board( ArrayUtil.rowFlippedBoard( this.board ) );
    }

    public BoardLogic getLogic()
    {
        return BoardLogic.ofBoard( this );
    }

    public Piece getPiece( Position position )
    {
        byte pieceData = board[position.getBoardPosition()];
        return Piece.fromByte( pieceData );
    }

    public boolean isPieceAt( Position position )
    {
        return board[position.getBoardPosition()] != 0;
    }

    public void setPiece( Position position, Piece piece )
    {
        board[position.getBoardPosition()] = piece == null ? 0 : piece.toByte();
    }

    public void movePiece( Position from, Position to )
    {
        setPiece( to, getPiece( from ) );
        setPiece( from, null );
    }

    public PieceColor topColor()
    {
        return topColor;
    }

    public Vector getOffensiveDirection( PieceColor color )
    {
        if ( topColor == PieceColor.BLACK )
        {
            return color == PieceColor.WHITE ? new Vector( 0, -1 ) : new Vector( 0, 1 );
        }
        else return color == PieceColor.WHITE ? new Vector( 0, 1 ) : new Vector( 0, -1 );
    }

    public PieceColor pieceColorAt( Position position )
    {
        if ( position == null )
        {
            return null;
        }
        return isPieceAt( position ) ? null : getPiece( position ).getColor();
    }

    public void loadFromFen( String fen )
    {

        reset();
        String squares = fen.substring( 0, fen.indexOf( ' ' ) );
        String data = fen.substring( fen.indexOf( ' ' ) + 1 );

        String[] settings = data.split( " " ); //TODO later, just skip it currently

        if ( settings[0].equalsIgnoreCase( "w" ) )
            topColor = PieceColor.BLACK;
        else topColor = PieceColor.WHITE;

        String[] ranks = squares.split( "/" );

        Scanner scanner;
        int currentField = 0;
        for ( String rank : ranks )
        {
            scanner = new Scanner( rank );
            scanner.useDelimiter( "\\r*" );
            while ( scanner.hasNext() )
            {
                if ( scanner.hasNext( "\\d" ) ) //number
                {
                    int amount = scanner.nextInt();
                    for ( int i = 0; i < amount; i++ )
                    {
                        board[currentField++] = 0;
                    }
                }
                else if ( scanner.hasNext( "\\w" ) ) //letter
                {
                    char pieceToken = scanner.next().charAt( 0 );
                    PieceType type = PieceType.fromChar( Character.toLowerCase( pieceToken ) );
                    PieceColor color = Character.isUpperCase( pieceToken ) ? PieceColor.WHITE : PieceColor.BLACK;
                    Piece piece = Piece.fromPartialData( type, color );
                    board[currentField++] = piece.toByte();
                }
                else
                {
                    throw new FenSyntaxException( "Invalid rank in FEN string: \"" + rank + "\"; cant read \"" + scanner.next() + "\"" );
                }
            }
        }

    }

    public void move( int startRow, int startCol, int endRow, int endCol )
    {

    }

    public void removePieceAt( int row, int col )
    {
        board[row * 8 + col] = 0;

    }
}
