package com.textocat.textokit.examples;

import com.textocat.textokit.morph.fs.Word;
import com.textocat.textokit.postagger.MorphCasUtils;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

/**
 * @author Rinat Gareev
 */
public class WordPosLemmaWriter extends JCasAnnotator_ImplBase {
    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        for(Word w : JCasUtil.select(jCas, Word.class)) {
            String src = w.getCoveredText();
            String lemma = MorphCasUtils.getFirstLemma(w);
            String posTag = MorphCasUtils.getFirstPosTag(w);
            System.out.print(String.format("%s/%s/%s ", src, lemma, posTag));
        }
        // mark the end of a document
        System.out.println("\n");
    }
}
