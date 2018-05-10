package com.wx.mybatis.config;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.dom.java.CompilationUnit;

public class GeneratedJavaFileImpl extends GeneratedJavaFile {

    public GeneratedJavaFileImpl(GeneratedJavaFile other, InterfaceImpl unit) {

        super(other,unit);
        origin = other;
        this.unit = unit;
    }

    private GeneratedJavaFile origin;

    private String fileName;

    private String targetPackage;

    private InterfaceImpl unit;


    @Override
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getTargetPackage() {
        if (targetPackage != null && targetPackage != "") {
            return targetPackage;
        }
        return this.origin.getTargetPackage();
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }
}
