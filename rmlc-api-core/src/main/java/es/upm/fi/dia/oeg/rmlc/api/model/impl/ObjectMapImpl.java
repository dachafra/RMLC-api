/*******************************************************************************
 * Copyright 2013, the Optique Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This first version of the R2RML API was developed jointly at the University of Oslo, 
 * the University of Bolzano, La Sapienza University of Rome, and fluid Operations AG, 
 * as part of the Optique project, www.optique-project.eu
 ******************************************************************************/
package es.upm.fi.dia.oeg.rmlc.api.model.impl;

import es.upm.fi.dia.oeg.rmlc.api.model.ObjectMap;
import es.upm.fi.dia.oeg.rmlc.api.model.R2RMLVocabulary;
import es.upm.fi.dia.oeg.rmlc.api.model.Template;
import org.apache.commons.rdf.api.*;

import java.util.*;

/**
 * An implementation of an ObjectMap.
 *
 * @author Marius Strandhaug
 */
public class ObjectMapImpl extends TermMapImpl implements ObjectMap {

    private List<IRI> validTermTypes = Arrays.asList(
            getRDF().createIRI(R2RMLVocabulary.TERM_IRI),
            getRDF().createIRI(R2RMLVocabulary.TERM_BLANK_NODE),
            getRDF().createIRI(R2RMLVocabulary.TERM_LITERAL));

    /**
     * dataType IRI for literals
     */
    private IRI dataType;

    private String function;

    /**
     * language tag when datatype rdf:langString
     */
    private String langTag;


    ObjectMapImpl(RDF c, Template template) {
		super(c, template);
	}

	ObjectMapImpl(RDF c, String columnName) {
		super(c, columnName);
	}

    ObjectMapImpl(RDF c, RDFTerm constant) {
        super(c, constant);
    }

    ObjectMapImpl(RDF c, ArrayList<RDFTerm> columns){
    	super(c,columns);
	}

    @Override
    public void setTermType(IRI typeIRI) {

        super.setTermType(typeIRI);

        if (!typeIRI.equals(getRDF().createIRI(R2RMLVocabulary.TERM_LITERAL))) {
            removeLanguageTag();
            removeDatatype();
        }
    }

    @Override
	public void setFunction(String function){
		for(int i=0 ; i< columns.size();i++){
			function = function.replace("columns["+i+"]",((Literal)columns.get(i)).getLexicalForm());
		}
		this.function = function;
	}

    @Override
    public List<IRI> getValidTermTypes() {
        return validTermTypes;
    }

    @Override
	public void setDefaultTermType() {
		/*
		 * An object map's default term termMapType is Literal if it's column valued,
		 * has a language tag, or if it's data typed.
		 */
		if (termMapType == TermMapType.COLUMN_VALUED || langTag != null || dataType != null || termMapType == TermMapType.COLUMNS_VALUED) {
            termTypeIRI = getRDF().createIRI(R2RMLVocabulary.TERM_LITERAL);
		} else {
            termTypeIRI = getRDF().createIRI(R2RMLVocabulary.TERM_IRI);
		}
	}

	@Override
	public void setLanguageTag(String lang) {
        if (termTypeIRI.equals(getRDF().createIRI(R2RMLVocabulary.TERM_LITERAL))) {
			removeDatatype();
			langTag = lang;
		} else {
			throw new IllegalStateException("The term termMapType is " + termTypeIRI
					+ ". Should be " + R2RMLVocabulary.TERM_LITERAL + ".");
		}
	}

	@Override
	public void setDatatype(IRI datatypeURI) {
        if (termTypeIRI.equals(getRDF().createIRI(R2RMLVocabulary.TERM_LITERAL))) {
			dataType = datatypeURI;
			removeLanguageTag();
		} else {
			throw new IllegalStateException("The term termMapType is " + termTypeIRI
					+ ". Should be " + R2RMLVocabulary.TERM_LITERAL + ".");
		}
	}

	@Override
	public String getLanguageTag() {
		return langTag;
	}

	@Override
	public IRI getDatatype() {
		return dataType;
	}

	@Override
	public void removeDatatype() {
		dataType = null;
	}

	@Override
	public String getFunction () { return function;}

	@Override
	public void removeLanguageTag() {
		langTag = null;
	}

	@Override
	public Set<Triple> serialize() {
		Set<Triple> stmtSet = new HashSet<>();

		stmtSet.addAll(super.serialize());

        stmtSet.add(getRDF().createTriple(getNode(),
                getRDF().createIRI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
                getRDF().createIRI(R2RMLVocabulary.TYPE_OBJECT_MAP)));

		if (dataType != null) {
            stmtSet.add(getRDF().createTriple(getNode(), getRDF().createIRI(R2RMLVocabulary.PROP_DATATYPE), dataType));
		}

		if (langTag != null) {
            stmtSet.add(getRDF().createTriple(getNode(), getRDF().createIRI(R2RMLVocabulary.PROP_LANGUAGE),
                    getRDF().createLiteral(langTag)));
		}

		return stmtSet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + ((langTag == null) ? 0 : langTag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (!super.equals(obj))
			return false;

		if (!(obj instanceof ObjectMapImpl))
			return false;

		ObjectMapImpl other = (ObjectMapImpl) obj;
		if (dataType == null) {
			if (other.dataType != null) {
				return false;
			}
		} else if (!dataType.equals(other.dataType)) {
			return false;
		}

		if (langTag == null) {
			if (other.langTag != null) {
				return false;
			}
		} else if (!langTag.equals(other.langTag)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "ObjectMapImpl [langTag=" + langTag + ", dataType=" + dataType
				+ ", termMapType=" + termMapType + ", termTypeIRI=" + termTypeIRI + ", template="
				+ template + ", constVal=" + constVal + ", columnName="
				+ columnName + ", inverseExp=" + inverseExp + ", node=" + getNode()
				+ "]";
	}

}
