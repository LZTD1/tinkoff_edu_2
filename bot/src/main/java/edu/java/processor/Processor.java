package edu.java.processor;

import edu.java.processor.methods.Default;
import edu.java.processor.methods.Help;
import edu.java.processor.methods.List;
import edu.java.processor.methods.Start;
import edu.java.processor.methods.Track;
import edu.java.processor.methods.Untrack;
import java.util.HashMap;
import java.util.Map;

public class Processor {

    static final Map<String, MethodProcessor> ROADS = new HashMap<>() {{
        put("/start", new Start());
        put("/help", new Help());
        put("/track", new Track());
        put("/untrack", new Untrack());
        put("/list", new List());
    }};

    private Processor() {
    }

    public static MethodProcessor getCommandByQuery(String query) {
        return ROADS.getOrDefault(query.split(" ")[0], new Default());
    }

    public static Map<String, MethodProcessor> getAllCommands() {
        return ROADS;
    }
}
