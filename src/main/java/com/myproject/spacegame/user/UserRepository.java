package com.myproject.spacegame.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

	List<User> findByUsernameContains(String username);
}
