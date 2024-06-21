package festival.rest.Client;

import festival.rest.Controller.SpectacolDTOR;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

public class SpectacolClient
{
    public static final String URL="http://localhost:55555/spectacol-management/spectacole";
    private RestClient restTemplate = RestClient.builder().requestInterceptor(new CustomRestClientInterceptor()).build();
    private  <T> T execute(Callable<T> callable)
    {
        try
        {
            return callable.call();
        }
        catch (HttpServerErrorException e)
        {
            System.out.println("Error from server "+e);
        }
        catch (ResourceAccessException e)
        {
            e.printStackTrace();}
        catch (Exception e)
        {
            e.printStackTrace();}
        return null;
    }
    public SpectacolDTOR[] getAllSpectacole()
    {
        return execute(() -> restTemplate.get().uri(URL).retrieve().body(SpectacolDTOR[].class));
    }
    public SpectacolDTOR getSpectacol(String id)
    {
        return execute(() -> restTemplate.get().uri(String.format("%s/%s", URL, id)).retrieve().body(SpectacolDTOR.class));
    }
    public SpectacolDTOR addSpectacol(SpectacolDTOR spectacol)
    {
        return execute(() -> restTemplate.post().uri(URL).body(spectacol).contentType(MediaType.APPLICATION_JSON).body(spectacol).retrieve().body(SpectacolDTOR.class));
    }
    public SpectacolDTOR updateSpectacol(SpectacolDTOR spectacol)
    {
        return execute(() -> {
            restTemplate.put().uri(String.format("%s/%s", URL, spectacol.getId())).body(spectacol).contentType(MediaType.APPLICATION_JSON).retrieve().body(SpectacolDTOR.class);
            return spectacol;
        });
    }
    public void deleteSpectacol(String id)
    {
        execute(() -> {
            restTemplate.delete().uri(String.format("%s/%s", URL, id)).retrieve().toBodilessEntity();
            return null;
        });
    }
    public class CustomRestClientInterceptor
            implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(
                HttpRequest request,
                byte[] body,
                ClientHttpRequestExecution execution) throws IOException {
            System.out.println("Sending a "+request.getMethod()+ " request to "+request.getURI()+ " and body ["+new String(body)+"]");
            ClientHttpResponse response=null;
            try {
                response = execution.execute(request, body);
                System.out.println("Got response code " + response.getStatusCode());
            }catch(IOException ex){
                System.err.println("Execution Error "+ex);
            }
            return response;
        }
    }


}
