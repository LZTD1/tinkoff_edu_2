package edu.java.bot.processor.linkValidator;

import org.springframework.stereotype.Component;

@Component
public class StackoverflowValidator implements LinkValidator {

    @Override
    public String getPathValidator() {
        return "^\\/questions\\/(\\d)+\\/([a-z0-9]+(-[a-z0-9]+)*)(\\/)?$";
    }

    @Override
    public String getHost() {
        return "stackoverflow.com";
    }

    @Override
    public String getExample() {
        return "https://stackoverflow.com/questions/2537694/how-to-pause-a-thread-in-java";
    }
}
