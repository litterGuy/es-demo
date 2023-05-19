package ga.litterguy.controller;

import ga.litterguy.dao.PortalDao;
import ga.litterguy.domain.PortalNews;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("estest")
@Slf4j
public class EsTestController {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Resource
    private PortalDao portalDao;

    @GetMapping("search")
    public Object search() {
        Query query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchAllQuery()).withPageable(PageRequest.of(0, 10)).build();
        SearchHits<PortalNews> search = elasticsearchRestTemplate.search(query, PortalNews.class);
        List<PortalNews> list = search.get().map(SearchHit::getContent).collect(Collectors.toList());
        return list;
    }

    @GetMapping("insert")
    public Object insert() {
        PortalNews news = new PortalNews();
        news.setTitle("es 7.17测试插入");
        news.setDescription("升级spring boot版本，升级es版本进行测试");
        news.setAuthor("es7.17");
        news.setChannelName("自媒体");
        news.setLink("http://litterguy.ga");
        // 不要拿到自增的返回值
        PortalNews save = elasticsearchRestTemplate.save(news);
        log.info(save.toString());
        return save;
    }

    @GetMapping("update")
    public Object update() {
        String id = "D6SnMogBwydCfEu2cSgX";
        // 覆盖修改
        PortalNews news = new PortalNews();
        news.setId(id);
        news.setTitle("es 7.17测试插入");
        news.setDescription("升级spring boot版本，升级es版本进行测试-通过覆盖修改值");
        news.setAuthor("es7.17");
        news.setChannelName("自媒体");
        news.setLink("http://litterguy.ga");
        elasticsearchRestTemplate.save(news);

        //根据ID 修改某个字段
        Document document = Document.create();
        document.putIfAbsent("title", "es 7.17测试插入数据更新"); //更新后的内容
        UpdateQuery updateQuery = UpdateQuery.builder(id)
                .withDocument(document)
                .withRetryOnConflict(5) //冲突重试
                .withDocAsUpsert(true) //不加默认false。true表示更新时不存在就插入
                .build();
        UpdateResponse response = elasticsearchRestTemplate.update(updateQuery, IndexCoordinates.of("portal_news"));
        log.info(response.getResult().toString()); //UPDATED 表示更新成功

        return elasticsearchRestTemplate.get(id, PortalNews.class);
    }

    @GetMapping("delete")
    public Object delete(String id) {
        // 根据id删除
//        String r = elasticsearchRestTemplate.delete(id, IndexCoordinates.of("portal_news"));
//        log.info("r : {} ", r);

        // 根据对象删除
        PortalNews news = elasticsearchRestTemplate.get(id, PortalNews.class);
        String r = elasticsearchRestTemplate.delete(news);

        // 根据条件删除
//        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//        queryBuilder.must(QueryBuilders.matchQuery("title", "es 7.17测试插入"));
//        queryBuilder.must(QueryBuilders.matchQuery("channelName", "自媒体"));
//        Query query = new NativeSearchQuery(queryBuilder);
//        ByQueryResponse r = elasticsearchRestTemplate.delete(query, PortalNews.class, IndexCoordinates.of("portal_news"));
        return r;
    }

    @GetMapping("useJpa")
    public Object useJpa(String id) {
        // 查询
        Optional<PortalNews> news = portalDao.findById(id);
        if (!news.isPresent()) {
            return new HashMap<String, String>() {{
                put("msg", "查询不到结果");
            }};
        }
        // 修改
        PortalNews bean = news.get();
        bean.setTitle("jpa操作数据");
        portalDao.save(bean);

        // 分页查询
        Page<PortalNews> pages = portalDao.findAll(PageRequest.of(0, 10));
        return pages;
    }
}
