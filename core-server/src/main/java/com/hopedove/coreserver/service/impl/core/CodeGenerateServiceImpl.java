package com.hopedove.coreserver.service.impl.core;

import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;
import com.hopedove.commons.response.Response;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.JsonUtil;
import com.hopedove.commons.utils.LocalDateTimeUtil;
import com.hopedove.commons.utils.UserUtil;
import com.hopedove.commons.utils.VOUtil;
import com.hopedove.coreserver.dao.core.ICodeRuleDao;
import com.hopedove.coreserver.service.core.ICodeGenerateService;
import com.hopedove.coreserver.vo.core.BusinCodeVO;
import com.hopedove.coreserver.vo.core.CodeRuleLogVO;
import com.hopedove.coreserver.vo.core.CodeRuleVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.System.out;

//
@Transactional
@RestController("codeGenerateService")
public class CodeGenerateServiceImpl implements ICodeGenerateService {

    private final static Logger log = LoggerFactory.getLogger(CodeGenerateServiceImpl.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ICodeRuleDao codeRuleDao;

    //业务系统之间使用
    @PostMapping("/busincode/{codeType}")
    public String getCode(@PathVariable String codeType, @RequestBody(required = false) BusinCodeVO businCodeVO) {
        Integer factoryId = UserUtil.get("factoryId", Integer.class);
        return getCode(factoryId.toString(), codeType, businCodeVO);
    }
    @PostMapping("/busincode/getCode3")
    public String getCode3( @RequestParam String codeType,@RequestParam Integer factoryId,@RequestBody(required = false) BusinCodeVO businCodeVO) {
        return getCode(factoryId.toString(), codeType, businCodeVO);
    }
    //业务系统之间使用--无token信息使用
    @PostMapping("/busincode/getCode2")
    public String getCode2(@RequestParam String codeType) {
    	String factoryId = "1";
        CodeRuleVO param = new CodeRuleVO();
        param.setFactoryId(Integer.parseInt(factoryId));
        param.setType(codeType);
        param = codeRuleDao.getCodeRule(param);

        //当前年份
        String year = LocalDateTimeUtil.formatNow("yy");

        //当前月份
        String month = LocalDateTimeUtil.formatNow("MM");

        //当前日
        String day = LocalDateTimeUtil.formatNow("dd");

        //主要是计算出年月的前缀，方便后续生成直接使用
        if (!param.getHasYear()) {
            year = "fixed";
        }

        if (!param.getHasMonth()) {
            month = "fixed";
        }

        if (!param.getHasDay()) {
            day = "fixed";
        }

        //当前匹配的编码规则key
        String codeRuleKey = "busincode:" + factoryId + ":" + codeType + ":" + year + ":" + month + ":" + day + ".coderule";

        //当前序列key
//        String seqKey = "busincode:" + factoryId + ":" + codeType + ":" + year + ":" + month + ".seq";

        //获取当前编码规则
        String codeRuleCache = stringRedisTemplate.opsForValue().get(codeRuleKey);
        CodeRuleVO codeRule = JsonUtil.readValue(codeRuleCache, CodeRuleVO.class);

        if (codeRule == null) {
            throw new BusinException(ErrorCode.EXP_CODE_NODATA);
        }

        //获取当前序列
        Long sequence = stringRedisTemplate.opsForValue().increment(codeRule.getSeqKey(), 1L);

        if (sequence > codeRule.getMaxSequence()) {
            throw new BusinException(ErrorCode.EXP_CODE_RANGE);
        }

        String sequenced = codeRule.getPrefix() == null ? "" : codeRule.getPrefix();

        if (codeRule.getHasYear()) {
            sequenced += codeRule.getPrefixYear();
        }

        if (codeRule.getHasMonth()) {
            sequenced += codeRule.getPrefixMonth();
        }

        if (codeRule.getHasDay()) {
            sequenced += codeRule.getPrefixDay();
        }

        sequenced += formatSequence(sequence, codeRule.getMaxSequence());
        try {
            //记录编码生成日志，后期改为异步
            CodeRuleLogVO codeRuleLogVO = new CodeRuleLogVO();
            codeRuleLogVO.setCodeRuleId(codeRule.getId());
            codeRuleLogVO.setResultCode(sequenced);

            BeanUtils.copyProperties(codeRule, codeRuleLogVO, "id", "prefixYear", "prefixMonth", "prefixDay", "seqKey");

            codeRuleLogVO.setCurrentSequence(sequence);
            codeRuleLogVO.setCreateDatetime(LocalDateTime.now());
            codeRuleLogVO.setUpdateDatetime(LocalDateTime.now());
            codeRuleLogVO.setDisabled(0);
            codeRuleDao.addCodeRuleLog(codeRuleLogVO);
        } catch (Exception e) {
            log.error("记录编码生成日志异常", e);
        }
        return sequenced;
    }

    /*
       会被网关调用
       接口描述：生成某一种类型的业务编码
       codeType参数解释：busin_code_rule表type字段
    */
    @PostMapping("/busincode/{factoryId}/{codeType}")
    @Override
    public String getCode(@PathVariable String factoryId, @PathVariable String codeType, @RequestBody(required = false) BusinCodeVO businCodeVO) {
        log.debug("* * 编码生成 * * 工厂={}，编码类型={}", factoryId, codeType);

        CodeRuleVO param = new CodeRuleVO();
        param.setFactoryId(Integer.parseInt(factoryId));
        param.setType(codeType);
        param = codeRuleDao.getCodeRule(param);

        //当前年份
        String year = LocalDateTimeUtil.formatNow("yy");

        //当前月份
        String month = LocalDateTimeUtil.formatNow("MM");

        //当前日
        String day = LocalDateTimeUtil.formatNow("dd");

        //主要是计算出年月的前缀，方便后续生成直接使用
        if (!param.getHasYear()) {
            year = "fixed";
        }

        if (!param.getHasMonth()) {
            month = "fixed";
        }

        if (!param.getHasDay()) {
            day = "fixed";
        }

        //当前匹配的编码规则key
        String codeRuleKey = "busincode:" + factoryId + ":" + codeType + ":" + year + ":" + month + ":" + day + ".coderule";

        //当前序列key
//        String seqKey = "busincode:" + factoryId + ":" + codeType + ":" + year + ":" + month + ".seq";

        //获取当前编码规则
        String codeRuleCache = stringRedisTemplate.opsForValue().get(codeRuleKey);
        CodeRuleVO codeRule = JsonUtil.readValue(codeRuleCache, CodeRuleVO.class);

        if (codeRule == null) {
            throw new BusinException(ErrorCode.EXP_CODE_NODATA);
        }

        //获取当前序列
        Long sequence = stringRedisTemplate.opsForValue().increment(codeRule.getSeqKey(), 1L);

        if (sequence > codeRule.getMaxSequence()) {
            throw new BusinException(ErrorCode.EXP_CODE_RANGE);
        }

        String sequenced = codeRule.getPrefix() == null ? "" : codeRule.getPrefix();

        if (codeRule.getHasYear()) {
            sequenced += codeRule.getPrefixYear();
        }

        if (codeRule.getHasMonth()) {
            sequenced += codeRule.getPrefixMonth();
        }

        if (codeRule.getHasDay()) {
            sequenced += codeRule.getPrefixDay();
        }

        sequenced += formatSequence(sequence, codeRule.getMaxSequence());
        try {
            //记录编码生成日志，后期改为异步
            CodeRuleLogVO codeRuleLogVO = new CodeRuleLogVO();
            codeRuleLogVO.setCodeRuleId(codeRule.getId());
            codeRuleLogVO.setResultCode(sequenced);

            BeanUtils.copyProperties(codeRule, codeRuleLogVO, "id", "prefixYear", "prefixMonth", "prefixDay", "seqKey");

            codeRuleLogVO.setCurrentSequence(sequence);

            VOUtil.fillCreate(codeRuleLogVO);

            codeRuleDao.addCodeRuleLog(codeRuleLogVO);
        } catch (Exception e) {
            log.error("记录编码生成日志异常", e);
        }
        return sequenced;
    }

    //初始化业务编码的编码规则缓存信息
    @PostMapping("/busincode/coderules")
    @Override
    public RestResponse<CodeRuleVO> initCodeRuleToCache(@RequestBody CodeRuleVO codeRule) {
        //默认初始化本日和明日的规则信息
        LocalDateTime[] times = new LocalDateTime[]{LocalDateTime.now(), LocalDateTimeUtil.plus(LocalDateTime.now(), 1, ChronoUnit.DAYS)};

        for (LocalDateTime time : times) {

            String baseKey = "busincode:" + codeRule.getFactoryId() + ":" + codeRule.getType();

            //当前年份
            String year = LocalDateTimeUtil.formatTime(time, "yy");

            //当前月份
            String month = LocalDateTimeUtil.formatTime(time, "MM");

            //当前日
            String day = LocalDateTimeUtil.formatTime(time, "dd");

            //主要是计算出年月的前缀，方便后续生成直接使用
            if (!codeRule.getHasYear()) {
                year = "fixed";
            } else {
                codeRule.setPrefixYear(year);
            }

            baseKey += ":" + year;

            if (!codeRule.getHasMonth()) {
                month = "fixed";
            } else {
                codeRule.setPrefixMonth(month);
            }

            baseKey += ":" + month;

            if (!codeRule.getHasDay()) {
                day = "fixed";
            } else {
                codeRule.setPrefixDay(day);
            }

            baseKey += ":" + day;

            //当前匹配的编码规则key
            String codeRuleKey = baseKey + ".coderule";

            //当前序列key
            String seqKey = baseKey + ".seq";

            codeRule.setSeqKey(seqKey);

            //判断cache中是否已有规则
            String codeRuleCache = stringRedisTemplate.opsForValue().get(codeRuleKey);
            if (StringUtils.isEmpty(codeRuleCache)) {
                //不存在信息，直接创建新的规则缓存
                stringRedisTemplate.opsForValue().set(seqKey, codeRule.getCurrentSequence().toString());

                stringRedisTemplate.opsForValue().set(codeRuleKey, JsonUtil.writeValueAsString(codeRule));

            } else {
                //已有信息的话，需要比对规则是否一致，如果不一致则覆盖。
                //注意这里不覆盖更新当前序列，因为会有并发问题
                CodeRuleVO codeRuleCO = JsonUtil.readValue(codeRuleCache, CodeRuleVO.class);
                if (!(codeRuleCO.getHasMonth().equals(codeRule.getHasMonth()) && codeRuleCO.getHasYear().equals(codeRule.getHasYear()) && codeRuleCO.getHasDay().equals(codeRule.getHasDay()) && codeRuleCO.getMaxSequence().equals(codeRule.getMaxSequence()))) {
                    stringRedisTemplate.opsForValue().set(codeRuleKey, JsonUtil.writeValueAsString(codeRule));
                }
            }

        }

        //最后删除失效的规则信息
        try {
            clearLastYear(codeRule.getFactoryId().toString(), codeRule.getType());
        } catch (Exception e) {
            log.error("清除过时的编码规则异常", e);
        }

        try {
            clearLastMonth(codeRule.getFactoryId().toString(), codeRule.getType());
        } catch (Exception e) {
            log.error("清除过时的编码规则异常", e);
        }

        try {
            clearYesterDay(codeRule.getFactoryId().toString(), codeRule.getType());
        } catch (Exception e) {
            log.error("清除过时的编码规则异常", e);
        }
        return new RestResponse<>(codeRule);
    }

    @PostMapping("/busincode/coderules/temp")
    @Override
    public RestResponse<List<CodeRuleVO>> initCodeRuleToCacheTemp() {
        List<CodeRuleVO> results = new ArrayList<>();

        List<CodeRuleVO> codeRuleVOS = codeRuleDao.getCodeRules(null);
        for (CodeRuleVO codeRuleVO : codeRuleVOS) {
            RestResponse<CodeRuleVO> response = initCodeRuleToCache(codeRuleVO);
            results.add(response.getResponseBody());
        }
        return new RestResponse<>(results);
    }

    //清理按日生成的 昨天的规则信息
    private void clearYesterDay(String factoryId, String codeType) {
        LocalDateTime localDateTime = LocalDateTime.now().minus(1, ChronoUnit.DAYS);

        //年份
        String year = LocalDateTimeUtil.formatTime(localDateTime, "yy");

        //月份
        String month = LocalDateTimeUtil.formatTime(localDateTime, "MM");

        //日
        String day = LocalDateTimeUtil.formatTime(localDateTime, "dd");

        String baseKey = "busincode:" + factoryId + ":" + codeType + ":" + year + ":" + month + ":" + day;

        //匹配的编码规则key
        String codeRuleKey = baseKey + ".coderule";

        //序列key
        String seqKey = baseKey + ".seq";

        stringRedisTemplate.delete(seqKey);

        stringRedisTemplate.delete(codeRuleKey);
    }

    //清理按月生成的 上个月的规则信息
    private void clearLastMonth(String factoryId, String codeType) {
        LocalDateTime localDateTime = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);

        //年份
        String year = LocalDateTimeUtil.formatTime(localDateTime, "yy");

        //月份
        String month = LocalDateTimeUtil.formatTime(localDateTime, "MM");

        //日
        String day = "fixed";

        String baseKey = "busincode:" + factoryId + ":" + codeType + ":" + year + ":" + month + ":" + day;

        //匹配的编码规则key
        String codeRuleKey = baseKey + ".coderule";

        //序列key
        String seqKey = baseKey + ".seq";

        stringRedisTemplate.delete(seqKey);

        stringRedisTemplate.delete(codeRuleKey);
    }

    //清理按年生成的 上一年的规则信息
    private void clearLastYear(String factoryId, String codeType) {
        LocalDateTime localDateTime = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);

        //年份
        String year = LocalDateTimeUtil.formatTime(localDateTime, "yy");

        //月份
        String month = "fixed";

        //日
        String day = "fixed";

        String baseKey = "busincode:" + factoryId + ":" + codeType + ":" + year + ":" + month + ":" + day;

        //匹配的编码规则key
        String codeRuleKey = baseKey + ".coderule";

        //序列key
        String seqKey = baseKey + ".seq";

        stringRedisTemplate.delete(seqKey);

        stringRedisTemplate.delete(codeRuleKey);
    }

    //根据编码规则的最大序列，格式化生成的当前序列
    private final static String formatSequence(Long sequence, Long max) {
        Integer maskLen = max.toString().length();

        String mask = "";
        for (int i = 0; i < maskLen; i++) {
            mask += "0";
        }
        DecimalFormat decimalFormat = new DecimalFormat(mask);

        return decimalFormat.format(sequence);
    }
    
    @PostMapping("/busincode/getNoTokenCode")
    @Override
    public String getNoTokenCode(@RequestParam(required = false) String factoryId, @RequestParam(required = false) String codeType, @RequestParam(required = false) String operatorName) {
        log.debug("* * 编码生成 * * 工厂={}，编码类型={}", factoryId, codeType);

        CodeRuleVO param = new CodeRuleVO();
        param.setFactoryId(Integer.parseInt(factoryId));
        param.setType(codeType);
        param = codeRuleDao.getCodeRule(param);

        //当前年份
        String year = LocalDateTimeUtil.formatNow("yy");

        //当前月份
        String month = LocalDateTimeUtil.formatNow("MM");

        //当前日
        String day = LocalDateTimeUtil.formatNow("dd");

        //主要是计算出年月的前缀，方便后续生成直接使用
        if (!param.getHasYear()) {
            year = "fixed";
        }

        if (!param.getHasMonth()) {
            month = "fixed";
        }

        if (!param.getHasDay()) {
            day = "fixed";
        }

        //当前匹配的编码规则key
        String codeRuleKey = "busincode:" + factoryId + ":" + codeType + ":" + year + ":" + month + ":" + day + ".coderule";

        //当前序列key
//        String seqKey = "busincode:" + factoryId + ":" + codeType + ":" + year + ":" + month + ".seq";

        //获取当前编码规则
        String codeRuleCache = stringRedisTemplate.opsForValue().get(codeRuleKey);
        CodeRuleVO codeRule = JsonUtil.readValue(codeRuleCache, CodeRuleVO.class);

        if (codeRule == null) {
            throw new BusinException(ErrorCode.EXP_CODE_NODATA);
        }

        //获取当前序列
        Long sequence = stringRedisTemplate.opsForValue().increment(codeRule.getSeqKey(), 1L);

        if (sequence > codeRule.getMaxSequence()) {
            throw new BusinException(ErrorCode.EXP_CODE_RANGE);
        }

        String sequenced = codeRule.getPrefix() == null ? "" : codeRule.getPrefix();

        if (codeRule.getHasYear()) {
            sequenced += codeRule.getPrefixYear();
        }

        if (codeRule.getHasMonth()) {
            sequenced += codeRule.getPrefixMonth();
        }

        if (codeRule.getHasDay()) {
            sequenced += codeRule.getPrefixDay();
        }

        sequenced += formatSequence(sequence, codeRule.getMaxSequence());
        try {
            //记录编码生成日志，后期改为异步
            CodeRuleLogVO codeRuleLogVO = new CodeRuleLogVO();
            codeRuleLogVO.setCodeRuleId(codeRule.getId());
            codeRuleLogVO.setResultCode(sequenced);

            BeanUtils.copyProperties(codeRule, codeRuleLogVO, "id", "prefixYear", "prefixMonth", "prefixDay", "seqKey");

            codeRuleLogVO.setCurrentSequence(sequence);

            codeRuleLogVO.setDisabled(0);
            codeRuleLogVO.setCreateDatetime(LocalDateTime.now());
            codeRuleLogVO.setCreateIp(VOUtil.getIP());
            codeRuleLogVO.setUpdateDatetime(LocalDateTime.now());
            codeRuleLogVO.setUpdateIp(VOUtil.getIP());

            //codeRuleLogVO.setCreateUserId(UserUtil.get("id", Integer.class));
            codeRuleLogVO.setCreateUserName(operatorName);
            //codeRuleLogVO.setCreateDeptId(UserUtil.get("deptId", Integer.class));



           // codeRuleLogVO.setUpdateUserId(UserUtil.get("id", Integer.class));
            codeRuleLogVO.setUpdateUserName(operatorName);
           // codeRuleLogVO.setUpdateDeptId(UserUtil.get("deptId", Integer.class));

            codeRuleDao.addCodeRuleLog(codeRuleLogVO);
        } catch (Exception e) {
            log.error("记录编码生成日志异常", e);
        }
        return sequenced;
    }
    
}
