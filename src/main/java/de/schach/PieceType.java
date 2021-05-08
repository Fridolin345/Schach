package de.schach;

public enum PieceType
{

    KING( 0, 'k' ),
    QUEEN( 1, 'q' ),
    BISHOP( 3, 'b' ),
    KNIGHT( 4, 'n' ),
    ROCK( 2, 'r' ),
    PAWN( 5, 'p' );

    private final byte representative;
    private final char fenChar;

    PieceType( int representative, char fenChar )
    {
        this.representative = (byte) representative;
        this.fenChar = fenChar;
    }

    public char getFenChar()
    {
        return fenChar;
    }

    public byte getRepresentative()
    {
        return representative;
    }

    public static PieceType fromByte( byte data )
    {
        for ( PieceType type : values() )
        {
            if ( type.representative == data )
                return type;
        }
        return null;
    }

}