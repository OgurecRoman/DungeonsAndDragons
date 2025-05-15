package com.example.dnd.controller;

import com.example.dnd.model.Game;
import com.example.dnd.model.User;
import com.example.dnd.repository.GameRepository;
import com.example.dnd.service.GameService;
import com.example.dnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/games")
public class GameController {
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserService userService;

    private final GameService gameService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("game", new Game());
        return "create-game";
    }

    @PostMapping("/create")
    public String createGame(@ModelAttribute Game game,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        // Добавляем создателя в игру
        User creator = (User) userService.loadUserByUsername(principal.getName());
        creator.getGames().add(game);
        game.getPlayers().add(creator);

        Game savedGame = gameRepository.save(game);
        redirectAttributes.addAttribute("id", savedGame.getId());

        return "redirect:/games/{id}";
    }

    @GetMapping("/connect")
    public String showConnectForm(Model model) {
        model.addAttribute("game", new Game());
        return "connect-game";
    }

    @PostMapping("/connect")
    public String connectGame(@ModelAttribute Game game,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        User player = (User) userService.loadUserByUsername(principal.getName());
        gameService.connectUserToGame(game.getId(), player.getUsername());

        redirectAttributes.addAttribute("id", game.getId());
        return "redirect:/games/{id}";
    }

    @GetMapping("/{id}")
    public String viewGame(@PathVariable Long id, Model model) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Game not found"));
        model.addAttribute("game", game);
        return "game";
    }
}
