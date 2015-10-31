import java.util.*;

public class algorithms
{
  public static void main(String[] args)
  {
    //Testing generatePermutations
    ArrayList<ArrayList<String>> nodes = new ArrayList<ArrayList<String>>();
    ArrayList<String> random = new ArrayList<String>(Arrays.asList("hi","hi2","hi3","hi4","hi5"));
    nodes = generatePermutations(random);
    System.out.println(nodes);
    System.out.println(nodes.size());

    //Testing TimeCostAve
    System.out.println("Testing TimeCostAve");
    System.out.println(TimeCostAve("Marina Bay Sands", "Buddha Tooth Relic Temple"));
    System.out.println(TimeCostAve("Buddha Tooth Relic Temple","Singapore Flyer"));

    //Testing TotalTimeCostAve
    ArrayList<String> it = new ArrayList<String>(Arrays.asList("Marina Bay Sands", "Buddha Tooth Relic Temple","Singapore Flyer"));
    System.out.println("Testing TotalTimeCostAve");
    System.out.println(TotalTimeCostAve(it));

    //Testing getOptimalItinerary
    System.out.println("Testing getOptimalItinerary");
    ArrayList<ArrayList<String>> its = new ArrayList<ArrayList<String>>();
    String e = it.remove(0);
    its = generatePermutations(it);
    for(ArrayList<String> itinerary : its)
    {
      itinerary.add(0,e);
    }
    System.out.println("Permutations");
    System.out.println(its);
    System.out.println("Optimal it "+ getOptimalItinerary(its));

    //Testing nextshortestEdge
    String[] shortestedge = {"Marina Bay Sands", "Buddha Tooth Relic Temple"};
    ArrayList<String[]> empty = new ArrayList<String[]>();
    ArrayList<String[]> shortedges = new ArrayList<String[]>();
    shortedges.add(shortestedge);
    
    ArrayList<String> it2 = new ArrayList<String>(Arrays.asList("Marina Bay Sands", "Buddha Tooth Relic Temple","Singapore Flyer", "Vivo City" ));
    System.out.println("Testig nextshortestEdge");
    for(String node : nextshortestEdge("walking time",it2,empty))
      System.out.println(node);

    //Testing getTime and getCost
    System.out.println("Testing getTime and getCost");
    System.out.println(getTime(2,"Marina Bay Sands","Vivo City"));
    System.out.println(getCost(2,"Marina Bay Sands","Vivo City"));

    //Testing getTotalCost
    System.out.println("Testing getTotalCost");
    int[] modes = {2,2,2};
    System.out.println(getTotalCost(modes,it2));

    //Testing getTransportMode
    System.out.println("Testing getTransportMode");
    double budget_transportmode = 2;
    ArrayList<String> it_transportmode = new ArrayList<String>(Arrays.asList("Marina Bay Sands", "Buddha Tooth Relic Temple", "Singapore Flyer", "Zoo"));
    for(int num:getTransportMode(it_transportmode,budget_transportmode))
    {
      System.out.println(num);
    }

    /*
    //Testing behavior of default initialized objects
    System.out.println("testing indexOf on null");
    String testr = null;
    ArrayList<String> test = new ArrayList<String>(Arrays.asList("lol","wut"));
    System.out.println(test.indexOf(testr));

    System.out.println("testing empty initalized string array");
    String[] testarr = new String[2];
    for(String str:testarr)
      System.out.println(str);
    System.out.println("testing contains on null string");
    System.out.println(it2.contains(testarr[0]));
    */

  }



  //Generates an arraylist of all possible permutations of an arraylist of strings
  public static ArrayList<ArrayList<String>> generatePermutations(ArrayList<String> destinations)
  {
      if(destinations.size() == 1)
      {
        ArrayList<ArrayList<String>> permutelists = new ArrayList<ArrayList<String>>();
        permutelists.add(destinations);
        return permutelists;
      }
      //Note that arrlist.remove(int index) returns the element removed!
      //It mutates the arrlist
      String lastele = destinations.remove(0); //remove the first element from destinations
      ArrayList<ArrayList<String>> oldpermutelists = generatePermutations(destinations);
      ArrayList<ArrayList<String>> newpermutelists = new ArrayList<ArrayList<String>>();
      for(ArrayList<String> list : oldpermutelists)
      {
        //Not modifying the array list of arraylists itself, but the ararylist elements
        for(int i=0; i<=list.size(); i++)
        {
          //Note newlist only exists within the scope of the for loop
          //With each iteration a new newlist object is created
          //newlist thus points to a new object each time, which is what we want
          //We don't want to add list directly as any subsequent modifications
          //would modify the same object
          ArrayList<String> newlist = new ArrayList<String>(list);
          newlist.add(i,lastele);
          newpermutelists.add(newlist);
        }
      }

      return newpermutelists;
  }

    
  /******************************************************************************
   * Average time-cost is used for the nearest-neighbour algorithm, where the 
   * nearest neighbout is determined in terms of the time-cost average
   *
   * This is also used for a faster computation of the optimal itinerary
   * generated by the brute force algorithm - thus not brute-forcing it 
   * all the way
   *
   * Might implement a true brute-force optimal finder as well
   * ***************************************************************************/

  //Returns the time-cost average of a given edge (specifed by fromNode and toNode)
  public static double TimeCostAve(String fromNode, String toNode)
  {
    double walkTimeCost = 1.0*Destinations.TIME_ARR[0][Destinations.DESTINATION_MAP.get(fromNode)][
                              Destinations.DESTINATION_MAP.get(toNode)];
    double publicTimeCost = Destinations.COST_ARR[1][Destinations.DESTINATION_MAP.get(fromNode)][
                            Destinations.DESTINATION_MAP.get(toNode)]*Destinations.TIME_ARR[1][Destinations.DESTINATION_MAP.get(fromNode)][
                            Destinations.DESTINATION_MAP.get(toNode)];
    double taxiTimeCost = Destinations.COST_ARR[2][Destinations.DESTINATION_MAP.get(fromNode)][
                            Destinations.DESTINATION_MAP.get(toNode)]*Destinations.TIME_ARR[2][Destinations.DESTINATION_MAP.get(fromNode)][
                            Destinations.DESTINATION_MAP.get(toNode)];

    return (walkTimeCost+publicTimeCost+taxiTimeCost)/3;
 
  }

  //Returns the total time-cost average of a given itinerary
  public static double TotalTimeCostAve(ArrayList<String> path)
  {
    double total = 0;
    for(int i =0; i<path.size(); i++)
    {
      if (i==path.size()-1)
      {
        total += TimeCostAve(path.get(i),path.get(0));
      }
      else
        total += TimeCostAve(path.get(i), path.get(i+1)); 
    }
    return total;
  }

  //Returns the optimal itinerary based on the total time-cost average
  public static ArrayList<String> getOptimalItinerary(ArrayList<ArrayList<String>> itineraries)
  {
    double optimaltimecost = -1.0;
    ArrayList<String> optimalit = new ArrayList<String>();
    for(int i =0; i<itineraries.size();i++)
    {
      double newtimecost = TotalTimeCostAve(itineraries.get(i));
      if(optimaltimecost == -1.0 || newtimecost < optimaltimecost)
      {
        optimaltimecost = newtimecost;
        optimalit = itineraries.get(i);
      }
      System.out.println("opttimecost "+optimaltimecost);
    }

    return optimalit;
  }
  
  /********************************************************************************************
   * Once given an optimal itinerary computed based on the time-cost average, the actual 
   * mode of transport between each node has to be determined, and is constrained by the 
   * budget 
   *
   * The algorithm to determine the mode between each node starts by assuming taxi
   * for every edge; while the budget is exceeded, find the shortest edge in terms 
   * of time-cost average, and check if the walking time is less than 15 min
   * If yes, change mode to walking, else change to bus, then check whether the budget
   * is exceeded; repeat until budget is no longer exceeded
   * 
   * *****************************************************************************************/
   
  //Convenience method for checking if a list of arrays of objects contains a given object
  //Have to implement this because arrlist.contains uses .equals() to check equality
  //But this does only shallow checking. To check equality of arrays of objects, one should use
  //Arrays.deepEquals
  public static boolean deepContainsArray(List<? extends Object[]> list, Object[] oarr)
  {
    for(Object[] objarr:list)
    {
      if(Arrays.deepEquals(objarr,oarr))
      {
        return true;
      }
    }

    return false;

  }
  //Returns the shortest edge, represented as an array of fromNode and toNode, from the itinerary that
  //is not already in the sortedges array
  //@param type: determines method to evaluate shortness of the edge; can be
  //"time-cost-ave" : lowest time-cost average
  //"walking time" : shortest walking time
  public static String[] nextshortestEdge(String type, ArrayList<String> itinerary, ArrayList<String[]> shortedges) 
  {
    double timecostave = -1.0;
    double newtimecostave = 0;
    String[] edge = new String[2];
    String[] testedge = new String[2];
    
    for(int i =0; i<itinerary.size();i++)
    {
      newtimecostave = type.equals("time-cost-ave")?TimeCostAve(itinerary.get(i), itinerary.get(i==itinerary.size()-1?0:i+1)):
        getTime(0,itinerary.get(i),itinerary.get(i==itinerary.size()-1?0:i+1));
      testedge[0] = itinerary.get(i);
      testedge[1] = itinerary.get(i==itinerary.size()-1?0:i+1);

      /*for(String node:testedge)
        System.out.print(node+" ");
      System.out.println("");
      System.out.println("edge is contained? " + deepContainsArray(shortedges,testedge));
*/
      if((timecostave == -1.0 || newtimecostave < timecostave ) && !deepContainsArray(shortedges,testedge))
      {
        timecostave = newtimecostave;
        edge[0]= itinerary.get(i);
        edge[1] = itinerary.get(i==itinerary.size()-1?0:i+1);

      }

    }

    return edge;
  }

      
  //Returns the time for a given mode of transport of an edge specified by fromNode and toNode
  public static int getTime(int mode, String fromNode, String toNode)
  {
    return Destinations.TIME_ARR[mode][Destinations.DESTINATION_MAP.get(fromNode)][Destinations.DESTINATION_MAP.get(toNode)];
  }

  //Returns the cost for a given mode of transport of an edge specified by fromNode and toNode
  public static double getCost(int mode, String fromNode, String toNode)
  {
    return Destinations.COST_ARR[mode][Destinations.DESTINATION_MAP.get(fromNode)][Destinations.DESTINATION_MAP.get(toNode)];
  }

  //Returns the total time given a mode array which corresponds to an itinerary
  public static int getTotalTime(int[] modes, ArrayList<String> itinerary)
  {
    int total=0;
    for(int i =0; i < modes.length; i++)
    {
      total += getTime(modes[i], itinerary.get(i), itinerary.get(i==modes.length-1?0:i+1));
    }

    return total;
  }

  //Returns the total cost given a mode array which corresponds to an itinerary
  public static double getTotalCost(int[] modes, ArrayList<String> itinerary)
  {
    double total=0;
    for(int i =0; i<modes.length; i++)
    {
      total+= getCost(modes[i], itinerary.get(i),itinerary.get(i==modes.length-1?0:i+1));
    }

    return total;
  }

  //Returns an array of integers, with size equal that of the itinerary size.
  //Integers specifiy the mode of transport
  //0: Walk
  //1: Public Transport
  //2: Taxi
  public static ArrayList<int[]> getTransportMode(ArrayList<String> itinerary, double budget)
  {
    //Set initial modes all to taxi
    ArrayList<int[]> possiblemodes = new ArrayList<int[]>();
    int[] modes = new int[itinerary.size()];
    for(int i=0; i<modes.length;i++)
      modes[i] = 2;
    for(int mode:modes)
      System.out.println("mode "+mode);
    
    double totalcost = getTotalCost(modes,itinerary);
    String[] shortestedge = new String[2];
    ArrayList<String[]> shortedges = new ArrayList<String[]>();

    //This will loop until all the modes are either walking (0) or public transportation(1)
    //At the moment it may get stuck when all shortedges have been changed, but the 
    //budget is still exceeded because it refuses to choose walk when it takes more than 15 min
    //Use a second exit condition (if none of the mode values are 2), so we know that it has 
    //evaluated all the edges once already
    //Have a second while loop to run through the edges again, this time getting shortestedge 
    //by walking time, and changing the mode for that edge to walk. Do this until budget is not exceeded
    //
    //Should be intelligent enough to identify if there are significant time savings with just a small increase in budget
    //Different thresholds of "significant" which will be made explicit to the user
    //Eg. Save 20/30mins of travelling time if you spend just 2/3/5 dollars over your current budget (new cost)
    //Instead of giving just the most time-optimal answer under thet budget, return a list of possible modes,
    //where one may get siginificant cost savings for a small increase in time, or like above, time savings 
    //for a small increase in cost
    //The change in cost and time is measured against the first solution (most time-optimal within budget)
    //Measure as change in cost per change in time (for finding less time-optimal but less costly options)
    //If the cost per time is above a certain threshold (tradeoff is worth it), then store that possibility
    //Maybe also use total extra time as an additional condition (so that the total travel time is not too ridiculous
    //even if cost per time is high?)
    //Measure as change in time per change in cost (for finding more time-optimal but over-budget options)
    //If the time per cost is above a certain threshold(tradeoff is worth it), then store that possibility
    //Use total extra cost as an additional condition so that choice is not ridiculously over budget 
    while(true)
    {
      System.out.println("total cost "+ totalcost);
      shortestedge = nextshortestEdge("time-cost-ave",itinerary, shortedges);
      shortedges.add(shortestedge);
      System.out.println("Shortestedge " + shortestedge[0] + " " + shortestedge[1]);

      if(totalcost>budget)
      {
        //If walking takes less than 15 minutes, then walk
        if(Destinations.TIME_ARR[0][Destinations.DESTINATION_MAP.get(shortestedge[0])][Destinations.DESTINATION_MAP.get(shortestedge[1])] <= 15)
        {
          modes[itinerary.indexOf(shortestedge[0])] = 0;
          totalcost = getTotalCost(modes,itinerary);
        }
        else //if not, take public transport
        {
          modes[itinerary.indexOf(shortestedge[0])] = 1;
          totalcost = getTotalCost(modes,itinerary);
        }
        
        if(totalcost<=budget)
        {
          possiblemodes.add(modes.clone());//have to clone modes so that I don't end up assigning the reference

          //break;
        }
      }
      else
      {
        possiblemodes.add(modes.clone());
        //break;
      }

      //Ran through all edges once but still over budget
      if(Arrays.stream(modes).allMatch(mode -> mode != 2))
        break;
    }

    possiblemodes.sort((mode1,mode2)-> {
      if(getTotalTime(mode1,itinerary)==getTotalTime(mode2,itinerary)) return 0;
      getTotalTime(mode1, itinerary)<getTotalTime(mode2,itinerary)?-1:)

    }    shortedges.clear();

    while(totalcost>budget)
    {
      shortestedge = nextshortestEdge("walking time",itinerary, shortedges);
      shortedges.add(shortestedge);
      System.out.println("After no taxi, shortestedge" + shortestedge[0]+" "+shortestedge[1]);

      modes[itinerary.indexOf(shortestedge[0])] = 0;
      totalcost = getTotalCost(modes,itinerary);

    }
    System.out.println("Final total cost "+ totalcost);
    return modes; 


  }
  

  public static class Destinations
  {
    //Static final variables can be initialized in a static block
    private static final HashMap<String,Integer> DESTINATION_MAP = new HashMap<String,Integer>();
    static
    {
      DESTINATION_MAP.put("Marina Bay Sands",0);
      DESTINATION_MAP.put("Singapore Flyer",1);
      DESTINATION_MAP.put("Vivo City",2);
      DESTINATION_MAP.put("Resorts World Sentosa",3);
      DESTINATION_MAP.put("Buddha Tooth Relic Temple",4);
      DESTINATION_MAP.put("Zoo",5);
    }

    private static final int NUM_DESTINATIONS = DESTINATION_MAP.size();

    private static final double[][][] COST_ARR= new double[3][NUM_DESTINATIONS][NUM_DESTINATIONS];
    static
    {
      COST_ARR[1][0][1] = 0.83;
      COST_ARR[1][0][2] = 1.18;
      COST_ARR[1][0][3] = 4.03;
      COST_ARR[1][0][4] = 0.88;
      COST_ARR[1][0][5] = 1.96;

      COST_ARR[1][1][0] = 0.83;
      COST_ARR[1][1][2] = 1.26;
      COST_ARR[1][1][3] = 4.03;
      COST_ARR[1][1][4] = 0.98;
      COST_ARR[1][1][5] = 1.89;

      COST_ARR[1][2][0] = 1.18;
      COST_ARR[1][2][1] = 1.26;
      COST_ARR[1][2][3] = 2.00;
      COST_ARR[1][2][4] = 0.98;
      COST_ARR[1][2][5] = 1.99;

      COST_ARR[1][3][0] = 1.18;
      COST_ARR[1][3][1] = 1.26;
      COST_ARR[1][3][2] = 0.0;
      COST_ARR[1][3][4] = 0.98;
      COST_ARR[1][3][5] = 1.99;

      COST_ARR[1][4][1] = 0.98;
      COST_ARR[1][4][2] = 0.98;
      COST_ARR[1][4][3] = 0.98;
      COST_ARR[1][4][4] = 3.98;
      COST_ARR[1][4][5] = 1.91;

      COST_ARR[1][5][1] = 1.88;
      COST_ARR[1][5][2] = 1.96;
      COST_ARR[1][5][3] = 2.11;
      COST_ARR[1][5][4] = 4.99;
      COST_ARR[1][5][5] = 1.91;

      // Taxi cost
      COST_ARR[2][0][1] = 3.22;
      COST_ARR[2][0][2] = 6.96;
      COST_ARR[2][0][3] = 8.50;
      COST_ARR[2][0][4] = 4.98;
      COST_ARR[2][0][5] = 18.40;

      COST_ARR[2][1][0] = 4.32;
      COST_ARR[2][1][2] = 7.84;
      COST_ARR[2][1][3] = 9.38;
      COST_ARR[2][1][4] = 4.76;
      COST_ARR[2][1][5] = 18.18;

      COST_ARR[2][2][0] = 8.30;
      COST_ARR[2][2][1] = 7.96;
      COST_ARR[2][2][3] = 4.54;
      COST_ARR[2][2][4] = 6.42;
      COST_ARR[2][2][5] = 22.58;

      COST_ARR[2][3][0] = 8.74;
      COST_ARR[2][3][1] = 8.40;
      COST_ARR[2][3][2] = 3.22;
      COST_ARR[2][3][4] = 6.64;
      COST_ARR[2][3][5] = 22.80;

      COST_ARR[2][4][0] = 5.32;
      COST_ARR[2][4][1] = 4.76;
      COST_ARR[2][4][2] = 4.98;
      COST_ARR[2][4][3] = 6.52;
      COST_ARR[2][4][5] = 18.40;

      COST_ARR[2][5][0] = 22.48;
      COST_ARR[2][5][1] = 19.40;
      COST_ARR[2][5][2] = 21.48;
      COST_ARR[2][5][3] = 23.68;
      COST_ARR[2][5][4] = 21.60;

    }

    private static final int[][][] TIME_ARR = new int[3][NUM_DESTINATIONS][NUM_DESTINATIONS];
    static{
      //Walking time
      TIME_ARR[0][0][1] = 14;
      TIME_ARR[0][0][2] = 69;
      TIME_ARR[0][0][3] = 76;
      TIME_ARR[0][0][4] = 28;
      TIME_ARR[0][0][5] = 269;

      TIME_ARR[0][1][0] = 14;
      TIME_ARR[0][1][2] = 81;
      TIME_ARR[0][1][3] = 88;
      TIME_ARR[0][1][4] = 39;
      TIME_ARR[0][1][5] = 264;

      TIME_ARR[0][2][0] = 69;
      TIME_ARR[0][2][1] = 81;
      TIME_ARR[0][2][3] = 12;
      TIME_ARR[0][2][4] = 47;
      TIME_ARR[0][2][5] = 270;

      TIME_ARR[0][3][0] = 76;
      TIME_ARR[0][3][1] = 88;
      TIME_ARR[0][3][2] = 12;
      TIME_ARR[0][3][4] = 55;
      TIME_ARR[0][3][5] = 285;

      TIME_ARR[0][4][0] = 28;
      TIME_ARR[0][4][1] = 39;
      TIME_ARR[0][4][2] = 47;
      TIME_ARR[0][4][3] = 55;
      TIME_ARR[0][4][5] = 264;

      TIME_ARR[0][5][0] = 269;
      TIME_ARR[0][5][1] = 264;
      TIME_ARR[0][5][2] = 270;
      TIME_ARR[0][5][3] = 285;
      TIME_ARR[0][5][4] = 264;

      //Bus time
      TIME_ARR[1][0][1] = 17;
      TIME_ARR[1][0][2] = 26;
      TIME_ARR[1][0][3] = 35;
      TIME_ARR[1][0][4] = 19;
      TIME_ARR[1][0][5] = 84;

      TIME_ARR[1][1][0] = 17;
      TIME_ARR[1][1][2] = 31;
      TIME_ARR[1][1][3] = 38;
      TIME_ARR[1][1][4] = 24;
      TIME_ARR[1][1][5] = 85;

      TIME_ARR[1][2][0] = 24;
      TIME_ARR[1][2][1] = 29;
      TIME_ARR[1][2][3] = 10;
      TIME_ARR[1][2][4] = 18;
      TIME_ARR[1][2][5] = 85;

      TIME_ARR[1][3][0] = 33;
      TIME_ARR[1][3][1] = 38;
      TIME_ARR[1][3][2] = 10;
      TIME_ARR[1][3][4] = 27;
      TIME_ARR[1][3][5] = 92;

      TIME_ARR[1][4][0] = 18;
      TIME_ARR[1][4][1] = 23;
      TIME_ARR[1][4][2] = 19;
      TIME_ARR[1][4][3] = 28;
      TIME_ARR[1][4][5] = 83;

      TIME_ARR[1][5][0] = 86;
      TIME_ARR[1][5][1] = 87;
      TIME_ARR[1][5][2] = 86;
      TIME_ARR[1][5][3] = 96;
      TIME_ARR[1][5][4] = 84;

      // Taxi cost
      TIME_ARR[2][0][1] = 3;
      TIME_ARR[2][0][2] = 14;
      TIME_ARR[2][0][3] = 19;
      TIME_ARR[2][0][4] = 8;
      TIME_ARR[2][0][5] = 30;

      TIME_ARR[2][1][0] = 6;
      TIME_ARR[2][1][2] = 13;
      TIME_ARR[2][1][3] = 18;
      TIME_ARR[2][1][4] = 8;
      TIME_ARR[2][1][5] = 29;

      TIME_ARR[2][2][0] = 12;
      TIME_ARR[2][2][1] = 14;
      TIME_ARR[2][2][3] = 9;
      TIME_ARR[2][2][4] = 11;
      TIME_ARR[2][2][5] = 31;

      TIME_ARR[2][3][0] = 13;
      TIME_ARR[2][3][1] = 14;
      TIME_ARR[2][3][2] = 4;
      TIME_ARR[2][3][4] = 12;
      TIME_ARR[2][3][5] = 32;

      TIME_ARR[2][4][0] = 7;
      TIME_ARR[2][4][1] = 8;
      TIME_ARR[2][4][2] = 9;
      TIME_ARR[2][4][3] = 14;
      TIME_ARR[2][4][5] = 30;

      TIME_ARR[2][5][0] = 32;
      TIME_ARR[2][5][1] = 29;
      TIME_ARR[2][5][2] = 32;
      TIME_ARR[2][5][3] = 36;
      TIME_ARR[2][5][4] = 30;

    }

  }
}