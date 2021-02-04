package com.jpznm.dht.sniffersearch.core.controller;

import com.jpznm.dht.snifferdao.dao.FileSystemDao;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.Cleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("dl")
public class DLController {

    @Autowired
    private FileSystemDao fileSystemDao;

    /**
     *
     */
    @RequestMapping(value = "/{fileName}.torrent", method = RequestMethod.GET)
    public void download(@PathVariable String fileName, HttpServletResponse response) throws Exception {
        GridFSFile gridFSFile = this.fileSystemDao.query(fileName);
        if (gridFSFile == null) {
            response.setStatus(404);
            return;
        }
        response.setContentType("application/x-bittorrent;");
        try {
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "utf-8") + ".torrent");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Length", String.valueOf(gridFSFile.getLength()));
        @Cleanup OutputStream outputStream = response.getOutputStream();
        this.fileSystemDao.read(gridFSFile, outputStream);


    }


}
