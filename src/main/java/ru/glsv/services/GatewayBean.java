package ru.glsv.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.glsv.scope.CalculationContext;
import ru.glsv.scope.SampleScope;
import ru.glsv.services.channels.IChannel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mark on 06.03.17.
 */
@Component
public class GatewayBean {

    private final SampleScope scope;
    private final ApplicationContext ctx;
    private static final Logger log = Logger.getLogger(GatewayBean.class);

    @Autowired
    public GatewayBean(SampleScope scope, ApplicationContext ctx) {
        this.scope = scope;
        this.ctx = ctx;
    }

    public List<Object> calculate(int deviceId, long from, long to, Collection<String> sysnames) {
        //Генерируем идентификатор запроса для скоупа
        String sid = Thread.currentThread().getId() + "||" + System.currentTimeMillis();

        //Активируем контекст
        scope.activate(sid);

        //Инициализируем контекст исполнения вычисления(свои данные какие-то)
        CalculationContext bean = ctx.getBean(CalculationContext.class);
        bean.init(deviceId, from, to);

        //Формируем индекс по системному имени. Раньше это сделать нельзя т.к. Scope - не тот. Нельзя будет заинжектить
        long before = System.currentTimeMillis();
        Map<String, IChannel> beansOfType = ctx.getBeansOfType(IChannel.class);
        Map<String, IChannel> index = beansOfType.entrySet()
                .stream()
                .filter((e -> e.getKey().startsWith("scopedTarget")))
                .collect(Collectors.toMap(e -> e.getValue().sysname(), Map.Entry::getValue));
        log.info("Index building takes: " + (System.currentTimeMillis() - before) + " milliseconds");

        //Вычисляем каждый из каналов который необходимо посчитать.
        List<Object> ret = new LinkedList<>();
        
        for (String name : sysnames) {
            IChannel handler = index.get(name);
            if (handler != null) {
                Object result = handler.calc();
                ret.add(result);
            } else
                ret.add(null);

        }

        //Деактивируем контекст
        scope.deactivate();

        return ret;
    }

}
