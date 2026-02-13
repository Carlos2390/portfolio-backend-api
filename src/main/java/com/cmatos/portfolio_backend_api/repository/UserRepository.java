package com.cmatos.portfolio_backend_api.repository;

import com.cmatos.portfolio_backend_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsernameExibition(String username);

    User findUserByEmail(String email);
}
