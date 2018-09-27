public class Main
{
    static int[] stage1 = new int[]{20, 33, 46, 560, 400, 378, 28, 31, 25};
    static int[] stage2 = new int[]{23, 41, 59, 580, 280, 500, 30, 35, 30};
    static int[] stage3 = new int[]{18, 32, 46, 545, 455, 182, 27, 32, 25};

    public static void main(String[] args)
    {
        //DataAnalysis.testSRGV(stage1);
        //DataAnalysis.testSRGV(stage2);
        //DataAnalysis.testSRGV(stage3);
        //DataAnalysis.testMRGV(stage1,85);
        //DataAnalysis.testMRGV(stage2,53);
        //DataAnalysis.testMRGV(stage3,41);

        //DataAnalysis.testESRGV(stage1);
        //DataAnalysis.testESRGV(stage2);
        DataAnalysis.testESRGV(stage3);
        //DataAnalysis.testEMRGV(stage1,85);
        //DataAnalysis.testEMRGV(stage2,53);
        //DataAnalysis.testEMRGV(stage3,41);


        //DataAnalysis.testsMRGV(stage1);
        //DataAnalysis.testsMRGV(stage2);
        //DataAnalysis.testsMRGV(stage3);
    }
}
