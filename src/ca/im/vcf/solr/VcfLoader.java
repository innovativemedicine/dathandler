package ca.im.vcf.solr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.params.UpdateParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.handler.ContentStreamLoader;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.update.AddUpdateCommand;
import org.apache.solr.update.CommitUpdateCommand;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class VcfLoader extends ContentStreamLoader
{

    public static final String OVERWRITE = "overwrite";
   
    public static final boolean ALREADY_LOGGED = true;
    
    public static final boolean OPTIMIZE = true;
    
    public static Logger log = LoggerFactory.getLogger(VcfRequestHandler.class);
    
    final IndexSchema schema;
    final SolrParams params;
    
    final UpdateRequestProcessor processor;


    
    AddUpdateCommand templateAdd;
    
    Map<String, Map<String, VcfMetaLine>> metaMap_ = new HashMap<String, Map<String, VcfMetaLine>>();
    
    VcfDataLineParser parser_;
    
    VcfLoader(SolrQueryRequest req, UpdateRequestProcessor processor)
    {
        log.error("VcfLoader !!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        
        metaMap_.put(VcfMetaLine.FORMAT, new HashMap<String, VcfMetaLine>());
        metaMap_.put(VcfMetaLine.INFO, new HashMap<String, VcfMetaLine>());
        
        this.processor = processor;
        this.params = req.getParams();
        schema = req.getSchema();
        
        templateAdd = new AddUpdateCommand();
        templateAdd.overwriteCommitted = params.getBool(OVERWRITE, true);
        templateAdd.commitWithin = params
                .getInt(UpdateParams.COMMIT_WITHIN, -1);
        
    }
    
    private void commit(boolean optimize) throws IOException
    {
        CommitUpdateCommand cmd = new CommitUpdateCommand( optimize );
        // cmd.waitFlush    = params.getBool( UpdateParams.WAIT_FLUSH,    cmd.waitFlush    );
        // cmd.waitSearcher = params.getBool( UpdateParams.WAIT_SEARCHER, cmd.waitSearcher );
        // cmd.expungeDeletes = params.getBool( UpdateParams.EXPUNGE_DELETES, cmd.expungeDeletes);      
        // cmd.maxOptimizeSegments = params.getInt(UpdateParams.MAX_OPTIMIZE_SEGMENTS, cmd.maxOptimizeSegments);
  
        processor.processCommit(cmd);
        
    }
    
    /** load the CSV input */
    @Override
    public void load(SolrQueryRequest req, SolrQueryResponse rsp,
            ContentStream stream) throws IOException
    {
        
        BufferedReader bReader = null;
        try
        {
            Reader reader = stream.getReader();
            if (!(reader instanceof BufferedReader))
            {
                bReader = new BufferedReader(reader);
            }
            String raw = null;
            int lineNum = 0;
            int samplesIndexed = 0;
            int samplesProcessed = 0;
            long timeMillis = System.currentTimeMillis();
            int interimCount = 0;
            
            while ((raw = bReader.readLine()) != null)
            {
                String[] cols = raw.split("\t");
                if (raw.startsWith("##"))
                {
                    VcfMetaLine mi = new VcfMetaLine(raw);
                    if (!VcfMetaLine.IGNORED.equals(mi.getMetaType()))
                    {
                        metaMap_.get(mi.getMetaType()).put(mi.getCode(), mi);
                    }
                }
                else if (raw.startsWith("#"))
                {
                    parser_ = new VcfDataLineParser(cols, metaMap_);
                }
                else
                {
                    
                    List<VcfSample> samples = parser_.parse(cols);
                    int colIndex = 0;
                    for (VcfSample vcfSample : samples)
                    {
                        if (vcfSample.isMutated())
                        {
                            addDoc(colIndex, lineNum, samplesIndexed, vcfSample.getSolrKeyValues());
                            samplesIndexed++;
                        }
                        colIndex++;
                    
                        samplesProcessed++;
                        interimCount++;
                        
                      
                        
                    }

                 
                  
                  
                }
              
                if (lineNum%100 ==0)
                {
                    long currTimeMillis = System.currentTimeMillis();
                    long elapsedTimeSecs = (currTimeMillis - timeMillis)/1000;
                
                    log.info("Line # " + lineNum + "   Samples Indexed:" + samplesIndexed + "   Samples Processed: " + samplesProcessed + "   Interim Count: " + interimCount + "   seconds: " + elapsedTimeSecs);

                    interimCount = 0;
                    timeMillis = System.currentTimeMillis();
                }
                
                if (lineNum > 0 && lineNum%1000 ==0)
                {
                  commit(!OPTIMIZE);
                    
                }
                
                lineNum++;
            }
            commit(OPTIMIZE);
            log.error("\n\nVCF load complete!!!!!!!!!!!!!\n\n");
        }
        catch (Throwable t)
        {
            String err = "Error loading VCF " + t;
            log.error(err, t);
            throw new SolrException(ErrorCode.BAD_REQUEST, err, t, ALREADY_LOGGED);
        }
        finally
        {
            if (bReader != null)
            {
                IOUtils.closeQuietly(bReader);
            }
        }
    }
    
    void addDoc(int colIndex, int line,int sampleNum, Map<String, String> valuesBySolrKey) throws IOException
    {
        templateAdd.clear();
        SolrInputDocument doc = new SolrInputDocument();
        
        int cohort = colIndex%5;
        
        doc.addField("cohort_s", "cohort_" + cohort);
        
     
        
        doc.addField("id", "variant" + sampleNum , 1.0f);
        
        
        for (String solrKey : valuesBySolrKey.keySet())
        {
            String value = valuesBySolrKey.get(solrKey);
            if (".".equals(value))
            {
                value = "0";
            }
            doc.addField(solrKey, value, 1.0f);
            
            
            
        }
        
 
        
        templateAdd.solrDoc = doc;
        processor.processAdd(templateAdd);
        
       
    }
    
}