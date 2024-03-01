package edu.java.scrapperapi.controllers;

import edu.java.scrapper.dto.AddLinkRequest;
import edu.java.scrapper.dto.DeleteLinkRequest;
import edu.java.scrapper.dto.LinkResponse;
import edu.java.scrapper.dto.ListLinksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LinksController implements LinksApi {

    @Override
    public ResponseEntity<LinkResponse> linksDelete(Long tgChatId, DeleteLinkRequest deleteLinkRequest) {
        return LinksApi.super.linksDelete(tgChatId, deleteLinkRequest);
    }

    @Override
    public ResponseEntity<ListLinksResponse> linksGet(Long tgChatId) {
        return LinksApi.super.linksGet(tgChatId);
    }

    @Override
    public ResponseEntity<LinkResponse> linksPost(Long tgChatId, AddLinkRequest addLinkRequest) {
        return LinksApi.super.linksPost(tgChatId, addLinkRequest);
    }
}
