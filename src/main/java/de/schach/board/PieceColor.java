package de.schach.board;

public enum PieceColor
{

    WHITE( (byte) 0b1000 ),
    BLACK( (byte) 0b0000 );

    private byte value;

    PieceColor( byte value )
    {
        this.value = value;
    }

    public byte getValue()
    {
        return value;
    }

    public PieceColor invert()
    {
        return this == WHITE ? BLACK : WHITE;
    }

}