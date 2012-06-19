package ca.im.vcf.solr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.im.vcf.solr.colparser.ColParser;

public class VcfDataLineParser
{
    public static Logger log = LoggerFactory.getLogger(VcfDataLineParser.class);
    public static final boolean SINGLE_VALUED = true;
    
    public final static int FIXED_FIELD_LENGTH = 9;
    
    Map<String, Map<String, VcfMetaLine>> metaMap_;
    String[] sampleIds_;
   
    
    public VcfDataLineParser(String[] sampleIds,
            Map<String, Map<String, VcfMetaLine>> metaMap)
    {
        sampleIds_ = sampleIds;
        metaMap_ = metaMap;
    }
    
    public List<VcfSample> parse(String[] fields)
    {
        List<VcfSample > samples = new ArrayList<VcfSample>();
        Map<String, String> commonSolrKeyValues = new HashMap<String, String>();
        
    
        
        for (VcfSolrField f: VcfSolrField.values())
        {
            if (f.getColIndex() != -1)
            {
                ColParser p = f.getColparser();
                p.parseAndPutData(commonSolrKeyValues, fields);
            }
            
        }
        
                
        
        
        
      
        for (int i = FIXED_FIELD_LENGTH; i < fields.length; i++)
        {
            Map<String, String> solrKeyValues = new HashMap<String, String>();
            solrKeyValues.putAll(commonSolrKeyValues);
            String sampleId = sampleIds_[i];
            
            solrKeyValues.put("SAMPLE_s", sampleId);
            String sampleField = fields[i];
  
            VcfSolrField.FORMAT_GL.getColparser().parseAndPutData(solrKeyValues, sampleField);
            
            VcfSample vcfSample = new VcfSample(true, solrKeyValues);
       
            samples.add(vcfSample);
        }
        
        return samples;
        
    }
    
}
