package com.hopedove.coreserver.files.ftp;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface FTPCallback<T> {
    T recv(InputStream var1) throws IOException;
}
