package com.south.system.controller;

import com.south.system.model.session.Session;
import com.south.system.model.session.SessionRequest;
import com.south.system.model.Vote;
import com.south.system.service.SessionService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/session/v1")
@AllArgsConstructor
public class SessionController {

    private SessionService sessionService;

    @GetMapping
    public List<Session> findAll() {
        return sessionService.findAll();
    }

    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "Session ID", required = true))
    @GetMapping("/{id}")
    public Session findById(@PathVariable String id) {
        return sessionService.findById(id);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "subjectId", value = "Subject ID", required = true),
            @ApiImplicitParam(name = "dateBegin", value = "Date Begin", required = true),
            @ApiImplicitParam(name = "dateEnd", value = "Date End", required = false),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Session insert(@RequestBody @Valid SessionRequest request) {
        return sessionService.insert(request);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "sessionId", value = "Session ID", required = true),
            @ApiImplicitParam(name = "vote", value = "Vote", required = true),
            @ApiImplicitParam(name = "cpf", value = "User CPF", required = true),
    })
    @PostMapping("/vote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@RequestBody @Valid Vote vote) {
        sessionService.vote(vote);
    }

    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "Session ID", required = true))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id) {
        sessionService.deleteById(id);
    }
}
