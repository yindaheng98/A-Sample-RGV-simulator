package RGV;

import java.util.LinkedList;
import java.util.List;

public abstract class RGV
{
    //状态相关
    int CNC_id;//RGV在哪个CNC前面
    Material hand;//机械手抓着的工件
    List<Integer> q;//消息队列

    //当前系统时间
    private int time;//时间

    //常量
    private final int[] move_time;//移动时间
    private final int[] load_time;//上下料时间
    private final int wash_time;//RGV清洗作业时间
    private final CNC[] cncs;//所有的CNC


    /**
     * RGV的构造函数复制进行进行基本的初始化
     *
     * @param rc RGV设置
     */
    RGV(RGVConfig rc)
    {
        //变量初始化
        CNC_id = 0;
        hand = null;
        q = new LinkedList<Integer>();
        time = -1;

        //常量初始化
        this.move_time = rc.move_time;
        this.load_time = rc.load_time;
        this.wash_time = rc.wash_time;
        this.cncs = rc.cncs;

        //材料id重新计数
        Material.idReset();
    }

    /**
     * 调度方法，根据系统状态返回下一步要去哪个CNC
     *
     * @return 要去的CNC的id
     */
    abstract int dispatch();

    /**
     * 系统运行一步
     * 有请求就判断-移动-拿起-装载-清洗
     * 没有请求就等待一步
     * 然后更新CNC和消息队列状态
     */
    public void run()
    {
        if (!q.isEmpty())//有请求才会响应
        {
            moveto(dispatch());//移动
            pick();//拿起一个物料
            load();//装载
            wash();//清洗
        }
        else waiting();//否则等待
        update();
    }

    /**
     * 更新所有的CNC状态
     * 并更新消息队列
     */
    void update()
    {
        for (CNC cnc : cncs)
            cnc.run(time, q);//更新CNC状态
    }

    /**
     * RGV移动
     * 并更新时间
     *
     * @param CNC_id 移动到哪个CNC前面
     */
    private void moveto(int CNC_id)
    {//CNC位置=CNC编号/2
        int bias = this.CNC_id / 2 - CNC_id / 2;
        this.CNC_id = CNC_id;
        time += move_time[Math.abs(bias)];
    }

    /**
     * 拿起一个物料
     * 时间忽略
     */
    protected void pick()
    {
        if (hand != null)
            throw new IllegalArgumentException("机械手中有物料,不可抓取");
        hand = new Material();

        //调试输出*********************
        //System.out.printf("工序一\t%d\t%d\tCNC%d上料开始\n", hand.id + 1, time,CNC_id+1);
        System.out.printf("工序一\t%d\t%d\t%d\t上料开始\n", hand.id + 1, time, CNC_id + 1);
        //调试输出*********************
    }

    /**
     * 拿出RGV面前的CNC里面的物料，然后向CNC装载物料
     * 并更新时间
     */
    private void load()
    {
        //调试输出*********************
        //if (cncs[CNC_id].m != null) System.out.printf("%d\t%d\tCNC%d下料开始\n", cncs[CNC_id].m.id + 1, time,CNC_id+1);
        if (cncs[CNC_id].m != null)
            System.out.printf("%s\t%d\t%d\t%d\t下料开始\n", cncs[CNC_id].type ? "工序二" : "工序一", cncs[CNC_id].m.id + 1, time, CNC_id + 1);
        if (hand != null && hand.type) System.out.printf("工序二\t%d\t%d\t%d\t上料开始\n", hand.id + 1, time, CNC_id + 1);
        //调试输出*********************

        time += load_time[CNC_id];
        hand = cncs[CNC_id].reload(time, hand);

        //调试输出*********************
        //if (hand != null) System.out.printf("%d\t%d\tCNC%d下料结束\n", hand.id + 1, time,CNC_id+1);
        //System.out.printf("%d\t%d\tCNC%d上料结束\n", cncs[CNC_id].m.id + 1, time,CNC_id+1);
        //调试输出*********************
    }

    /**
     * 清洗手中的物料并送出系统
     * 并更新时间
     */
    protected void wash()
    {
        if (hand != null)//手里有东西就去清洗
        {
            time += wash_time;

            //调试输出*********************
            //System.out.printf("%d\t%d\t清洗结束\n", hand.id + 1, time);
            //调试输出*********************

            hand = null;
        }
    }

    /**
     * 等待1s
     * 并更新时间
     */
    protected void waiting()
    {
        time++;
    }

    /**
     * 获取当前RGV时间
     *
     * @return RGV时间
     */
    public int getTime()
    {
        return time;
    }
}
