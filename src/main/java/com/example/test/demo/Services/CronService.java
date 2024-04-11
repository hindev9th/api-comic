package com.example.test.demo.Services;

import com.example.test.demo.Helpers.DateTimeConvertHelper;
import com.example.test.demo.Http.Responses.ErrorResponse;
import com.example.test.demo.Http.Responses.SuccessResponse;
import com.example.test.demo.Models.Chapter;
import com.example.test.demo.Models.Comic;
import com.example.test.demo.Network.ChapterNetwork;
import com.example.test.demo.Network.ComicNetwork;
import com.example.test.demo.Network.ParseHtml;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
@Component
public class CronService {
    private final MongoTemplate mongoTemplate;

    private CronService(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Scheduled(fixedRate = 180000)
    @Async
    public void loadTwo(){
        this.loadData(2);
    }

    public void loadAll(){
        this.loadData(611);
    }

    private void loadData(int page){
        try {

            for (int j =0; j < page; j++){
                Document doc = (Document) ParseHtml.getHtml(ParseHtml.BASE_COMIC_URL + "?page=" + j);
                Elements elements = ComicNetwork.getList(doc);

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

                    String chapterId = ChapterNetwork.getId(element);
                    String chapterName = ChapterNetwork.getName(element);
                    String chapterUrl = ChapterNetwork.getUrl(element);
                    String chapterTime = ChapterNetwork.getTime(element);

                    Date timeUpdate = DateTimeConvertHelper.convertTimeAgoToDateTime(chapterTime);

                    Chapter chapter = new Chapter(chapterId, chapterName, chapterUrl, chapterTime);

                    Query query = new Query(Criteria.where("id").is(id));

                    Update update = new Update();

                    update.set("id", id);
                    update.set("name", name);
                    update.set("another_name", anotherName);
                    update.set("url", url);
                    update.set("image", image);
                    update.set("view", view);
                    update.set("follow", follow);
                    update.set("description", description);
                    update.set("categories", categories);
                    update.set("chapter", chapter);
                    update.set("status", status);
                    update.set("updated_at", timeUpdate);

                    this.mongoTemplate.upsert(query, update, Comic.class);
                }
            }

            new SuccessResponse<>();

        }catch (IOException e){
            new ErrorResponse<>(400, e.getMessage());
        }
    }
}
