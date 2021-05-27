package de.schach.gui;

import de.schach.board.Board;
import de.schach.board.Position;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class DrawNotation extends JPanel
{


    Border border = BorderFactory.createLineBorder(Color.red, 2);

    int moveCount = 0;
    ArrayList<Move> playNotationWhite = new ArrayList<>();
    ArrayList<Move> playNotationBlack = new ArrayList<>();
    ArrayList<JButton> playNotationWhiteButtons = new ArrayList<>();
    ArrayList<JButton> playNotationBlackButtons = new ArrayList<>();
    ArrayList<String> playNotationWhiteString = new ArrayList<>();
    ArrayList<String> playNotationBlackString = new ArrayList<>();
    JPanel playWhiteMoves = new JPanel();
    JPanel playBlackMoves = new JPanel();
    JPanel playMoveCounter = new JPanel();

    final int PLAY_MOVE_HEIGHT = 25;
    final int PLAY_MOVE_WIDTH = 100;
    final int PLAY_MOVECOUNT_WEIDTH = 100;
    final int PUFFER_SIZE = 5;

    JPanel playScroll = new JPanel(); //Teil von playPanel
    JPanel playPanel = new JPanel(); //im playScrollPane
    JScrollPane playScrollPane; //der ganze Teil für die Play - Notation

    JPanel analysisScroll = new JPanel();
    JPanel analysisPanel = new JPanel();
    JScrollPane analysisScrollPane;


    public DrawNotation()
    {
        initScrollBar();

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds( 0, 0, 600, 800 );

        layeredPane.add( playScrollPane, Integer.valueOf( 1 ));
        layeredPane.add(analysisScroll, Integer.valueOf( 0 ));

        this.setBounds( 0, 0, 600, 800 );
        this.add( playScrollPane );
        this.setBackground( Color.BLUE );
        this.setVisible( true );
    }

    public void addPlayMove( Position startPos, Position endPos, Board beforeBoard ) //Wenn gerade gezogen wurde
    {
        Move m = new Move( startPos, endPos, beforeBoard ); //board is already a copy

        if ( m.whiteIsMoving() )
        {
            moveCount++;
            JButton moveCountButton = new JButton( String.valueOf( moveCount ) );
            moveCountButton.setEnabled( false );
            moveCountButton.setBackground( Color.WHITE );
            moveCountButton.setPreferredSize( new Dimension( PLAY_MOVECOUNT_WEIDTH, PLAY_MOVE_HEIGHT ) );
            playMoveCounter.add( moveCountButton );


            JButton whiteMove = new JButton( m.getAcronym() );
            whiteMove.setBackground( Color.WHITE );
            whiteMove.setPreferredSize( new Dimension( PLAY_MOVE_WIDTH, PLAY_MOVE_HEIGHT ) );
            whiteMove.addActionListener( e -> {
                System.out.println( e.getActionCommand() );
                System.out.println( playNotationWhiteString.indexOf( e.getActionCommand() ) );
            } );

            playWhiteMoves.add( whiteMove );
            playNotationWhiteButtons.add( whiteMove );
            playNotationWhite.add( m );
            playNotationWhiteString.add( m.getAcronym() );

        }
        else
        {
            JButton blackMove = new JButton( m.getAcronym() );
            blackMove.setBackground( Color.WHITE );
            blackMove.setPreferredSize( new Dimension( PLAY_MOVE_WIDTH, PLAY_MOVE_HEIGHT ) );

            playBlackMoves.add( blackMove );
            playNotationBlackButtons.add( blackMove );
            playNotationBlack.add( m );
            playNotationBlackString.add( m.getAcronym() );
        }

        playMoveCounter.setBounds( 0, 0, PLAY_MOVECOUNT_WEIDTH + PUFFER_SIZE, moveCount * PLAY_MOVE_HEIGHT );
        playWhiteMoves.setBounds( PLAY_MOVECOUNT_WEIDTH + PUFFER_SIZE, 0, PLAY_MOVE_WIDTH + PUFFER_SIZE, moveCount * PLAY_MOVE_HEIGHT );
        playBlackMoves.setBounds( PLAY_MOVECOUNT_WEIDTH + PLAY_MOVE_WIDTH + PUFFER_SIZE * 2, 0, PLAY_MOVE_WIDTH + PUFFER_SIZE, moveCount * PLAY_MOVE_HEIGHT );

        playScrollPane.revalidate();
        playScroll.setPreferredSize( new Dimension( PLAY_MOVE_WIDTH*2 + PLAY_MOVECOUNT_WEIDTH + +PUFFER_SIZE*3, moveCount * PLAY_MOVE_HEIGHT ) );
        playScrollPane.getVerticalScrollBar().setValue( playScrollPane.getVerticalScrollBar().getMaximum());
    }



    public void updateAllPlayMoves(){

    }


    void initScrollBar()
    {
        playPanel.add( playScroll );
        playScrollPane = new JScrollPane( playPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        playScrollPane.getVerticalScrollBar().setBlockIncrement( 100 );
        playScrollPane.getVerticalScrollBar().setUnitIncrement( 100 );

        playScroll.setPreferredSize( new Dimension( 100, 100 ) );
        playScrollPane.setPreferredSize( new Dimension( 500, 700 ) );


        playScroll.setLayout( null );
        playScroll.add( playMoveCounter );
        playScroll.add( playWhiteMoves );
        playScroll.add( playBlackMoves );
        playPanel.setBorder( border );
        playWhiteMoves.setLayout( new FlowLayout( 0, 0, 0 ) );
        playBlackMoves.setLayout( new FlowLayout( 0, 0, 0 ) );
        playMoveCounter.setLayout( new FlowLayout( 0, 0, 0 ) );
    }
}

