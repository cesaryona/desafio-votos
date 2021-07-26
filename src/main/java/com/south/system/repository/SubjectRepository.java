package com.south.system.repository;

import com.south.system.model.subject.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {

    @Transactional
    public void deleteById(String id);
}
