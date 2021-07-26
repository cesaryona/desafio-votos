package com.south.system.service;

import com.google.gson.Gson;
import com.south.system.config.AssociateProperties;
import com.south.system.exception.BadRequestException;
import com.south.system.exception.ConflictException;
import com.south.system.exception.NotFoundException;
import com.south.system.exception.ObjectErrorException;
import com.south.system.model.associate.Associate;
import com.south.system.model.associate.AssociateRequest;
import com.south.system.model.associate.StatusAssociate;
import com.south.system.repository.AssociateRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
@AllArgsConstructor
public class AssociateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssociateService.class);

    private static final String NOT_FOUND = "Not found. CPF: %s";
    private static final String UNABLE_TO_VOTE = "UNABLE_TO_VOTE";

    private AssociateRepository associateRepository;
    private AssociateProperties associateProperties;
    private RestTemplate restTemplate;

    public List<Associate> findAll() {
        LOGGER.info("Starting method 'findAll'");
        return associateRepository.findAll();
    }

    public Associate findByCpf(String cpf) {
        LOGGER.info("Starting method 'findByCpf' for: {}", cpf);
        return associateRepository.findByCpf(cpf).orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, cpf)));
    }

    public void associateAbleToVoteValidate(String cpf) {
        LOGGER.info("Starting method 'associateAbleToVoteValidate' for: {}", cpf);
        Associate associate = findByCpf(cpf);

        if (associate.getStatus().equalsIgnoreCase(UNABLE_TO_VOTE)) {
            LOGGER.info("Associate not able to vote. CPF: {}", cpf);
            throw new BadRequestException(String.format("Associate not able to vote. CPF: %s", cpf));
        }
    }

    public Associate insert(AssociateRequest request) {
        LOGGER.info("Starting method 'insert' for: {}", request.getCpf());
        try {
            Associate associate = Associate.builder().cpf(request.getCpf()).status(validateCpf(request.getCpf()).getStatus()).build();
            LOGGER.debug("associate data: {}", new Gson().toJson(associate));

            LOGGER.info("Starting save associate");
            return associateRepository.save(associate);
        } catch (DataIntegrityViolationException exception) {
            throw new ConflictException("CPF already exists");
        }
    }

    private StatusAssociate validateCpf(String cpf) {
        LOGGER.info("Starting method 'validateCpf' for: {}", cpf);
        String url = associateProperties.getCpfValidatorUrl() + cpf;
        try {
            LOGGER.info("Starting call 'cpfValidador' endpoint: {}", url);
            ResponseEntity<StatusAssociate> response = this.restTemplate.exchange(url, HttpMethod.GET,
                    new HttpEntity<>(null), StatusAssociate.class);

            return response.getBody();
        } catch (HttpClientErrorException ex) {
            throw new NotFoundException(String.format(NOT_FOUND, cpf));
        } catch (Exception ex) {
            throw new ObjectErrorException(ex.getMessage());
        }
    }

    public void deleteByCpf(String cpf) {
        LOGGER.info("Starting method 'deleteByCpf' for: {}", cpf);
        associateRepository.deleteByCpf(cpf);
    }
}
