package com.kangyonggan.pp.controller;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.common.web.BaseController;
import com.kangyonggan.pp.model.Phrasal;
import com.kangyonggan.pp.service.PhrasalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kangyonggan
 * @since 8/1/18
 */
@RestController
@RequestMapping("phrasal")
public class PhrasalController extends BaseController {

    @Autowired
    private PhrasalService phrasalService;

    /**
     * 搜索成语
     *
     * @return 返回搜索结果集
     */
    @GetMapping
    public PageInfo<Phrasal> list() {
        List<Phrasal> phrasalList = phrasalService.searchPhrasals(getRequestParams());
        return new PageInfo<>(phrasalList);
    }

    /**
     * 成语详情
     *
     * @param id ID
     * @return 返回成语详情
     */
    @GetMapping("{id:[\\d]+}")
    public Phrasal detail(@PathVariable("id") Long id) {
        return phrasalService.findPhrasalById(id);
    }

    /**
     * 成语接龙
     *
     * @param name name
     * @return 返回成语列表
     */
    @GetMapping("below")
    public List<Phrasal> below(@RequestParam("name") String name) {
        return phrasalService.searchPhrasalsStartWith(name.substring(name.length() - 1));
    }

}
