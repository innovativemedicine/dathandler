package ca.im.vcf.solr;

import org.apache.solr.handler.ContentStreamHandlerBase;
import org.apache.solr.handler.ContentStreamLoader;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */

public class VcfRequestHandler extends ContentStreamHandlerBase
{
    public static Logger log = LoggerFactory
            .getLogger(VcfRequestHandler.class);
    
    @Override
    protected ContentStreamLoader newLoader(SolrQueryRequest req,
            UpdateRequestProcessor processor)
    {
        return new VcfLoader(req, processor);
    }
    
    // ////////////////////// SolrInfoMBeans methods //////////////////////
    @Override
    public String getDescription()
    {
        return "Add/Update multiple documents with VCF formatted rows";
    }
    
    @Override
    public String getSource()
    {
        return "$URL$";
    }
    
    @Override
    public String getSourceId()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getVersion()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
