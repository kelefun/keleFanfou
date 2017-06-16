package com.funstill.kelefun.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于XAUTH的请求
 *
 * @author liukaiyang
 * @since 2017/2/16 15:56
 */

public class OAuthRequest {

    //不需要签名的参数
    private List<Parameter> params;
    //需要签名参数
    private Map<String, String> oauthParameters;

    //http请求方式 GET,POST,DELETE等
    private String verb;

    //去除参数的url
    private String baseUrl;

    public OAuthRequest() {
        this.oauthParameters = new HashMap<>();
        this.params = new ArrayList<>();
    }

    public void setParameter(Parameter param) {
        this.params.add(param);
    }
    public void setParameter(String key,String value) {
        this.params.add(new Parameter(key, value));
    }
//
//    public void setParameters(Map<String, String> params) {
//        for (String key : params.keySet()) {
//            this.params.add(new Parameter(key, params.get(key)));
//        }
//    }

    public List<Parameter> getParameters() {
        return params;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    public void setOauthParameter(String key, String value) {
        oauthParameters.put(key, value);
    }

    public List<Parameter> getOauthParameters() {
        List<Parameter> params = new ArrayList<>();
        for (String key : oauthParameters.keySet()) {
            params.add(new Parameter(key, oauthParameters.get(key)));
        }
        return params;
    }
}
