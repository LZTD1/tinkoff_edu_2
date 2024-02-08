package processor;

import database.SimpleDatabase;
import java.util.HashMap;
import java.util.Map;
import processor.methods.Default;
import processor.methods.Help;
import processor.methods.List;
import processor.methods.Start;
import processor.methods.Track;
import processor.methods.Untrack;

public class Processor {

    private Processor() {
    }

    static Map<String, MethodProcessor> roads = new HashMap<>() {{
        put("/start", new Start());
        put("/help", new Help());
        put("/track", new Track());
        put("/untrack", new Untrack());
        put("/list", new List());
    }};

    public static String getAnswer(String text, Long chatId, SimpleDatabase database) {
        String[] separated = separator(text);
        MethodProcessor answer = roads.getOrDefault(separated[0], new Default());
        return answer.get(separated, chatId, database);
    }

    private static String[] separator(String command) {
        return command.split(" ");
    }
}
