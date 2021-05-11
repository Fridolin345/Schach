package de.schach;

import java.io.IOException;
import java.util.Arrays;

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
        Position position = Position.ofBoard( 7, 3 );
        Piece piece = Board.getInstance().getPiece( position );
        Board.getInstance().movePiece( Position.ofBoard( 6, 3 ), Position.ofBoard( 4, 4 ) );
        Debug.log( Arrays.toString( BoardLogic.getUnblockedMoves( position ).toArray() ) );
    }

}