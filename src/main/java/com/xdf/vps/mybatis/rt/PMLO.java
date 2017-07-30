package com.xdf.vps.mybatis.rt;

public class PMLO {
	
    public static final PMLO EQUAL = new PMLO("=");
    public static final PMLO GREAT = new PMLO(">");
    public static final PMLO LESS = new PMLO("<");
    public static final PMLO LESS_EQUAL = new PMLO("<=");
    public static final PMLO GREAT_EQUAL = new PMLO(">=");
    public static final PMLO CONTAIN = new PMLO("LIKE");
    public static final PMLO NOT_EQUAL = new PMLO("!=");
    public static final PMLO IS_NULL = new PMLO("IS NULL", true);
    public static final PMLO IS_NOT_NULL = new PMLO("IS NOT NULL", true);
    
    private boolean single;
    
    private String operatorName;
    
    public PMLO()
    {
        this(EQUAL.getOperatorName(), EQUAL.isSingle());
    }

    public PMLO(String operationName)
    {
        this(operationName, false);
    }

    protected PMLO(String operatorName, boolean single)
    {
        this.operatorName = operatorName;
        this.single = single;
    }

    public String getOperatorName()
    {
        return operatorName;
    }

    public String toString()
    {
        return operatorName;
    }

    public boolean isSingle()
    {
        return single;
    }

}
