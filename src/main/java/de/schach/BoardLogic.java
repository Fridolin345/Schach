package de.schach;

import java.util.Collections;
import java.util.Set;

public class BoardLogic
{

    public static boolean[] getPossibleMoves()
    {
        return null;
    }

    public static boolean canMoveTo( Piece piece, Position start, Position wish )
    {
        if ( piece.getPieceType() == PieceType.PAWN )
        {
            if ( piece.getColor() == PieceColor.WHITE )
            {

            }
        }

        return false;
    }

    public static boolean isNothingBetween( Position start, Position end )
    { //noch nicht fertig
        if ( start.getColumn() == end.getColumn() )
        {
            for ( int row = start.getRow(); row == end.getRow(); row++ )
            {
                if ( Board.getInstance().isPieceAt( Position.ofBoard( row, start.getColumn() ) ) )
                {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isMoveAllowed( Position position, Vector vector )
    {
        if ( Board.getInstance().isPieceAt( position ) )
        {
            Piece piece = Board.getInstance().getPiece( position );

        }
        return false; //TODO
    }

    //Beinhaltet ALLE möglichen Züge
    //D.h. die Diagonalen Züge für den Bauer sowie die Doppelschritte - die Voraussetzungen werden hier nicht geprüft
    //Die Positionen liegen niemals außerhalb des Brettes
    public static Set<Position> getAllPossibleMoves( Position position )
    {
        Piece piece = Board.getInstance().getPiece( position );
        if ( piece == null ) return Collections.emptySet();

        //muss noch überarbeitet werden, aber erstmal ist schwarz oben - sorgt dafür, dass Bauern nur in Richtung des Gegners laufen können
        int opponentSide = piece.getColor() == PieceColor.WHITE ? 0 : 8;
        //return piece.getPieceType().getAllPossibleMoves( position, opponentSide );
        return null; //TODO
    }

}