import java.io.Console;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class fibonacci {
    public static int NUM_TESTS = 90;
    public static long MAX_WAIT = 10000000000L;
//    public static Hashtable<Long, Long> cache = new Hashtable<Long, Long>();
    public static long[] cache = new long[NUM_TESTS];

    public static void main(String[] args) {

//        long fibTest = fibCache(50);
//        System.out.println(fibTest);
//            testFunctionResults();
        TestSetup();
//        long result = Power(6,6);
//        System.out.println(result);

    }

    public static long fibRecur(long x){
        if ( x == 0 || x == 1)
            return x;
        else
            return fibRecur(x - 1 ) + fibRecur( x - 2);
    }

    public static long fibCache(long x){
        Arrays.fill(cache, -1);
        return fibCacheHelper(x);
    }

    public static long fibCacheHelper(long x){
//        Long x = x_long;
        if ( x == 0 || x == 1)
            return x;
        else if (cache[(int) x] != -1){
            return cache[(int) x];
        }
        else{
            long result = fibCacheHelper(x - 1 ) + fibCacheHelper( x - 2);
            cache[(int) x] = result;
            return result;
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

    public static void testFunctionResults(){
        boolean failedTest = false;
        int max_X = 41;
        for (int i = 0; i < max_X; i++) {
            long a = fibRecur(i);
            long b = fibCache(i);
            long c = fibLoop(i);
            long d = fibMatrix(i);

            if (a != b || b != c || c != d) {
                System.out.println("For x: " + i + ". fibRecur = " + a + " fibCache = " + b + " fibLoop = " + c + " fibMatrix = " + d);
                failedTest = true;
            }
        }
        if (failedTest)
            System.out.println("One or more tests failed!");
        else
            System.out.println("All tests passed with x less than " + max_X);
    }

    public static void TestSetup(){
        long[][] timings = new long[4][NUM_TESTS];
        boolean[] continueTesting = {true,true,true,true};
        long overhead = calculateOverhead();
        System.out.println("Calculated overhead: " + overhead);
        System.out.format("%5s%5s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s\n","","", "FibRecur","Tx(X) /", "Exp Tx(X) /", "FibCache", "Tx(X) /", "Exp Tx(X) /", "FibLoop", "Tx(X) /", "Exp Tx(X) /", "FibMatrix", "Tx(X) /", "Exp Tx(X) /");
        System.out.format("%5s%5s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s\n","X","N",  "Time", "Tx(X/2)", "Tx(X/2)",     "Time",    "Tx(X/2)",   "Tx(X/2)",       "Time", "Tx(X/2)", "Tx(X/2)", "Time", "Tx(X/2)", "Tx(X/2)");

        for (long x = 0; x < NUM_TESTS; x++ ) {
            for (int i = 0; i < 4; i++) {
                if (continueTesting[i] == true)
                    RunTests(x, i, timings, overhead);
                else
                    timings[i][(int) x] = 0;
            }
            System.out.format("%5s |%5s|", x, Math.ceil( (Math.log(x+1) /Math.log(2) ) ) );
            if (timings[0][(int) x] > MAX_WAIT || timings[0][(int) x] == 0) {
                System.out.format("%15s%15s%15s", "NA", "NA", "NA");
                continueTesting[0] = false;
            }
            else
                if (x % 2 == 0 && x >= 4)
                    System.out.format("%15s%15s%15.5f", timings[0][(int) x], (float)timings[0][(int) x] / timings[0][(int) x/2], Math.pow(2,(x/2)) / Math.pow(2, (float)x/4) );
                else
                    System.out.format("%15s%15s%15s", timings[0][(int) x], "-", "-");
            if (x % 2 == 0)
                System.out.format("%15s%15s%15s", timings[1][(int) x], (float)timings[1][(int) x] / timings[1][(int) x/2], 2.0);
            else
                System.out.format("%15s%15s%15s", timings[1][(int) x], "-", "-");

            if (x % 2 == 0)
                System.out.format("%15s%15s%15s", timings[2][(int) x], (float)timings[2][(int) x] / timings[2][(int) x/2], 2.0);
            else
                System.out.format("%15s%15s%15s", timings[2][(int) x], "-", "-");
            if (x % 2 == 0)
                System.out.format("%15s%15s%15.5f\n", timings[3][(int) x], (float)timings[3][(int) x] / timings[3][(int) x/2], (Math.log(x)/Math.log(2)) / (Math.log(x/2)/Math.log(2)));
            else
                System.out.format("%15s%15s%15s\n", timings[3][(int) x], "-", "-");


        }
    }

    public static void RunTests(long x, int currTest, long[][] timings, long overhead){
        long cumulativeTime = 0;
        int testsToRun = 20;

        long before = getCpuTime();
        for (int i = 0; i < testsToRun; i++) {
            if(currTest == 0){
                fibRecur(x);
            } else if ( currTest == 1){
                fibCache(x);
            } else if (currTest == 2) {
                fibLoop(x);
            } else{
                fibMatrix(x);
            }
        }
        long after = getCpuTime();
        long avgTime = (after - before)/testsToRun;
        timings[currTest][(int) x] = avgTime - overhead;
    }

    public static long calculateOverhead(){
//        long cumulativeTime = 0;
        int testsToRun = 100000;
//        long[] times = new long[testsToRun];
        long before = getCpuTime();
        for (int i = 0; i < testsToRun; i++) {

        }
        long after = getCpuTime();
        long overhead = ( after - before ) / testsToRun;
//        Arrays.sort(times);
//        long overhead = times[testsToRun/2];
        return overhead;
    }

    /** Get CPU time in nanoseconds since the program(thread) started. */
    /** from: http://nadeausoftware.com/articles/2008/03/java_tip_how_get_cpu_and_user_time_benchmarking#TimingasinglethreadedtaskusingCPUsystemandusertime **/
    public static long getCpuTime( ) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
                bean.getCurrentThreadCpuTime( ) : 0L;
    }
}
