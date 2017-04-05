package ru.glsv.services.channels;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.glsv.ScopetestApplication;
import ru.glsv.scope.CalculationContext;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by mark on 06.03.17.
 */
@Component
@Scope(value = ScopetestApplication.SAMPLE_SCOPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GpsTrack {

    private static final Logger log = Logger.getLogger(GpsTrack.class);

    @Autowired
    private CalculationContext context;

    private Collection<Long> track;

    @PostConstruct
    private void init() {

        track = loadTrackFromDB();
    }

    private Collection<Long> loadTrackFromDB() {

        log.info("Loading track from db, ctx: " + context);
        if (simulateDelay()) {
            return Arrays.asList(1L, 3L, 4L, 1L, 2L, 8L);
        }
        return Arrays.asList(1L, 3L, 4L, 1L, 2L);
    }

    private boolean simulateDelay() {
        if (context.getDeviceId() % 2 == 0) {
            try {
                TimeUnit.SECONDS.sleep(5);
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Collection<Long> getTrack() {
        return track;
    }
}
