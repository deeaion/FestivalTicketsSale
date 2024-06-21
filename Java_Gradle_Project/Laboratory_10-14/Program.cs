using System;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading;
using System.Threading.Tasks;
using System.Net.Http.Json;

using Newtonsoft.Json;
using System;
namespace CSharpRestClient
{
	class MainClass
	{
		//static HttpClient client = new HttpClient();
		//pentru jurnalizarea operatiilor efectuate si a datelor trimise/primite
		static HttpClient client = new HttpClient(new LoggingHandler(new HttpClientHandler()));

		private static string URL_Base = "http://localhost:55555/spectacol-management/spectacole";

		public static void Main(string[] args)
		{
			//Console.WriteLine("Hello World!");
			RunAsync().Wait();
		}


		static async Task RunAsync()
		{
			client.BaseAddress = new Uri("http://localhost:55555/spectacol-management/spectacole");
			client.DefaultRequestHeaders.Accept.Clear();
			client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
			// Get one spectacol
			String id = "3";
			Console.WriteLine("Get spectacol with id {0}", id);
			SpectacolDTO spectacol = await GetSpectacolAsync(URL_Base + "/" + id);
			Console.WriteLine("Am primit {0}",spectacol);
			//Get all spectacole
			Console.WriteLine("Get all spectacole");
			String spectacole = await GetAllSpectacoleAsync(URL_Base);
			//Create a spectacol
			SpectacolDTO spectacolDTO = new SpectacolDTO("2024-05-19T14:33", "Sala Palatului", 100, 0, "Andra", 1);
			Console.WriteLine("Create user {0}", spectacolDTO);
			SpectacolDTO result = await CreateSpectacolAsync(URL_Base, spectacolDTO);
			
			Console.WriteLine("Am primit {0}", result);
			//Update a spectacol
			SpectacolDTO spectacolDTO2 = new SpectacolDTO("2024-05-19T14:33", "Sala Palatului", 100, 0, "Andra", 3);
			Console.WriteLine("Update spectacol {0}", spectacolDTO2);
			SpectacolDTO result2 = await UpdateSpectacolAsync(URL_Base, spectacolDTO2);
			Console.WriteLine("Am primit {0}", result2);
			//Delete a spectacol
			/*String id2 = "9";
			Console.WriteLine("Delete spectacol with id {0}", id2);
			await DeleteSpectacolAsync(URL_Base + "/" + id2);*/
			
			Console.ReadLine();
		}

		private static async Task<SpectacolDTO> GetSpectacolAsync(string urlBase)
		{
			SpectacolDTO spectacol = null;
			HttpResponseMessage response = await client.GetAsync(urlBase);
			if (response.IsSuccessStatusCode)
			{
				spectacol = await response.Content.ReadFromJsonAsync<SpectacolDTO>();
			}
			return spectacol;
		}

		private static async Task DeleteSpectacolAsync(string urlBase)
		{
			HttpResponseMessage response = await client.DeleteAsync(urlBase);
			if (response.IsSuccessStatusCode)
			{
				Console.WriteLine("Spectacolul a fost sters cu succes");
			}
			else
			{
				Console.WriteLine("Spectacolul nu a fost sters");
			}
		}

		private static async Task<SpectacolDTO> UpdateSpectacolAsync(string urlBase, SpectacolDTO spectacolDto2)
		{
			HttpResponseMessage response = await client.PutAsJsonAsync(urlBase, spectacolDto2);
			if (response.IsSuccessStatusCode)
			{
				SpectacolDTO spectacol = await response.Content.ReadFromJsonAsync<SpectacolDTO>();
				return spectacol;
			}
			return null;
		}

		private static async Task<SpectacolDTO> CreateSpectacolAsync(string urlBase, SpectacolDTO spectacolDto)
		{
			HttpResponseMessage response = await client.PostAsJsonAsync(urlBase, spectacolDto);
			if (response.IsSuccessStatusCode)
			{
				SpectacolDTO spectacol = await response.Content.ReadFromJsonAsync<SpectacolDTO>();
				return spectacol;
			}
			return null;
		}

		private static async Task<string> GetAllSpectacoleAsync(string urlBase)
		{
			
			HttpResponseMessage response = await client.GetAsync(urlBase);
			if (response.IsSuccessStatusCode)
			{
				string spectacole = await response.Content.ReadAsStringAsync();
				Console.WriteLine("Am primit {0}", spectacole);
				return spectacole;
			}
			return null;
		}
		
		
	}
	
	
		public class SpectacolDTO
		{
			[JsonProperty("data")]
			public string Data { get; set; }

			[JsonProperty("locatie")]
			public string Locatie { get; set; }

			[JsonProperty("nrLocuriDisponibile")]
			public int NrLocuriDisponibile { get; set; }

			[JsonProperty("nrLocuriVandute")]
			public int NrLocuriVandute { get; set; }

			[JsonProperty("artist")]
			public string Artist { get; set; }

			[JsonProperty("id")]
			public long? Id { get; set; }

			public SpectacolDTO() { }

			public SpectacolDTO(string data, string locatie, int nrLocuriDisponibile, int nrLocuriVandute, string artist, long? id)
			{
				Data = data;
				Locatie = locatie;
				NrLocuriDisponibile = nrLocuriDisponibile;
				NrLocuriVandute = nrLocuriVandute;
				Artist = artist;
				Id = id;
			}

			public override string ToString()
			{
				return string.Format("[SpectacolDTO: Data={0}, Locatie={1}, NrLocuriDisponibile={2}, NrLocuriVandute={3}, Artist={4}, Id={5}]",
					Data, Locatie, NrLocuriDisponibile, NrLocuriVandute, Artist, Id);
			}

			public override bool Equals(object obj)
			{
				if (this == obj) return true;
				if (!(obj is SpectacolDTO that)) return false;
				return NrLocuriDisponibile == that.NrLocuriDisponibile && NrLocuriVandute == that.NrLocuriVandute &&
				       string.Equals(Data, that.Data) && string.Equals(Locatie, that.Locatie) &&
				       string.Equals(Artist, that.Artist) && Id == that.Id;
			}

			
		}
	

	
	public class LoggingHandler : DelegatingHandler
    {
        public LoggingHandler(HttpMessageHandler innerHandler)
            : base(innerHandler)
        {
        }
    
        protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            Console.WriteLine("Request:");
            Console.WriteLine(request.ToString());
            if (request.Content != null)
            {
                Console.WriteLine(await request.Content.ReadAsStringAsync());
            }
            Console.WriteLine();
    
            HttpResponseMessage response = await base.SendAsync(request, cancellationToken);
    
            Console.WriteLine("Response:");
            Console.WriteLine(response.ToString());
            if (response.Content != null)
            {
                Console.WriteLine(await response.Content.ReadAsStringAsync());
            }
            Console.WriteLine();
    
            return response;
        }
    }
	
}
