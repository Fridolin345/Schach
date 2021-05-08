package de.schach;

public class Piece
{

    char column;
    int line;
    PieceName name;


    Piece( PieceName name, char column, int line )
    {
        this.column = column;
        this.line = line;
        this.name = name;
    }


    public void Move()
    {
        switch ( this.name )
        {
            case BBISHOP:
                System.out.println( "ok" );
                break;

        }

    }

}
