package de.schach.board;

import de.schach.util.Vector;
import lombok.ToString;

import java.util.function.Consumer;

@ToString
public class Position implements Comparable<Position>
{

    private static final char[] FIELD = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };

    private int row;
    private int column;

    Position( int row, int column )
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

    public String toNotation()
    {
        return colToNotation() + rowToNotation();
    }

    public String colToNotation()
    {
        return String.valueOf( FIELD[getColumn()] );
    }

    public String rowToNotation()
    {
        return String.valueOf( ( ( 7 - getRow() ) + 1 ) );
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

    public Vector toVector()
    {
        return new Vector( getScreenX(), getScreenY() );
    }

    public Position inverted()
    {
        return Position.ofBoard( 7 - row, column );
    }

    public boolean isValidOnBoard()
    {
        return row >= 0 && column >= 0 && row < 8 && column < 8;
    }

    public void iterateTo( Position position, Consumer<Position> forEach )
    {
        int toRow = Math.max( this.row, position.getRow() );
        int toColumn = Math.max( this.column, position.getColumn() );
        for ( int row = Math.min( this.row, position.getRow() ); row <= toRow; row++ )
        {
            for ( int col = Math.min( this.column, position.getColumn() ); col <= toColumn; col++ )
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


    @Override
    public int compareTo( Position other )
    {
        if ( other.getRow() == this.getRow() )
            return Integer.compare( getColumn(), other.getColumn() );
        else return Integer.compare( getRow(), other.getRow() );
    }
}