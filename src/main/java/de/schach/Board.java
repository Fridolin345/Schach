package de.schach;

import java.util.*;

public class Board
{

    private byte[] board;

    public Board()
    {
        loadFromFen( "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1" );
        System.out.println( Arrays.toString( board ) );
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

    public Piece getPiece( Position position )
    {
        byte pieceData = board[position.getBoardPosition()];
        System.out.println();
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

    public boolean canMoveTo( Piece piece, Position start, Position wish )
    {
        if ( piece.getPieceType() == PieceType.PAWN )
        {
            if ( piece.getColor() == PieceColor.WHITE )
            {

            }
        }

        return false;
    }

    public boolean isNothingBetween( Position start, Position end )
    { //noch nicht fertig
        if ( start.getColumn() == end.getColumn() )
        {
            for ( int row = start.getRow(); row == end.getRow(); row++ )
            {
                if ( isPieceAt( Position.ofBoard( row, start.getColumn() ) ) )
                {
                    return false;
                }
            }
        }
        return true;
    }

    public PieceColor pieceColorAt( Position position )
    {
        return isPieceAt( position ) ? null : getPiece( position ).getColor();
    }

    public static boolean[] getPossibleMoves()
    {
        return null;
    }


}
