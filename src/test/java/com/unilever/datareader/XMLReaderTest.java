package com.unilever.datareader;

import com.unilever.datareader.reader.XMLReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class
 */
public class XMLReaderTest {

    private XMLReader reader;

    @Before
    public void setUp() {
        reader = new XMLReader(XMLReaderTest.class.getClassLoader().getResourceAsStream("testData.xml"));
    }

    @Test
    @DisplayName("Validate sub folders under the specified folder")
    public void testSubFolderNamesScenario1() {

        List<String> subFolderNames = reader.getAllSubFolderNames("c");
        assertEquals(3, subFolderNames.size());
        assertEquals("programfiles", subFolderNames.get(0));
   }

    @Test
    @DisplayName("When the specified folder doesn't contain sub folders")
    public void testSubFolderNamesScenario2() {

        List<String> subFolderNames2 = reader.getAllSubFolderNames("user");
        assertEquals(0, subFolderNames2.size());
    }

    @Test(expected = IllegalArgumentException.class)
    @DisplayName("When no file is specified")
    public void testNoFileSpecified() {
        XMLReader xmlReader = new XMLReader(null);
        List<String> subFolderNames2 = xmlReader.getAllSubFolderNames("user");
    }
}
