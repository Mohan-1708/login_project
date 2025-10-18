package com.login_project.Repo;

import com.login_project.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginRepo extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmailid(String emailid);


}
