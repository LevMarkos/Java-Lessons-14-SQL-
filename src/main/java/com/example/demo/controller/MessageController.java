package com.example.demo.controller;

import com.example.demo.dto.Message;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RestController
@RequestMapping("/message")
public class MessageController {

    private List<Message> messages = new ArrayList<>(Arrays.asList(
            new Message(1, "Дом", "Место где живут люди", LocalDateTime.now()),
            new Message(2, "Глобус", "Уменьшенная модель земли", LocalDateTime.now()),
            new Message(3, "Ручка", "Устройство для письма", LocalDateTime.now())
    ));

    // Вывод всех объектов Person
    @GetMapping
    public List<Message> getMessages() {
        return messages;
    }

    // Возврат объекта Message по id
    @GetMapping("/{id}" )
    public ResponseEntity<Message> findById(@PathVariable int id) {
        Optional<Message> message = messages.stream().filter(m -> m.getId() == id).findFirst();
        return message.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    // Добавление объекта Message
    @PostMapping
    public ResponseEntity<Message> save(@RequestBody Message message) {
        if (message.getTime() == null) {
            message.setTime(LocalDateTime.now());
        }
        messages.add(message);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
    // Изменение объекта Message по id
    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable int id, @RequestBody Message updatedMessage) {
        int index = -1;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId() == id) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Message existingMessage = messages.get(index);
        existingMessage.setTitle(updatedMessage.getTitle());
        existingMessage.setText(updatedMessage.getText());
        existingMessage.setTime(LocalDateTime.now());

        return new ResponseEntity<>(existingMessage, HttpStatus.OK);
    }

    // Удаление объекта Message по id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable int id) {
        boolean removed = messages.removeIf(m -> m.getId() == id);
        return removed ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

