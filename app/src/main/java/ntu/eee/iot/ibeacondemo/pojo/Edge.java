package ntu.eee.iot.ibeacondemo.pojo;


/**
 * Created by zhujianjie on 2015/9/20.
 */
public class Edge
{
    public String ID;
    public String ID_From;
    public String ID_To;

    public Edge(String from, String to)
    {
        this.ID = from+to;
        this.ID_From = from;
        this.ID_To = to;
    }

    public Edge()
    {

    }

    public String toString()
    {
        return  ID+"  "+ID_From+"  "+ID_To;
    }
}
