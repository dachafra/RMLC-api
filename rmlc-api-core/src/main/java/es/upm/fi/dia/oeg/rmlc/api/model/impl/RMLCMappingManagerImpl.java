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

import java.util.Collection;

import es.upm.fi.dia.oeg.rmlc.api.MappingFactory;
import es.upm.fi.dia.oeg.rmlc.api.RMLCMappingManager;
import es.upm.fi.dia.oeg.rmlc.api.model.RMLCMappingCollection;
import es.upm.fi.dia.oeg.rmlc.api.model.TriplesMap;
import org.apache.commons.rdf.api.Graph;
import org.apache.commons.rdf.api.RDF;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of the {@link RMLCMappingManager} interface.
 *
 * @author michael.schmidt
 */
public class RMLCMappingManagerImpl implements RMLCMappingManager {

    private final RDF rdf;

    private MappingFactory mf;

    public RMLCMappingManagerImpl(RDF rdf) {
        this.rdf = rdf;
        mf = new MappingFactoryImpl(rdf);
    }

    protected RDF getRDF() {
        return rdf;
    }

    @Override
    public MappingFactory getMappingFactory() {
        return mf;
    }

    @Override
    public Collection<TriplesMap> importMappings(Graph graph)
            throws IllegalArgumentException, InvalidRMLCMappingException {

        // try once to extract the mapping from the graph and do some basic
        // validations
        // e.g. whether are any triplesmap at all, whether those have a
        // mandatory logical table as well as a subjectmap
        RMLCMappingCollection mc = extractR2RMLMapping(graph);

        if (mc == null || mc.getTriplesMaps().isEmpty())
            throw new IllegalArgumentException("Does not contain any (valid) TriplesMaps");
        for (TriplesMap map : mc.getTriplesMaps()) {
            requireNonNull(map.getLogicalSource(),  () -> "No logical table for TriplesMap " + map.getNode().toString());
            if (map.getLogicalSource().getNode() == null && map.getLogicalSource().getSQLQuery() == null)
                throw new IllegalArgumentException("No logical table for TriplesMap " + map.getNode().toString());
            requireNonNull(map.getSubjectMap(), () -> map.getNode().toString() + " does not have any SubjectMap");
        }

        return mc.getTriplesMaps();
    }

    public Graph exportMappings(Collection<TriplesMap> maps) {

        requireNonNull(maps, "The mapping collection is null.");

        if (maps.isEmpty())
            throw new IllegalArgumentException("The mapping collection is empty");

        Graph m = rdf.createGraph();

        for (TriplesMap tm : maps) {
            tm.serialize().forEach(m::add);
        }

        return m;
    }

    /**
     * Extracts the R2RML mapping from an RDF graph.
     *
     * @param graph the RDF graph defining the R2RML mapping
     * @return the list of triples map objects defined in the graph
     * @throws InvalidRMLCMappingException if invalid mapping found
     */
    protected RMLCMappingCollection extractR2RMLMapping(Graph graph)
            throws InvalidRMLCMappingException {
        return new RMLCMappingCollectionImpl(this, rdf, graph);
    }
}
