package com.example.dnd.service;

import com.example.dnd.model.Game;
import com.example.dnd.model.User;
import com.example.dnd.repository.GameRepository;
import com.example.dnd.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public GameService(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void connectUserToGame(Long gameId, String username) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        game.getPlayers().add(user);
        userRepository.save(user);
    }
}
