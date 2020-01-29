package com.myproject.spacegame.user.attackAndDefenseSystem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttackAndDefenseSystemRepository extends JpaRepository<AttackAndDefenseSystemStats, Long>{

	AttackAndDefenseSystemStats findByNameOfAttackDefenseSystem(String nameOfAttackOrDefenseSystem);
	boolean existsByNameOfAttackDefenseSystem(String nameOfAttackOrDefenseSystem);
}
