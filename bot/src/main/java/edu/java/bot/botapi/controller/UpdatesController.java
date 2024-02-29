package edu.java.bot.botapi.controller;

import edu.java.api.UpdatesApi;
import edu.java.api.dto.LinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdatesController implements UpdatesApi {

    @Override
    public ResponseEntity<Void> updatesPost(LinkUpdate linkUpdate) {
        return UpdatesApi.super.updatesPost(linkUpdate);
    }
}
