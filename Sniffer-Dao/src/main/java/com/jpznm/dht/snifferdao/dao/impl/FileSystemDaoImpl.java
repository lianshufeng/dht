package com.jpznm.dht.snifferdao.dao.impl;

import com.jpznm.dht.snifferdao.dao.FileSystemDao;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.io.OutputStream;

@Repository
public class FileSystemDaoImpl implements FileSystemDao {


    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Override
    public void save(String fileName, InputStream inputStream) {
        this.gridFsTemplate.store(inputStream, fileName);
    }

    @Override
    @SneakyThrows
    public void read(GridFSFile gridFSFile, OutputStream outputStream) {
        if (gridFSFile == null) {
            return;
        }
        @Cleanup InputStream inputStream = this.gridFsTemplate.getResource(gridFSFile).getInputStream();
        StreamUtils.copy(inputStream, outputStream);
    }


    /**
     * 查询
     *
     * @param fileName
     * @return
     */
    public GridFSFile query(String fileName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("filename").is(fileName));
        return this.gridFsTemplate.findOne(query);
    }
}
