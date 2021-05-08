package de.schach;

import java.util.*;

public class Board
{

    private byte[] board;

    public Board()
    {
        loadFromFen( "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1" );
    }


    public void reset()
    {
        board = new byte[8 * 8];
    }

    public void loadFromFen( String fen )
    {

        reset();
        String squares = fen.substring( 0, fen.indexOf( ' ' ) );
        String data = fen.substring( fen.indexOf( ' ' ) + 1 );

        String[] settings = data.split( " " ); //TODO later, just skip it currently

        String[] ranks = squares.split( "/" );

        Scanner scanner;
        int currentField = 0;
        for ( String rank : ranks )
        {
            scanner = new Scanner( rank );
            scanner.useDelimiter( "\\r*" );
            if ( scanner.hasNext( "\\d" ) ) //number
            {
                int amount = scanner.nextInt();
                for ( int i = 0; i < amount; i++ )
                {
                    board[currentField++] = 0;
                }
            }
            else if ( scanner.hasNext( "\\w" ) ) //letter
            {
                char pieceToken = scanner.next().charAt( 0 );
                PieceType type = PieceType.fromChar( Character.toLowerCase( pieceToken ) );
                PieceColor color = Character.isUpperCase( pieceToken ) ? PieceColor.WHITE : PieceColor.BLACK;
                board[currentField++] = Piece.fromPartialData( type, color ).toByte();
            }
            else
            {
                throw new FenSyntaxException( "Invalid rank in FEN string: \"" + rank + "\"; cant read \"" + scanner.next() + "\"" );
            }
        }

    }

    public Piece getPiece( int row, int colum )
    {
        byte pieceData = board[row * 8 + colum];
        System.out.println();
        return Piece.fromByte( pieceData );
    }

    public Piece getPiece( Position position )
    {
        return getPiece( position.getRow(), position.getColumn() );
    }

    public void setPiece( int row, int colum, Piece piece )
    {
        board[row * 8 + colum] = piece == null ? 0 : piece.toByte();
    }

    public List<Position> getPositions( Piece piece )
    {
        List<Position> positions = new LinkedList<>();
        for ( int row = 0; row < 8; row++ )
        {
            for ( int col = 0; col < 8; col++ )
            {
                if ( Piece.fromByte( board[row * 8 + col] ) == piece )
                    positions.add( new Position( row, col ) );
            }
        }
        return positions;
    }

    public boolean canMoveTo( Piece piece, int startX, int startY, int wishX, int wishY )
    {
        if ( piece.getPieceType() == PieceType.PAWN )
        {
            if ( piece.getColor() == PieceColor.WHITE )
            {
                if ( startY == wishY )
                {

                }
            }
        }

        return false;
    }
    public boolean nothingBetween(int startX, int startY, int endX, int endY){ //noch nicht fertig
        if(startX == endX){
            for(int i = startY; i==endY; i++){
                if(this.getPiece(startX, i) != null){
                    return false;
                }
            }
        }
    }

    public PieceColor whatColorIsPieceAt(int x, int y){
        if(this.getPiece(x, y)==null){
            return null;
        } else {
            if(getPiece(x, y).getColor() == PieceColor.WHITE){
                return PieceColor.WHITE;
            } else {
                return PieceColor.BLACK;
            }
        }
    }


    public static boolean[] getPossibleMoves() {
        return null;
    }


}
