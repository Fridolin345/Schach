package de.schach;

/**
 * <h1>Schach</h1>
 *
 * @author Julius Korweck
 * @version 10.05.2021
 * @since 10.05.2021
 */
public class ArrayUtil
{

    public static byte[] rowFlippedBoard( byte[] board )
    {
        byte[] newBoard = new byte[board.length];
        for ( int row = 0; row < 4; row++ )
        {
            for ( int i = 0; i < 8; i++ )
            {
                newBoard[row * 8 + i] = board[( 7 - row ) * 8 + i];
                newBoard[( 7 - row ) * 8 + i] = board[row * 8 + i];
            }
        }
        return newBoard;
    }

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/