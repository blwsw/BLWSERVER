package com.hopedove.coreserver.dao.core;

import com.hopedove.coreserver.vo.core.CodeRuleLogVO;
import com.hopedove.coreserver.vo.core.CodeRuleSegmentVO;
import com.hopedove.coreserver.vo.core.CodeRuleVO;
import com.hopedove.ucserver.vo.DeptVO;

import java.util.List;
import java.util.Map;

public interface ICodeRuleDao {

    List<CodeRuleVO> getCodeRules(Map<String, Object> param);

    int getCodeRulesCount(Map<String, Object> param);

    CodeRuleVO getCodeRule(CodeRuleVO param);

    int addCodeRule(CodeRuleVO param);

    int modifyCodeRule(CodeRuleVO param);

    int removeCodeRule(Integer id);

    int addCodeRuleLog(CodeRuleLogVO param);
}
