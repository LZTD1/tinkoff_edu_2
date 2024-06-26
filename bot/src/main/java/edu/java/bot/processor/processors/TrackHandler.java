package edu.java.bot.processor.processors;

import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.clients.exceptions.BadLinkEntityException;
import edu.java.bot.processor.MethodProcessor;
import edu.java.bot.processor.linkValidator.LinkValidator;
import java.net.URI;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.bot.processor.Constants.FAIL_TRACK_MESSAGE;
import static edu.java.bot.processor.Constants.INCORRECT_LINK_TYPE;
import static edu.java.bot.processor.Constants.SUCCESSFUL_TRACK_MESSAGE;
import static edu.java.bot.processor.Constants.UNSUPPORTED_TRACK_LINK;

@Component
public class TrackHandler implements MethodProcessor {

    @Qualifier("validatorsMapAndName")
    private final Map<String, LinkValidator> validatorsMap;
    private ScrapperClient scrapperClient;
    private List<LinkValidator> validators;

    public TrackHandler(ScrapperClient scrapperClient, List<LinkValidator> validators) {
        this.scrapperClient = scrapperClient;
        this.validators = validators;
        this.validatorsMap = getValidatorContainer();
    }

    private Map<String, LinkValidator> getValidatorContainer() {
        return validators.stream()
            .collect(
                Collectors.toMap(LinkValidator::getHost, Function.identity())
            );
    }

    @Override
    public String handle(Update update) {
        String[] param = update.getMessage().getText().split(" ");

        String failTrackMessage = paramValidator(param);
        if (failTrackMessage != null) {
            return failTrackMessage;
        }

        try {
            scrapperClient.addTrackLink(update.getMessage().getChatId(), URI.create(param[1]));
            return SUCCESSFUL_TRACK_MESSAGE;
        } catch (BadLinkEntityException e) {
            return e.getMessage();
        }
    }

    @Nullable private String paramValidator(String[] param) {
        if (param.length != 2) {
            return FAIL_TRACK_MESSAGE;
        }
        URI uriParam = URI.create(param[1]);
        if (!validatorsMap.containsKey(uriParam.getHost())) {
            return MessageFormat.format("{0}\n{1}", UNSUPPORTED_TRACK_LINK, getSupportedResources());
        }

        LinkValidator validator = validatorsMap.get(uriParam.getHost());
        if (!uriParam.getPath().matches(validator.getPathValidator())) {
            return MessageFormat.format("{0}{1}", INCORRECT_LINK_TYPE, validator.getExample());
        }
        return null;
    }

    private String getSupportedResources() {
        return String.join(
            "\n",
            validators.stream()
                .map(LinkValidator::getHost)
                .toList()
        );
    }

    @Override
    public String getDescription() {
        return "начать отслеживание ссылки";
    }

    @Override
    public String getName() {
        return "/track";
    }
}
