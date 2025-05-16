package com.example.dnd.controller;

import com.example.dnd.model.Game;
import com.example.dnd.model.Message;
import com.example.dnd.model.User;
import com.example.dnd.repository.GameRepository;
import com.example.dnd.repository.MessageRepository;
import com.example.dnd.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Map;
import java.util.Random;

@Slf4j
@Controller
@RequestMapping("/games/{gameId}")
public class ChatController {

    private final MessageRepository messageRepository;
    private final GameRepository gameRepository;
    private final UserService userService;

    public ChatController(MessageRepository messageRepository, GameRepository gameRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    @GetMapping
    public String getChat(@PathVariable Long gameId,
                          @RequestParam(required = false) Boolean showDiceForm,
                          Model model,
                          Principal principal) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        User currentUser = (User) userService.loadUserByUsername(principal.getName());

        model.addAttribute("game", game);
        model.addAttribute("messages", messageRepository.findByGameOrderByTimestampAsc(game));
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("newMessage", new MessageDto());
        return "game";
    }

    @PostMapping
    public String sendMessage(
            @PathVariable Long gameId,
            @ModelAttribute MessageDto messageDto,
            Principal principal
    ) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        User author = (User) userService.loadUserByUsername(principal.getName());

        Message message = new Message();
        message.setText(messageDto.text);
        message.setAuthor(author);
        message.setGame(game);

        messageRepository.save(message);

        return "redirect:/games/" + gameId;
    }

    @PostMapping("/roll-dice")
    @ResponseBody
    public ResponseEntity<Map<String, String>> rollDice(
            @PathVariable Long gameId,
            @RequestParam int sides,
            Principal principal
    ) {
        // Генерация случайного числа
        int result = new Random().nextInt(sides) + 1;

        // Создание и сохранение сообщения
        Message message = new Message();
        message.setText("Выпало: " + result + " (d" + sides + ")");
        message.setGame(gameRepository.findById(gameId).orElseThrow());
        message.setAuthor((User) userService.loadUserByUsername(principal.getName()));
        message.setSpecial(true);

        messageRepository.save(message);

        // Возвращаем результат для AJAX
        return ResponseEntity.ok(Map.of(
                "result", String.valueOf(result),
                "sides", String.valueOf(sides),
                "user", principal.getName()
        ));
    }

    @Data
    public static class MessageDto {
        private String text;
    }
}