package de.schach;

import lombok.ToString;

@ToString
public class Position
{

    private int row;
    private int column;

    private Position( int row, int column )
    {
        this.row = row;
        this.column = column;
    }

    public static Position ofScreen( int x, int y )
    {
        return new Position( y, x );
    }

    public static Position ofBoard( int row, int column )
    {
        return new Position( row, column );
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

    public int getBoardPosition()
    {
        return row * 8 + column;
    }

    public int getScreenX()
    {
        return getColumn();
    }

    public int getScreenY()
    {
        return getRow();
    }

    public boolean isValidOnBoard()
    {
        return row >= 0 && column >= 0 && row < 8 && column < 8;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        Position position = (Position) o;

        if ( row != position.row ) return false;
        return column == position.column;
    }

    @Override
    public int hashCode()
    {
        return row * 10 + column;
    }

}