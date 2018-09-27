import RGV.CNC;
import RGV.ECNC;
import RGV.RGV;
import RGV.SRGV;
import RGV.MRGV;
import RGV.RGVConfig;

class DataAnalysis
{
    static RGVConfig singl_noerr_rc(int[] r)
    {
        int CNC_num = 8;
        int[] move_time = {0, r[0], r[1], r[2]};
        int[] load_time = new int[CNC_num];
        int[] process_time = new int[CNC_num];
        for (int i = 0; i < CNC_num; i++)
            process_time[i] = r[3];
        for (int i = 0; i < CNC_num; i++)
            load_time[i] = i % 2 == 0 ? r[6] : r[7];
        int wash_time = r[8];
        CNC[] cncs = new CNC[CNC_num];
        for (int i = 0; i < CNC_num; i++)
            cncs[i] = new CNC(i, process_time[i], false);
        return new RGVConfig(CNC_num, move_time, load_time, wash_time, cncs);
    }

    static RGVConfig multi_noerr_rc(int[] r, int bitnum)
    {
        //数据
        int CNC_num = 8;
        int[] move_time = {0, r[0], r[1], r[2]};
        boolean[] CNC_type = bitmap(bitnum, CNC_num);
        int[] load_time = new int[CNC_num];
        int[] process_time = new int[CNC_num];
        for (int i = 0; i < CNC_num; i++)
            process_time[i] = !CNC_type[i] ? r[4] : r[5];
        for (int i = 0; i < CNC_num; i++)
            load_time[i] = i % 2 == 0 ? r[6] : r[7];
        int wash_time = r[8];
        CNC[] cncs = new CNC[CNC_num];
        for (int i = 0; i < CNC_num; i++)
            cncs[i] = new CNC(i, process_time[i], CNC_type[i]);
        return new RGVConfig(CNC_num, move_time, load_time, wash_time, cncs);
    }

    static RGVConfig singl_error_rc(int[] r)
    {
        int CNC_num = 8;
        int[] move_time = {0, r[0], r[1], r[2]};
        int[] load_time = new int[CNC_num];
        int[] process_time = new int[CNC_num];
        for (int i = 0; i < CNC_num; i++)
            process_time[i] = r[3];
        for (int i = 0; i < CNC_num; i++)
            load_time[i] = i % 2 == 0 ? r[6] : r[7];
        int wash_time = r[8];
        CNC[] cncs = new CNC[CNC_num];
        for (int i = 0; i < CNC_num; i++)
            cncs[i] = new ECNC(i, process_time[i], false);
        return new RGVConfig(CNC_num, move_time, load_time, wash_time, cncs);
    }

    static RGVConfig multi_error_rc(int[] r, int bitnum)
    {
        int CNC_num = 8;
        int[] move_time = {0, r[0], r[1], r[2]};
        boolean[] CNC_type = bitmap(bitnum, CNC_num);
        int[] load_time = new int[CNC_num];
        int[] process_time = new int[CNC_num];
        for (int i = 0; i < CNC_num; i++)
            process_time[i] = !CNC_type[i] ? r[4] : r[5];
        for (int i = 0; i < CNC_num; i++)
            load_time[i] = i % 2 == 0 ? r[6] : r[7];
        int wash_time = r[8];
        CNC[] cncs = new CNC[CNC_num];
        for (int i = 0; i < CNC_num; i++)
            cncs[i] = new ECNC(i, process_time[i], CNC_type[i]);
        return new RGVConfig(CNC_num, move_time, load_time, wash_time, cncs);
    }

    static void testSRGV(int[] stage)
    {
        //运行
        RGV rgv = new SRGV(singl_noerr_rc(stage));
        while (rgv.getTime() < 8 * 3600)
            rgv.run();
    }

    static void testMRGV(int[] stage, int bitnum)
    {
        //运行
        RGV rgv = new MRGV(multi_noerr_rc(stage, bitnum));
        while (rgv.getTime() < 8 * 3600)
            rgv.run();
    }

    static void testESRGV(int[] stage)
    {
        //运行
        RGV rgv = new SRGV(singl_error_rc(stage));
        while (rgv.getTime() < 8 * 3600)
            rgv.run();
    }

    static void testEMRGV(int[] stage, int bitnum)
    {
        //运行
        RGV rgv = new MRGV(multi_error_rc(stage, bitnum));
        while (rgv.getTime() < 8 * 3600)
            rgv.run();
    }

    /**
     * 读取出一个正整数的比特图
     *
     * @param n 一个正整数
     * @param w 读取多少位
     * @return 比特图
     */
    private static boolean[] bitmap(int n, int w)
    {
        boolean[] r = new boolean[w];
        for (int i = 1; i <= w; i++)
            r[w - i] = (n >> (i - 1)) % 2 == 1;
        return r;
    }

    static void subtestsMRGV(int[] stage, int bitnum)
    {
        //运行
        RGV rgv = new MRGV(multi_noerr_rc(stage, bitnum));
        while (rgv.getTime() < 8 * 3600)
            rgv.run();
    }

    static void testsMRGV(int[] stage)
    {
        for (int i = 1; i < 255; i++)
        {
            System.out.println("0");
            subtestsMRGV(stage, i);
        }
    }
}
