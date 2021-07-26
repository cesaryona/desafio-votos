package com.south.system.service;

import com.google.gson.Gson;
import com.south.system.exception.BadRequestException;
import com.south.system.exception.NotFoundException;
import com.south.system.model.Vote;
import com.south.system.model.enums.VoteType;
import com.south.system.model.subject.Subject;
import com.south.system.model.subject.SubjectRequest;
import com.south.system.repository.SubjectRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SubjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectService.class);

    private SubjectRepository subjectRepository;

    public List<Subject> findAll() {
        LOGGER.info("Starting method 'findAll'");
        return subjectRepository.findAll();
    }

    public Subject findById(String id) {
        LOGGER.info("Starting method 'findById' for: {}", id);
        return subjectRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Subject not found. Id: %s", id)));
    }

    public Subject insert(SubjectRequest request) {
        LOGGER.info("Starting method 'insert'");

        Subject subject = Subject.builder().topic(request.getTopic()).cpfVotes(new ArrayList<>()).voteYes(0).voteNo(0).totalVotes(0).build();
        LOGGER.debug("subject data: {}", new Gson().toJson(subject));

        LOGGER.info("Starting save session");
        return subjectRepository.save(subject);
    }

    public void vote(Vote vote, String subjectId) {
        LOGGER.info("Starting method 'vote' for: {}", vote.getCpf());
        Subject subject = findById(subjectId);

        if(subject.getCpfVotes().stream().anyMatch(cpf -> cpf.equals(vote.getCpf()))){
            throw new BadRequestException("CPF already voted");
        }

        if (vote.getVote().toString().equalsIgnoreCase(VoteType.SIM.toString())) {
            subject.setVoteYes(subject.getVoteYes() + 1);
        } else if (vote.getVote().toString().equalsIgnoreCase(VoteType.NAO.toString())) {
            subject.setVoteNo(subject.getVoteNo() + 1);
        }

        subject.setTotalVotes(subject.getVoteYes() + subject.getVoteNo());
        subject.getCpfVotes().add(vote.getCpf());

        LOGGER.info("Starting save vote for: {}", vote.getCpf());
        subjectRepository.save(subject);
    }

    public void deleteById(String id){
        LOGGER.info("Starting method 'deleteById' for: {}", id);
        subjectRepository.deleteById(id);
    }
}
