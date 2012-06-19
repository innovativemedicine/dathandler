package ca.im.vcf.solr.colparser;

import java.util.Map;



public class StraightReadParser  extends AbstractColParser
{
   
    public void parseAndPutData(Map<String, String> valueBySolrKey, String... cols)
    {
       
        String values = cols[getField().getColIndex()];
        String v = values.split(",")[0];
       
        valueBySolrKey.put(getField().getKey(), v);
    }
}
