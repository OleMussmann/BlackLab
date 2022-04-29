package org.ivdnt.blacklab.aggregator.representation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class SearchParam {

    private String indexname = "";

    private String patt = "";

    private String sort = "";

    private String group = "";

    public SearchParam() {
    }

    public SearchParam(String indexname, String patt, String sort, String group) {
        this.indexname = indexname;
        this.patt = patt;
        this.sort = sort;
        this.group = group;
    }

    @Override
    public String toString() {
        return "SearchParam{" +
                "indexname='" + indexname + '\'' +
                ", patt='" + patt + '\'' +
                ", sort='" + sort + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
