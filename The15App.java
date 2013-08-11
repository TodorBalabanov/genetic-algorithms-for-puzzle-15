/************************************************************
*                                                           *
*     The Game Fifteen 0.50                                 *
*     Written by: © Todor Balabanov - tdb@tbsoft.eu         *
*                                                           *
*     New Bulgarian University, Sofia, 2004                 *
*                                                           *
*************************************************************
*                                                           *
*     This distribution is free, and comes with no          *
*     warranty. The program is open source provided under   *
*     the GNU General Public License.                       *
*                                                           *
************************************************************/


import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;


class GeneticAlgorithm{
  private final int POPUL_SIZE = 64;
  private final int AVR_CHOM_SIZE = 150;

  private int[][] population;
  private double fitness[];

  public GeneticAlgorithm()
  {
    population = new int[ 4*POPUL_SIZE ][];
    fitness = new double[ 4*POPUL_SIZE ];
  }

  public String toString()
  {
    String str = "";

    for(int i=0; i<fitness.length/4&&i<population.length/4; i++)
     {
     str += fitness[ i ] + "\t";

     for(int j=0; j<population[i].length; j++)
      str += population[ i ][ j ] + " ";

     str += "\n";
     }

    return( str );
  }

  private int[] chrom()
  {
    int[] chrom = null;

    chrom = new int[ 1+(int)(AVR_CHOM_SIZE*Math.random()) ];
    for(int i=0; i<chrom.length; i++)
     switch( (int)(4*Math.random()) ){
       case( 0 ): chrom[ i ] = The15.RIGHT;
                  break;
       case( 1 ): chrom[ i ] = The15.DOWN;
                  break;
       case( 2 ): chrom[ i ] = The15.LEFT;
                  break;
       case( 3 ): chrom[ i ] = The15.UP;
                  break;
      }

    return( chrom );
  }

  private int[] cross(int []a, int []b)
  {
    int []c = null;
    int min = Math.min(a.length, b.length);
    int max = Math.max(a.length, b.length);
    int pos = (int)((min+1)*Math.random());

    if(a.length == max)
     c = new int[ min ];
    else if(b.length == max)
     c = new int[ max ];

     for(int i=0; i<pos; i++)
      c[ i ] = a[ i ];
     for(int i=pos; i<c.length; i++)
      c[ i ] = b[ i ];

    return( c );
  }

  public void setFitness(double []values)
  {
     for(int i=0; values!=null&&i<fitness.length&&i<values.length; i++)
      fitness[ i ] = values[ i ];
  }

  public int[][] getPopulation()
  {
    int [][]values = new int[ population.length ][];

    for(int i=0; i<population.length; i++)
     values[ i ] = new int[ population[i].length ];

    for(int i=0; i<population.length; i++)
     for(int j=0; j<population[i].length; j++)
      values[ i ][ j ] = population[ i ][ j ];

    return( values );
  }

  public void init()
  {
    for(int i=0; i<population.length; i++)
     population[ i ] = chrom();
  }

  public void crossover()
  {
    for(int i=0; i<population.length/2; i++)
     population[ population.length/2+i ] = cross(population[i], population[(int)(population.length/2*Math.random())]);
  }

  public void mutate()
  {
    for(int i=population.length/2; i<population.length; i++)
     {
     int pos = (int)(population[ i ].length*Math.random());

     switch( (int)(4*Math.random()) ){
       case( 0 ): population[i][pos] = The15.RIGHT;
                  break;
       case( 1 ): population[i][pos] = The15.DOWN;
                  break;
       case( 2 ): population[i][pos] = The15.LEFT;
                  break;
       case( 3 ): population[i][pos] = The15.UP;
                  break;
      }
     }
  }

  public void select()
  {
    boolean done;
    do
     {
     done = true;

     for(int i=0; i<population.length-1; i++)
      if(fitness[i]>fitness[i+1] && population[i]!=null && population[i+1]!=null)
       {
       int []buff = population[ i ];
       double temp = fitness[ i ];
       population[ i ] = population[ i+1 ];
       fitness[ i ] = fitness[ i+1 ];
       population[ i+1 ] = buff;
       fitness[ i+1 ] = temp;

       done = false;
       }
     }
    while(done == false);

    for(int i=population.length/4; i<population.length/2; i++)
     population[ i ] = chrom();
    for(int i=population.length/2; i<population.length; i++)
     population[ i ] = null;
  }
}


class GATrainer{
  private final int TRAIN_LOOPS = 100;

  private The15 game;
  private GeneticAlgorithm ga;

  public GATrainer(The15 game, GeneticAlgorithm ga)
  {
    this.game = game;
    this.ga = ga;
  }

  public void train()
  {
    int [][]state = game.getState();

    ga.init();

    for(int k=0; k<TRAIN_LOOPS; k++)
     {
     ga.crossover();
     ga.mutate();

     int [][]population = ga.getPopulation();
     double []fitness = new double[ population.length ];

     for(int i=0; i<population.length; i++)
      {
      game.setState( state );

      for(int j=0; j<population[i].length; j++)
       game.makeMove( population[i][j] );

      fitness[ i ] = game.howDiffers();
      }

     ga.setFitness( fitness );
     ga.select();

     game.setState( state );
     for(int j=0; j<population[0].length; j++)
      game.makeMove( population[0][j] );

     if(fitness[0] == 0.0)
      return;
     }
  }
}


class The15{
  public static final int RIGHT = 1;
  public static final int DOWN = 2;
  public static final int LEFT = 4;
  public static final int UP = 16;

  private int [][]table;
  private int [][]solved;
  private int []pos;

  public The15()
  {
    table = new int[ 4 ][];
    for(int i=0; i<table.length; i++)
     table[ i ] = new int[ 4 ];

    solved = new int[ 4 ][];
    for(int i=0; i<solved.length; i++)
     solved[ i ] = new int[ 4 ];

    pos = new int[ 2 ];

    init();
  }

  public String toString()
  {
    String str = "";

    for(int i=0; i<table.length; i++)
     {
     for(int j=0; j<table[i].length; j++)
      {
      if(table[i][j] < 10)
       str += " ";

      str += table[ i ][ j ] + " ";
      }

     str += "\n";
     }

    return( str );
  }

  public void init()
  {
    for(int i=0, k=0; i<table.length; i++)
     for(int j=0; j<table[i].length; j++, k++)
      if(k == 15)
       table[ i ][ j ] = 0;
      else
       table[ i ][ j ] = k+1;

    for(int i=0, k=0; i<solved.length; i++)
     for(int j=0; j<solved[i].length; j++, k++)
      if(k == 15)
       solved[ i ][ j ] = 0;
      else
       solved[ i ][ j ] = k+1;

    pos();
  }

  private void pos()
  {
    for(int i=0; i<table.length; i++)
     for(int j=0; j<table[i].length; j++)
      if(table[i][j] == 0)
       {
       pos[ 0 ] = i;
       pos[ 1 ] = j;
       return;
       }
  }

  public boolean isValidMove(int dir)
  {
    boolean valid = false;

    switch( dir ){
      case(RIGHT):  if((pos[1]+1)>=0 && (pos[1]+1)<table[pos[0]].length)
                    valid = true;
                   break;
      case(DOWN): if((pos[0]+1)>=0 && (pos[0]+1)<table.length)
                    valid = true;
                   break;
      case(LEFT):  if((pos[1]-1)>=0 && (pos[1]-1)<table[pos[0]].length)
                    valid = true;
                   break;
      case(UP): if((pos[0]-1)>=0 && (pos[0]-1)<table.length)
                    valid = true;
                   break;
     }

    return( valid );
  }

  public int[][] getState()
  {
    int [][]state = new int[ table.length ][];

    for(int i=0; i<state.length; i++)
     state[ i ] = new int[ table[i].length ];

    for(int i=0; i<table.length; i++)
     for(int j=0; j<table[i].length; j++)
      state[ i ][ j ] = table[ i ][ j ];

    return( state );
  }

  public void setState(int [][]state)
  {
    try
     {
     for(int i=0; i<table.length; i++)
      for(int j=0; j<table[i].length; j++)
       table[ i ][ j ] = state[ i ][ j ];

     pos();
     }
    catch(Exception ex)
     {
     init();
     }
  }

  public boolean isDone()
  {
    if(howDiffers() == 0.0)
     return( true );

    return( false );
  }

  public double howDiffers()
  {
    double diff = 0.0;

    int k = 1;
    for(int i=0; i<table.length&&i<solved.length; i++)
     for(int j=0; j<table[i].length&&j<solved[i].length; j++, k++)
      diff += Math.pow(solved[i][j]-table[i][j], 2);

    diff = Math.sqrt( diff ) / k;

    return( diff );
  }

  public void mix()
  {
    int []posTo = new int[ 2 ];
    posTo[ 0 ] = pos[ 0 ];
    posTo[ 1 ] = pos[ 1 ];

    for(int i=0; i<10000||posTo[0]!=pos[0]||posTo[1]!=pos[1]; i++)
    switch( (int)(4*Math.random()) ){
      case( 0 ): makeMove( RIGHT );
                 break;
      case( 1 ): makeMove( DOWN );
                 break;
      case( 2 ): makeMove( LEFT );
                 break;
      case( 3 ): makeMove( UP );
                 break;
     }
  }

  public void makeMove(int dir)
  {
    switch( dir ){
      case(RIGHT):  makeMove(pos[0], pos[1]+1);
                    break;
      case(DOWN):   makeMove(pos[0]+1, pos[1]);
                    break;
      case(LEFT):   makeMove(pos[0], pos[1]-1);
                    break;
      case(UP):     makeMove(pos[0]-1, pos[1]);
                    break;
     }
  }

  public void makeMove(int a, int b)
  {
    for(int i=a-1; i<=a+1; i++)
     for(int j=b-1; j<=b+1; j++)
      if(i>=0&&j>=0&&a>=0&&b>=0 &&
         i<table.length&&j<table[i].length&&a<table.length&&b<table[i].length &&
         (a!=i||b!=j) && (a==i||b==j))
       if(table[i][j] == 0)
        {
        int buff;
        buff = table[ i ][ j ];
        table[ i ][ j ] = table[ a ][ b ];
        table[ a ][ b ] = buff;
        pos[ 0 ] = a;
        pos[ 1 ] = b;
        return;
        }
  }
}


public class The15App extends Applet{
  private Button [][]pulls;
  private Button []ops;
  private Panel []panels;
  private Label status;
  private The15 game;
  private GeneticAlgorithm ga;
  private GATrainer trainer;

  public void init()
  {
    game = new The15();
    ga = new GeneticAlgorithm();
    trainer = new GATrainer(game, ga);

    setLayout( new GridLayout(2, 1) );

    panels = new Panel[ 2 ];
    panels[ 0 ] = new Panel( new GridLayout(4, 4) );
    panels[ 1 ] = new Panel( new GridLayout(4, 1) );

    status = new Label( "The Game Fifteen by Todor Balabanov - TeodorGig@mail.ru" );

    pulls = new Button[ 4 ][];
    for(int i=0; i<pulls.length; i++)
     pulls[ i ] = new Button[ 4 ];

    ops = new Button[ 3 ];
    for(int i=0; i<ops.length; i++)
     {
     ops[ i ] = new Button();
     ops[ i ].addActionListener( new ActionListener(){
                                   public void actionPerformed(ActionEvent evt)
                                   {
                                     if( (evt.getActionCommand()).equals("Mix") )
                                      game.mix();
                                     else if( (evt.getActionCommand()).equals("Reset") )
                                      game.init();
                                     else if( (evt.getActionCommand()).equals("Solve") )
                                      trainer.train();

                                     update();
                                   }
                                  } );
     panels[ 1 ].add( ops[i] );
     }
    panels[ 1 ].add( status );

    ops[ 0 ].setLabel( "Mix" );
    ops[ 1 ].setLabel( "Reset" );
    ops[ 2 ].setLabel( "Solve" );

    for(int i=0, k=0; i<pulls.length; i++)
     for(int j=0; j<pulls[i].length; j++, k++)
      {
      String caption = "";
      if(k == 15)
       caption = "  ";
      else
       {
       if(k+1 < 10)
        caption = "0";

       caption += k+1;
       }

      pulls[ i ][ j ] = new Button( caption );
      pulls[ i ][ j ].addActionListener( new ActionListener(){
                                  public void actionPerformed(ActionEvent evt)
                                  {
                                    int a=0, b=0;
                                    Object source = evt.getSource();
                                    for(int m=0; m<pulls.length; m++)
                                      for(int n=0; n<pulls[m].length; n++)
                                       if(source == pulls[m][n])
                                        {
                                        a = m;
                                        b = n;
                                        }

                                   game.makeMove(a, b);
                                   update();
                                  }
                                } );
      }

    for(int i=0, k=0; i<pulls.length; i++)
     for(int j=0; j<pulls[i].length; j++, k++)
      panels[ 0 ].add( pulls[i][j] );

    for(int i=0; i<panels.length; i++)
     add( panels[i] );

    update();
  }

  private void update()
  {
    int [][]state = game.getState();

    for(int i=0; i<pulls.length; i++)
     for(int j=0; j<pulls[i].length; j++)
      {
      String caption = "";

      if(state[i][j] == 0)
       caption = "  ";
      else
       {
       if(state[i][j] < 10)
        caption = "0";
       caption += state[ i ][ j ];
       }

      pulls[ i ][ j ].setLabel( caption );
      }
  }

  public static void main(String[] args)
  {
    Frame theApp = new Frame( "" );
    theApp.setSize(380, 280);
    theApp.addWindowListener( new WindowAdapter(){
                                public void windowClosing(WindowEvent e)
                                {
                                  System.exit( 0 );
                                }
                              } );

    The15App the15 = new The15App();
    the15.init();
    the15.start();

    theApp.add( the15 );
    theApp.show();
  }
}
