package com.web.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.batch.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}