package edu.java.domain.jooq;

import edu.java.domain.jdbc.mappers.LinkMapper;
import edu.java.dto.Link;
import java.time.OffsetDateTime;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.tables.Tables.LINKS;

@Repository
public class JooqLinkRepository {
    private DSLContext dslContext;

    public JooqLinkRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public Long createLink(Link link) {
        return dslContext.insertInto(LINKS)
            .set(LINKS.LINK, link.getLink().toString())
            .onConflict(LINKS.LINK)
            .doUpdate()
            .set(LINKS.LINK, LINKS.LINK)
            .returning(LINKS.ID)
            .fetchOne()
            .getId();
    }

    public Link getLinkByLink(String url) {
        return dslContext
            .select(LINKS.fields())
            .from(LINKS)
            .where(LINKS.LINK.eq(url))
            .fetchInto(Link.class)
            .getFirst();
    }

    public List<Link> getLinksNotUpdates(int limit) {
        return dslContext
            .select(LINKS.fields())
            .from(LINKS)
            .orderBy(LINKS.UPDATETIME.sortDesc())
            .limit(limit)
            .fetch()
            .map(LinkMapper::mapFromRecord);
    }

    public void updateLastSendTime(Long idLink, OffsetDateTime newSendTime) {
        dslContext
            .update(LINKS)
            .set(LINKS.LASTSENDTIME, newSendTime)
            .set(LINKS.UPDATETIME, DSL.currentOffsetDateTime())
            .where(LINKS.ID.eq(idLink))
            .execute();
    }

    public Link getLinkById(Long linkId) {
        return dslContext
            .select(LINKS.fields())
            .from(LINKS)
            .where(LINKS.ID.eq(linkId))
            .fetchInto(Link.class)
            .getFirst();
    }
}
