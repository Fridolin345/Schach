package de.schach;

import java.util.*;
import java.util.stream.Collectors;

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

    //a list of all moves that are not blocked by a figure on the board
    //includes move onto fields with opponent figures
    //does NOT exclude invalid KING moves yet
    //does NOT exclude invalid PAWN moves yet
    public static List<Vector> getUnblockedMoves( Position position )
    {
        List<Vector> unblocked = new LinkedList<>();
        Piece piece = Board.getInstance().getPiece( position );
        if ( piece == null ) return Collections.emptyList();
        PieceColor color = piece.getColor();
        Set<Vector> moveVectors = piece.getPieceType().getMoveVectors( Board.getInstance().getOffensiveDirection( piece.getColor() ) );
        Debug.log( Arrays.toString( moveVectors.toArray() ) );
        for ( Vector moveVector : moveVectors )
        {
            Vector base = moveVector.toBaseVector();
            Vector current = base;
            int reach = 1;
            do
            {
                current = base.multiply( reach++ );
                if ( !position.move( current ).isValidOnBoard() ) break;
                int canMove = canMoveHere( color, position, current );
                if ( canMove == -1 ) break;
                unblocked.add( current );
                if ( canMove == 1 ) break; //opponent figure there;
                if(piece.getPieceType().isOnlyOneStep()) break;
            }
            while ( reach < 9 );
        }
        return unblocked;
    }

    //no checks for in between
    private static int canMoveHere( PieceColor color, Position start, Vector move )
    {
        return !Board.getInstance().isPieceAt( start.move( move ) ) ? 0 : ( Board.getInstance().getPiece( start.move( move ) ).getColor().invert() == color ? 1 : -1 );
    }

    //Beinhaltet ALLE möglichen Züge
    //D.h. die Diagonalen Züge für den Bauer sowie die Doppelschritte - die Voraussetzungen werden hier nicht geprüft
    //Die Positionen liegen niemals außerhalb des Brettes
    public static Set<Position> getAllPossibleMoves( Position position )
    {
        Piece piece = Board.getInstance().getPiece( position );
        if ( piece == null ) return Collections.emptySet();
        List<Vector> unblockedMoves = getUnblockedMoves( position );
        return unblockedMoves.stream().map( position::move ).collect( Collectors.toSet() );
    }

}