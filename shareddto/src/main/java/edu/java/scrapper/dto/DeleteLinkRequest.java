package edu.java.scrapper.dto;

import java.net.URI;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class DeleteLinkRequest {

    private URI link;

}

