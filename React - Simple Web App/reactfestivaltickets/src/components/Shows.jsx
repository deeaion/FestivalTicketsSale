import React, { useState, useEffect } from 'react';
import ShowForm from './ShowForm';
import { fetchShows, updateShow, deleteShow, addShowUnit, addShow } from './showService';
import './CSS/Shows.css';
import ShowInfo from "./ShowInformation.jsx";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEdit, faTrash, faPlus } from "@fortawesome/free-solid-svg-icons";



const Shows = () => {
    const [shows, setShows] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isEditing, setIsEditing] = useState(false);
    const [currentShow, setCurrentShow] = useState(null);
    const [selectedShow, setSelectedShow] = useState(null);
    const [formData, setFormData] = useState({
        locatie: '',
        date: '',
        time: '',
        nrLocuriDisponibile: '',
        nrLocuriVandute: '',
        artist: ''
    });

    const getShows = async () => {
        try {
            const data = await fetchShows();
            setShows(data);
            setLoading(false);
        } catch (error) {
            setError(error.message);
            setLoading(false);
        }
    };

    useEffect(() => {
        getShows();
    }, []);

    const handleEdit = (show) => {
        const [date, time] = show.data.split('T');
        setCurrentShow(show);
        setFormData({
            locatie: show.locatie || '',
            date: date || '',
            time: time || '',
            nrLocuriDisponibile: show.nrLocuriDisponibile || '',
            nrLocuriVandute: show.nrLocuriVandute || '',
            artist: show.artist || ''
        });
        setIsEditing(true);
    };

    const handleShowClick = (show) => {
        setSelectedShow(show);
    };

    const handleDelete = async (id) => {
        console.log(id);
        await deleteShow(id);
        setShows(shows.filter(show => show.id !== id));
        getShows(); // Reload the list of shows
    };

    const handleAdd = async (show) => {
        console.log(show.id);
        const updatedShow = await addShowUnit(show);
        setShows(shows.map(s => s.id === show.id ? updatedShow : s));
    };

    const handleFormChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleFormSubmit = async (e) => {
        e.preventDefault();
        //show
        const updatedShow = {
            locatie: formData.locatie,
            data: `${formData.date}T${formData.time}`,
            nrLocuriDisponibile: formData.nrLocuriDisponibile,
            nrLocuriVandute: formData.nrLocuriVandute,
            artist: formData.artist,
            id: currentShow ? currentShow.id : null
        };
        if (isEditing) {
            await updateShow(updatedShow);
            setIsEditing(false);
            setCurrentShow(null);
        } else {
            await addShow(updatedShow);
        }
        setFormData({
            locatie: '',
            date: '',
            time: '',
            nrLocuriDisponibile: '',
            nrLocuriVandute: '',
            artist: ''
        });
        getShows(); // Reload the list of shows
    };

    const handleCancel = () => {
        setIsEditing(false);
        setCurrentShow(null);
        setFormData({
            locatie: '',
            date: '',
            time: '',
            nrLocuriDisponibile: '',
            nrLocuriVandute: '',
            artist: ''
        });
    };

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div className="">
            <h1>Shows</h1>
            <div>
                <ShowForm
                    formData={formData || {}}
                    handleFormChange={handleFormChange}
                    handleFormSubmit={handleFormSubmit}
                    isEditing={isEditing}
                    handleCancel={handleCancel}
                />
            </div>
            {selectedShow && <div className="show-information"><ShowInfo event={selectedShow} /></div>}
            <table>
                <thead>
                    <tr >
                        <th>Location</th>
                        <th>Date</th>
                        <th>Available Tickets</th>
                        <th>Sold Tickets</th>
                        <th>Artist</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                {shows.map((show, index) => (
                    <tr key={show.id} onClick={() => handleShowClick(show)}>
                        <td>{show.locatie}</td>
                        <td>{show.data.split('T')[0]}</td>
                        <td>{show.nrLocuriDisponibile}</td>
                        <td>{show.nrLocuriVandute}</td>
                        <td>{show.artist}</td>
                        <td>
                            <button onClick={() => handleEdit(show)} >
                                <FontAwesomeIcon icon={faEdit} /></button>
                            <button onClick={() => handleDelete(show.id)}> <FontAwesomeIcon icon={faTrash} /></button>
                            <button onClick={() => handleAdd(show)}><FontAwesomeIcon icon={faPlus}/> </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default Shows;
