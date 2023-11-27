package com.project.Restaurant.Member.owner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long>  {

  Optional<Owner> findByusername(String username);

  Optional<Owner> findByemail(String email);

}
