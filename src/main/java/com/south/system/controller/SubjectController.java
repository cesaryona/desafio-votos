package com.south.system.controller;

import com.south.system.model.subject.Subject;
import com.south.system.model.subject.SubjectRequest;
import com.south.system.service.SubjectService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/subject/v1")
@AllArgsConstructor
public class SubjectController {

    private SubjectService subjectService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Subject> findAll() {
        return subjectService.findAll();
    }

    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "Subject ID", required = true))
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Subject findById(@PathVariable String id) {
        return subjectService.findById(id);
    }

    @ApiImplicitParams(@ApiImplicitParam(name = "topic", value = "Topic", required = true))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Subject insert(@RequestBody SubjectRequest request) {
        return subjectService.insert(request);
    }

    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "Subject ID", required = true))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id) {
        subjectService.deleteById(id);
    }
}
