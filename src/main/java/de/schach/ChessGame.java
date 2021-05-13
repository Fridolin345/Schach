package de.schach;

import de.schach.board.Board;
import de.schach.gui.GUI;
import de.schach.gui.PrintMyField;
import de.schach.util.Debug;

import java.io.IOException;

public class ChessGame
{

    public static void main( String[] args ) throws IOException
    {
        Debug.setDebug( true );
        test();
        new GUI();
    }

    private static void test()
    {
        //to do some tests
        new PrintMyField( new Board() );
    }
}