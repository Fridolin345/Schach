package de.schach;

import lombok.ToString;

import java.util.Set;

@ToString
public class Vector
{

    private int x;
    private int y;

    public Vector( int x, int y )
    {
        this.x = x;
        this.y = y;
    }

    public Vector( Vector vector )
    {
        this.x = vector.getX();
        this.y = vector.getY();
    }

    public Vector multiply( int multi )
    {
        return new Vector( x * multi, y * multi );
    }

    public Vector add( Vector vector )
    {
        return new Vector( x + vector.getX(), y + vector.getY() );
    }

    public void setX( int xOffset )
    {
        this.x = xOffset;
    }

    public void setY( int yOffset )
    {
        this.y = yOffset;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public Vector toBaseVector()
    {
        int euclid = euclid( Math.max( x, y ), Math.min( x, y ) );
        if ( euclid <= 0 ) return new Vector( x, y );
        return new Vector( x / euclid, y / euclid );
    }

    public static int euclid( int a, int b )
    {
        return b == 0 ? a : euclid( b, a % b );
    }

    public Set<Position> getPositionsAlong( Position position )
    {
        return null;
    }

}