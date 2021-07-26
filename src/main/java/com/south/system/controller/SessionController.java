package com.south.system.controller;

import com.south.system.model.session.Session;
import com.south.system.model.session.SessionRequest;
import com.south.system.model.Vote;
import com.south.system.service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/session/v1")
@AllArgsConstructor
public class SessionController {

    private SessionService sessionService;

    @GetMapping
    public List<Session> findAll(){
        return sessionService.findAll();
    }

    @GetMapping("/{id}")
    public Session findById(@PathVariable String id){
        return sessionService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Session insert(@RequestBody SessionRequest request){
        return sessionService.insert(request);
    }

    @PostMapping("/vote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@RequestBody Vote vote){
        sessionService.vote(vote);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id){
        sessionService.deleteById(id);
    }
}
