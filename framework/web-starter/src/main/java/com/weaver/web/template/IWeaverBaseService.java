package com.weaver.web.template;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weaver.core.model.WeaverResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zh 2023/9/17 1:02
 */
public interface IWeaverBaseService<E> extends IService<E> {
    /**
     * 导出
     * @param exportMenuDto
     * @param response
     */
    void export(ExportModel<E> exportMenuDto, HttpServletResponse response);

    WeaverResponse<String> importData(MultipartFile file, Boolean isCover);
}
