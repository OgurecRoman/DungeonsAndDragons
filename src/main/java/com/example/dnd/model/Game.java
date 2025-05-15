package com.example.dnd.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String theme;
    private int maxPlayers = 10;

    @OneToMany(mappedBy = "game")
    private List<Message> messages = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "game_players",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> players = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getTheme() {
        return theme;
    }

    public String getName() {
        return name;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Set<User> getPlayers() {
        return players;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setPlayers(Set<User> players) {
        this.players = players;
    }
}
