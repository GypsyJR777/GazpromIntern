package main.controllers;

import main.Storage;
import main.model.WikiModel;
import main.repository.CategoryRepository;
import main.repository.WikiRepository;
import org.springframework.web.bind.annotation.*;

@RestController
public class WikiController {
    final Storage storage;
    final WikiRepository wikiRepository;
    final CategoryRepository categoryRepository;

    public WikiController(WikiRepository wikiRepository, CategoryRepository categoryRepository, Storage storage) {
        this.wikiRepository = wikiRepository;
        this.categoryRepository = categoryRepository;
        this.storage = storage;
    }

    @PostMapping("/update")
    public String update(@RequestParam(name = "path", required = false) String path) {
        if (path != null) {
            storage.parserJSON(path);
            return "Update some data";
        }
        return "Null path";
    }

    @GetMapping("/wiki/{title}")
    public Object getWikiPretty(@PathVariable String title, @RequestParam(required = false) String pretty) {
        WikiModel wiki = wikiRepository.findByTitle(title);
        if (pretty != null) {
            return wiki;
        }

        return wiki.toString();
    }
}
