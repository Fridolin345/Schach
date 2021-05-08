package de.schach;

/**
 * <h1>Schach</h1>
 *
 * @author Julius Korweck
 * @version 08.05.2021
 * @since 08.05.2021
 */
public class Position
{

    private int row;
    private int column;

    public Position( int row, int column )
    {
        this.row = row;
        this.column = column;
    }

    public int getColumn()
    {
        return column;
    }

    public int getRow()
    {
        return row;
    }

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/