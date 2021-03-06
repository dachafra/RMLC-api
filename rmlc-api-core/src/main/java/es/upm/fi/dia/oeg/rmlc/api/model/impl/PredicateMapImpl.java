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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.upm.fi.dia.oeg.rmlc.api.model.PredicateMap;
import es.upm.fi.dia.oeg.rmlc.api.model.R2RMLVocabulary;
import es.upm.fi.dia.oeg.rmlc.api.model.Template;
import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.api.RDF;
import org.apache.commons.rdf.api.RDFTerm;
import org.apache.commons.rdf.api.Triple;

/**
 * An implementation of a PredicateMap.
 * 
 * @author Marius Strandhaug
 */
public class PredicateMapImpl extends TermMapImpl implements PredicateMap {

    private List<IRI> validTermTypes = Arrays.asList(getRDF().createIRI(R2RMLVocabulary.TERM_IRI));

	PredicateMapImpl(RDF rdf, Template template) {
		super(rdf, template);
	}

	PredicateMapImpl(RDF rdf, String columnName) {
		super(rdf, columnName);
	}

    PredicateMapImpl(RDF rdf, RDFTerm constant) {
        super(rdf, constant);
    }

	@Override
	public Set<Triple> serialize() {
		Set<Triple> stmtSet = new HashSet<>();

		stmtSet.addAll(super.serialize());

        stmtSet.add(getRDF().createTriple(getNode(),
                getRDF().createIRI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
                getRDF().createIRI(R2RMLVocabulary.TYPE_PREDICATE_MAP)));

		return stmtSet;
	}

	@Override
	public String toString() {
		return "PredicateMapImpl [termMapType=" + termMapType + ", termTypeIRI=" + termTypeIRI
				+ ", template=" + template + ", constVal=" + constVal
				+ ", columnName=" + columnName + ", inverseExp=" + inverseExp
				+ ", node=" + getNode() + "]";
	}

    @Override
    public List<IRI> getValidTermTypes() {
        return validTermTypes;
    }
}
