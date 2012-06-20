package ca.im.vcf.solr.colparser;

import ca.im.vcf.solr.VcfSolrField;

public abstract class AbstractColParser implements ColParser
{
    VcfSolrField field_;

    public VcfSolrField getField()
    {
        return field_;
    }

    public void setField(VcfSolrField field)
    {
        field_ = field;
    }
    
    
}
