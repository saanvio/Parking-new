package com.hcl.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.parking.entity.Raffle;

@Repository
public interface RaffleRepository extends JpaRepository<Raffle, Long>{

}
