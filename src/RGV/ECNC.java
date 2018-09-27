package RGV;

import java.util.List;

public class ECNC extends CNC
{
    private boolean toError;//本次加工会出故障吗
    private int timetoError;//从加工开始到出故障的时间
    private int timeSolveError;//解决本次故障的时间

    private final double p_error = 0.01;//故障概率
    private final int timeSolveError_max = 20 * 60;//最大检修时间
    private final int timeSolveError_min = 10 * 60;//最小检修时间

    public ECNC(int id, int process_time, boolean type)
    {
        super(id, process_time, type);
        toError = false;
        timetoError = 0;
        timeSolveError = 0;
    }


    void run(int t, List<Integer> q)
    {
        if (t < last_input_time)
            throw new IllegalArgumentException("不可获取上一次上料前的状态");

        if (!toError || t >= last_input_time + timetoError + timeSolveError || t < last_input_time + timetoError)
        {
            super.run(t, q);//本次加工不会出错||出错了但已经过了排除时间||没到故障时间-就正常运行
            return;//然后返回
        }
        //以上条件都不满足说明处在出错时间
        if (m != null)
        {
            //调试输出*********************
            //System.out.printf("%d\t%d\tCNC%d物料报废\n", m.id + 1, last_input_time + timetoError, id + 1);
            System.out.printf("%s\t%d\t%d\t%d\t物料报废\n", this.type ? "工序二" : "工序一", m.id + 1, last_input_time + timetoError, id + 1);
            System.out.printf("%s\t%d\t%d\t%d\t故障排除\n", this.type ? "工序二" : "工序一", m.id + 1, last_input_time + timetoError + timeSolveError, id + 1);
            //调试输出*********************
            m = null;
            waiting = false;
        }
        //调试输出*********************
        //System.out.printf("%d\tCNC%d修理中\n", t, id + 1);
        //调试输出*********************
    }

    Material reload(int t, Material m)
    {
        //如果有错误而且在t之前发生了而且t时没有排除
        if (toError && t > last_input_time + timetoError && t < last_input_time + timetoError + timeSolveError)
            throw new IllegalArgumentException("该CNC正在进行错误排除,不可装卸物料");
        if (m != null)//如果要上料
        {
            toError = Math.random() <= p_error;//随机生成是否出错
            if (toError)//如果有出错
            {
                //在运行时间process_time内随机生成出错时间
                timetoError = (int) (Math.random() * process_time);
                //在timeSolveError_min和timeSolveError_min之间随机生成错误排查时间
                timeSolveError = (int) (timeSolveError_min + (timeSolveError_max - timeSolveError_min) * Math.random());


                //调试输出*********************
                //System.out.printf("%s\t%d\t%d\t%d\t故障排除\n",this.type?"工序二":"工序一", m.id + 1, last_input_time + timetoError+timeSolveError, id + 1);
                //调试输出*********************
            }
        }
        return super.reload(t, m);
    }
}
