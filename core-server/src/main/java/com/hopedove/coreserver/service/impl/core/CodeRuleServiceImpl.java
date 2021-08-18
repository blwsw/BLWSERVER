package com.hopedove.coreserver.service.impl.core;

import com.hopedove.commons.response.Response;
import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.JsonUtil;
import com.hopedove.commons.utils.SortUtil;
import com.hopedove.commons.utils.VOUtil;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.coreserver.dao.core.ICodeRuleDao;
import com.hopedove.coreserver.service.core.ICodeGenerateService;
import com.hopedove.coreserver.service.core.ICodeRuleService;
import com.hopedove.coreserver.vo.core.CodeRuleVO;
import com.hopedove.ucserver.vo.DeptVO;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "编码规则服务")
@RestController
public class CodeRuleServiceImpl implements ICodeRuleService {

    private final static Logger log = LoggerFactory.getLogger(CodeRuleServiceImpl.class);

    @Autowired
    private ICodeRuleDao codeRuleDao;

    @Autowired
    private ICodeGenerateService codeGenerateService;

    //查询编码规则列表
    @GetMapping("/coderules")
    @Override
    public RestPageResponse<List<CodeRuleVO>> getCodeRules(
            @RequestParam(required = false) Integer factoryId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer currentPage,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sort) {
        //1.查询主数据
        BasicPageVO page = null;
        if (currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);

        Map<String, Object> param = new HashMap<>();
        param.put("type", type);
        param.put("factoryId", factoryId);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if (page != null) {
            int count = codeRuleDao.getCodeRulesCount(param);
            page.setPage_total(count);
        }

        List<CodeRuleVO> datas = codeRuleDao.getCodeRules(param);

        //3.返回
        return new RestPageResponse<>(datas, page);
    }

    @GetMapping("/coderules/{id}")
    @Override
    public RestResponse<CodeRuleVO> getCodeRule(@PathVariable Integer id) {
        CodeRuleVO param = new CodeRuleVO();
        param.setId(id);
        CodeRuleVO node = codeRuleDao.getCodeRule(param);
        return new RestResponse<>(node);
    }

    @PostMapping("/coderules")
    @Override
    public RestResponse<Integer> addCodeRule(@RequestBody CodeRuleVO param) {
        VOUtil.fillCreate(param);

        Integer node = codeRuleDao.addCodeRule(param);

        return new RestResponse<>(node);
    }

    @PutMapping("/coderules/{id}")
    @Override
    public RestResponse<Integer> modifyCodeRule(@PathVariable Integer id, @RequestBody CodeRuleVO param) {
        VOUtil.fillUpdate(param);
        param.setId(id);

        Integer node = codeRuleDao.modifyCodeRule(param);
        return new RestResponse<>(node);
    }

    @DeleteMapping("/coderules/{id}")
    @Override
    public RestResponse<Integer> removeCodeRule(@PathVariable Integer id) {
        Integer node = codeRuleDao.removeCodeRule(id);
        return new RestResponse<>(node);
    }

    /**
     * 生产编码规则
     */
    @GetMapping("/coderules/execCode")
    public void execCode(){
        RestPageResponse<List<CodeRuleVO>> response = this.getCodeRules(null, null, null, null, null);
        List<CodeRuleVO> codeRuleVOS = response.getResponseBody();
        if (codeRuleVOS != null && codeRuleVOS.size() > 0) {
            for (CodeRuleVO codeRuleVO : codeRuleVOS) {
                RestResponse<CodeRuleVO> codeRuleResponse = codeGenerateService.initCodeRuleToCache(codeRuleVO);
            }
        }
    }
}
