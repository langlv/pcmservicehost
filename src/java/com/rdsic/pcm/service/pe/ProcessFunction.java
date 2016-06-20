/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.service.pe;

import com.rdsic.pcm.common.Util;
import com.rdsic.pcm.common.HibernateUtil;
import com.rdsic.pcm.common.Constant;
import com.rdsic.pcm.common.GenericHql;
import com.rdsic.pcm.data.entity.Oprdef;
import com.rdsic.pcm.data.entity.Oprtag;
import com.rdsic.pcm.service.impl.PCMException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.json.JSONObject;

/**
 *
 * @author langl
 */
public class ProcessFunction {

    private String function;
    private String jsonTemplate = "";
    private String jsonDescription = "";

    public ProcessFunction(String function)
            throws PCMException {
        this.function = function;

        List<Oprdef> lst = GenericHql.INSTANCE.query("from Oprdef where code=:function", new Object[]{"function", function});
        if (lst.size() <= 0) {
            throw new PCMException("Function was not defined: " + function, Constant.STATUS_CODE.ERR_UNAUTHORIZED_ACTION);
        }
        JSONObject jsonTempl = new JSONObject();
        JSONObject jsonDesc = new JSONObject();
        List<Oprtag> tags = GenericHql.INSTANCE.query("from Oprtag where oprcode=:function order by seq ", new Object[]{"function", function});
        for (Oprtag tag : tags) {
            jsonTempl.put(tag.getId().getTag(), tag.getDefval());
            jsonDesc.put(tag.getId().getTag(), tag);
        }
        this.jsonTemplate = jsonTempl.toString();
        this.jsonDescription = jsonDesc.toString();
    }

    public String getFunction() {
        return this.function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getJsonTemplate() {
        return this.jsonTemplate;
    }

    public String getJsonDescription() {
        return this.jsonDescription;
    }

    public String prepareInput(String jsonStr)
            throws PCMException {
        if (Util.isNullOrEmpty(jsonStr)) {
            throw new PCMException("Invalid json data input", Constant.STATUS_CODE.ERR_SYSTEM_FAIL);
        }
        JSONObject jsonObj = new JSONObject(jsonStr);

        List<Oprtag> tags = GenericHql.INSTANCE.query("from Oprtag where oprcode=:function order by seq ", new Object[]{"function", this.function});
        StringWriter sw = new StringWriter();
        for (int i = 0; i < tags.size(); i++) {
            Oprtag tag = (Oprtag) tags.get(i);
            String tagName = tag.getId().getTag();
            if ((tag.getRequired()) && (!jsonObj.has(tagName))) {
                throw new PCMException("Tag name is required: " + tagName, Constant.STATUS_CODE.ERR_DATA_REQUIRED);
            }
            String tagVal = jsonObj.optString(tagName, tag.getDefval());
            sw.append(tag.getId().getTag().toUpperCase())
                    .append("=").append(tagVal);
            if (i < tags.size() - 1) {
                sw.append(Constant.GLOBAL.PCM_DATA_DELIMITER);
            }
        }
        return sw.toString();
    }

    public String execute(String jsonStr)
            throws PCMException {
        String input = prepareInput(jsonStr);
        String hql = String.format("call %s(:param) ", new Object[]{this.function});

        Query q = HibernateUtil.currentSession().createQuery(hql);
        q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        q.setParameter(":param", input);

        List<Map<String, Object>> list = q.list();

        return new JSONObject(list).toString();
    }
}
