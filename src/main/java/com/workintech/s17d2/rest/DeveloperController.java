package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {
    public Map<Integer, Developer> developers;
    public Taxable developerTax;

    @PostConstruct
    public void init() {
        developers = new HashMap<>();
    }

    @Autowired
    public DeveloperController(Taxable developerTax) {
        this.developerTax = developerTax;
    }

    @GetMapping
    public List<Developer> devs() {
        return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Developer dev(@PathVariable Integer id) {
        return developers.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Developer developer) {
        final Developer newDev = switch (developer.getExperience()) {
            case JUNIOR -> new JuniorDeveloper(developer.getId(), developer.getName(),
                    developer.getSalary() - developer.getSalary() * developerTax.getSimpleTaxRate());
            case MID -> new MidDeveloper(developer.getId(), developer.getName(),
                    developer.getSalary() - developer.getSalary() * developerTax.getMiddleTaxRate());
            case SENIOR -> new SeniorDeveloper(developer.getId(), developer.getName(),
                    developer.getSalary() - developer.getSalary() * developerTax.getUpperTaxRate());
        };
        developers.put(newDev.getId(), newDev);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody Developer developer) {
        developers.put(id, developer);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        developers.remove(id);
    }
}