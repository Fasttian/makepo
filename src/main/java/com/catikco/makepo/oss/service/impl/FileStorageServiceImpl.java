package com.catikco.makepo.oss.service.impl;

import com.catikco.makepo.entity.FileStorage;
import com.catikco.makepo.mapper.FileStorageMapper;
import com.catikco.makepo.oss.FileUtils;
import com.catikco.makepo.oss.ResponsnUtils;
import com.catikco.makepo.oss.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Create By: Cai Rong fei @Gui Yang
 * Time: 7/5/2018  10:55
 * Description：
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {
    @Value("${web.upload-path}")
    private String uploadPath;

    @Autowired
    private FileStorageMapper filestorageMapper;

    /**
     * 保存一次返回一个文件id
     * @param multipartFile
     * @param response
     * @param isTitle
     * @return
     */
    @Override
    public Integer uploads(MultipartFile multipartFile,HttpServletResponse response,Boolean isTitle) {

        StringBuffer result = new StringBuffer("");
        Integer fileId = null;

        if (null != multipartFile.getOriginalFilename()) {
            String tempFileName = null; //完整文件名
            String filePrefix = null;   //文件前缀
            String fileSuffix = null;   //文件后缀
            String filePath = null;     //保存路径：完整文件名+时间戳

            // 保存文件
            File file = null;
            FileUtils fu = new FileUtils();

                tempFileName = multipartFile.getOriginalFilename();

                if("".equals(tempFileName))
                    return null;
                //截取文件前缀并拼接时间戳
                filePrefix = tempFileName.substring(0,tempFileName.lastIndexOf(".")) + System.currentTimeMillis();

                if (null!=(filePrefix)) {
                    fileSuffix = FileUtils.getExt(tempFileName);
                    filePath = filePrefix+"."+fileSuffix;
                    OutputStream out;

                    file = new File(uploadPath + filePath);
                    fu.createFile(file);

                    try {
                        out = new FileOutputStream(file);
                        out.write(multipartFile.getBytes());
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //存文件表
                    FileStorage filestorage = new FileStorage();
                    filestorage.setFilePath(filePath);
                    filestorage.setFileSuffix(fileSuffix);
                    filestorage.setFilePrefix(filePrefix);
                    filestorage.setFileSize(multipartFile.getSize());
                    filestorage.setDeleted(false);
                    filestorage.setCreteTime(new Date());
                    filestorage.setFileStatus((byte)1);       //文件状态：1，临时 2，正常 3，废弃
                    filestorage.setRemark("临时文件");

                    filestorageMapper.insert(filestorage);      //插数据库
                    fileId = filestorage.getId();
                    result.append("{ \"url\":\"" +filestorage.getFilePath() + "\"},");
                    //把文件id存入页面，然后再返回后台存入数据库（ContentImagesFileid）
                    result.append("{ \"fileId\":\"" +filestorage.getId() + "\"},");
                    result.insert(0, "{\"result\":\"success\",\"list\":[").deleteCharAt(result.length() - 1).append("]}");
                    System.out.println("json = " + result.toString());

                    if(!isTitle)
                      ResponsnUtils.print(response, result.toString());
                    return fileId;
                }

        } else {
            ResponsnUtils.print(response, "J_NOT_FIND_FILE");
        }
        return null;
    }
}
