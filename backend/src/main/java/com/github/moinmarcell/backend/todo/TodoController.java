package com.github.moinmarcell.backend.todo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> getTodos() {
        return todoService.getTodos();
    }

    @GetMapping("/{id}")
    public Todo getTodo(@PathVariable String id) {
        return todoService.getTodo(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo addTodo(@RequestBody @Valid TodoDto todoDto) {
        return todoService.addTodo(todoDto);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable String id, @RequestBody @Valid TodoDto todoDto) {
        return todoService.updateTodo(id, todoDto);
    }

    @DeleteMapping("/{id}")
    public String deleteTodo(@PathVariable String id) {
        return todoService.deleteTodo(id);
    }
}
