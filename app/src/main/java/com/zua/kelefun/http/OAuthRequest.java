package com.zua.kelefun.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于XAUTH的请求
 * @author liukaiyang
 * @since 2017/2/16 15:56
 */

public class OAuthRequest extends Request {
    private Map<String, String> oauthParameters;
    public OAuthRequest() {
        this.oauthParameters = new HashMap<String, String>();
    }

    public void setOauthParameter(String key, String value) {
        oauthParameters.put(key, value);
    }

    public List<Parameter> getOauthParameters() {
        List<Parameter> params = new ArrayList<Parameter>();
        for (String key : oauthParameters.keySet()) {
            params.add(new Parameter(key, oauthParameters.get(key)));
        }
        return params;
    }
}
