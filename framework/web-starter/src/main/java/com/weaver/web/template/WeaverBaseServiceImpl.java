package com.weaver.web.template;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weaver.core.exception.FileException;
import com.weaver.core.model.WeaverResponse;
import com.weaver.core.utils.FileUtils;
import com.weaver.core.utils.JsonUtils;
import com.weaver.db.model.WeaverBaseEntity;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * T -> xxxMapper
 * E -> model
 * @author zh 2023/9/17 1:07
 */
public abstract class WeaverBaseServiceImpl<T extends BaseMapper<E>,E extends WeaverBaseEntity<E>> extends ServiceImpl<T,E> implements IWeaverBaseService<E> {

    @SneakyThrows
    @Override
    public void export(ExportModel<E> exportModelDto, HttpServletResponse response) {
        // 模板
        List<E> data;
        if (exportModelDto != null && exportModelDto.temp()) {
            List<E> models = new ArrayList<>();
            models.add(getEntityClass().getDeclaredConstructor().newInstance());
            data = models;
        } else {
            data = list();
        }
        // 实际导出数据
        File jsonFile = FileUtils.createJsonFile(data);
        FileUtils.downLoad(response, jsonFile);
    }

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public WeaverResponse<String> importData(MultipartFile file, Boolean isCover) {
        if (!"application/json".equals(file.getContentType())) {
            return WeaverResponse.error("文件类型不匹配，请上传txt文件");
        }
        String dataInfo = IoUtil.readUtf8(file.getInputStream());
        if (JsonUtils.isListJson(dataInfo)) {
            List<E> list = JsonUtils.jsonToList(dataInfo, getEntityClass());
            if (CollectionUtil.isEmpty(list)) {
                return WeaverResponse.error("上传文件为空，请确认数据是否正常");
            }
            list.forEach(item -> {
                if (item.getId() == null) {
                    item.setId(IdWorker.getId());
                }
            });
            // 系统导出同步的不需要这一步，但是人工手填的需要
            if (isCover) {
                saveOrUpdateBatch(list);
            } else {
                try {
                    saveBatch(list);
                } catch (Exception e) {
                    if (e.getMessage().contains("Duplicate entry")){
                        throw new FileException("导入失败，数据已存在，请重新导入");
                    }
                    log.error(e.getMessage(),e);
                    throw new FileException("导入失败");
                }
            }
            return WeaverResponse.success("导入成功");
        } else {
            return WeaverResponse.error("文件内容异常，请确认上传文件");
        }
    }
}
