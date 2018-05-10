/**
 * Copyright 2006-2018 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wx.mybatis.config;

import org.mybatis.generator.api.GeneratedJavaFile;

public class GeneratedJavaFileImpl extends GeneratedJavaFile {

    public GeneratedJavaFileImpl(GeneratedJavaFile other, InterfaceImpl unit) {

        super(other, unit);
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
