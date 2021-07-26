package com.south.system.service;

import com.south.system.config.AssociateProperties;
import com.south.system.exception.BadRequestException;
import com.south.system.exception.NotFoundException;
import com.south.system.model.associate.Associate;
import com.south.system.model.associate.AssociateRequest;
import com.south.system.model.associate.StatusAssociate;
import com.south.system.repository.AssociateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AssociateServiceTest {

    private AssociateRepository associateRepository;
    private AssociateProperties associateProperties;
    private RestTemplate restTemplate;
    private AssociateService associateService;

    @BeforeEach
    public void setup() {
        this.associateRepository = Mockito.mock(AssociateRepository.class);
        this.associateProperties = Mockito.mock(AssociateProperties.class);
        this.restTemplate = Mockito.mock(RestTemplate.class);

        this.associateService = new AssociateService(associateRepository, associateProperties, restTemplate);
    }

    @Test
    public void shouldFindAllAssociate() {
        List<Associate> associateMock = Arrays.asList(getAssociate(), new Associate("60fdb90b33f1906dd85cf418", "86331238000", "ABLE_TO_VOTE"));
        Mockito.when(this.associateRepository.findAll()).thenReturn(associateMock);

        List<Associate> associateList = this.associateService.findAll();

        assertNotNull(associateList);
        assertEquals(2, associateList.size());
    }

    @Test
    public void shouldFindAssociateByCpf() {
        Associate associateMock = getAssociate();
        Mockito.when(this.associateRepository.findByCpf(Mockito.anyString())).thenReturn(Optional.of(associateMock));

        Associate associate = this.associateService.findByCpf("69714231000");

        assertNotNull(associate);
        assertEquals("60fdab7511467a552f37cb62", associate.getId());
        assertEquals("69714231000", associate.getCpf());
        assertEquals("UNABLE_TO_VOTE", associate.getStatus());
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenFindAssociateByCpf() {
        Mockito.when(this.associateRepository.findByCpf(Mockito.anyString())).thenReturn(Optional.empty());

        Mockito.when(this.associateRepository.findByCpf(Mockito.any())).thenThrow(NotFoundException.class);

        Executable executable = () -> this.associateService.findByCpf("69714231000");

        assertThrows(NotFoundException.class, executable);
    }

    @Test
    public void shouldThrowBadRequestExceptionWhenAssociateNotAbleToVote() {
        Mockito.when(this.associateRepository.findByCpf(Mockito.anyString())).thenReturn(Optional.of(getAssociate()));

        Executable executable = () -> this.associateService.associateAbleToVoteValidate("69714231000");

        assertThrows(BadRequestException.class, executable);
    }

    @Test
    public void shouldInsertAnAssociate() {
        String cpf = "69714231000";
        Associate associateMock = getAssociate();
        AssociateRequest request = new AssociateRequest(cpf);
        Mockito.when(this.associateRepository.save(Mockito.any(Associate.class))).thenReturn(associateMock);

        String url = "api/validateCpf/";
        Mockito.when(this.associateProperties.getCpfValidatorUrl()).thenReturn(url);
        String fullUrl = url + cpf;

        ResponseEntity<StatusAssociate> response = new ResponseEntity<StatusAssociate>(new StatusAssociate("UNABLE_TO_VOTE"), HttpStatus.OK);
        Mockito.when(this.restTemplate.exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(null), StatusAssociate.class)).thenReturn(response);

        Associate associate = this.associateService.insert(request);

        Mockito.verify(this.restTemplate, Mockito.times(1)).exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(null), StatusAssociate.class);
        assertNotNull(associate);
        assertEquals("60fdab7511467a552f37cb62", associate.getId());
        assertEquals("69714231000", associate.getCpf());
        assertEquals("UNABLE_TO_VOTE", associate.getStatus());
    }

    @Test
    public void shoulDeleteAssociateByCpf() {
        String cpf = "69714231000";

        this.associateService.deleteByCpf(cpf);

        Mockito.verify(associateRepository, Mockito.times(1)).deleteByCpf(cpf);
    }

    private Associate getAssociate() {
        return new Associate("60fdab7511467a552f37cb62", "69714231000", "UNABLE_TO_VOTE");
    }
}
