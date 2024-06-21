package festival.rest.Client;



import festival.rest.Controller.SpectacolDTOR;
import org.springframework.web.client.RestClientException;


public class StartClient{
    private final static SpectacolClient spectacolClient=new SpectacolClient();
    public static void main(String[] args) {

        SpectacolDTOR spectacolT=new SpectacolDTOR("2024-05-19T14:33","Sala Palatului",100,0,"Andra",null);
        try{
            System.out.println("Adding a new user "+spectacolT);
            show(()-> System.out.println(spectacolClient.addSpectacol(spectacolT)));
            System.out.println("\nPrinting all users ...");
            show(()->{
               SpectacolDTOR[] res=spectacolClient.getAllSpectacole();
                for(SpectacolDTOR s:res){
                     System.out.println(s);
                }
            });
        }catch(RestClientException ex){
            System.out.println("Exception ... "+ex.getMessage());
        }

        System.out.println("\nInfo for user with id=1");
        show(()-> System.out.println(spectacolClient.getSpectacol("3")));

//        System.out.println("\nDeleting user with id="+"9");
        show(()-> spectacolT.setId(16L));
    }



    private static void show(Runnable task) {
        try {
            task.run();
        } catch (Exception e) {
            //  LOG.error("Service exception", e);
            System.out.println("Service exception"+ e);
        }
    }
}
