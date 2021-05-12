package de.schach;

import de.schach.board.Board;
import de.schach.board.Position;
import de.schach.gui.GUI;
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
        Position position = Position.ofBoard( 7, 7 );
        Debug.log( position.toNotation() );
        Debug.log( Board.getInstance().getPiece( position ) );
    }

}