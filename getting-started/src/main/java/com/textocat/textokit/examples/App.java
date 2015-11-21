package com.textocat.textokit.examples;

import com.textocat.textokit.commons.cpe.FileDirectoryCollectionReader;
import com.textocat.textokit.commons.util.PipelineDescriptorUtils;
import com.textocat.textokit.morph.dictionary.MorphDictionaryAPIFactory;
import com.textocat.textokit.morph.lemmatizer.LemmatizerAPI;
import com.textocat.textokit.postagger.PosTaggerAPI;
import com.textocat.textokit.segmentation.SentenceSplitterAPI;
import com.textocat.textokit.tokenizer.TokenizerAPI;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ExternalResourceDescription;

import java.io.File;
import java.io.IOException;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

/**
 * @author Rinat Gareev
 */
public class App {
    public static void main(String[] args) throws UIMAException, IOException {
        if (args.length != 1) {
            System.err.println("Usage: <input-dir>");
            System.exit(1);
        }
        File inputDir = new File(args[0]);

        CollectionReaderDescription readerDesc = FileDirectoryCollectionReader.createDescription(inputDir);

        AnalysisEngineDescription aeDesc = createEngineDescription(
                createEngineDescription(TokenizerAPI.AE_TOKENIZER),
                createEngineDescription(SentenceSplitterAPI.AE_SENTENCE_SPLITTER),
                createEngineDescription(PosTaggerAPI.AE_POSTAGGER),
                createEngineDescription(LemmatizerAPI.AE_LEMMATIZER)
        );
        ExternalResourceDescription morphDictDesc =
                MorphDictionaryAPIFactory.getMorphDictionaryAPI().getResourceDescriptionForCachedInstance();
        morphDictDesc.setName(PosTaggerAPI.MORPH_DICTIONARY_RESOURCE_NAME);
        PipelineDescriptorUtils.getResourceManagerConfiguration(aeDesc)
                .addExternalResource(morphDictDesc);

        AnalysisEngineDescription writerDesc = createEngineDescription(WordPosLemmaWriter.class);

        SimplePipeline.runPipeline(readerDesc, aeDesc, writerDesc);
    }
}
