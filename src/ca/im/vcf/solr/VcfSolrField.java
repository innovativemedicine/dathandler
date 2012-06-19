package ca.im.vcf.solr;

import static ca.im.vcf.solr.VcfMetaLine.FORMAT;
import static ca.im.vcf.solr.VcfMetaLine.INFO;
import static ca.im.vcf.solr.VcfMetaLine.NO_META_TYPE;
import static ca.im.vcf.solr.VcfMetaLine.*;
import ca.im.vcf.solr.colparser.ColParser;
import ca.im.vcf.solr.colparser.LikelihoodParser;
import ca.im.vcf.solr.colparser.StraightReadParser;
import ca.im.vcf.solr.colparser.StringPlusSizeParser;

public enum VcfSolrField
{
    
    CHROM(NO_META_TYPE,
            "s",
            "CHROM",
            0,
            new StraightReadParser()),
    POS(NO_META_TYPE,
            "ti",
            "POS",
            1,
            new StraightReadParser()),
    
    REF(NO_META_TYPE,
            "s",
            "REF",
            3,
            new StringPlusSizeParser()),
    ALT(NO_META_TYPE,
            "s",
            "ALT",
            4,
            new StringPlusSizeParser()),
    QUAL(NO_META_TYPE,
            "ti",
            "QUAL",
            5,
            new StraightReadParser()),
    FILTER(NO_META_TYPE,
            "s",
            "FILTER",
            6,
            new StraightReadParser()),
    
    FORMAT_GL(FORMAT,
            "tf",
            "GL",
            -1,
            new LikelihoodParser());
    
    private String metatype_;
    
    private String solrtype_;
    private String code_;
    
    private String key_;
    private int colIndex_;
    
    private ColParser colParser_;
    
    private VcfSolrField(String metatype, String solrtype, String code,
            int colIndex, ColParser colParser)
    {
        colParser.setField(this);
        colParser_ = colParser;
        metatype_ = metatype;
        
        
        solrtype_ = solrtype;
        code_ = code;
        
        colIndex_ = colIndex;
        if (metatype_ == NO_META_TYPE)
        {
            key_ = code_ + "_" + solrtype;
        }
        else
        {
            key_ = metatype_ + "_" + code_ + "_" + solrtype;
        }
        
        SOLR_FIELD_BY_CODE.put(code, this);
        if (colIndex != -1)
        {
            SOLR_FIELD_BY_COL_INDEX.put(colIndex, this);
        }
    }
    
    public String getMetatype()
    {
        return metatype_;
    }
    
    public String getSolrtype()
    {
        return solrtype_;
    }
    
    public String getCode()
    {
        return code_;
    }
    
    public String getKey()
    {
        return key_;
    }
    
    public int getColIndex()
    {
        return colIndex_;
    }
    public ColParser getColparser()
    {
        return colParser_;
    }
}
