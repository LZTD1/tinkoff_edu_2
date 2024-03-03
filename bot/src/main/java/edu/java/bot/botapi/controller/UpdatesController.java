package edu.java.bot.botapi.controller;

import edu.java.bot.botapi.controller.exceptions.EmptyIdListException;
import edu.java.bot.dto.ApiErrorResponse;
import edu.java.bot.dto.LinkUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Tag(name = "updates", description = "the updates API")
public class UpdatesController {
    /**
     * POST /updates : Отправить обновление
     *
     * @param linkUpdate (required)
     * @return Обновление обработано (status code 200)
     *     or Некорректные параметры запроса (status code 400)
     */
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
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/updates",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    public ResponseEntity<Void> updatesPost(
        @Parameter(name = "LinkUpdate", description = "", required = true) @Valid @RequestBody LinkUpdate linkUpdate
    ) {
        if (linkUpdate.getTgChatIds().isEmpty()) {
            throw new EmptyIdListException("Список идентификаторов пуст!");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
