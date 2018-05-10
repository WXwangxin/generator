package com.wx.mybatis.config;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;

public class InterfaceImpl extends Interface {

    private String content;

    public InterfaceImpl(FullyQualifiedJavaType type) {
        super(type);
    }

    @Override
    public String getFormattedContent() {
        return content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
