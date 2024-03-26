package edu.java.bot.processor.processors;

import edu.java.bot.processor.MethodProcessor;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HelpHandler implements MethodProcessor {

    public static final String PAPER_EMOJI = "\uD83D\uDCC4";
    private List<MethodProcessor> methodProcessors;

    public HelpHandler(List<MethodProcessor> methodProcessors) {
        this.methodProcessors = methodProcessors;
    }

    @Override
    public String handle(Update update) {
        return new StringBuilder()
            .append(PAPER_EMOJI).append("Список команд: \n\n")
            .append(
                methodProcessors
                    .stream()
                    .filter(e -> !e.getClass().equals(DefaultHandler.class))
                    .map(entry -> entry.getName() + " - " + entry.getDescription() + " \n")
                    .collect(Collectors.joining())
            )
            .toString();
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
