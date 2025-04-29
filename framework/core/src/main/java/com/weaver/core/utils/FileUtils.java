package com.weaver.core.utils;

import cn.hutool.core.io.FileUtil;
import com.weaver.core.exception.FileException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * @Author: zh
 * @CreateTime: 2025-04-04
 * @Description: 文件操作相关工具类
 * @Version: 1.0
 */

@Slf4j
public class FileUtils {
    //是否进行零拷贝的分界点，超过10M的进行零拷贝，否则进行普通nio拷贝
    private static final Integer ZeroCopyLimit = 10 * 1024 * 1024;

    //region 创建文件
    @SneakyThrows
    public static void createFile(String path) {
        if (StringUtils.isBlank(path)) {
            throw new FileException("未传入文件路径，创建文件失败");
        }
        File file = new File(path);
        createFile(file);
    }

    @Contract("null -> fail")
    @SneakyThrows
    public static void createFile(File file) {
        if (file == null) {
            throw new FileException("文件不存在，创建文件失败");
        }
        if (!file.exists()) {
            Files.createFile(file.toPath());
        }
    }

    @SuppressWarnings("rawtypes")
    public static File createJsonFile(List data) {
        return createJsonFile(data, "text");
    }

    public static File createJsonFile(Object data, String type) {
        File tempFile = FileUtil.createTempFile("temp", "." + type, true);
        String jsonString = JsonUtils.objectToJson(data);
        writeFile(tempFile, jsonString);
        return tempFile;
    }
    //endregion

    //region 删除文件
    @SneakyThrows
    public static void deleteFile(String path) {
        if (StringUtils.isBlank(path)) {
            throw new FileException("未传入文件路径，删除文件失败");
        }
        File file = new File(path);
        if (file.exists()) {
            Files.delete(file.toPath());
        }
    }
    //endregion

    //region 读取文件
    @SneakyThrows
    public static String readFile(String path) {
        if (StringUtils.isBlank(path)) {
            throw new FileException("未传入文件路径，读取文件失败");
        }
        return readFile(Paths.get(path).toFile());
    }

    @SneakyThrows
    public static String readFile(File file) {
        if (file == null) {
            throw new FileException("文件不存在，读取文件失败");
        }
        if (!file.exists()) {
            throw new FileException("文件不存在，读取文件失败");
        }
        return Files.readString(file.toPath());
    }
    //endregion

    //region 写入文件

    /**
     * 写入文件
     *
     * @param file    写入的文件
     * @param content 写入的内容
     */
    public static void writeFile(File file, String content) {
        writeFile(file.getAbsolutePath(), content);
    }

    /**
     * 根据文件大小动态判定写入文件方式
     *
     * @param path    文件路径
     * @param content 文件内容
     */
    public static void writeFile(String path, String content) {
        if (StringUtils.isBlank(path)) {
            throw new FileException("未传入文件路径，写入文件失败");
        }
        //通过nio零拷贝content写入文件
        if (content.length() > ZeroCopyLimit) {
            nioCopy(path, content);
        } else {
            zeroCopy(path, content);
        }
    }

    private static void nioCopy(String path, String content) {
        if (StringUtils.isBlank(path)) {
            throw new FileException("未传入文件路径，写入文件失败");
        }
        createFile(path);
        try (FileChannel outChannel = FileChannel.open(Paths.get(path),
                StandardOpenOption.READ,
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE)) {

            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            MappedByteBuffer mappedBuffer = outChannel.map(
                    FileChannel.MapMode.READ_WRITE,
                    outChannel.size(),  // 追加模式
                    bytes.length
            );
            mappedBuffer.put(bytes);
            mappedBuffer.force(); // 确保数据刷入磁盘
        } catch (IOException e) {
            throw new FileException("文件写入失败: " + e.getMessage());
        }
    }

    public static void zeroCopy(String path, String content) {
        if (StringUtils.isBlank(path)) {
            throw new FileException("未传入文件路径，写入文件失败");
        }
        createFile(path);
        try (FileChannel outChannel = FileChannel.open(Paths.get(path),
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            // 将字符串内容包装成ByteBuffer
            ByteBuffer buffer = StandardCharsets.UTF_8.encode(content);

            // 零拷贝写入（通道到通道）
            try (ReadableByteChannel inChannel = Channels.newChannel(
                    new ByteArrayInputStream(buffer.array()))) {
                outChannel.transferFrom(inChannel, 0, buffer.remaining());
            }
        } catch (IOException e) {
            throw new FileException("文件写入失败: " + e.getMessage());
        }
    }

    //endregion

    //下载
    public static void downLoad(HttpServletResponse response, File file) {
        downLoad(response, file.getAbsolutePath());
    }

    public static void downLoad(HttpServletResponse response, String filePath) {
        if (filePath.contains("%")) {
            filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
        }

        ServletOutputStream out = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(filePath);
            String[] dir = filePath.split("/");
            String fileName = dir[dir.length - 1];
            String[] array = fileName.split("[.]");
            String fileType = array[array.length - 1].toLowerCase();
            //设置文件ContentType类型
            if ("jpg,jepg,gif,png".contains(fileType)) {//图片类型
                response.setContentType("image/" + fileType);
            } else if ("pdf".contains(fileType)) {//pdf类型
                response.setContentType("application/pdf");
            } else {//自动判断下载文件类型
                response.setContentType("multipart/form-data");
            }
            //设置文件头：最后一个参数是设置下载文件名
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            out = response.getOutputStream();
            // 读取文件流
            int len;
            byte[] buffer = new byte[1024 * 10];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception ignored) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception ignored) {
            }
        }
    }
}
