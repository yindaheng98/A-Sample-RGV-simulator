package RGV;

class Material
{
    boolean type=false;
    private static int ids=0;
    int id=ids++;

    static void idReset()
    {
        ids=0;
    }
}
