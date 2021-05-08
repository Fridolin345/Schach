package de.schach;

/**
 * <h1>Schach</h1>
 *
 * @author Julius Korweck
 * @version 08.05.2021
 * @since 08.05.2021
 */
public enum PieceType
{

    KING( 0 ),
    QUEEN( 1 ),
    ROCK( 2 ),
    BISHOP( 3 ),
    KNIGHT( 4 ),
    PAWN( 5 );

    private byte representative;

    PieceType( int representative )
    {
        this.representative = (byte) representative;
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

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/