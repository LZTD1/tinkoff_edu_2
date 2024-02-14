package edu.java.processor.processors;

import edu.java.processor.MethodProcessor;
import java.util.stream.Collectors;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.processor.ProcessorHolder.getAllCommands;

public class HelpHandler implements MethodProcessor {

    @Override
    public String handle(Update update) {
        return getAllCommands()
            .stream()
            .map(entry -> entry.getName() + " - " + entry.getDescription() + " \n")
            .collect(Collectors.joining());
    }

    @Override
    public String getDescription() {
        return "вывести окно с командами";
    }

    @Override
    public String getName() {
        return "/help";
    }
}
