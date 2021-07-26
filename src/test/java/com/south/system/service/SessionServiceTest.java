package com.south.system.service;

import com.south.system.exception.BadRequestException;
import com.south.system.exception.NotFoundException;
import com.south.system.model.Vote;
import com.south.system.model.enums.VoteType;
import com.south.system.model.session.Session;
import com.south.system.model.session.SessionRequest;
import com.south.system.model.subject.Subject;
import com.south.system.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SessionServiceTest {

    private SessionRepository sessionRepository;
    private SubjectService subjectService;
    private AssociateService associateService;

    private SessionService sessionService;

    @BeforeEach
    public void setup() {
        this.sessionRepository = Mockito.mock(SessionRepository.class);
        this.subjectService = Mockito.mock(SubjectService.class);
        this.associateService = Mockito.mock(AssociateService.class);

        this.sessionService = new SessionService(sessionRepository, subjectService, associateService);
    }

    @Test
    public void shouldFindAllSession() {
        List<Session> sessionMockList = Arrays.asList(getSession(), getSession());
        Mockito.when(this.sessionRepository.findAll()).thenReturn(sessionMockList);

        List<Session> sessionList = this.sessionService.findAll();

        assertNotNull(sessionList);
        assertEquals(2, sessionList.size());
    }

    @Test
    public void shouldFindSessionById() {
        Mockito.when(this.sessionRepository.findById(Mockito.anyString())).thenReturn(Optional.of(getSession()));

        Session session = this.sessionService.findById("60fdbd77224e872da1cf41f9");

        assertNotNull(session);
        assertEquals("60fdbd77224e872da1cf41f9", session.getId());
        assertNotNull(session.getSubject());
        assertEquals(LocalDateTime.of(2021, 7, 25, 10, 00, 00), session.getDateBegin());
        assertEquals(LocalDateTime.of(2021, 7, 25, 11, 00, 00), session.getDateEnd());
    }

    @Test
    public void ShouldInsertSession() {
        SessionRequest request = new SessionRequest("60fdbd3e224e872da1cf41f7", LocalDateTime.now(), null);

        this.sessionService.insert(request);

        Mockito.verify(sessionRepository, Mockito.times(1)).save(Mockito.any(Session.class));
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenFindAssociateByCpf() {
        Mockito.when(this.sessionRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        Executable executable = () -> this.sessionService.findById("60fdbd77224e872da1cf41f9");

        assertThrows(NotFoundException.class, executable);
    }

    @Test
    public void shouldVote(){
        Vote vote = new Vote("60fdbd77224e872da1cf41f9", VoteType.SIM, "69714231000");
        Session session = getSession();
        session.setDateEnd(LocalDateTime.now().plusDays(1));
        Mockito.when(this.sessionRepository.findById(Mockito.anyString())).thenReturn(Optional.of(session));

        this.sessionService.vote(vote);

        Mockito.verify(associateService, Mockito.times(1)).associateAbleToVoteValidate(vote.getCpf());
        Mockito.verify(subjectService, Mockito.times(1)).vote(vote, session.getSubject().getId());
    }

    @Test
    public void shouldThrowBadRequestExceptionWhenDateNowIsBeforeDateBegin() {
        Executable executable = () -> this.sessionService.validateSession(LocalDateTime.now().plusHours(1), LocalDateTime.now());
        assertThrows(BadRequestException.class, executable);
    }

    @Test
    public void shouldThrowBadRequestExceptionWhenDateNowIsAfterDatEnd() {
        Executable executable = () -> this.sessionService.validateSession(LocalDateTime.now(), LocalDateTime.now().minusHours(1));
        assertThrows(BadRequestException.class, executable);
    }

    @Test
    public void shoulDeleteSessionById() {
        String id = "60fdbd77224e872da1cf41f9";

        this.sessionService.deleteById(id);

        Mockito.verify(sessionRepository, Mockito.times(1)).deleteById(id);
    }


    private Session getSession() {
        Subject subject = new Subject("60fdbd3e224e872da1cf41f7", "new topic", 10, 5, 15, new ArrayList<>());

        return new Session("60fdbd77224e872da1cf41f9", subject, LocalDateTime.of(2021, 7, 25, 10, 00, 00), LocalDateTime.of(2021, 7, 25, 11, 00, 00));
    }
}
