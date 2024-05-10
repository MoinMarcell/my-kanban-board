package com.github.moinmarcell.backend.todo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    public Todo getTodo(String id) {
        return todoRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("No todo with id " + id));
    }

    public Todo addTodo(TodoDto todoDto) {
        Todo todo = new Todo(todoDto);
        return todoRepository.save(todo);
    }

    public Todo updateTodo(String id, TodoDto todoDto) {
        Todo todo = getTodo(id);
        todo.setTitle(todoDto.title());
        todo.setDescription(todoDto.description());
        return todoRepository.save(todo);
    }

    public String deleteTodo(String id) {
        Todo todo = getTodo(id);
        todoRepository.delete(todo);
        return "Deleted todo with id " + id;
    }
}
