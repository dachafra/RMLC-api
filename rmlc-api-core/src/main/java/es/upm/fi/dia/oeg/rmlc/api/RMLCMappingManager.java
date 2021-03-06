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
package es.upm.fi.dia.oeg.rmlc.api;

import java.util.Collection;

import es.upm.fi.dia.oeg.rmlc.api.model.TriplesMap;
import es.upm.fi.dia.oeg.rmlc.api.model.impl.InvalidRMLCMappingException;
import org.apache.commons.rdf.api.Graph;

/**
 * General-purpose R2RML mapping management functionality. The API provides
 * methods for storage, retrieval, access, and modification of mappings.
 * 
 * @author Marius Strandhaug
 */
public interface RMLCMappingManager {

	/**
	 * @return The MappingFactory for this R2RMLMappingManager.
	 */
	public MappingFactory getMappingFactory();

	/**
	 * Imports an RDF mapping from a graph. The graph must contain a valid R2RML
	 * mapping. The graph parameter must be an instance of the library's graph
	 * class.
	 * 
	 * @param graph
	 *            The graph object containing R2RML mappings.
	 */
	public Collection<TriplesMap> importMappings(Graph graph)
			throws InvalidRMLCMappingException;

	/**
	 * Serializes the TriplesMaps and stores the RDF triples in a graph.
	 * 
	 * @param maps
	 *            A collection of mappings
	 */
	public Graph exportMappings(Collection<TriplesMap> maps);
}
