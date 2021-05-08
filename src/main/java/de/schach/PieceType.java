package de.schach;

public enum PieceType
{

    KING( 1, 'k' ),
    QUEEN( 2, 'q' ),
    BISHOP( 3, 'b' ),
    KNIGHT( 4, 'n' ),
    ROCK( 5, 'r' ),
    PAWN( 6, 'p' );

    private final byte representative;
    private final char fenChar;

    PieceType( int representative, char fenChar )
    {
        this.representative = (byte) representative;
        this.fenChar = fenChar;
    }

    public static PieceType fromChar( char fenData )
    {
        for ( PieceType type : values() )
        {
            if ( type.fenChar == fenData )
                return type;
        }
        return null;
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

    public char getFenChar()
    {
        return fenChar;
    }

    public byte getRepresentative()
    {
        return representative;
    }

}