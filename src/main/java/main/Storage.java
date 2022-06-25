package main;

import main.model.Category;
import main.model.WikiModel;
import main.repository.CategoryRepository;
import main.repository.WikiRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class Storage {
    final CategoryRepository categoryRepository;
    final WikiRepository wikiRepository;

    public Storage(CategoryRepository categoryRepository, WikiRepository wikiRepository) {
        this.categoryRepository = categoryRepository;
        this.wikiRepository = wikiRepository;
    }

    public void parserJSON(String filePath) {
        try {
            BufferedReader bufferReader = new BufferedReader(new FileReader(filePath));
            String line;
            Set<String> cS = new HashSet<>();

            while ((line = bufferReader.readLine()) != null) {
                if (line.contains("template")) {
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(line);

                    String date = String.valueOf(jsonObject.get("create_timestamp"));
                    date = date.replace("Z", "");
                    LocalDateTime startDate = LocalDateTime.parse(date);

                    date = String.valueOf(jsonObject.get("timestamp"));
                    date = date.replace("Z", "");
                    LocalDateTime dateTime = LocalDateTime.parse(date);

                    WikiModel wikiModel = new WikiModel();
                    wikiModel.setWiki(String.valueOf(jsonObject.get("wiki")));
                    wikiModel.setAuxiliary_text(String.valueOf(jsonObject.get("auxiliary_text")));
                    wikiModel.setCreateTimestamp(startDate);
                    wikiModel.setLanguage(String.valueOf(jsonObject.get("language")));
                    wikiModel.setTitle(String.valueOf(jsonObject.get("title")));
                    wikiModel.setTimestamp(dateTime);

                    JSONArray jsonArray = (JSONArray) jsonObject.get("category");

                    Set<Category> wikisCat = new HashSet<>();
                    for (Object o : jsonArray) {
                        String c = String.valueOf(o);
                        if (!cS.contains(c)) {
                            Category category = new Category();
                            category.setCategory(c);
                            categoryRepository.save(category);
                            cS.add(c);
                        }
                        wikisCat.add(categoryRepository.findByCategory(c));
                    }
                    wikiModel.setCategories(wikisCat);

                    wikiRepository.save(wikiModel);
                }
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
