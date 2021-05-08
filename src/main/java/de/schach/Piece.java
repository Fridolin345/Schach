package de.schach;

import java.util.HashMap;
import java.util.Map;

import static de.schach.PieceColor.*;

public enum Piece
{
    WKING( 0, PieceType.KING, WHITE ),
    WQUEEN( 1, PieceType.QUEEN, WHITE ),
    WBISHOP( 2, PieceType.BISHOP, WHITE ),
    WKNIGHT( 3, PieceType.KNIGHT, WHITE ),
    WROCK( 4, PieceType.ROCK, WHITE ),
    WPAWN( 5, PieceType.PAWN, WHITE ),
    BKING( 6, PieceType.KING, BLACK ),
    BQUEEN( 7, PieceType.QUEEN, BLACK ),
    BBISHOP( 8, PieceType.BISHOP, BLACK ),
    BKNIGHT( 9, PieceType.KNIGHT, BLACK ),
    BROCK( 10, PieceType.ROCK, BLACK ),
    BPAWN( 11, PieceType.PAWN, BLACK );


    private static Map<Byte, Piece> pieces;

    static
    {
        pieces = new HashMap<>();
        for ( Piece piece : values() )
        {
            pieces.put( piece.toByte(), piece );
        }
    }

    private int spriteIndex;
    private PieceType pieceType;
    private PieceColor colorByte; //0b0001 = w / 0b0000 = b

    Piece( int spriteIndex, PieceType pieceType, PieceColor colorByte )
    {
        this.pieceType = pieceType;
        this.spriteIndex = spriteIndex;
        this.colorByte = colorByte;
    }

    public static Piece fromPartialData( PieceType pieceType, PieceColor color )
    {
        for ( Piece piece : values() )
        {
            if ( piece.colorByte == color && piece.pieceType == pieceType )
            {
                return piece;
            }
        }
        return null;
    }

    public static PieceColor getColor( byte pieceData )
    {
        return ( pieceData & WHITE.getValue() ) == WHITE.getValue() ? WHITE : BLACK;
    }

    public static Piece fromByte( byte pieceData )
    {
        return pieces.get( pieceData );
    }

    public PieceType getPieceType()
    {
        return pieceType;
    }

    public byte getColorByte()
    {
        return colorByte.getValue();
    }

    public PieceColor getColor()
    {
        return colorByte;
    }

    public byte toByte()
    {
        return (byte) ( getPieceType().getRepresentative() | getColorByte() );
    }

    public int getSpriteIndex()
    {
        return spriteIndex;
    }


}
