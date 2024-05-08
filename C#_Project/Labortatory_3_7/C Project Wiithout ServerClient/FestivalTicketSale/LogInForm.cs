using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using FestivalTicketSale.Service;

namespace FestivalTicketSale
{
    public partial class LogInForm : Form
    {
        private IFestivalService _service;
        public LogInForm(IFestivalService service)
        {
            this._service = service;
            InitializeComponent();
        }
        

        private void logIn_Click(object sender, EventArgs e)
        {
           string username=emailTxt.Text;
           string password=passwordTxt.Text;
           if (_service.VerifyLogInInformation(username, password))
           {
               
                EmployeeForm mainForm = new EmployeeForm(_service);
                
                mainForm.Show();
           }
              else
              {
                MessageBox.Show("Invalid username or password");
              }
        }
        

        private void LogInForm_Load(object sender, EventArgs e)
        {
            
        }
    }
}