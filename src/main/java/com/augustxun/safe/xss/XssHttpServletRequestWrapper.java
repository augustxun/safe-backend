package com.augustxun.safe.xss;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }


    /**
     * 过滤request.getParameter的参数
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (!StrUtil.hasEmpty(value)) {
            value = StrUtil.trim(HtmlUtil.cleanHtmlTag(value));
        }
        return value;
    }


    /**
     * 过滤springmvc中的 @RequestParam 注解中的参数
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                if (!StrUtil.hasEmpty(value)) {
                    value = StrUtil.trim(HtmlUtil.cleanHtmlTag(value));
                }
                values[i] = value;
            }
        }
        return values;
    }


    /**
     * 过滤from表单提交参数
     *
     * @return
     * @throws IOException
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameters = super.getParameterMap();
        LinkedHashMap<String, String[]> map = new LinkedHashMap();
        if (parameters != null) {
            for (String key : parameters.keySet()) {
                String[] values = parameters.get(key);
                for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                    if (!StrUtil.hasEmpty(value)) {
                        value = StrUtil.trim(HtmlUtil.cleanHtmlTag(value));
                    }
                    values[i] = value;
                }
                map.put(key, values);
            }
        }
        return map;
    }


    /**
     * 过滤header头参数
     *
     * @return
     * @throws IOException
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (!StrUtil.hasEmpty(value)) {
            value = StrUtil.trim(HtmlUtil.cleanHtmlTag(value));
        }
        return value;
    }

    /**
     * 过滤json请求
     *
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        InputStream in = super.getInputStream();
        InputStreamReader reader = new InputStreamReader(in, Charset.forName("UTF-8"));
        BufferedReader buffer = new BufferedReader(reader);
        StringBuffer body = new StringBuffer();
        String line = buffer.readLine();
        while (line != null) {
            body.append(line);
            line = buffer.readLine();
        }
        buffer.close();
        reader.close();
        in.close();

        /**
         * 处理jsonArray的情况
         */
        String resultStr = null;
        //判断第一个字符是不是为[
        String bodyStr = body.toString();
        if (bodyStr.startsWith("[")) {
            List<Map<String, Object>> list = new ArrayList<>();
            JSONUtil.parseArray(bodyStr).stream().forEach(e -> {
                Map<String, Object> map = JSONUtil.parseObj(e);
                Map<String, Object> result = new LinkedHashMap<>();
                for (String key : map.keySet()) {
                    Object val = map.get(key);
                    if (val instanceof String) {
                        if (!StrUtil.hasEmpty(val.toString())) {
                            result.put(key, StrUtil.trim(HtmlUtil.cleanHtmlTag(val.toString())));
                        }
                    } else {
                        result.put(key, val);
                    }
                }
                list.add(result);
            });
            resultStr = JSONUtil.toJsonStr(list);
        } else {
            Map<String, Object> map = JSONUtil.parseObj(bodyStr);
            Map<String, Object> result = new LinkedHashMap<>();
            for (String key : map.keySet()) {
                Object val = map.get(key);
                if (val instanceof String) {
                    if (!StrUtil.hasEmpty(val.toString())) {
                        result.put(key, StrUtil.trim(HtmlUtil.cleanHtmlTag(val.toString())));
                    }
                } else {
                    result.put(key, val);
                }
            }
            resultStr = JSONUtil.toJsonStr(result);
        }
        ByteArrayInputStream bain = new ByteArrayInputStream(resultStr.getBytes());
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bain.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }


}
