package de.schach;

/**
 * <h1>Schach</h1>
 *
 * @author Julius Korweck
 * @version 08.05.2021
 * @since 08.05.2021
 */
enum PieceColor
{

    WHITE( (byte) 0b0001 ),
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

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/