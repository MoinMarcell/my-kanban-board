package com.github.moinmarcell.backend.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    private final TodoRepository todoRepository = mock(TodoRepository.class);
    private final TodoService todoService = new TodoService(todoRepository);

    @Test
    @DisplayName("getTodos - should return empty list")
    void getTodos_shouldReturnEmptyList() {
        assertEquals(0, todoService.getTodos().size());
    }

    @Test
    @DisplayName("getTodo - should return todo")
    void getTodo_shouldReturnTodo() {
        Todo todo = new Todo("title", "description");
        when(todoRepository.findById("id")).thenReturn(Optional.of(todo));
        assertEquals(todo, todoService.getTodo("id"));
    }

    @Test
    @DisplayName("getTodo - should throw exception")
    void getTodo_shouldThrowException() {
        when(todoRepository.findById("id")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> todoService.getTodo("id"));
    }

    @Test
    @DisplayName("addTodo - should return newly created todo")
    void addTodo_shouldReturnSavedTodo() {
        TodoDto todoDto = new TodoDto("title", "description");
        Todo todo = new Todo("title", "description");
        when(todoRepository.save(todo)).thenReturn(todo);
        assertEquals(todo, todoService.addTodo(todoDto));
    }

    @Test
    @DisplayName("updateTodo - should return updated todo")
    void updateTodo_shouldReturnUpdatedTodo() {
        TodoDto todoDto = new TodoDto("titleUpdated", "descriptionUpdated");
        Todo existingTodo = new Todo("title", "description");
        Todo updatedTodo = new Todo("titleUpdated", "descriptionUpdated");
        when(todoRepository.findById("id")).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(updatedTodo)).thenReturn(updatedTodo);
        assertEquals(updatedTodo, todoService.updateTodo("id", todoDto));
    }

    @Test
    @DisplayName("updateTodo - should throw exception")
    void updateTodo_shouldThrowException() {
        TodoDto todoDto = new TodoDto("titleUpdated", "descriptionUpdated");
        when(todoRepository.findById("id")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> todoService.updateTodo("id", todoDto));
    }

    @Test
    @DisplayName("deleteTodo - should return delete message")
    void deleteTodo_shouldReturnDeleteMessage() {
        Todo existingTodo = new Todo("title", "description");
        when(todoRepository.findById("id")).thenReturn(Optional.of(existingTodo));
        assertEquals("Deleted todo with id id", todoService.deleteTodo("id"));
    }

    @Test
    @DisplayName("deleteTodo - should throw exception")
    void deleteTodo_shouldThrowException() {
        when(todoRepository.findById("id")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> todoService.deleteTodo("id"));
    }
}