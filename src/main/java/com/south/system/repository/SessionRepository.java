package com.south.system.repository;

import com.south.system.model.session.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SessionRepository extends MongoRepository<Session, String> {

    @Transactional
    public void deleteById(String id);
}
