package edu.java.parsers;

import edu.java.bot.dto.LinkUpdate;
import java.net.URI;
import java.util.Set;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

@SuppressWarnings("HideUtilityClassConstructor")
public class MainHandler {

    public MainHandler() {
    }

    @SneakyThrows
    public static LinkUpdate handle(URI uri) {

        Reflections reflections = new Reflections("edu.java.parsers.web", new SubTypesScanner(false));
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
        for (Class<?> aClass : classes) {
            if (aClass.getInterfaces().length > 0 && aClass.getInterfaces()[0].getSimpleName().equals("WebHandler")) {
                WebHandler instance = (WebHandler) aClass.getDeclaredConstructor().newInstance();

//                  Но как я в конструктор передам бин, допустим, GithubClient

                if (instance.getHost().equals(uri.getHost())) {
//                    Optional<String> updates = instance.getUpdateReasonIfHas(uri);
                }
            }
        }

        return null;
    }
}
