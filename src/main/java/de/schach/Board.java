package de.schach;

import java.util.ArrayList;

public class Board extends ArrayList<Piece>
{

    boolean whiteToMove;


    public Board()
    {
    }


    public void startstellung()
    {
        this.add( new Piece( PieceName.WPAWN, 'a', 2 ) );
        this.add( new Piece( PieceName.WPAWN, 'b', 2 ) );
        this.add( new Piece( PieceName.WPAWN, 'c', 2 ) );
        this.add( new Piece( PieceName.WPAWN, 'd', 2 ) );
        this.add( new Piece( PieceName.WPAWN, 'e', 2 ) );
        this.add( new Piece( PieceName.WPAWN, 'f', 2 ) );
        this.add( new Piece( PieceName.WPAWN, 'g', 2 ) );
        this.add( new Piece( PieceName.WPAWN, 'h', 2 ) );
        this.add( new Piece( PieceName.WROCK, 'a', 1 ) );
        this.add( new Piece( PieceName.WKNIGHT, 'b', 1 ) );
        this.add( new Piece( PieceName.WBISHOP, 'c', 1 ) );
        this.add( new Piece( PieceName.WQUEEN, 'd', 1 ) );
        this.add( new Piece( PieceName.WKING, 'e', 1 ) );
        this.add( new Piece( PieceName.WBISHOP, 'f', 1 ) );
        this.add( new Piece( PieceName.WKNIGHT, 'g', 1 ) );
        this.add( new Piece( PieceName.WROCK, 'h', 1 ) );

        this.add( new Piece( PieceName.BPAWN, 'a', 7 ) );
        this.add( new Piece( PieceName.BPAWN, 'b', 7 ) );
        this.add( new Piece( PieceName.BPAWN, 'c', 7 ) );
        this.add( new Piece( PieceName.BPAWN, 'd', 7 ) );
        this.add( new Piece( PieceName.BPAWN, 'e', 7 ) );
        this.add( new Piece( PieceName.BPAWN, 'f', 7 ) );
        this.add( new Piece( PieceName.BPAWN, 'g', 7 ) );
        this.add( new Piece( PieceName.BPAWN, 'h', 7 ) );
        this.add( new Piece( PieceName.BROCK, 'a', 8 ) );
        this.add( new Piece( PieceName.BKNIGHT, 'b', 8 ) );
        this.add( new Piece( PieceName.BBISHOP, 'c', 8 ) );
        this.add( new Piece( PieceName.BQUEEN, 'd', 8 ) );
        this.add( new Piece( PieceName.BKING, 'e', 8 ) );
        this.add( new Piece( PieceName.BBISHOP, 'f', 8 ) );
        this.add( new Piece( PieceName.BKNIGHT, 'g', 8 ) );
        this.add( new Piece( PieceName.BROCK, 'h', 8 ) );
    }

    public void Move()
    {
    }

    public void Replace()
    {
    }


    public PieceName getAt( char column, int line )
    {
        for ( Piece piece : this )
        {
            if ( piece.column == column )
            {
                if ( piece.line == line )
                {
                    return piece.name;
                }
            }
        }
        return null;
    }

    public boolean isFree( char column, int line )
    {
        if ( getAt( column, line ) == null )
        {
            return true;
        }
        return false;
    }


    public ArrayList<Integer[]> PrepareToDraw()
    {
        ArrayList<Integer[]> drawBoard = new ArrayList();
        for ( Piece piece : this )
        {
            Integer[] transformedPiece = new Integer[3];
            ArrayList<Integer> a = new ArrayList();
            if ( piece.name == PieceName.WKING )
            {
                transformedPiece[0] = 0;
            }
            else if ( piece.name == PieceName.WQUEEN )
            {
                transformedPiece[0] = 1;
            }
            else if ( piece.name == PieceName.WBISHOP )
            {
                transformedPiece[0] = 2;
            }
            else if ( piece.name == PieceName.WKNIGHT )
            {
                transformedPiece[0] = 3;
            }
            else if ( piece.name == PieceName.WROCK )
            {
                transformedPiece[0] = 4;
            }
            else if ( piece.name == PieceName.WPAWN )
            {
                transformedPiece[0] = 5;
            }
            else if ( piece.name == PieceName.BKING )
            {
                transformedPiece[0] = 6;
            }
            else if ( piece.name == PieceName.BQUEEN )
            {
                transformedPiece[0] = 7;
            }
            else if ( piece.name == PieceName.BBISHOP )
            {
                transformedPiece[0] = 8;
            }
            else if ( piece.name == PieceName.BKNIGHT )
            {
                transformedPiece[0] = 9;
            }
            else if ( piece.name == PieceName.BROCK )
            {
                transformedPiece[0] = 10;
            }
            else if ( piece.name == PieceName.BPAWN )
            {
                transformedPiece[0] = 11;
            }
            else
            {
                System.out.println( "Error" );
                System.exit( 0 );
            }

            if ( piece.column == 'a' )
            {
                transformedPiece[1] = 0;
            }
            else if ( piece.column == 'b' )
            {
                transformedPiece[1] = 1;
            }
            else if ( piece.column == 'c' )
            {
                transformedPiece[1] = 2;
            }
            else if ( piece.column == 'd' )
            {
                transformedPiece[1] = 3;
            }
            else if ( piece.column == 'e' )
            {
                transformedPiece[1] = 4;
            }
            else if ( piece.column == 'f' )
            {
                transformedPiece[1] = 5;
            }
            else if ( piece.column == 'g' )
            {
                transformedPiece[1] = 6;
            }
            else if ( piece.column == 'h' )
            {
                transformedPiece[1] = 7;
            }

            transformedPiece[2] = piece.line - 1;
            drawBoard.add( transformedPiece );
        }
        return drawBoard;
    }

//	public void PrintBoard() {
//		for(ArrayList )
//	}


}
