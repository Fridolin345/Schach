package de.schach.logic;

import de.schach.board.*;
import de.schach.util.Debug;
import de.schach.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

import static de.schach.board.PieceType.*;

public class BoardLogic
{

    private Board board;

    private BoardLogic( Board board )
    {
        this.board = board;
    }

    public static BoardLogic ofBoard( Board board )
    {
        return new BoardLogic( board );
    }

    //Contains all valid moves for a piece at a given position
    public Set<Position> getAllValidMoves( Position position )
    {
        Piece piece = board.getPiece( position );
        if ( piece == null ) return Collections.emptySet();
        List<de.schach.util.Vector> unblockedMoves = getUnblockedMoves( position, true );
        if ( piece.getPieceType() == PAWN ) fixPawn( position, unblockedMoves );
        Set<Position> positions = unblockedMoves.stream().map( position::move ).collect( Collectors.toSet() );
        if ( piece.getPieceType() == KING ) fixKing( piece.getColor(), positions );
        return positions;
    }

    public void postMoveChanges( Piece piece, Position origin, Vector move )
    {

        Position newPosition = origin.move( move );

        //check EnPassant
        if ( board.isEnPassant( newPosition ) && piece.getPieceType() == PAWN )
        {
            Vector offensiveDirection = board.getOffensiveDirection( piece.getColor().invert() );
            board.setPiece( newPosition.move( offensiveDirection ), null );
            board.unsetEnPassantPosition();
            return;
        }
        //setEnPassant
        if ( move.getAbsY() == 2 && piece.getPieceType() == PAWN )
        {
            Vector offensiveDirection = board.getOffensiveDirection( piece.getColor() );
            board.setEnPassantPosition( origin.move( offensiveDirection ) );
            return;
        }

        board.unsetEnPassantPosition();

        if ( piece.getPieceType() == KING )
        {
            board.setNotAllowedToCastle( piece.getColor(), false );
            board.setNotAllowedToCastle( piece.getColor(), true );
        }

    }

    //---------------------------------------- Helper Functions -----------------------------------------------------


    //a list of all moves that are not blocked by a figure on the board
    //includes move onto fields with opponent figures
    //does NOT exclude invalid KING moves yet
    //does NOT exclude invalid PAWN moves yet
    private List<de.schach.util.Vector> getUnblockedMoves( Position position, boolean checkBlocking )
    {
        List<de.schach.util.Vector> unblocked = new LinkedList<>();
        Piece piece = board.getPiece( position );
        if ( piece == null ) return Collections.emptyList();
        PieceColor color = piece.getColor();
        Set<de.schach.util.Vector> moveVectors = piece.getPieceType().getMoveVectors( board.getOffensiveDirection( piece.getColor() ) );
        Debug.log( Arrays.toString( moveVectors.toArray() ) );
        for ( de.schach.util.Vector moveVector : moveVectors )
        {
            de.schach.util.Vector base = ( piece.getPieceType() == PAWN && ( position.getRow() == 6 || position.getRow() == 1 ) ) ? moveVector : moveVector.toBaseVector();
            de.schach.util.Vector current;
            int reach = 1;
            do
            {
                current = base.multiply( reach++ );
                if ( !position.move( current ).isValidOnBoard() ) break;
                int canMove = canMoveHere( color, position, current );
                if ( canMove == -1 && checkBlocking ) break; //own figure
                unblocked.add( current );
                if ( canMove == 1 && ( board.getPiece( position.move( current ) ).getPieceType() != KING || checkBlocking ) )
                    break; //opponent figure there;
                if ( piece.getPieceType().isOnlyOneStep() ) break;
            }
            while ( reach < 9 );
        }
        return unblocked;
    }

    //whether a figure is allowed to move there by movement rules
    //no checks for in between or blocking
    private int canMoveHere( PieceColor color, Position start, de.schach.util.Vector move )
    {
        return !board.isPieceAt( start.move( move ) ) ? 0 : ( board.getPiece( start.move( move ) ).getColor().invert() == color ? 1 : -1 );
    }

    //helper method to fix king movement - king can't move onto covered fields
    private void fixKing( PieceColor color, Set<Position> unblockedMoves )
    {
        Set<Position> coverage = getCoverage( color.invert() );
        unblockedMoves.removeIf( coverage::contains );
    }

    //helper method to fix pawn movement - pawn can only move diagonally when able to strike opponent or enPassant
    private void fixPawn( Position pos, List<Vector> unblockedMoves )
    {
        unblockedMoves.removeIf( vec -> vec.getAbsX() == 1 && !board.isPieceAt( pos.move( vec ) ) && !board.isEnPassant( pos.move( vec ) ) );
    }

    //get all covered positions by the given color
    private Set<Position> getCoverage( PieceColor color )
    {
        Set<Position> positions = new HashSet<>();
        Position start = Position.ofBoard( 0, 0 );
        start.iterateTo( Position.ofBoard( 7, 7 ), pos ->
        {
            if ( !board.isPieceAt( pos ) ) return;
            Piece piece = board.getPiece( pos );
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