package com.example.dnd.repository;

import com.example.dnd.model.Game;
import com.example.dnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByPlayersContaining(User player);
}
