package de.schach;

import java.io.IOException;

public class ChessGame
{

    public static Board mainBoard = new Board();


    public static void main( String[] args ) throws IOException
    {

        //System.out.println( mainBoard.getAt( 'a', 1 ) );
        new GUI();
    }
}