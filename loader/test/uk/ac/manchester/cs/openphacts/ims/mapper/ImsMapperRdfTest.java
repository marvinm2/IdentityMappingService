// BridgeDb,
// An abstraction layer for identifier mapping services, both local and online.
//
// Copyright 2006-2009  BridgeDb developers
// Copyright 2012-2013  Christian Y. A. Brenninkmeijer
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
package uk.ac.manchester.cs.openphacts.ims.mapper;

import java.io.File;
import org.bridgedb.sql.TestSqlFactory;
import org.bridgedb.sql.transative.DirectMapping;
import org.bridgedb.utils.BridgeDBException;
import org.bridgedb.utils.ConfigReader;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import uk.ac.manchester.cs.openphacts.ims.loader.RunLoader;

/**
 * Runs the UriMapper interface tests over SQLUriMapper class
 * 
 * Creates the mapper, loads in the test data and then runs the tests.
 *
 * @author Christian
 */
public class ImsMapperRdfTest extends org.bridgedb.uri.UriMapperRdfTest{
    
    @BeforeClass
    public static void setupStatementMaker() throws BridgeDBException{
        statementMaker = new ImsStatementMaker();
    }
    
    private static void loadTestData() throws BridgeDBException {
        File file = new File("test-data/load.xml");
        java.net.URI uri = file.toURI();
        String[] loadArgs = new String[1];
        loadArgs[0] = uri.toString();
        RunLoader.main(loadArgs);
    }


    @BeforeClass
    public static void setupIDMapper() throws BridgeDBException{
        connectionOk = false;
        TestSqlFactory.checkSQLAccess();
        connectionOk = true;
        ConfigReader.useTest();
        loadTestData();
        uriMapper = ImsMapper.getExisting();
    }
            
    protected void checkDirect(DirectMapping directMapping) {
        if (!directMapping.hasMappingToSelf()){
            assertNotNull(directMapping.getMappingSource());
            assertNotNull(directMapping.getMappingResource());
        }
    }

}
