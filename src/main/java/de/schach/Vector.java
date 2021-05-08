package de.schach;

import lombok.ToString;

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

}