package edu.java.scrapperapi.controllers;

import edu.java.shared.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Tag(name = "Tg-Chat", description = "управление чатами телеграм")
public class TgChatController {

    @Operation(
        operationId = "tgChatIdPost",
        summary = "Зарегистрировать чат",
        responses = {
            @ApiResponse(responseCode = "200", description = "Чат зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            })
        }
    )
    @PostMapping(
        value = "/tg-chat/{id}",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void tgChatPost(
        @NotNull @Parameter(name = "id", description = "", required = true, in = ParameterIn.PATH)
        @PathVariable(value = "id", required = true) Long id
    ) {
    }

    @Operation(
        operationId = "tgChatIdDelete",
        summary = "Удалить чат",
        responses = {
            @ApiResponse(responseCode = "200", description = "Чат удален"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            })
        }
    )
    @DeleteMapping(
        value = "/tg-chat/{id}",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void tgChatDelete(
        @NotNull @Parameter(name = "id", description = "", required = true, in = ParameterIn.PATH)
        @PathVariable(value = "id", required = true) Long id
    ) {
    }

}
