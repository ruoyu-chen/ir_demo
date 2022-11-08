package cn.edu.bistu.cs.ir.index;

import cn.edu.bistu.cs.ir.model.School;
import cn.edu.bistu.cs.ir.utils.FileUtils;
import cn.edu.bistu.cs.ir.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.SloppyMath;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.edu.bistu.cs.ir.model.IdxFields.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(SpringExtension.class)
@Slf4j
public class LuceneTest{

    /**
     * 分词器
     */
    private static final Class<? extends Analyzer> ANALYZER_CLS = StandardAnalyzer.class;

    /**
     * 测试的工作目录
     */
    private static final String TEST_HOME = "workspace/test/";

    /**
     * 测试使用的资源文件
     */
    private static final String TEST_FILE = "school.json";

    /**
     * 索引的Writer
     */
    private IndexWriter iWriter;

    @BeforeEach
    public void initWriter() throws Exception {
        log.info("初始化Writer");
        Analyzer analyzer = ANALYZER_CLS.getConstructor().newInstance();
        Directory directory = new MMapDirectory(Paths.get(TEST_HOME));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        this.iWriter = new IndexWriter(directory, config);
    }

    @AfterEach
    public void destroyWriter() throws Exception {
        if(this.iWriter!=null){
            this.iWriter.close();
        }
        log.info("关闭Writer");
    }

    @BeforeAll
    public static void init() throws Exception {
        IndexWriter iwriter = null;
        try{
            log.info("初始化索引内容");
            //初始化IndexWriter
            Analyzer analyzer = ANALYZER_CLS.getConstructor().newInstance();
            Directory directory = new MMapDirectory(Paths.get(TEST_HOME));
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            iwriter = new IndexWriter(directory, config);
            //读取资源文件中的信息
            String json = Files.readString(new ClassPathResource(TEST_FILE).getFile().toPath());
            List<School> schoolList = JSONObject.parseArray(json, School.class);
            //为学校信息构建索引
            for(School school: schoolList){
                Document doc = toDoc(school);
                iwriter.updateDocument(new Term(ID.name(), String.valueOf(school.getId())), doc);
            }
        } catch (IOException e) {
            log.error("初始化索引失败[{}]", e.getMessage());
            throw new RuntimeException(e);
        }finally {
            if(iwriter!=null){
                iwriter.commit();
                iwriter.close();
                log.info("成功完成索引构建");
            }
        }
    }

    @Test
    public void docCountTest() throws IOException {
        //获取索引中的文档数信息
        try(IndexReader reader = DirectoryReader.open(iWriter)){
            Assertions.assertNotEquals(0, reader.getDocCount(ID.name()));
            Assertions.assertEquals(reader.getDocCount(NAME.name()), reader.getDocCount(ID.name()));
            Assertions.assertEquals(0, reader.getDocCount(EMAIL.name()));
            log.info("共有索引文档[{}]个", reader.getDocCount(ID.name()));
        }
    }

    @Test
    public void complexQueryTest() throws Exception {
        //根据关键词、学校人数、学校的经纬度进行检索并按照距离排序
        String keyword = "grammar";//检索关键词
        double lat = -36.800616;//纬度坐标
        double lon = 174.788477;//经度坐标
        int distance = 50;//到上述坐标的距离范围，以千米为单位
        int roll_upr = 2000;//学生人数上限
        int roll_lwr = 1000;//学生人数下限
        List<SortField> sorts = new ArrayList<>();
        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        //添加距离检索，并添加排序条件
        Query qc = LatLonPoint.newDistanceQuery(LOCATION.name(), lat, lon, distance*1000);
        builder.add(qc, BooleanClause.Occur.MUST);
        sorts.add(LatLonDocValuesField.newDistanceSort(LOCATION.name(), lat, lon));

        //添加学生人数检索，并添加排序条件
        Query qr = IntPoint.newRangeQuery(ROLL.name(), roll_lwr, roll_upr);
        builder.add(qr, BooleanClause.Occur.MUST);
        sorts.add(new SortField(ROLL.name(), SortField.Type.INT, true));

        Analyzer analyzer = ANALYZER_CLS.getConstructor().newInstance();
        QueryParser queryParser = new QueryParser(NAME.name(), analyzer);
        String q = String.format("%s:%s", NAME.name(), QueryParser.escape(keyword));
        Query qk = queryParser.parse(q);
        builder.add(qk, BooleanClause.Occur.MUST);

        int page = 1;
        List<Map.Entry<School, Float>> result = pagedSearch(builder.build(), sorts, page, 10);
        while(!result.isEmpty()){
            log.info("检索结果第[{}]页，共[{}]条:", page, result.size());
            for(Map.Entry<School, Float> entry: result){
                School school = entry.getKey();
                //计算检索结果中的学校到坐标点的距离，单位为米
                double d = SloppyMath.haversinMeters(lat, lon,
                        Double.parseDouble(school.getLatitude()),
                        Double.parseDouble(school.getLongitude()));

                log.info("学校名称[{}], 与指定坐标点的距离[{}千米], 学生人数[{}]",
                        school.getName(), d/1000, school.getTotal_school_roll());
            }
            page++;
            result = pagedSearch(builder.build(), sorts, page, 10);
        }
    }

    @Test
    public void queryByNameTest() throws Exception{
        //根据学校名称字段进行关键词检索
        String keywords = "Albert";
        Analyzer analyzer = ANALYZER_CLS.getConstructor().newInstance();
        QueryParser queryParser = new QueryParser(NAME.name(), analyzer);
        String q = String.format("%s:%s", NAME.name(), QueryParser.escape(keywords));
        Query query = queryParser.parse(q);
        List<Map.Entry<School, Float>> results = search(query, null, 5);
        Assertions.assertFalse(results.isEmpty());
        log.info("检索结果共[{}]条:", results.size());
        for(Map.Entry<School, Float> entry: results){
            log.info("学校名称[{}], 检索评分[{}]", entry.getKey().getName(), entry.getValue());
        }
    }

    @Test
    public void queryByRollTest() throws Exception{
        //根据学生人数进行数字范围检索，并且按照学生人数从大到小排列
        int upperBound = 500;
        int lowerBound = 300;
        Query query =  IntPoint.newRangeQuery(ROLL.name(), lowerBound, upperBound);
        //按照学生人数，从大到小排序
        SortField rollSort = new SortField(ROLL.name(), SortField.Type.INT, true);
        List<Map.Entry<School, Float>> results = search(query, List.of(rollSort), 10);
        Assertions.assertFalse(results.isEmpty());
        log.info("检索结果共[{}]条:", results.size());
        for(Map.Entry<School, Float> entry: results){
            log.info("学校名称[{}], 学生人数[{}]", entry.getKey().getName(), entry.getKey().getTotal_school_roll());
        }
    }


    @Test
    public void queryByTypeTest() throws Exception{
        //根据学校类型进行枚举式的检索
        String type = "Composite";
        Query query =  new TermQuery(new Term(TYPE.name(), type));
        List<Map.Entry<School, Float>> results = search(query, null, 10);
        Assertions.assertFalse(results.isEmpty());
        log.info("检索结果共[{}]条:", results.size());
        for(Map.Entry<School, Float> entry: results){
            log.info("学校名称[{}], 学校类型[{}]", entry.getKey().getName(), entry.getKey().getSchool_type());
        }
    }


    private static Document toDoc(School school){
        Document doc = new Document();
        //添加ID字段
        doc.add(new StringField(ID.name(), String.valueOf(school.getId()), Field.Store.YES));
        //添加名称字段
        doc.add(new TextField(NAME.name(), school.getName(), Field.Store.YES));
        //添加电子邮件字段
        if(!StringUtil.isEmpty(school.getEmail())){
            doc.add(new StoredField(EMAIL.name(), school.getEmail()));
        }
        //添加校长姓名字段
        if(!StringUtil.isEmpty(school.getPrincipal())){
            doc.add(new TextField(PRINCIPAL.name(), school.getPrincipal(), Field.Store.YES));
        }
        //添加电子邮件字段
        if(!StringUtil.isEmpty(school.getWebsite())){
            doc.add(new StoredField(WEBSITE.name(), school.getWebsite()));
        }
        //添加所在城市字段
        if(!StringUtil.isEmpty(school.getCity())){
            doc.add(new TextField(CITY.name(), school.getCity(), Field.Store.YES));
        }
        //添加学校类型字段
        if(!StringUtil.isEmpty(school.getSchool_type())){
            doc.add(new StringField(TYPE.name(), school.getSchool_type(), Field.Store.YES));
        }
        //添加学校位置
        if(!StringUtil.isEmpty(school.getLatitude())&&!StringUtil.isEmpty(school.getLongitude())){
            doc.add(new LatLonPoint(LOCATION.name(),
                    Double.parseDouble(school.getLatitude()),
                    Double.parseDouble(school.getLongitude())));
            doc.add(new LatLonDocValuesField(LOCATION.name(),
                    Double.parseDouble(school.getLatitude()),
                    Double.parseDouble(school.getLongitude())));
            doc.add(new StoredField(LAT.name(), school.getLatitude()));
            doc.add(new StoredField(LON.name(), school.getLongitude()));
        }
        //添加学生人数
        if(!StringUtil.isEmpty(school.getTotal_school_roll())){
            doc.add(new IntPoint(ROLL.name(), Integer.parseInt(school.getTotal_school_roll())));
            doc.add(new StoredField(ROLL.name(), Integer.parseInt(school.getTotal_school_roll())));
            doc.add(new NumericDocValuesField(ROLL.name(), Integer.parseInt(school.getTotal_school_roll())));
        }
        return doc;
    }

    @AfterAll
    public static void destroy(){
        //测试完成后，清理索引目录
        FileUtils.deleteSubDirs(TEST_HOME);
        log.info("完成索引目录清理");
    }

    /**
     * 分页查询
     * @param query    查询条件
     * @param sorts    用于排序的字段，可以为空
     * @param pageNo   页码
     * @param pageSize 每页的记录个数
     * @return 检索结果
     */
    private List<Map.Entry<School, Float>> pagedSearch(Query query, List<SortField> sorts, int pageNo, int pageSize) throws IOException {
        Assertions.assertTrue(pageNo>=1);
        Assertions.assertTrue(pageSize>=1);
        int start = (pageNo-1)*pageSize;
        int end = pageNo*pageSize;
        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(iWriter));
        TopDocs topDocs;
        if(sorts==null||sorts.isEmpty()){
            topDocs = searcher.search(query, end);
        }else{
            topDocs = searcher.search(query, end, new Sort(sorts.toArray(new SortField[]{})));
        }
        List<Map.Entry<School, Float>> schoolList = new ArrayList<>();
        for (int i = start; i < end && i < topDocs.scoreDocs.length; i++){
            ScoreDoc scoreDoc = topDocs.scoreDocs[i];
            schoolList.add(new AbstractMap.SimpleEntry<>(fromDoc(searcher.doc(scoreDoc.doc)), scoreDoc.score));
        }
        return schoolList;
    }

    /**
     * 在索引上执行检索
     * @param query 查询条件
     * @param sorts 用于排序的字段，可以为空
     * @param size  返回的结果数
     * @return 检索结果列表，以School对象和评分组成的二元组形式返回
     * @throws IOException 如果检索的过程中出现索引IO错误，则抛出异常
     */
    private List<Map.Entry<School, Float>> search(Query query,List<SortField> sorts, int size) throws IOException {
        IndexReader iReader = DirectoryReader.open(iWriter);
        IndexSearcher searcher = new IndexSearcher(iReader);
        TopDocs topDocs;
        if(sorts==null||sorts.isEmpty()){
            topDocs = searcher.search(query, size);
        }else{
            topDocs = searcher.search(query, size, new Sort(sorts.toArray(new SortField[]{})));
        }
        List<Map.Entry<School, Float>> schoolList = new ArrayList<>();
        for(ScoreDoc scoreDoc : topDocs.scoreDocs){
            schoolList.add(new AbstractMap.SimpleEntry<>(fromDoc(iReader.document(scoreDoc.doc)), scoreDoc.score));
        }
        return schoolList;
    }

    private School fromDoc(Document doc){
        School school = new School();
        school.setId(Integer.parseInt(doc.get(ID.name())));
        school.setName(doc.get(NAME.name()));
        school.setEmail(doc.get(EMAIL.name()));
        school.setPrincipal(doc.get(PRINCIPAL.name()));
        school.setWebsite(doc.get(WEBSITE.name()));
        school.setCity(doc.get(CITY.name()));
        school.setSchool_type(doc.get(TYPE.name()));
        school.setLatitude(doc.get(LAT.name()));
        school.setLongitude(doc.get(LON.name()));
        school.setTotal_school_roll(doc.get(ROLL.name()));
        return school;
    }
}
