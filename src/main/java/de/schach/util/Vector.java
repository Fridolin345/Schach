package de.schach.util;

import lombok.ToString;

import java.util.*;

@ToString
public class Vector implements Comparable<Vector>
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

    public int getAbsX()
    {
        return Math.abs( x );
    }

    public int getAbsY()
    {
        return Math.abs( y );
    }

    public Vector toBaseVector()
    {
        int euclid = euclid( Math.max( getAbsX(), getAbsY() ), Math.min( getAbsX(), getAbsY() ) );
        if ( euclid <= 0 ) return new Vector( x, y );
        return new Vector( x / euclid, y / euclid );
    }

    public static int euclid( int a, int b )
    {
        return b == 0 ? a : euclid( b, a % b );
    }

    public List<Vector> getPositionVectorsAlong()
    {
        List<Vector> vectors = new LinkedList<>();
        Vector base = toBaseVector();
        Vector current = base;
        do
        {
            vectors.add( current );
            current = current.add( base );
        }
        while ( !current.equals( this ) );
        vectors.add( this );
        Collections.sort( vectors );
        return vectors;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        Vector vector = (Vector) o;

        if ( x != vector.x ) return false;
        return y == vector.y;
    }

    @Override
    public int hashCode()
    {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public int compareTo( Vector other )
    {
        return Integer.compare( this.getAbsX() + this.getAbsY(), other.getAbsX() + other.getAbsY() );
    }

}