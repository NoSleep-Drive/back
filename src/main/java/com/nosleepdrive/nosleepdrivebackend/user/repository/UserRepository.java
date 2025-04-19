package com.nosleepdrive.nosleepdrivebackend.user.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nosleepdrive.nosleepdrivebackend.user.repository.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}