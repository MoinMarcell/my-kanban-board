package com.github.moinmarcell.backend.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.moinmarcell.backend.exception.CustomErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TodoControllerTest {

    private static final String BASE_URI_TODOS = "/api/todos";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("getTodos - should return empty list")
    void getTodos_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get(BASE_URI_TODOS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("getTodo - should return 200 and found todo")
    void getTodo_shouldReturn201AndSavedTodo() throws Exception {
        TodoDto todoDto = new TodoDto("test", "test");
        String todoDtoJson = objectMapper.writeValueAsString(todoDto);

        MvcResult saveTodoResult = mockMvc.perform(post(BASE_URI_TODOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoDtoJson))
                .andExpect(status().isCreated())
                .andReturn();
        Todo savedTodo = objectMapper.readValue(saveTodoResult.getResponse().getContentAsString(), Todo.class);
        String savedTodoJson = objectMapper.writeValueAsString(savedTodo);

        mockMvc.perform(get(BASE_URI_TODOS + "/" + savedTodo.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(savedTodoJson));
    }

    @Test
    @DisplayName("addTodo - should return 201 and saved todo")
    void addTodo_shouldReturn201AndSavedTodo() throws Exception {
        TodoDto todoDto = new TodoDto("test", "test");
        String todoDtoJson = objectMapper.writeValueAsString(todoDto);

        MvcResult saveTodoResult = mockMvc.perform(post(BASE_URI_TODOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoDtoJson))
                .andExpect(status().isCreated())
                .andReturn();
        Todo savedTodo = objectMapper.readValue(saveTodoResult.getResponse().getContentAsString(), Todo.class);

        assertNotNull(savedTodo.getId());
        assertEquals(todoDto.title(), savedTodo.getTitle());
        assertEquals(todoDto.description(), savedTodo.getDescription());
        assertEquals(LocalDate.now(), savedTodo.getCreatedAt());
    }

    @Test
    @DisplayName("addTodo - should return 400 and error message, when title is null")
    void addTodo_shouldReturn400AndErrorMessageWhenTitleIsNull() throws Exception {
        TodoDto todoDto = new TodoDto(null, "test");
        String todoDtoJson = objectMapper.writeValueAsString(todoDto);

        MvcResult saveTodoResult = mockMvc.perform(post(BASE_URI_TODOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoDtoJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        CustomErrorMessage errorMessage = objectMapper.readValue(saveTodoResult.getResponse().getContentAsString(), CustomErrorMessage.class);
        assertEquals("Bad request: Title is required", errorMessage.message());
        assertNotNull(errorMessage.timestamp());
    }

    @Test
    @DisplayName("addTodo - should return 400 and error message, when title is blank")
    void addTodo_shouldReturn400AndErrorMessageWhenTitleIsBlank() throws Exception {
        TodoDto todoDto = new TodoDto("      ", "test");
        String todoDtoJson = objectMapper.writeValueAsString(todoDto);

        MvcResult saveTodoResult = mockMvc.perform(post(BASE_URI_TODOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoDtoJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        CustomErrorMessage errorMessage = objectMapper.readValue(saveTodoResult.getResponse().getContentAsString(), CustomErrorMessage.class);
        assertEquals("Bad request: Title is required", errorMessage.message());
        assertNotNull(errorMessage.timestamp());
    }

    @Test
    @DisplayName("addTodo - should return 400 and error message, when title has 1 character")
    void addTodo_shouldReturn400AndErrorMessageWhenTitleIsLessThan3Characters() throws Exception {
        TodoDto todoDto = new TodoDto("t", "test");
        String todoDtoJson = objectMapper.writeValueAsString(todoDto);

        MvcResult saveTodoResult = mockMvc.perform(post(BASE_URI_TODOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoDtoJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        CustomErrorMessage errorMessage = objectMapper.readValue(saveTodoResult.getResponse().getContentAsString(), CustomErrorMessage.class);
        assertEquals("Bad request: Title must be between 3 and 255 characters", errorMessage.message());
        assertNotNull(errorMessage.timestamp());
    }

    @Test
    @DisplayName("addTodo - should return 400 and error message, when title has 256 characters")
    void addTodo_shouldReturn400AndErrorMessageWhenTitleHasMoreThan255Characters() throws Exception {
        TodoDto todoDto = new TodoDto("dsdasdasdqwdr235fa cxasc wet 34 tg<syxvcwe et436 asfsadfg 346z taSfcqwe%\"§$ %T<SDVCWDTFGWERT  DSFdg a46534 yv wefasdf asf 463 asdfas f3425t asdfvsadf346tt afvcasdfwrdgt <sfcasd 436 asfasd gferz 547 asdfsa dgfer63456t dysfwsasdfr 435t wraasd fadsrtfasdf sad", "test");
        String todoDtoJson = objectMapper.writeValueAsString(todoDto);

        MvcResult saveTodoResult = mockMvc.perform(post(BASE_URI_TODOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoDtoJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        CustomErrorMessage errorMessage = objectMapper.readValue(saveTodoResult.getResponse().getContentAsString(), CustomErrorMessage.class);
        assertEquals("Bad request: Title must be between 3 and 255 characters", errorMessage.message());
        assertNotNull(errorMessage.timestamp());
    }

    @Test
    @DisplayName("updateTodo - should return 200 and updated todo")
    void updateTodo_shouldReturn200AndUpdatedTodo() throws Exception {
        TodoDto todoDto = new TodoDto("test", "test");
        String todoDtoJson = objectMapper.writeValueAsString(todoDto);

        MvcResult saveTodoResult = mockMvc.perform(post(BASE_URI_TODOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoDtoJson))
                .andExpect(status().isCreated())
                .andReturn();
        Todo savedTodo = objectMapper.readValue(saveTodoResult.getResponse().getContentAsString(), Todo.class);

        TodoDto updatedTodoDto = new TodoDto("testUpdated", "testUpdated");
        String updatedTodoDtoJson = objectMapper.writeValueAsString(updatedTodoDto);

        MvcResult updateTodoResult = mockMvc.perform(put(BASE_URI_TODOS + "/" + savedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTodoDtoJson))
                .andExpect(status().isOk())
                .andReturn();
        Todo updatedTodo = objectMapper.readValue(updateTodoResult.getResponse().getContentAsString(), Todo.class);

        assertEquals(updatedTodo.getId(), savedTodo.getId());
        assertEquals(updatedTodo.getTitle(), updatedTodoDto.title());
        assertEquals(updatedTodo.getDescription(), updatedTodoDto.description());
        assertEquals(updatedTodo.getCreatedAt(), savedTodo.getCreatedAt());
    }

    @Test
    @DisplayName("updateTodo - should return 400 and error message, when title is null")
    void updateTodo_shouldReturn400AndErrorMessageWhenTitleIsNull() throws Exception {
        TodoDto todoDto = new TodoDto("test", "test");
        String todoDtoJson = objectMapper.writeValueAsString(todoDto);

        MvcResult saveTodoResult = mockMvc.perform(post(BASE_URI_TODOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoDtoJson))
                .andExpect(status().isCreated())
                .andReturn();
        Todo savedTodo = objectMapper.readValue(saveTodoResult.getResponse().getContentAsString(), Todo.class);

        TodoDto updatedTodoDto = new TodoDto(null, "testUpdated");
        String updatedTodoDtoJson = objectMapper.writeValueAsString(updatedTodoDto);

        MvcResult updateTodoResult = mockMvc.perform(put(BASE_URI_TODOS + "/" + savedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTodoDtoJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        CustomErrorMessage errorMessage = objectMapper.readValue(updateTodoResult.getResponse().getContentAsString(), CustomErrorMessage.class);
        assertEquals("Bad request: Title is required", errorMessage.message());
        assertNotNull(errorMessage.timestamp());
    }

    @Test
    @DisplayName("updateTodo - should return 400 and error message, when title is blank")
    void updateTodo_shouldReturn400AndErrorMessageWhenTitleIsBlank() throws Exception {
        TodoDto todoDto = new TodoDto("test", "test");
        String todoDtoJson = objectMapper.writeValueAsString(todoDto);

        MvcResult saveTodoResult = mockMvc.perform(post(BASE_URI_TODOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoDtoJson))
                .andExpect(status().isCreated())
                .andReturn();
        Todo savedTodo = objectMapper.readValue(saveTodoResult.getResponse().getContentAsString(), Todo.class);

        TodoDto updatedTodoDto = new TodoDto("    ", "testUpdated");
        String updatedTodoDtoJson = objectMapper.writeValueAsString(updatedTodoDto);

        MvcResult updateTodoResult = mockMvc.perform(put(BASE_URI_TODOS + "/" + savedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTodoDtoJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        CustomErrorMessage errorMessage = objectMapper.readValue(updateTodoResult.getResponse().getContentAsString(), CustomErrorMessage.class);
        assertEquals("Bad request: Title is required", errorMessage.message());
        assertNotNull(errorMessage.timestamp());
    }

    @Test
    @DisplayName("updateTodo - should return 400 and error message, when title has 1 character")
    void updateTodo_shouldReturn400AndErrorMessageWhenTitleIsLessThan3Characters() throws Exception {
        TodoDto todoDto = new TodoDto("test", "test");
        String todoDtoJson = objectMapper.writeValueAsString(todoDto);

        MvcResult saveTodoResult = mockMvc.perform(post(BASE_URI_TODOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoDtoJson))
                .andExpect(status().isCreated())
                .andReturn();
        Todo savedTodo = objectMapper.readValue(saveTodoResult.getResponse().getContentAsString(), Todo.class);

        TodoDto updatedTodoDto = new TodoDto("t", "testUpdated");
        String updatedTodoDtoJson = objectMapper.writeValueAsString(updatedTodoDto);

        MvcResult updateTodoResult = mockMvc.perform(put(BASE_URI_TODOS + "/" + savedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTodoDtoJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        CustomErrorMessage errorMessage = objectMapper.readValue(updateTodoResult.getResponse().getContentAsString(), CustomErrorMessage.class);
        assertEquals("Bad request: Title must be between 3 and 255 characters", errorMessage.message());
        assertNotNull(errorMessage.timestamp());
    }

    @Test
    @DisplayName("updateTodo - should return 400 and error message, when title has 256 characters")
    void updateTodo_shouldReturn400AndErrorMessageWhenTitleHasMoreThan255Characters() throws Exception {
        TodoDto todoDto = new TodoDto("test", "test");
        String todoDtoJson = objectMapper.writeValueAsString(todoDto);

        MvcResult saveTodoResult = mockMvc.perform(post(BASE_URI_TODOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoDtoJson))
                .andExpect(status().isCreated())
                .andReturn();
        Todo savedTodo = objectMapper.readValue(saveTodoResult.getResponse().getContentAsString(), Todo.class);

        TodoDto updatedTodoDto = new TodoDto("dsdasdasdqwdr235fa cxasc wet 34 tg<syxvcwe et436 asfsadfg 346z taSfcqwe%\"§$ %T<SDVCWDTFGWERT  DSFdg a46534 yv wefasdf asf 463 asdfas f3425t asdfvsadf346tt afvcasdfwrdgt <sfcasd 436 asfasd gferz 547 asdfsa dgfer63456t dysfwsasdfr 435t wraasd fadsrtfasdf sad", "test");
        String updatedTodoDtoJson = objectMapper.writeValueAsString(updatedTodoDto);

        MvcResult updateTodoResult = mockMvc.perform(put(BASE_URI_TODOS + "/" + savedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTodoDtoJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        CustomErrorMessage errorMessage = objectMapper.readValue(updateTodoResult.getResponse().getContentAsString(), CustomErrorMessage.class);
        assertEquals("Bad request: Title must be between 3 and 255 characters", errorMessage.message());
        assertNotNull(errorMessage.timestamp());
    }

    @Test
    @DisplayName("updateTodo - should return 404 and error message, when todo not found")
    void updateTodo_shouldReturn404AndErrorMessageWhenTodoNotFound() throws Exception {
        TodoDto updatedTodoDto = new TodoDto("test", "test");
        String updatedTodoDtoJson = objectMapper.writeValueAsString(updatedTodoDto);

        MvcResult updateTodoResult = mockMvc.perform(put(BASE_URI_TODOS + "/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTodoDtoJson))
                .andExpect(status().isNotFound())
                .andReturn();
        CustomErrorMessage errorMessage = objectMapper.readValue(updateTodoResult.getResponse().getContentAsString(), CustomErrorMessage.class);
        assertEquals("Element not found: No todo with id 123", errorMessage.message());
        assertNotNull(errorMessage.timestamp());
    }

    @Test
    @DisplayName("deleteTodo - should return 200 and success message")
    void deleteTodo_shouldReturn200AndSuccessMessage() throws Exception {
        TodoDto todoDto = new TodoDto("test", "test");
        String todoDtoJson = objectMapper.writeValueAsString(todoDto);

        MvcResult saveTodoResult = mockMvc.perform(post(BASE_URI_TODOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoDtoJson))
                .andExpect(status().isCreated())
                .andReturn();
        Todo savedTodo = objectMapper.readValue(saveTodoResult.getResponse().getContentAsString(), Todo.class);

        MvcResult deleteTodoResult = mockMvc.perform(delete(BASE_URI_TODOS + "/" + savedTodo.getId()))
                .andExpect(status().isOk())
                .andReturn();
        String successMessage = deleteTodoResult.getResponse().getContentAsString();
        assertEquals("Deleted todo with id " + savedTodo.getId(), successMessage);
    }

    @Test
    @DisplayName("deleteTodo - should return 404 and error message, when todo not found")
    void deleteTodo_shouldReturn404AndErrorMessageWhenTodoNotFound() throws Exception {
        MvcResult deleteTodoResult = mockMvc.perform(delete(BASE_URI_TODOS + "/123"))
                .andExpect(status().isNotFound())
                .andReturn();
        CustomErrorMessage errorMessage = objectMapper.readValue(deleteTodoResult.getResponse().getContentAsString(), CustomErrorMessage.class);
        assertEquals("Element not found: No todo with id 123", errorMessage.message());
        assertNotNull(errorMessage.timestamp());
    }
}