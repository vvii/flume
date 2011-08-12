package org.apache.flume.sink;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.flume.EventSink;
import org.apache.flume.SinkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class DefaultSinkFactory implements SinkFactory {

  private static final Logger logger = LoggerFactory
      .getLogger(DefaultSinkFactory.class);

  public Map<String, Class<? extends EventSink>> sinkRegistry;

  public DefaultSinkFactory() {
    sinkRegistry = new HashMap<String, Class<? extends EventSink>>();
  }

  @Override
  public boolean register(String name, Class<? extends EventSink> sinkClass) {
    logger.info("Register sink class:{}", sinkClass);

    if (sinkRegistry.containsKey(name)) {
      return false;
    }

    sinkRegistry.put(name, sinkClass);
    return true;
  }

  @Override
  public boolean unregister(String name) {
    logger.info("Unregister source class:{}", name);

    return sinkRegistry.remove(name) != null;
  }

  @Override
  public Set<String> getSinkNames() {
    return sinkRegistry.keySet();
  }

  @Override
  public EventSink create(String name) throws InstantiationException {
    Preconditions.checkNotNull(name);

    logger.info("Creating instance of source {}", name);

    if (!sinkRegistry.containsKey(name)) {
      return null;
    }

    EventSink sink = null;

    try {
      sink = sinkRegistry.get(name).newInstance();
    } catch (IllegalAccessException e) {
      throw new InstantiationException("Unable to create sink " + name
          + " due to " + e.getMessage());
    }

    return sink;
  }

  @Override
  public String toString() {
    return "{ sinkRegistry:" + sinkRegistry + " }";
  }

  public Map<String, Class<? extends EventSink>> getSinkRegistry() {
    return sinkRegistry;
  }

  public void setSinkRegistry(
      Map<String, Class<? extends EventSink>> sinkRegistry) {
    this.sinkRegistry = sinkRegistry;
  }

}