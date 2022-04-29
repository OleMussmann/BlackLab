package org.ivdnt.blacklab.aggregator.helper;

import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.ivdnt.blacklab.aggregator.representation.MetadataValues;

@XmlSeeAlso(MetadataValues.class)
public class MapWrapper {
    @XmlAnyElement
    public List elements;
}
