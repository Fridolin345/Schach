package de.schach;

/**
 * <h1>Schach</h1>
 *
 * @author Julius Korweck
 * @version 11.05.2021
 * @since 11.05.2021
 */
public class Debug
{

    private static boolean debug;

    public static void setDebug( boolean debug )
    {
        Debug.debug = debug;
    }

    public static void log( Object log )
    {
        log( log.toString() );
    }

    public static void log( String log )
    {
        if ( debug )
            System.out.println( log );
    }

    public static void err( Object err )
    {
        err( err.toString() );
    }

    public static void err( String err )
    {
        if ( debug )
            System.err.println( err );
    }

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/