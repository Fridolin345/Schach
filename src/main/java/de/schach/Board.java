package de.schach;

import java.util.*;

public class Board
{

    private byte[] board;

    public Board()
    {
        reset();
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

        String[] settings = data.split( " " ); //TODO later

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
            }
            else if ( scanner.hasNext( "\\d" ) ) //number
            {
                int digit = scanner.nextInt();
            }
            else
            {
                throw new FenSyntaxException( "Invalid rank in FEN string: \"" + rank + "\"; cant read \"" + scanner.next() + "\"" );
            }
        }

    }

    //Beginnend bei 0/0 links oben auf dem Feld
    public Piece getPiece( int row, int colum )
    {
        byte pieceData = board[row * 8 + colum];
        return Piece.fromByte( pieceData );
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
