package RGV;

import java.util.List;

public class CNC
{
    final int id;//CNC的编号
    final int process_time;//这台的加工时间
    final boolean type;//可以加工哪个种类型的物料,在CNC中仅用于判断物料类型不匹配

    int last_input_time;//上一次上料的时间
    boolean waiting;//是否处于等待状态
    Material m;//当前物料

    /**
     * 初始化CNC
     *
     * @param process_time 这台CNC的加工时间
     * @param id           这台CNC的编号
     */
    public CNC(int id, int process_time, boolean type)
    {
        this.id = id;
        this.process_time = process_time;
        this.type = type;
        last_input_time = 0;
        waiting = false;
        m = null;
    }

    /**
     * 运行加工过程到某个时刻
     *
     * @param t 某个时刻
     * @param q 如果一个加工流程完就向此队列输入请求
     */
    void run(int t, List<Integer> q)
    {//m没有物料或者如果一个加工过程完且未发出上料请求就要发出请求
        if (!waiting && (t >= last_input_time + process_time || m == null))
        {
            q.add(id);//向此队列输入请求,请求内容为这台CNC的编号
            waiting = true;//置等待状态

            //调试输出*********************
            //System.out.printf("%d-CNC%d请求上料\n", last_input_time + process_time, id);
            //调试输出*********************
        }
    }


    /**
     * 装载物料
     * 并记录下物料的装载时间
     * 并让CNC进入工作状态
     *
     * @param t 装载物料的时刻
     * @param m 要装载的物料
     * @return 返回被卸下的物料
     */
    Material reload(int t, Material m)
    {
        //调试输出*********************
        //System.out.printf("%sCNC%d", type ? "工序二" : "工序一", id);
        //if (m != null) System.out.printf("装载工件%d", m.id);
        //if (this.m != null) System.out.printf("卸载工件%d", this.m.id);
        //调试输出*********************
        if (this.m != null && t < last_input_time + process_time)
            throw new IllegalArgumentException("加工未完成，不能装载物料\n");
        if (m != null && m.type != this.type)//物料类型不匹配
            throw new IllegalArgumentException("物料类型不匹配，不能装载物料\n");
        Material tm = this.m;
        this.m = m;
        last_input_time = t;
        waiting = false;
        if (tm != null)//如果有物料出来
            tm.type = !tm.type;//先修改物料类型
        return tm;
    }
}
