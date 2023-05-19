package ga.litterguy.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Document(indexName = "portal_news")
public class PortalNews implements Serializable {
    //这里id名称任意，只要有@Id，对应的就是es的_id，注意两点
    //1、如果没有此时不写getter，setter，那么在_source中是不包含id字段的
    //2、如果设置了book.setId("1")，那么为_source和_id相等，都为1，记住这个1是字符串哦；如果让_source 的id为Number型，则可以设置属性为private Intger id，然后实体操作类的泛型必须为对应为Intger
    @Id
    private String id;
    @Field(type = FieldType.Keyword)
    private String channelName; //频道名称

    // 让一个字段同时支持精确匹配和模糊检索
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_smart"), otherFields = {@InnerField(type = FieldType.Keyword, suffix = "keyword")})
//    @Field(analyzer = "ik_smart", type = FieldType.Text, fielddata = true)
    private String title;//新闻标题
    @Field(analyzer = "ik_smart", type = FieldType.Text, fielddata = true)
    private String description;//内容
    @MultiField(mainField = @Field(type = FieldType.Text), otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword))
    private String link; //新闻链接
    @Field(analyzer = "ik_smart", type = FieldType.Text)
    private String content;//新闻正文
    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss", format = DateFormat.custom)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;//新闻发布时间
    @MultiField(mainField = @Field(type = FieldType.Text), otherFields = @InnerField(suffix = "authorKey", type = FieldType.Keyword))
    private String author;// 作者
    @Field(type = FieldType.Text)
    private String thumbnail;// 缩略图
    @Field(type = FieldType.Integer)
    private int emotion;//新闻情感 0:未处理 1：正向 2：负向 3：中性
    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss", format = DateFormat.custom)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inputDate;//抓取时间

    @Field(type = FieldType.Keyword)
    private String province;
    @Field(type = FieldType.Keyword)
    private String city;
    @Field(type = FieldType.Keyword)
    private String sourceType; // 行业分类、rss抓取来源分类

    /**
     * 数据清洗标记：0未清洗，1已清洗
     */
    @Field(type = FieldType.Integer)
    private int cleanFlag;

    /**
     * 是否读取标记：0未读，1已读
     */
    @Field(type = FieldType.Integer)
    private int readFlag;

    private Float score;

    private int number;
}
