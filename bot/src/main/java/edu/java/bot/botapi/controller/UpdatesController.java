package edu.java.bot.botapi.controller;

import edu.java.bot.botapi.services.CommunicatorService;
import edu.java.bot.dto.LinkUpdate;
import edu.java.shared.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@Tag(name = "updates", description = "the updates API")
public class UpdatesController {

    private final CommunicatorService communicatorService;

    @Operation(
        operationId = "updatesPost",
        summary = "Отправить обновление",
        responses = {
            @ApiResponse(responseCode = "200", description = "Обновление обработано"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            })
        }
    )
    @PostMapping(
        value = "/updates",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    public void updatesPost(
        @NotNull @Parameter(name = "LinkUpdate", description = "", required = true) @Valid @RequestBody
        LinkUpdate linkUpdate
    ) {
        communicatorService.sendMessage(linkUpdate);
    }
}
