package edu.java.parsers;

import edu.java.bot.dto.LinkUpdate;
import java.net.URI;
import java.util.Set;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

public class MainHandler {

    @SneakyThrows
    public static LinkUpdate handle(URI uri) {

        Reflections reflections = new Reflections("edu.java.parsers.web", new SubTypesScanner(false));
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
        for (Class<?> aClass : classes) {
            if (aClass.getInterfaces().length > 0 && aClass.getInterfaces()[0].getSimpleName().equals("WebHandler")) {
                WebHandler instance = (WebHandler) aClass.getDeclaredConstructor().newInstance();

                if (instance.getHost().equals(uri.getHost())) {
//                    LinkUpdate linkUpdate = new LinkUpdate();
//                    Optional<String> updates = instance.getUpdateReasonIfHas(uri);

                    System.out.println("Нашелся инстанс!");
                    System.out.println(
                        instance.getUpdateReasonIfHas(uri)
                    );
                }
            }
        }

        return null;
    }
}
