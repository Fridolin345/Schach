package de.schach;

import java.util.*;

public class Board
{

    private byte[] board;

    public Board()
    {
        loadFromFen( "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1" );
    }

    public void reset()
    {
        board = new byte[8 * 8];
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
            if ( scanner.hasNext( "\\w" ) ) //letter
            {
                char pieceToken = scanner.next().charAt( 0 );
                PieceType type = PieceType.fromChar( Character.toLowerCase( pieceToken ) );
                PieceColor color = Character.isUpperCase( pieceToken ) ? PieceColor.WHITE : PieceColor.BLACK;
                board[currentField++] = Piece.fromPartialData( type, color ).toByte();
            }
            else if ( scanner.hasNext( "\\d" ) ) //number
            {
                int amount = scanner.nextInt();
                for ( int i = 0; i < amount; i++ )
                {
                    board[currentField++] = 0;
                }
            }
            else
            {
                throw new FenSyntaxException( "Invalid rank in FEN string: \"" + rank + "\"; cant read \"" + scanner.next() + "\"" );
            }
        }

    }

    public Piece getPiece( int row, int colum )
    {
        byte pieceData = board[row * 8 + colum];
        return Piece.fromByte( pieceData );
    }

    public Piece getPiece( Position position )
    {
        return getPiece( position.getRow(), position.getColumn() );
    }

    public void setPiece( int row, int colum, Piece piece )
    {
        board[row * 8 + colum] = piece == null ? -1 : piece.toByte();
    }

    public List<Position> getPosition( Piece piece )
    {
        List<Position> positions = new LinkedList<>();
        for ( int row = 0; row < 8; row++ )
        {
            for ( int col = 0; col < 8; col++ )
            {
                if ( Piece.fromByte( board[row * 8 + col] ) == piece )
                    positions.add( new Position( row, col ) );
            }
        }
        return positions;
    }

}
