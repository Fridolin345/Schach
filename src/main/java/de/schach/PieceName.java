package de.schach;

public enum PieceName
{
    WKING( 1 ),
    WQUEEN( 2 ),
    WROCK( 3 ),
    WBISHOP( 4 ),
    WKNIGHT( 5 ),
    WPAWN( 6 ),
    BKING( 7 ),
    BQUEEN( 8 ),
    BROCK( 9 ),
    BBISHOP( 10 ),
    BKNIGHT( 11 ),
    BPAWN( 12 );

    private int pieceNumber;

    PieceName( int pieceNumber )
    {
        this.pieceNumber = pieceNumber;
    }

    public int getPieceNumber()
    {
        return pieceNumber;
    }

}
