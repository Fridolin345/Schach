package de.schach.gui;

import de.schach.board.*;
import de.schach.util.Debug;
import de.schach.util.Vector;

import java.util.*;

import static de.schach.board.Piece.*;

public class Move
{

    private final Position startPos;
    private final Position endPos;
    public final Board beforeboard;
    static final String[] specialMoves = { "king side castle", "queen side castle", "en passant", "pawn to queen", "pawn to knight", "pawn to bishop",
            "pawn to  rock" };
    private String mySpecialMove = "";

    public Move( Position startPos, Position endPos, Board beforeboard )
    {
        Debug.log( "new Move" );
        Debug.log( "startPos: " + startPos.toNotation() + ", endPos: " + endPos.toNotation() );
        this.startPos = startPos;
        this.endPos = endPos;
        this.beforeboard = beforeboard;
    }

    public Move ( Position startPos, Position endPos, Board beforeboard, String special)
    {
        System.out.println( "new Move" );
        System.out.println( "startPos: " + startPos.toNotation() + "  endPos: " + endPos.toNotation() );
        this.startPos = startPos;
        this.endPos = endPos;
        this.beforeboard = beforeboard;
        for ( String s : specialMoves )
        {
            if ( s.toLowerCase().matches( special ) )
            {
                this.mySpecialMove = special;
            }
        }
        if ( this.mySpecialMove.matches( "" ) )
        {
            System.out.println( "specialMove is not available" );
            System.exit( 0 );
        }
    }

    public String getAcronym()
    {
        if ( mySpecialMove.matches( "king side castle" ) )
        {
            return "0-0";
        }
        else if ( mySpecialMove.matches( "queen side castle" ) )
        {
            return "0-0-0";
        }


        Piece startPiece = this.beforeboard.getPiece( startPos );
        String acronym = "";


        boolean hits = beforeboard.isPieceAt( endPos );
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
            if ( hits )
            {
                acronym += "x";
            }
        }
        else
        { //Wenn es ein Bauer ist
            if ( hits || mySpecialMove.matches( "en passant" ) )
            {
                acronym += startPos.colToNotation();
                acronym += "x";
            }
            switch ( mySpecialMove )
            {
                case "pawn to queen":
                    acronym += "=Q";
                    break;
                case "pawn to knight":
                    acronym += "=N";
                    break;
                case "pawn to bishop":
                    acronym += "=B";
                    break;
                case "pawn to  rock":
                    acronym += "=R";
                    break;
                default:
                    acronym += "=???";
                    break;
            }

        }
        acronym += endPos.toNotation();
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
        return ( beforeboard.getPiece( startPos ).getColor() == PieceColor.WHITE );
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
}
