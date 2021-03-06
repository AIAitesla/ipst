/**
 * Copyright (c) 2016, All partners of the iTesla project (http://www.itesla-project.eu/consortium)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package eu.itesla_project.modules.securityindexes;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Geoffroy Jamgotchian <geoffroy.jamgotchian at rte-france.com>
 */
public class TsoFrequencySecurityIndexTest {

    public TsoFrequencySecurityIndexTest() {
    }

    @Test
    public void testXml() throws IOException, XMLStreamException{
        String xml = "<?xml version=\"1.0\" ?><index name=\"tso-frequency\"><freq-out-count>1</freq-out-count></index>";
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        TsoFrequencySecurityIndex index;
        try (Reader reader = new StringReader(xml)) {
            XMLStreamReader xmlReader = xmlif.createXMLStreamReader(reader);
            try {
                index = TsoFrequencySecurityIndex.fromXml("c1", xmlReader);
            } finally {
                xmlReader.close();
            }
        }
        assertTrue(index.getFreqOutCount() == 1);
        assertEquals(xml, index.toXml());
    }

}
