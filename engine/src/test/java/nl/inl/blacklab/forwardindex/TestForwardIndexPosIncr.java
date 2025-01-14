package nl.inl.blacklab.forwardindex;

import java.io.File;
import java.text.Collator;
import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import nl.inl.util.UtilsForTesting;

public class TestForwardIndexPosIncr {
    private AnnotationForwardIndex fi;

    // The tokens to add
    final String[][] str = { { "How", "much", "many", "lots", "wood" } };

    // The tokens' position increments: multiple tokens at one position
    // (only the first should be stored) and a gap (empty tokens should be added)
    final Integer[][] pi = { { 1, 1, 0, 0, 3 } };

    private void setUpForwardIndex() {
        // Remove any previously left over temp test dirs
        UtilsForTesting.removeBlackLabTestDirs();

        // Create new test dir
        File dir = UtilsForTesting.createBlackLabTestDir("ForwardIndexPosIncr");

        fi = AnnotationForwardIndex.open(dir, true, Collator.getInstance(), true, null, true);
        try {
            // Store strings
            for (int i = 0; i < str.length; i++) {
                Assert.assertEquals(i, fi.addDocument(Arrays.asList(str[i]), Arrays.asList(pi[i])));
            }
        } finally {
            fi.close(); // close so everything is guaranteed to be written
        }
        fi = AnnotationForwardIndex.open(dir, false, Collator.getInstance(), false, null, true);
    }

    @After
    public void tearDown() {
        if (fi != null)
            fi.close();
        // Try to remove (some files may be locked though)
        UtilsForTesting.removeBlackLabTestDirs();
    }

    public int[] retrievePart(int id, int start, int end) {
        return fi.retrievePartsInt(id, new int[] { start }, new int[] { end }).get(0);
    }

    @Test
    public void testRetrieve() {
        setUpForwardIndex();

        // Retrieve strings
        String[][] expected = { { "How", "much", "", "", "wood" } };
        for (int i = 0; i < str.length; i++) {
            int[] retrieved = retrievePart(i, -1, -1);
            for (int j = 0; j < retrieved.length; j++) {
                Assert.assertEquals(expected[i][j], fi.terms().get(retrieved[j]));
            }
        }
    }

}
