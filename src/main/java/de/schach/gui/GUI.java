package de.schach.gui;

import de.schach.board.Board;
import de.schach.gui.buttons.*;
import de.schach.util.Debug;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GUI extends JFrame
{


    private final Board gameBoard = Board.create();

    JPanel windowPanel;
    Dimension windowPanelSize;
    Border red, b2, white;

    JPanel leftSide;
    public Dimension prefferedLSSize;
    DrawField drawField;

    JPanel rightSide;
    public Dimension preferredRSSize;
    JPanel rightTopSidePanel; //Das Panel enthält buttonsPanel und den moreButton
    JPanel buttonsPanel;
    public static ArrayList<JButton> buttonsList;
    public static Dimension prefferedButtonSize;
    public static DrawNotation notation;
    public static boolean buttonsPanelshowAll;
    JButton showMoreButton;

    JMenuBar menuBar;

    public GUI() throws IOException
    {
        windowPanel = new JPanel();
        red = BorderFactory.createLineBorder( Color.red, 1 );
        b2 = BorderFactory.createLineBorder( Color.green, 2 );
        white = BorderFactory.createLineBorder( Color.white, 1 );


        initComponents();

        windowPanel.setLayout( new BorderLayout() );
        windowPanel.add( leftSide, BorderLayout.WEST );
        windowPanel.add( rightSide, BorderLayout.EAST );

        initMenuBar();

        this.setJMenuBar( menuBar );
        this.setLayout( new BorderLayout() );
        this.setSize( 1400, 900 );
        this.setLocationRelativeTo( null );
        this.setDefaultCloseOperation( EXIT_ON_CLOSE );
        this.add( windowPanel, BorderLayout.CENTER );
        this.setVisible( true );
        //TODO resizeabel mit Listener
        repaint();
    }


    public void uptadeChange()
    {
        //TODO Hier werden die Größen festgelegt
    }

    public void getSettings()
    {
        //Die Einstellungen sollen gespeichert werden können, und genau so wieder aufgerufen werden
    }

    public void initComponents() throws IOException
    {
        prefferedLSSize = new Dimension( 550, 1000 );
        preferredRSSize = new Dimension( 800, 1000 );
        prefferedButtonSize = new Dimension( 180, 100 );

        //LeftSide:
        drawField = new DrawField( gameBoard );
        drawField.setBounds( 10, 100, 510, 510 );

        leftSide = new JPanel();
        leftSide.setPreferredSize( prefferedLSSize );
        leftSide.setLayout( null );
        leftSide.add( drawField );
        leftSide.setBorder( red );

        //RightSide:
        rightSide = new JPanel();
        rightSide.setPreferredSize( preferredRSSize );
        rightSide.setLayout( new BorderLayout() );
        notation = new DrawNotation();
        initButtons();

        rightSide.add( notation, BorderLayout.CENTER );
        rightSide.add( rightTopSidePanel, BorderLayout.NORTH );
        rightSide.setBorder( red );
    }


    void initButtons()
    {
        rightTopSidePanel = new JPanel();
        rightTopSidePanel.setPreferredSize( new Dimension( (int) preferredRSSize.getWidth(), 100 ) );
        rightTopSidePanel.setLayout( null );

        showMoreButton = new JButton( "V" );
        showMoreButton.setBounds( (int) preferredRSSize.getWidth() - 50, 0, 50, prefferedButtonSize.height );
        showMoreButton.setBackground( Color.GRAY );

        buttonsList = new ArrayList<>();
        buttonsList.add( new PlayButton() );
        buttonsList.add( new AnalyseButton() );
        buttonsList.add( new LetterButton( 'A' ) );
        buttonsList.add( new LetterButton( 'B' ) );
        buttonsList.add( new LetterButton( 'C' ) );

        //Reihen und Spalten Anzahl festlegen:
        int colAmmount = (int) ( preferredRSSize.getWidth() / prefferedButtonSize.getWidth() );
        int rowAmmount = 0;
        if ( colAmmount > 1 )
        {
            for ( int i = 0; i < buttonsList.size(); i += colAmmount )
            {
                rowAmmount++;
            }
        }
        else
        {
            rowAmmount = 1;
        }
        Debug.log( "colAmmount: " + colAmmount + ",   rowAmmount: " + rowAmmount );

        buttonsPanel = new JPanel();
        buttonsPanel.setBorder( b2 );
        buttonsPanel.setLayout( new GridLayout( rowAmmount, colAmmount ) );
        buttonsPanel.setBounds( 0, 0, (int) preferredRSSize.getWidth() - showMoreButton.getWidth(), (int) ( prefferedButtonSize.getHeight() * rowAmmount ) );

        for ( JButton button : buttonsList )
        {
            buttonsPanel.add( button );
        }

        for ( int i = 0; i < ( colAmmount * rowAmmount - buttonsList.size() ); i++ )
        {
            JButton temp = new JButton( "" );
            temp.setFocusable( false );
            temp.setBackground( Color.WHITE );
            temp.setBorder( white );
            buttonsPanel.add( temp );
        }

        rightTopSidePanel.add( buttonsPanel );
        rightTopSidePanel.add( showMoreButton );
    }

//    JButton initMyButton( String whatIsWished )
//    {
//        JButton myB = new JButton();
//        myB.setBackground( Color.WHITE );
//
//
//        switch ( whatIsWished.toLowerCase( Locale.ROOT ) )
//        {
//            case "play":
//                myB.setText( "Play" );
//                myB.addActionListener( PlayButton.getInstance() );
//                break;
//            case "analyse":
//                myB.setText( "Analyse" );
//                myB.addActionListener( new ActionListener()
//                {
//                    @Override
//                    public void actionPerformed( ActionEvent e )
//                    {
//                        System.out.println( "analyse got pressed" );
//                    }
//                } );
//                break;
//            default:
//                System.out.println( "Button konnte nicht geladen werden" );
//                myB.setText( whatIsWished );
//        }
//        return myB;
//    }

    void initMenuBar()
    {
        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu( "File" );
        JMenuItem openItem = new JMenuItem( "Open" );
        JMenuItem openRecentlyItem = new JMenuItem( "Open Recently" );
        JMenuItem saveItem = new JMenuItem( "Save" );
        JMenuItem printItem = new JMenuItem( "Print" );
        JMenuItem accountItem = new JMenuItem( "Account" );
        JMenuItem exitItem = new JMenuItem( "Exit" );
        fileMenu.add( openItem );
        fileMenu.add( openRecentlyItem );
        fileMenu.add( saveItem );
        fileMenu.add( printItem );
        fileMenu.add( accountItem );
        fileMenu.add( exitItem );

        JMenu loadMenu = new JMenu( "Load" );
        JMenuItem loadfenItem = new JMenuItem( "Load from FEN" );
        JMenuItem loadpgnItem = new JMenuItem( "Load from PGN" );
        JMenuItem loadSettingdataItem = new JMenuItem( "Load Settingdata" );
        loadMenu.add( loadfenItem );
        loadMenu.add( loadpgnItem );
        loadMenu.add( loadSettingdataItem );

        JMenu settingsMenu = new JMenu( "Settings" );
        JMenuItem allSettingsItem = new JMenuItem( "All Settings" );
        JMenuItem engineSettingsItem = new JMenuItem( "Engine" );
        JMenuItem chessBoardItem = new JMenuItem( "Chessboard" );
        JMenuItem piecesItem = new JMenuItem( "Pieces" );
        JMenuItem fontsItem = new JMenuItem( "Fonts" );
        JMenuItem themesItem = new JMenuItem( "Themes" );
        JMenuItem toolbuttonsItem = new JMenuItem( "Toolbuttons" );
        JMenuItem menubarItem = new JMenuItem( "Menubar" );
        //Combobox Premoves allowed?
        settingsMenu.add( allSettingsItem );
        settingsMenu.add( accountItem );
        settingsMenu.add( engineSettingsItem );
        settingsMenu.add( chessBoardItem );
        settingsMenu.add( piecesItem );
        settingsMenu.add( fontsItem );
        settingsMenu.add( themesItem );
        settingsMenu.add( toolbuttonsItem );
        settingsMenu.add( menubarItem );

        JMenu engineMenu = new JMenu( "Engine" );
        JMenuItem analyseItem = new JMenuItem( "Start Analysis" );
        JMenuItem gameReportItem = new JMenuItem( "Create Game Report" );
        JMenuItem vsEngineItem = new JMenuItem( "Play against Computer" );
        JMenuItem engineVSengineItem = new JMenuItem( "Computer vs Computer" );
        //Create Own Engine? Vlt. dass man eigene Algorythmen erstellen kann
        //Combobox Show best moves?
        //Restore default Settings
        engineMenu.add( analyseItem );
        engineMenu.add( engineSettingsItem );
        engineMenu.add( gameReportItem );
        engineMenu.add( vsEngineItem );
        engineMenu.add( engineVSengineItem );


        JMenu viewMenu = new JMenu( "View" );
        JMenuItem moveAnimation = new JMenuItem( "Move Animation" );
        JMenuItem organizeBoardGraphicItem = new JMenuItem( "Organize Board Graphic" ); //Wenn man z.B. das Brett lieber rechts von der Notation hat
        //Pfeilfarben und allgemeine Farben
        viewMenu.add( chessBoardItem );
        viewMenu.add( piecesItem );
        viewMenu.add( fontsItem );
        viewMenu.add( themesItem );
        viewMenu.add( moveAnimation );
        viewMenu.add( organizeBoardGraphicItem );

        JMenu helpMenu = new JMenu( "Help" );
        JMenuItem introductionGuideItem = new JMenuItem( "Introduction Guide" );
        JMenuItem learnChessItem = new JMenuItem( "Learn Chess" );
        JMenuItem aboutThisAppItem = new JMenuItem( "About this App" );
        helpMenu.add( introductionGuideItem );
        helpMenu.add( learnChessItem );
        helpMenu.add( aboutThisAppItem );


        menuBar.add( fileMenu );
        menuBar.add( loadMenu );
        menuBar.add( settingsMenu );
        menuBar.add( engineMenu );
        menuBar.add( viewMenu );
        menuBar.add( helpMenu );

        menuBar.setBackground( Color.WHITE );
    }


}