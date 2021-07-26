package com.south.system.controller;

import com.south.system.model.associate.Associate;
import com.south.system.model.associate.AssociateRequest;
import com.south.system.service.AssociateService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/associate/v1")
@AllArgsConstructor
public class AssociateController {

    private AssociateService associadoService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Associate> findAll() {
        return associadoService.findAll();
    }

    @ApiImplicitParams(@ApiImplicitParam(name = "cpf", value = "Associate CPF", required = true))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Associate insert(@RequestBody @Valid AssociateRequest request) {
        return associadoService.insert(request);
    }

    @DeleteMapping("/{cpf}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByCpf(@PathVariable String cpf) {
        associadoService.deleteByCpf(cpf);
    }
}
