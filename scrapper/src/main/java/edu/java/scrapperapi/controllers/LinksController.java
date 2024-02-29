package edu.java.scrapperapi.controllers;

import edu.java.api.LinksApi;
import edu.java.api.dto.AddLinkRequest;
import edu.java.api.dto.LinkResponse;
import edu.java.api.dto.ListLinksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LinksController implements LinksApi {

    @Override
    public ResponseEntity<LinkResponse> linksDelete(Long tgChatId) {
        return LinksApi.super.linksDelete(tgChatId);
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
