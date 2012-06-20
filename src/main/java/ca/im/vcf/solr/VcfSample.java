package ca.im.vcf.solr;

import java.util.Map;

public class VcfSample
{
    boolean mutated_;
    
    Map<String, String> solrKeyValues_;

    public VcfSample(boolean hasMutation, Map<String, String> solrKeyValues)
    {
        super();
        mutated_ = hasMutation;
        solrKeyValues_ = solrKeyValues;
    }

    public boolean isMutated()
    {
        return mutated_;
    }

    public Map<String, String> getSolrKeyValues()
    {
        return solrKeyValues_;
    }
    
    
    
    
}
