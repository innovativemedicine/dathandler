package ca.im.vcf.solr.colparser;

import java.util.Map;

import ca.im.vcf.solr.VcfSolrField;

public class LikelihoodParser extends AbstractColParser
{
    public void parseAndPutData(Map<String, String> valueBySolrKey,
            String... cols)
    {
        String s = cols[0];
        s = s.split(":")[2];
        String[] values = s.split(",");
        for (int i = 0; i < values.length; i++)
        {
            String subvalue = values[i];

            VcfSolrField f = getField();
            String indexedKey = f.getMetatype() + "_" + f.getCode()  +"_" + i+"_tf";
            valueBySolrKey.put(indexedKey, subvalue);
        }
        
    }
    
}
