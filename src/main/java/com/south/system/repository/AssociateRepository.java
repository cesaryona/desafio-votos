package com.south.system.repository;

import com.south.system.model.associate.Associate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AssociateRepository extends MongoRepository<Associate, String> {

    Optional<Associate> findByCpf(String cpf);

    @Transactional
    void deleteByCpf(String cpf);
}
