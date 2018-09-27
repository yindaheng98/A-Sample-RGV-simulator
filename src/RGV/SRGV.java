package RGV;


public class SRGV extends RGV
{
    public SRGV(RGVConfig rc)
    {
        super(rc);
    }

    int dispatch()
    {//这是最近距离调度
        //System.out.println(q);
        int lmin = CNC_id - q.get(0);
        int inearst = 0;
        for (int i = 1; i < q.size(); i++)
        {
            if (Math.abs(CNC_id - q.get(i)) < lmin)
            {
                lmin = Math.abs(CNC_id - q.get(i));
                inearst = i;
            }
        }
        int nearst = q.get(inearst);
        q.remove(inearst);
        return nearst;
    }
}
