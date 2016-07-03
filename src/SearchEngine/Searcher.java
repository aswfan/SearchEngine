package SearchEngine;

import java.io.File;
import java.io.IOException;

import javax.swing.text.Highlighter.Highlight;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {
	
	private IndexSearcher searcher = null;
	private Query query = null;
	
	public TopDocs search(String key, String indexDir){
		try{
			FSDirectory dir = FSDirectory.open(new File(indexDir));
			IndexReader reader = DirectoryReader.open(dir);
			searcher = new IndexSearcher(reader);
			QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "content", new SmartChineseAnalyzer(Version.LUCENE_CURRENT));
			query = parser.parse(key);
			TopDocs topDocs = searcher.search(query, 10);
			return topDocs;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	public String addHighLight(TopDocs topDocs) throws Exception{
		if(searcher == null|| query == null){
			return null;
		}
		StringBuilder strs = new StringBuilder();
		ScoreDoc[] hits = topDocs.scoreDocs;
		for(ScoreDoc hit : hits){
			Document doc = searcher.doc(hit.doc);
			SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<font color='red'","</font>");
			Highlighter highlighter = new Highlighter(formatter, new QueryScorer(query));
			Fragmenter fragmenter = new SimpleFragmenter(80);
			highlighter.setTextFragmenter(fragmenter);
			String result = highlighter.getBestFragment(new SmartChineseAnalyzer(Version.LUCENE_CURRENT), "content", doc.get("content"));
			strs.append(result);
		}
		return strs.toString();
	}

}
