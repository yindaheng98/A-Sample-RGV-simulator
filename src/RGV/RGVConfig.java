package RGV;

public class RGVConfig
{
    int CNC_num;//CNC数量
    int[] move_time;//移动时间
    int[] load_time;//上下料时间
    int wash_time;//RGV清洗作业时间
    CNC[] cncs;//CNC所有

    public RGVConfig(int CNC_num, int[] move_time, int[] load_time, int wash_time, CNC[] cncs)
    {
        this.CNC_num = CNC_num;
        this.move_time = move_time;
        this.load_time = load_time;
        this.wash_time = wash_time;
        this.cncs = cncs;
    }
}
