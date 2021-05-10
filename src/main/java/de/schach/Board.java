package de.schach;

import java.util.*;

public class Board
{

    private static Board instance = new Board();

    public static Board getInstance()
    {
        return instance;
    }

    private byte[] board;

    private Board()
    {
        loadFromFen( "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1" );
    }

    private Board( byte[] board )
    {
        this.board = board;
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

    public List<Position> getPositions( Piece piece )
    {
        List<Position> positions = new LinkedList<>();
        for ( int row = 0; row < 8; row++ )
        {
            for ( int col = 0; col < 8; col++ )
            {
                if ( Piece.fromByte( board[row * 8 + col] ) == piece )
                    positions.add( Position.ofBoard( row, col ) );
            }
        }
        return positions;
    }

    public PieceColor pieceColorAt( Position position )
    {
        return isPieceAt( position ) ? null : getPiece( position ).getColor();
    }

    public void loadFromFen( String fen )
    {

        reset();
        String squares = fen.substring( 0, fen.indexOf( ' ' ) );
        String data = fen.substring( fen.indexOf( ' ' ) + 1 );

        String[] settings = data.split( " " ); //TODO later, just skip it currently

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
                    System.out.println( "placed " + piece.name() );
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
