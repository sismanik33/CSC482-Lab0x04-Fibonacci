import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Hashtable;

public class fibonacci {
    public static int NUM_TESTS = 64;
    public static long MAX_WAIT = 300000000L;
    public static Hashtable<Long, Long> cache = new Hashtable<Long, Long>();

    public static void main(String[] args) {

//        long fibTest = fibMatrix(75);
//        System.out.println(fibTest);
        TestSetup();
//        long result = Power(6,6);
//        System.out.println(result);

    }
//1134903170
    public static long fibRecur(long x){
        if ( x == 0 || x == 1)
            return x;
        else
            return fibRecur(x - 1 ) + fibRecur( x - 2);
    }

    public static long fibCache(long x){
        cache.clear();
        return fibCacheHelper(x);
    }

    public static long fibCacheHelper(long x_long){
        Long x = x_long;
        if ( x == 0 || x == 1)
            return x;
        else if (cache.containsKey(x)){
            return cache.get(x);
        }
        else{
            Long fibX = fibCacheHelper(x - 1 ) + fibCacheHelper( x - 2);
            cache.put(x, fibX) ;
            return fibX;
        }
    }

//    173,402,521,172,797,813,159,685,037,284,371,942,044,301

    public static long fibLoop(long x){
        long A = 0;
        long B = 1;
        if ( x < 2)
            return  x;
        for (int i = 2; i <= x; i++) {
            long next = A + B;
            A = B;
            B = next;
        }
        return B;
    }

    public static long fibMatrix(long n){
        long F[][] = new long[][]{{1,1},{1,0}};
        if (n == 0)
            return 0;
        power(F, n-1);

        return F[0][0];
    }

    static void multiply(long F[][], long M[][])
    {
        long x =  F[0][0]*M[0][0] + F[0][1]*M[1][0];
        long y =  F[0][0]*M[0][1] + F[0][1]*M[1][1];
        long z =  F[1][0]*M[0][0] + F[1][1]*M[1][0];
        long w =  F[1][0]*M[0][1] + F[1][1]*M[1][1];

        F[0][0] = x;
        F[0][1] = y;
        F[1][0] = z;
        F[1][1] = w;
    }

    static void power(long F[][], long n)
    {

        long M[][] = new long[][]{{1,1},{1,0}};

        // n - 1 times multiply the matrix to {{1,0},{0,1}}
        for (int i = 2; i <= n; i++)
            multiply(F, M);
    }

    public static void TestSetup(){
        long[][] timings = new long[4][NUM_TESTS];
        boolean[] continueTesting = {true,true,true,true};
        System.out.format("%5s%5s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s\n","","", "FibRecur","Tx(X) /", "Exp Tx(X) /", "FibCache", "Tx(X) /", "Exp Tx(X) /", "FibLoop", "Tx(X) /", "Exp Tx(X) /", "FibMatrix", "Tx(X) /", "Exp Tx(X) /");
        System.out.format("%5s%5s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s\n","X","N",  "Time", "Tx(X/2)", "Tx(X/2)",     "Time",    "Tx(X/2)",   "Tx(X/2)",       "Time", "Tx(X/2)", "Tx(X/2)", "Time", "Tx(X/2)", "Tx(X/2)");

        for (long x = 0; x < NUM_TESTS; x++) {
            for (int i = 0; i < 4; i++) {
                if (continueTesting[i] == true)
                    RunTests(x, i, timings);
                else
                    timings[i][(int) x] = 0;
            }
            System.out.format("%5s |%5s|", x, Math.ceil( (Math.log(x+1) /Math.log(2) ) ) );
            if (timings[0][(int) x] > MAX_WAIT || timings[0][(int) x] == 0) {
                System.out.format("%15s%15s%15s", "NA", "NA", "NA");
                continueTesting[0] = false;
            }
            else
                if (x % 2 == 0)
                    System.out.format("%15s%15s%15s", timings[0][(int) x], (float)timings[0][(int) x] / timings[0][(int) x/2], "-");
                else
                    System.out.format("%15s%15s%15s", timings[0][(int) x], "-", "-");
            if (x % 2 == 0)
                System.out.format("%15s%15s%15s", timings[1][(int) x], (float)timings[1][(int) x] / timings[1][(int) x/2], "-");
            else
                System.out.format("%15s%15s%15s", timings[1][(int) x], "-", "-");

            if (x % 2 == 0)
                System.out.format("%15s%15s%15s", timings[2][(int) x], (float)timings[2][(int) x] / timings[2][(int) x/2], "-");
            else
                System.out.format("%15s%15s%15s", timings[2][(int) x], "-", "-");
            if (x % 2 == 0)
                System.out.format("%15s%15s%15s\n", timings[3][(int) x], (float)timings[3][(int) x] / timings[3][(int) x/2], "-");
            else
                System.out.format("%15s%15s%15s\n", timings[3][(int) x], "-", "-");


        }
    }

    public static void RunTests(long x, int currTest, long[][] timings){
        long cumulativeTime = 0;
        int testsRun = 0;
        long after, before;

        for (int i = 0; i < 20; i++) {
            if(currTest == 0){
                before = getCpuTime();
                fibRecur(x);
                after = getCpuTime();
            } else if ( currTest == 1){
                before = getCpuTime();
                fibCache(x);
                after = getCpuTime();
            } else if (currTest == 2) {
                before = getCpuTime();
                fibLoop(x);
                after = getCpuTime();
            } else{
                before = getCpuTime();
                fibMatrix(x);
                after = getCpuTime();
            }
            testsRun++;
            cumulativeTime += after - before;
        }
        long avgTime = cumulativeTime/testsRun;
        timings[currTest][(int) x] = avgTime;
    }

    /** Get CPU time in nanoseconds since the program(thread) started. */
    /** from: http://nadeausoftware.com/articles/2008/03/java_tip_how_get_cpu_and_user_time_benchmarking#TimingasinglethreadedtaskusingCPUsystemandusertime **/
    public static long getCpuTime( ) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
                bean.getCurrentThreadCpuTime( ) : 0L;
    }
}