package de.schach.gui.settings;

public interface Setting<T>
{

    void change( T newValue );

    T current();

}