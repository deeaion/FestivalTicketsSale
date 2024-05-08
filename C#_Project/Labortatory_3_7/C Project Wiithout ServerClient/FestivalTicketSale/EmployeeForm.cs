using System;
using System.Collections.Generic;
using System.Drawing;
using System.Globalization;
using System.Windows.Forms;
using FestivalTicketSale.Model;
using FestivalTicketSale.Service;

namespace FestivalTicketSale;

public partial class EmployeeForm : Form
{
    IFestivalService _service;
    public EmployeeForm(IFestivalService service)
    {
        this._service=service;
        InitializeComponent();
        initItems();
        
      
    }

    private void initItems()
    {
        setArtistCombo();
        setTableMain(null,null);
        setTableSecond(null,null);
       
    }

    private void setArtistCombo()
    {
        List<String> artists = (List<string>)_service.GetArtisti();
        artists.Add("None");
        comboArtist.Items.Clear();
        comboArtist.Items.AddRange(artists.ToArray());
        comboArtist.SelectedValue = comboArtist.Items[artists.Count - 1];
        comboArtist.Text = "None";
    }

    private void setTableSecond(object sender, EventArgs e)
    {
        List<Spectacol> spectacole = (List<Spectacol>)_service.FilterSpectacole(dateTimeSecond.Value, "None",true);
        dataGridSecond.Rows.Clear();
        foreach (var spectacol in spectacole)
        {
            int rowIndex = dataGridSecond.Rows.Add();  // Add a new row and get its index
            DataGridViewRow row = dataGridSecond.Rows[rowIndex];  // Get the new row
            row.Cells["ArtistColS"].Value = spectacol.Artist;
            row.Cells["DateCol"].Value = spectacol.Data.ToString(CultureInfo.InvariantCulture);
            row.Cells["PlaceCol"].Value = spectacol.Locatie;
            row.Cells["AvailableCol"].Value = spectacol.NumarLocuriDisponibile.ToString();
            row.Tag = spectacol;
            if (spectacol.NumarLocuriDisponibile == 0)
            {
                row.DefaultCellStyle.BackColor = Color.Red;
            }
        }
    }
    private void setTableMain(object sender, EventArgs e)
    {
        string artist = comboArtist.Text;
        List<Spectacol> spectacole = (List<Spectacol>)_service.FilterSpectacole(dateTimePickerMain.Value, artist, checkBoxDate.Checked);
        dataGridMain.Rows.Clear();
        foreach (var spectacol in spectacole)
        {
            int rowIndex = dataGridMain.Rows.Add();  // Add a new row and get its index
            DataGridViewRow row = dataGridMain.Rows[rowIndex];  // Get the new row
            row.Cells["ArtistColMain"].Value = spectacol.Artist;
            row.Cells["DataColMain"].Value = spectacol.Data.ToString(CultureInfo.InvariantCulture);
            row.Cells["PlaceColMain"].Value = spectacol.Locatie;
            row.Cells["AvailableSeatsMain"].Value = spectacol.NumarLocuriDisponibile.ToString();
            row.Cells["SoldSeatsMain"].Value = spectacol.NumarLocuriVandute.ToString();
            row.Tag = spectacol;
            if (spectacol.NumarLocuriDisponibile == 0)
            {
                row.DefaultCellStyle.BackColor = Color.Red;
            }
        }
    }



    private void label11_Click(object sender, EventArgs e)
    {
        
    }

  

    

    private void EmployeeForm_Load(object sender, EventArgs e)
    {
        setTableMain(null,null);
    }

    private void checkBox1_CheckedChanged(object sender, EventArgs e)
    {
        setTableMain(null,null);
    }

    private void setInformationAboutShowMain(object sender, DataGridViewCellEventArgs e)
    {
        DataGridViewRow row=dataGridMain.Rows[dataGridMain.SelectedCells[0].RowIndex];
        if(row!=null)
        {
            artistLblMain.Visible=true;
            dateLblMain.Visible=true;
            placeLblMain.Visible=true;
            label6.Visible=true;
            label9.Visible=true;
            label4.Visible=true;
            string artist = row.Cells["ArtistColMain"].Value.ToString();
            string locatie = row.Cells["PlaceColMain"].Value.ToString();
            string data = row.Cells["DataColMain"].Value.ToString();
            artistLblMain.Text=artist;
            dateLblMain.Text=data;
            placeLblMain.Text=locatie;
        }
    }

    private void btnAddMain_Click(object sender, EventArgs e)
    {
        string nume = nameTxtMain.Text.ToString();
        int nr = Convert.ToInt32(nPeopleMain.Value.ToString());
        DataGridViewRow row = dataGridMain.Rows[dataGridMain.SelectedCells[0].RowIndex];
        Spectacol spectacol = (Spectacol)row.Tag;
        try
        {
            _service.AddBilet(nume, spectacol, nr);
        }
        catch (Exception exception)
        {
           MessageBox.Show(exception.Message);
        }
        initItems();
    }

    private void sellTicketSecond_Click(object sender, EventArgs e)
    {
        string nume = nameTxt.Text.ToString();
        int nr = Convert.ToInt32(peopleNr.Value.ToString());
        DataGridViewRow row = dataGridSecond.Rows[dataGridSecond.SelectedCells[0].RowIndex];
        Spectacol spectacol = (Spectacol)row.Tag;
        try
        {
            _service.AddBilet(nume, spectacol, nr);
        }
        catch (Exception exception)
        {
            MessageBox.Show(exception.Message);
        }
        initItems();
    }

    private void dateTimePickerMain_ValueChanged(object sender, EventArgs e)
    {
        tabView.SelectedTab = tabPage2;
        dateTimeSecond.Value=dateTimePickerMain.Value;
        initItems();
    }

    private void dateTimeSecond_ValueChanged(object sender, EventArgs e)
    {
        setTableSecond(null,null);
    }

    private void logOutMain_Click(object sender, EventArgs e)
    {
        this.Close();
    }
}