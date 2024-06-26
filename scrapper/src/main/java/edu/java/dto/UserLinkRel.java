package edu.java.dto;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users_links")
public class UserLinkRel {
    @EmbeddedId
    private UsersLinkId id = new UsersLinkId();

    @MapsId("userid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @MapsId("linkid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "linkid", nullable = false)
    private Link link;

}
