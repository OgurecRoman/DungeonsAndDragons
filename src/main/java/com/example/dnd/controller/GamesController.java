package com.example.dnd.controller;

import com.example.dnd.model.Game;
import com.example.dnd.model.User;
import com.example.dnd.repository.GameRepository;
import com.example.dnd.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class GamesController {

    private final GameRepository gameRepository;
    private final UserService userService;

    public GamesController(GameRepository gameRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    @GetMapping("/mygames")
    public String getUserGames(Model model, Principal principal) {
        try {
            User currentUser = (User) userService.loadUserByUsername(principal.getName());
            List<Game> userGames = gameRepository.findByPlayersContaining(currentUser);
            model.addAttribute("games", userGames);
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка загрузки списка игр");
        }
        return "mygames";
    }
}
