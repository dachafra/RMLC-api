package es.upm.fi.dia.oeg.rmlc.api.model.impl;

import es.upm.fi.dia.oeg.rmlc.api.model.MappingComponent;
import org.apache.commons.rdf.api.BlankNodeOrIRI;
import org.apache.commons.rdf.api.RDF;

import static java.util.Objects.requireNonNull;

/**
 * @author xiao
 */
public abstract class MappingComponentImpl implements MappingComponent {

    private final RDF rdf;

    protected  BlankNodeOrIRI node;

    @Override
    public void setNode(BlankNodeOrIRI node) {
        this.node = requireNonNull(node);
    }

    @Override
    public BlankNodeOrIRI getNode() {
        return node;
    }

    MappingComponentImpl(RDF rdf){
        this.rdf = rdf;
    }

    protected RDF getRDF() { return rdf; }
}
