package com.example.test.demo.Services;

import com.example.test.demo.Helpers.CommonHelper;
import com.example.test.demo.Helpers.DateTimeConvertHelper;
import com.example.test.demo.Models.*;
import com.example.test.demo.Network.*;
import com.example.test.demo.Reponsitories.CategoryRepository;
import com.example.test.demo.Reponsitories.ComicRepository;
import com.example.test.demo.Reponsitories.ConfigRepository;
import com.example.test.demo.Reponsitories.LogRepository;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
public class CronService {
    private final MongoTemplate mongoTemplate;
    private final LogRepository logRepository;
    private final ComicRepository comicRepository;
    private final CategoryRepository categoryRepository;

    private final ConfigRepository configRepository;

    private CronService(MongoTemplate mongoTemplate,
                        LogRepository logRepository,
                        ComicRepository comicRepository,
                        CategoryRepository categoryRepository,
                        ConfigRepository configRepository
    ) {
        this.mongoTemplate = mongoTemplate;
        this.logRepository = logRepository;
        this.comicRepository = comicRepository;
        this.categoryRepository = categoryRepository;
        this.configRepository = configRepository;
    }

    @Scheduled(fixedRate = 180000)
    @Async
    public void loadTwo() {
        this.loadData(1);
    }


    public void loadAll() {
        this.loadData(1000);
    }

    private void loadData(int page) {
        try {
            Optional<Config> config = this.configRepository.findByType(Config.TYPE.BASE_CRAWL_URL.name());
            ParseHtml.BASE_CRAWL_URL = config.get().getContent();

            for (int j = 1; j <= page; j++) {
                String baseUrl = ParseHtml.BASE_CRAWL_URL.concat("?page=").concat(String.valueOf(j));
                Document doc = ParseHtml.getHtml(baseUrl);
                Elements elements = ComicNetwork.getList(doc);

                if (elements.isEmpty()) {
                    break;
                }

                for (org.jsoup.nodes.Element element : elements) {

                    String id = ComicNetwork.getId(element);
                    String name = ComicNetwork.getName(element);
                    String anotherName = ComicNetwork.getAnotherName(element);
                    String url = ComicNetwork.getUrl(element);
                    String image = ComicNetwork.getImage(element);
                    String view = ComicNetwork.getViewCount(element);
                    String follow = ComicNetwork.getFollowCount(element);
                    String description = ComicNetwork.getDescription(element);
                    String categories = ComicNetwork.getCategories(element);
                    String status = ComicNetwork.getStatus(element);

                    String chapterId = ComicNetwork.getChapterId(element);
                    String chapterName = ComicNetwork.getChapterName(element);
                    String chapterUrl = ComicNetwork.getChapterUrl(element);
                    String chapterTime = ComicNetwork.getChapterTime(element);

                    Date timeUpdate = DateTimeConvertHelper.convertTimeAgoToDateTime(chapterTime);

                    Chapter chapter = new Chapter(chapterId, chapterName, chapterUrl, chapterTime);
                    Comic comic = new Comic();
                    comic.setId(id);
                    comic.setName(name);
                    comic.setAnotherName(anotherName);
                    comic.setUrl(url);
                    comic.setImage(image);
                    comic.setView(view);
                    comic.setFollow(follow);
                    comic.setDescription(description);
                    comic.setCategories(categories);
                    comic.setStatus(status);
                    comic.setChapter(chapter);
                    comic.setUpdatedAt(timeUpdate);

                    Query query = new Query(Criteria.where("id").is(id));

                    Update update = new Update();
                    Comic comic1 = checkExist(id);
                    if (comic1 == null) {
                        this.createComic(comic);
                        continue;
                    }
                    if (comic1.getChapter().getId() != chapterId){
                        update.set("updated_at", timeUpdate);
                    }

                    update.set("view", view);
                    update.set("follow", follow);
                    update.set("description", description);
                    update.set("categories", categories);
                    update.set("chapter", chapter);
                    update.set("status", status);

                    this.mongoTemplate.findAndModify(query, update, Comic.class);
                }
                this.saveLog("Update " + elements.size() + " comics!", "");
            }
            this.saveLog("update comic end", "");

        } catch (IOException e) {
            this.saveLog("Error update comic", e.getMessage());
        }
    }

    public void createCategories() {
        try {
            Document doc = (Document) ParseHtml.getHtml(ParseHtml.BASE_CRAWL_URL + "?page=1");
            Elements elements = CategoryNetwork.getList(doc);

            for (org.jsoup.nodes.Element element : elements) {
                String name = CategoryNetwork.getName(element);
                if (name.isEmpty()) {
                    continue;
                }
                String title = CategoryNetwork.getTitle(element);
                String url = CategoryNetwork.getUrl(element);

                Category category = new Category();
                category.setName(name);
                category.setTitle(title);
                category.setUrl(url);

                this.categoryRepository.save(category);
            }

            this.saveLog("Create categories success!", "");
        } catch (IOException e) {
            this.saveLog("Error create categories", e.getMessage());
        }

    }

    private void createComic(Comic comic) {
        this.comicRepository.save(comic);
//        this.saveLog("Save  comics!", "");
    }

    private Comic checkExist(String comicId) {
        Query query = Query.query(Criteria.where("id").is(comicId));
        return this.mongoTemplate.findOne(query, Comic.class);
    }

    private void saveLog(String name, String content) {
        Log log = new Log();
        log.setName(name);
        log.setContent(content);
        log.setAddress(this.getClass().getCanonicalName());
        this.logRepository.save(log);
    }
}
