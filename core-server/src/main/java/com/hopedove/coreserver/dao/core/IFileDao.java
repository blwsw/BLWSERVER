package com.hopedove.coreserver.dao.core;

import com.hopedove.coreserver.vo.core.FileVO;

public interface IFileDao {

    FileVO getFile(Integer fileId);

    Integer addFile(FileVO fileVO);

    Integer removeFile(FileVO fileVO);
}
