package com.ysy.basetools.properties;


import com.ysy.basetools.map.SMap;

/**
 * Created by guoqiang on 2016/8/4.
 */
public class PropertiesNode {
    private String name;
    private String value;
    private final SMap<String, PropertiesNode> children = new SMap<String, PropertiesNode>();

    public SMap<String, PropertiesNode> getChildren() {
        return children;
    }

    public PropertiesNode() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public PropertiesNode(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public void put(String key, PropertiesNode value) {
        children.put(key, value);
    }

    public PropertiesNode getChild(String name) {
        return children.get(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
