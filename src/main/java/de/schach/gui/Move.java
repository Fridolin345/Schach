package de.schach.gui;

import de.schach.board.*;
import de.schach.logic.BoardLogic;
import de.schach.util.Debug;
import de.schach.util.Vector;

import java.util.*;

import static de.schach.board.Piece.*;

public class Move
{

    private final Position startPos;
    private final Position endPos;
    public final Board beforeboard;

    public PieceType pawnPromotes;


    public Move( Position startPos, Position endPos, Board beforeboard )
    {
        Debug.log( "new Move" );
        Debug.log( "startPos: " + startPos.toNotation() + ", endPos: " + endPos.toNotation() );
        this.startPos = startPos;
        this.endPos = endPos;
        this.beforeboard = beforeboard;
        this.pawnPromotes = PieceType.PAWN; //Bauer bleibt Bauer (brauche das für switch-case)
    }

    public Move( Position startPos, Position endPos, Board beforeboard, PieceType pawnPromotes )
    {
        this.startPos = startPos;
        this.endPos = endPos;
        this.beforeboard = beforeboard;
        this.pawnPromotes = pawnPromotes;
    }

    public String getAcronym()
    {
        Piece startPiece = this.beforeboard.getPiece( startPos );
        String acronym = "";
        boolean hits = beforeboard.isPieceAt( endPos );

        if ( startPiece.getPieceType() == PieceType.KING )
        { //Rochade
            if ( startPos.getColumn() - endPos.getColumn() > 1 )
            {
                return "0-0";
            }
            if ( startPos.getColumn() - endPos.getColumn() < -1 )
            {
                return "0-0-0";
            }
        }
        if ( startPiece.getPieceType() != PieceType.PAWN )
        {
            acronym += startPiece.getPieceType().getNotationChar();
            //Wenn z.B. zwei Türme auf das selbe Feld fahren können, muss klar gemacht werden, welcher Turm gemeint ist
            Piece temp = getOtherColorPiece( startPiece );
            Board tempBoard = Board.createCopy( beforeboard );
            tempBoard.setPiece( endPos, temp );
            tempBoard.removePiece( startPos );
            Set<Position> pMoves = tempBoard.getLogic().getAllValidMoves( endPos );
            boolean considerRow = false;
            boolean considerCol = false;
            for ( Position p : pMoves )
            {
                if ( tempBoard.getPiece( p ) == startPiece )
                {
                    if ( p.getColumn() != startPos.getColumn() )
                    {
                        considerCol = true;
                    }
                    else
                    {
                        considerRow = true;
                    }

                }
            }
            if ( considerCol )
            {
                acronym += startPos.colToNotation();
            }
            if ( considerRow )
            {
                acronym += startPos.rowToNotation();
            }
            if ( hits ) //Wenn figur geschlagen wurde
            {
                acronym += "x";
            }
        }
        else
        { //Wenn es ein Bauer ist
            if ( !beforeboard.isPieceAt( endPos ) && startPos.getColumn() != endPos.getColumn() || hits ) //En Passant
            { // Keine Figur auf ZielFeld         und Bauer wechselt Spalte --> schlägt etwas
                acronym += startPos.colToNotation();
                acronym += "x";
            }
            switch ( pawnPromotes )
            {
                case QUEEN:
                    acronym += "=Q";
                    break;
                case KNIGHT:
                    acronym += "=N";
                    break;
                case BISHOP:
                    acronym += "=B";
                    break;
                case ROCK:
                    acronym += "=R";
                    break;
                default:
                    break;
            }

        }
        acronym += endPos.toNotation();
        if (getAfterBoard().getLogic().KingInCheck( startPiece.getColor().invert() ) )
        {
            acronym += "+";
        }
        if ( false ) //TODO König Schachmatt
        {
            acronym += "+";
        }


        return acronym;
    }


    public Piece getOtherColorPiece( Piece piece )
    {
        ArrayList<Piece> firstcolor = new ArrayList<>();
        boolean b = Collections.addAll( firstcolor, WKING, WQUEEN, WBISHOP, WKNIGHT, WROCK, WPAWN, BKING, BQUEEN, BBISHOP, BKNIGHT, BROCK, BPAWN );
        Piece[] othercolor = { BKING, BQUEEN, BBISHOP, BKNIGHT, BROCK, BPAWN, WKING, WQUEEN, WBISHOP, WKNIGHT, WROCK, WPAWN };
        return othercolor[firstcolor.indexOf( piece )];
    }

    public boolean whiteIsMoving()
    {
        return ( beforeboard.pieceColorAt( startPos ) == PieceColor.WHITE );
    }

    public Vector getMoveVector()
    {
        return startPos.getDiff( getEndPos() );
    }

    public Position getStartPos()
    {
        return startPos;
    }

    public Position getEndPos()
    {
        return endPos;
    }

    public Board getAfterBoard(){
        Board myAfterBoard = Board.createCopy( beforeboard );
        Piece startPiece = beforeboard.getPiece( startPos );
        myAfterBoard.removePiece( startPos );
        myAfterBoard.setPiece( endPos, startPiece );
        if(startPiece.getPieceType() == PieceType.KING){
            if ( startPos.getColumn() - endPos.getColumn() > 1 ) //Kleien Rochade
            {
                Position rockStartPos = new Position( startPos.getRow(), 7);
                myAfterBoard.removePiece( rockStartPos );
                myAfterBoard.setPiece( new Position( startPos.getRow(), 5), beforeboard.getPiece( rockStartPos ) );
            }
            if ( startPos.getColumn() - endPos.getColumn() < -1 ) //Große Rochade (Damenseite)
            {
                Position rockStartPos = new Position( startPos.getRow(), 0);
                myAfterBoard.removePiece( rockStartPos );
                myAfterBoard.setPiece( new Position( startPos.getRow(), 3), beforeboard.getPiece( rockStartPos ) );
            }
        } else if(startPiece.getPieceType() == PieceType.PAWN){
            if ( !beforeboard.isPieceAt( endPos ) && startPos.getColumn() != endPos.getColumn() ) //En Passant
            {
                if(startPiece.getColor() == PieceColor.WHITE)
                {
                    myAfterBoard.removePiece( new Position( endPos.getRow() - 1, endPos.getColumn() ) );
                } else {
                    myAfterBoard.removePiece( new Position( endPos.getRow()+1, endPos.getColumn() ) );
                }
            }
            PieceColor pc = startPiece.getColor();
            switch ( pawnPromotes ) {
                case QUEEN:
                    myAfterBoard.setPiece( endPos, Piece.getPiece( PieceType.QUEEN, pc) );
                    break;
                case ROCK:
                    myAfterBoard.setPiece( endPos, Piece.getPiece( PieceType.ROCK, pc) );
                    break;
                case KNIGHT:
                    myAfterBoard.setPiece( endPos, Piece.getPiece( PieceType.KNIGHT, pc) );
                    break;
                case BISHOP:
                    myAfterBoard.setPiece( endPos, Piece.getPiece( PieceType.BISHOP, pc) );
                    break;
                default:
                    break;
            }
        }
        myAfterBoard.setPiece( endPos, startPiece);
        return myAfterBoard;
    }
}
