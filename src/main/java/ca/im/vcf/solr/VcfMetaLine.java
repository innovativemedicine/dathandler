package ca.im.vcf.solr;

import java.util.HashMap;
import java.util.Map;

class VcfMetaLine
{
    public final static String INFO = "INFO";
    public final static String FORMAT = "FORMAT";
    public final static String IGNORED = "IGNORED";
    public final static boolean RANGE = true;

    public final static String NO_META_TYPE = null;
   public static Map<String, VcfSolrField> SOLR_FIELD_BY_CODE = new HashMap<String, VcfSolrField>();
   public static Map<Integer, VcfSolrField> SOLR_FIELD_BY_COL_INDEX = new HashMap<Integer, VcfSolrField>();
    
    String type;
    String metaType;
    String code;
    String desc;
    String occurrences;
    Map<String, String> fMap = new HashMap<String, String>();
    String solrFieldName;
    private String multipleSolrFieldName;
    
    public VcfMetaLine(String line)
    {
        
        if (line.startsWith("##INFO"))
        {
            metaType = INFO;
        }
        else if (line.startsWith("##FORMAT"))
        {
            metaType = FORMAT;
        }
        else
        {
            metaType = IGNORED;
        }
        
        if (!IGNORED.equals(metaType))
        {
            
            line = line.replaceAll(".*<", "").replaceAll(">.*$", "");
            
            String[] fields = line.split(",");
            for (String string : fields)
            {
                
                String[] kv = string.split("=");
                if (kv.length == 2)
                {
                    String k = kv[0];
                    String v = kv[1];
                    if ("ID".equals(k))
                        code = v;
                    else if ("Type".equals(k))
                    {
                        
                        type = v;
                        solrFieldName = metaType + "_" + code + "_"+"INDEX"+"_";
                        if ("String".equals(v))
                            solrFieldName += "s";
                        else if ("Integer".equals(v))
                            solrFieldName += "i";
                        else if ("Float".equals(v))
                            solrFieldName += "f";
                        else if ("Flag".equals(v))
                            solrFieldName += "s";
                        else
                            throw new RuntimeException("Unhandled type: " + v);
                        
                        multipleSolrFieldName = solrFieldName;
                        solrFieldName = solrFieldName.replaceAll("_INDEX","");
                        
                    }
                    else if ("Description".equals(k))
                        desc = v;
                    else if ("Number".equals(k))
                        occurrences = v;
                    else
                    {
                        // ignore
                    }
                    
                    fMap.put(k, v);
                }
                else
                {
                    // ignored
                }
            }
        }
        
    }
    
    public String getDesc()
    {
        return desc;
    }
    
   
    
    public String getType()
    {
        return type;
    }
    
    public String getMetaType()
    {
        return metaType;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public String getOccurrences()
    {
        return occurrences;
    }
    
    public Map<String, String> getfMap()
    {
        return fMap;
    }
    
    public String getSolrFieldName()
    {
        return solrFieldName;
    }
    
    public String getMultipleSolrFieldName(int i)
    {
        return multipleSolrFieldName.replaceAll("INDEX", i +"");
    }
}
