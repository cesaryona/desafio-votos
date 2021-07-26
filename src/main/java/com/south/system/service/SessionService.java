package com.south.system.service;

import com.google.gson.Gson;
import com.south.system.exception.BadRequestException;
import com.south.system.exception.NotFoundException;
import com.south.system.model.Vote;
import com.south.system.model.session.Session;
import com.south.system.model.session.SessionRequest;
import com.south.system.repository.SessionRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class SessionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class);

    private static final String SESSION_NOT_FOUND = "Session not found. Id: %s";
    private static final String SESSION_NOT_STARTED = "Session not started yet. Starts at: %s";
    private static final String SESSION_ENDED = "Session already ended. Ended at: %s";

    private SessionRepository sessionRepository;

    private SubjectService subjectService;
    private AssociateService associateService;

    public List<Session> findAll() {
        LOGGER.info("Starting method 'findAll'");
        return sessionRepository.findAll();
    }

    public Session findById(String id) {
        LOGGER.info("Starting method 'findById' for: {}", id);
        return sessionRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(SESSION_NOT_FOUND, id)));
    }

    public Session insert(SessionRequest request) {
        LOGGER.info("Starting method 'insert'");
        LocalDateTime dateBegin = request.getDateBegin();
        LocalDateTime dateEnd = request.getDateEnd();

        if (dateEnd == null || dateEnd.isBefore(dateBegin) || dateEnd.equals(dateBegin)) {
            dateEnd = dateBegin.plusMinutes(1);
        }

        Session session = Session.builder()
                .subject(subjectService.findById(request.getSubjectId()))
                .dateBegin(dateBegin)
                .dateEnd(dateEnd)
                .build();
        LOGGER.debug("session data: {}", new Gson().toJson(session));

        LOGGER.info("Starting save session");
        return sessionRepository.save(session);
    }

    public void vote(Vote vote) {
        LOGGER.info("Starting method 'vote' for: {}", vote.getCpf());
        Session session = findById(vote.getSessionId());

        validateSession(session.getDateBegin(), session.getDateEnd());
        associateService.associateAbleToVoteValidate(vote.getCpf());
        subjectService.vote(vote, session.getSubject().getId());
    }

    public void validateSession(LocalDateTime dateBegin, LocalDateTime dateEnd) {
        LOGGER.info("Starting method 'validateSession'");

        if (LocalDateTime.now().isBefore(dateBegin)) {
            throw new BadRequestException(String.format(SESSION_NOT_STARTED, dateBegin));
        }
        if (LocalDateTime.now().isAfter(dateEnd)) {
            throw new BadRequestException(String.format(SESSION_ENDED, dateEnd));
        }
    }

    public void deleteById(String id) {
        LOGGER.info("Starting method 'deleteById' for: {}", id);
        sessionRepository.deleteById(id);
    }
}
