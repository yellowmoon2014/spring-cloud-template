package com.hy.template.user.util;

import feign.Feign;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class FeignCloud {

    public static <T> T getInstance(Class<T> targetClass, String url) throws ClassNotFoundException {
        String[] apiSrc = getApiSrc(targetClass);
        String className = apiSrc[0];
        String javaSrc = apiSrc[1];
        Class clazz = new DynamicLoader.MemoryClassLoader(DynamicLoader.compile(className + ".java", javaSrc)).loadClass(className);
        T obj = Feign.builder().target((Class<T>) clazz, url);
        return obj;
    }

    public static String[] getApiSrc(Class<?> clazz) {
        FeignClient feignClient = clazz.getAnnotation(FeignClient.class);
        String path = feignClient.path();//path
        if (path.startsWith("/")) path = path.substring(1);
        String simpleName = clazz.getSimpleName();
        String newClassName = clazz.getName() + "Feign";
        String newClassSimpleName = simpleName + "Feign";
        String packageName = clazz.getPackage().getName();
        String  doc = "package "+packageName+";\n";
        doc+= "\n";
        doc+="public interface "+newClassSimpleName+" extends "+simpleName+" {\n";
        doc+="\n";
        Method[] methods = clazz.getMethods();
        for (Method method: methods) {
            StringBuffer requestLine = new StringBuffer();
            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (postMapping != null) {
                requestLine.append(String.format("POST /%s/%s", path, postMapping.name()));
            }
            if (getMapping != null) {
                requestLine.append(String.format("GET /%s/%s", path, getMapping.value()[0]));
            }
            if (requestMapping != null) {
                requestLine.append(String.format("%s /%s/%s", requestMapping.method()[0].name(), path, requestMapping.name()));
            }
            Parameter[] parameters = method.getParameters();
            StringBuilder paramsBuild = new StringBuilder();
            StringBuilder paramsBuild2 = new StringBuilder();
            String bodyName = null;
            for (Parameter parameter: parameters) {
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                if (requestParam == null) {
                    bodyName = parameter.getName();
                    paramsBuild.append(parameter.getType().getName() + " "+bodyName);
                    continue;
                }
                paramsBuild.append(String.format(",@feign.Param(\"%s\") %s %s", requestParam.value(), parameter.getType().getName(), requestParam.value()));
                paramsBuild2.append(String.format("&%s={%s}", requestParam.value(), requestParam.value()));
            }
            if (paramsBuild2.length() > 0) requestLine.append("?" + paramsBuild2.substring(1));
            String params = "";
            if(paramsBuild.length() > 0) {
                if(paramsBuild.indexOf(",") == 0) params = paramsBuild.substring(1);
                else params = paramsBuild.toString();
            }
            doc+="    @feign.RequestLine(\""+requestLine+"\")\n";
            if (bodyName != null) {
                doc+="    @feign.Body(\"{"+bodyName+"}\")\n";
            }
            doc+="    "+method.getReturnType().getName()+" "+method.getName()+"("+params+");\n";
            doc+= "\n";
        }
        doc+="}\n";
        return new String[]{newClassName, doc};
    }
}

