/*******************************************************************************
 * Copyright (c) 2010, 2012 Institute for Dutch Lexicology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package nl.inl.blacklab.indexers.alto;

import java.io.File;
import java.util.Properties;

import nl.inl.blacklab.index.Indexer;
import nl.inl.util.LogUtil;
import nl.inl.util.PropertiesUtil;

/**
 * The indexer class and main program for the ANW corpus.
 */
public class IndexAlto {
	/**
	 * If true, always wipes existing index. If false, appends to existing index.
	 * @param args commandline arguments
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("IndexAlto\n");
		if (args.length < 1) {
			System.out
					.println("Usage:\n"
							+ "  java nl.inl.blacklab.test.IndexTei <propfile> [<file_to_process>] [create]\n"
							+ "(see docs for more information)");
			return;
		}
		File propFile = new File(args[0]);
		File baseDir = propFile.getParentFile();
		boolean createNewIndex = args[args.length - 1].equals("create");

		LogUtil.initLog4jBasic();

		// Do we wish to index a single input file?
		String whichFile = null;
		if (args.length == 2 && !args[1].equals("create"))
			whichFile = args[1]; // yes

		// Read property file
		Properties properties = PropertiesUtil.readFromFile(propFile);

		// Metadata is in separate file for EDBO set
		// TODO: generalize this so we don't need this special case anymore
		AltoUtils.setMetadataFile(PropertiesUtil.getFileProp(properties, "metadataFile", null));

		// The indexer tool
		File indexDir = PropertiesUtil.getFileProp(properties, "indexDir", "index", baseDir);
		Indexer indexer = new Indexer(indexDir, createNewIndex, DocIndexerAlto.class);
		indexer.setContinueAfterInputError(true);
		try {
			// How many documents to process (0 = all of them)
			int maxDocs = PropertiesUtil.getIntProp(properties, "maxDocs", 0);
			if (maxDocs > 0)
				indexer.setMaxDocs(maxDocs);

			// Where the source files are
			File inputDir = PropertiesUtil.getFileProp(properties, "inputDir", "input", baseDir);

			// Index a directory
			File fileToIndex = inputDir;
			if (whichFile != null)
				fileToIndex = new File(inputDir, whichFile);
			indexer.index(fileToIndex);
		} catch (Exception e) {
			System.err.println("An error occurred, aborting indexing. Error details follow.");
			e.printStackTrace();
		} finally {
			// Finalize and close the index.
			indexer.close();
		}
	}

}
