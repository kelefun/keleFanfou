package com.zua.kelefun.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liukaiyang
 * @since 2017/2/16 15:54
 */

public class Request {
    //请求参数
    private List<Parameter> params;

    //请求方式 GET,POST,DELETE
    private String verb;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;
    public Request() {
        this.params = new ArrayList<Parameter>();
    }

    public void setParameter(Parameter param) {
        this.params.add(param);
    }

    public void setParameters(Map<String, String> params) {
        for (String key : params.keySet()) {
            this.params.add(new Parameter(key, params.get(key)));
        }
    }

    /**
     * Obtains the body parameters.
     *
     * @return containing the body parameters.
     */
    public List<Parameter> getParameters() {
        return params;
    }
    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }
}
