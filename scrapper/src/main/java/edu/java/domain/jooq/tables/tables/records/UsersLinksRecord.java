/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooq.tables.tables.records;

import edu.java.domain.jooq.tables.tables.UsersLinks;
import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class UsersLinksRecord extends UpdatableRecordImpl<UsersLinksRecord> implements Record2<Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Create a detached UsersLinksRecord
     */
    public UsersLinksRecord() {
        super(UsersLinks.USERS_LINKS);
    }

    /**
     * Create a detached, initialised UsersLinksRecord
     */
    public UsersLinksRecord(Long userid, Long linkid) {
        super(UsersLinks.USERS_LINKS);

        setUserid(userid);
        setLinkid(linkid);
        resetChangedOnNotNull();
    }

    /**
     * Getter for <code>public.users_links.userid</code>.
     */
    public Long getUserid() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.users_links.userid</code>.
     */
    public void setUserid(Long value) {
        set(0, value);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>public.users_links.linkid</code>.
     */
    public Long getLinkid() {
        return (Long) get(1);
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>public.users_links.linkid</code>.
     */
    public void setLinkid(Long value) {
        set(1, value);
    }

    @Override
    public Record2<Long, Long> key() {
        return (Record2) super.key();
    }

    @Override
    public Row2<Long, Long> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Long, Long> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return UsersLinks.USERS_LINKS.USERID;
    }

    @Override
    public Field<Long> field2() {
        return UsersLinks.USERS_LINKS.LINKID;
    }

    @Override
    public Long component1() {
        return getUserid();
    }

    @Override
    public Long component2() {
        return getLinkid();
    }

    @Override
    public Long value1() {
        return getUserid();
    }

    @Override
    public Long value2() {
        return getLinkid();
    }

    @Override
    public UsersLinksRecord value1(Long value) {
        setUserid(value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    @Override
    public UsersLinksRecord value2(Long value) {
        setLinkid(value);
        return this;
    }

    @Override
    public UsersLinksRecord values(Long value1, Long value2) {
        value1(value1);
        value2(value2);
        return this;
    }
}