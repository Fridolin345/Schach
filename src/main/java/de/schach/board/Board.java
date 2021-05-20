package de.schach.board;

import de.schach.exception.FenSyntaxException;
import de.schach.logic.BoardLogic;
import de.schach.util.*;

import java.util.Arrays;
import java.util.Scanner;

public class Board
{

    private byte[] board;
    private PieceColor topColor;
    private Position enPassantPosition = null;
    private boolean[] allowedCastles = new boolean[4]; // [K,Q,k,q]

    public Board()
    {
        loadFromFen( "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1" );
    }

    private Board( Board board )
    {
        this.board = board.board;
        this.enPassantPosition = board.enPassantPosition;
        this.topColor = board.topColor;
        this.allowedCastles = board.allowedCastles;
        this.enPassantPosition = board.enPassantPosition;
    }

    public static Board create( Board board )
    {
        return new Board( board );
    }

    public static Board create()
    {
        return new Board();
    }

    public void reset()
    {
        board = new byte[8 * 8];
    }

    public void setBoard( byte[] board )
    {
        this.board = board;
    }

    public Board getInvertedCopy()
    {
        Board board = new Board( this );
        board.board = ArrayUtil.rowFlippedBoard( this.board );
        return board;
    }

    public BoardLogic getLogic()
    {
        return BoardLogic.ofBoard( this );
    }

    public boolean isEnPassant( Position position )
    {
        return position.equals( this.enPassantPosition );
    }

    public void setEnPassantPosition( Position enPassantPosition )
    {
        this.enPassantPosition = enPassantPosition;
        Debug.log( "New EnPassant position:" );
        Debug.log( this.enPassantPosition );
    }

    public void unsetEnPassantPosition()
    {
        this.enPassantPosition = null;
    }

    public boolean isAllowedToCastle( PieceColor color, boolean kingSide )
    {
        return kingSide ? allowedCastles[( color == PieceColor.BLACK ? 2 : 0 )] : allowedCastles[( color == PieceColor.BLACK ? 3 : 1 )];
    }

    public void setNotAllowedToCastle( PieceColor color, boolean kingSide )
    {
        if ( kingSide )
            allowedCastles[( color == PieceColor.BLACK ? 2 : 0 )] = false;
        else
            allowedCastles[( color == PieceColor.BLACK ? 3 : 1 )] = false;
    }

    public Piece getPiece( Position position )
    {
        byte pieceData = board[position.getBoardPosition()];
        return Piece.fromByte( pieceData );
    }

    public boolean isPieceAt( Position position )
    {
        return board[position.getBoardPosition()] != 0;
    }

    public void setPiece( Position position, Piece piece )
    {
        board[position.getBoardPosition()] = piece == null ? 0 : piece.toByte();
    }

    public Board getCopy()
    {
        Board copyB = new Board();
        for ( int row = 0; row < 8; row++ )
        {
            for ( int col = 0; col < 8; col++ )
            {
                copyB.setPiece( new Position( row, col ), this.getPiece( new Position( row, col ) ) );
            }
        }
        return copyB;
    }

    public byte[] getCopy( boolean byteForm )
    {
        byte[] copyB = new byte[8 * 8];
        for ( int row = 0; row < 8; row++ )
        {
            for ( int col = 0; col < 8; col++ )
            {
                if ( isPieceAt( new Position( row, col ) ) )
                {
                    copyB[row * 8 + col] = this.getPiece( new Position( row, col ) ).toByte();
                }
            }
        }
        return copyB;
    }

    public void movePiece( Position from, Position to )
    {
        Piece piece = getPiece( from );
        setPiece( to, piece );
        setPiece( from, null );
        getLogic().postMoveChanges( piece, from, from.getDiff( to ) );
    }

    public PieceColor topColor()
    {
        return topColor;
    }

    public Vector getOffensiveDirection( PieceColor color )
    {
        if ( topColor == PieceColor.BLACK )
        {
            return color == PieceColor.WHITE ? new Vector( 0, -1 ) : new Vector( 0, 1 );
        }
        else return color == PieceColor.WHITE ? new Vector( 0, 1 ) : new Vector( 0, -1 );
    }

    public PieceColor pieceColorAt( Position position )
    {
        if ( position == null )
        {
            return null;
        }
        return isPieceAt( position ) ? null : getPiece( position ).getColor();
    }

    public void loadFromFen( String fen )
    {

        reset();
        String squares = fen.substring( 0, fen.indexOf( ' ' ) );
        String data = fen.substring( fen.indexOf( ' ' ) + 1 );

        String[] settings = data.split( " " ); //TODO later, just skip it currently

        //Who's turn
        if ( settings[0].equalsIgnoreCase( "w" ) )
            topColor = PieceColor.BLACK;
        else topColor = PieceColor.WHITE;

        //Castles
        Arrays.fill( allowedCastles, false );
        if ( !settings[1].equalsIgnoreCase( "-" ) )
        {
            for ( int i = 0; i < settings[1].length(); i++ )
            {
                switch ( settings[1].charAt( i ) )
                {
                    case 'K':
                        allowedCastles[0] = true;
                        break;
                    case 'Q':
                        allowedCastles[1] = true;
                        break;
                    case 'k':
                        allowedCastles[2] = true;
                        break;
                    case 'q':
                        allowedCastles[3] = true;
                        break;
                }
            }
        }

        //EnPassant
        if ( !settings[2].equalsIgnoreCase( "-" ) )
        {
            this.enPassantPosition = Position.ofNotation( settings[2] );
        }
        else this.enPassantPosition = null;

        String[] ranks = squares.split( "/" );

        Scanner scanner;
        int currentField = 0;
        for ( String rank : ranks )
        {
            scanner = new Scanner( rank );
            scanner.useDelimiter( "\\r*" );
            while ( scanner.hasNext() )
            {
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
                    Piece piece = Piece.getPiece( type, color );
                    board[currentField++] = piece.toByte();
                }
                else
                {
                    throw new FenSyntaxException( "Invalid rank in FEN string: \"" + rank + "\"; cant read \"" + scanner.next() + "\"" );
                }
            }
        }

    }

    public void removePieceAt( int row, int col )
    {
        board[row * 8 + col] = 0;
    }


}
