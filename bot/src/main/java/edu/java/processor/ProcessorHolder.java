package edu.java.processor;

import edu.java.processor.processors.DefaultHandler;
import edu.java.processor.processors.HelpHandler;
import edu.java.processor.processors.ListHandler;
import edu.java.processor.processors.StartHandler;
import edu.java.processor.processors.TrackHandler;
import edu.java.processor.processors.UntrackHandler;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProcessorHolder {

    static final Map<String, MethodProcessor> ROADS = new HashMap<>() {{
        put("/start", new StartHandler());
        put("/help", new HelpHandler());
        put("/track", new TrackHandler());
        put("/untrack", new UntrackHandler());
        put("/list", new ListHandler());
    }};

    private ProcessorHolder() {
    }

    public static MethodProcessor getCommandByName(String name) {
        return ROADS.getOrDefault(name, new DefaultHandler());
    }

    public static Collection<MethodProcessor> getAllCommands() {
        return ROADS.values();
    }
}
