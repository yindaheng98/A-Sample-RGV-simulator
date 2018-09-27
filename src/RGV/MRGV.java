package RGV;


public class MRGV extends RGV
{
    private boolean[] CNC_type;

    public MRGV(RGVConfig rc)
    {
        super(rc);
        this.CNC_type = new boolean[rc.CNC_num];
        for (int i = 0; i < rc.CNC_num; i++)
            this.CNC_type[i] = rc.cncs[i].type;
    }

    int dispatch()//前面的算法可以保证到这里的时候队列里有能响应的
    {
        boolean type_tofind = hand != null && hand.type;
        //手没有东西的时候去找原料CNC或者是拿着东西的时候去找hand.type型对应的CNC
        int lmin = 65535;
        int r = -1;
        for (int i = 0; i < q.size(); i++)
        {
            if (CNC_type[q.get(i)] == type_tofind)//有要找的cnc
            {
                if (Math.abs(CNC_id - q.get(i)) < lmin)//而且路径比较短
                    r = i;//就选这个
                lmin = Math.abs(CNC_id - q.get(i));//更新最小值
            }
        }
        if (r == -1)
            throw new IllegalArgumentException("找不到可以响应的CNC");
        int res = q.get(r);
        q.remove(r);
        return res;
    }

    @Override
    public void run()
    {
        if (hand != null && !hand.type)//如果手里拿着东西,但不是半成品
            throw new IllegalArgumentException("机械手不能一直抓着成品或原料");
        else
        {//剩下两种情况:手里没拿东西|手里拿着半成品
            if (!q.isEmpty())
            {
                for (int id : q)
                    if (CNC_type[id] == (hand != null))
                    {//手里拿着半成品即hand != null,找半成品CNC即true
                        //手里拿着东西即hand==null,原料半成品CNC即false
                        super.run();//就运行
                        return;//然后返回
                    }
            }
            waiting();
            update();//没有半成品CNC加工请求就等待
        }
    }

    @Override
    protected void pick()
    {//如果去半成品CNC就不pick任何物料,手里是什么就给什么,否则就要pick一个新物料
        if (!CNC_type[CNC_id])
            super.pick();
    }

    @Override
    protected void wash()
    {
        if (hand != null && !hand.type)//手里拿着完成品才去清洗
        {
            super.wash();
        }
    }
}
