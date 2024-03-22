package edu.java.database.dto;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users_links")
@Getter
@Setter
public class UserLinkRel {

    @EmbeddedId
    private UserLinkPK id;

    @MapsId("userid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid")
    private User user;

    @MapsId("linkid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "linkid")
    private Link link;

}
