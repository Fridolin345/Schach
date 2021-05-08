package de.schach;

import lombok.ToString;

@ToString
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

    public Position move( Vector vector )
    {
        return new Position( row + vector.getY(), column + vector.getX() );
    }

    public boolean isValidOnBoard()
    {
        return row >= 0 && column >= 0 && row < 8 && column < 8;
    }

}