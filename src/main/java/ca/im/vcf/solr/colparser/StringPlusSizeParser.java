package ca.im.vcf.solr.colparser;

import java.util.Map;

import ca.im.vcf.solr.VcfSolrField;

public class StringPlusSizeParser extends StraightReadParser
{

    @Override
    public void parseAndPutData(Map<String, String> valueBySolrKey,
            String... cols)
    {
        super.parseAndPutData(valueBySolrKey, cols);
        
        
     
            String values = cols[getField().getColIndex()];
            String v = values.split(",")[0];
            VcfSolrField f = getField();
            String sizeKey =  f.getCode() + "_size_ti";
            if (v.length() == 0)
            {
                System.out.println(v);
            }
            valueBySolrKey.put(sizeKey, v.length()+"");
        
        
    }
    
}
