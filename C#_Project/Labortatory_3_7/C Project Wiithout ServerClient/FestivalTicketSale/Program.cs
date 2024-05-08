using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;
using FestivalTicketSale.Model;
using FestivalTicketSale.Repository.DBRepositories;
using FestivalTicketSale.Repository.Interfaces;
using FestivalTicketSale.Service;
using log4net.Config;

namespace FestivalTicketSale
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        ///
        static string GetConnectionStringByName(string name)
        {
            // Assume failure.
            string returnValue = null;

            // Look for the name in the connectionStrings section.
            ConnectionStringSettings settings =ConfigurationManager.ConnectionStrings[name];

            // If found, return the connection string.
            if (settings != null)
                returnValue = settings.ConnectionString;

            return returnValue;
        }
        private static readonly log4net.ILog logger = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        [STAThread]
        static void Main()
        {
            string configFilePath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "log4net.config");
            FileInfo fileInfo;
            
            
            try
            {
                fileInfo = new System.IO.FileInfo(configFilePath);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                throw;
            }
           
           // XmlConfigurator.Configure(fileInfo);
           XmlConfigurator.Configure(new FileInfo("log4net.config"));
            Console.WriteLine("Configuration Settings for tasksDB {0}",GetConnectionStringByName("ticket_sales"));
            
            IDictionary<String, string> props = new SortedList<String, String>();
            props.Add("ConnectionString", GetConnectionStringByName("ticket_sales"));
            Console.WriteLine("Sorting Tasks Repository DB ...");
            IAngajatRepository angajatRepository = new DBAngajatRepository(props);
            IBiletRepository biletRepository = new DBBiletRepository(props);
            ISpectacolRepository spectacolRepository = new DBSpectacolRepository(props);
            IFestivalService service = new FestivalService(angajatRepository, biletRepository, spectacolRepository);
            
            
           
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new LogInForm(service));
        }
    }
}