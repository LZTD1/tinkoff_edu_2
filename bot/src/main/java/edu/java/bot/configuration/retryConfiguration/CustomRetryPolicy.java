package edu.java.bot.configuration.retryConfiguration;

import edu.java.bot.telegramExceptions.RuntimeTelegramApiRequestException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.context.RetryContextSupport;

@RequiredArgsConstructor
public class CustomRetryPolicy implements RetryPolicy {

    private static final String CONTEXT_NO_RECOVERY = "context.no-recovery";
    private final List<Integer> codesOfRetry;

    @Override
    public boolean canRetry(RetryContext context) {
        Throwable t = context.getLastThrowable();
        boolean can = (t == null || this.retryForException(t));
        if (!can && !this.retryForException(t)) {
            context.setAttribute(CONTEXT_NO_RECOVERY, true);
        } else {
            context.removeAttribute(CONTEXT_NO_RECOVERY);
        }
        return can;
    }

    private boolean retryForException(Throwable throwable) {
        if (throwable instanceof RuntimeTelegramApiRequestException exception) {
            return codesOfRetry.contains(exception.getStatusCode());
        }
        return false;
    }

    @Override
    public RetryContext open(RetryContext parent) {
        return new CustomRetryContext(parent);
    }

    @Override
    public void close(RetryContext context) {
    }

    @Override
    public void registerThrowable(RetryContext context, Throwable throwable) {
        CustomRetryContext simpleContext = (CustomRetryContext) context;
        simpleContext.registerThrowable(throwable);
    }

    private static class CustomRetryContext extends RetryContextSupport {
        CustomRetryContext(RetryContext parent) {
            super(parent);
        }
    }
}
