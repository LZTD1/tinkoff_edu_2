package edu.java.domain.jooq;

import edu.java.database.dto.User;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.tables.Tables.USERS;

@Repository
public class JooqUserRepository {

    private DSLContext dslContext;

    public JooqUserRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public void createUser(User user) {
        dslContext.insertInto(USERS)
            .set(USERS.TELEGRAMID, user.getTelegramId())
            .returning(USERS.ID)
            .fetchOne()
            .getId();
    }

    public int deleteUser(User user) {
        return dslContext
            .delete(USERS)
            .where(USERS.TELEGRAMID.eq(user.getTelegramId()))
            .execute();
    }

    public User getUserByTgId(Long tgId) {
        return dslContext
            .select(USERS.fields())
            .from(USERS)
            .where(USERS.TELEGRAMID.eq(tgId))
            .fetchInto(User.class)
            .getFirst();
    }
}
