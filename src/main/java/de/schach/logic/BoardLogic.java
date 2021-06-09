package de.schach.logic;

import de.schach.board.*;
import de.schach.gui.PrintMyField;
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
        List<Vector> unblockedMoves = getUnblockedMoves( position, true );
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
            //if king moves, he can no longer castle on both sides
            board.setNotAllowedToCastle( piece.getColor(), false );
            board.setNotAllowedToCastle( piece.getColor(), true );
        }

    }

    //---------------------------------------- Helper Functions -----------------------------------------------------


    //a list of all moves that are not blocked by a figure on the board
    //includes move onto fields with opponent figures
    //does NOT exclude invalid KING moves @see fixKing
    //does NOT exclude invalid PAWN moves @see fixPawn
    private List<Vector> getUnblockedMoves( Position position, boolean checkBlocking )
    {
        List<Vector> unblocked = new LinkedList<>();
        Piece piece = board.getPiece( position );
        if ( piece == null ) return Collections.emptyList();
        PieceColor color = piece.getColor();
        Set<Vector> moveVectors = piece.getPieceType().getMoveVectors( board.getOffensiveDirection( piece.getColor() ) );
        Debug.log( "raw move vectors: " + Arrays.toString( moveVectors.toArray() ) );
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
    private int canMoveHere( PieceColor color, Position start, Vector move )
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
        //removes all diagonal non-enPassant moves
        unblockedMoves.removeIf( vec -> vec.getAbsX() == 1 && !board.isPieceAt( pos.move( vec ) ) && !board.isEnPassant( pos.move( vec ) ) );
        //removes all straight forward moves, that are blocked by a piece / pawns cant fight moving forward
        unblockedMoves.removeIf( vec -> vec.getAbsX() == 0 && board.isPieceAt( pos.move( vec ) ) );
    }

    //get all covered positions by the given color
    private Set<Position> getCoverage( PieceColor color )
    {
        Set<Position> positions = new HashSet<>();
        Board.TOP_LEFT_CORNER.iterateTo( Board.BOTTOM_RIGHT_CORNER, pos ->
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


    public boolean KingInCheck( PieceColor kingColor )
    {
        new PrintMyField(this.board);
        Set<Position> enemyMoves = getCoverage( kingColor.invert() );
        if ( enemyMoves.contains( getKing( kingColor ) ) )
        {
            return true;
        }
        return false;
    }

    public Position getKing( PieceColor kingColor )
    {
        ArrayList<Position> allMyPieces = this.getAllPiecesForOneColor( kingColor );
        for ( Position p : allMyPieces )
        {
            Piece temp = this.board.getPiece( p );
            if ( temp.getPieceType() == KING )
            {
                if ( temp.getColor() == kingColor )
                {
                    System.out.println(p);
                    return p;
                }
            }
        }
        System.out.println( "König konnte nicht gefunden werden" );
        return null;
    }

    public ArrayList<Position> getAllPiecesForOneColor( PieceColor pc )
    {
        ArrayList<Position> allPositions = new ArrayList<>();
        Board.TOP_LEFT_CORNER.iterateTo( Board.BOTTOM_RIGHT_CORNER, position ->
        {
            if ( this.board.isPieceAt( position ) )
            {
                Piece temp = this.board.getPiece( position );
                if ( temp.getColor() == pc )
                {
                    allPositions.add( position );
                }
            }
        } );
        return allPositions;
    }

}