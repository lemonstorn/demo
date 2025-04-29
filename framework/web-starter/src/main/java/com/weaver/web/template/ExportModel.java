package com.weaver.web.template;

import com.weaver.web.enums.ExportType;

/**
 * @author zh 2023/9/17 0:39
 */
public record ExportModel<T>(boolean temp, ExportType exportType, T meta) {
}
