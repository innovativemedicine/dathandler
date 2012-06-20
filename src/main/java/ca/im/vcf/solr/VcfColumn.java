package ca.im.vcf.solr;

public enum VcfColumn
{
    
    CHROM("CHROM_s", 0, VcfDataLineParser.SINGLE_VALUED), POS("POS_i", 1,
            VcfDataLineParser.SINGLE_VALUED), VID("VID_s", 2,
            VcfDataLineParser.SINGLE_VALUED), REF("REF_s", 3,
            VcfDataLineParser.SINGLE_VALUED), ALT("ALT_multi_s", 4,
            !VcfDataLineParser.SINGLE_VALUED), QUAL("QUAL_i", 5,
            VcfDataLineParser.SINGLE_VALUED), FILTER("FILTER_s", 6,
            VcfDataLineParser.SINGLE_VALUED), INFO("INFO_x", 7,
            !VcfDataLineParser.SINGLE_VALUED), FORMAT("FORMAT_x", 8,
            !VcfDataLineParser.SINGLE_VALUED), SAMPLE("SAMPLE_s",9, !VcfDataLineParser.SINGLE_VALUED);
    
    private final String solrKey_;
    private final int colIndex_;
    private final boolean singleValued_;
    
    VcfColumn(String solrKey, int index, boolean singledValued)
    {
        solrKey_ = solrKey;
        colIndex_ = index;
        singleValued_ = singledValued;
    }
    
    public String getSolrKey()
    {
        return solrKey_;
    }
    
    public int getColIndex()
    {
        return colIndex_;
    }
    
    public boolean isSingleValued()
    {
        return singleValued_;
    }
    
 
}
