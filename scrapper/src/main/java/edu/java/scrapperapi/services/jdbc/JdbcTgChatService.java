package edu.java.scrapperapi.services.jdbc;

import edu.java.database.dto.User;
import edu.java.domain.UsersDao;
import edu.java.scrapperapi.services.TgChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcTgChatService implements TgChatService {

    private UsersDao usersDao;

    @Autowired
    public JdbcTgChatService(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @Override
    public void register(long tgChatId) {
        usersDao.createUser(new User() {{
            setTelegramId(tgChatId);
        }});
    }

    @Override
    public void unregister(long tgChatId) {
        usersDao.deleteUser(new User() {{
            setTelegramId(tgChatId);
        }});
    }
}
