package com.south.system.service;

import com.south.system.exception.BadRequestException;
import com.south.system.exception.NotFoundException;
import com.south.system.model.Vote;
import com.south.system.model.enums.VoteType;
import com.south.system.model.subject.Subject;
import com.south.system.model.subject.SubjectRequest;
import com.south.system.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SubjectServiceTest {

    private SubjectRepository subjectRepository;

    private SubjectService subjectService;

    @BeforeEach
    public void setup() {
        this.subjectRepository = Mockito.mock(SubjectRepository.class);

        this.subjectService = new SubjectService(subjectRepository);
    }

    @Test
    public void shouldFindAllSubject() {
        List<Subject> subjectMockList = Arrays.asList(getSubject(), getSubject());
        Mockito.when(this.subjectRepository.findAll()).thenReturn(subjectMockList);

        List<Subject> subjectList = this.subjectService.findAll();

        assertNotNull(subjectList);
        assertEquals(2, subjectList.size());
    }

    @Test
    public void shouldFindSessionById() {
        Mockito.when(this.subjectRepository.findById(Mockito.anyString())).thenReturn(Optional.of(getSubject()));

        Subject subject = this.subjectService.findById("60fdbd3e224e872da1cf41f7");

        assertNotNull(subject);
        assertEquals("60fdbd3e224e872da1cf41f7", subject.getId());
        assertEquals("new topic", subject.getTopic());
        assertEquals(10, subject.getVoteYes());
        assertEquals(5, subject.getVoteNo());
        assertEquals(15, subject.getTotalVotes());
        assertNotNull(subject.getCpfVotes());
    }

    @Test
    public void ShouldInsertSession() {
        SubjectRequest request = new SubjectRequest("new topic");

        this.subjectService.insert(request);

        Mockito.verify(subjectRepository, Mockito.times(1)).save(Mockito.any(Subject.class));
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenFindAssociateByCpf() {
        Mockito.when(this.subjectRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        Executable executable = () -> this.subjectService.findById("60fdbd3e224e872da1cf41f7");

        assertThrows(NotFoundException.class, executable);
    }

    @Test
    public void shouldVoteYes() {
        Vote vote = new Vote("60fdbd77224e872da1cf41f9", VoteType.SIM, "69714231000");

        Mockito.when(this.subjectRepository.findById(Mockito.anyString())).thenReturn(Optional.of(getSubject()));

        this.subjectService.vote(vote, "60fdbd3e224e872da1cf41f7");

        Mockito.verify(subjectRepository, Mockito.times(1)).save(Mockito.any(Subject.class));
    }

    @Test
    public void shouldVoteNo() {
        Vote vote = new Vote("60fdbd77224e872da1cf41f9", VoteType.NAO, "69714231000");

        Mockito.when(this.subjectRepository.findById(Mockito.anyString())).thenReturn(Optional.of(getSubject()));

        this.subjectService.vote(vote, "60fdbd3e224e872da1cf41f7");

        Mockito.verify(subjectRepository, Mockito.times(1)).save(Mockito.any(Subject.class));

    }

    @Test
    public void shouldThrowBadRequestWhenVote() {
        Vote vote = new Vote("60fdbd77224e872da1cf41f9", VoteType.NAO, "69714231000");
        List<String> cpfVotes = Arrays.asList("69714231000");
        Subject subject = new Subject("60fdbd3e224e872da1cf41f7", "new topic", 10, 5, 15, cpfVotes);

        Mockito.when(this.subjectRepository.findById(Mockito.anyString())).thenReturn(Optional.of(subject));

        Executable executable = () -> this.subjectService.vote(vote, "60fdbd3e224e872da1cf41f7");

        assertThrows(BadRequestException.class, executable);
    }

    @Test
    public void shoulDeleteSessionById() {
        String id = "60fdbd77224e872da1cf41f9";

        this.subjectService.deleteById(id);

        Mockito.verify(subjectRepository, Mockito.times(1)).deleteById(id);
    }

    private Subject getSubject() {
        return new Subject("60fdbd3e224e872da1cf41f7", "new topic", 10, 5, 15, new ArrayList<>());
    }
}
