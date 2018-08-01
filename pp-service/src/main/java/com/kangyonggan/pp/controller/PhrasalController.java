package com.kangyonggan.pp.controller;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.common.Response;
import com.kangyonggan.common.web.BaseController;
import com.kangyonggan.pp.model.Phrasal;
import com.kangyonggan.pp.service.PhrasalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Response list() {
        Response response = Response.getSuccessResponse();

        List<Phrasal> phrasalList = phrasalService.searchPhrasals(getRequestParams());
        PageInfo pageInfo = new PageInfo<>(phrasalList);

        response.put("pageInfo", pageInfo);
        return response;
    }

    /**
     * 成语详情
     *
     * @param id ID
     * @return 返回成语详情
     */
    @GetMapping("{id:[\\d]+}")
    public Response detail(@PathVariable("id") Long id) {
        Response response = Response.getSuccessResponse();
        Phrasal phrasal = phrasalService.findPhrasalById(id);
        List<Phrasal> phrasalList = phrasalService.searchPhrasalsStartWith(phrasal.getName().substring(phrasal.getName().length() - 1));

        response.put("phrasal", phrasal);
        response.put("phrasalList", phrasalList);
        return response;
    }

}
