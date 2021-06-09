package de.schach.gui.buttons;

import de.schach.util.Debug;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnalyseButton extends JButton implements ActionListener
{

    public AnalyseButton()
    {
        super( "Analyse" );
        setBackground( Color.WHITE );
        addActionListener( this );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        //play button was pressed
        Debug.log( "Analyse was pressed" );
    }
}