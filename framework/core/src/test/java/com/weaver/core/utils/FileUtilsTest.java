package com.weaver.core.utils;

import cn.hutool.core.date.StopWatch;
import com.weaver.core.exception.FileException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

class FileUtilsTest {
    @Test
    void readFile() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("fileRead.txt");
        if (classPathResource.exists()){
            String content = FileUtils.readFile(classPathResource.getFile());
            System.out.println(content);
        }
    }

//    @Test
//    @SneakyThrows
//    void zeroCopyTest() throws IOException {
//
//        boolean needBreak = false;
//        boolean accident = false;
//        //从5M开始测试，每次加1M，测试分界点是什么
//        int start = 5;
//        while (!needBreak){
//            String fileName = String.format("test_%sm.txt", start);
//            String filePath = "D:\\java\\projects\\demo\\framework\\core\\src\\test\\resources\\" + fileName;
//            ClassPathResource classPathResource = new ClassPathResource(filePath);
//            String path = classPathResource.getPath();
//            int targetSize = 10 * 1024 * 1024; // 10MB
//            // 生成随机内容（更接近真实场景）
//            String s = RandomUtil.randomString(targetSize);
//            nioWriteFile(path,s);
//            Thread.sleep(100);
//            //判断当前是否为分界点
//            boolean currentBreak = testNioWrite(filePath);
//            //二次判定都为分界点，说明前一个确实为分界点
//            if (accident && currentBreak){
//                needBreak = true;
//            }
//            accident = currentBreak;
//            //删除旧文件
//            FileUtils.deleteFile(filePath);
//        }
//    }

    @SneakyThrows
    boolean testNioWrite(String filePath){
        ClassPathResource classPathResource = new ClassPathResource(filePath);
        String content = FileUtils.readFile(classPathResource.getPath());
        ClassPathResource w1 = new ClassPathResource("D:\\java\\projects\\demo\\framework\\core\\src\\test\\resources\\w1.txt");
        ClassPathResource w2 = new ClassPathResource("D:\\java\\projects\\demo\\framework\\core\\src\\test\\resources\\w2.txt");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("w1");
        nioWriteFile(w1.getPath(),content);
        stopWatch.stop();
        stopWatch.start("w2");
        zeroCopyWriteFile(w2.getPath(),content);
        stopWatch.stop();
        StopWatch.TaskInfo w1Task = stopWatch.getTaskInfo()[0];
        StopWatch.TaskInfo w2Task = stopWatch.getTaskInfo()[0];
        boolean needBreak = w1Task.getTimeMillis() > w2Task.getTimeMillis();
        if (needBreak){
            System.out.println(stopWatch.prettyPrint());
        }
        FileUtils.deleteFile(w1.getPath());
        FileUtils.deleteFile(w2.getPath());
        return needBreak;

    }


    public static void nioWriteFile(String path, String content) {
        if (StringUtils.isBlank(path)) {
            throw new FileException("未传入文件路径，写入文件失败");
        }
        FileUtils.createFile(path);
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


    public static void zeroCopyWriteFile(String path, String content) {
        if (StringUtils.isBlank(path)) {
            throw new FileException("未传入文件路径，写入文件失败");
        }
        FileUtils.createFile(path);
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


}