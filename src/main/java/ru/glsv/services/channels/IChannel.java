package ru.glsv.services.channels;

/**
 * Created by mark on 06.03.17.
 */
public interface IChannel<T> {
    public static final String AVG_SPEED = "avgspeed";
    public static final String MAX_SPEED = "maxspeed";

    T calc();

    String sysname();
}

