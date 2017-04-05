package ru.glsv.scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by mark on 06.03.17.
 */
public class SampleScope implements Scope {
    private Map<String, Map<String, Object>> instances = new HashMap<String, Map<String, Object>>();
    private Map<String, Runnable> destructionCollbacks = new HashMap<>();

    private final ThreadLocal<String> currentSessionId = ThreadLocal.withInitial(() -> null);

    public String getCurrentSessionId() {
        return currentSessionId.get();
    }

    public void setCurrentSessionId(String currentSessionId) {
        this.currentSessionId.set(currentSessionId);
    }


    public void activate(String sessionId) {
        instances.put(sessionId, new HashMap<>());
        this.currentSessionId.set(sessionId);
    }

    public void deactivate() {
        String id = currentSessionId.get();
        Thread currentThread = Thread.currentThread();
        Map<String, Object> map = instances.get(id);
        if (map == null) {
            throw new RuntimeException("SampleScope with id =" + id + " doesn't exist");
        }
        Map<String, Object> objectsMap = instances.get(id);
        Set<String> keySet = objectsMap.keySet();
        for (String name : keySet) {
            remove(name, true);
        }
        instances.remove(id);
        currentSessionId.set(null);
    }

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Object object = resolveContextualObject(name);
        if (object != null) {
            return object;
        }
        String sessionId = currentSessionId.get();
        if (sessionId == null) {
            throw new RuntimeException("SampleScope is inactive");
        }
        Map<String, Object> map = instances.get(sessionId);
        if (map == null) {
            throw new RuntimeException("SampleScope is inactive");
        }
        object = objectFactory.getObject();
        map.put(name, object);
        return object;
    }

    private Object remove(String name, boolean keep) {
        String sessionId = currentSessionId.get();
        if (sessionId == null) {
            throw new RuntimeException("SampleScope is inactive");
        }
        Map<String, Object> map = instances.get(sessionId);
        if (map == null) {
            throw new RuntimeException("SampleScope is inactive");
        }
        Runnable runnable = destructionCollbacks.get(name);
        Thread t = new Thread(runnable);
        t.start();
        return keep ? map.get(name) : map.remove(name);
    }

    public Object remove(String name) {
       return remove(name, false);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        destructionCollbacks.put(name, callback);
    }

    public Object resolveContextualObject(String name) {
        String sessionId = currentSessionId.get();
        if (sessionId == null) {
            return null;
        }
        Map<String, Object> map = instances.get(sessionId);
        if (map == null) {
            return null;
        }
        Object object = map.get(name);
        return object;
    }

    @Override
    public String getConversationId() {
        return currentSessionId.get();
    }

}
