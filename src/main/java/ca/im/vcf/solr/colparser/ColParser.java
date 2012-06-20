package ca.im.vcf.solr.colparser;

import java.util.Map;

import ca.im.vcf.solr.VcfSolrField;

public interface ColParser
{
    public void parseAndPutData(Map<String, String> valueBySolrKey, String...cols);
    
    public void setField(VcfSolrField field);
}
