class Viterbi
{

    public static int[] compute(int[] obs, int[] states, double[] start_p, double[][] trans_p, double[][] emit_p)
    {
      //Array V is the dynamic array
        double[][] V = new double[obs.length][states.length];
        //path is the path for the most probable state
        int[][] path = new int[states.length][obs.length];

        for (int y : states)
        {
            V[0][y] = start_p[y] * emit_p[y][obs[0]];
            path[y][0] = y;
        }

        for (int t = 1; t < obs.length; ++t)
        {
            int[][] newpath = new int[states.length][obs.length];

            for (int y : states)
            {
                double prob = -1;
                int state;
                for (int y0 : states)
                {
                    double nprob = V[t - 1][y0] * trans_p[y0][y] * emit_p[y][obs[t]];
                    if (nprob > prob)
                    {
                        prob = nprob;
                        state = y0;

                        V[t][y] = prob;

                        System.arraycopy(path[state], 0, newpath[y], 0, t);
                        newpath[y][t] = y;
                    }
                }
            }

            path = newpath;
        }
        double prob = -1;
        int state = 0;
        for (int y : states)
        {
            if (V[obs.length - 1][y] > prob)
            {
                prob = V[obs.length - 1][y];
                state = y;
            }
        }

        return path[state];
    }
}

class WeatherExample
{
  enum Weather
      {
          Hot,
          Cold,
      }
      enum Activity
      {
          one,
          two,
          three,
      }

    static int[] states = new int[]{Weather.Hot.ordinal(), Weather.Cold.ordinal()};
    static int[] observations1 = new int[]{Activity.three.ordinal(),Activity.three.ordinal(),Activity.one.ordinal(),Activity.one.ordinal(),Activity.two.ordinal(),
      Activity.two.ordinal(),Activity.three.ordinal(), Activity.one.ordinal(), Activity.three.ordinal()};
    static int[] observations2 = new int[]{Activity.three.ordinal(),Activity.three.ordinal(),Activity.one.ordinal(),Activity.one.ordinal(),Activity.two.ordinal(),
        Activity.three.ordinal(),Activity.three.ordinal(), Activity.one.ordinal(), Activity.two.ordinal()};

    static double[] start_probability = new double[]{0.8, 0.2};
    static double[][] transititon_probability = new double[][]{
            {0.7, 0.3},
            {0.4, 0.6},
    };
    static double[][] emission_probability = new double[][]{
            {0.2, 0.4, 0.4},
            {0.5, 0.4, 0.1},
    };

    public static void main(String[] args)
    {
        Viterbi v = new Viterbi();
        int[] result1 = v.compute(observations1, states, start_probability, transititon_probability, emission_probability);
        int[] result2 = v.compute(observations2, states, start_probability, transititon_probability, emission_probability);
        //for(i=0;i<2;i++)
        System.out.print("The most likely weather sequences for sequence #1 :: ");
        for (int r : result1)
        {
            System.out.print(Weather.values()[r] + " ");
        }
        System.out.print("\nThe most likely weather sequences for sequence #2 :: ");
        for (int r : result2)
        {
            System.out.print(Weather.values()[r] + " ");
        }
        System.out.println();
    }
}
