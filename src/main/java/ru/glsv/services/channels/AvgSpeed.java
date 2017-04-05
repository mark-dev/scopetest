package ru.glsv.services.channels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.glsv.ScopetestApplication;

import java.util.Collection;
import java.util.OptionalDouble;

/**
 * Created by mark on 06.03.17.
 */
@Component
@Scope(value = ScopetestApplication.SAMPLE_SCOPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AvgSpeed implements IChannel<Double> {

    @Autowired
    private GpsTrack trackHolder;


    @Override
    public Double calc() {
        Collection<Long> track = trackHolder.getTrack();

        OptionalDouble opt = track.stream().mapToLong(value -> value).average();
        if (opt.isPresent())
            return opt.getAsDouble();
        return 0.0;

    }

    @Override
    public String sysname() {
        return IChannel.AVG_SPEED;
    }
}
