package ru.glsv.services.channels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.glsv.ScopetestApplication;

import java.util.Collection;
import java.util.OptionalLong;

/**
 * Created by mark on 06.03.17.
 */
@Component
@Scope(value = ScopetestApplication.SAMPLE_SCOPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MaxSpeed implements IChannel<Long> {

    @Autowired
    private GpsTrack trackHolder;

    @Override
    public Long calc() {


        Collection<Long> track = trackHolder.getTrack();
        OptionalLong opt = track.stream().mapToLong(value -> value).max();

        if (!opt.isPresent())
            return 0L;
        else
            return opt.getAsLong();
    }

    @Override
    public String sysname() {
        return IChannel.MAX_SPEED;
    }
}
