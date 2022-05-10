package org.ivdnt.blacklab.aggregator.representation;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.ivdnt.blacklab.aggregator.helper.SerializationUtil;
import org.ivdnt.blacklab.aggregator.helper.MapAdapter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@XmlAccessorType(XmlAccessType.FIELD)
public class MetadataField {

    @XmlAttribute
    public String name = "title";

    public String fieldName = "title";

    public boolean isAnnotatedField = false;

    public String displayName = "Title";

    public String description = "Document title";

    public String uiType = "";

    public String type = "";

    public String analyzer = "";

    public String unknownCondition = "";

    public String unknownValue = "";

    @XmlJavaTypeAdapter(MapAdapter.class)
    @JsonSerialize(using= SerializationUtil.StringMapSerializer.class)
    @JsonDeserialize(using= SerializationUtil.StringMapDeserializer.class)
    public Map<String, String> displayValues = new LinkedHashMap<>();

    public Map<String, Integer> fieldValues = new LinkedHashMap<>();

    public boolean valueListComplete = true;

    @Override
    public String toString() {
        return "MetadataField{" +
                "name='" + name + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", isAnnotatedField=" + isAnnotatedField +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", uiType='" + uiType + '\'' +
                ", type='" + type + '\'' +
                ", analyzer='" + analyzer + '\'' +
                ", unknownCondition='" + unknownCondition + '\'' +
                ", unknownValue='" + unknownValue + '\'' +
                ", displayValues=" + displayValues +
                ", fieldValues=" + fieldValues +
                ", valueListComplete=" + valueListComplete +
                '}';
    }
}
