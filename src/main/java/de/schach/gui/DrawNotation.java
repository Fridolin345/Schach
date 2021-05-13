package de.schach.gui;

import de.schach.board.Board;
import de.schach.board.Position;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DrawNotation extends JPanel
{

    JPanel playNotation = new JPanel();
    JPanel AnalysisNotation = new JPanel();

    JButton notationButton = new JButton();
    JButton analysisButton = new JButton();

    //ArrayList<Move> playMoves = new ArrayList<>();
    ArrayList<JButton> buttons = new ArrayList<>();

    int moveCount = 0;


    public void addPlayMove( Position startPos, Position endPos, Board beforeBoard )
    {
        Move m = new Move( startPos, endPos, beforeBoard.getCopy() );
        buttons.get( moveCount ).setText( m.getAcronym() );
        moveCount++;
        repaint();
    }

    public DrawNotation()
    {
        for(int i=0; i<40; i++){
            JButton myButton = new JButton("");
            myButton.setFocusable( false );
            buttons.add( myButton );
            playNotation.add( myButton );
        }

        playNotation.setLayout( new GridLayout( 20, 3 ) );
        playNotation.setBounds( 0, 50, 550, 800 );
        this.setLayout( null );
        this.add( playNotation );
        this.setBackground( Color.BLACK );
    }


}
