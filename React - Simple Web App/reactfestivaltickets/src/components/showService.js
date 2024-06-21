const apiEndpoint = 'http://localhost:55555/spectacol-management/spectacole';

export const fetchShows = async () =>
{
    try {
        const response = await fetch(apiEndpoint);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching shows:', error);
        throw error;
    }
};

export const updateShow = async (show) => {
    try {
        const response = await fetch(apiEndpoint, { // Append show ID to the endpoint
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(show)
        });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error updating show:', error);
        throw error;
    }
};

export const deleteShow = async (id) => {
    try {
        const response = await fetch(`${apiEndpoint}/${id}`, { // Correct template literal usage
            method: 'DELETE'
        });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
    } catch (error) {
        console.error('Error deleting show:', error);
        throw error;
    }
};

export const addShowUnit =async (show) => {
    try {
        show.nrLocuriVandute=show.nrLocuriVandute+1;
        show.nrLocuriDisponibile=show.nrLocuriDisponibile-1;

        const response = await fetch(apiEndpoint, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(show)
        });
        if (response.ok) {
            const data = await response.json();
            return data;
        } else {
            throw new Error('Network response was not ok');
        }
    } catch (error) {
        console.error('Error adding show:', error);
        throw error;
    }

}

export const addShow = async (show) => {
    try {
        const response = await fetch(apiEndpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(show)
        });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error adding show:', error);
        throw error;
    }
}