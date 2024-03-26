package edu.java.bot.processor.processors;

import edu.java.bot.processor.MethodProcessor;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class HelpHandler implements MethodProcessor {

    private final List<MethodProcessor> methodProcessors;

    @Override
    public String handle(Update update) {
        return methodProcessors
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
