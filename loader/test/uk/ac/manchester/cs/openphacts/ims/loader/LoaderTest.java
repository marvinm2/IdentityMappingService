// OpenPHACTS RDF Validator,
// A tool for validating and storing RDF.
//
// Copyright 2012-2013  Christian Y. A. Brenninkmeijer
// Copyright 2012-2013  University of Manchester
// Copyright 2012-2013  OpenPhacts
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package uk.ac.manchester.cs.openphacts.ims.loader;

import java.io.File;
import java.util.List;
import org.bridgedb.rdf.constants.DCTermsConstants;
import org.bridgedb.sql.SQLUriMapper;
import org.bridgedb.sql.TestSqlFactory;
import org.bridgedb.statistics.MappingSetInfo;
import org.bridgedb.utils.BridgeDBException;
import org.bridgedb.utils.ConfigReader;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import uk.ac.manchester.cs.datadesc.validator.rdftools.RdfFactory;
import uk.ac.manchester.cs.datadesc.validator.rdftools.RdfReader;
import uk.ac.manchester.cs.datadesc.validator.rdftools.Reporter;
import uk.ac.manchester.cs.datadesc.validator.rdftools.VoidValidatorException;

/**
 *
 * @author Christian
 */
public class LoaderTest {
    
    static SQLUriMapper uriListener;
    static RdfReader reader;
             
    public LoaderTest() {
        
    }
    
    @BeforeClass
    public static void setUpClass() throws BridgeDBException, VoidValidatorException {
        TestSqlFactory.checkSQLAccess();
        ConfigReader.useTest();
        //TestSqlFactory.checkSQLAccess();
        uriListener = SQLUriMapper.createNew();
        reader = RdfFactory.getTestFilebase();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of load method, of class Loader.
     */
    @Test
    public void testLoadFile() throws Exception {
        Resource context;
        List<Statement> statements;
        Reporter.println("loadFile");
        File file  = new File("test-data/parentVoid.ttl");
        reader.loadFile(file);
        //context = new URIImpl(file.toURI().toString());
        //statements = reader.getStatementList(null, null,  null, context);
        //ystem.out.println("statements.size() = " + statements.size());
        //for (Statement statement:statements){
            //ystem.out.println(statement);
        //}
        file  = new File("test-data/grandParentVoid.ttl");
        reader.loadFile(file);
        //context = new URIImpl(file.toURI().toString());
        //statements = reader.getStatementList(null, null,  null, context);
        //ystem.out.println("statements.size() = " + statements.size());
        //for (Statement statement:statements){
            //ystem.out.println(statement);
        //}
        file  = new File("test-data/cw-cs.ttl");
        context = new URIImpl(file.toURI().toString());
        String formatName = null;
        int result = LinksetLoader.load(file, formatName);
        MappingSetInfo mapping = uriListener.getMappingSetInfo(result);
        int numberOfLinks = mapping.getNumberOfLinks();
        assertThat(numberOfLinks, greaterThanOrEqualTo(3));
        statements = reader.getStatementList(null, null,  null, context);
        //ystem.out.println("statements.size() = " + statements.size());
        //for (Statement statement:statements){
            //ystem.out.println(statement);
        //}
        assertThat(statements.size(), greaterThanOrEqualTo(3));
        assertTrue(findPredicate(DCTermsConstants.LICENSE_URI, statements));
        statements = reader.getStatementList(new URIImpl(mapping.getMappingSource()));
        assertThat(statements.size(), greaterThanOrEqualTo(3));
        //assertTrue(findPredicate(DCTermsConstants.LICENSE_URI, statements));
    }

    private boolean findPredicate(URI predicate, List<Statement> statements){
        for (Statement statement:statements){
            if (predicate.equals(statement.getPredicate())){
                return true;
            }
        }
        return false;
    }
    
    
}
