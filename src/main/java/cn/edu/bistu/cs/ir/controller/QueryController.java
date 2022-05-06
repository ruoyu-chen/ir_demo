package cn.edu.bistu.cs.ir.controller;

import cn.edu.bistu.cs.ir.index.IdxService;
import cn.edu.bistu.cs.ir.utils.QueryResponse;
import org.apache.lucene.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 面向检索服务接口的控制器类
 * Restful Web Services/Rest风格的Web服务
 * @author chenruoyu
 */
@RestController
@RequestMapping("/query")
public class QueryController {

    private static final Logger log = LoggerFactory.getLogger(QueryController.class);


    private final IdxService idxService;

    public QueryController(@Autowired IdxService idxService){
        this.idxService = idxService;
    }


    /**
     * 根据关键词对索引进行分页检索，
     * 根据页号和页面大小，
     * 返回指定页的数据记录
     * @param kw 待检索的关键词
     * @param pageNo 页号，默认为1
     * @param pageSize 页的大小，默认为10
     * @return 检索得到的结果记录，以<页面ID, 页面标题>二元组的形式返回
     */
    @GetMapping(value = "/kw", produces = "application/json;charset=UTF-8")
    public QueryResponse<List<Map<String, String>>> queryByKw(@RequestParam(name = "kw") String kw,
                                                                    @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                                                                    @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){
        try {
            //TODO 请大家思考如何在queryByKw函数中添加分页参数
            List<Document> docs = idxService.queryByKw(kw);
            List<Map<String, String>> results = new ArrayList<>();
            for(Document doc : docs){
                Map<String, String> record = new HashMap<>(2);
                record.put("ID", doc.get("ID"));
                record.put("TITLE", doc.get("TITLE"));
                record.put("TIME", doc.get("TIME_STORE"));
                results.add(record);
            }
            return QueryResponse.genSucc("检索成功", results);
        } catch (Exception e) {
            log.error("检索过程中发生异常:[{}]", e.getMessage());
            return QueryResponse.genErr("检索过程中发生异常");
        }
    }
}
