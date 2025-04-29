package com.weaver.web.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.weaver.core.enums.BaseEnum;
import com.weaver.web.utils.EnumUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zh
 * @CreateTime: 2025-04-13
 * @Description: 枚举类映射
 * @Version: 1.0
 */
@Slf4j
public class EnumManager {
    @Getter
    private static final Map<String, List<Map<String, Object>>> ENUM_MAP = new ConcurrentHashMap<>();
    private static volatile EnumManager instance;

    public static EnumManager getInstance() {
        if (instance == null) {
            synchronized (EnumManager.class) {
                if (instance == null) {
                    instance = new EnumManager();
                }
            }
        }
        return instance;
    }

    private EnumManager() {
    }

    private void initMap(List<String> enumPath) {
        // zh's todo 研究springboot的路径解析器，实现表达式获取路径
        if (ENUM_MAP.isEmpty() && CollectionUtil.isNotEmpty(enumPath)) {
            enumPath.add("com.weaver.enums");
            enumPath.add("com.weaver.web.enums");
            enumPath.stream()
                    .distinct()
                    .forEach(path -> {
                        int length = path.length() + 1;
                        //获取该路径下所有类
                        Reflections reflections = new Reflections(path);
                        //获取继承了ISuperClass的所有类
                        Set<Class<? extends BaseEnum>> classSet = reflections.getSubTypesOf(BaseEnum.class);

                        for (Class<? extends BaseEnum> clazz : classSet) {
                            // 实例化获取到的类
                            if (clazz.isEnum()) {
                                List<Map<String, Object>> mapList = EnumUtils.enumToListMap(clazz);
                                ENUM_MAP.put(clazz.getName().substring(length), mapList);
                            }
                        }
                    });
        }
    }

}
