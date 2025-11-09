package org.teste.casetecnico.repository;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.teste.casetecnico.model.Users;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {

    UserDetails findByLogin(String login);
}
