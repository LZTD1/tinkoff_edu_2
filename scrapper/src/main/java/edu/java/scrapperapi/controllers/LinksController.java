package edu.java.scrapperapi.controllers;

import edu.java.scrapper.dto.AddLinkRequest;
import edu.java.scrapper.dto.DeleteLinkRequest;
import edu.java.scrapper.dto.LinkResponse;
import edu.java.scrapper.dto.ListLinksResponse;
import edu.java.scrapperapi.RateLimiterByIp;
import edu.java.scrapperapi.exceptions.TooManyRequests;
import edu.java.scrapperapi.services.LinkService;
import edu.java.shared.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@Tag(name = "links", description = "управление ссылками в чатах")
public class LinksController {

    private final LinkService linkService;
    private final RateLimiterByIp limiter;

    @Operation(
        operationId = "linksDelete",
        summary = "Убрать отслеживание ссылки",
        responses = {
            @ApiResponse(responseCode = "200", description = "Ссылка успешно убрана", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = LinkResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Ссылка не найдена", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            })
        }
    )
    @DeleteMapping(
        value = "/links",
        produces = {"application/json"}
    )
    public void linksDelete(
        HttpServletRequest request,

        @NotNull @Parameter(name = "Tg-Chat-Id", description = "", required = true, in = ParameterIn.HEADER)
        @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId,

        @Parameter(name = "DeleteLinkRequest", description = "", required = true)
        @Valid @RequestBody DeleteLinkRequest deleteLinkRequest
    ) {
        if (!limiter.tryConsume(request.getRemoteAddr())) {
            throw new TooManyRequests();
        }
        linkService.remove(tgChatId, deleteLinkRequest.getLink());
    }

    @Operation(
        operationId = "linksGet",
        summary = "Получить все отслеживаемые ссылки",
        responses = {
            @ApiResponse(responseCode = "200", description = "Ссылки успешно получены", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ListLinksResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            })
        }
    )
    @GetMapping(
        value = "/links",
        produces = {"application/json"}
    )
    public ListLinksResponse linksGet(
        HttpServletRequest request,

        @NotNull @Parameter(name = "Tg-Chat-Id", description = "", required = true, in = ParameterIn.HEADER)
        @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId,

        @NotNull @Parameter(name = "limit", description = "", required = true, in = ParameterIn.HEADER)
        @RequestHeader(value = "limit", required = true) int limit,

        @NotNull @Parameter(name = "offset", description = "", required = true, in = ParameterIn.HEADER)
        @RequestHeader(value = "offset", required = true) int offset

    ) {
        if (!limiter.tryConsume(request.getRemoteAddr())) {
            throw new TooManyRequests();
        }
        return new ListLinksResponse() {{
            List<LinkResponse> links = linkService.listAll(tgChatId, limit, offset);

            setLinks(links);
            setSize(links.size());
        }};
    }

    @Operation(
        operationId = "linksPost",
        summary = "Добавить отслеживание ссылки",
        responses = {
            @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = LinkResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            })
        }
    )
    @PostMapping(
        value = "/links",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    public void linksPost(
        HttpServletRequest request,

        @NotNull @Parameter(name = "Tg-Chat-Id", description = "", required = true, in = ParameterIn.HEADER)
        @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId,

        @Parameter(name = "AddLinkRequest", description = "", required = true)
        @Valid @RequestBody AddLinkRequest addLinkRequest
    ) {
        if (!limiter.tryConsume(request.getRemoteAddr())) {
            throw new TooManyRequests();
        }
        linkService.createLink(
            tgChatId,
            addLinkRequest.getLink()
        );
    }
}
