package com.github.moinmarcell.backend.todo;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.util.Objects;

@Document(collection = "todos")
public class Todo {
    @MongoId
    private String id;
    private String title;
    private String description;
    private LocalDate createdAt;

    public Todo(String title, String description) {
        this.id = null; // mongo will generate a unique id
        this.title = title;
        this.description = description;
        this.createdAt = LocalDate.now();
    }

    public Todo() {
        // empty constructor for mongo to create a document
    }

    public Todo(TodoDto todoDto) {
        this(todoDto.title(), todoDto.description());
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // setter for id, used by mongo
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(id, todo.id) && Objects.equals(title, todo.title) && Objects.equals(description, todo.description) && Objects.equals(createdAt, todo.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, createdAt);
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
