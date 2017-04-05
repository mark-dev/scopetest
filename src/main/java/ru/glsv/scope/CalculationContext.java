package ru.glsv.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.glsv.ScopetestApplication;

/**
 * Created by mark on 06.03.17.
 */
@Component
@Scope(value = ScopetestApplication.SAMPLE_SCOPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CalculationContext {
    private int deviceId;
    private long from;
    private long to;


    public void init(int deviceId, long from, long to) {
        this.deviceId = deviceId;
        this.from = from;
        this.to = to;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public long getFrom() {
        return from;
    }

    public long getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "CalculationContext{" +
                "deviceId=" + deviceId +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
