package com.kangyonggan.pp.service.impl;

import com.github.pagehelper.PageHelper;
import com.kangyonggan.app.util.StringUtil;
import com.kangyonggan.common.BaseService;
import com.kangyonggan.common.Params;
import com.kangyonggan.pp.model.Phrasal;
import com.kangyonggan.pp.service.PhrasalService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * @author kangyonggan
 * @since 8/1/18
 */
@Service
@Log4j2
public class PhrasalServiceImpl extends BaseService<Phrasal> implements PhrasalService {

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void importPhrasals(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            log.error("成语导入失败，文件path={}不存在!", path);
            return;
        }

        BufferedReader reader = null;
        int failureCount = 0;
        try {
            reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isNotEmpty(line)) {
                    Phrasal phrasal = processLine(line);
                    if (phrasal == null) {
                        failureCount++;
                    } else {
                        savePhrasal(phrasal);
                    }
                }
            }
        } catch (Exception e) {
            log.error("成语导入异常，解析文件失败", e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }


        log.info("成语导入完成，一共解析失败{}行", failureCount);
    }

    @Override
    public List<Phrasal> searchPhrasals(Params params) {
        Example example = new Example(Phrasal.class);

        Example.Criteria criteria = example.createCriteria();
        String wordLen = params.getQuery().getString("wordLen");
        if (StringUtils.isNotEmpty(wordLen)) {
            criteria.andEqualTo("wordLen", wordLen);
        }
        String capitalWord = params.getQuery().getString("capitalWord");
        if (StringUtils.isNotEmpty(capitalWord)) {
            criteria.andEqualTo("capitalWord", capitalWord);
        }
        String type = params.getQuery().getString("type");
        if (StringUtils.isNotEmpty(type)) {
            criteria.andEqualTo("type", type);
        }
        String name = params.getQuery().getString("name");
        if (StringUtils.isNotEmpty(name)) {
            criteria.andLike("name", StringUtil.toLike(name));
        }

        example.selectProperties("id", "name");

        String sort = params.getSort();
        String order = params.getOrder();
        if (!StringUtil.hasEmpty(sort, order)) {
            example.setOrderByClause(sort + " " + order.toUpperCase());
        } else {
            example.setOrderByClause("id desc");
        }

        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        return myMapper.selectByExample(example);
    }

    @Override
    public Phrasal findPhrasalById(Long id) {
        return myMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Phrasal> searchPhrasalsStartWith(String name) {
        Example example = new Example(Phrasal.class);

        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(name)) {
            criteria.andLike("name", name + "%");
        }

        example.selectProperties("id", "name");

        example.setOrderByClause("id desc");
        PageHelper.startPage(1, 20);
        return myMapper.selectByExample(example);
    }

    private void savePhrasal(Phrasal phrasal) {
        try {
            myMapper.insertSelective(phrasal);
        } catch (Exception e) {
            log.error("保存成语失败, 可能已存在", e);
        }
    }

    private Phrasal processLine(String line) {
        try {
            Phrasal phrasal = new Phrasal();
            String name = line.substring(0, line.indexOf("拼音：")).trim();
            int wordLen = name.length();
            String type = getType(name);
            String spelling = line.substring(line.indexOf("拼音：") + 3, line.indexOf("释义：")).trim();
            String capitalWord = spelling.substring(0, 1).toUpperCase();
            String definition = line.substring(line.indexOf("释义：") + 3, line.indexOf("出处：")).trim();
            String source = line.substring(line.indexOf("出处：") + 3, line.indexOf("示例：")).trim();
            String example = line.substring(line.indexOf("示例：") + 3).trim();

            phrasal.setName(name);
            phrasal.setWordLen(wordLen);
            phrasal.setType(type);
            phrasal.setCapitalWord(capitalWord);
            phrasal.setSpelling(spelling);
            phrasal.setDefinition(definition);
            phrasal.setSource(source);
            phrasal.setExample(example);

            return phrasal;
        } catch (Exception e) {
            log.error("解析成语异常,line={}", line, e);
            return null;
        }
    }

    private String getType(String name) {
        if (StringUtils.isEmpty(name)) {
            return "";
        }

        StringBuilder type = new StringBuilder();
        char[] typeWords = new char[name.length()];
        int index, len = 0;
        for (int i = 0; i < name.toCharArray().length; i++) {
            if ((index = getCharIndex(typeWords, name.charAt(i))) != -1) {
                type.append(Character.valueOf((char) (65 + index)));
            } else {
                type.append(Character.valueOf((char) (65 + len)));
                typeWords[len++] = name.charAt(i);
            }
        }

        return type.toString();
    }

    private int getCharIndex(char[] typeWords, char ch) {
        for (int i = 0; i < typeWords.length; i++) {
            if (ch == typeWords[i]) {
                return i;
            }
        }

        return -1;
    }
}
