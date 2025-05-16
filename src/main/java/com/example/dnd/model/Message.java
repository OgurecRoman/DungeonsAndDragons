package com.example.dnd.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne
    private User author;

    @ManyToOne
    private Game game;

    @Column(nullable = false)
    private boolean special = false;

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isSpecial() {
        return special;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public User getAuthor() {
        return author;
    }

    public Game getGame() {
        return game;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }
}