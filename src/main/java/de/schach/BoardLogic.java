package de.schach;

import java.util.*;
import java.util.stream.Collectors;

import static de.schach.PieceType.*;

public class BoardLogic
{

    //Contains all valid moves for a piece at a given position
    public static Set<Position> getAllValidMoves( Position position )
    {
        Piece piece = Board.getInstance().getPiece( position );
        if ( piece == null ) return Collections.emptySet();
        List<Vector> unblockedMoves = getUnblockedMoves( position, true );
        if ( piece.getPieceType() == PAWN ) fixPawn( position, unblockedMoves );
        Set<Position> positions = unblockedMoves.stream().map( position::move ).collect( Collectors.toSet() );
        if ( piece.getPieceType() == KING ) fixKing( piece.getColor(), positions );
        return positions;
    }


    //---------------------------------------- Helper Functions -----------------------------------------------------


    //a list of all moves that are not blocked by a figure on the board
    //includes move onto fields with opponent figures
    //does NOT exclude invalid KING moves yet
    //does NOT exclude invalid PAWN moves yet
    private static List<Vector> getUnblockedMoves( Position position, boolean checkBlocking )
    {
        List<Vector> unblocked = new LinkedList<>();
        Piece piece = Board.getInstance().getPiece( position );
        if ( piece == null ) return Collections.emptyList();
        PieceColor color = piece.getColor();
        Set<Vector> moveVectors = piece.getPieceType().getMoveVectors( Board.getInstance().getOffensiveDirection( piece.getColor() ) );
        Debug.log( Arrays.toString( moveVectors.toArray() ) );
        for ( Vector moveVector : moveVectors )
        {
            Vector base = ( piece.getPieceType() == PAWN && ( position.getRow() == 6 || position.getRow() == 1 ) ) ? moveVector : moveVector.toBaseVector();
            Vector current;
            int reach = 1;
            do
            {
                current = base.multiply( reach++ );
                if ( !position.move( current ).isValidOnBoard() ) break;
                int canMove = canMoveHere( color, position, current );
                if ( canMove == -1 && checkBlocking ) break; //own figure
                unblocked.add( current );
                if ( canMove == 1 && ( Board.getInstance().getPiece( position.move( current ) ).getPieceType() != KING || checkBlocking ) )
                    break; //opponent figure there;
                if ( piece.getPieceType().isOnlyOneStep() ) break;
            }
            while ( reach < 9 );
        }
        return unblocked;
    }

    //whether a figure is allowed to move there by movement rules
    //no checks for in between or blocking
    private static int canMoveHere( PieceColor color, Position start, Vector move )
    {
        return !Board.getInstance().isPieceAt( start.move( move ) ) ? 0 : ( Board.getInstance().getPiece( start.move( move ) ).getColor().invert() == color ? 1 : -1 );
    }

    //helper method to fix king movement - king can't move onto covered fields
    private static void fixKing( PieceColor color, Set<Position> unblockedMoves )
    {
        Set<Position> coverage = getCoverage( color.invert() );
        unblockedMoves.removeIf( coverage::contains );
    }

    //helper method to fix pawn movement - pawn can only move diagonally when able to strike opponent
    private static void fixPawn( Position pos, List<Vector> unblockedMoves )
    {
        unblockedMoves.removeIf( vec -> vec.getAbsX() == 1 && !Board.getInstance().isPieceAt( pos.move( vec ) ) );
    }

    //get all covered positions by the given color
    private static Set<Position> getCoverage( PieceColor color )
    {
        Set<Position> positions = new HashSet<>();
        Position start = Position.ofBoard( 0, 0 );
        start.iterateTo( Position.ofBoard( 7, 7 ), pos ->
        {
            if ( !Board.getInstance().isPieceAt( pos ) ) return;
            Piece piece = Board.getInstance().getPiece( pos );
            if ( piece.getColor() != color ) return;
            if ( piece.getPieceType() == KING )
                positions.addAll( getUnblockedMoves( pos, false ).stream().map( pos::move ).collect( Collectors.toSet() ) );
            else if ( piece.getPieceType() == PAWN )
                positions.addAll( getUnblockedMoves( pos, false ).stream().filter( vec -> vec.getAbsX() == 1 ).map( pos::move ).collect( Collectors.toSet() ) );
            else
                positions.addAll( getUnblockedMoves( pos, false ).stream().map( pos::move ).collect( Collectors.toSet() ) );
        } );
        return positions;
    }



}