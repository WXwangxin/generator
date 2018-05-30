/**
 *    Copyright 2006-2018 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.wx.mybatis.config;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author wangxin
 * @version 1.0 2018-05-11
 */
public class OfferServiceLayer {

    public static final String offset = "    ";

    public static final String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    private static final String remark = "/**\r\n"
            + " * @author wangxin \r\n"
            + " * @version 1.0 " + date + "\r\n"
            + " */\r\n";

    private static final String exampleAlias = System.getProperty("exampleAlias","Example");


    public static GeneratedJavaFile findModel(List<GeneratedJavaFile> javaFiles, String javaName) {
        for (int i = 0; i < javaFiles.size(); i++) {
            GeneratedJavaFile generatedJavaFile = javaFiles.get(i);
            CompilationUnit compilationUnit = generatedJavaFile.getCompilationUnit();
            if (compilationUnit instanceof TopLevelClass) {
                TopLevelClass unit = (TopLevelClass) compilationUnit;
                String name = generatedJavaFile.getFileName();
                name = name.replace(".java", "");
                if (name.equals(javaName)) {
                    return generatedJavaFile;
                }
            }
        }
        return null;
    }


    public static GeneratedJavaFileImpl getBaseServiceInter(GeneratedJavaFile generatedJavaFile, String baseMapperContent, String modelPackage) {
        CompilationUnit compilationUnit = generatedJavaFile.getCompilationUnit();
        InterfaceImpl serviceInter = new InterfaceImpl(compilationUnit.getType());
        GeneratedJavaFileImpl serviceBase = new GeneratedJavaFileImpl(generatedJavaFile, serviceInter);
        serviceBase.setFileName("BaseService.java");
        String packagePrefix = modelPackage.substring(0, modelPackage.lastIndexOf("."));
        String servicePackageName = packagePrefix + ".service"; //service层目录
        serviceBase.setTargetPackage(servicePackageName);
        //更改package
        String[] strings = baseMapperContent.split(";"); //第0个就是package信息

        serviceInter.setContent(baseMapperContent.replace("Mapper", "Service")
                .replace(strings[0], "package " + servicePackageName)
                .replace("@Param(\"record\") ", "")
                .replace("@Param(\"example\") ", "")
                .replace("import org.apache.ibatis.annotations.Param;\r\n", "")
                .replace("    long countByExample(MC example);\r\n", "")
                .replace("    int insert(M record);\r\n\r\n", "")
                .replace("    int updateByPrimaryKey(M record);\r\n", "")

        );
        return serviceBase;
    }

    public static GeneratedJavaFileImpl getBaseServiceClass(GeneratedJavaFile generatedJavaFile, String baseServiceContent, String modelPackage) {
        CompilationUnit compilationUnit = generatedJavaFile.getCompilationUnit();
        InterfaceImpl serviceInter = new InterfaceImpl(compilationUnit.getType());
        GeneratedJavaFileImpl serviceBase = new GeneratedJavaFileImpl(generatedJavaFile, serviceInter);
        serviceBase.setFileName("BaseServiceImpl.java");
        String packagePrefix = modelPackage.substring(0, modelPackage.lastIndexOf("."));
        String servicePackageName = packagePrefix + ".service"; //service层目录
        serviceBase.setTargetPackage(servicePackageName);
        String content = baseServiceContent.replace("public interface", "@Service\r\n@Transactional\r\npublic abstract class BaseServiceImpl<ID, M, MC> implements");
        //更改package
        String[] strings = content.split(";");
        content = content.replace(strings[0], "package " + servicePackageName);
        //写入basedao,写入Spring注解
        String baseMapperImport = generatedJavaFile.getTargetPackage() + ".BaseMapper;";
        content = content.replace("import java.util.List;"
                , "import " + baseMapperImport
                        + "\r\nimport org.springframework.stereotype.Service;"
                        + "\r\nimport org.springframework.beans.factory.annotation.Autowired;"
                        + "\r\nimport org.springframework.transaction.annotation.Transactional;"
                        + "\r\nimport org.springframework.util.Assert;"
                        + "\r\n\r\nimport java.util.List;")
                .replace("{", "{\r\n\r\n    @Autowired\r\n    private BaseMapper<ID, M, MC> baseMapper;");
        //拆分具体方法
        String methodsStr = content.substring(content.indexOf("baseMapper;") + 11, content.indexOf("}"));
        methodsStr = methodsStr.replace("\r\n", "").replace("    ", ""); //取得方法
        String methods[] = methodsStr.split(";");
        for (int i = 0; i < methods.length; i++) {
            String method = methods[i];
            String methodName = method.substring(method.indexOf(" ") + 1, method.indexOf("("));
            String[] parameters = method.substring(method.indexOf("(") + 1, method.indexOf(")")).split(",");
            StringBuilder sb = new StringBuilder();
            sb.append("{\r\n");
            for (int j = 0; j < parameters.length; j++) {
                String parameter = parameters[j];
                parameter = parameter.trim();
                sb.append(offset + offset + "Assert.notNull(" + parameter.split(" ")[1] + ",\"你访问的参数非法!\");\r\n");
            }
            sb.append(offset + offset + "return baseMapper." + methodName + "(");
            for (int j = 0; j < parameters.length; j++) {
                String parameter = parameters[j];
                parameter = parameter.trim();
                sb.append(parameter.split(" ")[1]).append(",");
            }
            sb.delete(sb.lastIndexOf(","), sb.length());//删除最后一个逗号
            sb.append(");\r\n" + offset + "}");
            content = content.replace(method + ";", "public " + method + sb.toString());
        }
        serviceInter.setContent(content);
        return serviceBase;
    }


    public static GeneratedJavaFileImpl getServiceInter(GeneratedJavaFile generatedJavaFile, String baseMapperContent, String modelPackage, String modelName, String idType) {
        CompilationUnit compilationUnit = generatedJavaFile.getCompilationUnit();
        InterfaceImpl serviceInter = new InterfaceImpl(compilationUnit.getType());
        GeneratedJavaFileImpl service = new GeneratedJavaFileImpl(generatedJavaFile, serviceInter);
        service.setFileName(modelName + "Service.java");
        String packagePrefix = modelPackage.substring(0, modelPackage.lastIndexOf("."));
        String servicePackageName = packagePrefix + ".service"; //service层目录
        service.setTargetPackage(servicePackageName);
        String content = baseMapperContent.replace("Mapper", "Service");
        //更改package
        String[] strings = content.split(";");
        content = content.replace(strings[0], "package " + servicePackageName);
        serviceInter.setContent(content);
        return service;
    }

    public static GeneratedJavaFileImpl getServiceClass(GeneratedJavaFile generatedJavaFile, String baseMapperContent, String modelPackage, String modelName, String idType) {
        CompilationUnit compilationUnit = generatedJavaFile.getCompilationUnit();
        InterfaceImpl serviceInter = new InterfaceImpl(compilationUnit.getType());
        GeneratedJavaFileImpl service = new GeneratedJavaFileImpl(generatedJavaFile, serviceInter);
        service.setFileName(modelName + "ServiceImpl.java");
        String packagePrefix = modelPackage.substring(0, modelPackage.lastIndexOf("."));
        String servicePackageName = packagePrefix + ".service"; //service层目录
        service.setTargetPackage(servicePackageName);

        String content = baseMapperContent
                .replace("interface", "class")
                .replace("public class " + modelName + "Mapper extends BaseMapper<" + idType + ", " + modelName + ", " + modelName + ""+exampleAlias+">",
                        "@Service\r\n@Transactional(rollbackFor = Exception.class)\r\npublic class " + modelName + "ServiceImpl extends BaseServiceImpl<" + idType + ", " + modelName + ", " + modelName + ""+exampleAlias+"> implements " + modelName + "Service");
        //更改package
        String[] strings = content.split(";");
        content = content.replace(strings[0] + ";", "package " + servicePackageName + ";"
                + "\r\n\r\nimport org.springframework.stereotype.Service;"
                + "\r\nimport org.springframework.transaction.annotation.Transactional;");
        serviceInter.setContent(content);
        return service;
    }

    public static void addService(List<GeneratedJavaFile> javaFiles) {
        try {
            boolean isFirst = true;
            GeneratedJavaFileImpl base = null;
            List<GeneratedJavaFile> extendsFiles = new ArrayList<GeneratedJavaFile>();
            for (int i = 0; i < javaFiles.size(); i++) {
                GeneratedJavaFile generatedJavaFile = javaFiles.get(i);
                CompilationUnit compilationUnit = generatedJavaFile.getCompilationUnit();

                if (compilationUnit instanceof Interface) {
                    Interface unit = (Interface) compilationUnit;
                    Set<FullyQualifiedJavaType> types = unit.getImportedTypes();
                    FullyQualifiedJavaType type = (FullyQualifiedJavaType) types.toArray()[0];
                    String nameImport = type.getFullyQualifiedName();
                    String name = nameImport.substring(nameImport.lastIndexOf(".") + 1);
                    GeneratedJavaFile modelJavaFile = findModel(javaFiles, name);
                    String modelPackage = modelJavaFile.getTargetPackage();
                    TopLevelClass modelUnit = (TopLevelClass) modelJavaFile.getCompilationUnit();
                    String idType = "";
                   if(modelUnit.getSuperClass()!=null && modelUnit.getSuperClass().getShortName().equals(name+"Key")){
                       idType = modelUnit.getSuperClass().getShortName();//兼容多主键情况
                   }else {
                       idType = modelUnit.getFields().get(0).getType().getShortName(); //取第一个属性为主键
                   }
                    if (isFirst) {
                        InterfaceImpl inter = new InterfaceImpl(compilationUnit.getType());
                        base = new GeneratedJavaFileImpl(generatedJavaFile, inter);
                        base.setFileName("BaseMapper.java");
                        String s = generatedJavaFile.getCompilationUnit().getFormattedContent()
                                .replace("import " + modelPackage + "." + name + ";\r\n", "")
                                .replace("import " + modelPackage + "." + name + ""+exampleAlias+";\r\n", "")
                                .replace(name + "Mapper", "BaseMapper<ID, M, MC>").replace(name + ""+exampleAlias+"", "MC").replace(name, "M")
                                .replace(idType + " ", "ID ");

                        inter.setContent(s);
                        System.out.println(base.getCompilationUnit().getFormattedContent());
                        isFirst = false;
                        GeneratedJavaFileImpl serviceBase = getBaseServiceInter(generatedJavaFile, s, modelPackage);

                        GeneratedJavaFileImpl serviceBaseClass = getBaseServiceClass(generatedJavaFile, serviceBase.getCompilationUnit().getFormattedContent(), modelPackage);
                        extendsFiles.add(serviceBaseClass);
                        extendsFiles.add(serviceBase);
                        extendsFiles.add(base);
                    }


                    String content = generatedJavaFile.getCompilationUnit().getFormattedContent();
                    content = content.substring(0, content.indexOf("{") + 1) + "\r\n\r\n}";

                    String className = name + "Mapper extends BaseMapper<" + idType + ", " + name + ", " + name + ""+exampleAlias+">";
                    content = content.replace(name + "Mapper", className)
                            .replace("import org.apache.ibatis.annotations.Param;\r\n", "")
                            .replace("import java.util.List;\r\n", "");
                    Interface inter = (Interface) generatedJavaFile.getCompilationUnit();
                    inter.setContent(content);

                    GeneratedJavaFileImpl service = getServiceInter(generatedJavaFile, content, modelPackage, name, idType);
                    GeneratedJavaFileImpl serviceClass = getServiceClass(generatedJavaFile, content, modelPackage, name, idType);
                    extendsFiles.add(serviceClass);
                    extendsFiles.add(service);
                }
            }

            //将多出的文件写入
            for (int i = 0; i < extendsFiles.size(); i++) {
                GeneratedJavaFile generatedJavaFile = extendsFiles.get(i);
                javaFiles.add(generatedJavaFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

