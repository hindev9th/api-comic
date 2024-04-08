package com.example.test.demo.Services;

import com.example.test.demo.Helpers.DateTimeConvertHelper;
import com.example.test.demo.Http.Responses.ApiResponse;
import com.example.test.demo.Http.Responses.ErrorResponse;
import com.example.test.demo.Http.Responses.SuccessResponse;
import com.example.test.demo.Models.Chapter;
import com.example.test.demo.Models.Comic;
import com.example.test.demo.Network.ChapterNetwork;
import com.example.test.demo.Network.ComicNetwork;
import com.example.test.demo.Network.ParseHtml;
import com.example.test.demo.Reponsitories.ComicRepository;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.jsoup.nodes.Document;

import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComicService {
    private final ComicRepository comicRepository;
    private final MongoTemplate mongoTemplate;
    private final static int LIMIT = 16;

    private ComicService(ComicRepository comicRepository,MongoTemplate mongoTemplate){
        this.comicRepository = comicRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Scheduled(fixedRate = 180000)
    public ApiResponse<?> create(){
        try {

            for (int j =0; j < 5; j++){
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

                    String chapterId = ChapterNetwork.getId(element);
                    String chapterName = ChapterNetwork.getName(element);
                    String chapterUrl = ChapterNetwork.getUrl(element);
                    String chapterTime = ChapterNetwork.getTime(element);

                    Date timeUpdate = DateTimeConvertHelper.convertTimeAgoToDateTime(chapterTime);


                    Chapter chapter = new Chapter(chapterId, chapterName, chapterUrl, chapterTime);

//                    Comic comic = new Comic();
//                    comic.setId(id);
//                    comic.setName(name);
//                    comic.setAnotherName(anotherName);
//                    comic.setUrl(url);
//                    comic.setImage(image);
//                    comic.setView(view);
//                    comic.setFollow(follow);
//                    comic.setDescription(description);
//                    comic.setCategories(categories);
//                    comic.setChapter(chapter);
//                    comic.setUpdated_at(timeUpdate);

                    Query query = new Query(Criteria.where("id").is(id));
                    FindAndModifyOptions options = new FindAndModifyOptions().upsert(true).returnNew(true);

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
                    update.set("updated_at", timeUpdate);

                    this.mongoTemplate.upsert(query, update, Comic.class);
                }
            }

            return new SuccessResponse<>();

        }catch (IOException e){
            return new ErrorResponse<>(400,e.getMessage());
        }
    }

    public ApiResponse<?> list(int page){
        Pageable pageable = PageRequest.of(page,LIMIT, Sort.by("updated_at").descending());
        Page<Comic> comics = this.comicRepository.findAll(pageable);

        Map<String, Object> result = new HashMap<>();

        result.put("list",comics.getContent().stream().map(item ->{
            Map<String, Object> itemOj = new HashMap<>();
            itemOj.put("id",item.getId());
            itemOj.put("name",item.getName());
            itemOj.put("another_name",item.getAnotherName());
            itemOj.put("url",item.getUrl());
            itemOj.put("image",item.getImage());
            itemOj.put("categories",item.getCategories());
            itemOj.put("view",item.getView());
            itemOj.put("follow",item.getFollow());
            itemOj.put("description",item.getDescription());
            itemOj.put("chapter",item.getChapter());

            return itemOj;
        }));

        result.put("total_page",comics.getTotalPages());
        result.put("size_page",LIMIT);
        result.put("current_page",page);
        result.put("total_item",comics.getTotalElements());
        result.put("current_size",comics.getContent().size());

        return new SuccessResponse<>(result);
    }
}
