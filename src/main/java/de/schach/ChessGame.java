package de.schach;

import java.io.IOException;

public class ChessGame
{

    public static void main( String[] args ) throws IOException
    {
        Debug.setDebug( true );
        new GUI();
    }

    private static void test()
    {
        //to do some tests
        Vector vector = new Vector( 6, 2 );
        Debug.log( vector.toBaseVector() );
    }

}