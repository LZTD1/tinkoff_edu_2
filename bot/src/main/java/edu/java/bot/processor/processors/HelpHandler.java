package edu.java.bot.processor.processors;

import edu.java.bot.processor.MethodProcessor;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class HelpHandler implements MethodProcessor {

    public static final String PAPER_EMOJI = "\uD83D\uDCC4";
    private final List<MethodProcessor> methodProcessors;

    @Override
    public String handle(Update update) {
        return new StringBuilder()
            .append(PAPER_EMOJI).append("Список команд: \n\n")
            .append(
                methodProcessors
                    .stream()
                    .filter(e -> !e.getClass().equals(DefaultHandler.class))
                    .map(entry -> MessageFormat.format("{0} - {1} \n", entry.getName(), entry.getDescription()))
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
