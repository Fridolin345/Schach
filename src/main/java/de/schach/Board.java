package de.schach;

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
        String settings = fen.substring( fen.indexOf( ' ' ) + 1 );

        String[] ranks = squares.split( "/" );



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

}
