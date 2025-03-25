package com.example.demo.controller;

import com.example.demo.dto.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    private List<Person> persons = new ArrayList<>(Arrays.asList(
            new Person(1, "Иван", 25),
            new Person(2, "Петр", 30),
            new Person(3, "Евгений", 28),
            new Person(4, "Максим", 45)
    ));

    @Autowired
    private PersonRepository repository;

    // Вывод всех объектов Person
    @GetMapping
    public List<Person> getPersons() {
        return persons;
    }
    // Поиск обьекта Person по id
    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        Optional<Person> person = persons.stream().filter(p -> p.getId() == id).findFirst();
        return person.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Добавление объекта Person
    @PostMapping
    public ResponseEntity<Person> save(@RequestBody Person person) {
        persons.add(person); // Добавляем в список
        return ResponseEntity.status(HttpStatus.CREATED).body(person);
    }

    // Изменение объекта Person по id
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable int id, @RequestBody Person updatedPerson) {
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getId() == id) {
                persons.get(i).setName(updatedPerson.getName());
                persons.get(i).setAge(updatedPerson.getAge());
                return ResponseEntity.ok(persons.get(i));
            }
        }
        return ResponseEntity.notFound().build();
    }

    // Удаление объекта Person по id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable int id) {
        boolean removed = persons.removeIf(p -> p.getId() == id);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
