package de.schach;

import lombok.ToString;

import java.util.function.Consumer;

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

    public void iterateTo( Position position, Consumer<Position> forEach )
    {
        int toRow = Math.max( this.row, position.getRow() );
        int toColumn = Math.max( this.column, position.getColumn() );
        for ( int row = Math.min( this.row, position.getRow() ); row < toRow; row++ )
        {
            for ( int col = Math.min( this.column, position.getColumn() ); col < toColumn; col++ )
            {
                forEach.accept( Position.ofBoard( row, col ) );
            }
        }
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