package com.jpznm.dht.snifferdao.dao;

import com.mongodb.client.gridfs.model.GridFSFile;

import java.io.InputStream;
import java.io.OutputStream;

public interface FileSystemDao {

    /**
     * 保存文件
     *
     * @param fileName
     * @param inputStream
     */
    void save(String fileName, InputStream inputStream);


    /**
     * 读取文件流
     *
     * @param gridFSFile
     * @param outputStream
     */
    void read(GridFSFile gridFSFile, OutputStream outputStream);


    /**
     *
     * @param fileName
     * @return
     */
    GridFSFile query(String fileName);

}
