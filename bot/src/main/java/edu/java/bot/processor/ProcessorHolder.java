package edu.java.bot.processor;

import edu.java.bot.processor.processors.DefaultHandler;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ProcessorHolder {

    final List<MethodProcessor> ROADS;

    @Qualifier("MapHandlerContainer")
    private final Map<String, MethodProcessor> handlerContainer;

    private ProcessorHolder(List<MethodProcessor> roads) {
        ROADS = roads;
        handlerContainer = getHandlerContainer();
    }

    public MethodProcessor getCommandByName(String name) {
        return handlerContainer.getOrDefault(name, new DefaultHandler());
    }

    public Collection<MethodProcessor> getAllCommands() {
        return handlerContainer.values();
    }

    private Map<String, MethodProcessor> getHandlerContainer() {
        return this.ROADS.stream().collect(
            Collectors.toMap(MethodProcessor::getName, Function.identity())
        );
    }
}
