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
/*
 * .............................................
 *
 *                  _ooOoo_
 *                 o8888888o
 *                 88" . "88
 *                 (| -_- |)
 *                  O\ = /O
 *              ____/`---*\____
 *               . * \\| |// `.
 *             / \\||| : |||// \
 *           / _||||| -:- |||||- \
 *             | | \\\ - /// | |
 *            | \_| **\---/** | |
 *           \  .-\__ `-` ___/-. /
 *            ___`. .* /--.--\ `. . __
 *        ."" *< `.___\_<|>_/___.* >*"".
 *      | | : `- \`.;`\ _ /`;.`/ - ` : | |
 *         \ \ `-. \_ __\ /__ _/ .-` / /
 *======`-.____`-.___\_____/___.-`____.-*======
 *
 * .............................................
 *              佛祖保佑 永无BUG
 *
 * 佛曰:
 * 写字楼里写字间，写字间里程序员；
 * 程序人员写程序，又拿程序换酒钱。
 * 酒醒只在网上坐，酒醉还来网下眠；
 * 酒醉酒醒日复日，网上网下年复年。
 * 但愿老死电脑间，不愿鞠躬老板前；
 * 奔驰宝马贵者趣，公交自行程序员。
 * 别人笑我忒疯癫，我笑自己命太贱；
 * 不见满街漂亮妹，哪个归得程序员？
 *
 * @author wangxin
 */
package com.wx.mybatis.config;


import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.DefaultCommentGenerator;

import java.util.List;

public class CustomCommentGenerator extends DefaultCommentGenerator {

    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        StringBuffer sb = new StringBuffer();
        field.addJavaDocLine("/**");
        if (introspectedColumn.getRemarks() != null) {
            field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
        }

        sb.append(" * 表字段 : ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        sb.append('.');
        sb.append(introspectedColumn.getActualColumnName());
        field.addJavaDocLine(sb.toString());
        field.addJavaDocLine(" */");
    }

    private void addComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        innerClass.addJavaDocLine("/**");
        if (introspectedTable.getRemarks() != null) {
            innerClass.addJavaDocLine(" * " + introspectedTable.getRemarks());
        }
        innerClass.addJavaDocLine(" * 表名 : " + introspectedTable.getFullyQualifiedTable());
        innerClass.addJavaDocLine("* @author wangxin");
        innerClass.addJavaDocLine(" */");
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addComment(topLevelClass, introspectedTable);
    }

    public void addClassComment(InnerClass innerClass,
                                IntrospectedTable introspectedTable) {
        addComment(innerClass, introspectedTable);

    }

    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
    }

    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
    }

    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
    }

    public void addComment(XmlElement xmlElement) {
    }

    public static void main(String[] args) {

        String path = CustomCommentGenerator.class.getResource("/mybatis-generator-config.xml").getPath();
        String[] p = {"-configfile", path, "-overwrite"};
        ShellRunner.main(p);

    }

    public static void addService(List<GeneratedJavaFile> javaFiles) {

        try {
            for (int i = 0; i < javaFiles.size(); i++) {
                GeneratedJavaFile generatedJavaFile = javaFiles.get(i);
                CompilationUnit compilationUnit = generatedJavaFile.getCompilationUnit();
                if (compilationUnit instanceof Interface) {
                    Interface inter = (Interface) generatedJavaFile.getCompilationUnit();
                    GeneratedJavaFileImpl base = new GeneratedJavaFileImpl(generatedJavaFile, inter);
                    String name = generatedJavaFile.getFileName().replace("Mapper", "").replace(".java", "");
                    base.setFileName("BaseMapper.java");
                    String s = base.getCompilationUnit().getFormattedContent().replace(name + "Mapper", "BaseMapper").replace(name + "Criteria", "MC").replace(name, "M");
                    inter.setContent(s);
                    System.out.println(base.getCompilationUnit().getFormattedContent());
                    javaFiles.add(base);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
