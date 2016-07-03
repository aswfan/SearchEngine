package SearchEngine;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
	
	private IndexWriter writer = null;
	
	public void createIndex(String strs, String docDir){
		
		try{
			writer = this.configWriter(docDir);
			Document doc = new Document();
			
			Field content = new TextField("content", strs, Field.Store.YES);
			doc.add(content);
			writer.addDocument(doc);
			writer.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean updateIndex(String docDir) throws Exception{
		if(writer == null){
			writer = this.configWriter(docDir);
		}
//		writer.updateDocument(new Term("path", file.getPath()), doc); 
		return true;
	}
	
	public IndexWriter configWriter(String docDir) throws Exception{
		Directory directory = FSDirectory.open(new File(docDir));
		Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_CURRENT);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		IndexWriter writer = new IndexWriter(directory, iwc);
		return writer;
	}

}
