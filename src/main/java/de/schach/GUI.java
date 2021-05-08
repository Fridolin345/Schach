package de.schach;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;

public class GUI extends JFrame
{

    public GUI() throws IOException
    {
        this.setLayout( new BorderLayout() );
        this.setSize( 1600, 900 );
        this.setLocationRelativeTo( null );
        this.setDefaultCloseOperation( EXIT_ON_CLOSE );

        Border b1 = BorderFactory.createLineBorder( Color.red, 5 );
        Border b2 = BorderFactory.createLineBorder( Color.green, 2 );

        DrawField drawField = new DrawField();
        drawField.setBounds( 10, 50, 510, 510 );
        drawField.setBorder( b1 );

        JPanel leftSide = new JPanel();
        leftSide.setPreferredSize( new Dimension( 550, 1000 ) );
        leftSide.setLayout( null );
        leftSide.add( drawField );
        leftSide.setBorder( b2 );

        repaint();
        this.add( leftSide, BorderLayout.WEST );
        this.add( new Button(), BorderLayout.CENTER );
        this.setVisible( true ); //test
    }
}
