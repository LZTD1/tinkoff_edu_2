package edu.java.bot.processor;

public class Constants {

    public final static String START_MESSAGE = "Привет!";
    public final static String DEFAULT_MESSAGE = "Упс, какая-то не ожиданная команда!";
    public final static String EMPTY_LIST_MESSAGE = "У вас нету никаких ссылок!";
    private static final String MOON_EMOJI = "\uD83C\uDF1A ";
    public final static String SUCCESSFUL_TRACK_MESSAGE = MOON_EMOJI + "Ссылка успешно затрекана!";
    private static final String STOP_EMOJI = "⛔\uFE0F ";
    public final static String INCORRECT_LINK_TYPE = STOP_EMOJI + "Некорректная форма ввода ссылки!\n"
        + "Введите ссылку в формате: \n\n";
    public final static String UNSUPPORTED_TRACK_LINK = STOP_EMOJI + "Данная ссылка не поддерживается!\n"
        + "Список поддерживаемых ресурсов: \n\n";
    public final static String SUCCESSFUL_UNTRACK_MESSAGE = "Ссылка успешно откреплена!";
    public final static String FAIL_TRACK_MESSAGE = "Не верный форма ввода!\nНапишите: /track url";
    public final static String FAIL_UNTRACK_MESSAGE = "Не верный форма ввода!\nНапишите: /untrack url";

    private Constants() {

    }

}
