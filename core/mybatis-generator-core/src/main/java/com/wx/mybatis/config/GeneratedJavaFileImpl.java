package com.wx.mybatis.config;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.dom.java.CompilationUnit;

public class GeneratedJavaFileImpl extends GeneratedJavaFile {

    public GeneratedJavaFileImpl(GeneratedJavaFile other, CompilationUnit unit) {
        super(other,unit);
    }

    private String fileName;


    @Override
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
