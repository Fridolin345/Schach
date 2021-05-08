package de.schach;

import java.io.IOException;

public class ChessGame
{

    private static Board mainBoard = new Board();

    public static Board getMainBoard()
    {
        return mainBoard;
    }

    public static void main( String[] args ) throws IOException
    {
        //System.out.println( mainBoard.getAt( 'a', 1 ) );
        new GUI();

    }
}