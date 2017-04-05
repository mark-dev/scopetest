package ru.glsv.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.glsv.services.GatewayBean;
import ru.glsv.services.channels.IChannel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by mark on 06.03.17.
 */
@Controller
public class SampleController {
    private final GatewayBean bean;


    @Autowired
    public SampleController(GatewayBean bean) {
        this.bean = bean;
    }

    @ResponseBody
    @RequestMapping("/{deviceId}")
    List<Object> home(@PathVariable(value = "deviceId") int deviceId) {
        Collection<String> channels = Arrays.asList(IChannel.AVG_SPEED, IChannel.MAX_SPEED);
        return bean.calculate(deviceId, System.currentTimeMillis(), System.currentTimeMillis(), channels);
    }
}
