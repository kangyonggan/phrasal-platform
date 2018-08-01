package com.kangyonggan.pp.service;

import com.kangyonggan.common.Params;
import com.kangyonggan.pp.model.Phrasal;

import java.io.IOException;
import java.util.List;

/**
 * @author kangyonggan
 * @since 8/1/18
 */
public interface PhrasalService {

    /**
     * 批量导入成语
     *
     * @param path 文件路径
     * @throws IOException 可能会抛异常
     */
    void importPhrasals(String path) throws IOException;

    /**
     * 搜索成语
     *
     * @param params 参数
     * @return 返回成语列表
     */
    List<Phrasal> searchPhrasals(Params params);

    /**
     * 根据ID查找成语
     *
     * @param id ID
     * @return 返回成语
     */
    Phrasal findPhrasalById(Long id);

    /**
     * 查找以XXX开头的成语
     *
     * @param name 开头的字
     * @return 返回成语列表
     */
    List<Phrasal> searchPhrasalsStartWith(String name);
}
