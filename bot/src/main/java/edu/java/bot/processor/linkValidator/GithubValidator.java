package edu.java.bot.processor.linkValidator;

import org.springframework.stereotype.Component;

@Component
public class GithubValidator implements LinkValidator {
    @Override
    public String getPathValidator() {
        return "^\\/(\\w+)\\/([a-z0-9]+(_[a-z0-9]+)*)(\\/)?$";
    }

    @Override
    public String getHost() {
        return "github.com";
    }

    @Override
    public String getExample() {
        return "https://github.com/LZTD1/tinkoff_edu_2/";
    }
}
