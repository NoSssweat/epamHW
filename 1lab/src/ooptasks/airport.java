package ooptasks;

public class airport {
    
   public static void main(String[] args){
       
       Plane AN2 = new AgrigatureAirplane();
       
       AN2.cargoSpace = 21;
       AN2.carrying = 11;
       System.out.println(AN2.cargoSpace + " " + AN2.carrying);
       Plane sdfdf = new Airliner();
       sdfdf.test();
   }

}
