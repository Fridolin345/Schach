package de.schach;

public class Board
{

    private byte[] board;

    private boolean whiteToMove;

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
        String state = fen.substring( fen.indexOf( ' ' ) + 1 );

        String[] ranks = squares.split( "/" );

    }

}
