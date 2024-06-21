import React from 'react';
import './CSS/Shows.css';


const ShowForm = ({ formData = {}, handleFormChange, handleFormSubmit, isEditing, handleCancel }) => {
    return (
        <div className="formContainer">
            <form className="formCustomization" onSubmit={handleFormSubmit}>
                <label>
                    Location:
                    <input type="text" name="locatie" value={formData.locatie || ''} onChange={handleFormChange} required/>
                </label><br />
                <label>
                    Date:
                    <input type="date" name="date" value={formData.date || ''} onChange={handleFormChange} required/>
                </label><br />
                <label>
                    Time:
                    <input type="time" name="time" value={formData.time || ''} onChange={handleFormChange}required />
                </label><br />
                <label>
                    Number of Available Tickets:
                    <input type="number" name="nrLocuriDisponibile" value={formData.nrLocuriDisponibile || ''} onChange={handleFormChange} required />
                </label><br />
                <label>
                    Number of Sold Tickets:
                    <input type="number" name="nrLocuriVandute" value={formData.nrLocuriVandute || ''} onChange={handleFormChange} required/>
                </label><br />
                <label>
                    Artist:
                    <input type="text" name="artist" value={formData.artist || ''} onChange={handleFormChange} required/>
                </label><br />
                <button type="submit" className="submit-button" disabled={!Object.keys(formData).some(key => formData[key] !== '')}>{isEditing ? 'Update' : 'Add'} </button>
                {isEditing && <button type="button" className="cancel-button" onClick={handleCancel}>Cancel</button>}
            </form>
        </div>
    );
};

export default ShowForm;