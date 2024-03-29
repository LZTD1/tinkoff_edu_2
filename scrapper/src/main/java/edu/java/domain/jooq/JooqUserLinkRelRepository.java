package edu.java.domain.jooq;

import edu.java.dto.Link;
import edu.java.dto.User;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.tables.Tables.LINKS;
import static edu.java.domain.jooq.tables.Tables.USERS;
import static edu.java.domain.jooq.tables.Tables.USERS_LINKS;

@Repository
@RequiredArgsConstructor
public class JooqUserLinkRelRepository {

    private final DSLContext dslContext;

    public void createRelational(User user, Link link) {
        dslContext.insertInto(USERS_LINKS)
            .set(USERS_LINKS.LINKID, link.getId())
            .set(USERS_LINKS.USERID, user.getId())
            .execute();
    }

    public void deleteRelational(User user, Link link) {
        dslContext
            .delete(USERS_LINKS)
            .where(USERS_LINKS.USERID.eq(user.getId()))
            .and(USERS_LINKS.LINKID.eq(link.getId()))
            .execute();
    }

    public Iterator<Record> getAllLinksByTgId(long tgChatId, int limit, int offset) {
        return dslContext
            .select()
            .from(USERS_LINKS)
            .join(USERS).on(USERS_LINKS.USERID.eq(USERS.ID))
            .join(LINKS).on(USERS_LINKS.LINKID.eq(LINKS.ID))
            .where(USERS.TELEGRAMID.eq(tgChatId))
            .limit(limit)
            .offset(offset)
            .fetch()
            .iterator();
    }

    public Iterator<Record> getAllUsersIdWithLink(Long id) {
        return dslContext
            .select()
            .from(USERS_LINKS)
            .join(USERS).on(USERS_LINKS.USERID.eq(USERS.ID))
            .join(LINKS).on(USERS_LINKS.LINKID.eq(LINKS.ID))
            .where(LINKS.ID.eq(id))
            .fetch()
            .iterator();
    }
}
